package ticket.com.server.server.model;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ticket.com.server.server.CommandsManager;
import ticket.com.server.server.DB.DatabaseManager;
import ticket.com.utility.model.Chat;
import ticket.com.utility.model.DestinationCard;
import ticket.com.utility.model.Game;
import ticket.com.utility.model.Player;
import ticket.com.utility.model.PlayerAction;
import ticket.com.utility.model.TrainCard;
import ticket.com.utility.model.User;
import ticket.com.utility.web.Command;

public class ServerModel {
    private static final String JOIN_SERVICE_PATH = "ticket.com.tickettoridegames.client.service.JoinService";
    private static final String LOBBY_SERVICE_PATH = "ticket.com.tickettoridegames.client.service.LobbyService";

    private static ServerModel instance = null;
    public static ServerModel getInstance(){
        if(instance == null){
            instance = new ServerModel();
            instance.initWithDb();
        }
        return instance;
    }

    private void initWithDb() {
        try {
            setRegisteredUsers(DatabaseManager.getInstance().getAllUsers());
            setGames(DatabaseManager.getInstance().getAllGames());
            executeCommands(DatabaseManager.getInstance().getAllCommands());
        } catch (Exception e) {
            System.out.println("ERROR: Server failed to initialize from db");
            e.printStackTrace();
            return;
        }
        System.out.println("Server was successfully initialized from db");
    }

    private void executeCommands(List<Command> commands) {
        for(Command each : commands) {
            try {
                each.execute();
            } catch(Exception e) {
                System.out.println("ERROR: Server failed to execute command");
                e.printStackTrace();
            }
        }
    }

    //Map of users that stores the Username as the key
    private Map<String, User> registeredUsers;
    //Map of games that stores the GameId ast he key
    private Map<String, Game> games; //key is gameId
    //Map of users that stores the UserID as the kay
    private Map<String, User> activeUsers; //key is userId

    private ServerModel(){
        registeredUsers = new HashMap<>();
        games = new HashMap<>();
        activeUsers = new HashMap<>();
    }

    public void clear(){
        instance = new ServerModel();
    }

    public void addNewUser(User user) throws Exception {
        if(registeredUsers.containsKey(user.getUsername())){
            throw new Exception();
        }
        else{
            registeredUsers.put(user.getUsername(), user);
            activeUsers.put(user.getId(), user);
            System.out.println("User: "+user.getId()+" registered ");
            for(String gameId : games.keySet()){
                Command command;
                Game currentGame = games.get(gameId);
                try{
                    command = new Command(JOIN_SERVICE_PATH,
                            null,
                            "addGame",
                            new Object[]{currentGame});
                }
                catch(Exception e){
                    command = null;
                }
                CommandsManager.addCommand(command,user.getId());
            }
        }
    }

    public String loginUser(String username, String password) throws Exception{
        User user = getUserByName(username);
        if(user == null){
            throw new Exception();
        }
        else{
            if(user.getPassword().equals(password)){
                if(!activeUsers.containsKey(user.getId())){
                    activeUsers.put(user.getId(), user);
                    System.out.println("User: " + user.getId() + " Logged in");
                }
                for(String gameId : games.keySet()){
                    Command command;
                    Game currentGame = games.get(gameId);
                    try{
                        command = new Command(JOIN_SERVICE_PATH,
                                null,
                                "addGame",
                                new Object[]{currentGame});
                    }
                    catch(Exception e){
                        command = null;
                    }
                    CommandsManager.addCommand(command,user.getId());
                }
                return user.getId();
            }
            else{
                return null;
            }
        }
    }

    public void addNewGame(Game game) throws Exception{
        if(games.containsKey(game.getId())){
            throw new Exception("Game already exists.");
        }
        else{
            games.put(game.getId(), game);
            System.out.println("Game with id: " + game.getId() + " created "+game.toString());
            //send commands to other connected Users
            for(String id : activeUsers.keySet()){
                Command command;
                try{
                    command = new Command(JOIN_SERVICE_PATH,
                            null,
                            "addGame",
                            new Object[]{game});
                }
                catch(Exception e){
                    command = null;
                }
                CommandsManager.addCommand(command, id);
            }
            //addPlayerToGame(userId, game.getId());
        }
    }

    private User getUserByName(String username) {
        return registeredUsers.get(username);
    }

    private User getUserById(String id){
        return activeUsers.get(id);
    }

    public boolean addPlayerToGame(String userId, String gameId) throws Exception{
        User user = getUserById(userId);
        Player player = new Player(user.getUsername(),user.getId());
        Game game = games.get(gameId);
        if(game == null){
            throw new Exception("Game not found with id "+gameId);
        }
        else{
            boolean addSuccess = game.addPlayers(player);

            //update database
            Command gCommand = new Command(Game.class.getName(), null, "addPlayers", new Object[]{player});
            Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
            DatabaseManager.getInstance().addCommand(dbCommand, gameId);

            if(addSuccess){
                System.out.println("User: " + player.getId() + " added to game: " + gameId);
                for(String id : activeUsers.keySet()){
                    Command command;
                    try{
                        command = new Command(JOIN_SERVICE_PATH,
                                null,
                                "addPlayer",
                                new Object[]{game.getId(), player});
                    }
                    catch (Exception e){
                        command = null;
                    }
                    CommandsManager.addCommand(command,id);
                }
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean removePlayerFromGame(String gameId, String playerId) throws Exception{
        Game game = games.get(gameId);
        if(game == null){
            throw new Exception("Game not found with id "+gameId);
        }
        else{
            boolean removeSuccess = game.removePlayer(playerId);
            //update database
            Command gCommand = new Command(Game.class.getName(), null, "removePlayer", new Object[]{playerId});
            Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
            DatabaseManager.getInstance().addCommand(dbCommand, gameId);

            if(removeSuccess){
                System.out.println("User: " + playerId + " removed from game: " + gameId);
                for(String id : activeUsers.keySet()){
                    Command command;
                    try{
                        command = new Command(LOBBY_SERVICE_PATH,
                                null,
                                "removePlayer",
                                new Object[]{gameId, playerId});
                    }
                    catch (Exception e){
                        command = null;
                    }
                    CommandsManager.addCommand(command,id);
                }
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void addChatToGame(String gameId, String playerId, String message){
        Game game = games.get(gameId);
        User player = activeUsers.get(playerId);
        Chat chat = new Chat(player.getUsername(), message);
        game.addToChat(chat);

        //update database
        Command gCommand = new Command(Game.class.getName(), null, "addToChat", new Object[]{chat});
        Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
        DatabaseManager.getInstance().addCommand(dbCommand, gameId);

        System.out.println("User: " + playerId + " added chat to game: " + gameId);
        //send commands to all the users in the game.
        for(String id : game.getPlayersId()){
            Command command;
            try {
                command = new Command(LOBBY_SERVICE_PATH,
                        null,
                        "updateChat",
                        new Object[]{game.getId(), chat});
            }
            catch(Exception e){
                command = null;
                //do some kind of error notification. Error command?
            }
            CommandsManager.addCommand(command,id);
        }
    }

    public boolean startGame(String gameId) throws Exception {
        Game game = games.get(gameId);
        if(game == null){
            throw new Exception("Null game passed into start game");
        }
        if(game.getNumberOfPlayers() == game.getMaxPlayers()){
            game.setStarted(true);

            //update database
            Command gCommand = new Command(Game.class.getName(), null, "setStarted", new Object[]{true});
            Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
            DatabaseManager.getInstance().addCommand(dbCommand, gameId);

            for(String playerId : game.getPlayersId()){
                Command command;
                try{
                    command = new Command(
                            LOBBY_SERVICE_PATH,
                            null,
                            "startingGame",
                            new Object[]{game.getId()});
                }
                catch (Exception e){
                    command = null;
                }
                CommandsManager.addCommand(command, playerId);
            }
            return true;
        }
        return false;
    }
    //Destination Card Functions
    public List<DestinationCard> drawTemporaryDestinationCards(String gameId) {
        return games.get(gameId).drawDestinationCards();
    }

    public void claimDestinationCards(String playerId, String gameId, LinkedList<DestinationCard> cards){
        Game game = games.get(gameId);
        game.claimDestinationCards(cards, playerId);

        //update database
        Class<?> listType = new TypeToken<LinkedList<TrainCard>>(){}.getRawType();
        Command gCommand = new Command(Game.class.getName(), null, null, "claimDestinationCards", new Class<?>[]{listType}, new Object[]{cards, playerId});
        Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
        DatabaseManager.getInstance().addCommand(dbCommand, gameId);
    }

    public void addDestinationCard(String gameId, LinkedList<DestinationCard> cards) {
        games.get(gameId).discardDestinationCards(cards);

        //update database
        Class<?> listType = new TypeToken<LinkedList<TrainCard>>(){}.getRawType();
        Command gCommand = new Command(Game.class.getName(), null, null, "discardDestinationCards", new Class<?>[]{listType}, new Object[]{cards});
        Command dbCommand = new Command(ServerModel.class.getName(), null, "execOnGame", new Object[]{gameId, gCommand});
        DatabaseManager.getInstance().addCommand(dbCommand, gameId);
    }
    //End Destination Card Functions

    //Game History Function
//    public void addToGameHistory(String gameId, PlayerAction pa){
//        Game game = games.get(gameId);
//        game.addToHistory(pa);
//    }

    public Map<String, Game> getGames() {
        return games;
    }

    public Game getGameById(String gameId){
        return games.get(gameId);
    }

    public void endGame(String gameId) {
        getGames().get(gameId).setGameOver(true);
    }

    public static boolean execOnGame(String gameId, Command command) {
        Game game = getInstance().getGames().get(gameId);
        if (game == null) {
            System.out.println("ERROR: Couldn't find game for " + gameId);
            return false;
        }

        command.setInstance(game);
        command.setInstanceType(Game.class);

        try {
            command.execute();
            return true;
        } catch(Exception e) {
            System.out.println("ERROR: command failed to execute");
            return false;
        }
    }

    private void setRegisteredUsers(List<User> registeredUsers) {
        for(User each : registeredUsers) {
            this.registeredUsers.put(each.getUsername(), each);
        }
    }

    private void setGames(List<Game> games) {
        for(Game each : games) {
            this.games.put(each.getId(), each);
        }
    }
}

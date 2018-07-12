package ticket.com.tickettoridegames.client.model;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import ticket.com.tickettoridegames.utility.model.Chat;
import ticket.com.tickettoridegames.utility.model.Game;
import ticket.com.tickettoridegames.utility.model.Player;
import ticket.com.tickettoridegames.utility.model.User;

public class ClientModel extends Observable {

    // Singleton Class
    static private ClientModel _instance;

    static public ClientModel get_instance() {
        if (_instance == null) {
            _instance = new ClientModel();
        }
        return _instance;
    }

    static private User currentUser = null;
    static private Map<String, Game> gameList = null;

    private ClientModel() { }

    public void setUser(User user){
        currentUser = user;
        // notify the login presenter
        setChanged();
        notifyObservers();
    }

    public String getUserId(){
        return currentUser.getId();
    }

    public User getUser(){
        return currentUser;
    }

    public void setGames(Map<String, Game> games){
        gameList = games;
        // notify lobby presenter
        setChanged();
        notifyObservers();
    }

    public Map<String, Game> getGames(){
        return gameList;
    }

    public boolean addGameToList(Game game){
        if (gameList.containsValue(game)){
            // Game is already in the list
            return false;
        }
        gameList.put(game.getId(), game);
        // notify lobby presenter
        setChanged();
        notifyObservers();
        return true;
    }

    public List<Chat> getGameChat(String gameID){
        Game game = gameList.get(gameID);
        return game.getChatList();
    }

    public Set<String> getGamePlayers(String gameID){
        Game game = gameList.get(gameID);
        return game.getPlayers();
    }
}

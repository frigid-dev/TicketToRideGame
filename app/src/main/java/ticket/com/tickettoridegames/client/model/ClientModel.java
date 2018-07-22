package ticket.com.tickettoridegames.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import ticket.com.tickettoridegames.utility.model.Chat;
import ticket.com.tickettoridegames.utility.model.DestinationCard;
import ticket.com.tickettoridegames.utility.model.Game;
import ticket.com.tickettoridegames.utility.model.Player;
import ticket.com.tickettoridegames.utility.model.PlayerAction;
import ticket.com.tickettoridegames.utility.model.PlayerStats;
import ticket.com.tickettoridegames.utility.model.Route;
import ticket.com.tickettoridegames.utility.model.TrainCard;
import ticket.com.tickettoridegames.utility.model.User;
import static ticket.com.tickettoridegames.utility.TYPE.NEWCHAT;
import static ticket.com.tickettoridegames.utility.TYPE.NEWROUTE;
import static ticket.com.tickettoridegames.utility.TYPE.NEWTRAINCARD;
import static ticket.com.tickettoridegames.utility.TYPE.START;

public class ClientModel extends Observable {

    // Singleton Class
    static private ClientModel _instance;

    static public ClientModel get_instance() {
        if (_instance == null) {
            _instance = new ClientModel();
        }
        return _instance;
    }

    static private User currentUser;
    static private Map<String, Game> gameList;

    private Game myActiveGame = null;
    private Player myPlayer = null;

    private ClientModel() {
        if(gameList == null) {
            gameList = new HashMap<>();
        }
    }

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

    public void updateGame(Game game){
        gameList.put(game.getId(), game);
        setChanged();
        notifyObservers();
    }

    public List<Chat> getGameChat(String gameID){
        Game game = gameList.get(gameID);
        return game.getChatList();
    }

    public Chat getNewestChat(String gameID){
        Game game = gameList.get(gameID);
        return game.getNewestChat();
    }

    public void addGameChat(String gameID, Chat chat){
        Game game = gameList.get(gameID);
        game.addToChat(chat);
        setChanged();
        notifyObservers(NEWCHAT);
    }

    public String getCurrentGameID(){
        return currentUser.getGameId();
    }

    public Set<String> getGamePlayersName(String gameID){
        Game game = gameList.get(gameID);
        return game.getPlayerNames();
    }

    public void addPlayerToGame(String gameID, Player player){
        Game game = gameList.get(gameID);
        game.addPlayers(player);
        //gameList.put(gameID, game);
        setChanged();
        notifyObservers();
    }

    public void removePlayerFromGame(String gameID, Player player){
        Game game = gameList.get(gameID);
        game.removePlayer(player);
        //gameList.put(gameID, game);
        setChanged();
        notifyObservers();
    }

    public void startGame(String gameId){
        Game game = gameList.get(gameId);
        if (game == null){
            game = new Game();
            game.setId(gameId);
            addGameToList(game);
        }
        game.setStarted(true);
        setChanged();
        notifyObservers(START);
    }

    public boolean isGameStarted(String gameId) {
        Game game = gameList.get(gameId);
        return game != null && game.isStarted();
    }

    public List<TrainCard> getMyHand() {
        return getMyPlayer().getTrainCards();
    }

    public void addTrainCard(TrainCard drawnCard, String playerId) {
        getMyActiveGame().getPlayer(playerId).addTrainCard(drawnCard);
        myNotify(NEWTRAINCARD);
    }

    public Map<String, Integer> getPoints() {
        return getMyActiveGame().getCountsOfPoints();
    }

    public Boolean isMyTurn() {
        return true; //no turns in phase 2
    }

    public void addDestinationCard(DestinationCard card, String playerId) {
        getMyActiveGame().getPlayer(playerId).addDestinationCard(card);
    }

    public Set<DestinationCard> getDestinationCards() {
        return getMyPlayer().getDestinationCards();
    }

    public void setTurnOrder(LinkedList<String> order) {
        getMyActiveGame().setTurnOrder(order);
    }

    public void addDestinationCard(List<DestinationCard> cards) {
        getMyActiveGame().discardDestinationCards(cards);
    }

    public void setPlayersColors(HashMap<String,Player.COLOR> colors) {
        getMyActiveGame().setPlayersColors(colors);
    }

    public Map<String, Integer> getCountsOfPoints() {
        return getMyActiveGame().getCountsOfPoints();
    }

    public Map<String, Integer> getCountsOfTrains() {
        return getMyActiveGame().getCountsOfTrains();
    }

    public Map<String, Integer> getCountsOfCards() {
        return getMyActiveGame().getCountsOfCardsInHand();
    }

    public Map<String, Integer> getCountsOfRoutes() {
        return getMyActiveGame().getCountsOfRoutes();
    }

    public Game getMyActiveGame() {
        if(myActiveGame == null) { locateMyActiveGame(); }
        return myActiveGame;
    }

    private void locateMyActiveGame() {
        //check every player in every game
        for (String curKey : gameList.keySet()) {
            Game curGame = gameList.get(curKey);
            if (curGame.getPlayer(ClientModel.get_instance().getUserId()) != null) {
                myActiveGame = curGame;
                break; //done
            }
        }
    }

    public Player getMyPlayer() {
        if(myPlayer != null) { return myPlayer; } //convenience function
        return getMyActiveGame().getPlayer(ClientModel.get_instance().getUserId());
    }

    public List<PlayerStats> getPlayerStats(){
        List<PlayerStats> stats = new ArrayList<>();
        Game myGame = getMyActiveGame();

        for (Player player : myGame.getPlayers().values()){
            PlayerStats newStat = new PlayerStats();

            newStat.setName(player.getUsername());
            newStat.setNumberOfCards(player.getCardCount());
            newStat.setNumberOfRoutes(player.getRouteCount());
            newStat.setPoints(player.getPoints());

            stats.add(newStat);
        }

        return stats;
    }

    public List<PlayerAction> getHistory(){
        Game myGame = getMyActiveGame();
        return myGame.getGameHistory();
    }

    public void drawTrainCard(String playerId) {
        getMyActiveGame().drawTrainCard(playerId);
    }

    public void pickupTrainCard(String playerId, Integer index) {
        getMyActiveGame().pickupTrainCard(playerId, index);
    }
    public void setMyPlayerTempDeck(List<DestinationCard> deck){
        Player player = getMyPlayer();
        player.setTempDeck(deck);
    }

    public Boolean claimRoute(String playerId, Route route) {
        return getMyActiveGame().claimRoute(playerId, route);
    }

    public void initGameNonRandom() {
        getMyActiveGame().initGameNonRandom();
    }

    private void myNotify(Object arg) {
        setChanged();
        if(arg != null) { notifyObservers(arg); }
        else { notifyObservers(); }
//        clearChanged();
    }
}

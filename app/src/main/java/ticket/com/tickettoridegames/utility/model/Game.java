package ticket.com.tickettoridegames.utility.model;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Game {

    //General game data
    private Map<String, Player> players; //key is playerId
    private List<Chat> chatList;
    private String id;
    private String name;
    private int  maxPlayers;
    private int numberOfPlayers;
    private boolean isStarted;

    // Stores the playerIDs in turn order
    private List<String> turnOrder;
    private Integer turnNumber = 0;

     // Map data
    private GameMap map;
    private Set<DestinationCard> destinationCards;
    private Map<String, DestinationCard> claimedDestinationCards;
    private TrainCard topTrainCard;

    // Stores player actions viewed in the stats history tab
    private List<PlayerAction> gameHistory;

    public Game(){
        this.players = new HashMap<>();
        this.chatList = new ArrayList<>();
        this.turnOrder = new LinkedList<>();
        this.map = map;
        this.destinationCards = new ArraySet<>();
        this.claimedDestinationCards = new HashMap<>();
        this.gameHistory = new LinkedList<>();
    }

    public Game(String name, int numberOfPlayers){
        //collection subject to change
        this.players = new HashMap<>();
        this.chatList = new ArrayList<>();
        this.name = name;
        this.maxPlayers = numberOfPlayers;
        this.numberOfPlayers = 0;
        this.id = UUID.randomUUID().toString();
        this.isStarted = false;
        this.turnOrder = new LinkedList<>();
        this.map = map;
        this.destinationCards = new ArraySet<>();
        this.claimedDestinationCards = new HashMap<>();
        this.gameHistory = new LinkedList<>();

        setupRoutes();
    }

    public void setupRoutes(){
        // this function can set the default route data
    }

    public void initGame() {
        setTurnOrder();

        for(String curKey : players.keySet()) {
            Player curPlayer = players.get(curKey);
            initHand(curPlayer);
        }
    }

    private void initHand(Player player) {
        for(int i = 0; i < 4; i++) {
            player.addTrainCard(removeTopTrainCard());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getPlayersId(){
        return players.keySet();
    }

    public String getPlayerNamesString() {
        String string = "";
        for (String key:players.keySet()) {
            string += players.get(key).getUsername() + " ";
        }
        return string;
    }

    public Set<String> getPlayerNames() {
        Set<String> names = new HashSet<>();
        for (String key:players.keySet()) {
            names.add(players.get(key).getUsername());
        }
        return names;
    }

    public boolean addPlayers(Player p){
        if(numberOfPlayers != maxPlayers) {
            //check if already in game.
            if(players.containsKey(p.getId())){
                //call the join game function
                return true;
            }
            else{
                players.put(p.getId(), p);
                numberOfPlayers++;
                return true;
            }
        }
        else{
            if(players.containsKey(p.getId())){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean removePlayer(Player p){
        if (players.containsValue(p)){
            players.remove(p);
            return true;
        }
        else {
            return false;
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public void addToChat(Chat c){
        chatList.add(c);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public Player getPlayer(String userId) {
        return players.get(userId);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public List<String> getTurnOrder() {
        return turnOrder;
    }

    public GameMap getMap() {
        return map;
    }

    public Set<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    public Map<String, DestinationCard> getClaimedDestinationCards() {
        return claimedDestinationCards;
    }

    public List<PlayerAction> getGameHistory() {
        return gameHistory;
    }

    public Map<String,Integer> getPoints() {
        Map<String,Integer> points = new HashMap<>();

        for(String curKey : players.keySet()) {
            Player curPlayer = players.get(curKey);
            points.put(curPlayer.getId(), curPlayer.getPoints());
        }

        return points;
    }

    public Map<String,Integer> getCountsOfCardsInHand() {
        Map<String,Integer> counts = new HashMap<>();

        for(String curKey : players.keySet()) {
            Player curPlayer = players.get(curKey);
            counts.put(curPlayer.getId(), curPlayer.getTrainCards().size());
        }

        return counts;
    }

    public TrainCard getTopTrainCard() {
        if(topTrainCard == null) { removeTopTrainCard(); } //cycle through it
        return topTrainCard;
    }

    public TrainCard removeTopTrainCard() {
        TrainCard card = topTrainCard;

        //set a new rand card
        Integer randInt = Math.abs(new Random().nextInt() % TrainCard.TRAIN_TYPE.values().length);
        topTrainCard = new TrainCard(TrainCard.TRAIN_TYPE.values()[randInt]);

        if(card == null) { return removeTopTrainCard(); } //cycle through it
        return card;
    }

    public Map<String,Integer> getTrainCounts() {
        Map<String,Integer> counts = new HashMap<>();

        for(String curKey : players.keySet()) {
            Player curPlayer = players.get(curKey);
            counts.put(curPlayer.getId(), curPlayer.getTrains());
        }

        return counts;
    }

    public void switchTurn() {
        turnNumber = (turnNumber + 1) % players.size();
    }

    //player up is the player who's turn it currently is
    public String getIdPlayerUp() {
        return turnOrder.get(turnNumber);
    }

    private void setTurnOrder() {
        for(String curKey : players.keySet()) {
            Player curPlayer = players.get(curKey);
            turnOrder.add(curPlayer.getId());
        }
    }
}

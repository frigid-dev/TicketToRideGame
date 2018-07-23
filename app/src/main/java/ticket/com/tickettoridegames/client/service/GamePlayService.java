package ticket.com.tickettoridegames.client.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ticket.com.tickettoridegames.client.model.ClientModel;
import ticket.com.tickettoridegames.client.web.ServerProxy;
import ticket.com.tickettoridegames.server.service.GameService;
import ticket.com.tickettoridegames.utility.model.Chat;
import ticket.com.tickettoridegames.utility.model.DestinationCard;
import ticket.com.tickettoridegames.utility.model.Player;
import ticket.com.tickettoridegames.utility.model.PlayerAction;
import ticket.com.tickettoridegames.utility.model.Route;
import ticket.com.tickettoridegames.utility.service.IGameService;
import ticket.com.tickettoridegames.utility.web.Command;

public class GamePlayService implements IGameService {
    @Override
    public void initGame(String gameId) {
        try {
//            GameService.class.newInstance().initGame(gameId);
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "initGame", new Object[]{gameId}
            );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawTrainCard(String playerId, String gameId) {
        try {
//            GameService.class.newInstance().drawTrainCard(playerId, gameId);
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "drawTrainCard", new Object[]{playerId, gameId}
                    );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pickupTrainCard(String playerId, String gameId, Integer index) {
        try {
//            GameService.class.newInstance().pickupTrainCard(playerId, gameId, index);
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "pickupTrainCard", new Object[]{playerId, gameId, index}
            );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //Destination Card (Command) commands
    @Override
    public void drawDestinationCard(String playerId, String gameId) {
        try {
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "drawDestinationCard", new Object[]{playerId, gameId}
            );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void claimDestinationCard(String playerId, String gameId, List<DestinationCard> cards){
        try{
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "claimDestinationCard", new Object[]{playerId, gameId, cards});
            ServerProxy.sendCommand(command);
        }
        catch (InstantiationException e){
            e.printStackTrace();
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void returnDestinationCard(String gameId, List<DestinationCard> cards) {
        try {
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "returnDestinationCards", new Object[]{gameId, cards}
            );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    //END Destination Card (Command) functions

    @Override
    public void claimRoute(String gameId, String playerId, Route route) {
        try {
//            GameService.class.newInstance().claimRoute(gameId, playerId, route);
            Command command = new Command(GameService.class, GameService.class.newInstance(),
                    "claimRoute", new Object[]{gameId, playerId, route}
            );
            ServerProxy.sendCommand(command);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTurnOrder(LinkedList<String> order) {
        ClientModel.get_instance().getMyActiveGame().setTurnOrder(order);
    }

    public void setPlayersColors(HashMap<String, Player.COLOR> colors) {
        ClientModel.get_instance().getMyActiveGame().setPlayersColors(colors);
    }

    public void initiatingGameNonRandom() {
        ClientModel.get_instance().getMyActiveGame().initGameNonRandom();
    }

    public void drawingTrainCard(String playerId) {
        ClientModel.get_instance().getMyActiveGame().drawTrainCard(playerId);
    }

    public void pickingUpTrainCard(String playerId, Integer index) {
        ClientModel.get_instance().getMyActiveGame().pickupTrainCard(playerId, index);
    }

    public void claimingRoute(String playerId, Route route) {
        ClientModel.get_instance().getMyActiveGame().claimRoute(playerId, route);
    }

    //Destination Cards (Model) functions
    public void setTempDeck(List<DestinationCard> tempDeck){
        ClientModel.get_instance().setMyPlayerTempDeck(tempDeck);
    }

    public void updateDestinationCards(String playerId, List<DestinationCard> cards){
        ClientModel.get_instance().updateDestinationCards(playerId, cards);
    }

    public void discardDestinationCards(List<DestinationCard> cards){
        ClientModel.get_instance().discardDestinationCards(cards);
    }
    //END Destination Cards (Model) functions
    //Game History functions
    public void addToHistory(PlayerAction history){
        ClientModel.get_instance().addGameHistory(history);
    }
}

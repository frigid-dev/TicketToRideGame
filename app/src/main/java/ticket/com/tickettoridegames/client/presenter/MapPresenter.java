package ticket.com.tickettoridegames.client.presenter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import ticket.com.tickettoridegames.client.model.ClientModel;
import ticket.com.tickettoridegames.client.service.GamePlayService;
import ticket.com.tickettoridegames.client.view.IMapView;
import ticket.com.tickettoridegames.utility.TYPE;
import ticket.com.tickettoridegames.utility.model.City;
import ticket.com.tickettoridegames.utility.model.DestinationCard;
import ticket.com.tickettoridegames.utility.model.Route;
import ticket.com.tickettoridegames.utility.model.TrainCard;

public class MapPresenter implements IMapPresenter, Observer {

    private GamePlayService gamePlayService;
    private ClientModel clientModel;
    private IMapView mapView;

    public MapPresenter(IMapView view) {
        mapView = view;
        gamePlayService = new GamePlayService();
        clientModel = ClientModel.get_instance();
        clientModel.addObserver(this);

        if(clientModel.getMyPlayer().getTempDeck().size() != 0){
            List<DestinationCard> cards = clientModel.getMyPlayer().getTempDeck();
            Set<DestinationCard> destinationCards = new HashSet<>(cards);
            mapView.displayDestinationCards(destinationCards);
            mapView.disableTurn();
        }
    }

    @Override
    public void update(Observable observable, Object arg){
        clientModel = (ClientModel) observable;
        TYPE type = (TYPE) arg;
        switch(type){
            case TURNCHANGED:
                // This will use state in the future
                if (clientModel.isMyTurn()){
                    mapView.enableTurn();
                }
                else {
                    mapView.disableTurn();
                }
                break;
            case NEWTEMPDECK:
                String gameId = clientModel.getMyActiveGame().getId();
                String output = "options: ";

                List<DestinationCard> cards = clientModel.getMyPlayer().getTempDeck();
                Set<DestinationCard> destinationCards = new HashSet<>(cards);
                mapView.displayDestinationCards(destinationCards);

//                for(DestinationCard card : clientModel.getMyPlayer().getTempDeck()) {
//                    output += card.toString() + " ";
//                }
//                output += "\n";
//
//                //return card 0
//                LinkedList<DestinationCard> returnedCards = new LinkedList<>();
//                DestinationCard card0 = clientModel.getMyPlayer().getTempDeck().get(0);
//                returnedCards.add(card0);
//                gamePlayService.returnDestinationCard(gameId, returnedCards);
//                output += "returning: " + card0.to_String()  + "\n";
//
//                //claim cards
//                LinkedList<DestinationCard> claimedCards = new LinkedList<>();
//                DestinationCard card1 = clientModel.getMyPlayer().getTempDeck().get(1);
//                claimedCards.add(card1);
//                DestinationCard card2 = clientModel.getMyPlayer().getTempDeck().get(2);
//                claimedCards.add(card2);
//                gamePlayService.returnDestinationCard(gameId, claimedCards);
//                output += "claiming: " + card1.to_String() + " " + card2.to_String() + "\n";
                break;
            default:
                //Why you updated me?
        }
    }

    @Override
    public void passOff(){
        // Use this function for phase 2 pass off

        // Change turn
        clientModel.changeTurn(clientModel.getMyActiveGame().getId());

        // Change face up deck cards
        mapView.displayMessage("Prev trainCard at index 1: " + clientModel.getMyActiveGame().getTrainBank().get(1).getType());
        clientModel.getMyActiveGame().pickupTrainCard(clientModel.getMyPlayer().getId(), 1);

        // Route claiming.

        // Player Points change

        // Trains remaining change
    }

    @Override
    public void drawTrainCard() {
        gamePlayService.drawTrainCard(clientModel.getUserId(), clientModel.getMyActiveGame().getId());
    }

    @Override
    public void drawDestinationCards(){
        if (clientModel.getMyPlayer().getTempDeck().size() == 0) {
            String gameId = clientModel.getMyActiveGame().getId();
            //get cards
            gamePlayService.drawDestinationCard(clientModel.getUserId(), gameId);
        }
        else {
            mapView.displayMessage("You have already picked cards.");
        }
    }

    @Override
    public void changeTurn(){
        clientModel.changeTurn(clientModel.getMyActiveGame().getId());
        mapView.displayMessage("It's now " + clientModel.getMyActiveGame().playerUpString() + "'s turn");
    }

    @Override
    public void claimRoute(Route route){
        Integer length = 5;
        TrainCard.TRAIN_TYPE type = TrainCard.TRAIN_TYPE.BLACK;
        mapView.displayMessage("Tried to claim route, type: " + type.toString() + " length: " + length
                + " prevTrains: " + clientModel.getMyPlayer().getTrains());

        Route routeStub = new Route(new City("a"), new City("b"), length, type);
        clientModel.getMyActiveGame().claimRoute(clientModel.getMyPlayer().getId(), routeStub);
//        clientModel.getMyActiveGame().claimRoute(clientModel.getMyPlayer().getId(), route);
    }

    @Override
    public void setDestinationCards(LinkedList<DestinationCard> claimedCards, LinkedList<DestinationCard> discardedCards){
        if (claimedCards.size() < 2){
            mapView.displayMessage("Too few routes picked");
            List<DestinationCard> cards = clientModel.getMyPlayer().getTempDeck();
            Set<DestinationCard> newCards = new HashSet<>(cards);
            mapView.displayDestinationCards(newCards);
        }
        else {
            gamePlayService.claimDestinationCard(clientModel.getUserId(), clientModel.getMyActiveGame().getId(), claimedCards);
            gamePlayService.returnDestinationCard(clientModel.getMyActiveGame().getId(), discardedCards);
            mapView.disablePickRoutes();
        }
    }

}

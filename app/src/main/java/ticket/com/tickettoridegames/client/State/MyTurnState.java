package ticket.com.tickettoridegames.client.State;

import ticket.com.tickettoridegames.client.model.ClientModel;
import ticket.com.tickettoridegames.client.presenter.IAssetsPresenter;
import ticket.com.tickettoridegames.client.presenter.IMapPresenter;
import ticket.com.tickettoridegames.client.service.GamePlayService;
import ticket.com.utility.model.Game;
import ticket.com.utility.web.Result;

public class MyTurnState extends PlayerState {

    private static MyTurnState instance = new MyTurnState();
    private GamePlayService gamePlayService;
    public static MyTurnState getInstance(){
        return instance;
    }
    private MyTurnState(){
        gamePlayService = new GamePlayService();
    }

    public void drawTrainCard(ClientModel cm){
        gamePlayService.drawTrainCard(cm.getUserId(), cm.getCurrentGameID());
        cm.setState(DrewOneTrainState.getInstance());
    }

    public void drawDestinationCard(IMapPresenter presenter, ClientModel cm){
        gamePlayService.drawDestinationCard(cm.getUserId(), cm.getCurrentGameID());
    }

    public void changeTurn(ClientModel cm) {
        gamePlayService.switchTurn(cm.getCurrentGameID());
        cm.setState(NotMyTurnState.getInstance()); //todo should be coming from server
    }

    public Result claimRoute(ClientModel cm, String route) {
        return gamePlayService.claimRoute(cm.getMyActiveGame().getId(), cm.getMyPlayer().getId(), route);
    }

    public void drawFromBank(IAssetsPresenter presenter, Integer index) {
        ClientModel clientModel = ClientModel.get_instance();
        Game game = clientModel.getMyActiveGame();

        // check these before modifying the deck to prevent race conditions
        boolean wildCard = game.isBankCardWild(index);

        // send command to server
        gamePlayService.pickupTrainCard(clientModel.getUserId(), clientModel.getMyActiveGame().getId(), index);

        // end turn based on drawing a bank wild
        if (wildCard) {
            gamePlayService.switchTurn(game.getId());
        }
        else {
            clientModel.setState(DrewOneTrainState.getInstance());
        }
    }

}

package ticket.com.tickettoridegames.client.presenter;

import java.util.Observable;
import java.util.Observer;

import ticket.com.tickettoridegames.client.model.ClientModel;
import ticket.com.tickettoridegames.client.service.LobbyService;
import ticket.com.tickettoridegames.client.view.IStatsView;
import ticket.com.tickettoridegames.utility.TYPE;
import ticket.com.tickettoridegames.utility.model.Game;
import ticket.com.tickettoridegames.utility.web.Result;

public class StatsPresenter implements IStatsPresenter , Observer {

    private ClientModel clientModel;
    private IStatsView statsView;

    public StatsPresenter(IStatsView view){
        statsView = view;
        clientModel = ClientModel.get_instance();
//        clientModel.addObserver(this);
        clientModel.getMyActiveGame().addObserver(this);

        statsView.setChat(ClientModel.get_instance().getMyActiveGame().getChatList());
//        statsView.setPlayerStats(ClientModel.get_instance().getMyActiveGame().getPlayerStats()); //recyclerview is null
//        statsView.setHistory(ClientModel.get_instance().getMyActiveGame().getGameHistory()); //history adapter is null
    }

    @Override
    public void update(Observable observable, Object arg){
//        clientModel = (ClientModel) observable;
        Game game = (Game) observable;
        TYPE type = (TYPE) arg;
        //Update the stats, chats and history here.
        switch(type){
            case NEWCHAT:
                statsView.displayChat(game.getNewestChat());
                break;
            case STATSUPDATE:
                statsView.setPlayerStats(game.getPlayerStats());
                break;
            case ALLHISTORY:
                statsView.setHistory(clientModel.getHistory());
            case HISTORYUPDATE:
                statsView.displayHistory(clientModel.getNewestGameHistory());
                break;
            case NEWTRAINCARD:
                statsView.setPlayerStats(game.getPlayerStats());
                break;
            default:
                // We got an update that we don't care about.
                break;
        }
    }

    @Override
    public void sendMessage(String message){
        String userID = clientModel.getUserId();
        String gameID = clientModel.getUser().getGameId();
        if (gameID == null || gameID.equals("")){
            statsView.displayMessage("Invalid game ID, failed to send.");
        }
        else if (userID == null || userID.equals("")){
            statsView.displayMessage("Invalid player ID, failed to send.");
        }
        else if (message == null || message.isEmpty()){
            statsView.displayMessage("Invalid message, failed to send.");
        }
        else {
            Result result = LobbyService.sendChat(gameID,userID,message);
            if (!result.isSuccess()){
                // Error happened address as necessary.
                statsView.displayMessage("Error sending message. ");
            }
        }
    }

}

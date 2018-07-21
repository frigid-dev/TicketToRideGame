package ticket.com.tickettoridegames.utility.service;

import ticket.com.tickettoridegames.utility.model.Chat;
import ticket.com.tickettoridegames.utility.model.DestinationCard;

public interface IGameService {
    void initGame(String gameId);

    //for the deck
    void drawTrainCard(String playerId, String gameId);

    //for face up cards
    void pickupTrainCard(String playerId, String gameId, Integer index);

    void drawDestinationCard(String playerId, String gameId);

    void returnDestinationCard(String gameId, DestinationCard card);
}

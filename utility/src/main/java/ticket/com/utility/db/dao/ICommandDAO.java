package ticket.com.utility.db.dao;

import ticket.com.utility.web.Command;

public interface ICommandDAO {

    Boolean addCommand(Command command);

    //some sort of array or list?
    Command getCommand(String gameId);

    Boolean clearCommands();
}
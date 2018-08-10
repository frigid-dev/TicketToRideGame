package ticket.com.jsondb.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ticket.com.jsondb.JsonDbFactory;
import ticket.com.utility.db.IDbFactory;
import ticket.com.utility.db.dao.ICommandDAO;
import ticket.com.utility.web.Command;

import static org.junit.Assert.assertTrue;

public class JsonCommandDaoTest {
    IDbFactory factory;

    @Before
    public void setup(){
        factory = new JsonDbFactory();
    }

    @After
    public void tearDown(){
        ICommandDAO commandDao = factory.getCommandDAO();
        commandDao.clearCommands();
    }

    @Test
    public void testAddUser(){
        Command comm1 = new Command("ticket.com.tickettoridegame.client.service.GamePlay",
                null, "setTurnOrder", new Object[]{"Hello World"});
        Command comm2 = new Command("ticket.com.tickettoridegame.client.service.stuff", null,
                "finishProject", new Object[]{"More Stuff"});
        ICommandDAO commandDao = factory.getCommandDAO();
        commandDao.addCommand(comm1);
        commandDao.addCommand(comm2);
    }
}

package ticket.com.sqldb.dao;

import java.util.List;

import ticket.com.utility.db.IDbFactory;
import ticket.com.utility.db.dao.IUserDAO;
import ticket.com.utility.model.User;

public class SqlUserDao implements IUserDAO {

    private IDbFactory factory;

    public SqlUserDao(IDbFactory factory){}

    public Boolean addUser(User user){
        return true;
    }

    public User getUser(String username){
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    public Boolean clearUsers(){
        return true;
    }
}

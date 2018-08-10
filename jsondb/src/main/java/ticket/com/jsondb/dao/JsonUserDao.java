package ticket.com.jsondb.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ticket.com.utility.db.dao.IUserDAO;
import ticket.com.utility.model.User;

public class JsonUserDao implements IUserDAO {
    private File file;
    private Gson gson;

    public JsonUserDao(File file){
        this.file = file;
        gson = new Gson();
    }

    public Boolean addUser(User user){
        List<User> users = getCurrentJson();
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
        String json = gson.toJson(users);
        try{
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.flush();
            bw.close();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public User getUser(String username){
        List<User> users = getCurrentJson();
        if(users == null){
            return null;
        }
        else{
            for(User user : users){
                if(user.getUsername().equals(username)){
                    return user;
                }
            }
            return null;
        }
    }

    public void clearUsers(){
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    private List<User> getCurrentJson(){
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
            String currentJson = new String(encoded);
            return gson.fromJson(currentJson, new TypeToken<List<User>>() {}.getType());
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}

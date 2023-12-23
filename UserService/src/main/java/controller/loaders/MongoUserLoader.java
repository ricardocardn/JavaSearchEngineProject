package controller.loaders;

import com.mongodb.client.model.Filters;
import controller.MongoConnection;
import model.User;
import org.bson.Document;

public record MongoUserLoader(MongoConnection datamartConnection) implements UserLoader {
    @Override
    public User getUser(String username, String password) {
        Document user = datamartConnection.collection().find(Filters.eq("username", username)).first();

        if (user != null) {
            String userpass = user.getString("password");

            if (password.equals(userpass)) return new User(username);
            else return new User("");
        } else return null;
    }
}

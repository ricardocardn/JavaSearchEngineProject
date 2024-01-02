package controller.registers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import controller.connections.MongoConnection;
import controller.loaders.MongoUserLoader;
import controller.loaders.UserLoader;
import controller.sessions.SessionHandler;
import controller.sessions.cookie.SessionHazelcastHandler;
import model.User;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class MongoUserRegister implements UserRegister {
    private final SessionHandler sessionHandler;
    private final UserLoader userLoader;
    private final MongoConnection datamartConnection;

    public MongoUserRegister(MongoConnection datamartConnection) {
        this.datamartConnection = datamartConnection;
        userLoader = new MongoUserLoader(datamartConnection);
        sessionHandler = new SessionHazelcastHandler();
    }

    @Override
    public String register(String username, String password) {
        User user = userLoader.getUser(username, password);
        if (exists(user) || badCredentials(user))
            throw new RuntimeException("User already exists");
        else
            user = saveToDatamart(username, password);
            return sessionHandler.getSessionToken(user);
    }

    private boolean badCredentials(User user) {
        return user != null && user.username().equals("");
    }

    private boolean exists(User user) {
        return user != null && !badCredentials(user);
    }

    private User saveToDatamart(String username, String password) {
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Document userDocument = new Document()
                .append("username", username)
                .append("password", hashPassword)
                .append("documents", new ArrayList<>());

        datamartConnection.collection().insertOne(userDocument);
        return new User(username);
    }

    private MongoCollection<Document> getMongoConnection() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("users_database");
        return database.getCollection("users");
    }
}

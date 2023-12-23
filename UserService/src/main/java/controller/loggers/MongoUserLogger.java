package controller.loggers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import controller.MongoConnection;
import controller.loaders.MongoUserLoader;
import controller.loaders.UserLoader;
import controller.sessions.cookie.SessionHazelcastHandler;
import controller.sessions.SessionHandler;
import model.User;
import org.bson.Document;

import java.util.ArrayList;

public class MongoUserLogger implements UserLogger{
    private final SessionHandler sessionHandler;
    private final UserLoader userLoader;
    private final MongoConnection datamartConnection;

    public MongoUserLogger(MongoConnection datamartConnection) {
        this.sessionHandler = new SessionHazelcastHandler();
        this.userLoader = new MongoUserLoader(datamartConnection);
        this.datamartConnection = datamartConnection;
    }

    @Override
    public String logUser(String username, String password) {
        User user = userLoader.getUser(username, password);
        if (exists(user))
            return sessionHandler.getSessionToken(user);
        else if (badCredentials(user))
            return null;

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
        Document userDocument = new Document()
                .append("username", username)
                .append("password", password)
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

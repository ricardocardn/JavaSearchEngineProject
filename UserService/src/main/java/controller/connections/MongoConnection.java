package controller.connections;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {
    private final MongoCollection<Document> usersCollection;

    public MongoConnection() {
        this.usersCollection = getMongoConnection();
    }

    public MongoCollection<Document> collection() {
        return usersCollection;
    }

    private MongoCollection<Document> getMongoConnection() {
        String connectionURL = String.format("mongodb+srv://ricardocardenesp:%s@libookusercluster.abhhtfe.mongodb.net/?retryWrites=true&w=majority",
                                System.getenv("MONGO_ATLAS_PASSWORD"));
        MongoClient mongoClient = MongoClients.create(connectionURL);
        MongoDatabase database = mongoClient.getDatabase("users_database");
        return database.getCollection("users");
    }
}

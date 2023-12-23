package controller;

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
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("users_database");
        return database.getCollection("users");
    }
}

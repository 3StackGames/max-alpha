package com.three_stack.maximum_alpha.backend.game.utilities;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.three_stack.maximum_alpha.backend.Config;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoService {
    private MongoClient client;
    private MongoDatabase database;

    public MongoService() {
        client = getClient();
        database = client.getDatabase("max-alpha");
    }

    public MongoCollection<Document> getCardsCollection() {
        return database.getCollection("cards");
    }

    private MongoClient getClient() {
        String username = Config.getProperty("mongo.auth.username");
        String database = Config.getProperty("mongo.auth.database");
        char[] password = Config.getProperty("mongo.auth.password").toCharArray();

        int port = Integer.parseInt(Config.getProperty("mongo.port"));

        MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password);
        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(mongoCredential);

        ServerAddress address = new ServerAddress(Config.getProperty("mongo.address"), port);
        return new MongoClient(address, credentials);
    }
}

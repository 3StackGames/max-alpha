package com.three_stack.maximum_alpha.backend.game.utilities;

import com.three_stack.maximum_alpha.database_client.DatabaseClient;
import com.three_stack.maximum_alpha.database_client.pojos.DBCard;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class DatabaseClientFactory {
    /**
     * Cache cards to reduce mongo calls
     * //@Todo: move this somewhere more appropriate
     */
    public static Map<ObjectId, DBCard> cardCache;

    static {
        cardCache = new HashMap<>();
    }

    public static DBCard getCard(ObjectId objectId) {
        if(cardCache.containsKey(objectId)) {
            return cardCache.get(objectId);
        } else {
            DatabaseClient databaseClient = create();
            DBCard dbCard = databaseClient.getCard(objectId);
            cardCache.put(objectId, dbCard);
            return dbCard;
        }
    }

    public static DatabaseClient create() {
        return new DatabaseClient(
                Config.getProperty("mongo.address"),
                Integer.parseInt(Config.getProperty("mongo.port")),
                Config.getProperty("mongo.auth.username"),
                Config.getProperty("mongo.auth.password"),
                Config.getProperty("mongo.auth.database"),
                Config.getProperty("mongo.database"));
    }
}

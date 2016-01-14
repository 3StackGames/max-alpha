package com.three_stack.maximum_alpha.backend.game.utilities;

import com.three_stack.maximum_alpha.database_client.DatabaseClient;

public class DatabaseClientFactory {
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

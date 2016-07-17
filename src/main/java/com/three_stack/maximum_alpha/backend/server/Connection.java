package com.three_stack.maximum_alpha.backend.server;

import org.bson.types.ObjectId;
import org.java_websocket.WebSocket;

public class Connection {
    public final WebSocket socket;
    public final ObjectId playerId;
    public final ObjectId deckId;
    public boolean ready = false;

    public Connection(WebSocket socket, ObjectId playerId, ObjectId deckId) {
        this.socket = socket;
        this.playerId = playerId;
        this.deckId = deckId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}

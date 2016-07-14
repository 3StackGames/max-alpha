package com.three_stack.maximum_alpha.backend.server;

import org.json.JSONObject;

public class Message {
	//public ServerEvent eventType;
	public String eventType;
	public JSONObject message;

    public final static String EVENT_TYPE = "eventType";

    //TODO: implement
    public enum ServerEvent {

    }

    //TODO: implement
    public enum ClientEvent {

    }

	public Message (String eventType) {
		this.eventType = eventType;
		message = new JSONObject();
		message.put(EVENT_TYPE, eventType.toString());
	}

	public void add(String key, Object value) {
		message.put(key, value);
	}

	@Override
  public String toString() {
		return message.toString();
	}
}

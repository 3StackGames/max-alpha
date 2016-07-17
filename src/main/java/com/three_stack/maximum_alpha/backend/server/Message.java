package com.three_stack.maximum_alpha.backend.server;

import org.json.JSONObject;

public class Message {
	public JSONObject message;

  public final static String EVENT_TYPE = "eventType";

  //TODO: implement
  public enum ServerEvent {
    MESSAGE ("Server Message"), 
    FOUND ("Game Found"), 
    DECLINED ("Game Declined"), 
    PROMPT ("Player Prompt"),
    UPDATE ("State Update"),
    ERROR ("Error");
    
    private final String name;       

    private ServerEvent(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
  }

	public Message (ServerEvent eventType) {
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

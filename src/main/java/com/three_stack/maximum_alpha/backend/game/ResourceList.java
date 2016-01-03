package com.three_stack.maximum_alpha.backend.game;

import java.util.HashMap;
import java.util.Map;

public class ResourceList {
	public int red;
	public int black;
	public int green;
	public int white;
	public int yellow;
	public int blue;
	
	Map<String, Integer> custom = new HashMap<String, Integer>();
	
	public ResourceList() {
		red = 0;
		black = 0;
		green = 0;
		white = 0;
		yellow = 0;
		blue = 0;
	}
	
	public ResourceList(int r, int bk, int g, int w, int y, int b) {
		red = r;
		black = bk;
		green = g;
		white = w;
		yellow = y;
		blue = b;
	}
	
	public int getCustom(String name) {
		return custom.get(name);
	}
	
	public void add(ResourceList other) {
		red += other.red;
		black += other.black;
		green += other.green;
		white += other.white;
		yellow += other.yellow;
		blue += other.blue;
		
		for (Map.Entry<String, Integer> kv : other.custom.entrySet()) {
			String key = kv.getKey();
			int value = kv.getValue();
			
			if(custom.containsKey(key)) {
				custom.put(key, value + custom.get(key));
			}
			else {
				custom.put(key, value);
			}
		}
	}
}

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
	public int colorless;
	
	Map<String, Integer> custom;

	public ResourceList() {
        //all colors default to value 0
		custom = new HashMap<>();
	}

	public ResourceList(int red, int black, int green, int white, int yellow, int blue, int colorless) {
		this.red = red;
		this.black = black;
		this.green = green;
		this.white = white;
		this.yellow = yellow;
		this.blue = blue;
		this.colorless = colorless;
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
		colorless += other.colorless;
		
		for (Map.Entry<String, Integer> entry : other.custom.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(custom.containsKey(key)) {
				custom.put(key, value + custom.get(key));
			}
			else {
				custom.put(key, value);
			}
		}
	}
}

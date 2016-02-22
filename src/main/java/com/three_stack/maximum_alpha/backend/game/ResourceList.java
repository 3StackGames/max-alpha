package com.three_stack.maximum_alpha.backend.game;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceList {

    public enum Color {
		RED, BLACK, GREEN, WHITE, YELLOW, BLUE,
        /**
         * Note: COLORLESS must be last for calculation purposes.
         */
        COLORLESS
	}

	Map<Color, Integer> colors;
	
	Map<String, Integer> custom;

	public ResourceList() {
        setup();
	}

	public ResourceList(int red, int black, int green, int white, int yellow, int blue, int colorless) {
        setup();
        setColor(Color.RED, red);
        setColor(Color.BLACK, black);
        setColor(Color.GREEN, green);
        setColor(Color.WHITE, white);
        setColor(Color.YELLOW, yellow);
        setColor(Color.BLUE, blue);
        setColor(Color.COLORLESS, colorless);
	}

    public ResourceList(Color color, int colorAmount, int colorlessAmount) {
        setup();
        setColor(color, colorAmount);
        setColor(Color.COLORLESS, colorAmount);
    }

    public ResourceList(Map<String, Integer> costMap) {
        setup();
        for(Map.Entry<String, Integer> keyValue : costMap.entrySet()) {
            String key = keyValue.getKey();
            int value = keyValue.getValue();

            Color color = Color.valueOf(key.toUpperCase());
            setColor(color, value);
        }
    }

    public ResourceList(int colorlessAmount) {
        setup();
        setColor(Color.COLORLESS, colorlessAmount);
    }

    public ResourceList(ResourceList other) {
        colors = other.colors.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue())
                );
        custom = other.custom.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new String(entry.getKey()),
                        entry -> entry.getValue()
                ));
    }

    private void setup() {
        //all colors default to values 0
        colors = new HashMap<>();
        for (Color c : Color.values()) {
        	colors.put(c, 0);
        }
        
        custom = new HashMap<>();
    }

    public int getColor(Color color) {
        if(colors.containsKey(color)) {
            return colors.get(color);
        }

        return 0;
    }

    public void setColor(Color color, int value) {
        if(value < 0) {
            value = 0;
        }
        colors.put(color, value);
    }

    public void addColor(Color color, int value) {
        setColor(color, value+getColor(color));
    }
	
	public int getCustom(String name) {
        if(custom.containsKey(name)) {
            return custom.get(name);
        }

        return 0;
	}

    public void setCustom(String name, int value) {
        if(value < 0) {
            value = 0;
        }
        custom.put(name, value);
    }

    public void addCustom(String name, int value) {
        setCustom(name, value+getCustom(name));
    }
	
	public void add(ResourceList other) {
        for (Map.Entry<Color, Integer> color : other.colors.entrySet()) {
            Color key = color.getKey();
            int value = color.getValue();

            addColor(key, value);
        }

        for (Map.Entry<String, Integer> entry : other.custom.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			
			addCustom(key, value);
		}
	}
	
	public void lose(ResourceList other) {
	    for (Map.Entry<Color, Integer> color : other.colors.entrySet()) {
            Color key = color.getKey();
            int value = color.getValue();

            addColor(key, -value);
        }

        for (Map.Entry<String, Integer> entry : other.custom.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            
            addCustom(key, -value);
        }
	}
	
	public boolean hasResources(ResourceList other) {	
		int totalColorless = 0;
	    for (Color color : Color.values()) {
            int colorValue = getColor(color);
            int otherColorValue = other.getColor(color);

            if (color != Color.COLORLESS) {
                if (otherColorValue > colorValue)
                    return false;
                else
                    totalColorless += colorValue - otherColorValue;
            }
            else {
                totalColorless += colorValue;

                if(totalColorless < otherColorValue) {
                    return false;
                }
            }
	    }
	    
	    for (Map.Entry<String, Integer> entry : other.custom.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            
            if (value > getCustom(key))
                return false;
        }
	    
	    return true;
	}
	
	public int getTotal() {
		int sum = 0;
		for (int mana : colors.values()) {
        	sum += mana;
        }
		return sum;
	}
}

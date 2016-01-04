package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	List<Card> deck = new ArrayList<Card>();
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public Card draw() {
		Card draw = deck.get(0);
		deck.remove(0);
		return draw;
	}
}

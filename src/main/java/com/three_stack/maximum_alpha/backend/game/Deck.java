package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class Deck {
	List<Card> deck = new ArrayList<>();
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public Card draw() {
		Card draw = deck.get(0);
		deck.remove(0);
		return draw;
	}
	
	public List<Card> drawCards(int numCards) {
		List<Card> subDeck = deck.subList(0, numCards);
		deck.removeAll(subDeck);
		return subDeck;
	}
	
	public List<Card> topCards(int numCards) {
		return deck.subList(0, numCards);
	}
	
	public Card takeCard(Card c) {
		if(contains(c)) {
			deck.remove(c);
			return c;
		}
		
		return null;
	}
	
	public Collection<Card> takeCards(Collection<Card> c) {
		if(containsAll(c)) {
			deck.removeAll(c);
			return c;
		}
		
		return null;
	}
	
	public void placeAtBottom(Card c) {
		if(contains(c)) {
			deck.remove(c);
			deck.add(c);
		}
		
	}
	
	public void placeAtBottom(Collection<Card> c) {
		if(containsAll(c)) {
			deck.removeAll(c);
			deck.addAll(c);
		}
	}
	
	public boolean contains(Card c) {
		if(!deck.contains(c)) {
			//ERROR report
			return false;
		}
		
		return true;
	}
	
	public boolean containsAll(Collection<Card> c) {
		if(!deck.contains(c)) {
			//ERROR report
			return false;
		}
		
		return true;
	}
}

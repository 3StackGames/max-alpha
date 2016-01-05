package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.FieldCleric;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.MilitiaMinuteman;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.TravelingMerchant;

public class Deck {
	List<Card> cards = new ArrayList<>();

	public Deck(List<Card> cards) {
		this.cards = cards;
	}
	
	public List<Card> getDeck() {
		return cards;
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public Card draw() {
		Card draw = cards.get(0);
		cards.remove(0);
		return draw;
	}
	
	public List<Card> drawCards(int numCards) {
		List<Card> subDeck = cards.subList(0, numCards);
		cards.removeAll(subDeck);
		return subDeck;
	}
	
	public List<Card> topCards(int numCards) {
		return cards.subList(0, numCards);
	}
	
	public Card takeCard(Card c) {
		if(contains(c)) {
			cards.remove(c);
			return c;
		}
		
		return null;
	}
	
	public Collection<Card> takeCards(Collection<Card> c) {
		if(containsAll(c)) {
			cards.removeAll(c);
			return c;
		}
		
		return null;
	}
	
	public void placeAtBottom(Card c) {
		if(contains(c)) {
			cards.remove(c);
			cards.add(c);
		}
		
	}
	
	public void placeAtBottom(Collection<Card> c) {
		if(containsAll(c)) {
			cards.removeAll(c);
			cards.addAll(c);
		}
	}
	
	public boolean contains(Card c) {
		if(!cards.contains(c)) {
			//ERROR report
			return false;
		}
		
		return true;
	}
	
	public boolean containsAll(Collection<Card> c) {
		if(!cards.contains(c)) {
			//ERROR report
			return false;
		}
		
		return true;
	}

	public static Deck loadDeck(int deckId) {
		List<Card> cards = new ArrayList<Card>();
		for(int i = 0; i < 15; i++) {
			cards.add(new FieldCleric());
			cards.add(new MilitiaMinuteman());
			cards.add(new TravelingMerchant());
		}

		for(int i = 0; i < 5; i++) {
			cards.add(new MilitiaMinuteman());
		}

		return new Deck(cards);
	}
}

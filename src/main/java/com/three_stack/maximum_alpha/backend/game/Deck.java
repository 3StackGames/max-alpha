package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.foobar.test.FieldCleric;
import com.three_stack.maximum_alpha.backend.game.cards.foobar.test.MilitiaMinuteman;
import com.three_stack.maximum_alpha.backend.game.cards.foobar.test.PilotRecruit;
import com.three_stack.maximum_alpha.backend.game.cards.foobar.test.TravelingMerchant;

public class Deck {
	List<Card> deck = new ArrayList<>();

	public Deck(List<Card> cards) {
		this.deck = cards;
	}
	
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

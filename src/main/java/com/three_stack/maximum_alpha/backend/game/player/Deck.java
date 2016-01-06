package com.three_stack.maximum_alpha.backend.game.player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.*;

public class Deck {
	CardList<Card> cards = new CardList<>();

	public Deck(CardList<Card> cards) {
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
		CardList<Card> cards = new CardList<>();
		for(int i = 0; i < 7; i++) {
			cards.add(new WhiteCreature());
            cards.add(new BlackCreature());
            cards.add(new YellowCreature());
            cards.add(new RedCreature());
            cards.add(new BlueCreature());
            cards.add(new GreenCreature());
            cards.add(new MilitiaMinuteman());
		}

		return new Deck(cards);
	}
}

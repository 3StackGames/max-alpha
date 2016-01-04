package com.three_stack.maximum_alpha.backend.game;


//State will have a history as one of its fields (?)

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.event.Event;

public final class Game {
	
	private Game() {}
	
	public static State newGame(Parameters parameters) {
		return null;
	}

	public static State processAction(State state, Event event) {
		return null;
	}

	public static boolean isLegalAction(State state, Action action) {
		return false;
	}

	public static Deck loadDeck(int deckId) {
		return null;
	}

	public static Card loadCard(int cardId) {
		return null;
	}
	
	public static Action stringToAction(String action) {
		return null;
	}
}

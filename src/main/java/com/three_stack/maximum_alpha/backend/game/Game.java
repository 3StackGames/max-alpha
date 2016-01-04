package com.three_stack.maximum_alpha.backend.game;


//GameState will have a history as one of its fields (?)

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public final class Game {
	
	private Game() {}
	
	public static GameState newGame(GameParameters gameParameters) {
		return null;
	}

	public static GameState processAction(GameState state, GameEvent event) {
		return null;
	}

	public static boolean isLegalAction(GameState state, GameAction action) {
		return false;
	}

	public static Deck loadDeck(int deckId) {
		return null;
	}

	public static Card loadCard(int cardId) {
		return null;
	}
	
	public static GameAction stringToAction(String action) {
		return null;
	}
}

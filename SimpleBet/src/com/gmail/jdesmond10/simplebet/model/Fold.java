package com.gmail.jdesmond10.simplebet.model;

import com.gmail.jdesmond10.simplebet.model.GameStateData.Player;

/**
 * The action of folding during play.
 * 
 * Note that folds are legal even a check would make more sense.
 * 
 * @author Josh Desmond
 *
 */
public class Fold extends BettingAction {

	@Override
	protected void apply(GameStateData gameState) {
		if (!isValid(gameState)) {
			throw new IllegalArgumentException("Given move was invalid");
		}

		// First find which player is acting
		Player player = gameState.getPlayerToBet();
		assert player != null; // Shouldn't happen because of above check.

		// Then apply the fold from their perspective.
		gameState.endHand(player.getOther());
	}

	@Override
	protected boolean isValid(GameStateData gameState) {
		if (gameState == null) throw new IllegalArgumentException();
		return (gameState.isInActionableState());
	}

}

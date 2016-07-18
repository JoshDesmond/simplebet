package com.gmail.jdesmond10.simplebet.model;

public class Check extends BettingAction {

	@Override
	protected void apply(GameStateData gameState) {
		if (!isValid(gameState)) {
			throw new IllegalArgumentException("Given move was invalid");
		}

	}

	@Override
	protected boolean isValid(GameStateData gameState) {
		if (gameState == null)
			throw new IllegalArgumentException();
		if (gameState.isInActionableState() == false) {
			return false;
		}

		// A check is valid if the opponent has a big blind's worth of chips,
		// and you do too.
		if (gameState.playerOneBet == 2 * gameState.smallBlindAmount) {
			if (gameState.playerTwoBet == 2 * gameState.smallBlindAmount) {
				return true;
			}
		}

		return false;
	}

}

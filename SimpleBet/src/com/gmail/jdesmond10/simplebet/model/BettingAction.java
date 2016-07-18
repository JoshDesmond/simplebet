package com.gmail.jdesmond10.simplebet.model;

/**
 * A betting action can be one of three types. A Fold, a Check, or a Raise
 * (which can also be a call).
 * 
 * @author Josh Desmond
 *
 */
public abstract class BettingAction {

	/**
	 * Applies the move to the given gameState. Make sure the move is valid
	 * before attempting it.
	 * 
	 * @param gameState
	 */
	protected abstract void apply(GameStateData gameState);

	/**
	 * Return true if the move is a valid move to be applied to the game.
	 * 
	 * @param gameState
	 * @return True if the move is valid.
	 */
	protected abstract boolean isValid(GameStateData gameState);

}

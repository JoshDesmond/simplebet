package com.gmail.jdesmond10.simplebet.model.game;

import com.gmail.jdesmond10.simplebet.model.game.GameStateData.Player;

public class Raise extends BettingAction {

	/** The number of chips being bet. Always greater than zero. */
	int amount;

	/**
	 * @param amount
	 *            The amount of the raise. Must be greater than zero.
	 */
	public Raise(int amount) {
		super();
		if (amount <= 0) {
			throw new IllegalArgumentException(
					String.format("Illegal bet amount of %s chips", amount));
		}

		this.amount = amount;
	}

	@Override
	protected void apply(GameStateData gameState) {
		if (!isValid(gameState)) {
			throw new IllegalArgumentException("Given move was invalid");
		}

		boolean isCall = this.isCall(gameState);
		
		// Apply the bet
		Player player = gameState.getPlayerToBet();
		gameState.betChips(player, amount);
	
		// If it is a call, end the hand.
		if (isCall) {
			gameState.showdown();
		}
	}

	@Override
	protected boolean isValid(GameStateData gameState) {
		if (gameState == null) throw new IllegalArgumentException();
		if (!gameState.isInActionableState()) {
			return false;
		}

		// Find out which player is up to act.
		Player player = gameState.getPlayerToBet();
		assert player != null; // Shouldn't happen because of above check.

		// Check minimum and maximum bet amounts
		if (amount > getMaximumBetAmount(gameState)) {
			return false;
		}

		if (amount < getMinimumBetAmount(gameState)) {
			return false;
		}

		return true;
	}

	/**
	 * Determines the maximum amount of chips the acting player can bet for a
	 * given game state. This takes into account the opponents stack size (you
	 * can't bet more than their all-in amount), and the number of chips the
	 * active player has to bet.
	 * 
	 * Note that the game must be in an actionable state.
	 * 
	 * @param gameState
	 *            State of the game to check. Must be in a state where a player
	 *            is actionable.
	 * @return The maximum number of chips the acting player can bet.
	 */
	public static int getMaximumBetAmount(GameStateData gameState) {
		if (gameState == null) throw new IllegalArgumentException();
		if (!gameState.isInActionableState()) {
			throw new IllegalArgumentException("Neither player has the action");
		}

		// Find out which player is up to act.
		final Player player = gameState.getPlayerToBet();
		final Player opponent = player.getOther();
		assert player != null; // Shouldn't happen because of above check.

		int maxBet; // Return value

		// First set the max bet equal to the number of chips the player has
		maxBet = gameState.getPlayerStack(player);

		// Reduce it to the amount of the opponents max all in stack.
		int opponentAllInAmount = gameState.getPlayerStack(opponent)
				+ gameState.getPlayerChipsBet(opponent);
		if (maxBet > opponentAllInAmount) {
			maxBet = opponentAllInAmount - gameState.getPlayerChipsBet(player);
		}

		return maxBet;
	}

	public static int getMinimumBetAmount(GameStateData gameState) {
		if (gameState == null) throw new IllegalArgumentException();
		if (!gameState.isInActionableState()) {
			throw new IllegalArgumentException("Neither player has the action");
		}

		// Find out which player is up to act.
		final Player player = gameState.getPlayerToBet();
		final Player opponent = player.getOther();
		assert player != null; // Shouldn't happen because of above check.

		int minBet; // Return value
		// Establish chips in play.
		final int chipsPlayer = gameState.getPlayerChipsBet(player);
		final int chipsOpponent = gameState.getPlayerChipsBet(opponent);
		assert (chipsPlayer > chipsOpponent);

		minBet = chipsOpponent - chipsPlayer;
		return minBet;
	}

	/**
	 * Determines if a bet is actually a call. This is when a bet brings the
	 * amount each player has bet to an equal amount, excluding the case where
	 * the bet was the first action of the hand.
	 * 
	 * @param gameState
	 *            Gamestate or context to analyze the raise within.
	 * @return True if the raise is actually a call.
	 */
	public boolean isCall(GameStateData gameState) {
		if (gameState == null) throw new IllegalArgumentException();
		if (amount != getMinimumBetAmount(gameState)) {
			return false;
		}

		// Now determine if it was the first bet of the game.
		if (gameState.getPlayerChipsBet(gameState.getPlayerToBet()) == gameState
				.getSmallBlindAmount()) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		return "Bet " + amount;
	}

}

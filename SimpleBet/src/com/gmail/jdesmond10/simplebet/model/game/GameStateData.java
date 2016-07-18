package com.gmail.jdesmond10.simplebet.model.game;

import java.util.Optional;

/**
 * The server side representation of the current game state.
 * 
 * @author Josh Desmond
 */
public class GameStateData {

	enum State {
		Showdown, HandEnd, PlayerOneToAct, PlayerTwoToAct;
	}

	enum Player {
		One, Two;

		/** returns the other player */
		public Player getOther() {
			if (this == Player.One) {
				return Player.Two;
			}

			return Player.One;
		}
	}

	/** The current state of the game. */
	State state;
	/**
	 * The number of chips the player has. This doesn't include amount committed
	 * in the pot.
	 */
	int playerOneStack, playerTwoStack;
	/** The card the player currently has. */
	Optional<Card> playerOneCard, playerTwoCard;
	/**
	 * The number of chips the player currently has committed. This includes the
	 * blinds.
	 */
	int playerOneBet, playerTwoBet;
	/**
	 * True if player one has the button. This means they get the small blind,
	 * and are first to act in the hand.
	 */
	boolean playerOneHasButton;
	/** The number of chips for a small blind. */
	int smallBlindAmount;

	/**
	 * Creates a new game from the start. The game will start player one having
	 * the button.
	 * 
	 * @param startingChips
	 * @param smallBlind
	 */
	public GameStateData(int startingChips, int smallBlind) {
		if (smallBlind <= 0) {
			throw new IllegalArgumentException(String
					.format("Small bind value of %s is illegal", smallBlind));
		} else if (startingChips <= 0) {
			throw new IllegalArgumentException(String.format(
					"Starting chip amount of %s is illegal", startingChips));
		}
		playerOneStack = startingChips;
		playerTwoStack = startingChips;
		smallBlindAmount = smallBlind;
		playerOneBet = playerTwoBet = 0;
		state = State.HandEnd;
		playerOneCard = Optional.empty();
		playerTwoCard = Optional.empty();
	}

	/**
	 * Switches the button from the player who currently has it to the other
	 * player.
	 */
	private void swapButton() {
		playerOneHasButton = !playerOneHasButton;
	}

	/**
	 * Moves all the chips that were bet to the given player's stash. This does
	 * not do anything else.
	 * 
	 * @param player
	 *            The player to receive the chips.
	 */
	private void moveBetsToStack(Player player) {
		if (player == Player.One) {
			playerOneStack += playerOneBet;
			playerOneStack += playerTwoBet;
			playerOneBet = playerTwoBet = 0;
		} else if (player == Player.Two) {
			playerTwoStack += playerOneBet;
			playerTwoStack += playerTwoBet;
			playerOneBet = playerTwoBet = 0;
		}
	}

	/**
	 * Moves chips from the given players stack to their committed chips in
	 * play.
	 * 
	 * @param player
	 *            The given player to move the chips from their stash.
	 * @param numberOfChips
	 *            The number of chips to transfer.
	 */
	protected void betChips(Player player, int numberOfChips) {
		if (player == Player.One) {
			playerOneStack -= numberOfChips;
			playerOneBet += numberOfChips;
			state = State.PlayerTwoToAct;
		} else if (player == Player.Two) {
			playerTwoStack -= numberOfChips;
			playerTwoBet += numberOfChips;
			state = State.PlayerOneToAct;
		}

		// We already switched states, but didn't take into account all-ins.
		// TODO FIXME code this.

		// A quick little post error check
		if (playerOneStack < 0 || playerTwoStack < 0) {
			throw new IllegalArgumentException(String.format(
					"An illegal bet of %s chips was made, causing "
							+ "player %s to have negative chips",
					numberOfChips, player));
		}
	}

	/**
	 * Moves the number of chips specified by {@link #smallBlindAmount} from
	 * each players stack to their active betting pile. This takes into account
	 * the current position of the button.
	 */
	protected void payBlinds() {
		// First, both players pay one small blind
		betChips(Player.One, smallBlindAmount);
		betChips(Player.Two, smallBlindAmount);

		// Then the small blind is played for thee who doesn't have the button.
		if (playerOneHasButton) {
			betChips(Player.Two, smallBlindAmount);
		} else {
			betChips(Player.One, smallBlindAmount);
		}
	}

	/**
	 * Determines whether the game is in a state where betting actions are
	 * allowed.
	 * 
	 * @return True if there are betting actions, or if the game is in the state
	 *         of PlayerOneToAct or PlayerTwoToAct.
	 * 
	 */
	public boolean isInActionableState() {
		return (state == State.PlayerOneToAct || state == State.PlayerTwoToAct);
	}

	/**
	 * Ends the hand by moving the committed chips to the winners stack, and
	 * removing the cards from play.
	 * 
	 * @param winner
	 */
	protected void endHand(Player winner) {
		playerOneCard = Optional.empty();
		playerTwoCard = Optional.empty();
		moveBetsToStack(winner);

		state = State.HandEnd;
	}

	/**
	 * Swaps the button, deals out the next cards, and makes each player pay the
	 * blinds.
	 */
	protected void dealNextHand() {
		if (state != State.HandEnd) {
			// Throw an error? Not really sure if this should ever happen.
			throw new IllegalStateException(
					"Can't deal another hand until the game is in HandEnd "
							+ "state. Currently the game state is " + state);
		}

		playerOneCard = Optional.of(Card.getRandomCard());
		playerTwoCard = Optional.of(Card.getRandomCard());
		swapButton();
		payBlinds();
		if (playerOneHasButton) {
			state = State.PlayerOneToAct;
		} else {
			state = State.PlayerTwoToAct;
		}
	}

	/**
	 * Enters the gameState to showdown phase. This is called after a call or a
	 * check is made.
	 */
	protected void showdown() {
		this.state = State.Showdown;
	}

	protected void resolveShowdown() {
		if (this.state != State.Showdown) {
			throw new IllegalArgumentException(
					"Can't resolve showdown - gamestate is in " + this.state);
		}

		Player bestHand;
		int handDifference = playerTwoCard.get().compareTo(playerOneCard.get());

		// Determine which player has the better card, or if there was a tie.
		bestHand = Player.One;
		if (handDifference == 0) {
			// resolve a tie.
			playerOneCard = Optional.empty();
			playerTwoCard = Optional.empty();
			assert playerOneBet == playerTwoBet;
			playerOneStack += playerOneBet;
			playerTwoStack += playerTwoBet;
			playerOneBet = 0;
			playerTwoBet = 0;
			state = State.HandEnd;
			return;
		} else if (handDifference > 0) {
			bestHand = Player.Two;
		}
		
		endHand(bestHand);
	}

	/**
	 * Returns the number of chips the player has bet so far.
	 * 
	 * @param player
	 *            The player to check.
	 * @return Integer number of chips they have bet so far. Should always be
	 *         greater than zero.
	 */
	public int getPlayerChipsBet(Player player) {
		return (player == Player.One) ? playerOneBet : playerTwoBet;
	}

	/**
	 * Returns the number of chips the player has in their stack. This doesn't
	 * include the number of chips they have in play/the number of chips bet.
	 * 
	 * @param player
	 *            The player to check.
	 * @return Integer number of chips they have available for play.
	 */
	public int getPlayerStack(Player player) {
		return (player == Player.One) ? playerOneStack : playerTwoStack;
	}

	/**
	 * Determines which player is up to act next.
	 * 
	 * @return Player.One or Player.Two, or null if neither player is up to act.
	 */
	public Player getPlayerToBet() {
		if (state == State.PlayerOneToAct) {
			return Player.One;
		} else if (state == State.PlayerTwoToAct) {
			return Player.Two;
		}

		else {
			// Since the game state is such that neither player is up to act,
			// returning null makes sense I think?
			return null;
		}
	}

	public int getSmallBlindAmount() {
		return smallBlindAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameStateData [");
		if (state != null)
			builder.append("state=").append(state).append(", ");
		builder.append("playerOneStack=").append(playerOneStack)
				.append(", playerTwoStack=").append(playerTwoStack)
				.append(", ");
		if (playerOneCard != null)
			builder.append("playerOneCard=").append(playerOneCard).append(", ");
		if (playerTwoCard != null)
			builder.append("playerTwoCard=").append(playerTwoCard).append(", ");
		builder.append("playerOneBet=").append(playerOneBet)
				.append(", playerTwoBet=").append(playerTwoBet)
				.append(", playerOneHasButton=").append(playerOneHasButton)
				.append("]");
		return builder.toString();
	}

}

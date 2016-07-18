package com.gmail.jdesmond10.simplebet.model.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gmail.jdesmond10.simplebet.model.game.GameStateData;
import com.gmail.jdesmond10.simplebet.model.game.Raise;
import com.gmail.jdesmond10.simplebet.model.game.GameStateData.State;

public class RaiseTest {

	/**
	 * Game in which player one is up to bet, and player two has bet 2 chips,
	 * and player one has bet 1 chip, and both player started with/have 10
	 * chips.
	 */
	GameStateData data;
	/**
	 * See Setup for details about the gamestate.
	 */
	GameStateData maxBetData;

	@Before
	public void setUp() throws Exception {
		data = new GameStateData(10, 1);
		data.dealNextHand();
		assert data.state == State.PlayerOneToAct;
		assert data.getPlayerChipsBet(data.getPlayerToBet()) == 1;

		/*
		 * [state=PlayerTwoToBet, playerOneStack=2, playerTwoStack=8,
		 * playerOneBet=2, playerTwoBet=1, playerOneHasButton=false]
		 * 
		 * maxBet should be 3 chips.
		 */
		maxBetData = new GameStateData(10, 1);
		maxBetData.state = State.PlayerTwoToAct;
		maxBetData.playerOneStack = 2;
		maxBetData.playerTwoStack = 8;
		maxBetData.playerOneBet = 2;
		maxBetData.playerTwoBet = 1;
		maxBetData.playerOneHasButton = false;
	}

	@Test
	public void testApply() {
		// First lets make a new Gamestate since we're going to be altering it.
		GameStateData state = new GameStateData(10, 1);
		state.dealNextHand();

		new Raise(1).apply(state);
		assertTrue(state.playerOneBet == 2);
		assertTrue(state.playerOneStack == 8);
		assertTrue(state.playerTwoBet == 2);
		assertTrue(state.playerTwoStack == 8);

		new Raise(3).apply(state);
		assertTrue(state.playerOneBet == 2);
		assertTrue(state.playerOneStack == 8);
		assertTrue(state.playerTwoBet == 5);
		assertTrue(state.playerTwoStack == 5);
	}

	@Test
	public void testApplyCall() {
		// Sometimes a raise isn't a raise at all and is really just a call.
		// This is when the opponent bets the minimum amount, and it is not the
		// first action of the hand.

		// Lets test a call in the first action of a hand, this should not end
		// the hand.
		GameStateData g = new GameStateData(10, 1);
		g.dealNextHand();
		new Raise(1).apply(g);
		assertTrue(g.state == State.PlayerTwoToAct);

		// Now lets do that again, but instead, raise two, and then have player
		// two call one chip.
		GameStateData d = new GameStateData(10, 1);
		d.dealNextHand();
		new Raise(2).apply(d);
		new Raise(1).apply(d); // This is the call that ends the action.
		assertTrue(d.state == State.Showdown);
	}

	@Test
	public void testIsValid() {
		Raise validRaise = new Raise(1);
		Raise otherValidRaise = new Raise(9);
		assertTrue(validRaise.isValid(data));
		assertTrue(otherValidRaise.isValid(data));

		Raise otherInvalidRaise = new Raise(10);
		assertTrue(!otherInvalidRaise.isValid(data));

		// Now for a couple of quick checks on the other gamestate.
		Raise aboveMaxBet = new Raise(4);
		assertTrue(!aboveMaxBet.isValid(maxBetData));
		assertTrue(new Raise(1).isValid(maxBetData));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegative() {
		@SuppressWarnings("unused")
		Raise r = new Raise(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorZero() {
		@SuppressWarnings("unused")
		Raise r = new Raise(0);
	}

	@Test
	public void testGetMaximumBetAmount() {
		// Where the max bet is the most chips you have.
		assertTrue(Raise.getMaximumBetAmount(data) == 9);

		// Where max bet is limited by the opponents all-in amount.
		assertTrue(Raise.getMaximumBetAmount(maxBetData) == 3);
	}

	@Test
	public void testGetMinimumBetAmount() {
		assertTrue(Raise.getMinimumBetAmount(data) == 1);
		assertTrue(Raise.getMinimumBetAmount(maxBetData) == 1);
	}
	
	@Test
	public void testIsCall() {
		GameStateData d = new GameStateData(10, 1);
		d.dealNextHand();
		
		assertTrue((new Raise(1).isCall(d)) == false);
		new Raise(2).apply(d);
		assertTrue(new Raise(1).isCall(d));
		assertTrue(new Raise(2).isCall(d) == false);		
	}

}

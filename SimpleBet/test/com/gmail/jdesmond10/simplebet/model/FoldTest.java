package com.gmail.jdesmond10.simplebet.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gmail.jdesmond10.simplebet.model.GameStateData.State;

public class FoldTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Generates a new GameStateData using 10 chips, 1 chip small blind, and
	 * dealing the first hand, where player one is up to bet first having the
	 * small blind.
	 * 
	 * @return new GamestateData object.
	 */
	private static GameStateData getNewGameState() {
		GameStateData d = new GameStateData(10, 1);
		d.dealNextHand();
		return d;
	}

	@Test
	public void testApply() {
		GameStateData d = getNewGameState();
		new Fold().apply(d);
		assertTrue(d.state == State.HandEnd);
		d.dealNextHand();
		assertTrue(d.playerOneBet == 2);
		assertTrue(d.playerTwoBet == 1);
		assertTrue(d.playerOneStack == 7);
		assertTrue(d.playerTwoStack == 10);
	}

	@Test
	public void testIsValid() {
		GameStateData d = getNewGameState();
		assertTrue(new Fold().isValid(d));
		new Raise(2).apply(d);
		assertTrue(new Fold().isValid(d));
		new Raise(1).apply(d);
		assertTrue(new Fold().isValid(d) == false);
	}

}

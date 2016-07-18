package com.gmail.jdesmond10.simplebet.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmail.jdesmond10.simplebet.model.GameStateData.State;

public class CheckTest {

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
		new Raise(1).apply(d);
		
		new Check().apply(d);
		assertTrue(d.state == State.Showdown);
	}

	@Test
	public void testIsValid() {
		GameStateData d = getNewGameState();
		new Raise(1).apply(d);
		assertTrue(new Check().isValid(d));
		
		GameStateData g = getNewGameState();
		new Fold().apply(g);
		g.dealNextHand();
		new Raise(1).apply(g);
		assertTrue(new Check().isValid(g));
	}
	
	@Test
	public void testIsNotValid() {
		GameStateData d = getNewGameState();
		new Fold().apply(d);
		assertTrue(new Check().isValid(d) == false);
		
		GameStateData a = getNewGameState();
		assertTrue(new Check().isValid(a) == false);
		
		GameStateData g = getNewGameState();
		new Fold().apply(g);
		g.dealNextHand();
		new Raise(5).apply(g); // Player 2 has bet 6 total
		new Raise(5).apply(g); // Player 1 has bet 7 total now
		new Raise(1).apply(g); // Player 2 has bet 7 total now, this is a call
		assertTrue(new Check().isValid(g) == false);
	}

}

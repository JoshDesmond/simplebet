package com.gmail.jdesmond10.simplebet.model.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gmail.jdesmond10.simplebet.model.game.GameStateData.Player;

public class GameStateDataTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetOtherPlayer() {
		Player p = Player.One;
		assertTrue(p.getOther() == Player.Two);
		Player p2 = Player.Two	;
		assertTrue(p2.getOther() == Player.One);
	}

}

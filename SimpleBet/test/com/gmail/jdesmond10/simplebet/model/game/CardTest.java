package com.gmail.jdesmond10.simplebet.model.game;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmail.jdesmond10.simplebet.model.game.Card;

public class CardTest {

	@Test
	public void testComparable() {
		assertTrue(Card.Ace.compareTo(Card.Three) > 0);
		assertTrue(Card.Queen.compareTo(Card.Jack) > 0);
		assertFalse(Card.King.compareTo(Card.Ace) > 0);
		assertTrue(Card.Seven.compareTo(Card.Seven) == 0);
	}

	@Test
	public void testGetRandomCard() {
		for (int i = 50; i > 0; i--) {
			Card.getRandomCard();
		}
		assertNotNull(Card.getRandomCard());
	}

	@Test
	public void testGetBoundaryCards() {
		assertNotNull(Card.getCard(2));
		assertNotNull(Card.getCard(14));
	}

}

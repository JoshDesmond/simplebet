package com.gmail.jdesmond10.simplebet.model.game;

/**
 * A Card is a number between 2 and 14.
 * 
 * @author Josh
 */
public enum Card implements Comparable<Card> {
	Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(
			9), Ten(10), Jack(11), Queen(12), King(13), Ace(14);

	public final int value;

	Card(int i) {
		value = i;
	}

	/**
	 * Returns the Card corresponding to the given int.
	 * 
	 * @param cardVal
	 *            Integer value corresponding to the card. To get a Two, use 2.
	 * @return Card corresponding to given int.
	 */
	protected static Card getCard(int cardVal) {
		// Minus two because the 0th card in the array is Two.
		return Card.values()[cardVal - 2];
	}

	/**
	 * Picks a random card.
	 * 
	 * @return A random card between Two and Ace.
	 */
	public static Card getRandomCard() {
		final double d = Math.random() * 12;
		return Card.getCard((int) d + 2);
	}
}

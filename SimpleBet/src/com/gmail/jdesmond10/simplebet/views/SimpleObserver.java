package com.gmail.jdesmond10.simplebet.views;

/**
 * A simple observer can observe one object, which implements SimpleObservable.
 * 
 * @author Josh Desmond
 *
 */
public interface SimpleObserver {

	/**
	 * Notifies the Observer that the Observable object has updated.
	 */
	public void updated();

}

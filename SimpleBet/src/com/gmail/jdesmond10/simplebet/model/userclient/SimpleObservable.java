package com.gmail.jdesmond10.simplebet.model.userclient;

import com.gmail.jdesmond10.simplebet.views.SimpleObserver;

/**
 * If a class implements SimpleObserverable, it must call every registered
 * Observer's Updated() method when it changes.
 * 
 * @author Josh Desmond
 *
 */
public interface SimpleObservable {

	public void registerObserver(SimpleObserver observer);

	/**
	 * Instead of calling to update each observer to update, use this method to
	 * handle changes.
	 */
	void updated();

}

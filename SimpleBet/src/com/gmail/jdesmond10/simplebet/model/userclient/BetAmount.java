package com.gmail.jdesmond10.simplebet.model.userclient;

import java.util.Objects;

import com.gmail.jdesmond10.simplebet.views.SimpleObserver;

public class BetAmount implements SimpleObservable {

	SimpleObserver observer;
	private int proposedAmount;

	public BetAmount() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registerObserver(SimpleObserver observer) {
		this.observer = Objects.requireNonNull(observer);
	}

	@Override
	public void updated() {
		observer.updated();
	}

	public int getProposedAmount() {
		return proposedAmount;
	}

	protected void setProposedAmount(int proposedAmount) {
		this.proposedAmount = proposedAmount;
		this.updated();
	}

}

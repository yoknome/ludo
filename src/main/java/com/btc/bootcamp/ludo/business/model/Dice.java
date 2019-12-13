package com.btc.bootcamp.ludo.business.model;

public class Dice {

	private final Integer sideCount;
	private Integer result;

	public Dice(Integer sideCount) {
		this.sideCount = sideCount;
	}

	public void roll() {
		this.result = (int) (Math.random() * sideCount) + 1;
	}

	public Integer getResult() {
		return result;
	}

	public Integer getSideCount() {
		return sideCount;
	}

}

package com.epam.hackathon.domain.request.bid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Budgets {
	
	@SerializedName("A")
	@Expose
	private Integer playerABudget;
	@SerializedName("B")
	@Expose
	private Integer playerBBudget;

	public Integer getplayerABudget() {
	return playerABudget;
	}

	public void setplayerABudget(Integer a) {
	this.playerABudget = a;
	}

	public Integer getplayerBBudget() {
	return playerBBudget;
	}

	public void setplayerBBudget(Integer b) {
	this.playerBBudget = b;
	}

}

package com.epam.hackathon.domain.request.matchStart;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchStartConfig {
	
	@SerializedName("roundLimit")
	@Expose
	private Integer roundLimit;
	@SerializedName("turnLimit")
	@Expose
	private Integer turnLimit;
	@SerializedName("players")
	@Expose
	private Budgets players;
	@SerializedName("map")
	@Expose
	private Map map;
	@SerializedName("treasureValue")
	@Expose
	private Integer treasureValue;

	public Integer getRoundLimit() {
	return roundLimit;
	}

	public void setRoundLimit(Integer roundLimit) {
	this.roundLimit = roundLimit;
	}

	public Integer getTurnLimit() {
	return turnLimit;
	}

	public void setTurnLimit(Integer turnLimit) {
	this.turnLimit = turnLimit;
	}

	public Budgets getPlayers() {
	return players;
	}

	public void setPlayers(Budgets players) {
	this.players = players;
	}

	public Map getMap() {
	return map;
	}

	public void setMap(Map map) {
	this.map = map;
	}

	public Integer getTreasureValue() {
	return treasureValue;
	}

	public void setTreasureValue(Integer treasureValue) {
	this.treasureValue = treasureValue;
	}

}

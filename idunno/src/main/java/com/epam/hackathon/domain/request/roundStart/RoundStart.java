package com.epam.hackathon.domain.request.roundStart;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoundStart {

	@SerializedName("bluePlayer")
	@Expose
	private String bluePlayer;
	@SerializedName("bluePlayerBudget")
	@Expose
	private Integer bluePlayerBudget;
	@SerializedName("bluePlayerDirection")
	@Expose
	private String bluePlayerDirection;
	@SerializedName("redPlayer")
	@Expose
	private String redPlayer;
	@SerializedName("redPlayerBudget")
	@Expose
	private Integer redPlayerBudget;
	@SerializedName("redPlayerDirection")
	@Expose
	private String redPlayerDirection;
	@SerializedName("map")
	@Expose
	private List<Country> map = null;
	@SerializedName("round")
	@Expose
	private Integer round;

	public String getBluePlayer() {
		return bluePlayer;
	}

	public void setBluePlayer(String bluePlayer) {
		this.bluePlayer = bluePlayer;
	}

	public Integer getBluePlayerBudget() {
		return bluePlayerBudget;
	}

	public void setBluePlayerBudget(Integer bluePlayerBudget) {
		this.bluePlayerBudget = bluePlayerBudget;
	}

	public String getBluePlayerDirection() {
		return bluePlayerDirection;
	}

	public void setBluePlayerDirection(String bluePlayerDirection) {
		this.bluePlayerDirection = bluePlayerDirection;
	}

	public String getRedPlayer() {
		return redPlayer;
	}

	public void setRedPlayer(String redPlayer) {
		this.redPlayer = redPlayer;
	}

	public Integer getRedPlayerBudget() {
		return redPlayerBudget;
	}

	public void setRedPlayerBudget(Integer redPlayerBudget) {
		this.redPlayerBudget = redPlayerBudget;
	}

	public String getRedPlayerDirection() {
		return redPlayerDirection;
	}

	public void setRedPlayerDirection(String redPlayerDirection) {
		this.redPlayerDirection = redPlayerDirection;
	}

	public List<Country> getMap() {
		return map;
	}

	public void setMap(List<Country> map) {
		this.map = map;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

}

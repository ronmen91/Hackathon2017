package com.epam.hackathon.gameobjects;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.epam.hackathon.strategy.AnotherStrategy;

public class Game {
	private static final Logger logger = LoggerFactory.getLogger(AnotherStrategy.class);
	private Team teamA;
	private Team teamB;
	private Team ourTeam;
	private List<Country> countries;
	private IslandState[][] gameMap;
	private Island[][] islandMap;

	private int roundLimit;
	private int turnLimit;
	private int currentTurn;
	private int currentRound;
	private int treasureValue;
	private int treasureCount;

	public int getTreasureCount() {
		return treasureCount;
	}

	public void setTreasureCount(int treasureCount) {
		this.treasureCount = treasureCount;
	}

	public int getTreasureValue() {
		return treasureValue;
	}

	public void setTreasureValue(int treasureValue) {
		this.treasureValue = treasureValue;
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public IslandState[][] getGameMap() {
		return gameMap;
	}

	public void setGameMap(IslandState[][] gameMap) {
		this.gameMap = gameMap;
	}

	public int getRoundLimit() {
		return roundLimit;
	}

	public void setRoundLimit(int roundLimit) {
		this.roundLimit = roundLimit;
	}

	public int getTurnLimit() {
		return turnLimit;
	}

	public void setTurnLimit(int turnLimit) {
		this.turnLimit = turnLimit;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

	public Team getOurTeam() {
		return ourTeam;
	}

	public void setOurTeam(Team ourTeam) {
		this.ourTeam = ourTeam;
	}

	public Island[][] getIslandMap() {
		return islandMap;
	}

	public void setIslandMap(Island[][] islandMap) {
		this.islandMap = islandMap;
	}

	public void printMap(Marker marker) {
		String side = "|";
		StringBuilder map = new StringBuilder();

		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap.length; j++) {
				map.append(side).append(gameMap[i][j].getValue()).append(side);
			}
			if (marker == null) {
				logger.debug(map.toString());
			} else {
				logger.debug(marker, map.toString());
			}
			map = new StringBuilder();
		}
	}

	public void printMap() {
		printMap(null);
	}

}

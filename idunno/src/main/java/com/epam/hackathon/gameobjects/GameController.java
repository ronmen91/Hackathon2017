package com.epam.hackathon.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hackathon.domain.request.bid.Bid;
import com.epam.hackathon.domain.request.matchStart.MatchStart;
import com.epam.hackathon.domain.request.roundStart.RoundStart;
import com.epam.hackathon.domain.request.turn_end.TurndEnd;

import com.epam.hackathon.strategy.DefaultStrategy;
import com.epam.hackathon.strategy.DiagonalStrategy;
import com.epam.hackathon.strategy.Strategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameController {
	private final Logger logger = LoggerFactory.getLogger(GameController.class);

	private Game game;
	private boolean isEnd;
	private Strategy strategy;
	private String teamName;

	public GameController() {
		game = new Game();
		strategy = new DiagonalStrategy(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void start() {
		JsonParser parser = new JsonParser();
		Scanner sc = new Scanner(System.in);
		String line = null;

		while (!isEnd && (line = sc.nextLine()) != null) {
			JsonObject message = (JsonObject) parser.parse(line);
			switch (message.get("event").toString()) {
			case "\"match_start\"":
				startMatch(message);
				break;
			case "\"round_start\"":
				startRound(message);
				break;
			case "\"bid\"":
				bid(message);
				break;
			case "\"turn_end\"":
				endTurn(message);
				break;
			case "\"round_end\"":
				endRound(message);
				break;
			case "\"match_end\"":
				endMatch(message);
				break;

			default:
				System.out.println("default case: " + message.get("event").toString());
				break;
			}

		}
	}

	public void startMatch(JsonObject message) {

		Gson gson = new Gson();
		MatchStart matchStart = gson.fromJson(message.get("data"), MatchStart.class);
		teamName = matchStart.getName();
		game.setRoundLimit(matchStart.getMatchStartConfig().getRoundLimit());
		game.setTurnLimit(matchStart.getMatchStartConfig().getTurnLimit());
		game.setTreasureValue(matchStart.getMatchStartConfig().getTreasureValue());
		game.setTreasureCount(matchStart.getMatchStartConfig().getMap().getTreasureCount());
		Team teamA = new Team();
		teamA.setScore(0);
		Team teamB = new Team();
		teamB.setScore(0);
		game.setTeamA(teamA);
		game.setTeamB(teamB);
	}

	public void startRound(JsonObject message) {
		Gson gson = new Gson();
		RoundStart roundStart = gson.fromJson(message.get("data"), RoundStart.class);
		game.setIslandMap(new Island[7][7]);
		game.setCountries(createCountries(roundStart));
		game.setCurrentRound(roundStart.getRound());
		game.setGameMap(initializeGameMap());
		logger.debug(message.toString());
		game.setTeamA(createTeamA(roundStart));
		game.setTeamB(createTeamB(roundStart));
		game.setOurTeam(teamName.equals(game.getTeamA().getName()) ? game.getTeamA() : game.getTeamB());
	}

	private List<Country> createCountries(RoundStart roundStart) {
		List<Country> countries = new ArrayList<>();
		for (int i = 0; i < roundStart.getMap().size(); i++) {
			Country gameCountry = new Country();
			gameCountry.setIndex(i);
			gameCountry.setIslands(createGameIslands(roundStart, i));
			countries.add(gameCountry);
		}
		return countries;
	}

	private List<Island> createGameIslands(RoundStart roundStart, int i) {
		Island[][] islandMap = game.getIslandMap();
		List<Island> gameIslands = new ArrayList<>();
		for (List<Integer> position : roundStart.getMap().get(i).getIslands()) {
			Island gameIsland = new Island();
			gameIsland.setX(position.get(0));
			gameIsland.setY(position.get(1));
			gameIsland.setTreasure(isStreasure(position, roundStart.getMap().get(i).getTreasures()));
			gameIslands.add(gameIsland);
			islandMap[position.get(0)][position.get(1)] = gameIsland;

		}
		return gameIslands;
	}

	private Team createTeamB(RoundStart roundStart) {
		Team teamB = new Team();
		teamB.setBudget(roundStart.getRedPlayerBudget());
		teamB.setDirection(
				roundStart.getRedPlayerDirection().equals("Horizontal") ? Direction.HORIZONTAL : Direction.VERTICAL);
		teamB.setName(roundStart.getRedPlayer());
		return teamB;
	}

	private Team createTeamA(RoundStart roundStart) {
		Team teamA = new Team();
		teamA.setBudget(roundStart.getBluePlayerBudget());
		teamA.setDirection(
				roundStart.getBluePlayerDirection().equals("Horizontal") ? Direction.HORIZONTAL : Direction.VERTICAL);
		teamA.setName(roundStart.getBluePlayer());
		return teamA;
	}

	private IslandState[][] initializeGameMap() {
		IslandState[][] gameMap = new IslandState[7][7];
		for (IslandState[] gameMapRow : gameMap) {
			Arrays.fill(gameMapRow, IslandState.FREE_ISLAND);
		}
		return gameMap;
	}

	private boolean isStreasure(List<Integer> position, List<List<Integer>> treasureIslands) {
		boolean isTreasure = false;
		for (List<Integer> treasureIslandPosition : treasureIslands) {
			if ((position.get(0) == treasureIslandPosition.get(0))
					&& (position.get(1) == treasureIslandPosition.get(1))) {
				isTreasure = true;
			}
		}
		return isTreasure;
	}

	public void bid(JsonObject message) {
		Gson gson = new Gson();
		Bid bid = gson.fromJson(message.get("data"), Bid.class);
		int countryIndex = bid.getCountryIndex();
		game.setCurrentTurn(bid.getTurn());
		System.out.println(gson.toJson(strategy.bid(countryIndex)));
	}

	public void endTurn(JsonObject message) {
		Gson gson = new Gson();
		TurndEnd turnEnd = gson.fromJson(message.get("data"), TurndEnd.class);
		int teamABudget = Integer.parseInt(message.getAsJsonObject("data").getAsJsonObject("state")
				.getAsJsonObject("budgets").get(game.getTeamA().getName()).toString());
		int teamBBudget = Integer.parseInt(message.getAsJsonObject("data").getAsJsonObject("state")
				.getAsJsonObject("budgets").get(game.getTeamB().getName()).toString());
		game.getTeamA().setBudget(teamABudget);
		game.getTeamB().setBudget(teamBBudget);
		logger.debug(message.toString());
		game.printMap();
		String winnerName = turnEnd.getState().getHighBidder();
		if (winnerName != null) {
			setNewIslandOwner(getIslandPosition(message, winnerName), getWinner(winnerName));
		}
	}

	private List<Integer> getIslandPosition(JsonObject message, String winnerName) {
		JsonArray ownerBy = message.getAsJsonObject("data").getAsJsonObject("state").getAsJsonObject("ownedBy")
				.getAsJsonArray(winnerName);

		int size = ownerBy.size();
		JsonArray islands = (JsonArray) ownerBy.get(size - 1);
		int x = Integer.parseInt(islands.get(0).toString());
		int y = Integer.parseInt(islands.get(1).toString());
		logger.debug(winnerName + " won the bid , island at {" + x + "," + y + "}");

		return Arrays.asList(x, y);
	}

	public void endRound(JsonObject message) {
		logger.debug(message.toString());
		int teamARank = Integer.parseInt(
				message.getAsJsonObject("data").getAsJsonObject("rankings").get(game.getTeamA().getName()).toString());
		int teamBRank = Integer.parseInt(
				message.getAsJsonObject("data").getAsJsonObject("rankings").get(game.getTeamB().getName()).toString());
		if (teamARank == 1) {
			game.getTeamA().setScore(game.getTeamA().getScore() + 1);
		} else if (teamBRank == 1) {
			game.getTeamB().setScore(game.getTeamB().getScore() + 1);
		}
		strategy.dangerIslandList.clear();
		strategy.danger = false;
	}

	public void endMatch(JsonObject message) {
		int teamARank = Integer.parseInt(
				message.getAsJsonObject("data").getAsJsonObject("rankings").get(game.getTeamA().getName()).toString());
		int teamBRank = Integer.parseInt(
				message.getAsJsonObject("data").getAsJsonObject("rankings").get(game.getTeamB().getName()).toString());
		if (teamARank == 1) {
			game.getTeamA().setScore(game.getTeamA().getScore() + 1);
		} else if (teamBRank == 1) {
			game.getTeamB().setScore(game.getTeamB().getScore() + 1);
		}
		isEnd = true;
	}

	private IslandState getWinner(String name) {
		return name.equals(game.getTeamA().getName()) ? IslandState.OWNED_BY_PLAYER_A : IslandState.OWNED_BY_PLAYER_B;
	}

	private void setNewIslandOwner(List<Integer> position, IslandState owner) {
		game.getGameMap()[position.get(0)][position.get(1)] = owner;
		if (owner.equals(IslandState.OWNED_BY_PLAYER_A)) {
			Island island = game.getIslandMap()[position.get(0)][position.get(1)];
			island.setOwner(game.getTeamA());
			game.getTeamA().addIsland(island);
		} else {
			Island island = game.getIslandMap()[position.get(0)][position.get(1)];
			island.setOwner(game.getTeamB());
			game.getTeamB().addIsland(island);
		}
	}
}

package com.epam.hackathon.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hackathon.domain.response.BidResponse;
import com.epam.hackathon.domain.response.bidIsland.BidIsland;
import com.epam.hackathon.domain.response.passIsland.PassIsland;
import com.epam.hackathon.gameobjects.Country;
import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;
import com.epam.hackathon.gameobjects.IslandState;

public class DefaultStrategy extends Strategy {

	public DefaultStrategy(Game game) {
		super(game);
	}

	@Override
	public BidResponse bid(int countryIndex) {
		List<Island> possibleIslands = getPossibleIslands(countryIndex);
		Map<Island, Integer> islandValues = getValuesToIslands(possibleIslands);
		Island result = new Island();
		int max = 0;
		for (Map.Entry<Island, Integer> entry : islandValues.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				result = entry.getKey();
			}
		}
		BidIsland response = new BidIsland();
		setIslandToResponse(response, result);
		response.setAmount(amountDecider(max, result, countryIndex));
		if(response.getAmount() == 0) {
			PassIsland passResponse = new PassIsland();
			passResponse.setPass(true);
			return passResponse;
		}
		return response;
	}

	private Map<Island, Integer> getValuesToIslands(List<Island> possibleIslands) {
		Map<Island, Integer> islandValues = new HashMap<>();
		for (Island island : possibleIslands) {
			islandValues.put(island, generateValue(island));
		}
		return islandValues;
	}

	private List<Island> getPossibleIslands(int countryIndex) {
		List<Island> possibleIslands = game.getCountries().get(countryIndex).getIslands();
		Iterator<Island> ite = possibleIslands.iterator();
		while (ite.hasNext()) {
			Island island = ite.next();
			if (game.getGameMap()[island.getX()][island.getY()] != IslandState.FREE_ISLAND) {
				ite.remove();
			}
		}
		return possibleIslands;
	}


	private Integer amountDecider(int max, Island result, int countryIndex) {
		Country country = game.getCountries().get(countryIndex);
		int treasure = 0;
		for (Island island : country.getIslands()) {
			if(result.equals(island)) {
				if(island.isTreasure()) {
					//// WATCH THIS!!!!!
					treasure = game.getTreasureValue();
				}
			}
		}
		int amount = 1;
		int budget = game.getOurTeam().getBudget();
		if (max == 4) {
			amount = budget / 5 + 2 + treasure/2;
			if(amount > budget) {
				amount = 1;
			}
		} else if (max == 3) {
			amount = budget / 6 + 2 + treasure/2;
			if(amount > budget) {
				amount = 1;
			}
		} else if (max == 2) {
			amount = budget / 9 + 1 + treasure/3;
			if(amount > budget) {
				amount = 1;
			}
		}
		return amount;
	}

	private Integer generateValue(Island island) {
		int xDiff = Math.abs(island.getX() - 4);
		int yDiff = Math.abs(island.getY() - 4);
		int value = 4;
		if ((xDiff > 2) || (yDiff > 2)) {
			value = 1;
		} else if ((xDiff > 1) || (yDiff > 1)) {
			value = 2;
		} else if ((xDiff > 0) || (yDiff > 0)) {
			value = 3;
		}
		return value;
	}

	private void setIslandToResponse(BidIsland response, Island tmp) {
		List<Integer> island = new ArrayList<>();
		island.add(tmp.getX());
		island.add(tmp.getY());
		response.setIsland(island);
	}

}

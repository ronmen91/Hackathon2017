package com.epam.hackathon.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hackathon.domain.response.BidResponse;
import com.epam.hackathon.domain.response.bidIsland.BidIsland;
import com.epam.hackathon.domain.response.passIsland.PassIsland;
import com.epam.hackathon.gameobjects.Country;
import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;
import com.epam.hackathon.gameobjects.IslandState;

public class RandomStrategy extends Strategy {

	private final Logger logger = LoggerFactory.getLogger(RandomStrategy.class);

	public RandomStrategy(Game game) {
		super(game);
	}

	private static final int _20 = 20;

	@Override
	public BidResponse bid(int countryIndex) {
		BidIsland response = new BidIsland();
		List<Integer> island = new ArrayList<>();
		// Random rn = new Random();
		// int random =
		// rn.nextInt(game.getCountries().get(countryIndex).getIslands().size()
		// + 1);
		List<Island> possibleIslands = getPossibleIslands(countryIndex);
		Island tmp = possibleIslands.get(0);
		island.add(tmp.getX());
		island.add(tmp.getY());
		response.setAmount(amountDecider(tmp, countryIndex));
		response.setIsland(island);
		logger.debug("RANDOM");
		if(response.getAmount() == 0) {
			PassIsland passResponse = new PassIsland();
			passResponse.setPass(true);
			return passResponse;
		}
		return response;
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

	private Integer amountDecider(Island result, int countryIndex) {
		Country country = game.getCountries().get(countryIndex);
		int treasure = 0;
		for (Island island : country.getIslands()) {
			if (result.equals(island)) {
				if (island.isTreasure()) {
					//// WATCH THIS!!!!!
					treasure = game.getTreasureValue();
				}
			}
		}
		int amount = 1;	
		int budget = game.getOurTeam().getBudget();

		amount += treasure / 3;
		if(amount > budget) {
			amount = 1;
			if(budget == 0) {
				amount = 0;
			}
		}

		return amount;
	}

}

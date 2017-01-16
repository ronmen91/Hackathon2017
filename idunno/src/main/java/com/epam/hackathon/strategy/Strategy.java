package com.epam.hackathon.strategy;

import java.util.List;

import com.epam.hackathon.domain.response.BidResponse;
import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;

public abstract class Strategy {
	protected Game game;
	public List<Island> dangerIslandList;
	public boolean danger;

	public Strategy(Game game) {
		this.game = game;
	}

	public abstract BidResponse bid(int countryIndex);

}

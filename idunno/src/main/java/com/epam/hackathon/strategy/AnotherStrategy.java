package com.epam.hackathon.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.epam.hackathon.domain.response.BidResponse;
import com.epam.hackathon.gameobjects.Game;

public class AnotherStrategy extends Strategy {
	private static final Logger logger = LoggerFactory.getLogger(AnotherStrategy.class);
	private Marker jocoMarker;
	private int x;
	private int y;

	public AnotherStrategy(Game game) {
		super(game);
	}

	@Override
	public BidResponse bid(int countryIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}

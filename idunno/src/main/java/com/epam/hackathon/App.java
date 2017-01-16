package com.epam.hackathon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hackathon.gameobjects.GameController;
import com.epam.hackathon.strategy.DefaultStrategy;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		try {
			GameController gameController = new GameController();
			gameController.start();

		} catch (Exception e) {
			logger.error("ex: ", e);
		}

	}
}

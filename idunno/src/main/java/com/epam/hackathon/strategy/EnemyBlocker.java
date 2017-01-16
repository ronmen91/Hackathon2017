package com.epam.hackathon.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;
import com.epam.hackathon.gameobjects.Team;

public class EnemyBlocker {
	private Game game;

	public EnemyBlocker(Game game) {
		this.game = game;
	}

	public Island tryToBlock(List<Island> availableIslands) {
		Island result = null;
		Team enemy = game.getOurTeam().equals(game.getTeamB()) ? game.getTeamA() : game.getTeamB();
		if (enemy.getIslands().size() > 5) {

			int dangerousColumn = getDangerousColumn(enemy.getIslands());
			if (dangerousColumn != -1) {
				for (Island island : availableIslands) {
					if (island.getOwner() == null && island.getY() == dangerousColumn) {
						return island;
					}
				}
			} else {
				int dangerousRow = getDangerousRow(enemy.getIslands());
				if (dangerousRow != -1) {
					for (Island island : availableIslands) {
						if (island.getOwner() == null && island.getX() == dangerousRow) {
							return island;
						}
					}
				}
			}
		}
		return result;
	}

	private int getDangerousRow(List<Island> islands) {
		Map<Integer, Integer> temp = new HashMap<>();
		islands.forEach(island -> {
			int x = island.getX();
			if (temp.get(x) == null) {
				temp.put(x, 1);
			} else {
				temp.replace(x, temp.get(x) + 1);
			}
		});
		for (Integer key : temp.keySet()) {
			if (temp.get(key) == 6) {
				return key;
			}
		}
		return -1;
	}

	private int getDangerousColumn(List<Island> islands) {
		Map<Integer, Integer> temp = new HashMap<>();
		islands.forEach(island -> {
			int y = island.getY();
			if (temp.get(y) == null) {
				temp.put(y, 1);
			} else {
				temp.replace(y, temp.get(y) + 1);
			}
		});
		for (Integer key : temp.keySet()) {
			if (temp.get(key) == 6) {
				return key;
			}
		}
		return -1;
	}
}

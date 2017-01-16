package com.epam.hackathon.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class Team {

	private String name;
	private int budget;
	private Direction direction;
	private List<Island> islands;
	int score;

	public Team() {
		islands = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean hasIslandInArow(int rowIndex) {
		return islands.stream().anyMatch(island -> island.getX() == rowIndex);
	}

	public boolean hasIslandInAColumn(int colIndex) {
		return islands.stream().anyMatch(island -> island.getY() == colIndex);
	}

	public void addIsland(Island island) {
		islands.add(island);
	}

	public List<Island> getIslands() {
		return islands;
	}

	public void setIslands(List<Island> islands) {
		this.islands = islands;
	}
}

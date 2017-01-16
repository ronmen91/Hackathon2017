package com.epam.hackathon.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.epam.hackathon.gameobjects.Direction;
import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;
import com.epam.hackathon.gameobjects.Team;

public class BlockFinalStep {
	private Game game;
	private Team enemy;

	public BlockFinalStep(Game game) {
		super();
		this.game = game;
		enemy = game.getOurTeam().equals(game.getTeamB()) ? game.getTeamA() : game.getTeamB();
	}

	public Island BlockFInalStep(List<Island> availableIslands) {

		Island result = null;
		List<Island> freeAvailableIslands = availableIslands.stream().filter(island -> island.getOwner() == null)
				.collect(Collectors.toList());
		if (freeAvailableIslands.size() > 0) {
			for (int i = 0; i < freeAvailableIslands.size() && result == null; i++) {
				if (isDangerous(freeAvailableIslands.get(i))) {
					result = freeAvailableIslands.get(i);
				}
			}
		}
		return result;
	}

	private boolean isDangerous(Island island) {
		boolean result = false;
		island.setOwner(enemy);
		if (enemy.getDirection().equals(Direction.HORIZONTAL)) {
			List<Island> islandsInColumn = enemy.getIslands().stream().filter(i -> i.getY() == 0)
					.collect(Collectors.toList());
			if (islandsInColumn.size() != 0) {
				result = checkHorisontal(0, islandsInColumn);
			}
		} else {
			List<Island> islandsInRow = enemy.getIslands().stream().filter(i -> i.getX() == 0)
					.collect(Collectors.toList());
			if (islandsInRow.size() != 0) {
				result = checkVertical(0, islandsInRow);
			}
		}
		island.setOwner(null);
		return result;
	}

	private boolean checkHorisontal(int column, List<Island> islands) {
		column++;
		if (column < 6) {
			for (Island island : islands) {
				List<Island> neighBours = getNeighboursInNextColumn(island);
				if (neighBours.size()>0) {
					return checkHorisontal(column, neighBours);
				}else{
					return false;
				}
			}
		}

		return true;
	}

	private List<Island> getNeighboursInNextColumn(Island island){
		return enemy.getIslands().stream().filter(i -> i.getX()==island.getX()+1).filter(i -> Math.abs(i.getX() - island.getX())+Math.abs(i.getY()-island.getY())<3).collect(Collectors.toList()); 
		
	}
	private boolean checkVertical(int row, List<Island> islands) {
		row++;
		if (row < 6) {
			for (Island island : islands) {
				List<Island> neighBours = getNeighboursInNextRow(island);
				if (neighBours.size()>0) {
					return checkHorisontal(row, neighBours);
				}else{
					return false;
				}
			}
		}
		
		return true;
	}
	
	private List<Island> getNeighboursInNextRow(Island island){
		return enemy.getIslands().stream().filter(i -> i.getY()==island.getY()+1).filter(i -> Math.abs(i.getX() - island.getX())+Math.abs(i.getY()-island.getY())<3).collect(Collectors.toList()); 
		
	}

	
}

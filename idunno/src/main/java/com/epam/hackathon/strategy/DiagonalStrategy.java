package com.epam.hackathon.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hackathon.domain.response.BidResponse;
import com.epam.hackathon.domain.response.bidIsland.BidIsland;
import com.epam.hackathon.domain.response.passIsland.PassIsland;
import com.epam.hackathon.gameobjects.Country;
import com.epam.hackathon.gameobjects.Direction;
import com.epam.hackathon.gameobjects.Game;
import com.epam.hackathon.gameobjects.Island;
import com.epam.hackathon.gameobjects.IslandState;
import com.epam.hackathon.gameobjects.Team;

public class DiagonalStrategy extends Strategy {

	List<Island> islandsGoodForMe = new ArrayList<>();
	List<Island> islandsVERYGoodForMe = new ArrayList<>();
	boolean danger;

	private final Logger logger = LoggerFactory.getLogger(DiagonalStrategy.class);

	public DiagonalStrategy(Game game) {
		super(game);
		dangerIslandList = new ArrayList<>();
		danger = false;
	}

	@Override
	public BidResponse bid(int countryIndex) {
		for (int i = 0; i < 6; i++) {
			if(blockEnemyIslandIfWeMustBool(i)) {
				danger = true;
			}
		}
		if (blockEnemyIslandIfWeMust(countryIndex) != null) {
			return blockEnemyIslandIfWeMust(countryIndex);
		}
		
		List<Island> possibleIslands = getPossibleIslands(countryIndex);
		if(dangerIslandList.size() != 0 || danger){
			List<Island> commonEnemy = getCommonIslands(dangerIslandList, possibleIslands);
			if (commonEnemy.size() > 0) {
				BidIsland response = new BidIsland();
				setIslandToResponse(response, commonEnemy.get(0));
				Team enemy = game.getOurTeam().equals(game.getTeamB()) ? game.getTeamA() : game.getTeamB();
				if (enemy.getBudget() + 1 > game.getOurTeam().getBudget()) {
					response.setAmount(game.getOurTeam().getBudget());
				} else {
					response.setAmount(enemy.getBudget() + 1);
				}
				
				Iterator<Island> ite = dangerIslandList.iterator();
				while (ite.hasNext()) {
					Island island = ite.next();
					if (commonEnemy.get(0).equals(island)) {
						ite.remove();
					}
				}
				if(response.getAmount() == 0) {
					PassIsland passResponse = new PassIsland();
					passResponse.setPass(true);
					return passResponse;
				}
				return response;
			}
		}

		if (game.getOurTeam().getIslands().size() == 0) {
			return firstRoundMovement(countryIndex);
		} else if (game.getOurTeam().getIslands().size() == 1) {
			return secondRoundMovement(countryIndex);
		} else {
			String debug = "VÁLASZTHATÓ: ";
			for (Island island : possibleIslands) {
				debug.concat(island.toString() + " ");
			}
			logger.debug(debug);
			fillIslandsVERYGoodForMe();

			List<Island> common = getCommonIslands(islandsGoodForMe, possibleIslands); // homokora
			List<Island> commonVERY = getCommonIslands(islandsVERYGoodForMe, possibleIslands); // szomszédok
			List<Island> soFuckingVeryGood = getCommonIslands(findDuplicates(islandsVERYGoodForMe), possibleIslands); // gap

			if (soFuckingVeryGood.size() > 0) {
				logger.debug("soverygooodba léptem");
				BidIsland response = new BidIsland();
				setIslandToResponse(response, soFuckingVeryGood.get(0));
				response.setAmount(amountDecider(4, soFuckingVeryGood.get(0), countryIndex));
				islandsVERYGoodForMe.clear();
				if(response.getAmount() == 0) {
					PassIsland passResponse = new PassIsland();
					passResponse.setPass(true);
					return passResponse;
				}
				return response;
			} else if (commonVERY.size() > 0) {
				logger.debug("gooodba léptem");
				BidIsland response = new BidIsland();
				Island result = commonVERY.get(0);
				if (game.getOurTeam().getDirection().equals(Direction.HORIZONTAL)) { // VERTICAL!!!
					for (Island island : commonVERY) {
						if (!game.getOurTeam().hasIslandInArow(island.getX())) {
							result = island;
							break;
						}
					}
				} else {
					for (Island island : commonVERY) {
						if (!game.getOurTeam().hasIslandInAColumn(island.getY())) {
							result = island;
							break;
						}
					}
				}

				setIslandToResponse(response, result);
				response.setAmount(amountDecider(4, result, countryIndex));
				islandsVERYGoodForMe.clear();
				if(response.getAmount() == 0) {
					PassIsland passResponse = new PassIsland();
					passResponse.setPass(true);
					return passResponse;
				}
				return response;
			} else if (common.size() > 0) {
				logger.debug("commonba léptem");
				Island result = new Island();
				Map<Island, Integer> islandValues = getValuesToIslands(common);
				int max = 0;
				for (Map.Entry<Island, Integer> entry : islandValues.entrySet()) {
					if (entry.getValue() > max) {
						max = entry.getValue();
						result = entry.getKey();
					}
				}
				if (game.getOurTeam().getDirection().equals(Direction.HORIZONTAL)) { // VERTICAL!!!
					for (Island island : common) {
						if (!game.getOurTeam().hasIslandInArow(island.getX())) {
							result = island;
							break;
						}
					}
				} else {
					for (Island island : common) {
						if (!game.getOurTeam().hasIslandInAColumn(island.getY())) {
							result = island;
							break;
						}
					}
				}
				BidIsland response = new BidIsland();
				setIslandToResponse(response, result);
				response.setAmount(amountDecider(2, result, countryIndex));
				islandsVERYGoodForMe.clear();
				if(response.getAmount() == 0) {
					PassIsland passResponse = new PassIsland();
					passResponse.setPass(true);
					return passResponse;
				}
				return response;
			} else {
				islandsVERYGoodForMe.clear();
				return new RandomStrategy(game).bid(countryIndex);
			}
		}
	}

	private boolean blockEnemyIslandIfWeMustBool(int i) {
		EnemyBlocker emEnemyBlocker = new EnemyBlocker(game);
		List<Island> availableIslands = new ArrayList<>();
		for (Island island : game.getCountries().get(i).getIslands()) {
			if (island.getOwner() == null) {
				availableIslands.add(island);
			}
		}
		Island islandToBlock = emEnemyBlocker.tryToBlock(availableIslands);
		if (islandToBlock != null) {
			return true;
		} else {
			return false;
		}
		
	}

	private BidResponse blockEnemyIslandIfWeMust(int countryIndex) {
		EnemyBlocker emEnemyBlocker = new EnemyBlocker(game);
		List<Island> availableIslands = new ArrayList<>();
		for (Island island : game.getCountries().get(countryIndex).getIslands()) {
			if (island.getOwner() == null) {
				availableIslands.add(island);
			}
		}
		Island islandToBlock = emEnemyBlocker.tryToBlock(availableIslands);
		if (islandToBlock != null) {
			checkNeighbours(islandToBlock);
			BidIsland response = new BidIsland();
			setIslandToResponse(response, islandToBlock);
			Team enemy = game.getOurTeam().equals(game.getTeamB()) ? game.getTeamA() : game.getTeamB();
			if (enemy.getBudget() + 1 > game.getOurTeam().getBudget()) {
				response.setAmount(game.getOurTeam().getBudget());
			} else {
				response.setAmount(enemy.getBudget() + 1);
			}
			if(response.getAmount() == 0) {
				PassIsland passResponse = new PassIsland();
				passResponse.setPass(true);
				return passResponse;
			}
			return response;
		} else {
			return null;
		}
	}

	private void checkNeighbours(Island islandToBlock) {
		Team enemy = game.getOurTeam().equals(game.getTeamB()) ? game.getTeamA() : game.getTeamB();
		if(enemy.getDirection().equals(Direction.HORIZONTAL)){
			if(islandToBlock.getY() == 0 && game.getGameMap()[islandToBlock.getX()][islandToBlock.getY() + 1].equals(IslandState.FREE_ISLAND)){
				dangerIslandList.add(new Island(islandToBlock.getX(), islandToBlock.getY() + 1));	
			} else if(islandToBlock.getY() == 6 && game.getGameMap()[islandToBlock.getX()][islandToBlock.getY() -1].equals(IslandState.FREE_ISLAND)){
				dangerIslandList.add(new Island(islandToBlock.getX(), islandToBlock.getY() - 1));	
			} else {
				if(game.getGameMap()[islandToBlock.getX()][islandToBlock.getY() - 1].equals(IslandState.FREE_ISLAND)){
					dangerIslandList.add(new Island(islandToBlock.getX(), islandToBlock.getY() - 1));	
				}
				if(game.getGameMap()[islandToBlock.getX()][islandToBlock.getY() + 1].equals(IslandState.FREE_ISLAND)){
					dangerIslandList.add(new Island(islandToBlock.getX(), islandToBlock.getY() + 1));	
				}
			}
		} else {
			if(islandToBlock.getX() == 0 && game.getGameMap()[islandToBlock.getX() +1] [islandToBlock.getY()].equals(IslandState.FREE_ISLAND)){
				dangerIslandList.add(new Island(islandToBlock.getX() + 1, islandToBlock.getY()));	
			} else if(islandToBlock.getX() == 6 && game.getGameMap()[islandToBlock.getX() - 1][islandToBlock.getY()].equals(IslandState.FREE_ISLAND)){
				dangerIslandList.add(new Island(islandToBlock.getX() - 1, islandToBlock.getY()));	
			} else {
				if(game.getGameMap()[islandToBlock.getX() - 1][islandToBlock.getY()].equals(IslandState.FREE_ISLAND)){
					dangerIslandList.add(new Island(islandToBlock.getX() - 1, islandToBlock.getY()));	
				}
				if(game.getGameMap()[islandToBlock.getX() + 1][islandToBlock.getY()].equals(IslandState.FREE_ISLAND)){
					dangerIslandList.add(new Island(islandToBlock.getX() + 1, islandToBlock.getY()));	
				}
			}
		}
	}

	private void fillIslandsVERYGoodForMe() {
		for (Island island : game.getOurTeam().getIslands()) {
			if (game.getOurTeam().getDirection().equals(Direction.VERTICAL)) {
				islandsVERYGoodForMe.addAll(findHorizantalNeighbours(island));
			} else {
				islandsVERYGoodForMe.addAll(findVerticalNeigbours(island));
			}
		}
	}

	public List<Island> findDuplicates(List<Island> listContainingDuplicates) {
		final Set<Island> setToReturn = new HashSet<>();
		final Set<Island> set1 = new HashSet<>();

		for (Island yourInt : listContainingDuplicates) {
			if (!set1.add(yourInt)) {
				setToReturn.add(yourInt);
			}
		}
		return new ArrayList<>(setToReturn);
	}

	private Collection<? extends Island> findVerticalNeigbours(Island island) {
		List<Island> neigbours = new ArrayList<>();
		List<Island> upperneigbours = new ArrayList<>();
		List<Island> downneigbours = new ArrayList<>();

		if (island.getX() == 0) {
			if (island.getY() == 0) {
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
			} else if (island.getY() == 6) {
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
			} else {
				downneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
			}
		} else if (island.getX() == 6) {
			if (island.getY() == 0) {
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				upperneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
			} else if (island.getY() == 6) {
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				upperneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
			} else {
				upperneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				upperneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
			}
		} else {
			if (island.getY() == 0) {
				upperneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
			} else if (island.getY() == 6) {
				upperneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
			} else {
				upperneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
				upperneigbours.add(new Island(island.getX() - 1, island.getY()));
				upperneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
				downneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
				downneigbours.add(new Island(island.getX() + 1, island.getY()));
				downneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
			}
		}
		for (Island island2 : downneigbours) {
			if (game.getOurTeam().getIslands().contains(island2)) {
				downneigbours.clear();
				break;
			}
		}
		for (Island island2 : upperneigbours) {
			if (game.getOurTeam().getIslands().contains(island2)) {
				upperneigbours.clear();
				break;
			}
		}
		neigbours.addAll(upperneigbours);
		neigbours.addAll(downneigbours);
		return neigbours;
	}

	private Collection<? extends Island> findHorizantalNeighbours(Island island) {
		List<Island> neigbours = new ArrayList<>();
		List<Island> leftneigbours = new ArrayList<>();
		List<Island> rightneigbours = new ArrayList<>();

		if (island.getY() == 0) {
			if (island.getX() == 0) {
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
			} else if (island.getX() == 6) {
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
			} else {
				rightneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
			}
		} else if (island.getY() == 6) {
			if (island.getX() == 0) {
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
			} else if (island.getX() == 6) {
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
			} else {
				leftneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
			}
		} else {
			if (island.getX() == 0) {
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
			} else if (island.getX() == 6) {
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
			} else {
				rightneigbours.add(new Island(island.getX() + 1, island.getY() + 1));
				rightneigbours.add(new Island(island.getX(), island.getY() + 1));
				rightneigbours.add(new Island(island.getX() - 1, island.getY() + 1));
				leftneigbours.add(new Island(island.getX() + 1, island.getY() - 1));
				leftneigbours.add(new Island(island.getX(), island.getY() - 1));
				leftneigbours.add(new Island(island.getX() - 1, island.getY() - 1));
			}
		}
		for (Island island2 : rightneigbours) {
			if (game.getOurTeam().getIslands().contains(island2)) {
				rightneigbours.clear();
				break;
			}
		}
		for (Island island2 : leftneigbours) {
			if (game.getOurTeam().getIslands().contains(island2)) {
				leftneigbours.clear();
				break;
			}
		}
		neigbours.addAll(rightneigbours);
		neigbours.addAll(leftneigbours);
		return neigbours;
	}

	private BidResponse secondRoundMovement(int countryIndex) {
		DefaultStrategy defStrategy = new DefaultStrategy(game);
		BidIsland response = (BidIsland) defStrategy.bid(countryIndex);
		int firstX = game.getOurTeam().getIslands().get(0).getX();
		int firstY = game.getOurTeam().getIslands().get(0).getY();
		int secondX = response.getIsland().get(0);
		int secondY = response.getIsland().get(1);
		islandsGoodForMe.clear();
		if (game.getOurTeam().getDirection().equals(Direction.VERTICAL)) {
			if (firstY > secondY) {
				islandsGoodForMe.addAll(getRightTriangle(firstX, firstY));
				islandsGoodForMe.addAll(getLeftTriangle(secondX, secondY));
				islandsGoodForMe.addAll(calculatePathHorizontal(secondX, secondY, firstX, firstY));
			} else {
				islandsGoodForMe.addAll(getRightTriangle(secondX, secondY));
				islandsGoodForMe.addAll(getLeftTriangle(firstX, firstY));
				islandsGoodForMe.addAll(calculatePathHorizontal(firstX, firstY, secondX, secondY));
			}
		} else {
			if (firstX < secondX) {
				islandsGoodForMe.addAll(getUpperTriangle(firstX, firstY));
				islandsGoodForMe.addAll(getDownTriangle(secondX, secondY));
				islandsGoodForMe.addAll(calculatePathVertical(firstX, firstY, secondX, secondY));
			} else {
				islandsGoodForMe.addAll(getUpperTriangle(secondX, secondY));
				islandsGoodForMe.addAll(getDownTriangle(firstX, firstY));
				islandsGoodForMe.addAll(calculatePathVertical(secondX, secondY, firstX, firstY));
			}

		}
		return response;
	}

	private List<Island> calculatePathVertical(int upperX, int upperY, int downX, int downY) {
		List<Island> result = new ArrayList<>();
		List<Island> upperPointDowner = getDownTriangle(upperX, upperY);
		List<Island> downPointUpper = getUpperTriangle(downX, downY);
		result = getCommonIslandsPath(upperPointDowner, downPointUpper);
		return result;
	}

	private List<Island> calculatePathHorizontal(int leftX, int leftY, int rightX, int rightY) {
		List<Island> result = new ArrayList<>();
		List<Island> leftPointRight = getRightTriangle(leftX, leftY);
		List<Island> rightPointLeft = getLeftTriangle(rightX, rightY);
		result = getCommonIslandsPath(leftPointRight, rightPointLeft);
		return result;
	}

	private Integer amountDecider(int max, Island result, int countryIndex) {
		if(dangerIslandList.size() > 0) {
			return 1;
		}
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
		if (max == 4) {
			amount = budget / 5 + 2 + treasure / 2;
			if (amount > budget) {
				amount = 1;
				if( budget == 0) {
					amount = 0;
				}
			}
		} else if (max == 3) {
			amount = budget / 6 + 2 + treasure / 2;
			if (amount > budget) {
				amount = 1;
				if( budget == 0) {
					amount = 0;
				}
			}
		} else if (max == 2) {
			amount = budget / 9 + 1 + treasure / 3;
			if (amount > budget) {
				amount = 1;
				if( budget == 0) {
					amount = 0;
				}
			}
		}
		return amount;
	}

	private void setIslandToResponse(BidIsland response, Island tmp) {
		List<Integer> island = new ArrayList<>();
		island.add(tmp.getX());
		island.add(tmp.getY());
		response.setIsland(island);
	}

	private Map<Island, Integer> getValuesToIslands(List<Island> possibleIslands) {
		Map<Island, Integer> islandValues = new HashMap<>();
		for (Island island : possibleIslands) {
			islandValues.put(island, generateValue(island));
		}
		return islandValues;
	}

	private List<Island> getCommonIslands(List<Island> islandsGoodForMe2, List<Island> possibleIslands) {
		List<Island> result = new ArrayList<>();
		for (Island island : islandsGoodForMe2) {
			for (Island island2 : possibleIslands) {
				if (island.equals(island2)) {
					result.add(island);
					break;
				}
			}
		}
		return result;
	}

	private List<Island> getCommonIslandsPath(List<Island> islandsGoodForMe2, List<Island> possibleIslands) {
		List<Island> result = new ArrayList<>();
		for (Island island : islandsGoodForMe2) {
			for (Island island2 : possibleIslands) {
				if (island.equals(island2)) {
					result.add(island);
					break;
				}
			}
		}
		return result;
	}

	private BidIsland firstRoundMovement(int countryIndex) {
		DefaultStrategy defStrategy = new DefaultStrategy(game);
		BidIsland response = (BidIsland) defStrategy.bid(countryIndex);
		int firstX = response.getIsland().get(0);
		int firstY = response.getIsland().get(1);
		islandsGoodForMe.clear();
		if (game.getOurTeam().getDirection().equals(Direction.VERTICAL)) {
			logger.debug("HORIZONTAL");
			islandsGoodForMe.addAll(getLeftTriangle(firstX, firstY));
			islandsGoodForMe.addAll(getRightTriangle(firstX, firstY));

		} else {
			logger.debug("VERTICAL");
			islandsGoodForMe.addAll(getUpperTriangle(firstX, firstY));
			islandsGoodForMe.addAll(getDownTriangle(firstX, firstY));
		}

		return response;
	}

	private List<Island> getLeftTriangle(int firstX, int firstY) {
		List<Island> result = new ArrayList<>();
		for (int i = 0; i < game.getGameMap().length; i++) {
			for (int j = 0; j < game.getGameMap()[i].length; j++) {
				if ((j < firstY) && (i >= (firstX - (firstY - j))) && (i <= (firstX + (firstY - j)))) {
					Island tmpIsland = new Island();
					tmpIsland.setX(i);
					tmpIsland.setY(j);
					result.add(tmpIsland);
				}
			}
		}
		return result;
	}

	private List<Island> getRightTriangle(int firstX, int firstY) {
		List<Island> result = new ArrayList<>();
		for (int i = 0; i < game.getGameMap().length; i++) {
			for (int j = 0; j < game.getGameMap()[i].length; j++) {
				if ((j > firstY) && (i >= (firstX - (j - firstY))) && (i <= (firstX + (j - firstY)))) {
					Island tmpIsland = new Island();
					tmpIsland.setX(i);
					tmpIsland.setY(j);
					result.add(tmpIsland);
				}
			}
		}
		return result;
	}

	private List<Island> getUpperTriangle(int firstX, int firstY) {
		List<Island> result = new ArrayList<>();
		for (int i = 0; i < game.getGameMap().length; i++) {
			for (int j = 0; j < game.getGameMap()[i].length; j++) {
				if ((i < firstX) && (j >= (firstY - (firstX - i))) && (j <= (firstY + (firstX - i)))) {
					Island tmpIsland = new Island();
					tmpIsland.setX(i);
					tmpIsland.setY(j);
					result.add(tmpIsland);

				}
			}
		}
		return result;
	}

	private List<Island> getDownTriangle(int firstX, int firstY) {
		List<Island> result = new ArrayList<>();
		for (int i = 0; i < game.getGameMap().length; i++) {
			for (int j = 0; j < game.getGameMap()[i].length; j++) {
				if ((i > firstX) && (j >= (firstY - (i - firstX))) && (j <= firstY + (i - firstX))) {
					Island tmpIsland = new Island();
					tmpIsland.setX(i);
					tmpIsland.setY(j);
					result.add(tmpIsland);

				}
			}
		}
		return result;
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
}

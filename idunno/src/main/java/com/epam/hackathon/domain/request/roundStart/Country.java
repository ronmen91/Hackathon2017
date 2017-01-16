package com.epam.hackathon.domain.request.roundStart;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

	@SerializedName("islands")
	@Expose
	private List<List<Integer>> islands = null;
	@SerializedName("treasures")
	@Expose
	private List<List<Integer>> treasures = null;

	public List<List<Integer>> getIslands() {
		return islands;
	}

	public void setIslands(List<List<Integer>> islands) {
		this.islands = islands;
	}

	public List<List<Integer>> getTreasures() {
		return treasures;
	}

	public void setTreasures(List<List<Integer>> treasures) {
		this.treasures = treasures;
	}

}

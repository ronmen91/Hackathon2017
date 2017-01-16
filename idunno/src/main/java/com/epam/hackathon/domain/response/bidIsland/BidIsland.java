package com.epam.hackathon.domain.response.bidIsland;

import java.util.List;

import com.epam.hackathon.domain.response.BidResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidIsland implements BidResponse{
	@SerializedName("amount")
	@Expose
	private Integer amount;
	@SerializedName("island")
	@Expose
	private List<Integer> island = null;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public List<Integer> getIsland() {
		return island;
	}

	public void setIsland(List<Integer> island) {
		this.island = island;
	}
}

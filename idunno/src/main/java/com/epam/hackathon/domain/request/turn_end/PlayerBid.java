package com.epam.hackathon.domain.request.turn_end;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerBid {
	
	@SerializedName("pass")
	@Expose
	private Boolean pass;
	@SerializedName("amount")
	@Expose
	private Integer amount;
	@SerializedName("island")
	@Expose
	private List<Integer> island = null;

	public Boolean getPass() {
	return pass;
	}

	public void setPass(Boolean pass) {
	this.pass = pass;
	}

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

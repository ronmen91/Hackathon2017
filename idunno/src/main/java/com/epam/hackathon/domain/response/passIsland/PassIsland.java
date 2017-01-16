package com.epam.hackathon.domain.response.passIsland;

import com.epam.hackathon.domain.response.BidResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassIsland implements BidResponse{
	@SerializedName("pass")
	@Expose
	private Boolean pass;

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}
}

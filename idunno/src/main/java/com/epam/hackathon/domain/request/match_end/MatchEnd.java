package com.epam.hackathon.domain.request.match_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchEnd {
	@SerializedName("rankings")
	@Expose
	private Rankings rankings;

	public Rankings getRankings() {
	return rankings;
	}

	public void setRankings(Rankings rankings) {
	this.rankings = rankings;
	}
}

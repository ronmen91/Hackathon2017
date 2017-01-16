package com.epam.hackathon.domain.request.round_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoundEnd {
	@SerializedName("rankings")
	@Expose
	private Rankings rankings;
	@SerializedName("round")
	@Expose
	private Integer round;

	public Rankings getRankings() {
		return rankings;
	}

	public void setRankings(Rankings rankings) {
		this.rankings = rankings;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}
}

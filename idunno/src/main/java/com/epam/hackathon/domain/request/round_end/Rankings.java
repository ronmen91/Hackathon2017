package com.epam.hackathon.domain.request.round_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rankings {
	@SerializedName("B")
	@Expose
	private Integer rankingB;
	@SerializedName("A")
	@Expose
	private Integer rankingA;
	
	public Integer getRankingB() {
		return rankingB;
	}
	public void setRankingB(Integer rankingB) {
		this.rankingB = rankingB;
	}
	public Integer getRankingA() {
		return rankingA;
	}
	public void setRankingA(Integer rankingA) {
		this.rankingA = rankingA;
	}
}

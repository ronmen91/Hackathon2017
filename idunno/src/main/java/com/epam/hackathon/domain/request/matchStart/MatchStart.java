package com.epam.hackathon.domain.request.matchStart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchStart {

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("config")
	@Expose
	private MatchStartConfig config;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MatchStartConfig getMatchStartConfig() {
		return config;
	}

	public void setMatchStartConfig(MatchStartConfig config) {
		this.config = config;
	}

}
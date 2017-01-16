package com.epam.hackathon.gameobjects;

public enum IslandState {
	OWNED_BY_PLAYER_A("A"), OWNED_BY_PLAYER_B("B"), FREE_ISLAND(" ");
	private final String value;

	private IslandState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}

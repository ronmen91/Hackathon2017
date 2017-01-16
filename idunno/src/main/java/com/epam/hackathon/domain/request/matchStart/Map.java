package com.epam.hackathon.domain.request.matchStart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map {
	
	@SerializedName("width")
	@Expose
	private Integer width;
	@SerializedName("height")
	@Expose
	private Integer height;
	@SerializedName("treasureCount")
	@Expose
	private Integer treasureCount;

	public Integer getWidth() {
	return width;
	}

	public void setWidth(Integer width) {
	this.width = width;
	}

	public Integer getHeight() {
	return height;
	}

	public void setHeight(Integer height) {
	this.height = height;
	}

	public Integer getTreasureCount() {
	return treasureCount;
	}

	public void setTreasureCount(Integer treasureCount) {
	this.treasureCount = treasureCount;
	}

}

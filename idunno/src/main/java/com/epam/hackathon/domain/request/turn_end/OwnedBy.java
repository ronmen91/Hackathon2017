package com.epam.hackathon.domain.request.turn_end;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwnedBy {
	@SerializedName(value="A", alternate={"B"})
	@Expose
	private List<List<Integer>> ownedBy = null;

	public List<List<Integer>> getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(List<List<Integer>> ownedBy) {
		this.ownedBy = ownedBy;
	}
}

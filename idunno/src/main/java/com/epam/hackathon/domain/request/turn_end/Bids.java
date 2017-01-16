package com.epam.hackathon.domain.request.turn_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bids {

@SerializedName("A")
@Expose
private PlayerBid playerABid;
@SerializedName("B")
@Expose
private PlayerBid playerBBid;

public PlayerBid getPlayerABid() {
	return playerABid;
}
public void setPlayerABid(PlayerBid a) {
	this.playerABid = a;
}
public PlayerBid PlayerBBid() {
	return playerBBid;
}
public void setPlayerBBid(PlayerBid b) {
	this.playerBBid = b;
}



}
package com.epam.hackathon.domain.request.turn_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State {

@SerializedName("highBidder")
@Expose
private String highBidder;
@SerializedName("bids")
@Expose
private Bids bids;
@SerializedName("ownedBy")
@Expose
private OwnedBy ownedBy;
@SerializedName("budgets")
@Expose
private Budgets budgets;

public String getHighBidder() {
return highBidder;
}

public void setHighBidder(String highBidder) {
this.highBidder = highBidder;
}

public Bids getBids() {
return bids;
}

public void setBids(Bids bids) {
this.bids = bids;
}

public OwnedBy getOwnedBy() {
return ownedBy;
}

public void setOwnedBy(OwnedBy ownedBy) {
this.ownedBy = ownedBy;
}

public Budgets getBudgets() {
return budgets;
}

public void setBudgets(Budgets budgets) {
this.budgets = budgets;
}

}
package com.epam.hackathon.domain.request.bid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bid {

@SerializedName("budgets")
@Expose
private Budgets budgets;
@SerializedName("countryIndex")
@Expose
private Integer countryIndex;
@SerializedName("turn")
@Expose
private Integer turn;
@SerializedName("round")
@Expose
private Integer round;

public Budgets getBudgets() {
return budgets;
}

public void setBudgets(Budgets budgets) {
this.budgets = budgets;
}

public Integer getCountryIndex() {
return countryIndex;
}

public void setCountryIndex(Integer countryIndex) {
this.countryIndex = countryIndex;
}

public Integer getTurn() {
return turn;
}

public void setTurn(Integer turn) {
this.turn = turn;
}

public Integer getRound() {
return round;
}

public void setRound(Integer round) {
this.round = round;
}

}
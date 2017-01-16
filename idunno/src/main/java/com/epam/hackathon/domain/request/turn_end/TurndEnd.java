package com.epam.hackathon.domain.request.turn_end;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TurndEnd {

@SerializedName("state")
@Expose
private State state;
@SerializedName("turn")
@Expose
private Integer turn;
@SerializedName("round")
@Expose
private Integer round;

public State getState() {
return state;
}

public void setState(State state) {
this.state = state;
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

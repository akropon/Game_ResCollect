package com.akropon.game_rescollect.util;

/**
 * Created by akropon on 17.09.2017.
 */

public enum PlayerID {
    P1,
    P2;

    public PlayerID getOpponent() {
        return this==P1 ? P2 : P1;
    }

    @Override
    public String toString() {
        if (this==P1) return "P1";
        else return "P2";
    }
}

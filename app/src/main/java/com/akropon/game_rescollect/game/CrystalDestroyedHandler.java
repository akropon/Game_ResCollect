package com.akropon.game_rescollect.game;

import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.game.util.ICrystalDestroyedHandler;
import com.akropon.game_rescollect.management.Constants;
import com.akropon.game_rescollect.util.PlayerID;

/**
 * Created by akropon on 17.09.2017.
 */

public class CrystalDestroyedHandler implements ICrystalDestroyedHandler {

    private GameManager gameManager;

    public CrystalDestroyedHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public void onCrystalDestroyed(PlayerID caller, Crystal crystal) {
        switch (crystal.getCrystalType()) {
            case SIMPLE:
                gameManager.updateScore(
                        caller, gameManager.getScore(caller) + Constants.GAME_COST_CRYSTAL_SIMPLE);
                break;
            case FREEZE:
                gameManager.updateScore(
                        caller, gameManager.getScore(caller) + Constants.GAME_COST_CRYSTAL_FREEZE);
                gameManager.getScene(caller.getOpponent())
                        .getEffectManager().setFreezed(Constants.GAME_EFFECT_FREEZE_DURATION);
            case RICH:
                gameManager.updateScore(
                        caller, gameManager.getScore(caller) + Constants.GAME_COST_CRYSTAL_RICH);
                break;
        }

        /*if (handlingScene==null) return;

        switch (crystal.getCrystalType()) {
            case SIMPLE:
                score1 += 10;
                score1Text.setText("Score (1): " + score1);
                break;
            case FREEZE:
                score1 += 5;
                score1Text.setText("Score (1): " + score1);
                scene3.getEffectManager().setFreezed(3);
            case RICH:
                score1 += 50;
                score1Text.setText("Score (1): " + score1);
                break;
        }*/
    }
}

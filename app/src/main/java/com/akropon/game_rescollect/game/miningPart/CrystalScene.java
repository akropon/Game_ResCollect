package com.akropon.game_rescollect.game.miningPart;

import com.akropon.game_rescollect.game.util.ICrystalDestroyedHandler;
import com.akropon.game_rescollect.management.Constants;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.util.PlayerID;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by akropon on 16.09.2017.
 */

public abstract class CrystalScene extends Scene {

    protected EffectManager effectManager;
    protected int clearingRate;
    protected int ticksFromLastCleaning;
    IUpdateHandler tickUpdateHandler;
    ICrystalDestroyedHandler crystalDestroyedCallback;
    ShotHandlerLayout shotHandlerLayout;
    PlayerID playerID;

    public CrystalScene(
            PlayerID playerID, ICrystalDestroyedHandler crystalDestroyedCallback) {
        this.crystalDestroyedCallback = crystalDestroyedCallback;
        this.playerID = playerID;

        effectManager = new EffectManager(this);
        clearingRate = 60;
        ticksFromLastCleaning = 0;
        setBackground(new Background(0, 0, 0f));

        tickUpdateHandler = new IUpdateHandler() {
            public void onUpdate(float pSecondsElapsed) {
                onTickUpdateWithClearing(pSecondsElapsed);
            }
            public void reset() {}
        };
        this.registerUpdateHandler(tickUpdateHandler);

        shotHandlerLayout = new ShotHandlerLayout(ScreenManager.getMiningScreenWidth(),
                ScreenManager.getMiningScreenHeight(), this);
        shotHandlerLayout.setZIndex(Constants.GAME_ZINDEX_SHOT_HANDLER_LAYOUT);
        this.attachChild(shotHandlerLayout);
        this.registerTouchArea(shotHandlerLayout);
        this.sortChildren();

    }



    public EffectManager getEffectManager() {
        return effectManager;
    }

    public int getClearingRate() {
        return clearingRate;
    }

    public PlayerID getPlayerID() {
        return playerID;
    }

    public void setClearingRate(int clearingRate) {
        this.clearingRate = clearingRate;
    }



    /*@Override
    public abstract boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent);*/




    final protected void onTickUpdateWithClearing(float pSecondsElapsed) {
        ticksFromLastCleaning++;
        if (ticksFromLastCleaning >= clearingRate) {
            shotHandlerLayout.onClearing();
            onClearing();
            ticksFromLastCleaning=0;
        }
        effectManager.onUpdate(pSecondsElapsed);
        onTickUpdate();
    }

    abstract protected void onClearing();

    abstract protected void onTickUpdate();
}
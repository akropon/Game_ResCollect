package com.akropon.game_rescollect.game.miningPart;

import com.akropon.game_rescollect.management.Constants;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by akropon on 17.09.2017.
 */

public class EffectManager {
    CrystalScene crystalScene;

    boolean freeze_isFreezed;
    float freeze_time_left;
    Sprite freeze_screen_sprite;

    float charge_current;
    int charge_max;
    float charge_chargingPerSecond;


    public EffectManager(CrystalScene crystalScene) {
        this.crystalScene = crystalScene;

        freeze_screen_sprite = new Sprite(0,0, ResourceManager.rGame_freeze_screen_pict, ResourceManager.vbom);
        freeze_screen_sprite.setWidth(ScreenManager.getScreenWidth());
        freeze_screen_sprite.setHeight(ScreenManager.getMiningScreenHeight());
        freeze_screen_sprite.setZIndex(Constants.GAME_ZINDEX_EFFECT_FREEZE);
        freeze_isFreezed = false;

        charge_max = 10;
        charge_current = 10;
        charge_chargingPerSecond = 2;
    }



    public void onUpdate(float pTimeElapsed) {
        if (freeze_isFreezed) {
            freeze_time_left -= pTimeElapsed;
            if (freeze_time_left <= 0) { // defrosted
                freeze_isFreezed = false;
                if (freeze_screen_sprite.getParent() != null)
                    freeze_screen_sprite.detachSelf();
            }
        }

        charge_current += pTimeElapsed*charge_chargingPerSecond;
        if (charge_current > charge_max)
            charge_current = charge_max;
    }



    public boolean isFreezed() {
        return freeze_isFreezed;
    }

    public void setFreezed(float seconds) {
        freeze_time_left = seconds;
        freeze_isFreezed = true;
        if (freeze_screen_sprite.getParent() == null) {
            crystalScene.attachChild(freeze_screen_sprite);
            crystalScene.sortChildren();
        }

    }


    public float getCharge_current() {
        return charge_current;
    }

    public int getCharge_max() {
        return charge_max;
    }

    public float getCharge_chargingPerSecond() {
        return charge_chargingPerSecond;
    }

    public void setCharge_current(float charge_current) {
        this.charge_current = charge_current;
    }

    public void setCharge_max(int charge_max) {
        this.charge_max = charge_max;
    }

    public void setCharge_chargingPerSecond(float charge_chargingPerSecond) {
        this.charge_chargingPerSecond = charge_chargingPerSecond;
    }

    public boolean charge_containsEnergy(float neededEnergy) {
        return neededEnergy <= charge_current;
    }

    public boolean charge_tryToSpendEnergy(float energy) {
        if (charge_containsEnergy(energy)) {
            charge_current -= energy;
            return true;
        } else
            return false;
    }

    public float charge_getRelative() {
        return charge_current / charge_max;
    }








    public void dispose() {
        // TODO
    }

}

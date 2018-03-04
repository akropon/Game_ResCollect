package com.akropon.game_rescollect.game.miningPart;

import android.util.Log;

import com.akropon.game_rescollect.BuildConfig;
import com.akropon.game_rescollect.game.objects.Cross;
import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.game.util.MortalParticlesGenerator;
import com.akropon.game_rescollect.management.Constants;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.game.util.CrystalType;
import com.akropon.game_rescollect.util.ICanBeDead;
import com.akropon.game_rescollect.game.util.ICrystalDestroyedHandler;
import com.akropon.game_rescollect.util.PlayerID;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import java.util.Random;

/**
 * Created by akropon on 16.09.2017.
 */

public class PlayerCrystalScene extends CrystalScene {

    Random random;

    public PlayerCrystalScene(PlayerID playerID, long randomSeed, ICrystalDestroyedHandler crystalDestroyedCallback) {
        super(playerID, crystalDestroyedCallback);
        random = new Random(randomSeed);
        this.crystalDestroyedCallback = crystalDestroyedCallback;


        //setOnAreaTouchTraversalBackToFront();
        //setTouchAreaBindingOnActionDownEnabled(true);
    }

    /*@Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {

            final Cross cross = new Cross(0, 0
                    , ResourceManager.rGame_cross_pict, ResourceManager.vbom, 0.3f);
            cross.setScale(ScreenManager.getScaleMultiplier(
                    cross.getWidth(), 0.4f, ScreenManager.getScreenWidth()));
            cross.setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
            cross.setZIndex(Constants.GAME_ZINDEX_CROSS);
            pScene.attachChild(cross);
            pScene.sortChildren();
        }

        return false;
    }*/


    @Override
    protected void onClearing() {
        IEntity child;

        for (int i = 0; i < getChildCount(); i++) {
            child = getChildByIndex(i);
            if (child instanceof ICanBeDead) {
                if (((ICanBeDead) child).isDead()) {
                    if (child instanceof Crystal) {
                        unregisterTouchArea((Crystal) child);
                    }
                    detachChild(child);
                    child.dispose();
                    i--;
                    continue;
                }
            }
        }

        if (BuildConfig.DEBUG)
            Log.d("akropon-debug", "PlayerScene ("+getPlayerID()
                    +") childCount = " + getChildCount());
    }

    @Override
    protected void onTickUpdate() {
        if (effectManager.isFreezed() == false) {
            if (random.nextFloat() < Constants.GAME_SPAWN_RATE_CRYSTAL_SIMPLE)
                spawnCrystalSimple();

            if (random.nextFloat() < Constants.GAME_SPAWN_RATE_CRYSTAL_FREEZE)
                spawnCrystalFreeze();


            if (random.nextFloat() < Constants.GAME_SPAWN_RATE_CRYSTAL_RICH)
                spawnCrystalRich();
        }


    }

    public void onCrystalTouched(Crystal crystal, TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
            if (effectManager.isFreezed() == false) {
                if (crystal.isDead() == false) {
                    crystal.setDead(true);

                    ResourceManager.sGame_glassBreaking.play();

                    MortalParticlesGenerator.createBreakingFragmentsFromCrystal(this, crystal, random);

                    crystalDestroyedCallback.onCrystalDestroyed(playerID, crystal);
                }
            }
        }
    }



    /*private void createFragments(Crystal crystal, TouchEvent pSceneTouchEvent,
                                 float pTouchAreaLocalX, float pTouchAreaLocalY) {


        int numberOfFragments = 10;
        ITextureRegion textureRegion = ResourceManager.rGame_crystal_yellow_pict;

        switch (crystal.getCrystalType()) {
            case SIMPLE:
                break;
            case FREEZE:
                textureRegion = ResourceManager.rGame_crystal_blue_pict;
                numberOfFragments = 10;
                break;
            case RICH:
                textureRegion = ResourceManager.rGame_crystal_yellow_pict;
                numberOfFragments = 50;
                break;
        }

        float lifetimeAverage = 0.7f;
        TemporarySprite[] fragments = new TemporarySprite[numberOfFragments];
        for (int i = 0; i < numberOfFragments; i++) {
            fragments[i] = new TemporarySprite(
                    pSceneTouchEvent.getX(),
                    pSceneTouchEvent.getY(),
                    textureRegion,
                    ResourceManager.vbom,
                    lifetimeAverage);
            fragments[i].setScale(ScreenManager.getScaleMultiplier(
                    fragments[i].getWidth(), crystal.getWidthScaled() / 2 + 1)); // "+1" to be more than 0
            fragments[i].registerEntityModifier(new MoveModifier(lifetimeAverage,
                    crystal.getCenterX() - fragments[i].getWidth() / 2,
                    crystal.getCenterX() - fragments[i].getWidth() / 2
                            + 10 * crystal.getWidthScaled() * (random.nextFloat() - 0.5f),
                    crystal.getCenterY() - fragments[i].getHeight() / 2,
                    crystal.getCenterY() - fragments[i].getHeight() / 2
                            + 10 * crystal.getHeightScaled() * (random.nextFloat() - 0.5f)));
            fragments[i].registerEntityModifier(new AlphaModifier(lifetimeAverage, 1, 0));
            fragments[i].registerEntityModifier(new RotationModifier(lifetimeAverage,
                    0, 40 * 2 * (float) Math.PI * (random.nextFloat() - 0.5f)));
            this.attachChild(fragments[i]);
        }
        this.sortChildren();

    }*/

    private void spawnCrystalSimple() {
        Crystal crystal = new Crystal(0, 0, ResourceManager.rGame_crystal_yellow_pict,
                ResourceManager.vbom, this, CrystalType.SIMPLE) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    /*boolean superReturn =  super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    onCrystalTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    return superReturn;*/
                onCrystalTouched(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                //return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return false;
            }
        };

        float xPos = 0;
        float yPos = 0;
        float xVelocity = 0;
        float yVelocity = 0;
        int decision = random.nextInt(2);
        float pseudoScale = 0.07f + 0.06f * random.nextFloat();
        //float pseudoScale = 0.2f + 0.06f * random.nextFloat(); //ODO DEBUG

        crystal.setScale(ScreenManager.getScaleMultiplier(crystal.getWidth(), pseudoScale,
                ScreenManager.getMiningScreenWidth()));

        switch (decision) {
            case 0: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = -crystal.getWidthScaled();
                xVelocity = (0.5f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                //xVelocity = (0.5f + 0f * random.nextFloat()) * ScreenManager.getScreenWidth(); //ODO DEBUG
                yVelocity = 0;
            }
            break;
            case 1: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = ScreenManager.getScreenWidth() + crystal.getWidthScaled();
                xVelocity = -(0.5f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                //xVelocity = -(0.5f + 0f * random.nextFloat()) * ScreenManager.getScreenWidth(); //ODO DEBUG
                yVelocity = 0;
            }
            break;
        }
        crystal.setPosition(xPos - crystal.getWidth() / 2, yPos - crystal.getHeight() / 2);

        crystal.mPhysicsHandler.setVelocity(xVelocity, yVelocity);

        attachChild(crystal);
        registerTouchArea(crystal);
    }

    private void spawnCrystalFreeze() {
        Crystal crystal = new Crystal(0, 0, ResourceManager.rGame_crystal_blue_pict,
                ResourceManager.vbom, this, CrystalType.FREEZE) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    /*boolean superReturn =  super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    onCrystalTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    return superReturn;*/
                onCrystalTouched(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };

        float xPos = 0;
        float yPos = 0;
        float xVelocity = 0;
        float yVelocity = 0;
        int decision = random.nextInt(2);
        float pseudoScale = 0.06f + 0.05f * random.nextFloat();

        crystal.setScale(ScreenManager.getScaleMultiplier(crystal.getWidth(), pseudoScale,
                ScreenManager.getMiningScreenWidth()));

        switch (decision) {
            case 0: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = -crystal.getWidthScaled();
                xVelocity = (1.0f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                yVelocity = 0;
            }
            break;
            case 1: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = ScreenManager.getScreenWidth() + crystal.getWidthScaled();
                xVelocity = -(1.0f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                yVelocity = 0;
            }
            break;
        }
        crystal.setPosition(xPos - crystal.getWidth() / 2, yPos - crystal.getHeight() / 2);

        crystal.mPhysicsHandler.setVelocity(xVelocity, yVelocity);

        attachChild(crystal);
        registerTouchArea(crystal);
    }

    private void spawnCrystalRich() {
        Crystal crystal = new Crystal(0, 0, ResourceManager.rGame_crystal_green_pict,
                ResourceManager.vbom, this, CrystalType.RICH) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    /*boolean superReturn =  super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    onCrystalTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    return superReturn;*/
                onCrystalTouched(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };

        float xPos = 0;
        float yPos = 0;
        float xVelocity = 0;
        float yVelocity = 0;
        int decision = random.nextInt(2);
        float pseudoScale = 0.05f + 0.04f * random.nextFloat();

        crystal.setScale(ScreenManager.getScaleMultiplier(crystal.getWidth(), pseudoScale,
                ScreenManager.getMiningScreenWidth()));

        switch (decision) {
            case 0: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = -crystal.getWidthScaled();
                xVelocity = (1.0f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                yVelocity = 0;
            }
            break;
            case 1: {
                yPos = crystal.getHeightScaled() / 2 + random.nextFloat()
                        * (ScreenManager.getMiningScreenHeight() - crystal.getHeightScaled());
                xPos = ScreenManager.getScreenWidth() + crystal.getWidthScaled();
                xVelocity = -(1.0f + 1f * random.nextFloat()) * ScreenManager.getScreenWidth();
                yVelocity = 0;
            }
            break;
        }
        crystal.setPosition(xPos - crystal.getWidth() / 2, yPos - crystal.getHeight() / 2);

        crystal.mPhysicsHandler.setVelocity(xVelocity, yVelocity);

        attachChild(crystal);
        registerTouchArea(crystal);
    }


}

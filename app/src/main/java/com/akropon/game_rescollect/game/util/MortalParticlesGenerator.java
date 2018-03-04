package com.akropon.game_rescollect.game.util;

import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.game.objects.TemporarySprite;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

import java.util.Random;

/**
 * Created by akropon on 17.09.2017.
 */

public class MortalParticlesGenerator {

    public static void createBreakingFragmentsFromCrystal(Scene scene, Crystal crystal, Random random) {
        int numberOfFragments = 1;
        ITextureRegion textureRegion = ResourceManager.rGame_crystal_white_pict;

        switch (crystal.getCrystalType()) {
            case SIMPLE:
                numberOfFragments = 5;
                textureRegion = ResourceManager.rGame_crystal_yellow_pict;
                break;
            case FREEZE:
                textureRegion = ResourceManager.rGame_crystal_blue_pict;
                numberOfFragments = 5;
                break;
            case RICH:
                textureRegion = ResourceManager.rGame_crystal_green_pict;
                numberOfFragments = 10;
                break;
        }

        float lifetimeAverage = 0.7f;
        TemporarySprite[] fragments = new TemporarySprite[numberOfFragments];
        for (int i = 0; i < numberOfFragments; i++) {
            fragments[i] = new TemporarySprite(
                    crystal.getCenterX(),
                    crystal.getCenterY(),
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
            scene.attachChild(fragments[i]);
        }
        scene.sortChildren();
    }


    /*private void createBreakingFragmentsFromRectZoneWithFrowingAlpha(
            Scene scene,
            long randomSeed,
            float zoneX,
            float zoneY,
            float zoneWidth,
            float zoneHeight,
            TextureRegion[] fragmentTextureRegions,
            int zIndex,
            int minNumberOfFragments,
            int maxNumberOfFragments,
            int minSideLength,
            int maxSideLength,
            float minVelocity,
            float maxVelocity,
            float minLifeTime,
            float maxLifeTime,
            float minRotateCorner,
            float maxRotateCorner) {
        Random random = new Random(randomSeed);

        int numberOfFragments = minNumberOfFragments + random.nextInt(maxNumberOfFragments-minNumberOfFragments);

        TemporarySprite[] fragments = new TemporarySprite[numberOfFragments];
        for (int i = 0; i < numberOfFragments; i++) {
            fragments[i] = new TemporarySprite(
                    zoneX + zoneWidth*random.nextFloat(),
                    zoneY + zoneHeight*random.nextFloat(),
                    fragmentTextureRegions[random.nextInt(fragmentTextureRegions.length)],
                    ResourceManager.vbom,
                    minLifeTime + (maxLifeTime-minLifeTime)*random.nextFloat());
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

}

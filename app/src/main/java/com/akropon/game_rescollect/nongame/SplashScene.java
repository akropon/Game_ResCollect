package com.akropon.game_rescollect.nongame;

import android.provider.Settings;

import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;


/**
 * Created by akropon on 03.09.2017.
 */

public class SplashScene extends Scene {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private Sprite splash;

    // ===========================================================
    // Constructors
    // ===========================================================

    public SplashScene() {
        splash = new Sprite(0, 0, ResourceManager.rSplash_pict, ResourceManager.vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        splash.setScale(ScreenManager.getScaleMultiplier(
                splash.getWidth(), 0.9f, ScreenManager.getScreenWidth()));
        splash.setPosition((GlobalManager.getScreenWidth() - splash.getWidth()) / 2,
                (GlobalManager.getScreenHeight() - splash.getHeight()) / 2);
        attachChild(splash);
    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================



    // ===========================================================
    // Methods
    // ===========================================================


    public void disposeScene() {
        splash.detachSelf();
        splash.dispose();
        //this.detachSelf();
        this.dispose();
    }



    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
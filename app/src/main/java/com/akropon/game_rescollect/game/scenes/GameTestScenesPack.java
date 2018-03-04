package com.akropon.game_rescollect.game.scenes;

import android.util.Log;

import com.akropon.game_rescollect.engines.TripleTripleEngine;
import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

/**
 * Created by akropon on 11.09.2017.
 */

public class GameTestScenesPack implements IOnSceneTouchListener{

    Scene scene1;
    Scene scene2;
    Scene scene3;

    HUD hud;
    FPSCounter fpsCounter;
    IUpdateHandler fpsUpdateHandler;
    Text fpsText;

    public GameTestScenesPack() {
        createScene1();
        createScene2();
        createScene3();
        createHUD();
        createFPSUpdater();
    }

    private void createScene1() {
        scene1 = new Scene();
        scene1.setBackground(new Background(1,0,0));
        scene1.setOnSceneTouchListener(this);
    }

    private void createScene2() {
        scene2 = new Scene();
        scene2.setBackground(new Background(0,1,0));
        scene2.setOnSceneTouchListener(this);
    }

    private void createScene3() {
        scene3 = new Scene();
        scene3.setBackground(new Background(0,0,1));
        scene3.setOnSceneTouchListener(this);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        final Rectangle coloredRect = new Rectangle(pSceneTouchEvent.getX(),
                pSceneTouchEvent.getY(), 10, 10, ResourceManager.vbom);
        coloredRect.setColor(1, 1, 0.8f);
        pScene.attachChild(coloredRect);

        return true;
    }

    private void createHUD() {
        hud = new HUD();

        fpsText = new Text(30, 30, ResourceManager.fGame_font, "FPS: 0123456789",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        fpsText.setText("FPS: 0");
        fpsText.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(fpsText);
    }

    private void createFPSUpdater() {

        fpsCounter = new FPSCounter();
        GlobalManager.getTTEngine().registerUpdateHandler(fpsCounter);

        fpsUpdateHandler = new IUpdateHandler() {

            @Override
            public void onUpdate(float pSecondsElapsed) {
                fpsText.setText("FPS: " + fpsCounter.getFPS());
                Log.d("akropon-debug", "1frame");
            }

            @Override
            public void reset() {}
        };
    }

    public void connectToEngine() {
        TripleTripleEngine engine = GlobalManager.getTTEngine();

        engine.setMode(TripleTripleEngine.MODE.THREE);

        engine.setFirstScene(scene1);
        engine.setSecondScene(scene2);
        engine.setThirdScene(scene3);

        engine.getFirstCamera().setHUD(hud);

        engine.registerUpdateHandler(fpsUpdateHandler);
    }

    public void disconnectFromEngine(){
        TripleTripleEngine engine = GlobalManager.getTTEngine();

        engine.unregisterUpdateHandler(fpsUpdateHandler);

        engine.getFirstCamera().setHUD(null);
        engine.getSecondCamera().setHUD(null);

        engine.setFirstScene(null);
        engine.setSecondScene(null);
        engine.setThirdScene(null);
    }

    public void disposeAll() {
        GlobalManager.getTTEngine().unregisterUpdateHandler(fpsUpdateHandler);
        fpsText.dispose();
        scene1.dispose();
        scene2.dispose();
        scene3.dispose();
    }
}

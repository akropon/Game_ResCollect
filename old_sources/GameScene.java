package com.akropon.game_rescollect.game;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.splitscreen.DoubleSceneSplitScreenEngine;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

/**
 * Created by akropon on 04.09.2017.
 */

public class GameScene extends BaseScene {
    private static final float WIDTH   = SceneManager.WIDTH;
    private static final float HEIGHT   = SceneManager.HEIGHT;
    private static final float HEIGHT_EVENT_PART_TOP    = 0.05f * SceneManager.HEIGHT;
    private static final float HEIGHT_EVENT_PART_BOTTOM = 0.45f * SceneManager.HEIGHT;
    private static final float HEIGHT_EVENT_PART_DIFF = HEIGHT_EVENT_PART_BOTTOM-HEIGHT_EVENT_PART_TOP;
    private static final float HEIGHT_RES_PART_TOP      = 0.55f * SceneManager.HEIGHT;
    private static final float HEIGHT_RES_PART_BOTTOM   = 1.00f * SceneManager.HEIGHT;
    private static final float HEIGHT_RES_PART_DIFF   = HEIGHT_RES_PART_BOTTOM-HEIGHT_RES_PART_TOP;


    private Scene scene;

    private Text fpsText;
    private Text scoreText;

    private FPSCounter fpsCounter;
    private IUpdateHandler fpsUpdateHandler;

    private HUD gameHUD;

    /*-v-LAYERS-v-*/
    private Entity layer_deviderEventFromResPart;
    private Entity layer_eventPart_background;
    private Entity layer_resPart_resources;
    private Entity layer_resPart_background;
    /*-^-LAYERS-^-*/



    @Override
    public void createScene() {
        //setBackground(new Background(Color.BLUE));
        createAndPrepareLayers();
        createHUD();
        createFPSUpdater();

        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                GameScene.this.onSceneTouch(pScene, pSceneTouchEvent);
                return true;
            }
        });
    }



    @Override
    public void onBackKeyPressed()
    {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        engine.unregisterUpdateHandler(fpsCounter);
        engine.unregisterUpdateHandler(fpsUpdateHandler);
        //camera.setCenter(400, 240);

        fpsCounter = null;
        fpsUpdateHandler = null;

        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }




    private void createAndPrepareLayers() {
        layer_deviderEventFromResPart = new Entity();
        layer_eventPart_background    = new Entity();
        layer_resPart_resources       = new Entity();
        layer_resPart_background      = new Entity();

        layer_deviderEventFromResPart.setZIndex(50);
        layer_eventPart_background   .setZIndex(40);
        layer_resPart_resources      .setZIndex(30);
        layer_resPart_background     .setZIndex(20);

        attachChild(layer_deviderEventFromResPart);
        attachChild(layer_eventPart_background   );
        attachChild(layer_resPart_resources      );
        attachChild(layer_resPart_background     );

        sortChildren();

        DoubleSceneSplitScreenEngine d;

        Rectangle rect = new Rectangle(0, HEIGHT_RES_PART_TOP, WIDTH, HEIGHT_RES_PART_BOTTOM, vbom);
        rect.setColor(0, 0, 1);
        layer_resPart_background.attachChild(rect);

        rect = new Rectangle(0, HEIGHT_EVENT_PART_BOTTOM, WIDTH, HEIGHT_RES_PART_TOP-HEIGHT_EVENT_PART_BOTTOM, vbom);
        rect.setColor(0.5f, 0.2f, 0.9f);
        layer_deviderEventFromResPart.attachChild(rect);

        rect = new Rectangle(0, HEIGHT_EVENT_PART_TOP, WIDTH, HEIGHT_EVENT_PART_DIFF, vbom);
        rect.setColor(0, 1, 0);
        layer_eventPart_background.attachChild(rect);
    }

    private void onSceneTouch(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {
            Sprite sprite = new Sprite(0, 0,
                    ResourcesManager.getInstance().explosion_region, vbom);
            sprite.setScale(0.5f, 0.5f);
            sprite.setPosition(pSceneTouchEvent.getX()-sprite.getWidth()/2,
                    pSceneTouchEvent.getY()-sprite.getHeight()/2);

            if (pSceneTouchEvent.getY() > HEIGHT_RES_PART_TOP) {
                layer_resPart_resources.attachChild(sprite);
                Log.d("akropon-debug", "touch layer res");
            }
            else if (pSceneTouchEvent.getY() > HEIGHT_EVENT_PART_BOTTOM) {
                layer_deviderEventFromResPart.attachChild(sprite);
                Log.d("akropon-debug", "touch layer dev");
            }
            else {
                layer_eventPart_background.attachChild(sprite);
                Log.d("akropon-debug", "touch layer eve");
            }
        }
    }

    private void createHUD() {
        gameHUD = new HUD();

        // CREATE SCORE TEXT
        fpsText = new Text(20, 20, resourcesManager.font, "FPS: 0123456789",
                new TextOptions(HorizontalAlign.LEFT), vbom);
        fpsText.setText("FPS: 0");
        scoreText = new Text(20, 40+fpsText.getHeight(), resourcesManager.font, "Score: 0123456789",
                new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setText("Score: 0");

        gameHUD.attachChild(fpsText);
        gameHUD.attachChild(scoreText);

        camera.setHUD(gameHUD);
    }

    private void createFPSUpdater() {

        fpsCounter = new FPSCounter();
        this.engine.registerUpdateHandler(fpsCounter);

        fpsUpdateHandler = new IUpdateHandler() {
            int counter = 0;

            @Override
            public void onUpdate(float pSecondsElapsed) {
                fpsText.setText("FPS: " + fpsCounter.getFPS());
                counter++;
                if (counter==240) {
                    Log.d("akropon_debug", "240 tick");
                    counter=0;
                }
            }

            @Override
            public void reset() {}
        };

        this.engine.registerUpdateHandler(fpsUpdateHandler);
    }

}

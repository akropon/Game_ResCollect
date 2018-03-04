package com.akropon.game_rescollect.game;

import android.util.Log;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

/**
 * Created by akropon on 08.09.2017.
 */

public class ResourceScene extends Scene {

    private int height = 100;
    private int width = 100;


    public ResourceScene(int width, int height, Color color) {

        this.width = width;
        this.height = height;
        setBackground(new Background(color));
        createScene();
    }

    private void createScene() {
        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                ResourceScene.this.onSceneTouch(pScene, pSceneTouchEvent);
                return true;
            }
        });
    }

    public void disposeScene() {



    }

    private void onSceneTouch(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {
            Sprite sprite = new Sprite(0, 0,
                    ResourcesManager.getInstance().explosion_region, ResourcesManager.getInstance().vbom);
            sprite.setScale(0.5f, 0.5f);
            sprite.setPosition(pSceneTouchEvent.getX()-sprite.getWidth()/2,
                    pSceneTouchEvent.getY()-sprite.getHeight()/2);

            attachChild(sprite);
            Log.d("akropon-debug", "touch res scene");
        }
    }
}

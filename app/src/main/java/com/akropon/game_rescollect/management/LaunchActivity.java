package com.akropon.game_rescollect.management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;

public class LaunchActivity extends BaseGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalManager.init(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        return GlobalManager.getEngineOptions();
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return GlobalManager.getTTEngine();
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        //GlobalManager.onInitResourses();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        //GlobalManager.onInitSceneManager();
        pOnCreateSceneCallback.onCreateSceneFinished(null);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
        GlobalManager.launch();
    }

    @Override
    public void onBackPressed() {
        GlobalManager.onBackPressed();
    }
}

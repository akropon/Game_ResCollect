package com.akropon.game_rescollect;

import com.akropon.game_rescollect.game.GameScene;
import com.akropon.game_rescollect.game.ResourceScene;
import com.akropon.game_rescollect.game.TripleSceneSplitVerticalScreenEngine;
import com.akropon.game_rescollect.nongame.SplashScene;
import com.akropon.game_rescollect.util.SceneType;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.ui.IGameInterface;
import org.andengine.util.color.Color;


/**
 * Created by akropon on 03.09.2017.
 */

public class SceneManager {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;

    public static final float GAMESCENE_BORDER12PART = 0.3f;
    public static final float GAMESCENE_BORDER23PART = 0.5f;

    public static final int GAMESCENE_BORDER12ABS = (int)(HEIGHT * GAMESCENE_BORDER12PART);
    public static final int GAMESCENE_BORDER23ABS = (int)(HEIGHT * GAMESCENE_BORDER23PART);


    private static final SceneManager INSTANCE = new SceneManager();


    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;

    private SceneType currentSceneType = SceneType.SCENE_SPLASH;

    private BaseScene currentScene;

    private Engine engine = ResourcesManager.getInstance().engine;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public BaseScene getCurrentScene() {
        return currentScene;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public void setScene(BaseScene scene) {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setScene(SceneType sceneType) {
        switch (sceneType) {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            default:
                break;
        }
    }

    public void createMenuScene()
    {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        setScene(menuScene);
        disposeSplashScene();
    }

    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }

    // menu --> (loading...) --> game
    public void loadGameScene(final Engine mEngine)
    {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

    // game --> (loading...) --> menu
    public void loadMenuScene(final Engine mEngine)
    {
        setScene(loadingScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameTextures();
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                setScene(menuScene);
            }
        }));
    }

    public void switchMenuToGame() {
        /*prepare everything for game scene*/
        Camera camera1 = new Camera(0, 0, WIDTH, GAMESCENE_BORDER12ABS);
        Camera camera2 = new Camera(0, 0, WIDTH, GAMESCENE_BORDER23ABS-GAMESCENE_BORDER12ABS);
        Camera camera3 = new Camera(0, 0, WIDTH, HEIGHT-GAMESCENE_BORDER23ABS);
        ResourceScene scene1 = new ResourceScene(WIDTH, GAMESCENE_BORDER12ABS, Color.RED);
        ResourceScene scene2 = new ResourceScene(WIDTH, GAMESCENE_BORDER12ABS, Color.GREEN);
        ResourceScene scene3 = new ResourceScene(WIDTH, GAMESCENE_BORDER12ABS, Color.BLUE);
        scene1.setUserData(camera1);
        scene2.setUserData(camera2);
        scene3.setUserData(camera3);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(SceneManager.WIDTH, SceneManager.HEIGHT), camera1);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        TripleSceneSplitVerticalScreenEngine gameEngine = new TripleSceneSplitVerticalScreenEngine(
                engineOptions, camera2, camera3, GAMESCENE_BORDER12PART, GAMESCENE_BORDER23PART);

        Engine menuEngine = ResourcesManager.getInstance().activity.getEngine();

        /*switch*/
        ResourcesManager.getInstance().activity.


        /*unload menu resources*/

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}

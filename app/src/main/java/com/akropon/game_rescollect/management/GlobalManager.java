package com.akropon.game_rescollect.management;

import android.transition.Scene;
import android.widget.Toast;

import com.akropon.game_rescollect.engines.TripleTripleEngine;
import com.akropon.game_rescollect.nongame.GameMenuScene;
import com.akropon.game_rescollect.util.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by akropon on 10.09.2017.
 */

public class GlobalManager {
    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Fields
    // ===========================================================

    private static LaunchActivity launchActivity;
    private static int screenWidth;
    private static int screenHeight;

    private static int fps;
    private static TripleTripleEngine ttEngine;
    private static EngineOptions engineOptions;
    private static VertexBufferObjectManager vbom;

    private static SceneType activeScene;

    // ===========================================================
    // Constructors (or smth like)
    // ===========================================================

    public static void init(LaunchActivity _launchActivity) {
        launchActivity = _launchActivity;
        fps = Constants.INITIAL_FPS;

        ScreenManager.init(launchActivity);
        screenWidth = ScreenManager.getScreenWidth();
        screenHeight = ScreenManager.getScreenHeight();

        Camera camera1 = new Camera(0, 0, screenWidth, screenHeight);
        Camera camera2 = new Camera(0, 0, screenWidth, screenHeight);
        Camera camera3 = new Camera(0, 0, screenWidth, screenHeight);

        engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(screenWidth, screenHeight), camera1);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true).setTouchEventIntervalMilliseconds(0);
        engineOptions.getAudioOptions().setNeedsSound(true);

        if(MultiTouch.isSupported(launchActivity)) {
            if(MultiTouch.isSupportedDistinct(launchActivity)) {
                Toast.makeText(launchActivity, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(launchActivity, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(launchActivity, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
        }

        ttEngine = new TripleTripleEngine(engineOptions, camera2, camera3, fps);
        vbom = ttEngine.getVertexBufferObjectManager();

        activeScene = SceneType.NONE;
    }

    /*public static void onInitResourses() {

    }

    public static void onInitSceneManager() {

    }*/


    // ===========================================================
    // Getters
    // ===========================================================

    public static LaunchActivity getLaunchActivity() {
        return launchActivity;
    }

    public static VertexBufferObjectManager getVBOM() {
        return vbom;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getFPS() {
        return fps;
    }

    public static TripleTripleEngine getTTEngine() {
        return ttEngine;
    }

    public static EngineOptions getEngineOptions() {
        return engineOptions;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public static void launch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                in_launch();
            }
        });
        thread.start();
    }

    public static void onBackPressed() {
        switch (activeScene) {

            case MENU: {
                in_exitAppFromMenu();
            }
            break;

            case GAME: {
                new Thread(new Runnable() {
                    public void run() {
                        in_gotoMenuFromGameWithResults();
                    }
                }).start();
            }
            break;
        }
    }

    public static void onBtnClickedInMenu(int btnID) {
        switch (btnID) {
            case GameMenuScene.MENU_BTN_PLAY_ID: {
                new Thread(new Runnable() {
                    public void run() {
                        in_gotoGameFromMenu();
                    }
                }).start();
            }
            break;

            case GameMenuScene.MENU_BTN_EXIT_ID: {
                in_exitAppFromMenu();
            }
            break;
        }
    }


    private static void CloseDisposeExit() {


    }

    // ===========================================================
    // Inner functionality
    // ===========================================================

    private static void in_launch() {
        ResourceManager.init(launchActivity);
        SceneManager.init();

        ResourceManager.createSplash();
        ResourceManager.loadSplash();
        SceneManager.createSplash();
        ttEngine.setMode(TripleTripleEngine.MODE.ONE);
        ttEngine.setFirstScene(SceneManager.getSplashScene());
        activeScene = SceneType.SPLASH;
        ttEngine.resumeDrawingAndUpdating();

        long moment = TimeManager.getMillisNow();

        ResourceManager.createMenu();
        ResourceManager.loadMenu();
        SceneManager.createGameMenuScene();
        ResourceManager.createLoadingScreen();
        ResourceManager.loadLoadingScreen();
        SceneManager.createLoadingScreenScene();

        TimeManager.sleepUpToIntervalEndIfNeed(moment, 500);

        ttEngine.stopDrawingAndUpdating();
        ttEngine.setFirstScene(SceneManager.getGameMenuScene());
        activeScene = SceneType.MENU;
        ttEngine.resumeDrawingAndUpdating();

        SceneManager.destroySplash();
        ResourceManager.unloadSplash();
        ResourceManager.destroySplash();
    }

    private static void in_exitAppFromMenu() {
        ttEngine.stopDrawingAndUpdating();
        ttEngine.setFirstScene(null);
        activeScene = SceneType.NONE;
        SceneManager.destroyGameMenuScene();
        ResourceManager.unloadMenu();
        ResourceManager.destroyMenu();
        SceneManager.destroyLoadingScreenScene();
        ResourceManager.unloadLoadingScreen();
        ResourceManager.destroyLoadingScreen();
        System.exit(0);
    }

    private static void in_gotoGameFromMenu() {
        ttEngine.stopDrawingAndUpdating();
        ttEngine.setFirstScene(SceneManager.getLoadingScreenScene());
        activeScene = SceneType.LOADING;
        ttEngine.resumeDrawingAndUpdating();

        long moment = TimeManager.getMillisNow();

        SceneManager.destroyGameMenuScene();
        ResourceManager.unloadMenu();
        ResourceManager.destroyMenu();

        ResourceManager.createGame();
        ResourceManager.loadGame();
        SceneManager.createGameManager();

        TimeManager.sleepUpToIntervalEndIfNeed(moment, 500);

        ttEngine.stopDrawingAndUpdating();
        SceneManager.getGameManager().connectToEngine();
        activeScene = SceneType.GAME;
        ttEngine.resumeDrawingAndUpdating();
    }

    public static void in_gotoMenuFromGame() {
        ttEngine.stopDrawingAndUpdating();
        SceneManager.getGameManager().disconnectFromEngine();
        ttEngine.getFirstCamera().set(0,0,
                ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight());
        ttEngine.setMode(TripleTripleEngine.MODE.ONE);
        ttEngine.setFirstScene(SceneManager.getLoadingScreenScene());
        //ttEngine.setSecondScene(SceneManager.getLoadingScreenScene());
        //ttEngine.getFirstCamera().set(0,0,screenWidth,screenHeight);
        activeScene = SceneType.LOADING;
        ttEngine.resumeDrawingAndUpdating();

        long moment = TimeManager.getMillisNow();

        SceneManager.destroyGameManager();
        ResourceManager.unloadGame();
        ResourceManager.destroyGame();

        ResourceManager.createMenu();
        ResourceManager.loadMenu();
        SceneManager.createGameMenuScene();

        TimeManager.sleepUpToIntervalEndIfNeed(moment, 500);

        ttEngine.stopDrawingAndUpdating();
        ttEngine.setFirstScene(SceneManager.getGameMenuScene());
        //ttEngine.setSecondScene(SceneManager.getGameMenuScene());
        activeScene = SceneType.MENU;
        ttEngine.resumeDrawingAndUpdating();
    }

    public static void in_gotoMenuFromGameWithResults() {
        ttEngine.stopDrawingAndUpdating();
        SceneManager.createResultsScene();
        SceneManager.getGameManager().disconnectFromEngine();
        ttEngine.getFirstCamera().set(0,0,
                ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight());
        ttEngine.setMode(TripleTripleEngine.MODE.ONE);
        ttEngine.setFirstScene(SceneManager.getResultsScene());
        //ttEngine.setSecondScene(SceneManager.getLoadingScreenScene());
        //ttEngine.getFirstCamera().set(0,0,screenWidth,screenHeight);
        activeScene = SceneType.RESULTS;
        ttEngine.resumeDrawingAndUpdating();

        long moment = TimeManager.getMillisNow();

        TimeManager.sleepUpToIntervalEndIfNeed(moment, 2000);
        ttEngine.setFirstScene(SceneManager.getLoadingScreenScene());

        moment = TimeManager.getMillisNow();

        SceneManager.destroyResultsScene();

        SceneManager.destroyGameManager();
        ResourceManager.unloadGame();
        ResourceManager.destroyGame();

        ResourceManager.createMenu();
        ResourceManager.loadMenu();
        SceneManager.createGameMenuScene();

        TimeManager.sleepUpToIntervalEndIfNeed(moment, 500);

        ttEngine.stopDrawingAndUpdating();
        ttEngine.setFirstScene(SceneManager.getGameMenuScene());
        //ttEngine.setSecondScene(SceneManager.getGameMenuScene());
        activeScene = SceneType.MENU;
        ttEngine.resumeDrawingAndUpdating();
    }
}

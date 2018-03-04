package com.akropon.game_rescollect.management;

import com.akropon.game_rescollect.game.GameManager;
import com.akropon.game_rescollect.game.scenes.GameTestScenesPack;
import com.akropon.game_rescollect.nongame.GameMenuScene;
import com.akropon.game_rescollect.nongame.LoadingScreenScene;
import com.akropon.game_rescollect.nongame.ResultsScene;
import com.akropon.game_rescollect.nongame.SplashScene;

/**
 * Created by akropon on 10.09.2017.
 */

public class SceneManager {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static SplashScene splashScene;
    private static GameMenuScene gameMenuScene;
    private static LoadingScreenScene loadingScreenScene;
    private static GameTestScenesPack gameTestScenesPack;
    private static GameManager gameManager;
    private static ResultsScene resultsScene;

    // ===========================================================
    // Constructors (or smth like)
    // ===========================================================

    public static void init() {
        splashScene = null;
        gameMenuScene = null;

    }

    // ===========================================================
    // Getters
    // ===========================================================

    public static SplashScene getSplashScene() {
        return splashScene;
    }

    public static GameMenuScene getGameMenuScene() {
        return gameMenuScene;
    }

    public static LoadingScreenScene getLoadingScreenScene() {
        return loadingScreenScene;
    }

    public static GameTestScenesPack getGameTestScenesPack() {
        return gameTestScenesPack;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static ResultsScene getResultsScene() {
        return resultsScene;
    }

    // ===========================================================
    // Setters
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static void createSplash() {
        splashScene = new SplashScene();
    }

    public static void destroySplash() {
        splashScene.dispose();
        splashScene = null;
    }

    public static void createGameMenuScene() {
        gameMenuScene = new GameMenuScene();
    }

    public static void destroyGameMenuScene() {
        gameMenuScene.dispose();
        gameMenuScene = null;
    }

    public static void createLoadingScreenScene() {
        loadingScreenScene = new LoadingScreenScene();
    }

    public static void destroyLoadingScreenScene() {
        loadingScreenScene.dispose();
        loadingScreenScene = null;
    }

    public static void createGameTestScenesPack() {
        gameTestScenesPack = new GameTestScenesPack();
    }

    public static void destroyGameTestScenesPack() {
        gameTestScenesPack.disposeAll();
        gameTestScenesPack = null;
    }

    public static void createGameManager() {
        gameManager = new GameManager();
    }

    public static void destroyGameManager() {
        gameManager.disposeAll();
        gameManager = null;
    }


    public static void createResultsScene() {
        resultsScene = new ResultsScene();
    }

    public static void destroyResultsScene() {
        resultsScene.dispose();
        resultsScene = null;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}

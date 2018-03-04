package com.akropon.game_rescollect.game;

import android.os.SystemClock;
import android.view.MotionEvent;

import com.akropon.game_rescollect.engines.TripleTripleEngine;
import com.akropon.game_rescollect.game.miningPart.CrystalScene;
import com.akropon.game_rescollect.game.miningPart.PlayerCrystalScene;
import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.management.TimeManager;
import com.akropon.game_rescollect.util.ICanBeDead;
import com.akropon.game_rescollect.util.PlayerID;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import java.util.Random;

/**
 * Created by akropon on 13.09.2017.
 */

public class GameManager {

    private  CrystalScene scene1;
    private Scene sceneMid;
    private CrystalScene scene2;

    private HUD hud;
    private FPSCounter fpsCounter;
    private IUpdateHandler fpsUpdateHandler;
    private IUpdateHandler hudUpdateHadler;
    private Text fpsText;
    private Text score1Text;
    private Text score2Text;
    private Text charge1Text;
    private Text charge2Text;

    private int score1;
    private int score2;

    private CrystalDestroyedHandler crystalDestroyedHandler;

    public GameManager() {
        score1 = 0;
        score2 = 0;

        crystalDestroyedHandler = new CrystalDestroyedHandler(this);
        scene1 = new PlayerCrystalScene(PlayerID.P1, System.currentTimeMillis(), crystalDestroyedHandler);
        scene2 = new PlayerCrystalScene(PlayerID.P2, System.currentTimeMillis()+100L, crystalDestroyedHandler);
        createSceneMid();

        createHUD();
        createFPSUpdater();
        createHUDUpdater();
    }



    private void createSceneMid() {
        sceneMid = new Scene();
        sceneMid.setBackground(new Background(0.3f, 0.3f, 0.3f));
    }

    private void createHUD() {
        hud = new HUD();

        fpsText = new Text(30, 30, ResourceManager.fGame_font, "FPS: 0123456789",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        fpsText.setText("FPS: 0");
        fpsText.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(fpsText);

        score1Text = new Text(30, 100, ResourceManager.fGame_font, "Score (1): 0123456789",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        score1Text.setText("Score (1): 0");
        score1Text.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(score1Text);

        score2Text = new Text(30, 170, ResourceManager.fGame_font, "Score (2): 0123456789",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        score2Text.setText("Score (2): 0");
        score2Text.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(score2Text);

        charge1Text = new Text(30, 260, ResourceManager.fGame_font, "Charge (1): 0123456789%",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        charge1Text.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(charge1Text);

        charge2Text = new Text(30, 330, ResourceManager.fGame_font, "Charge (1): 0123456789%",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        charge2Text.setColor(0.2f, 0.6f, 0.8f, 1.0f);
        hud.attachChild(charge2Text);

        Text target = new Text(0, 0, ResourceManager.fGame_font, "Target:\n1000",
                new TextOptions(HorizontalAlign.LEFT), ResourceManager.vbom);
        target.setScale(ScreenManager.getScaleMultiplier(
                target.getWidth(),0.5f,ScreenManager.getScreenWidth()));
        target.setPosition(ScreenManager.getScreenWidth()/2+target.getWidthScaled()/2-target.getWidth()/2,
                ScreenManager.getEventScreenHeight()/2-target.getHeight()/2);
        target.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        hud.attachChild(target);
    }

    private void createFPSUpdater() {

        fpsCounter = new FPSCounter();
        GlobalManager.getTTEngine().registerUpdateHandler(fpsCounter);

        fpsUpdateHandler = new IUpdateHandler() {

            @Override
            public void onUpdate(float pSecondsElapsed) {
                fpsText.setText("FPS: " + fpsCounter.getFPS());
            }

            @Override
            public void reset() {
            }
        };
    }

    private void createHUDUpdater() {
        hudUpdateHadler = new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                /*charge1Text.setText("Charge (1): "+(int)(scene1.getEffectManager().getCharge_current()
                        +" / "+scene1.getEffectManager().getCharge_max()));*/
                charge1Text.setText("Charge (1): "+
                        (int)(scene1.getEffectManager().charge_getRelative()*100)+"%");
                charge2Text.setText("Charge (2): "+
                        (int)(scene2.getEffectManager().charge_getRelative()*100)+"%");


                emiteII();
                checkWinner();

            }

            @Override
            public void reset() {

            }
        };
    }

    private void checkWinner() {
        if (score1 >= 1000 || score2 >= 1000) {
            new Thread(new Runnable() {
                public void run() {
                    GlobalManager.in_gotoMenuFromGameWithResults();
                }
            }).start();
        }
    }

    private void emiteII() {
        Random random = new Random(System.nanoTime());
        if (random.nextFloat() >= 0.05f) return;
        if (random.nextFloat() < scene1.getEffectManager().charge_getRelative()) {
            IEntity child;
            Crystal crystal;

            for (int i = 0; i < scene1.getChildCount(); i++) {
                child = scene1.getChildByIndex(i);
                if (child instanceof Crystal) {
                    crystal = (Crystal)child;
                    if (crystal.isDead()) continue;
                    float x = crystal.getCenterX();
                    float y = crystal.getCenterY();

                    if (x<0 || x>ScreenManager.getScreenWidth()
                            || y<0 || y>ScreenManager.getMiningScreenHeight())
                        continue;

                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = downTime + 100;
                    // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
                    int metaState = 0;
                    MotionEvent motionEventDown = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_DOWN,
                            x,
                            y,
                            metaState
                    );
                    MotionEvent motionEventUp = MotionEvent.obtain(
                            downTime,
                            eventTime+10,
                            MotionEvent.ACTION_UP,
                            x,
                            y,
                            metaState
                    );

                    // Dispatch touch event to view
                    GlobalManager.getLaunchActivity().dispatchTouchEvent(motionEventDown);
                    GlobalManager.getLaunchActivity().dispatchTouchEvent(motionEventUp);
                    break;
                }
            }
        }
    }

    public void connectToEngine() {
        TripleTripleEngine engine = GlobalManager.getTTEngine();

        engine.setBorder12part(ScreenManager.getBorder12Part());
        engine.setBorder23part(ScreenManager.getBorder23Part());
        engine.getFirstCamera().set(0, 0,
                ScreenManager.getMiningScreenWidth(),
                ScreenManager.getMiningScreenHeight());
        engine.getSecondCamera().set(0, 0,
                ScreenManager.getEventScreenWidth(),
                ScreenManager.getEventScreenHeight());
        engine.getThirdCamera().set(0, 0,
                ScreenManager.getMiningScreenWidth(),
                ScreenManager.getMiningScreenHeight());
        engine.setMode(TripleTripleEngine.MODE.THREE);


        engine.setFirstScene(scene1);
        engine.setSecondScene(sceneMid);
        engine.setThirdScene(scene2);

        engine.getSecondCamera().setHUD(hud);

        engine.registerUpdateHandler(fpsUpdateHandler);
        engine.registerUpdateHandler(hudUpdateHadler);
    }

    public void disconnectFromEngine() {
        TripleTripleEngine engine = GlobalManager.getTTEngine();

        engine.unregisterUpdateHandler(hudUpdateHadler);
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
        sceneMid.dispose();
        scene2.dispose();
    }


    public Text getScoreText(PlayerID playerID) {
        return playerID==PlayerID.P1 ? score1Text : score2Text;
    }

    public int getScore(PlayerID playerID) {
        return playerID==PlayerID.P1 ? score1 : score2;
    }

    public CrystalScene getScene(PlayerID playerID) {
        return playerID==PlayerID.P1 ? scene1 : scene2;
    }

    public Scene getMidScene() {
        return sceneMid;
    }

    public void setScore(PlayerID playerID, int newValue) {
        if (playerID==PlayerID.P1)
            score1 = newValue;
        else
            score2 = newValue;
    }

    public void setScoreText(PlayerID playerID, String newText) {
        if (playerID==PlayerID.P1)
            score1Text.setText(newText);
        else
            score2Text.setText(newText);
    }

    public void updateScore(PlayerID playerID, int newValue) {
        if (playerID==PlayerID.P1) {
            score1 = newValue;
            score1Text.setText("Score (1): "+score1);
        }
        else {
            score2 = newValue;
            score2Text.setText("Score (2): "+score2);
        }

    }

    /*void createPartSs() {

        final CircleOutlineParticleEmitter particleEmitter =
                new CircleOutlineParticleEmitter(CAMERA_WIDTH * 0.5f,
                        CAMERA_HEIGHT * 0.5f + 20, 80);
        final SpriteParticleSystem particleSystem =
                new SpriteParticleSystem(particleEmitter, 60, 60, 360, this.mParticleTextureRegion,
                        vbom);

        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
                particleEmitter.setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                return true;
            }
        });

        particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1, 0, 0));
        particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
        particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(
                GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-2, 2, -20, -10));
        particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
        particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6));

        particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 1.0f, 2.0f));
        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0, 3, 1, 1, 0, 0.5f, 0, 0));
        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(4, 6, 1, 1, 0.5f, 1, 0, 1));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 1, 0, 1));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(5, 6, 1, 0));

        scene.attachChild(particleSystem);

        return scene;
    }*/
}

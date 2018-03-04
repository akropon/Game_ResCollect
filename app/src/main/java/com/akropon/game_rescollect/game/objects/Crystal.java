package com.akropon.game_rescollect.game.objects;

import com.akropon.game_rescollect.game.miningPart.CrystalScene;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.game.util.CrystalType;
import com.akropon.game_rescollect.util.ICanBeDead;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by akropon on 13.09.2017.
 */

public class Crystal extends Sprite implements ICanBeDead {
    CrystalScene crystalScene;
    CrystalType crystalType;
    private boolean isDead;

    public PhysicsHandler mPhysicsHandler;

    public Crystal(float pX, float pY, ITextureRegion pTextureRegion,
                   VertexBufferObjectManager pVertexBufferObjectManager,
                   CrystalScene crystalScene, final CrystalType crystalType) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.crystalScene = crystalScene;
        this.crystalType = crystalType;

        isDead = false;

        mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(mPhysicsHandler);
    }


    public CrystalType getCrystalType() {
        return crystalType;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    /*@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
            if (isDead == false || crystalScene.getEffectManager().isFreezed() == false) {
                isDead = true;
                GameManager gameManager = SceneManager.getGameManager();
                Scene parentScene = (Scene) this.getParent();

                if (parentScene == gameManager.scene1) {
                    gameManager.score1 += 10;
                    gameManager.score1Text.setText("Score (1): " + gameManager.score1);

                } else {
                    gameManager.score2 += 10;
                    gameManager.score2Text.setText("Score (2): " + gameManager.score2);
                }
            }
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }*/

    @Override
    protected void onManagedDraw(GLState pGLState, Camera pCamera) {
        if (isDead == false)
            super.onManagedDraw(pGLState, pCamera);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if (isDead == false && crystalScene.getEffectManager().isFreezed() == false) {

            super.onManagedUpdate(pSecondsElapsed);

            if (getCenterX() > ScreenManager.getScreenWidth() + getWidthScaled()
                    && mPhysicsHandler.getVelocityX() >= 0
                    || getCenterX() < -getWidthScaled()
                    && mPhysicsHandler.getVelocityX() <= 0) {
                isDead = true;
            }
        }
    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
    }

    public void setDead(boolean value) {
        isDead = value;
    }

}

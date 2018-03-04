package com.akropon.game_rescollect.game.miningPart;

import android.util.Log;

import com.akropon.game_rescollect.BuildConfig;
import com.akropon.game_rescollect.game.objects.Cross;
import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.management.Constants;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.util.ICanBeDead;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

/**
 * Created by akropon on 18.09.2017.
 */

public class ShotHandlerLayout extends Rectangle {
    CrystalScene crystalScene;

    public ShotHandlerLayout(float width, float height, CrystalScene crystalScene ) {
        super(0,0,width,height, ResourceManager.vbom);
        this.crystalScene = crystalScene;

        this.setColor(1,0,0,0.2f);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {
            if (crystalScene.getEffectManager().charge_tryToSpendEnergy(1)) {

                final Cross cross = new Cross(0, 0
                        , ResourceManager.rGame_cross_pict, ResourceManager.vbom, 0.3f);
                cross.setScale(ScreenManager.getScaleMultiplier(
                        cross.getWidth(), 0.4f, ScreenManager.getScreenWidth()));
                cross.setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                //cross.setZIndex(Constants.GAME_ZINDEX_CROSS);
                this.attachChild(cross);
                //this.sortChildren();


                return false;
            } else
                return true;
        }
        return true;
    }

    @Override
    protected void draw(GLState pGLState, Camera pCamera) {
        //super.draw(pGLState, pCamera);
    }

    @Override
    protected void preDraw(GLState pGLState, Camera pCamera) {
        //super.preDraw(pGLState, pCamera);
    }

    @Override
    protected void postDraw(GLState pGLState, Camera pCamera) {
        //super.postDraw(pGLState, pCamera);
    }

    public void onClearing() {
        IEntity child;

        for (int i = 0; i < getChildCount(); i++) {
            child = getChildByIndex(i);
            if (child instanceof ICanBeDead) {
                if (((ICanBeDead) child).isDead()) {
                    detachChild(child);
                    child.dispose();
                    i--;
                    continue;
                }
            }
        }


        if (BuildConfig.DEBUG)
            Log.d("akropon-debug", "ShotHandlerLayout ("+crystalScene.getPlayerID()
                    +") childCount = " + getChildCount());
    }
}

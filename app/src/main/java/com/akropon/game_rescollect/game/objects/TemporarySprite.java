package com.akropon.game_rescollect.game.objects;

import com.akropon.game_rescollect.util.ICanBeDead;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by akropon on 17.09.2017.
 */

public class TemporarySprite extends Sprite implements ICanBeDead {

    public float lifeTime_secs;
    public float maxLifeTime_secs;

    public TemporarySprite(float pX, float pY, ITextureRegion pTextureRegion,
                           VertexBufferObjectManager pVertexBufferObjectManager, float maxLifeTime_secs) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        this.lifeTime_secs = 0;
        this.maxLifeTime_secs = maxLifeTime_secs;
    }


    @Override
    public boolean isDead() {
        return (lifeTime_secs >= maxLifeTime_secs);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if (isDead()) return;
        super.onManagedUpdate(pSecondsElapsed);
        lifeTime_secs += pSecondsElapsed;
    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
    }


    public void setCenter(float centerX, float centerY) {
        setPosition(centerX - getWidth() / 2, centerY - getHeight() / 2);
    }

}
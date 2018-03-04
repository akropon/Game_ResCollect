package com.akropon.game_rescollect;

import com.akropon.game_rescollect.util.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by akropon on 04.09.2017.
 */

public class LoadingScene extends BaseScene {
    @Override
    public void createScene() {
        setBackground(new Background(Color.BLACK));

        Text text = new Text(SceneManager.WIDTH/2, SceneManager.HEIGHT/2,
                resourcesManager.font, "Loading...", vbom);
        text.setX( (SceneManager.WIDTH - text.getWidth()) / 2 );
        text.setY( (SceneManager.HEIGHT - text.getHeight()) / 2 );
        attachChild(text);
    }

    @Override
    public void onBackKeyPressed() {
        return;
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {

    }
}

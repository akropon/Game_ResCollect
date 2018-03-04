package com.akropon.game_rescollect.nongame;

import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by akropon on 04.09.2017.
 */

public class LoadingScreenScene extends Scene {

    private Text text;

    public LoadingScreenScene() {

        setBackground(new Background(Color.BLACK));

        text = new Text(0, 0, ResourceManager.fLoadingScreen_font,
                "Loading...", ResourceManager.vbom);
        text.setX( (GlobalManager.getScreenWidth() - text.getWidth()) / 2 );
        text.setY( (GlobalManager.getScreenHeight() - text.getHeight()) / 2 );
        text.setScale(ScreenManager.getScaleMultiplier(
                text.getWidth(), 0.8f, ScreenManager.getScreenWidth()));
        attachChild(text);
    }

    public void disposeScene() {
        text.detachSelf();
        text.dispose();
        //this.detachSelf();
        this.dispose();
    }
}

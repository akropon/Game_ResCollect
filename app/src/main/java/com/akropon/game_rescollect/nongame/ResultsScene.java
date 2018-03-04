package com.akropon.game_rescollect.nongame;

import com.akropon.game_rescollect.game.GameManager;
import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.SceneManager;
import com.akropon.game_rescollect.management.ScreenManager;
import com.akropon.game_rescollect.util.PlayerID;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by akropon on 18.09.2017.
 */

public class ResultsScene extends Scene {

    private Text text;

    public ResultsScene() {

        setBackground(new Background(Color.BLACK));

        String str = null;
        if (SceneManager.getGameManager().getScore(PlayerID.P1)
                > SceneManager.getGameManager().getScore(PlayerID.P2))
            str = "LOOOOSER!\nFUUUUUUU!!!";
            //str = "P1 win";
        else
            str = "You WIN!";
            //str = "P2 win";

        text = new Text(0, 0, ResourceManager.fLoadingScreen_font,
                str, ResourceManager.vbom);
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

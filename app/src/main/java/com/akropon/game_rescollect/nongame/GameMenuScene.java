package com.akropon.game_rescollect.nongame;

import android.graphics.Color;

import com.akropon.game_rescollect.management.GlobalManager;
import com.akropon.game_rescollect.management.ResourceManager;
import com.akropon.game_rescollect.management.ScreenManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

/**
 * Created by akropon on 11.09.2017.
 */

public class GameMenuScene extends Scene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuScene;
    public static final int MENU_BTN_PLAY_ID = 0;
    public static final int MENU_BTN_OPTIONS_ID = 1;
    public static final int MENU_BTN_EXIT_ID = 2;


    public GameMenuScene() {
        createMenuChildScene();
       // setBackground(new Background(0.4f,0.5f,0.6f));
    }



    public void disposeScene() {
        menuScene.detachSelf();
        menuScene.clearMenuItems();
        menuScene.dispose();
        //this.detachSelf();
        this.dispose();
    }



    private void createMenuChildScene() {
        menuScene = new MenuScene(GlobalManager.getTTEngine().getFirstCamera());
        //menuChildScene.setPosition(SceneManager.WIDTH / 2, SceneManager.HEIGHT / 2);
        menuScene.setPosition(0, 0);


        float scale = ScreenManager.getScaleMultiplier(ResourceManager.rMenu_btn_play_pict.getWidth(),
                0.7f, ScreenManager.getScreenWidth());

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_PLAY_ID, ResourceManager.rMenu_btn_play_pict, ResourceManager.vbom),
                1.2f*scale, scale);
        /*final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_OPTIONS_ID, ResourceManager.rMenu_btn_options_pict, ResourceManager.vbom),
                1.2f*scale, scale);*/
        final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_EXIT_ID, ResourceManager.rMenu_btn_exit_pict, ResourceManager.vbom),
                1.2f*scale, scale);

        playMenuItem.setColor(0,1,0);
        //optionsMenuItem.setColor(1,1,0);
        exitMenuItem.setColor(1,0,0);


        menuScene.addMenuItem(playMenuItem);
        //menuScene.addMenuItem(optionsMenuItem);
        menuScene.addMenuItem(exitMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        playMenuItem.setPosition((GlobalManager.getScreenWidth()-playMenuItem.getWidth()) / 2,
                (0.75f*GlobalManager.getScreenHeight()-playMenuItem.getHeight()) / 2 );
        //optionsMenuItem.setPosition((GlobalManager.getScreenWidth()-optionsMenuItem.getWidth()) / 2,
        //        (1.00f*GlobalManager.getScreenHeight()-optionsMenuItem.getHeight()) / 2 );
        exitMenuItem.setPosition((GlobalManager.getScreenWidth()-exitMenuItem.getWidth()) / 2,
                (1.25f*GlobalManager.getScreenHeight()-exitMenuItem.getHeight()) / 2 );
        //playMenuItem.setPosition(0, 0);

        menuScene.setOnMenuItemClickListener(this);

        this.setChildScene(menuScene);
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        GlobalManager.onBtnClickedInMenu(pMenuItem.getID());
        return true;
    }



}

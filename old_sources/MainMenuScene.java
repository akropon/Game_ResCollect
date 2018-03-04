package com.akropon.game_rescollect;

import com.akropon.game_rescollect.util.SceneType;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

/**
 * Created by akropon on 04.09.2017.
 */

public class MainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuChildScene;
    private final int MENU_BTN_PLAY_ID = 0;
    private final int MENU_BTN_OPTIONS_ID = 1;
    private final int MENU_BTN_EXIT_ID = 2;

    @Override
    public void createScene() {

        createMenuChildScene();

    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
                                     float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case MENU_BTN_PLAY_ID:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_BTN_OPTIONS_ID:
                return true;
            case MENU_BTN_EXIT_ID:
                System.exit(0);
                return true;
            default:
                return false;
        }
    }

    private void createMenuChildScene() {
        menuChildScene = new MenuScene(camera);
        //menuChildScene.setPosition(SceneManager.WIDTH / 2, SceneManager.HEIGHT / 2);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_PLAY_ID, resourcesManager.menu_btn_play_region, vbom), 1.2f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_OPTIONS_ID, resourcesManager.menu_btn_options_region, vbom), 1.2f, 1);
        final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(
                MENU_BTN_EXIT_ID, resourcesManager.menu_btn_exit_region, vbom), 1.2f, 1);

        playMenuItem.setColor(0,1,0);
        optionsMenuItem.setColor(1,1,0);
        exitMenuItem.setColor(1,0,0);

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);
        menuChildScene.addMenuItem(exitMenuItem);

        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition((SceneManager.WIDTH-playMenuItem.getWidth()) / 2,
                (0.75f*SceneManager.HEIGHT-playMenuItem.getHeight()) / 2 );
        optionsMenuItem.setPosition((SceneManager.WIDTH-optionsMenuItem.getWidth()) / 2,
                (1.00f*SceneManager.HEIGHT-optionsMenuItem.getHeight()) / 2 );
        exitMenuItem.setPosition((SceneManager.WIDTH-exitMenuItem.getWidth()) / 2,
                (1.25f*SceneManager.HEIGHT-exitMenuItem.getHeight()) / 2 );
        //playMenuItem.setPosition(0, 0);
        /*optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 70);
        exitMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 160);*/

        menuChildScene.setOnMenuItemClickListener(this);

        this.setChildScene(menuChildScene);
    }


}

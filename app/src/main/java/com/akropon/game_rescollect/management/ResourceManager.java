package com.akropon.game_rescollect.management;

import android.graphics.Color;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created by akropon on 10.09.2017.
 */

public class ResourceManager {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    /*--- Common BEGIN ---*/
    private static LaunchActivity launchActivity;
    public static VertexBufferObjectManager vbom;
    /*--- Common END ---*/

    /*--- Splash BEGIN ---*/
    public static ITextureRegion rSplash_pict;
    private static BitmapTextureAtlas taSplast_pict;
    /*--- Splash END ---*/

    /*--- Menu BEGIN ---*/
    public static ITextureRegion rMenu_btn_play_pict;
    public static ITextureRegion rMenu_btn_options_pict;
    public static ITextureRegion rMenu_btn_exit_pict;
    private static BuildableBitmapTextureAtlas taMenu_btns;
    /*--- Menu END ---*/

    /*--- LoadingScreen BEGIN ---*/
    public static Font fLoadingScreen_font;
    private static BitmapTextureAtlas taLoadingScreen_font;
    /*--- LoadingScreen END ---*/

    /*--- Game BEGIN ---*/
    public static Font fGame_font;
    private static BitmapTextureAtlas taGame_font;
    public static ITextureRegion rGame_explosion_pict;
    private static BitmapTextureAtlas taGameExplosion_pict;

    public static ITextureRegion rGame_crystal_yellow_pict;
    public static ITextureRegion rGame_crystal_blue_pict;
    public static ITextureRegion rGame_crystal_white_pict;
    public static ITextureRegion rGame_crystal_green_pict;
    private static BitmapTextureAtlas taGameCrystals_pict;

    public static ITextureRegion rGame_cross_pict;
    private static BitmapTextureAtlas taGameCross_pict;

    public static ITextureRegion rGame_freeze_screen_pict;
    private static BitmapTextureAtlas taGameFreezeScreen_pict;

    public static Sound sGame_glassBreaking;
    /*--- Game END ---*/

    /*---  BEGIN ---*/
    /*---  END ---*/

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static void init(LaunchActivity launchActivity) {
        ResourceManager.launchActivity = launchActivity;
        ResourceManager.vbom = launchActivity.getVertexBufferObjectManager();
    }



    public static void createSplash() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        taSplast_pict = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        rSplash_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taSplast_pict, launchActivity, "splash_ept_512х512.png", 0, 0);
    }
    public static void loadSplash() {
        taSplast_pict.load();
    }
    public static void unloadSplash() {
        taSplast_pict.unload();
    }
    public static void destroySplash() {
        taSplast_pict.clearTextureAtlasSources();
        taSplast_pict = null;
        rSplash_pict = null;
    }



    public static void createMenu() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        taMenu_btns = new BuildableBitmapTextureAtlas(
                launchActivity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
        rMenu_btn_play_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taMenu_btns, launchActivity, "menu_btn_play.png");
        rMenu_btn_options_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taMenu_btns, launchActivity, "menu_btn_options.png");
        rMenu_btn_exit_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taMenu_btns, launchActivity, "menu_btn_exit.png");
    }
    public static void loadMenu() {
        try
        {
            taMenu_btns.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
                    BitmapTextureAtlas>(0, 1, 0));
            taMenu_btns.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }
    public static void unloadMenu() {
        taMenu_btns.unload();
    }
    public static void destroyMenu() {
        taMenu_btns.clearTextureAtlasSources();
        taMenu_btns = null;
        rMenu_btn_play_pict = null;
        rMenu_btn_options_pict = null;
        rMenu_btn_exit_pict = null;
    }

    public static void createLoadingScreen() {
        FontFactory.setAssetBasePath("font/");
        taLoadingScreen_font = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fLoadingScreen_font = FontFactory.createStrokeFromAsset(
                launchActivity.getFontManager(), taLoadingScreen_font,
                launchActivity.getAssets(), "font.ttf", 50, true, Color.GREEN, 2, Color.BLACK);
    }
    public static void loadLoadingScreen() {
        fLoadingScreen_font.load();
    }
    public static void unloadLoadingScreen() {
        fLoadingScreen_font.unload();
    }
    public static void destroyLoadingScreen() {
        taLoadingScreen_font.clearTextureAtlasSources();
        taLoadingScreen_font = null;
        fLoadingScreen_font = null;
    }

    public static void createGame() {
        FontFactory.setAssetBasePath("font/");
        taGame_font = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fGame_font = FontFactory.createStrokeFromAsset(
                launchActivity.getFontManager(), taGame_font,
                launchActivity.getAssets(), "font.ttf", 50, true, Color.BLUE, 2, Color.WHITE);


        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        taGameExplosion_pict = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        rGame_explosion_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameExplosion_pict, launchActivity, "explosion_01.png", 0, 0);

        taGameCrystals_pict = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        rGame_crystal_yellow_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameCrystals_pict, launchActivity, "crystal_yellow.png", 0, 0);
        rGame_crystal_white_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameCrystals_pict, launchActivity, "crystal_white.png", 512, 0);
        rGame_crystal_blue_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameCrystals_pict, launchActivity, "crystal_blue.png", 0, 512);
        rGame_crystal_green_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameCrystals_pict, launchActivity, "crystal_green.png", 512, 512);

        taGameCross_pict = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        rGame_cross_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameCross_pict, launchActivity, "cross.png", 0, 0);

        taGameFreezeScreen_pict = new BitmapTextureAtlas(
                launchActivity.getTextureManager(), 1024, 512, TextureOptions.BILINEAR);
        rGame_freeze_screen_pict = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                taGameFreezeScreen_pict, launchActivity, "freeze_screen.png", 0, 0);

        SoundFactory.setAssetBasePath("mfx/");
        try {
            sGame_glassBreaking = SoundFactory.createSoundFromAsset(
                    GlobalManager.getTTEngine().getSoundManager(), launchActivity, "glass_breaking.ogg");
        } catch (final IOException e) {
            Debug.e(e);
        }

    }
    public static void loadGame() {
        fGame_font.load();
        taGameExplosion_pict.load();
        taGameCrystals_pict.load();
        taGameCross_pict.load();
        taGameFreezeScreen_pict.load();
    }
    public static void unloadGame() {
        fGame_font.unload();
        taGameExplosion_pict.unload();
        taGameCrystals_pict.unload();
        taGameCross_pict.unload();
        taGameFreezeScreen_pict.unload();
    }
    public static void destroyGame() {
        taGame_font.clearTextureAtlasSources();
        taGame_font = null;
        fGame_font = null;

        taGameExplosion_pict.clearTextureAtlasSources();
        taGameExplosion_pict = null;
        rGame_explosion_pict = null;

        taGameCrystals_pict.clearTextureAtlasSources();
        taGameCrystals_pict = null;
        rGame_crystal_yellow_pict = null;
        rGame_crystal_white_pict = null;
        rGame_crystal_blue_pict = null;
        rGame_crystal_green_pict = null;

        taGameCross_pict.clearTextureAtlasSources();
        taGameCross_pict = null;
        rGame_cross_pict = null;

        taGameFreezeScreen_pict.clearTextureAtlasSources();
        taGameFreezeScreen_pict = null;
        rGame_freeze_screen_pict = null;

        sGame_glassBreaking = null;
    }

    /*public static void create() {
    }
    public static void load() {
    }
    public static void unload() {
    }
    public static void destroy() {
    }*/


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}

package com.akropon.game_rescollect.engines;

import android.opengl.GLES20;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.time.TimeConstants;

/**
 * Created by akropon on 10.09.2017.
 */

/**
 * Has 3 modes of scenes amount:
 * ONE: only the first scene is updating and drawing
 * TWO: first and second scenes ...
 * THREE: first, second and third scenes ...
 */
public class TripleTripleEngine extends Engine {
    // ===========================================================
    // Constants
    // ===========================================================

    public static enum MODE {
        ONE,
        TWO,
        THREE
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private MODE mode;

    private final long preferredFrameLengthNanoseconds;

    private boolean fUpdate;
    private boolean fDrawScenes;

    private final Camera mSecondCamera;
    private final Camera mThirdCamera;

    private Scene mSecondScene;
    private Scene mThirdScene;

    private float border12part; // относительное значение первой границы экранов между первой и второй сценами. in(0;1)
    private float border23part; // относительно значение второй границы экранов между второй и третьей сценами. in(0;1)


    // ===========================================================
    // Constructors
    // ===========================================================

    public TripleTripleEngine(final EngineOptions pEngineOptions,
                              final Camera secondCamera,
                              final Camera thirdCamera,
                              final int pFramesPerSecond) {
        super(pEngineOptions);

        this.mode = MODE.ONE;

        this.preferredFrameLengthNanoseconds =
                TimeConstants.NANOSECONDS_PER_SECOND / pFramesPerSecond;

        fUpdate = true;
        fDrawScenes = true;

        this.border12part = 0.5f;
        this.border23part = 0.8f;

        this.mSecondCamera = secondCamera;
        this.mThirdCamera = thirdCamera;

    }

    // ===========================================================
    // Getters
    // ===========================================================


    public MODE getMode() {
        return mode;
    }

    public boolean isUpdate() {
        return fUpdate;
    }

    public boolean isDrawScenes() {
        return fDrawScenes;
    }

    public float getBorder12part() {
        return border12part;
    }

    public float getBorder23part() {
        return border23part;
    }

    @Deprecated
    @Override
    public Camera getCamera() {
        return super.mCamera;
    }

    public Camera getFirstCamera() {
        return super.mCamera;
    }

    public Camera getSecondCamera() {
        return this.mSecondCamera;
    }

    public Camera getThirdCamera() {
        return this.mThirdCamera;
    }

    @Deprecated
    @Override
    public Scene getScene() {
        return super.getScene();
    }

    public Scene getFirstScene() {
        return super.getScene();
    }

    public Scene getSecondScene() {
        return this.mSecondScene;
    }

    public Scene getThirdScene() {
        return this.mThirdScene;
    }



    // ===========================================================
    // Setters
    // ===========================================================


    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public void setUpdate(boolean fUpdate) {
        this.fUpdate = fUpdate;
    }

    public void setDrawScenes(boolean fDrawScenes) {
        this.fDrawScenes = fDrawScenes;
    }

    public void setBorder12part(float border12part) {
        this.border12part = border12part;
    }

    public void setBorder23part(float border23part) {
        this.border23part = border23part;
    }

    @Deprecated
    @Override
    public void setScene(final Scene pScene) {
        this.setFirstScene(pScene);
    }

    public void setFirstScene(final Scene pScene) {
        super.setScene(pScene);
    }

    public void setSecondScene(final Scene pScene) {
        this.mSecondScene = pScene;
    }

    public void setThirdScene(Scene pScene) {
        this.mThirdScene = pScene;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdate(final long pNanosecondsElapsed) throws InterruptedException {
        final long preferredFrameLengthNanoseconds = this.preferredFrameLengthNanoseconds;
        final long deltaFrameLengthNanoseconds = preferredFrameLengthNanoseconds - pNanosecondsElapsed;

        if(deltaFrameLengthNanoseconds <= 0) {
            super.onUpdate(pNanosecondsElapsed);
        } else {
            final int sleepTimeMilliseconds =
                    (int) (deltaFrameLengthNanoseconds / TimeConstants.NANOSECONDS_PER_MILLISECOND);

            Thread.sleep(sleepTimeMilliseconds);
            super.onUpdate(pNanosecondsElapsed + deltaFrameLengthNanoseconds);
        }
    }

    @Override
    protected void onDrawScene(final GLState pGLState, final Camera pFirstCamera) {
        if (fDrawScenes == false) return;

        switch (mode) {
            case ONE:
                drawOneScene(pGLState, pFirstCamera);
                break;
            case TWO:
                drawTwoScenes(pGLState, pFirstCamera);
                break;
            case THREE:
                drawThreeScenes(pGLState, pFirstCamera);
                break;
        }


    }

    protected void drawOneScene(final GLState pGLState, final Camera pFirstCamera) {
        //super.onDrawScene(pGLState, pFirstCamera);

        final int surfaceWidth = this.mSurfaceWidth;
        final int surfaceHeight = this.mSurfaceHeight;

        pGLState.enableScissorTest();

		/* Upper Screen. With upper camera. */
        if (super.mScene != null) {
            GLES20.glScissor(0, 0,
                    surfaceWidth, surfaceHeight);
            GLES20.glViewport(0, 0,
                    surfaceWidth, surfaceHeight);

            super.mScene.onDraw(pGLState, pFirstCamera);
            pFirstCamera.onDrawHUD(pGLState);
        }

        pGLState.disableScissorTest();
    }

    protected void drawTwoScenes(final GLState pGLState, final Camera pFirstCamera) {
        final Camera secondCamera = this.getSecondCamera();

        final int surfaceWidth = this.mSurfaceWidth;

        final int surfaceHeight = this.mSurfaceHeight;
        final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);

        pGLState.enableScissorTest();

		/* Upper Screen. With upper camera. */
        if (super.mScene != null) {
            GLES20.glScissor(0, this.mSurfaceHeight - surfaceHeight_1stBorder,
                    surfaceWidth, surfaceHeight_1stBorder - 0);
            GLES20.glViewport(0, this.mSurfaceHeight - surfaceHeight_1stBorder,
                    surfaceWidth, surfaceHeight_1stBorder - 0);

            super.mScene.onDraw(pGLState, pFirstCamera);
            pFirstCamera.onDrawHUD(pGLState);
        }

        /* Second Screen. With second camera. */
        if (mSecondScene != null) {
            GLES20.glScissor(0, 0,
                    surfaceWidth, surfaceHeight - surfaceHeight_1stBorder);
            GLES20.glViewport(0, 0,
                    surfaceWidth, surfaceHeight - surfaceHeight_1stBorder);

            this.mSecondScene.onDraw(pGLState, secondCamera);
            secondCamera.onDrawHUD(pGLState);
        }

        pGLState.disableScissorTest();
    }

    protected void drawThreeScenes(final GLState pGLState, final Camera pFirstCamera) {
        final Camera secondCamera = this.getSecondCamera();
        final Camera thirdCamera = this.getThirdCamera();

        final int surfaceWidth = this.mSurfaceWidth;

        final int surfaceHeight = this.mSurfaceHeight;
        final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);
        final int surfaceHeight_2ndBorder = (int) (mSurfaceHeight * border23part);

        pGLState.enableScissorTest();

		/* Upper Screen. With upper camera. */
        if (super.mScene != null) {
            GLES20.glScissor(0, this.mSurfaceHeight - surfaceHeight_1stBorder,
                    surfaceWidth, surfaceHeight_1stBorder - 0);
            GLES20.glViewport(0, this.mSurfaceHeight - surfaceHeight_1stBorder,
                    surfaceWidth, surfaceHeight_1stBorder - 0);

            super.mScene.onDraw(pGLState, pFirstCamera);
            pFirstCamera.onDrawHUD(pGLState);
        }

        /* Mid Screen. With mid camera. */
        if (mSecondScene != null) {
            GLES20.glScissor(0, this.mSurfaceHeight - surfaceHeight_2ndBorder,
                    surfaceWidth, surfaceHeight_2ndBorder - surfaceHeight_1stBorder);
            GLES20.glViewport(0, this.mSurfaceHeight - surfaceHeight_2ndBorder,
                    surfaceWidth, surfaceHeight_2ndBorder - surfaceHeight_1stBorder);

            this.mSecondScene.onDraw(pGLState, secondCamera);
            secondCamera.onDrawHUD(pGLState);
        }

        /* Lower Screen. With lower camera. */
        if (mThirdScene != null) {
            GLES20.glScissor(0, 0,
                    surfaceWidth, surfaceHeight - surfaceHeight_2ndBorder);
            GLES20.glViewport(0, 0,
                    surfaceWidth, surfaceHeight - surfaceHeight_2ndBorder);

            this.mThirdScene.onDraw(pGLState, thirdCamera);
            thirdCamera.onDrawHUD(pGLState);
        }


//        /* Upper Screen. With upper camera. */
//        if(super.mScene != null) {
//            GLES20.glScissor(0, 0, surfaceWidth, surfaceHeight_1stBorder-0);
//            GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight_1stBorder-0);
//
//            super.mScene.onDraw(pGLState, pFirstCamera);
//            pFirstCamera.onDrawHUD(pGLState);
//        }
//
//        /* Mid Screen. With mid camera. */
//        if(mSecondScene != null) {
//            GLES20.glScissor(0, surfaceHeight_1stBorder,
//                    surfaceWidth, surfaceHeight_2ndBorder-surfaceHeight_1stBorder);
//            GLES20.glViewport(0, surfaceHeight_1stBorder,
//                    surfaceWidth, surfaceHeight_2ndBorder-surfaceHeight_1stBorder);
//
//            this.mSecondScene.onDraw(pGLState, secondCamera);
//            secondCamera.onDrawHUD(pGLState);
//        }
//
//        /* Lower Screen. With lower camera. */
//        if(mThirdScene != null) {
//            GLES20.glScissor(0, surfaceHeight_2ndBorder,
//                    surfaceWidth, surfaceHeight - surfaceHeight_2ndBorder);
//            GLES20.glViewport(0, surfaceHeight_2ndBorder,
//                    surfaceWidth, surfaceHeight - surfaceHeight_2ndBorder);
//
//            this.mThirdScene.onDraw(pGLState, thirdCamera);
//            thirdCamera.onDrawHUD(pGLState);
//        }

        pGLState.disableScissorTest();
    }


    @Override
    protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
        switch (mode) {

            case ONE:
                return this.getFirstCamera();

            case TWO:
                if (pTouchEvent.getY() <= this.mSurfaceHeight * border12part)
                    return this.getFirstCamera();
                else
                    return this.getSecondCamera();

            case THREE:
                if (pTouchEvent.getY() <= this.mSurfaceHeight * border12part)
                    return this.getFirstCamera();
                else if (pTouchEvent.getY() <= this.mSurfaceHeight * border23part)
                    return this.getSecondCamera();
                else
                    return this.getThirdCamera();
        }
        return null;
    }

    @Override
    protected Scene getSceneFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
        switch (mode) {

            case ONE:
                return this.getFirstScene();

            case TWO:
                if (pTouchEvent.getY() <= this.mSurfaceHeight * border12part)
                    return this.getFirstScene();
                else
                    return this.getSecondScene();

            case THREE:
                if (pTouchEvent.getY() <= this.mSurfaceHeight * border12part)
                    return this.getFirstScene();
                else if (pTouchEvent.getY() <= this.mSurfaceHeight * border23part)
                    return this.getSecondScene();
                else
                    return this.getThirdScene();
        }
        return null;
    }

    @Override
    protected void onUpdateScene(final float pSecondsElapsed) {
        if (fUpdate == false) return;

        switch (mode) {
            case THREE:
                if (this.mThirdScene != null)
                    this.mThirdScene.onUpdate(pSecondsElapsed);
            case TWO:
                if (this.mSecondScene != null)
                    this.mSecondScene.onUpdate(pSecondsElapsed);
            case ONE:
                if (this.mScene != null)
                    this.mScene.onUpdate(pSecondsElapsed);
        }
    }

    @Override
    protected void convertSurfaceToSceneTouchEvent(final Camera pCamera, final TouchEvent pSurfaceTouchEvent) {
        switch (mode) {

            case ONE: {
                pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                        this.mSurfaceWidth, this.mSurfaceHeight);
            }
            break;

            case TWO: {
                final int surfaceHeight = this.mSurfaceHeight;
                final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);

                if (pCamera == this.getFirstCamera()) {
                    pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                            this.mSurfaceWidth, surfaceHeight_1stBorder);
                } else {
                    pSurfaceTouchEvent.offset(0, -surfaceHeight_1stBorder);
                    pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                            this.mSurfaceWidth, surfaceHeight - surfaceHeight_1stBorder);
                }
            }
            break;

            case THREE: {
                final int surfaceHeight = this.mSurfaceHeight;
                final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);
                final int surfaceHeight_2ndBorder = (int) (mSurfaceHeight * border23part);
                if (pCamera == this.getFirstCamera()) {
                    pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                            this.mSurfaceWidth, surfaceHeight_1stBorder);
                } else if (pCamera == this.getSecondCamera()) {
                    pSurfaceTouchEvent.offset(0, -surfaceHeight_1stBorder);
                    pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                            this.mSurfaceWidth, surfaceHeight_2ndBorder - surfaceHeight_1stBorder);
                } else {
                    pSurfaceTouchEvent.offset(0, -surfaceHeight_2ndBorder);
                    pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
                            this.mSurfaceWidth, surfaceHeight - surfaceHeight_2ndBorder);
                }
            }
            break;
        }
    }

    @Override
    protected void onUpdateUpdateHandlers(final float pSecondsElapsed) {
        if (fUpdate == false) return;


        super.onUpdateUpdateHandlers(pSecondsElapsed);

        switch (mode) {
            case THREE:
                this.getThirdCamera().onUpdate(pSecondsElapsed);
            case TWO:
                this.getSecondCamera().onUpdate(pSecondsElapsed);
        }
    }

    @Override
    protected void onUpdateCameraSurface() {
        switch (mode) {
            case ONE: {
                this.getFirstCamera().setSurfaceSize(0, 0,
                        this.mSurfaceWidth, this.mSurfaceHeight);
            }
            break;

            case TWO: {
                final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);

                this.getFirstCamera().setSurfaceSize(0, 0,
                        this.mSurfaceWidth, surfaceHeight_1stBorder);
                this.getSecondCamera().setSurfaceSize(0, surfaceHeight_1stBorder,
                        this.mSurfaceWidth, this.mSurfaceHeight - surfaceHeight_1stBorder);
            }
            break;

            case THREE: {
                final int surfaceHeight_1stBorder = (int) (mSurfaceHeight * border12part);
                final int surfaceHeight_2ndBorder = (int) (mSurfaceHeight * border23part);

                this.getFirstCamera().setSurfaceSize(0, 0,
                        this.mSurfaceWidth, surfaceHeight_1stBorder);
                this.getSecondCamera().setSurfaceSize(0, surfaceHeight_1stBorder,
                        this.mSurfaceWidth, surfaceHeight_2ndBorder - surfaceHeight_1stBorder);
                this.getThirdCamera().setSurfaceSize(0, surfaceHeight_2ndBorder,
                        this.mSurfaceWidth, this.mSurfaceHeight - surfaceHeight_2ndBorder);
            }
            break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void stopDrawing() {
        this.fDrawScenes = false;
    }
    public void stopUpdating() {
        this.fUpdate = false;
    }
    public void stopDrawingAndUpdating() {
        this.fDrawScenes = false;
        this.fUpdate = false;
    }

    public void resumeDrawing() {
        this.fDrawScenes = true;
    }
    public void resumeUpdating() {
        this.fUpdate = true;
    }
    public void resumeDrawingAndUpdating() {
        this.fDrawScenes = true;
        this.fUpdate = true;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}

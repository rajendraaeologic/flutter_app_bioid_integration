package com.bioid.authenticator.base.opengl;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.view.MotionEvent;

import com.bioid.authenticator.R;
import com.bioid.authenticator.base.logging.LoggingHelper;
import com.bioid.authenticator.base.logging.LoggingHelperFactory;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

/**
 * Rajawali renderer for the 3D head.
 */
class RajawaliHeadRenderer extends Renderer {

    private static final LoggingHelper LOG = LoggingHelperFactory.create(RajawaliHeadRenderer.class);
    @ColorRes
    private static final int MODEL_COLOR = R.color.color3DHead;
    @FloatRange(from = 0.0, to = 1.0)
    private static final float MODEL_TRANSPARENCY = 0.85f;

    private Object3D head;
    private Animation3D lastAnimation;

    RajawaliHeadRenderer(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        getCurrentScene().addLight(setupLight());
        getCurrentCamera().setPosition(0, 0.2, 3.0);

        // preload and hide the model because this is quite expensive
        head = loadModel();
        configureModel(head);
        getCurrentScene().addChild(head);
    }


    @NonNull
    private DirectionalLight setupLight() {
        DirectionalLight light = new DirectionalLight(0, 0.2, -10);
        light.setPower(1.4f);
        return light;
    }

    @NonNull
    private Object3D loadModel() {
        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.head_obj);
        try {
            return objParser.parse().getParsedObject();
        } catch (ParsingException e) {
            throw new RuntimeException("could not load 3D head obj-file", e);
        }
    }

    private void configureModel(@NonNull Object3D head) {
        head.setColor(ContextCompat.getColor(getContext(), MODEL_COLOR));
        head.setAlpha(MODEL_TRANSPARENCY);
        head.setVisible(false);
    }

    /**
     * Does add the 3D head to the rendered scene.
     */
    void showModel() {
        if (!getSceneInitialized()) {
            return;
        }

        LOG.d("showModel()");
        head.setVisible(true);
    }

    /**
     * Does remove the 3D head from the rendered scene.
     */
    void hideModel() {
        if (!getSceneInitialized()) {
            return;
        }

        LOG.d("hideModel()");
        head.setVisible(false);
    }

    /**
     * Does rotate the 3D head by the specified angle around the specified axis.
     *
     * @param axis                      for the rotation
     * @param angle                     for the rotation
     * @param animationDurationInMillis how long the animation should take
     */
    @SuppressWarnings("SameParameterValue")
    void rotateModel(@NonNull Vector3.Axis axis, @IntRange(from = 0, to = 360) int angle,
                     @IntRange(from = 0) int animationDurationInMillis) {
        if (!getSceneInitialized()) {
            return;
        }

        LOG.d("rotateModel(axis=%s, angle=%d)", axis, angle);

        Animation3D animation = new RotateOnAxisAnimation(axis, angle);
        animation.setDurationMilliseconds(animationDurationInMillis);
        animation.setTransformable3D(head);
        animation.play();

        getCurrentScene().registerAnimation(animation);
        lastAnimation = animation;
    }

    /**
     * Returns true if the 3D head does currently perform an animation.
     */
    boolean isAnimationRunning() {
        return lastAnimation != null && lastAnimation.isPlaying();
    }

    /**
     * Does reset the model rotation and stops an eventually running animation.
     */
    void resetModelRotation() {
        if (!getSceneInitialized()) {
            return;
        }

        LOG.d("resetModelRotation()");

        if (isAnimationRunning()) {
            lastAnimation.pause();
            lastAnimation = null;
        }

        head.resetToLookAt();
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        // do nothing
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // do nothing
    }
}

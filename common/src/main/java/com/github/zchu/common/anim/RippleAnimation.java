package com.github.zchu.common.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by wuyr on 3/15/18 5:23 PM.
 */

@SuppressLint("ViewConstructor")
public class RippleAnimation extends View {

    private Bitmap mBackground;//屏幕截图
    private Paint mPaint;
    private int mMaxRadius, mStartRadius, mCurrentRadius;
    private boolean isStarted;
    private long mDuration;
    private float mStartX, mStartY;//扩散的起点
    private ViewGroup mRootView;//DecorView
    private OnAnimationEndListener mOnAnimationEndListener;
    private Animator.AnimatorListener mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private ValueAnimator animator;

    private RippleAnimation(Context context, float startX, float startY, int radius) {
        super(context);
        //获取activity的根视图,用来添加本View
        mRootView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        mStartX = startX;
        mStartY = startY;
        mDuration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mStartRadius = radius;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //设置为擦除模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        updateMaxRadius();
        initListener();
    }

    public static RippleAnimation create(View onClickView) {
        return create(onClickView, onClickView.getContext());
    }

    public static RippleAnimation create(View onClickView, Context context) {
        int newWidth = onClickView.getWidth() / 2;
        int newHeight = onClickView.getHeight() / 2;
        //计算起点位置
        float startX = getAbsoluteX(onClickView) + newWidth;
        float startY = getAbsoluteY(onClickView) + newHeight;
        //起始半径
        //因为我们要避免遮挡按钮
        int radius = Math.max(newWidth, newHeight);
        return new RippleAnimation(context, startX, startY, radius);
    }

    /**
     * 获取view在屏幕中的绝对x坐标
     */
    private static float getAbsoluteX(View view) {
        float x = view.getX();
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            x += getAbsoluteX((View) parent);
        }
        return x;
    }

    /**
     * 获取view在屏幕中的绝对y坐标
     */
    private static float getAbsoluteY(View view) {
        float y = view.getY();
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            y += getAbsoluteY((View) parent);
        }
        return y;
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            updateBackground();
            attachToRootView();
            animator = getAnimator();
            animator.start();
        }
    }

    public RippleAnimation setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    private void initListener() {
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画播放完毕, 移除本View
                detachFromRootView();
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onAnimationEnd();
                }
                isStarted = false;
            }
        };
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新圆的半径
                mCurrentRadius = (int) (float) animation.getAnimatedValue() + mStartRadius;
                postInvalidate();
            }
        };
    }

    /**
     * 根据起始点将屏幕分成4个小矩形,mMaxRadius就是取它们中最大的矩形的对角线长度
     * 这样的话, 无论起始点在屏幕中的哪一个位置上, 我们绘制的圆形总是能覆盖屏幕
     */
    private void updateMaxRadius() {
        //将屏幕分成4个小矩形
        RectF leftTop = new RectF(0, 0, mStartX + mStartRadius, mStartY + mStartRadius);
        RectF rightTop = new RectF(leftTop.right, 0, mRootView.getRight(), leftTop.bottom);
        RectF leftBottom = new RectF(0, leftTop.bottom, leftTop.right, mRootView.getBottom());
        RectF rightBottom = new RectF(leftBottom.right, leftTop.bottom, mRootView.getRight(), leftBottom.bottom);
        //分别获取对角线长度
        double leftTopHypotenuse = Math.sqrt(Math.pow(leftTop.width(), 2) + Math.pow(leftTop.height(), 2));
        double rightTopHypotenuse = Math.sqrt(Math.pow(rightTop.width(), 2) + Math.pow(rightTop.height(), 2));
        double leftBottomHypotenuse = Math.sqrt(Math.pow(leftBottom.width(), 2) + Math.pow(leftBottom.height(), 2));
        double rightBottomHypotenuse = Math.sqrt(Math.pow(rightBottom.width(), 2) + Math.pow(rightBottom.height(), 2));
        //取最大值
        mMaxRadius = (int) Math.max(
                Math.max(leftTopHypotenuse, rightTopHypotenuse),
                Math.max(leftBottomHypotenuse, rightBottomHypotenuse));
    }

    /**
     * 添加到根视图
     */
    private void attachToRootView() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.addView(this);
    }

    /**
     * 从根视图中移除
     */
    private void detachFromRootView() {
        mRootView.removeView(this);
    }

    /**
     * 更新屏幕截图
     */
    private void updateBackground() {
        if (mBackground != null && !mBackground.isRecycled()) {
            mBackground.recycle();
        }
        mRootView.setDrawingCacheEnabled(true);
        mBackground = mRootView.getDrawingCache();
        mBackground = Bitmap.createBitmap(mBackground);
        mRootView.setDrawingCacheEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在新的图层上面绘制
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius, mPaint);
        canvas.restoreToCount(layer);
    }

    private ValueAnimator getAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mMaxRadius).setDuration(mDuration);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(mAnimatorListener);
        return valueAnimator;
    }

    public RippleAnimation setOnAnimationEndListener(OnAnimationEndListener listener) {
        mOnAnimationEndListener = listener;
        return this;
    }


    public void destroy() {
        if (mBackground != null && !mBackground.isRecycled()) {
            mBackground.recycle();
        }
        mBackground = null;
        mRootView.setDrawingCacheEnabled(false);
        mRootView = null;
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public interface OnAnimationEndListener {
        void onAnimationStart();

        void onAnimationEnd();
    }
}
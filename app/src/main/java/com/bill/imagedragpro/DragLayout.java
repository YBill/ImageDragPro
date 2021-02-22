package com.bill.imagedragpro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by Bill on 2021/2/22.
 */

public class DragLayout extends RelativeLayout {
    private ViewDragHelper mViewDragHelper;

    private Point originPoint = new Point();

    private View targetView;

    private DragListener listener;

    private boolean callEvent = false;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragLayout bind(Activity activity) {
        activity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragCallback());
    }

    float lastX;
    float lastY;
    float disX = 0;
    float disY = 0;

    public static double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    public static float dp2Px(Context ctx, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getApplicationContext().getResources().getDisplayMetrics());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            lastX = ev.getX();
            lastY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            disX = lastX - ev.getX();
            disY = lastY - ev.getY();
            double degree = Math.atan2(Math.abs(disY), Math.abs(disX));
            if (disX < -dp2Px(getContext(), 100) && radianToDegree(degree) < 30) {
                return mViewDragHelper.shouldInterceptTouchEvent(ev);
            }
        } else if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        originPoint.x = targetView.getLeft();
        originPoint.y = targetView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        targetView = findViewById(R.id.picture);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == targetView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == targetView) {
                if (callEvent || yvel > 8000) {
                    if (listener != null) {
                        listener.onDragFinished();
                    }
                    callEvent = false;
                } else {
                    mViewDragHelper.settleCapturedViewAt(originPoint.x, originPoint.y);
                    invalidate();
                }
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (top > originPoint.y) {
                float a = (float) (top - originPoint.y) / (float) (getMeasuredHeight() - originPoint.y);
                setBackgroundColor(changeAlpha(0xff000000, 1 - a));
                targetView.setScaleX(1 - a);
                targetView.setScaleY(1 - a);
                if ((top - originPoint.y) > getMeasuredHeight() / 5) {
                    callEvent = true;
                } else {
                    callEvent = false;
                }
            }
        }
    }

    public void setDragListener(DragListener listener) {
        this.listener = listener;
    }

    public interface DragListener {
        void onDragFinished();
    }

    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
}

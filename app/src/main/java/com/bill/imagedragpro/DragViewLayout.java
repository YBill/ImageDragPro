package com.bill.imagedragpro;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * author : Bill
 * date : 2021/2/22
 * description :
 */

public class DragViewLayout extends RelativeLayout {

    private ViewDragHelper mViewDragHelper;

    private Point originPoint = new Point();

    private View targetView;

    private DragListener listener;

    private boolean callEvent = false;

    public DragViewLayout(Context context) {
        super(context);
    }

    public DragViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }

        // 处理事件冲突，多指时将事件交给子View去处理
        if (ev.getPointerCount() > 1) {
            return false;
        }

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        // 处理松手回到原来位置
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 获取图片起始位置
        originPoint.x = targetView.getLeft();
        originPoint.y = targetView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        targetView = getChildAt(0);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == targetView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left; // 跟随手指水平移动

            // 水平方向位置固定
//            return (getWidth() - child.getWidth()) / 2;

            // 跟随手指水平移动，判断不超出边界
            /*final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - child.getWidth() - getPaddingRight();
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;*/
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top; // 跟随手指竖直移动

            // 禁止向上移动
            /*if (top > (getHeight() - child.getHeight()) / 2) {
                return top;
            } else {
                return (getHeight() - child.getHeight()) / 2;
            }*/

            // 跟随手指竖直移动，判断不超出边界
            /*final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - child.getHeight() - getPaddingBottom();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;*/
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
            // xvel和yvel分别表示水平和竖直方向的滑动速度
            if (releasedChild == targetView) {
                if (callEvent || yvel > 8000) {
                    if (listener != null) {
                        listener.onDragFinished();
                    }
                    callEvent = false;
                } else {
                    // 处理松手回到原来位置
                    mViewDragHelper.settleCapturedViewAt(originPoint.x, originPoint.y);
                    invalidate();
                }
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            int dis = top - originPoint.y;
            float a = (float) (Math.abs(dis)) / (float) (getMeasuredHeight() - originPoint.y);

            if (listener != null) {
                listener.onDrag(a);
            }

            if (a > 0.25f) {
                callEvent = true;
            } else {
                callEvent = false;
            }
        }
    }

    public void setDragListener(DragListener listener) {
        this.listener = listener;
    }

    public interface DragListener {
        void onDragFinished();

        void onDrag(float change);
    }

}

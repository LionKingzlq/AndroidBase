package com.chetuan.askforit.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by YT on 2015/11/13.
 */
public class LeftRightLayout extends FrameLayout {

    private static final String LOG_TAG = "xiyuan";

    private Context context;

    private int mTouchSlop;

    private static final float DRAG_RATE = 25;

    private LinearLayout leftContent;

    private LinearLayout rightContent;

    private int layoutW;

    private int layoutH;

    private int rightW;

    private float curScrollPercent = 0;

    private static final int INVALID_POINTER = -1;

    private int mActivePointerId = INVALID_POINTER;

    private boolean mIsBeingDragged;

    private int mLastMotionX;

    private int mLastMotionY;

    private static final int SPINNER_OPEN = 1;

    private static final int SPINNER_AUTO = 0;

    private static final int SPINNER_CLOSE = -1;

    private int spinnerType = SPINNER_AUTO;

    private int lastDragDirection = 0;

    private final DecelerateInterpolator mInterpolator = new DecelerateInterpolator();

    public LeftRightLayout(Context context) {
        super(context);
        init(context);
    }

    public LeftRightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LeftRightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        this.context = context;
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop() / 3;

        curScrollPercent = 0;

        leftContent = new LinearLayout(context);
        leftContent.setOrientation(LinearLayout.VERTICAL);
        addView(leftContent);

        rightContent = new LinearLayout(context);
        rightContent.setOrientation(LinearLayout.VERTICAL);
        addView(rightContent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutW = MeasureSpec.getSize(widthMeasureSpec);
        layoutH = MeasureSpec.getSize(heightMeasureSpec);

        leftContent.setLayoutParams(new LayoutParams(layoutW, layoutH));
        leftContent.measure(MeasureSpec.makeMeasureSpec(layoutW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(layoutH, MeasureSpec.EXACTLY));

        rightContent.measure(MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(layoutH, MeasureSpec.EXACTLY));
        rightW = rightContent.getMeasuredWidth();
        rightContent.setLayoutParams(new LayoutParams(rightW, layoutH));

        setMeasuredDimension(layoutW + rightW, layoutH);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        leftContent.layout(left, top, right - rightW, bottom);
        rightContent.layout(right - rightW, top, right, bottom);

        moveSpinnerByScale(curScrollPercent);
    }

    public final void setLeftView(View view)
    {
        leftContent.removeAllViews();
        leftContent.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public final void setRightView(View view)
    {
        rightContent.removeAllViews();
        rightContent.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public final void setRightView(View view, LinearLayout.LayoutParams lp)
    {
        rightContent.removeAllViews();
        rightContent.addView(view);
    }

    private void moveSpinner(int deltaX)
    {
        if(deltaX > 0)
        {
            curScrollPercent = Math.max(0, curScrollPercent + deltaX / (float) layoutW);
            if(curScrollPercent >= 0.995f)
            {
                curScrollPercent = 1;
            }
        }
        else
        {
            curScrollPercent = Math.min(1, curScrollPercent + deltaX / (float)layoutW);
            if(curScrollPercent <= 0.005f)
            {
                curScrollPercent = 0;
            }
        }

        changePosition();
    }

    private void moveSpinnerByScale(float newScale)
    {
        curScrollPercent = newScale;
        if(curScrollPercent <= 0.005f)
        {
            curScrollPercent = 0;
        }
        else if(curScrollPercent >= 0.995f)
        {
            curScrollPercent = 1;
        }

        changePosition();
    }

    private void changePosition()
    {
        this.scrollTo((int) (rightW * curScrollPercent), 0);
    }

    private final Animation spinnerAnimation = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            float newPercent = 1;
            switch (spinnerType)
            {
                case SPINNER_OPEN: {
                    newPercent = curScrollPercent + (1 - 0)* interpolatedTime;
                    break;
                }
                case SPINNER_AUTO: {
                    if(curScrollPercent >= 0.5f)
                    {
                        newPercent = curScrollPercent + 0.5f * interpolatedTime;
                    }
                    else
                    {
                        newPercent = curScrollPercent - 0.5f * interpolatedTime;
                    }
                    break;
                }
                case SPINNER_CLOSE: {
                    newPercent = curScrollPercent - (1 - 0)* interpolatedTime;
                    break;
                }
            }

            moveSpinnerByScale(newPercent);
        }
    };

    private void openSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_OPEN;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (2000 / (1 - 0) * (1 - curScrollPercent) + 0.5f));
        spinnerAnimation.setInterpolator(mInterpolator);
        this.startAnimation(spinnerAnimation);
    }

    public void closeSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_CLOSE;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (2000 / (1 - 0) * (curScrollPercent - 0) + 0.5f));
        spinnerAnimation.setInterpolator(mInterpolator);
        this.startAnimation(spinnerAnimation);
    }

    private void autoSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_AUTO;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (10000 / (1 - 0) * Math.min(curScrollPercent - 0, 1 - curScrollPercent) + 0.5f));
        spinnerAnimation.setInterpolator(mInterpolator);
        this.startAnimation(spinnerAnimation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int direction = 0;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(LOG_TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int x = (int) ev.getX(pointerIndex);
                final int xDiff = x - mLastMotionX;
                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);

                mLastMotionX = x;
                mLastMotionY = y;
                if (-xDiff > yDiff * 2 && -xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                else
                {
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mLastMotionX = x;
                mLastMotionY = y;
                mActivePointerId = ev.getPointerId(0);

                mIsBeingDragged = false;

                if(curScrollPercent != 0 && x <= layoutW - rightW * curScrollPercent)
                {
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = (int) ev.getX(index);
                mLastMotionY = (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }
                // Remember where the motion event started
                mLastMotionX = (int) ev.getX();
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(LOG_TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                final int x = (int) ev.getX(activePointerIndex);
                final int y = (int) ev.getY(activePointerIndex);
                int deltaX = mLastMotionX - x;
                int deltaY = mLastMotionY - y;
                mLastMotionX = x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop && Math.abs(deltaX) > Math.abs(deltaY)) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged) {
                    this.clearAnimation();
                    lastDragDirection = deltaX > 0? 1: -1;
                    moveSpinner((int) (deltaX > 0? Math.min(mTouchSlop * DRAG_RATE, deltaX * DRAG_RATE):  Math.max(-mTouchSlop * DRAG_RATE, deltaX * DRAG_RATE)));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;

                    autoSpinner();
                }
                else if (curScrollPercent == 1) {
                    float touchX = ev.getX();
                    if(touchX <= layoutW - rightW)
                    {
                        closeSpinner();
                    }
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    lastDragDirection = 0;

                    autoSpinner();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

}

package com.chetuan.askforit.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
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

import java.util.ArrayList;

/**
 * Created by YT on 2015/10/28.
 */
public class LeftMenuLayout extends FrameLayout {

    private static final String LOG_TAG = "xiyuan";

    private Context context;

    private LinearLayout leftMenu;

    private LinearLayout mainContent;

    private int leftMenuW;

    private int layoutW;

    private int layoutH;

    private static final int INVALID_POINTER = -1;

    private int mActivePointerId = INVALID_POINTER;

    private boolean mIsBeingDragged;

    private boolean mIsTouchOnMenu;

    private int mLastMotionX;
    private int mLastMotionY;

    private int mTouchSlop;

    protected ArrayList<View> mCheckScrollTargets;

    private float leftMenuScale = SCALE_MIN;

    private static final int SPINNER_AUTO = 0;

    private static final int SPINNER_CLOSE = -1;

    private static final int SPINNER_OPEN = 1;

    private int spinnerType = SPINNER_AUTO;

    private static final float SCALE_MIN = 0.8f;

    private static final float ALPHA_MIN = 0.1f;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    public LeftMenuLayout(Context context) {
        super(context);
        init(context);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        this.getChildAt(1);
        this.getChildAt(2);

        this.context = context;
        mCheckScrollTargets = new ArrayList<>();
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop() / 2;

        leftMenu = new LinearLayout(context);
        leftMenu.setOrientation(LinearLayout.VERTICAL);
        addView(leftMenu);

        mainContent = new LinearLayout(context);
        mainContent.setOrientation(LinearLayout.VERTICAL);
        addView(mainContent);
    }

    public void addCheckScrollTarget(View view)
    {
        if(!mCheckScrollTargets.contains(view))
        {
            mCheckScrollTargets.add(view);
        }
    }

    public void removeCheckScrollTarget(View view)
    {
        if(mCheckScrollTargets.contains(view))
        {
            mCheckScrollTargets.remove(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutW = MeasureSpec.getSize(widthMeasureSpec);
        layoutH = MeasureSpec.getSize(heightMeasureSpec);

        leftMenuW = layoutW * 3 / 4;
        leftMenu.setLayoutParams(new LayoutParams(leftMenuW, layoutH));
        mainContent.setLayoutParams(new LayoutParams(layoutW, layoutH));

        leftMenu.measure(MeasureSpec.makeMeasureSpec(leftMenuW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(layoutH, MeasureSpec.EXACTLY));
        mainContent.measure(MeasureSpec.makeMeasureSpec(layoutW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(layoutH, MeasureSpec.EXACTLY));
        setMeasuredDimension(layoutW, layoutH);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        leftMenu.layout((int) (left - leftMenuW / SCALE_MIN + leftMenuW), top, left + leftMenuW, bottom);
        mainContent.layout(left, top, (int) (layoutW / SCALE_MIN + left), bottom);

        moveSpinnerByScale(leftMenuScale);
    }

    public final void setLeftMenuView(View view)
    {
        leftMenu.removeAllViews();
        leftMenu.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public final void setMainContentView(View view)
    {
        mainContent.removeAllViews();
        mainContent.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public final boolean isOpen()
    {
        return leftMenuScale != SCALE_MIN;
    }

    private void moveSpinner(int deltaX)
    {
        if(deltaX > 0)
        {
            leftMenuScale = Math.max(SCALE_MIN, leftMenuScale - deltaX / (float) layoutW);
            if(leftMenuScale <= 0.005f + SCALE_MIN)
            {
                leftMenuScale = SCALE_MIN;
            }
        }
        else
        {
            leftMenuScale = Math.min(1, leftMenuScale - deltaX / (float)layoutW);
            if(leftMenuScale >= 0.995f)
            {
                leftMenuScale = 1;
            }
        }

        changeScaleAlpha();
    }

    private void moveSpinnerByScale(float newScale)
    {
        leftMenuScale = newScale;
        if(leftMenuScale <= 0.005f + SCALE_MIN)
        {
            leftMenuScale = SCALE_MIN;
        }
        else if(leftMenuScale >= 0.995f)
        {
            leftMenuScale = 1;
        }

        changeScaleAlpha();
    }

    private void changeScaleAlpha()
    {
        if(leftMenuScale == SCALE_MIN)
        {
            mainContent.setClickable(true);
        }
        else
        {
            mainContent.setClickable(false);
        }

        float mainContentScale = 1 + SCALE_MIN - leftMenuScale;

        leftMenu.setScaleX(leftMenuScale);
        leftMenu.setScaleY(leftMenuScale);

        mainContent.setScaleX(mainContentScale);
        mainContent.setScaleY(mainContentScale);

        leftMenu.scrollTo((int) (leftMenuW * (2 - leftMenuScale * 2 + 1 - 1 / SCALE_MIN)), 0);
        mainContent.scrollTo((int) ((1 - mainContentScale) / (1 - SCALE_MIN) * (layoutW * (1 / SCALE_MIN - 1) / 2 - leftMenuW) / SCALE_MIN), 0);

        leftMenu.setAlpha(1 - (1 - ALPHA_MIN) * (1 - leftMenuScale) / (1 - SCALE_MIN));
    }

    private final Animation spinnerAnimation = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            float newScale = 1;
            float avgScale = (1 + SCALE_MIN) / 2.0f;
            switch (spinnerType)
            {
                case SPINNER_AUTO: {
                    if(leftMenuScale >= avgScale)
                    {
                        newScale = leftMenuScale + (1 - avgScale) * interpolatedTime;
                    }
                    else
                    {
                        newScale = leftMenuScale - (1 - avgScale) * interpolatedTime;
                    }
                    break;
                }
                case SPINNER_CLOSE: {
                    newScale = leftMenuScale - (1 - SCALE_MIN) * interpolatedTime;
                    break;
                }
                case SPINNER_OPEN: {
                    newScale = leftMenuScale + (1 - SCALE_MIN) * interpolatedTime;
                    break;
                }
            }

            moveSpinnerByScale(newScale);
        }
    };

    private void autoSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_AUTO;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (10000 / (1 - SCALE_MIN) * Math.min(leftMenuScale - SCALE_MIN, 1 - leftMenuScale) + 0.5f));
        spinnerAnimation.setInterpolator(mDecelerateInterpolator);
        this.startAnimation(spinnerAnimation);
    }

    public void closeSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_CLOSE;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (10000 / (1 - SCALE_MIN) * (leftMenuScale - SCALE_MIN) + 0.5f));
        spinnerAnimation.setInterpolator(mDecelerateInterpolator);
        this.startAnimation(spinnerAnimation);
    }

    public void openSpinner()
    {
        this.clearAnimation();
        spinnerType = SPINNER_OPEN;
        spinnerAnimation.reset();
        spinnerAnimation.setDuration((long) (10000 / (1 - SCALE_MIN) * (1 - leftMenuScale) + 0.5f));
        spinnerAnimation.setInterpolator(mDecelerateInterpolator);
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
                if(xDiff > 0)
                {
                    direction = 1;
                }

                mLastMotionX = x;
                mLastMotionY = y;
                if (xDiff > yDiff * 2 && xDiff > mTouchSlop) {
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
                mIsTouchOnMenu = false;
                if(leftMenuScale == 1 && x >= leftMenuW && y >= layoutH * (1 - SCALE_MIN) / 2 && y <= layoutH * (1 + SCALE_MIN) / 2)
                {
                    return true;
                }
                else if(leftMenuScale == 1 && x < leftMenuW)
                {
                    mIsTouchOnMenu = true;
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mIsTouchOnMenu = false;
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

        if(direction == 1 && canChildScrollLeft())
        {
            mIsBeingDragged = false;
            mIsTouchOnMenu = false;
            return false;
        }

        return mIsBeingDragged && !mIsTouchOnMenu;
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
                if (mIsBeingDragged && !mIsTouchOnMenu) {
                    this.clearAnimation();
                    moveSpinner(deltaX > 0? Math.min(mTouchSlop * 4, deltaX):  Math.max(-mTouchSlop * 4, deltaX));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    mIsTouchOnMenu = false;

                    autoSpinner();
                }
                else if (leftMenuScale == 1) {
                    float touchX = ev.getX();
                    float touchY = ev.getY();
                    if(touchX >= leftMenuW && touchY >= layoutH * (1 - SCALE_MIN) / 2 && touchY <= layoutH * (1 + SCALE_MIN) / 2)
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
                    mIsTouchOnMenu = false;

                    autoSpinner();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return true;
    }

    private boolean canChildScrollLeft()
    {
        int size = mCheckScrollTargets.size();
        for(int i = 0; i < size; i ++)
        {
            View view = mCheckScrollTargets.get(i);
            if(isViewVisible(view) && ViewCompat.canScrollHorizontally(view, -1))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isViewVisible(View view)
    {
        if(view.getVisibility() != View.VISIBLE)
        {
            return false;
        }
        View temp = (View) view.getParent();
        while (temp != null)
        {
            if(temp.getVisibility() != View.VISIBLE)
            {
                return false;
            }
            if(temp.getParent() instanceof View)
            {
                temp = (View) temp.getParent();
            }
            else
            {
                return true;
            }
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

package com.findeway.FloatSatelliteMenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Qing on 2015/4/22.
 */
public class FloatButton extends ImageButton {

    public enum FloatPosition {
        FloatPosition_LT(0),
        FloatPosition_RT(1),
        FloatPosition_LB(2),
        FloatPosition_RB(3);

        /**
         * convert between MenuPosition and int
         */
        private int _value = 3;

        FloatPosition(int value) {
            _value = value;
        }

        public int getValue() {
            return _value;
        }

        public static FloatPosition fromInt(int value) {
            for (FloatPosition position : FloatPosition.values()) {
                if (position.getValue() == value) {
                    return position;
                }
            }
            return null;
        }
    }

    private int mPadding = 5;

    private float mPosX = 0;
    private float mPosY = 0;

    private boolean mClickEnabledFlag = true;

    private FloatPosition mPosition = FloatPosition.FloatPosition_RB;

    private OnPositionUpdateListener mPositionListener = null;

    public FloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FloatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatButton(Context context) {
        this(context, null, 0);
    }

    public void setOnPositionUpdateListener(OnPositionUpdateListener listener) {
        mPositionListener = listener;
    }

    public void initFloatButton(int padding, FloatPosition position) {
        mPadding = padding;
        mPosition = position;
        registerMouseListener();

        int moveX = 0;
        int moveY = 0;

        if(mPosition == FloatPosition.FloatPosition_RB || mPosition == FloatPosition.FloatPosition_RT){
            moveX = getScreenWidth();
        }
        if(mPosition == FloatPosition.FloatPosition_LB || mPosition == FloatPosition.FloatPosition_RB){
            moveY = getScreenHeight();
        }
        updatePosition(this, moveX, moveY);
    }

    public void initFloatButton(int padding){
        initFloatButton(padding,FloatPosition.FloatPosition_RB);
    }

    public void initFloatButton() {
        initFloatButton(mPadding);
    }

    private int getScreenHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int appHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        ;
        return dm.heightPixels - appHeaderHeight;
    }

    private int getScreenWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    protected void setClickEnabled(boolean enabled){
        mClickEnabledFlag = enabled;
    }

    protected boolean isClickEnabled(){
        return mClickEnabledFlag;
    }

    private void registerMouseListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mPosX = motionEvent.getRawX();
                        mPosY = motionEvent.getRawY();
                        setClickEnabled(true);
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int dx = (int) ((int) motionEvent.getRawX() - mPosX);
                        int dy = (int) ((int) motionEvent.getRawY() - mPosY);
                        updatePosition(view, dx, dy);
                        setClickEnabled(dx == 0 && dy == 0);
                    }
                    case MotionEvent.ACTION_UP:{
                        if(isClickEnabled()) {
                            return false;
                        }else {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    protected void updatePosition(View view, int moveX, int moveY) {
        int left = view.getLeft() + moveX;
        int top = view.getTop() + moveY;
        int right = view.getRight() + moveX;
        int bottom = view.getBottom() + moveY;

        final int screenWidth = getScreenWidth();
        final int screenHeight = getScreenHeight();

        //check screen boundry
        if (left < 0) {
            left = 0;
            right = left + view.getWidth();
        }
        if (top < 0) {
            top = 0;
            bottom = top + view.getHeight();
        }
        if (right > screenWidth) {
            right = screenWidth;
            left = right - view.getWidth();
        }
        if (bottom > screenHeight) {
            bottom = screenHeight;
            top = bottom - view.getHeight();
        }
        // update position of view
        view.layout(left, top, right, bottom);
        view.postInvalidate();

        mPosX += moveX;
        mPosY += moveY;
        if (mPositionListener != null) {
            mPositionListener.onPositionUpdate(mPosX, mPosY);
        }
    }

    public interface OnPositionUpdateListener {
        public void onPositionUpdate(float posX, float posY);
    }
}

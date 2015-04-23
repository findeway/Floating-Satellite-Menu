package com.findeway.SatelliteMenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.findeway.SatelliteMenu.R;

/**
 * Created by Qing on 2015/4/22.
 */
public class SatelliteMenu extends ViewGroup implements FloatButton.OnPositionUpdateListener,View.OnClickListener {

    public enum MenuPosition {
        MenuPosition_LT(0),
        MenuPosition_RT(1),
        MenuPosition_LB(2),
        MenuPosition_RB(3);

        /**
         * convert between MenuPosition and int
         */
        private int _value = 3;

        MenuPosition(int value) {
            _value = value;
        }

        public int getValue() {
            return _value;
        }

        public static MenuPosition fromInt(int value) {
            for (MenuPosition position : MenuPosition.values()) {
                if (position.getValue() == value) {
                    return position;
                }
            }
            return null;
        }
    }

    public enum MenuStatus {
        MenuState_Closed(0),
        MenuState_Opened(1);

        private int _value = 0;

        MenuStatus(int value) {
            _value = value;
        }

        public int getValue() {
            return _value;
        }

        public static MenuStatus fromInt(int value) {
            for (MenuStatus satus : MenuStatus.values()) {
                if (satus.getValue() == value) {
                    return satus;
                }
            }
            return null;
        }
    }

    /**
     * initial position of SatelliteMenu
     */
    private MenuPosition mPosition = MenuPosition.MenuPosition_RB;

    /**
     * indicates that menu is expanded or not
     */
    private MenuStatus mStatus = MenuStatus.MenuState_Closed;

    /**
     * distance between submenu item and main menu item
     */
    private int mRadius = 50;

    private FloatButton mSwitchButton = null;

    public SatelliteMenu(Context context) {
        this(context, null, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyleAttr, 0);
        mPosition = MenuPosition.fromInt(attributeArray.getInt(R.styleable.SatelliteMenu_position, MenuPosition.MenuPosition_RB.getValue()));
        attributeArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            //measure child
            measureChild(getChildAt(childIndex), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            // layout main switch button
            initSwitchButton();
            // layout sub-menu items
            updateChildrenLayout();
        }
    }

    protected void initSwitchButton(){
        if(mSwitchButton == null){
            mSwitchButton = (FloatButton) getChildAt(0);
            mSwitchButton.setOnPositionUpdateListener(this);
            mSwitchButton.initFloatButton();
            mSwitchButton.setOnClickListener(this);
            initSwitchButtonLayout();
        }
    }

    protected void initSwitchButtonLayout() {
        if (mSwitchButton != null) {
            int left = 0;
            int top = 0;
            int measuredButtonWidth = mSwitchButton.getMeasuredWidth();
            int measuredButtonHeight = mSwitchButton.getMeasuredHeight();
            switch (mPosition) {
                case MenuPosition_LT: {
                    left = 0;
                    top = 0;
                    break;
                }
                case MenuPosition_RT: {
                    left = getMeasuredWidth() - measuredButtonWidth;
                    top = 0;
                    break;
                }
                case MenuPosition_LB: {
                    left = 0;
                    top = getMeasuredHeight() - measuredButtonHeight;
                    break;
                }
                case MenuPosition_RB: {
                    left = getMeasuredWidth() - measuredButtonWidth;
                    top = getMeasuredHeight() - measuredButtonHeight;
                    break;
                }
            }
            mSwitchButton.layout(left, top, left + measuredButtonWidth, top + measuredButtonHeight);
        }
    }

    protected void updateChildrenLayout(){
        int childCount = getChildCount();
        for (int childIndex = 1; childIndex < childCount; childIndex++) {
            View childView = getChildAt(childIndex);
            int childLeft = (int) (mRadius * Math.sin(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));
            int childTop = (int) (mRadius * Math.cos(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //calcute position of y-coordinate
            if (mPosition == MenuPosition.MenuPosition_LB || mPosition == MenuPosition.MenuPosition_RB) {
                childTop = (mSwitchButton.getTop() + mSwitchButton.getBottom() )/2 - childHeight - childTop;
            } else {
                childTop = (mSwitchButton.getTop() + mSwitchButton.getBottom() )/2 + childTop;
            }

            //calcute position of y-coordinate
            if (mPosition == MenuPosition.MenuPosition_RT || mPosition == MenuPosition.MenuPosition_RB) {
                childLeft = (mSwitchButton.getLeft() + mSwitchButton.getRight())/2 - childWidth - childLeft;
            } else {
                childLeft = (mSwitchButton.getLeft() + mSwitchButton.getRight())/2 + childLeft;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPositionUpdate(float posX, float posY) {
        if(posX <= this.getMeasuredWidth() / 2 && posY <= this.getMeasuredHeight() / 2){
            mPosition = MenuPosition.MenuPosition_LT;
        }else if(posX > this.getMeasuredWidth() / 2 && posY <= this.getMeasuredHeight() / 2){
            mPosition = MenuPosition.MenuPosition_RT;
        }else if(posX <= this.getMeasuredWidth() / 2 && posY > this.getMeasuredHeight() / 2){
            mPosition = MenuPosition.MenuPosition_LB;
        }else if(posX > this.getMeasuredWidth() / 2 && posY > this.getMeasuredHeight() / 2){
            mPosition = MenuPosition.MenuPosition_RB;
        }
        updateChildrenLayout();
    }

    public interface OnMenuItemClickListener {
        public void OnMenuItemClick(int itemId);
    }
}

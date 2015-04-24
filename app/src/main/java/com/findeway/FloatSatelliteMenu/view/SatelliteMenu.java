package com.findeway.FloatSatelliteMenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.findeway.FloatSatelliteMenu.R;

/**
 * Created by Qing on 2015/4/22.
 */
public class SatelliteMenu extends ViewGroup implements FloatButton.OnPositionUpdateListener, View.OnClickListener {

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
     * initial position of FloatSatelliteMenu
     */
    private MenuPosition mSwitchPosition = MenuPosition.MenuPosition_RB;

    /**
     * indicates that menu is expanded or not
     */
    private MenuStatus mStatus = MenuStatus.MenuState_Closed;

    /**
     * distance between submenu item and main menu item
     */
    private int mRadius = 50;

    private FloatButton mSwitchButton = null;

    private OnMenuItemClickListener mMenuItemClickListener = null;

    public SatelliteMenu(Context context) {
        this(context, null, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyleAttr, 0);
        mSwitchPosition = MenuPosition.fromInt(attributeArray.getInt(R.styleable.SatelliteMenu_position, MenuPosition.MenuPosition_RB.getValue()));
        attributeArray.recycle();
    }

    /**
     * indicates that satellitemenu is closed or expanded
     * @return
     */
    public boolean isMenuClosed(){
        return mStatus == MenuStatus.MenuState_Closed;
    }

    protected void initMenuItems(){
        registerChildClickListener();
        updateMenuItemsStatus();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener){
        mMenuItemClickListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initMenuItems();
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

    //initizlization can be done for only once;
    protected void initSwitchButton() {
        if (mSwitchButton == null) {
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
            switch (mSwitchPosition) {
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

    protected void updateChildrenLayout() {
        int childCount = getChildCount();
        for (int childIndex = 1; childIndex < childCount; childIndex++) {
            View childView = getChildAt(childIndex);
            int childLeft = (int) (mRadius * Math.sin(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));
            int childTop = (int) (mRadius * Math.cos(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //calcute position of y-coordinate
            if (mSwitchPosition == MenuPosition.MenuPosition_LB || mSwitchPosition == MenuPosition.MenuPosition_RB) {
                childTop = mSwitchButton.getBottom() - childHeight - childTop;
            } else {
                childTop = mSwitchButton.getTop() + childTop;
            }

            //calcute position of y-coordinate
            if (mSwitchPosition == MenuPosition.MenuPosition_RT || mSwitchPosition == MenuPosition.MenuPosition_RB) {
                childLeft = mSwitchButton.getRight() - childWidth - childLeft;
            } else {
                childLeft = mSwitchButton.getLeft() + childLeft;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
    }

    @Override
    public void onClick(View view) {
        toggleMenu();
    }

    protected void toggleMenu(){
        doToggleAnimation(300);
        //expand or close satellite [ppmenu;
        updateMenuStatus();
        updateMenuItemsStatus();
    }

    protected void hideMenuItems(){
        updateMenuStatus();
        updateMenuItemsStatus();
    }

    @Override
    public void onPositionUpdate(float posX, float posY) {
        if (posX <= this.getMeasuredWidth() / 2 && posY <= this.getMeasuredHeight() / 2) {
            mSwitchPosition = MenuPosition.MenuPosition_LT;
        } else if (posX > this.getMeasuredWidth() / 2 && posY <= this.getMeasuredHeight() / 2) {
            mSwitchPosition = MenuPosition.MenuPosition_RT;
        } else if (posX <= this.getMeasuredWidth() / 2 && posY > this.getMeasuredHeight() / 2) {
            mSwitchPosition = MenuPosition.MenuPosition_LB;
        } else if (posX > this.getMeasuredWidth() / 2 && posY > this.getMeasuredHeight() / 2) {
            mSwitchPosition = MenuPosition.MenuPosition_RB;
        }
        updateChildrenLayout();
    }

    protected void doToggleAnimation(int duration){
        if (mSwitchButton != null) {
            rotateSwitchButton(mSwitchButton, 0f, 360f, duration);
        }
        doChildrenToggleAnimation(duration);
    }
    /**
     * rotate switch button
     *
     * @param view     target view
     * @param start    start angle
     * @param end      end angle
     * @param duration time for rotation
     */
    protected void rotateSwitchButton(View view, float start, float end, int duration) {
        RotateAnimation animation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private void updateMenuStatus() {
        //update menu status
        mStatus = isMenuClosed() ? MenuStatus.MenuState_Opened : MenuStatus.MenuState_Closed;
    }

    /**
     * start menu items animation
     * @param duration
     */
    protected void doChildrenToggleAnimation(int duration){
        int childCount = getChildCount();
        for(int childIndex = 1; childIndex < childCount; childIndex++){
            View childView = getChildAt(childIndex);
            AnimationSet animationSet = new AnimationSet(true);
            //rotate
            RotateAnimation rotateAnimation = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setFillAfter(true);
            animationSet.addAnimation(rotateAnimation);
            //translate
            TranslateAnimation translateAnimation = null;
            int childLeft = (int) (mRadius * Math.sin(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));
            int childTop = (int) (mRadius * Math.cos(Math.PI / 2 / (childCount - 2) * (childIndex - 1)));

            //move direction
            int xDirection = 1;
            int yDirection = 1;
            if(mSwitchPosition == MenuPosition.MenuPosition_LT || mSwitchPosition == MenuPosition.MenuPosition_LB){
                xDirection = -1;
            }
            if(mSwitchPosition == MenuPosition.MenuPosition_LT || mSwitchPosition == MenuPosition.MenuPosition_RT) {
                yDirection = -1;
            }
            int endX = 0;
            int endY = 0;
            int deltaX = mSwitchButton.getWidth() / 2 - childView.getWidth()/2;
            int deltaY = mSwitchButton.getHeight() / 2 - childView.getHeight()/2;

            if(isMenuClosed()){
                //to expand
                translateAnimation = new TranslateAnimation(endX + xDirection * childLeft - xDirection * deltaX,endX, endY + yDirection* childTop - yDirection * deltaY, endY);
            }else{
                //to close
                translateAnimation = new TranslateAnimation(endX, xDirection * childLeft + endX - xDirection* deltaX, endY,endY + yDirection* childTop - yDirection*deltaY);
            }
            translateAnimation.setDuration(duration);
            translateAnimation.setFillAfter(true);
            translateAnimation.setStartOffset(childIndex * 100 / childCount);
            animationSet.addAnimation(translateAnimation);
            //start animation
            childView.startAnimation(animationSet);

        }
    }

    /**
     * show animation when item clicked
     */
    protected void doMenuItemClickAnimation(int targetChildIndex){
        ScaleAnimation targetAnimation = new ScaleAnimation(1f,1.5f,1f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        targetAnimation.setDuration(300);
        targetAnimation.setFillAfter(true);
        final View targetChildView = getChildAt(targetChildIndex);
        if(targetChildView != null) {
            targetAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    targetChildView.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            targetChildView.startAnimation(targetAnimation);
        }
        for(int childIndex = 1; childIndex < getChildCount(); childIndex++){
            if(childIndex != targetChildIndex){
                ScaleAnimation scaleSmallnimation = new ScaleAnimation(1f,0.8f,1f,0.8f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                scaleSmallnimation.setDuration(300);
                scaleSmallnimation.setFillAfter(true);
                final View childView = getChildAt(childIndex);
                if(childView != null) {
                    scaleSmallnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            childView.clearAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    childView.startAnimation(scaleSmallnimation);
                }
            }
        }
    }

    /**
     * update menu items' visibility and clickable attribute;
     */
    private void updateMenuItemsStatus(){
        for(int childIndex = 1; childIndex < getChildCount(); childIndex++){
            getChildAt(childIndex).setClickable(isMenuClosed() ? false : true);
            getChildAt(childIndex).setVisibility(isMenuClosed()?GONE:VISIBLE);
        }
    }

    protected void registerChildClickListener(){
        for(int childIndex = 1; childIndex < getChildCount(); childIndex++){
            final int finalChildIndex = childIndex;
            getChildAt(childIndex).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMenuItemClickListener != null) {
                        mMenuItemClickListener.OnMenuItemClick(finalChildIndex);
                    }
                    doMenuItemClickAnimation(finalChildIndex);
                    hideMenuItems();
                }
            });
        }
    }

    public interface OnMenuItemClickListener {
        public void OnMenuItemClick(int itemId);
    }
}

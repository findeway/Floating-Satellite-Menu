package com.findeway.SatelliteMenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.findeway.SatelliteMenu.R;

/**
 * Created by Qing on 2015/4/22.
 */
public class SatelliteMenu extends ViewGroup {

    public enum MenuPosition{
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
            for(MenuPosition position : MenuPosition.values()){
                if(position.getValue() == value){
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
            for(MenuStatus satus : MenuStatus.values()){
                if(satus.getValue() == value){
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

    private FloatButton mMainButton = null;

    public SatelliteMenu(Context context) {
        this(context, null, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics());
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SatelliteMenu,defStyleAttr,0);
        mPosition = MenuPosition.fromInt(attributeArray.getInt(R.styleable.SatelliteMenu_position, MenuPosition.MenuPosition_RB.getValue()));
        attributeArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public interface OnMenuItemClickListener{
        public void OnMenuItemClick();
    }
}

# Floating-Satellite-Menu
Dragable satellite menu for android

Inspired by android-satellite-menu; I made a floating android satellite menu, you can put it anywhere dynamically.

A layout file for Floating-Satellite-Menu may be like this:

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:samenu="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.findeway.SatelliteMenu.view.SatelliteMenu
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        samenu:position="right_bottom"
        samenu:radius="100dp">

        <com.findeway.SatelliteMenu.view.FloatButton
            android:id="@+id/satelliteMainButton"
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:background="@drawable/switch_button" />

        <ImageButton
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:background="@drawable/yellow_button"
            android:tag="item1" />
        <ImageButton
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:background="@drawable/green_button"
            android:tag="item2" />
        <ImageButton
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:background="@drawable/blue_button"
            android:tag="item3" />
        <ImageButton
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:background="@drawable/ygreen_button"
            android:tag="item3" />
        <ImageButton
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:background="@drawable/gblue_button"
            android:tag="item3" />
    </com.findeway.SatelliteMenu.view.SatelliteMenu>
</RelativeLayout>

Note:
  The first child of SatelliteMenu must be a FloatButton; Otherwise it won't work;
  but other children can be views of any kind;

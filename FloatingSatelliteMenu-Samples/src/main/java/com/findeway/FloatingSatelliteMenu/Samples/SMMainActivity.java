package com.findeway.floatingsatellitemenu.Samples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.findeway.floatingsatellitemenu.SatelliteMenu;

public class SMMainActivity extends Activity implements SatelliteMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smmain);
        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.satelliteMenu);
        menu.setOnMenuItemClickListener(this);
    }

    @Override
    public void OnMenuItemClick(int itemId, View view) {
        Log.i("item clicked", String.valueOf(itemId));
    }
}

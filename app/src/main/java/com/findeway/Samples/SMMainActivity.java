package com.findeway.Samples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.findeway.FloatSatelliteMenu.R;
import com.findeway.FloatSatelliteMenu.view.SatelliteMenu;

public class SMMainActivity extends Activity implements SatelliteMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smmain);
        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.satelliteMenu);
        menu.setOnMenuItemClickListener(this);
    }

    @Override
    public void OnMenuItemClick(int itemId) {
        Log.i("item clicked", String.valueOf(itemId));
    }
}

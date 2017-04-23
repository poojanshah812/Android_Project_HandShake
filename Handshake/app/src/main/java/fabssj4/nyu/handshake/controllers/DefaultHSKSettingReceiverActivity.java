package fabssj4.nyu.handshake.controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import fabssj4.nyu.handshake.R;

public class DefaultHSKSettingReceiverActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static Toolbar toolbar;
    private static FragmentDrawer drawerFragment;
    private static final String PREF_ALLOWFLASHLIGHT = "allowflashlight";
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static Switch sw;
    private static Button receiversubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hsksetting_receiver);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        sw = (Switch) findViewById(R.id.receiver_flashlight_switch);

        receiversubmit = (Button) findViewById(R.id.receiver_submit);
        receiversubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(PREF_ALLOWFLASHLIGHT,sw.isChecked());
                editor.commit();
            }
        });


    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}

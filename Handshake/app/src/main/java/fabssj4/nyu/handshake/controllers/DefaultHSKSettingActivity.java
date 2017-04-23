package fabssj4.nyu.handshake.controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.HashMap;

import fabssj4.nyu.handshake.R;

public class DefaultHSKSettingActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static Toolbar toolbar;
    private static FragmentDrawer drawerFragment;

    private static SeekBar seekBarDuration;
    private static SeekBar seekBarVibration;
    private static Button piano1;
    private static Button piano2;
    private static Button piano3;
    private static Button piano4;
    private static RadioGroup flashlightgroup;
    private static RadioButton currentflashlightsetting;
    private static Button submitbtn;

    private static int pattern;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static final String PREF_VIBRATIONDURATION = "duration";
    private static final String PREF_MILLISVIBRATION = "millisvibration";
    private static final String PREF_PATTERN = "pattern";
    private static final String PREF_PATTERNDELAY = "patterndelay";
    private static final String PREF_PATTERNFLASHLIGHT = "patternflashlight";
    private static final String PREF_TIMESTAMP = "timestamp";
    private static final String PREF_IP = "ip";

    private static HashMap<String,Object> storeTemp = new HashMap<String,Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsksetting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        /*editor.clear().commit();
        System.exit(0);*/

        seekBarDuration = (SeekBar) findViewById(R.id.durationvibrationip);
        seekBarDuration.setMax( (60 - 5) / 1 ); //(MAX-MIN)/STEP
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    storeTemp.put(PREF_VIBRATIONDURATION, 5 + (progress * 1)); //(MIN+(progress*STEP))
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarVibration = (SeekBar) findViewById(R.id.weightvibrationip);
        seekBarVibration.setMax((700 - 100) / 20); //(MAX-MIN)/STEP
        seekBarVibration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    storeTemp.put(PREF_MILLISVIBRATION, 100 + (progress * 20)); //(MIN+(progress*STEP))
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        piano1 = (Button) findViewById(R.id.pianobtn1);
        piano1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                piano1.setPressed(!piano1.isPressed());
                return true;
            }
        });

        piano2 = (Button) findViewById(R.id.pianobtn2);
        piano2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                piano2.setPressed(!piano2.isPressed());
                return true;
            }
        });

        piano3 = (Button) findViewById(R.id.pianobtn3);
        piano3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                piano3.setPressed(!piano3.isPressed());
                return true;
            }
        });

        piano4 = (Button) findViewById(R.id.pianobtn4);
        piano4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                piano4.setPressed(!piano4.isPressed());
                return true;
            }
        });

        flashlightgroup = (RadioGroup) findViewById(R.id.radioFlashlight);
        currentflashlightsetting = getCheckedRadioButton(flashlightgroup);

        submitbtn = (Button) findViewById(R.id.sender_submit);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern = Integer.valueOf(String.valueOf((piano1.isPressed() ? 1 : 0) + (piano2.isPressed()?1:0) + (piano3.isPressed()?1:0)+ (piano4.isPressed()?1:0)),16);
                editor.putInt(PREF_PATTERN,pattern);
                editor.putInt(PREF_VIBRATIONDURATION,(int)storeTemp.get(PREF_VIBRATIONDURATION));
                editor.putInt(PREF_MILLISVIBRATION,(int)storeTemp.get(PREF_MILLISVIBRATION));
                editor.putInt(PREF_PATTERNDELAY,25);
                editor.putInt(PREF_PATTERNFLASHLIGHT,getCheckedRadioButton(flashlightgroup).getText().toString().equalsIgnoreCase("constant")?0:1);
                editor.commit();

            }
        });


    }

    public RadioButton getCheckedRadioButton(RadioGroup r){
        return (RadioButton)findViewById(r.getCheckedRadioButtonId());
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}

package com.example.cynosure;

import android.R.integer;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class Frag_Vibrations extends Fragment implements
		OnCheckedChangeListener 
		{
	ImageButton vib1;
	ImageButton vib2;
	ImageButton vib3;
	ImageButton vib4;
	Switch sw;
	Boolean feel=false;

	int status;
	// --------------------------------------------//
	Vibrator mVibrator;
	int dot = 200; // Length of a Morse Code "dot" in milliseconds
	int dash = 500; // Length of a Morse Code "dash" in milliseconds
	int longdash = 900;
	int short_gap = 200; // Length of Gap Between dots/dashes
	int medium_gap = 500; // Length of Gap Between Letters
	int long_gap = 1000; // Length of Gap Between Words
	long[] pattern1 = { 0, // Start immediately
			dash, short_gap,dash,short_gap,dash,short_gap,dash,short_gap,short_gap, dash, short_gap, longdash,};    
	long[] pattern2 = { 0, // Start immediately
			dash, short_gap,longdash,short_gap,longdash,dash, short_gap, dash, short_gap,longdash}; //dash, short_gap, dash, short_gap, dash, short_gap//
	long[] pattern3 = { 0, // Start immediately
			dash,long_gap,dash,long_gap,dash,long_gap,longdash,dash,long_gap,dash,long_gap,dash,long_gap,dash,long_gap,longdash,dash,long_gap,dash,long_gap,dash,long_gap,dash,long_gap,longdash,dash,long_gap, };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)

	{
		View v = inflater.inflate(R.layout.activity_main1, container, false);
		configureImageButton(v);
		// Find button 1
		vib1 = (ImageButton) v.findViewById(R.id.imageButton1);
		// Find button 2
		vib2 = (ImageButton) v.findViewById(R.id.imageButton2);
		// Find button 3
		vib3 = (ImageButton) v.findViewById(R.id.imageButton3);
		// Find button 4
		vib4 = (ImageButton) v.findViewById(R.id.imageButton4);
		// Create Vibrator instance for current context
		mVibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		sw = (Switch) v.findViewById(R.id.switchvibration);

		sw.setOnCheckedChangeListener(this);
		// Click Listener for button1
		vib1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) {
				vib1.setImageResource(R.drawable.vibrationselceted);
				vib2.setImageResource(R.drawable.vibration1);
				vib3.setImageResource(R.drawable.vibration1);
				vib4.setImageResource(R.drawable.vibration1);
				if (feel)
					mVibrator.vibrate(300);
				status = 0;
				setpreference();
			}
		});
		// Click Listener for button2
		vib2.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				vib1.setImageResource(R.drawable.vibration1);
				vib2.setImageResource(R.drawable.vibrationselceted);
				vib3.setImageResource(R.drawable.vibration1);
				vib4.setImageResource(R.drawable.vibration1);
				if (feel)

					mVibrator.vibrate(pattern1, -1);
				status = 1;
				setpreference();
			}
		});
		// Click Listener for button3

		vib3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vib1.setImageResource(R.drawable.vibration1);
				vib2.setImageResource(R.drawable.vibration1);
				vib3.setImageResource(R.drawable.vibrationselceted);
				vib4.setImageResource(R.drawable.vibration1);
				if (feel)
					mVibrator.vibrate(pattern2, -1);
				status = 2;
				setpreference();
			}
		});
		// Click Listener for button4
		vib4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vib1.setImageResource(R.drawable.vibration1);
				vib2.setImageResource(R.drawable.vibration1);
				vib3.setImageResource(R.drawable.vibration1);
				vib4.setImageResource(R.drawable.vibrationselceted);
				if (feel)
					mVibrator.vibrate(pattern3, -1);
				status = 3;
				setpreference();
			}
		});
		return v;
	}

	private void setpreference()
	{
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Editor editor = preference.edit();
		editor.putInt("statusVibration", status);
		editor.commit();
	}
	private void configureImageButton(View v) {
		ImageButton btn1 = (ImageButton) v.findViewById(R.id.imageButton1);

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						"you have selected the first pattern",
						Toast.LENGTH_SHORT).show();

				ImageButton btn1 = (ImageButton) v
						.findViewById(R.id.imageButton1);
				btn1.setImageResource(R.drawable.vibrationselceted);
				
			}
		});

		ImageButton btn2 = (ImageButton) v.findViewById(R.id.imageButton2);
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						"you have selected the second pattern",
						Toast.LENGTH_SHORT).show();

				ImageButton btn2 = (ImageButton) v
						.findViewById(R.id.imageButton2);
				btn2.setImageResource(R.drawable.vibrationselceted);
			}
		});

		ImageButton btn3 = (ImageButton) v.findViewById(R.id.imageButton3);
		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						"you have selected the third pattern",
						Toast.LENGTH_SHORT).show();

				ImageButton btn3 = (ImageButton) v
						.findViewById(R.id.imageButton3);
				btn3.setImageResource(R.drawable.vibrationselceted);
			}
		});

		ImageButton btn4 = (ImageButton) v.findViewById(R.id.imageButton4);
		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						"you have selected the fourth pattern",
						Toast.LENGTH_SHORT).show();

				ImageButton btn4 = (ImageButton) v
						.findViewById(R.id.imageButton4);
				btn4.setImageResource(R.drawable.vibrationselceted);
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {

			feel = true;
		}

		else {
			feel = false;

		}
	}
}

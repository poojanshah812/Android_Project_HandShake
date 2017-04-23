package com.example.cynosure;

import java.util.logging.Logger;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class Frag_Flashlight extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	// Set boolean flag when torch is turned on/off
	private boolean isFlashOn = false;
	// Create camera object to access flashlight//
	public Camera camera;
	// Torch button
	private Button button;
	Parameters p;
	ImageButton btn2, btn3, btn1;
	Switch swf;
	Boolean feel = false;
	Boolean flicker = false;
	int status = 0;
	Thread th;

	//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_main2, container, false);

		btn1 = (ImageButton) v.findViewById(R.id.imageButton1);
		btn1.setOnClickListener(this);

		// Refer the button control
		btn2 = (ImageButton) v.findViewById(R.id.imageButton2);
		btn2.setOnClickListener(this);

		btn3 = (ImageButton) v.findViewById(R.id.imageButton3);
		btn3.setOnClickListener(this);

		// Context object to refer context of the application
		Context context = getActivity();
		swf = (Switch) v.findViewById(R.id.switchflash);
		swf.setOnCheckedChangeListener(this);
		// Retrieve application packages that are currently installed
		// on the device which includes camera, GPS etc.
		
		PackageManager pm = context.getPackageManager();

		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Logger message;
			Log.e("err", "Device has no camera!");
			// Toast a message to let the user know that camera is not
			// installed in the device
			Toast.makeText(getActivity().getApplicationContext(),
					"Your device doesn't have camera!", Toast.LENGTH_SHORT)
					.show();
			// Return from the method, do nothing after this code block
			return v;
		}

		try {
			if (camera == null) {

				camera = Camera.open();
				p = camera.getParameters();
			}

		} catch (Exception e) {
			Log.i("Exception is ", "" + e);
		}

		return v;
	}

	private void configureImageButton(View v) {

	}

	protected void onStop(View v) {
		super.onStop();
		if (camera != null)
		{
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			// Pass the parameter ti camera object
			camera.setParameters(p);
			camera.release();
			camera = null;
		}
	}

	public void turnOnFlash(boolean isFlashOn) 
	{

		// If Flag is set to true
		if (isFlashOn) {
			Log.i("info", "torch is turned on!");
			
			if (p != null)
			{
				// Set the flashmode to on
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				// Pass the parameter ti camera object
				camera.setParameters(p);
				// Set flag to true
				// Set the button text to Torch-OFF
				// button.setText("Torch-OFF");
				
			}
		}
		// If Flag is set to false
		else {
			// Set the flashmode to off
			if (p != null) {
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				// Pass the parameter ti camera object
				camera.setParameters(p);
				// Set flag to false
				// Set the button text to Torcn-ON
				// button.setText("Torch-ON");
				
			}
		}

		
	}

	@Override
	public void onClick(View v) {
		
			switch (v.getId()) 
			{
			case R.id.imageButton2:

				btn2 = (ImageButton) v.findViewById(R.id.imageButton2);
				btn2.setImageResource(R.drawable.flashbutton2changed);
				btn3.setImageResource(R.drawable.flashbutton3normal);
				btn1.setImageResource(R.drawable.flashbutton1normal);
				flicker = false;
				if (feel)
				{
					turnOnFlash(true);
				}
				
				status = 1;
				break;
			case R.id.imageButton3:

				btn3 = (ImageButton) v.findViewById(R.id.imageButton3);
				btn3.setImageResource(R.drawable.flashbutton3changed);
				btn2.setImageResource(R.drawable.flashbutton2normal);
				btn1.setImageResource(R.drawable.flashbutton1normal);
				flicker = false;
				if (feel)
				{
					turnOnFlash(false);
				}
				
				
				status = 2;
				break;
			case R.id.imageButton1:
				btn1 = (ImageButton) v.findViewById(R.id.imageButton1);
				btn1.setImageResource(R.drawable.flashbutton1changed);

				btn2.setImageResource(R.drawable.flashbutton2normal);
				flicker = true;
				status = 0;
				th = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (flicker) 
						{
							if (feel)
							{
								turnOnFlash(true);
							}
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (feel)
							{
								turnOnFlash(false);
							}
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				th.start();
				break;
				
			}
			
			SharedPreferences preference = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			Editor editor = preference.edit();
			editor.putInt("statusFlash", status);
			editor.commit();
		
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

/*
 * package com.example.cynosure;
 * 
 * import android.app.Fragment; import android.hardware.Camera.Parameters;
 * import android.os.Bundle; import android.util.Log; import
 * android.view.LayoutInflater; import android.view.Menu; import
 * android.view.View; import android.view.ViewGroup; import
 * android.widget.ImageButton; import android.widget.Toast;
 * 
 * public class Frag_Flashlight extends Fragment {
 * 
 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
 * container, Bundle savedInstanceState) { View v =
 * inflater.inflate(R.layout.activity_main2, container, false); return v; } }
 */
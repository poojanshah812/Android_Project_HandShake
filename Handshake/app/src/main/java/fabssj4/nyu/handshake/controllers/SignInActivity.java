package fabssj4.nyu.handshake.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import fabssj4.nyu.handshake.R;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 200;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7;

    private static TelephonyManager tMgr;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static GoogleApiClient mGoogleApiClient;

    private static final String PREF_CONTACT = "contact";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_DISPLAYNAME = "displayname";
    private static final String PREF_ISSIGNEDIN = "issignedin";
    private static final String PREF_ISINITIALIZED = "isinitialized";
    private static final String PREF_PHOTOURI = "photouri";
    private static final String PREF_REGISTRATIONTIMESTAMP = "registrationtimestamp";
    private static final String PREF_REGISTRATIONIP = "registrationip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        /*editor.clear().commit();
        System.exit(0);*/


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }else{
            tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            editor.putString(PREF_CONTACT, tMgr.getLine1Number());
            editor.commit();
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (preferences.getBoolean(PREF_ISINITIALIZED, false) && preferences.getBoolean(PREF_ISSIGNEDIN, false)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        Log.i(TAG, "Initialized " + PREF_ISINITIALIZED + preferences.contains(PREF_ISINITIALIZED) + "" + preferences.getBoolean(PREF_ISSIGNEDIN, false));

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Hello");
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                    editor.putString(PREF_CONTACT, tMgr.getLine1Number());
                    editor.commit();

                    Log.i(TAG, tMgr.getLine1Number());
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i(TAG, "No Permissions Granted");
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "Contacts Permission is required to move ahead. Please allow access")) {
                        Toast.makeText(this, "Exiting Handshake :(", Toast.LENGTH_SHORT).show();
                        editor.clear().commit();
                        finish();
                    }

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Log.i(TAG, "Granted Read Contacts");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i(TAG, "No Permissions Granted");
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "Contacts Permission is required to move ahead. Please allow access")) {
                        Toast.makeText(this, "Exiting Handshake :(", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Failed due to " + connectionResult.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            if (!preferences.contains(PREF_ISINITIALIZED)) {
                editor.putBoolean(PREF_ISINITIALIZED, true);
                editor.putLong(PREF_REGISTRATIONTIMESTAMP, System.currentTimeMillis());
                editor.putString(PREF_REGISTRATIONIP, getLocalIpAddress());
            }
            editor.putString(PREF_DISPLAYNAME, acct.getDisplayName());
            editor.putString(PREF_EMAIL, acct.getEmail());
            editor.putBoolean(PREF_ISSIGNEDIN, true);
            //editor.putString(PREF_PHOTOURI, acct.getPhotoUrl().toString());
            editor.commit();
            Log.i(TAG, acct.getDisplayName());
            Log.i(TAG, acct.getEmail());
            new SignInRegistrationServerAsyncTask().execute(tMgr.getLine1Number(), acct.getEmail(), acct.getDisplayName(), getLocalIpAddress());
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Signed out, show unauthenticated UI.
            Log.i(TAG, "//Signed Out, show unauthenticated UI");
            if (preferences.getBoolean(PREF_ISINITIALIZED, false)) {
                editor.putBoolean(PREF_ISSIGNEDIN, false);
                editor.commit();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    class SignInRegistrationServerAsyncTask extends AsyncTask<String, Void, Boolean> {
        private URL saveurl;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String postdata = "contact=" + params[0] + "&email=" + params[1] + "&displayname=" + params[2] + "&ip=" + params[3];
                byte[] postdatabytes = postdata.getBytes(StandardCharsets.UTF_8);
                saveurl = new URL("http://devs.bitnamiapp.com/nyu/handshake/register.php");
                HttpURLConnection connection = (HttpURLConnection) saveurl.openConnection();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.write(postdatabytes);

                if (connection.getResponseCode() == 200) {
                    String op;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((op = br.readLine()) != null)
                        Log.i(TAG, "AsyncTaskOP/t" + op);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}

package fabssj4.nyu.handshake.controllers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import fabssj4.nyu.handshake.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, RecentSentFragment.OnFragmentInteractionListener, RecentReceivedFragment.OnFragmentInteractionListener {
    private static final int PICK_CONTACT = 500;
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private android.support.v4.app.Fragment recentsent;
    private android.support.v4.app.Fragment recentreceived;
    private FragmentDrawer drawerFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    private HandshakeAppDbHelper hadh;

    private static String contactId;
    private static String name;

    private static ArrayList<String> phoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        //Log.i(TAG,deleteDatabase("HandshakeAppDb.sqlite")+"");
        hadh = new HandshakeAppDbHelper(this);



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentSentFragment(), "Recent Sent");
        adapter.addFragment(new RecentReceivedFragment(), "Recent Received");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_logout:
                break;
            case R.id.action_exit:
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    while (cursor.moveToNext()) {
                        contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            phoneNumber = new ArrayList<String>();
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\+","").replaceAll(" ",""));
                            }
                            phones.close();
                            if(!phoneNumber.isEmpty())
                                new ValidateUserContact().execute(phoneNumber);
                            else Toast.makeText(getApplicationContext(),"User doesn't have Handshake installed!!! Please select another user",Toast.LENGTH_LONG).show();
                            Log.i(TAG, contactId + "/t" + name + "/t" + new JSONArray(phoneNumber) + ContactsContract.CommonDataKinds.Phone.TYPE);
                        }


                    }
                }
                break;
        }
    }

    class ValidateUserContact extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            int b = 0;
            try {
                String postdata = "phonenumber="+params[0];
                byte[] postdatabytes = postdata.getBytes(StandardCharsets.UTF_8);
                URL saveurl = new URL("http://devs.bitnamiapp.com/nyu/handshake/verify.php");
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
                        b=Integer.parseInt(br.readLine());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }

        @Override
        protected void onPostExecute(Integer index) {
            if(index==0)
                Toast.makeText(getApplicationContext(),"User doesn't have Handshake installed!!! Please select another user",Toast.LENGTH_LONG).show();
            else{
                    Long l = hadh.saveSendHandshakeContact(contactId, phoneNumber.get(index), name, getLocalIpAddress());
                    Log.i("asdasdasd",String.valueOf(l));
            }
        }
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


}



package fabssj4.nyu.handshake.controllers;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fabssj4.nyu.handshake.R;

/**
 * Created by FabSSJ4 on 4/6/2016.
 */
public class ProfileListActivity  extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, ProfileListFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    private android.support.v4.app.Fragment profilelistfragment;
    private FragmentDrawer drawerFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_profile_list, new ProfileListFragment()).commit();

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

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
}

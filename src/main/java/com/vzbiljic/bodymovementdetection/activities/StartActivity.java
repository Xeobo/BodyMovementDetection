package com.vzbiljic.bodymovementdetection.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.vzbiljic.bodymovementdetection.R;


public abstract class StartActivity extends AbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected abstract void initializeMenuItems(Menu menu);

    protected abstract int getMainLayout();

    protected abstract void initMainLayout(Bundle savedInstanceState);


    @Override
    protected final void onCreateBase(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMainLayout(savedInstanceState);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        initializeMenuItems(navigationView.getMenu());

        // add NavigationItemSelectedListener to check the navigation clicks
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.i("StartActivity", "BackstackCount = " + getSupportFragmentManager().getBackStackEntryCount());
            if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Odjava?")
                        .setMessage("Da li ste sigurni da želite da zatvorite?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                StartActivity.super.onBackPressed();
                            }
                        }).create();

                dialog.show();

                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setTextColor(Color.RED);
                button.setText("DA");

                button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                button.setTextColor(Color.RED);
                button.setText("NE");
            }else{
                StartActivity.super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

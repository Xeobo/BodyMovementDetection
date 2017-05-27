package com.vzbiljic.bodymovementdetection.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.vzbiljic.bodymovementdetection.MongoDBUtils;
import com.vzbiljic.bodymovementdetection.R;
import com.vzbiljic.bodymovementdetection.fragment.CreateDataFragment;
import com.vzbiljic.bodymovementdetection.fragment.ListDataFragment;

public class ColectDataActivity extends StartFragmentActivity{


    @Override
    protected void onDestroy() {
        MongoDBUtils.deinit();
        super.onDestroy();
    }

    @Override
    protected void initializeMenuItems(Menu menu) {
        menu.add(0,Menu.FIRST, Menu.NONE,"Pokreni");
        menu.add(0,Menu.FIRST + 1,Menu.NONE,"Listaj podatke");
    }

    @Override
    protected int getMainLayout() {
        return R.layout.activity_colect_data;
    }

    @Override
    protected Fragment getMainFragment() {
        return new CreateDataFragment();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1://start stop
                attachFragment(new CreateDataFragment());
                break;
            case 2:
                attachFragment(new ListDataFragment());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}

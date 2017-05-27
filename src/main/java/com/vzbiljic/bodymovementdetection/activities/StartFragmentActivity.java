package com.vzbiljic.bodymovementdetection.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.vzbiljic.bodymovementdetection.R;


/**
 * Created by Xeobo on 3/18/2017.
 */

public abstract class StartFragmentActivity extends StartActivity {

    protected void attachFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
    protected abstract Fragment getMainFragment();

    @Override
    protected final void initMainLayout(Bundle savedInstanceState) {
        Log.i("HiThere","fragment activity create started!");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            Fragment firstFragment = getMainFragment();

            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

            Log.i("HiThere","fragment activity create ended!");

        }
    }
}

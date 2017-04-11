package com.vzbiljic.bodymovementdetection.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vzbiljic.bodymovementdetection.R;

/**
 * Created by vzbiljic on 17.3.17..
 */

public abstract class AbstractFragment extends Fragment {

    protected void changeToFragment(Fragment fragment){


        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

//        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    protected void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState){
        ((TextView)getActivity().findViewById(R.id.mainName)).setText(getHeading());

        return createView(inflater,container,savedInstanceState);
    }

    protected abstract CharSequence getHeading();

    protected abstract View createView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState);
}

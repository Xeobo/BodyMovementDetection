package com.vzbiljic.bodymovementdetection.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.vzbiljic.bodymovementdetection.AxisDiffereceData;
import com.vzbiljic.bodymovementdetection.DatabaseUpdaterService;
import com.vzbiljic.bodymovementdetection.IDatabaseUpdateService;
import com.vzbiljic.bodymovementdetection.R;
import com.vzbiljic.bodymovementdetection.activities.StartActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDataFragment extends AbstractFragment implements ServiceConnection{
    private static final String TAG = CreateDataFragment.class.getName();
    private boolean collectData = false;
    private int state;
    private IDatabaseUpdateService service;
    private View root;

    public CreateDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_crate_data, container, false);

        final Button button = (Button) root.findViewById(R.id.button);
        final Spinner spinner = (Spinner) root.findViewById(R.id.spinner);

        Intent intent = new Intent(getActivity(),DatabaseUpdaterService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData = !collectData;

                try {
                    if (collectData) {
                        if (null != service) {
                            service.setStatus(state);
                            service.start();
                            Button delbt = (Button) root.findViewById(R.id.delete_data_base);
                            Button syncBtn = (Button) root.findViewById(R.id.sync_data_base);
                            delbt.setEnabled(false);
                            syncBtn.setEnabled(false);
                            button.setText("Zaustavi");
                        }
                    } else {
                        if (null != service && service.pause()) {

                            Button delbt = (Button) root.findViewById(R.id.delete_data_base);
                            Button syncBtn = (Button) root.findViewById(R.id.sync_data_base);
                            delbt.setEnabled(true);
                            syncBtn.setEnabled(true);
                            button.setText("Pokreni");

                        }
                    }
                }catch (RemoteException r){
                    r.printStackTrace();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("HiThere","selected: "  + i);
                state = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button delbt = (Button) root.findViewById(R.id.delete_data_base);

        delbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Brisanje")
                        .setMessage("Da li ste sigurni da želite da obrisete sve podatke?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                AxisDiffereceData.deleteAllData();
                            }
                        }).create();

                dialog.show();

                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setTextColor(Color.BLUE);
                button.setText("DA");

                button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                button.setTextColor(Color.BLUE);
                button.setText("NE");
            }
        });


        Button syncBtn = (Button) root.findViewById(R.id.sync_data_base);

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AxisDiffereceData.syncRemoteDatabase();
            }
        });
        Log.i(TAG,"Fragment created");

        return root;

    }

    @Override
    protected CharSequence getHeading() {
        return "Sakupljanje podataka";
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i(TAG,"Service connected");

        service = IDatabaseUpdateService.Stub.asInterface(iBinder);

        try {
            if(service.isServiceActive()){
                Button bt = (Button) root.findViewById(R.id.button);
                Button delbt = (Button) root.findViewById(R.id.delete_data_base);
                delbt.setEnabled(false);

                collectData = true;
                bt.setText("Zaustavi");




            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(this);
        super.onDestroy();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }
}

package com.vzbiljic.bodymovementdetection.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vzbiljic.bodymovementdetection.AxisDiffereceData;
import com.vzbiljic.bodymovementdetection.R;

import java.util.List;


public class ListDataFragment extends Fragment {

    private final List<AxisDiffereceData> datas;
    private String[] desc = {};

    public ListDataFragment() {
        datas = AxisDiffereceData.listAll(AxisDiffereceData.class);
        desc = new String[datas.size()];

        for(int i=0; i<datas.size(); i++){
            desc[i] = datas.get(i).getX() + "";

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_data, container, false);

        ListView listView = (ListView)root.findViewById(R.id.list);

        Log.i("HiThere","DataSize FIRST: " + AxisDiffereceData.listAll(AxisDiffereceData.class).size());

        listView.setAdapter(new ListAdapter(getActivity()));

        // Inflate the layout for this fragment
        return root;
    }


    public class ListAdapter extends ArrayAdapter<String>{

        private final Context context;
        private LayoutInflater inflater;

        public ListAdapter(Context context){
            super(context, R.layout.simple_list_view_item, desc);

            this.context = context;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.simple_list_view_item,null);
            }

            TextView xView = (TextView) convertView.findViewById(R.id.columnX);
            TextView yView = (TextView) convertView.findViewById(R.id.columnY);
            TextView zView = (TextView) convertView.findViewById(R.id.columnZ);
            TextView labelView = (TextView) convertView.findViewById(R.id.label);
            TextView spView = (TextView) convertView.findViewById(R.id.sample_period);



            xView.setText(datas.get(position).getX() + "");
            yView.setText(datas.get(position).getY() + "");
            zView.setText(datas.get(position).getZ() + "");
            labelView.setText(datas.get(position).getLabel() + "");
            spView.setText(datas.get(position).getSamplePeriod() + "");

            Log.i("HiThere","position: " + position);

            return convertView;
        }
    }
}




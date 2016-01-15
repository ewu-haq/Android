package com.example.htq.mapsampleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by qha on 1/14/2016.
 */
public class LocationItemAdapter extends ArrayAdapter<Location> {

    private List<Location> mLocationList;
    private Context mContext;

    public LocationItemAdapter(Context context, int resource, List<Location> objects) {
        super(context, resource, objects);
        mContext = context;
        mLocationList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.location_item_layout, parent,
                    false);
        } else {
            view = convertView;
        }
        Location location = mLocationList.get(position);
        TextView name = (TextView) view.findViewById(R.id.LocationName);
        name.setText(location.LocationName);
        TextView email = (TextView) view.findViewById(R.id.LocationAddress);
        email.setText(location.LocationAddress);

        return view;
    }
}

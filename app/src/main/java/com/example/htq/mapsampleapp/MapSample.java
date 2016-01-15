package com.example.htq.mapsampleapp;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapSample extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleMap mMap;

    private EditText _searchEditText;
    private Button _searchButton;
    private DrawerLayout _drawerLayout;
    private ListView _locationListView;
    private ListView _optionsListView;
    private String[] menuOptions;
    private ArrayAdapter<Location> mAdapter;

    private static final int ERROR_DIALOG_REQUEST = 9000;
    private static final int NUMBER_OF_LOCATIONS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOkay())
        {
            setContentView(R.layout.activity_map);

            if(InitMap())
            {
                Toast.makeText(this,"Mapping is ready to display",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Mapping is NOT connected",Toast.LENGTH_SHORT).show();
            }

        }else
        {
            setContentView(R.layout.activity_map_sample);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayout);
        _optionsListView =(ListView) findViewById(R.id.drawerList);
        menuOptions = getResources().getStringArray(R.array.MenuOptions);
        _optionsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuOptions));
        _optionsListView.setOnItemClickListener(this);

        _searchButton = (Button) findViewById(R.id.searchButton);
        _locationListView = (ListView) findViewById(R.id.locationsListView);
        _searchEditText = (EditText)findViewById(R.id.searchEditText);
        _searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s) {
                // avoid if typing is too short
                if(s.length() <3)
                {
                    return;
                }

                List<Location> locations = new ArrayList<Location>();
                SetLocationsViewAdapter(locations,_locationListView);

                Geocoder gc = new Geocoder(getApplicationContext());
                try {
                    List<Address> list = gc.getFromLocationName(s.toString(),NUMBER_OF_LOCATIONS);
                    for (int i = 0; i < list.size(); i++) {
                        locations.add(new Location(list.get(i).getFeatureName(),String.valueOf(list.get(i).getLatitude())));
                    }
                    SetLocationsViewAdapter(locations,_locationListView);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Search Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void HideSoftKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void SetLocationsViewAdapter(List<Location> locations,ListView listview)
    {
        mAdapter = new LocationItemAdapter(this,R.layout.location_item_layout,locations);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,menuOptions[position] + " is clicked",Toast.LENGTH_SHORT).show();
        SelectItem(position);
    }

    private void SelectItem(int position)
    {
        _optionsListView.setItemChecked(position, true);
        SetTitle(menuOptions[position]);
    }

    private void SetTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_sample, menu);
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

    public boolean servicesOkay() {
        int isMapAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isMapAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isMapAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isMapAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else
        {
            Toast.makeText(this,"Can't Connect to Google Mapping Service",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean InitMap()
    {
        if(mMap == null)
        {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = supportMapFragment.getMap();
        }

        return mMap != null;
    }
}

package com.example.htq.mapsampleapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class MapSample extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleMap mMap;

    private EditText _searchEditText;
    private Button _searchButton;
    private DrawerLayout _drawerLayout;
    private ListView _listView;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menuOptions;

    private static final int ERROR_DIALOG_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOkay())
        {
            setContentView(R.layout.activity_map);

            //inital values
            _drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayout);
            _listView = (ListView) findViewById(R.id.locationsListView);
            _searchEditText = (EditText) findViewById(R.id.searchEditText);
            _searchButton = (Button) findViewById(R.id.searchButton);


            drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayout);
            menuOptions = getResources().getStringArray(R.array.MenuOptions);
            listView = (ListView) findViewById(R.id.drawerList);
            listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menuOptions));
            listView.setOnItemClickListener(this);

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



        // set up search listerner
        _searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> typedCharacters = new ArrayList<String>();
                SetLocationsViewAdapter(typedCharacters);
                for (int i = 0; i < s.length(); i++) {
                    typedCharacters.add(s.subSequence(0,i).toString());
                }

                SetLocationsViewAdapter(typedCharacters);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
    }

    private void SetLocationsViewAdapter(List<String> locations)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, locations);
        _listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,menuOptions[position] + " is clicked",Toast.LENGTH_SHORT).show();
        SelectItem(position);
    }

    private void SelectItem(int position)
    {
        listView.setItemChecked(position,true);
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

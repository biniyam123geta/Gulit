package com.biniyamshopping.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class Catagories extends AppCompatActivity {
    ListView myListView;
    Spinner mySpinner;
    String catagory="";private AdView mAdView;
    ArrayAdapter<CosmicBody> adapter;
    String[] categories={"Select","Cars","Houses","Shoes and Clothing","Mobile","Electronics","Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catagories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();
           //Advertisment

    }
    /*
    Initialize ListView and Spinner, set their adapters and listen to spinner itemSelection events
    */
    private void initializeViews() {

        mySpinner = findViewById(R.id.mySpinner);
        mySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories));

        myListView = findViewById(R.id.myListView);
       myListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCosmicBodies()));

        //spinner selection events
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {
                if (position >= 0 && position < categories.length) {
                    getSelectedCategoryData(position);
                    catagory=categories[(int) itemID];
                   // Toast.makeText(Catagories.this, categories[(int) itemID], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Catagories.this, "Selected Category Does not Exist!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    /*
    Populate an arraylist that will act as our data source.
     */
    private ArrayList<CosmicBody> getCosmicBodies() {
        ArrayList<CosmicBody> data = new ArrayList<>();
        data.clear();

        data.add(new CosmicBody("Cars", 1));
        data.add(new CosmicBody("Motorcycle", 1));
        data.add(new CosmicBody("House For Sale", 2));
        data.add(new CosmicBody("House for Rent", 2));
        data.add(new CosmicBody("Men clothing", 3));
        data.add(new CosmicBody("Men shoes", 3));
        data.add(new CosmicBody("Women clothing", 3));
        data.add(new CosmicBody("Women shoes", 3));
        data.add(new CosmicBody("Kids cloething and shoes", 3));
        data.add(new CosmicBody("Bags", 3));
        data.add(new CosmicBody("Mobile", 4));
        data.add(new CosmicBody("Tablet", 4));
        data.add(new CosmicBody("Accessories", 4));
        data.add(new CosmicBody("Laptop", 5));
        data.add(new CosmicBody("Computer", 5));
        data.add(new CosmicBody("Tv", 5));
        data.add(new CosmicBody("other Electronics", 5));
        data.add(new CosmicBody("Other", 6));
        return data;
    }
    /*
    Get the selected category's cosmic bodies and bind to ListView
     */
    private void getSelectedCategoryData(int categoryID) {
        //arraylist to hold selected cosmic bodies
        ArrayList<CosmicBody> cosmicBodies = new ArrayList<>();
        if(categoryID == 0)
        {
           //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCosmicBodies());
        }else{
            //filter by id
            for (CosmicBody cosmicBody : getCosmicBodies()) {
                if (cosmicBody.getCategoryId() == categoryID) {
                    cosmicBodies.add(cosmicBody);
                }
            }
            //instatiate adapter a
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cosmicBodies);
        }
        //set the adapter to GridView
        myListView.setAdapter(adapter);
    }
}
/*
Data Object class to represent a single Cosmic body. Class has default access modifier
 */
class CosmicBody {
    private String name;
    private int categoryID;

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryID;
    }

    public CosmicBody(String name, int categoryID) {
        this.name = name;
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return name;
    }
}

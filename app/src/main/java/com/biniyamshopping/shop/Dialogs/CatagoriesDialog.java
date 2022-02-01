package com.biniyamshopping.shop.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.biniyamshopping.shop.Catagories;
import com.biniyamshopping.shop.R;
import com.biniyamshopping.shop.Sell;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class CatagoriesDialog extends DialogFragment {
    ListView myListView;
    Spinner mySpinner;
    String catagory="";private AdView mAdView;
    ArrayAdapter<CosmicBody> adapter;
    String[] categories={"Select","Cars","Houses","Shoes and Clothing","Mobile","Electronics","Other"};
    public static CatagoriesDialog newInstance() {
        return new CatagoriesDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.catagories, null);

        //MobileAds.initialize(this,"ca-app-pub-6002206915132015~3669917621");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        myListView=view.findViewById(R.id.myListView);
       // mAdView.loadAd(adRequest);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(Catagories.this,categories[position], Toast.LENGTH_SHORT).show();
                String city="Addis";
                Intent intent=new Intent(getContext(), Sell.class);
                intent.putExtra("cat",catagory);
                intent.putExtra("cit",city);
                startActivity(intent);

//                if (catagoriesDialog == null) {
//                    catagoriesDialog = CatagoriesDialog.newInstance();
//                }
//                catagoriesDialog.show(getChildFragmentManager(), "ddd");

            }
        });
        mySpinner = view.findViewById(R.id.mySpinner);
        mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));

        myListView = view.findViewById(R.id.myListView);
        myListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getCosmicBodies()));

        //spinner selection events
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {
                if (position >= 0 && position < categories.length) {
                    getSelectedCategoryData(position);
                    catagory=categories[(int) itemID];
                    // Toast.makeText(Catagories.this, categories[(int) itemID], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Selected Category Does not Exist!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
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
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cosmicBodies);
        }
        //set the adapter to GridView
        myListView.setAdapter(adapter);
    }
}


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

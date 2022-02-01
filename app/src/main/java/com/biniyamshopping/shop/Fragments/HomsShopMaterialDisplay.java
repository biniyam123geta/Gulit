package com.biniyamshopping.shop.Fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.biniyamshopping.shop.About;
import com.biniyamshopping.shop.Home;
import com.biniyamshopping.shop.ImageAdapter;
import com.biniyamshopping.shop.MainActivity;
import com.biniyamshopping.shop.Messages;
import com.biniyamshopping.shop.R;
import com.biniyamshopping.shop.SearchAdapter;
import com.biniyamshopping.shop.SearchMessage;
import com.biniyamshopping.shop.abut;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import www.sanju.motiontoast.MotionToast;

public class HomsShopMaterialDisplay extends Fragment {
    private HomeShopMaterialViewModel mhomeShopMaterialViewModel;
    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView searchrecycler;
    private List<SearchMessage> searchUploads;
    CardView car, house, cloth, mobile, elec, shoes, food;
    RelativeLayout pay;
    private SearchAdapter searchAdapter;
    public static HomsShopMaterialDisplay newInstance() {
     return new HomsShopMaterialDisplay();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=  inflater.inflate(R.layout.fragment_homs_shop_material_display, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerid);
        car = view.findViewById(R.id.car);
//        house = view.findViewById(R.id.house);
        cloth = view.findViewById(R.id.cloth);
        mobile = view.findViewById(R.id.mobile);
        elec = view.findViewById(R.id.electronics);
        shoes = view.findViewById(R.id.shoes);
        food=view.findViewById(R.id.food);
        searchrecycler=view.findViewById(R.id.searchrecycler);
        searchrecycler.setVisibility(View.GONE);
        searchUploads = new ArrayList<>();
        searchrecycler.setHasFixedSize(true);
        searchrecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchAdapter = new SearchAdapter(requireContext(), searchUploads);
        searchrecycler.setAdapter(searchAdapter);
        setHasOptionsMenu(true);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search("Cars");
            }
        });
//                house.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search("Houses");
//            }
//        });
        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search("Shoes and Clothing");
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search("Mobile");
            }
        });
        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            search("Electronics");
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search("Shoes and Clothing");
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               search("food");
            }
        });
        mhomeShopMaterialViewModel=new ViewModelProvider(requireActivity()).get(HomeShopMaterialViewModel.class);
        mhomeShopMaterialViewModel.init();
        mhomeShopMaterialViewModel.getlivedata().observe(requireActivity(),response->{
           populate(response);

        });
    return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mhomeShopMaterialViewModel.getdata();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem m = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) m.getActionView();
        searchView.setQueryHint("search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchrecycler.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                searchUploads.clear();
                List<Messages> messages = mhomeShopMaterialViewModel.filterbyname(newText);
                if (messages != null) {
                    for (int i = 0; i < messages.size(); i++) {
                        SearchMessage searchMessage = new SearchMessage();
                        searchMessage.setCat(messages.get(i).getCat());
                        searchMessage.setCit(messages.get(i).getCit());
                        searchMessage.setCond(messages.get(i).getCond());
                        searchMessage.setDesc(messages.get(i).getDesc());
                        searchMessage.setImageName(messages.get(i).getImageName());
                        searchMessage.setImageURL(messages.get(i).getImageURL());
                        searchMessage.setKey(messages.get(i).getKey());
                        searchMessage.setPhone(messages.get(i).getPhone());
                        searchMessage.setPrice(messages.get(i).getPrice());
                        searchMessage.setType(messages.get(i).getType());

                        searchUploads.add(searchMessage);
                    }
                    searchAdapter.notifyDataSetChanged();
                } else {
                    showMotionToast((Activity) requireContext(),
                            newText,
                            "There is no data in our database",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM);

                }
                return true;
            }

        });
         super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.About) {
            Intent i = new Intent(requireContext(), abut.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.contact) {
            // Toast.makeText(this, "welcome to contact", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(requireContext(), About.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.logout) {
            Toast.makeText(requireContext(), "Successful Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(requireContext(), MainActivity.class);
            startActivity(i);

        }
        if (item.getItemId() == R.id.car) {
            search("Cars");
        }
        if (item.getItemId() == R.id.house) {
            search("Houses");
        }
        if (item.getItemId() == R.id.other) {
            search("Other");
        }
        //location
        if (item.getItemId() == R.id.addis) {
            locationsearch("Addis Ababa");
        }
        if (item.getItemId() == R.id.adama) {
            locationsearch("Adama");
        }
        if (item.getItemId() == R.id.bahir) {
            locationsearch("Bahir Dar");
        }
        if (item.getItemId() == R.id.dire) {
            locationsearch("Dire Dawa");
        }
        if (item.getItemId() == R.id.awasa) {
            locationsearch("Awassa");
        }
        if (item.getItemId() == R.id.gondar) {
            locationsearch("Gondar");
        }
        if (item.getItemId() == R.id.mekele) {
            locationsearch("Mekele");
        }
        return super.onOptionsItemSelected(item);
    }
    public void populate(List<Messages> list){
        for(int i=0;i<list.size();i++){
            System.out.println("home data"+list.get(i).getKey());
        }
        mAdapter = new ImageAdapter(requireContext(), list);

        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setHasFixedSize(true);
       mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter.notifyDataSetChanged();

    }
    public void search(String value) {

        searchrecycler.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        searchUploads.clear();
        // pay.setVisibility(View.VISIBLE);
        //  ss.setVisibility(View.GONE);
        // ll.setVisibility(View.GONE);
        List<Messages> messages = mhomeShopMaterialViewModel.filterbycatagory(value);
        if (messages != null) {
            for (int i = 0; i < messages.size(); i++) {
                SearchMessage searchMessage = new SearchMessage();
                searchMessage.setCat(messages.get(i).getCat());
                searchMessage.setCit(messages.get(i).getCit());
                searchMessage.setCond(messages.get(i).getCond());
                searchMessage.setDesc(messages.get(i).getDesc());
                searchMessage.setImageName(messages.get(i).getImageName());
                searchMessage.setImageURL(messages.get(i).getImageURL());
                searchMessage.setKey(messages.get(i).getKey());
                searchMessage.setPhone(messages.get(i).getPhone());
                searchMessage.setPrice(messages.get(i).getPrice());
                searchMessage.setType(messages.get(i).getType());

                searchUploads.add(searchMessage);
            }
            searchAdapter.notifyDataSetChanged();
        } else {
            showMotionToast((Activity) requireContext(),
                    value,
                    "There is no data in our database",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM);

        }
    }
    public void locationsearch(String value) {

        searchrecycler.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        searchUploads.clear();
        List<Messages> messages = mhomeShopMaterialViewModel.filterbylocation(value);
        if (messages != null) {
            for (int i = 0; i < messages.size(); i++) {
                SearchMessage searchMessage = new SearchMessage();
                searchMessage.setCat(messages.get(i).getCat());
                searchMessage.setCit(messages.get(i).getCit());
                searchMessage.setCond(messages.get(i).getCond());
                searchMessage.setDesc(messages.get(i).getDesc());
                searchMessage.setImageName(messages.get(i).getImageName());
                searchMessage.setImageURL(messages.get(i).getImageURL());
                searchMessage.setKey(messages.get(i).getKey());
                searchMessage.setPhone(messages.get(i).getPhone());
                searchMessage.setPrice(messages.get(i).getPrice());
                searchMessage.setType(messages.get(i).getType());

                searchUploads.add(searchMessage);
            }
            searchAdapter.notifyDataSetChanged();
        } else {
            showMotionToast((Activity) requireContext(),
                    value,
                    "There is no data in our database",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM);

        }
    }
    public static void showMotionToast(Activity activity, String title, String message, String type, int position) {
        MotionToast.Companion.createColorToast(activity, title, message, type, position, MotionToast.LONG_DURATION, ResourcesCompat.getFont(activity.getApplicationContext(), R.font.helvetica_regular));
    }
}
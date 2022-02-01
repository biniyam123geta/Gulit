package com.biniyamshopping.shop;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.Fragments.CartViewModel;
import com.biniyamshopping.shop.offline.ObjectBox;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayCartRetrive extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private List<Messages> mUploads;
    private RecyclerView mRecyclerView;
    private DisplayCartAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private CartViewModel cartviewmodel;

    public DisplayCartRetrive() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display__retrive);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        ImageButton imageButton = findViewById(R.id.button_back);
        imageButton.setOnClickListener(v -> onBackPressed());
        mRecyclerView = findViewById(R.id.recyclerdisplay);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        String value = getIntent().getStringExtra("value");
        CartMessage cart = new CartMessage();
        Messages mcart = new Messages();
        cartviewmodel = new ViewModelProvider(this).get(CartViewModel.class);
        cartviewmodel.init();
        cart = cartviewmodel.getfiltereddata(value);
        mcart.setImageName(cart.getImageName());
        mcart.setImageURL(cart.getImageURL());
        mcart.setCat(cart.getCat());
        mcart.setCit(cart.getCit());
        mcart.setCond(cart.getCond());
        mcart.setDesc(cart.getDesc());
        mcart.setPrice(cart.getPrice());
        mcart.setPhone(cart.getPhone());
        mcart.set_id(cart.get_id());
        mcart.setType(cart.getType());
        mcart.setKey(cart.getKey());
        mUploads.add(mcart);
        mAdapter = new DisplayCartAdapter(DisplayCartRetrive.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    public boolean makePhoneCall() {
        String number = "0929039713";
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

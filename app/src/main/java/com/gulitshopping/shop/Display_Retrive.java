package com.gulitshopping.shop;

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

import com.gulitshopping.shop.Fragments.HomeShopMaterialViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Display_Retrive extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private List<Messages> mUploads;
    private RecyclerView mRecyclerView;
    private DisplayAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private HomeShopMaterialViewModel homeviewmodel;

    public Display_Retrive() {
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
        Messages cart = new Messages();
        homeviewmodel = new ViewModelProvider(this).get(HomeShopMaterialViewModel.class);
        homeviewmodel.init();
        cart = homeviewmodel.filterdata(value);
        mUploads.add(cart);
        if(mUploads!=null) {
            mAdapter = new DisplayAdapter(Display_Retrive.this, mUploads);
            mRecyclerView.setAdapter(mAdapter);
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    public boolean makePhoneCall() {
        String number = "0929039713";
//       Toast.makeText(Display_Retrive.this,number,Toast.LENGTH_SHORT).show();
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

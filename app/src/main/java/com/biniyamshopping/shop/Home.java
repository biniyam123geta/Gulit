package com.biniyamshopping.shop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.biniyamshopping.shop.Adapters.HomeTabAdapter;
import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.Dialogs.LocationDialoge;
import com.biniyamshopping.shop.Fragments.CartViewModel;
import com.biniyamshopping.shop.offline.ObjectBox;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class Home extends AppCompatActivity {
    LinearLayout ss;
    RelativeLayout pay;
    LinearLayout ll;
    CardView car, house, cloth, mobile, elec, shoes, food;
    LinearLayout v;
    EditText email;
    TabLayout tt;
    Box<Messages> messagesBox;
    FirebaseAuth mauth;
    int ii = 0;
    private Button sa;
    private AdView mAdView;
    //
    private RecyclerView searchRecyclerView;
    private SearchAdapter searchAdapter;
    //
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    //
    private ProgressBar p;
    private List<Messages> mUploads;
    private List<CartMessage> cartUploads;
    private List<SearchMessage> searchUploads;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference cartDatabaseRef;
    private LocationDialoge locationDialoge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //ObjectBox.init(this);

        house = findViewById(R.id.shoes);
        cloth = findViewById(R.id.cloth);
        mobile = findViewById(R.id.mobile);
        elec = findViewById(R.id.electronics);
        shoes = findViewById(R.id.shoes);
        food = findViewById(R.id.food);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ጉሊት");
        getSupportActionBar().setSubtitle("gulit");

        FloatingActionButton mAddCollection = findViewById(R.id.fab_add_collection);
        mAddCollection.setOnClickListener(v -> showCreateDialog());
        TabLayout mTabLayout = findViewById(R.id.tabs);
        ViewPager2 mViewpager = findViewById(R.id.viewPager);

        mViewpager.setAdapter(new HomeTabAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");

                    break;
                case 1:
                    tab.setText("Cart");
                    break;

            }
        });
        tabLayoutMediator.attach();

        ii = Integer.parseInt(getIntent().getStringExtra("val"));
        mauth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        //Home Display
        mRecyclerView = findViewById(R.id.recyclerid);
        //listView.setFastScrollEnabled(true);
        searchRecyclerView = findViewById(R.id.result_list);
        cartRecyclerView = findViewById(R.id.cartlist);
        p = findViewById(R.id.progress);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("dev");


        //
        ll = (LinearLayout) findViewById(R.id.ll);
        ss = (LinearLayout) findViewById(R.id.ss);
        searchUploads = new ArrayList<>();
        searchAdapter = new SearchAdapter(Home.this, searchUploads);
//        searchRecyclerView.setAdapter(searchAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


    }

    public void showMessage(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(authListener);
    }


    private void showCreateDialog() {
        if (locationDialoge == null) {
            locationDialoge = LocationDialoge.newInstance();
        }
        locationDialoge.show(getSupportFragmentManager(), "ddd");
    }

}
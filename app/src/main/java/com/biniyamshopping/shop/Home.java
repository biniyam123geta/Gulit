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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    LinearLayout ss;
    RelativeLayout pay;
    LinearLayout ll;
    CardView car,house,cloth,mobile,elec,shoes,food;
    private Button sa;
    LinearLayout v;
    EditText email;
    TabLayout tt;
    SqlDatabase db; private AdView mAdView;
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
    FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference cartDatabaseRef;
int ii=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ጉሊት");
        getSupportActionBar().setSubtitle("gulit");
        car=findViewById(R.id.car);
        house=findViewById(R.id.shoes);
        cloth=findViewById(R.id.cloth);
        mobile=findViewById(R.id.mobile);
        elec=findViewById(R.id.electronics);
        shoes=findViewById(R.id.shoes);
        food=findViewById(R.id.food);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carsearch();
            }
        });
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                housesearch();
            }
        });
        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clothsearch();
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobilesearch();
            }
        });
        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elecsearch();
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoessearch();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodsearch();
            }
        });
        ii= Integer.parseInt(getIntent().getStringExtra("val"));
        mauth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              //  if (firebaseAuth.getCurrentUser() == null) {
                    //String cc=firebaseAuth.getCurrentUser().getEmail();
                    // Toast.makeText(Home.this, "Please Login First", Toast.LENGTH_LONG).show();

                   // Intent i = new Intent(Home.this, MainActivity.class);
                   // startActivity(i);
               // }
            }
        };
        //Home Display
        db = new SqlDatabase(this);
        mRecyclerView = findViewById(R.id.recyclerid);
        //listView.setFastScrollEnabled(true);
        searchRecyclerView = findViewById(R.id.result_list);
        cartRecyclerView = findViewById(R.id.cartlist);
         mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        p = findViewById(R.id.progress);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Messages upload = postSnapshot.getValue(Messages.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);

                }
                mAdapter = new ImageAdapter(Home.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);

                mAdapter.notifyDataSetChanged();
               // mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
                p.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                p.setVisibility(View.INVISIBLE);
            }
        });
        //
        ll = (LinearLayout) findViewById(R.id.ll);
        ss = (LinearLayout) findViewById(R.id.ss);
        pay = (RelativeLayout) findViewById(R.id.searchrecycler);
        // sa=(Button)findViewById(R.id.signup);
        ll.setVisibility(View.GONE);
        ss.setVisibility(View.VISIBLE);
        tt = (TabLayout) findViewById(R.id.tab);
        v = (LinearLayout) findViewById(R.id.v);
        //email=findViewById(R.id.email);
        //tt.addTab(tt.newTab().setText("Cart"));
   tt.addTab(tt.newTab().setIcon(R.drawable.cart));
        tt.addTab(tt.newTab().setIcon(R.drawable.home));
        tt.addTab(tt.newTab().setIcon(R.drawable.sell));
       // tt.addTab(tt.newTab().setText("Home"));
       // tt.addTab(tt.newTab().setText("sell"));
        tt.setTabGravity(TabLayout.GRAVITY_FILL);

        tt.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ll.setVisibility(View.VISIBLE);
                        ss.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        //SQL DATABASE CHECK
                        Cursor res = db.getData();
                        if (res.getCount() == 0) {
                            showMessage("Your cart is empty");
                            ll.setVisibility(View.GONE);
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();

                        while (res.moveToNext()) {

                            buffer.append(res.getString(4));
                            displaycart(res.getString(4));
                        }

                        //SQL DATABASE END
                        return;
                    case 1:
                        ss.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.GONE);

                        return;
                    case 2:
                        // ll.setVisibility(View.GONE);
                        // ss.setVisibility(View.VISIBLE);
                        if(ii==0){
                        Intent intent = new Intent(Home.this, Where.class);
                        startActivity(intent);}
                        else
                        {
                            Intent intent = new Intent(Home.this, MainActivity.class);
                            startActivity(intent);
                        }
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mProgressCircle = findViewById(R.id.progress_circle);
        searchUploads = new ArrayList<>();
        searchAdapter = new SearchAdapter(Home.this, searchUploads);
        searchRecyclerView.setAdapter(searchAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
                pay.setVisibility(View.VISIBLE);
                ss.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                Query c = mDatabaseRef.child("uploads").orderByChild("imageName").startAt(newText).endAt(newText + "\uf8ff");
                c.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        searchUploads.clear();
                        if (dataSnapshot.exists()) {
                            // String cat=dataSnapshot.child("cat").getValue().t;
                            //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                                upload.setKey(postSnapshot.getKey());
                                searchUploads.add(upload);
                            }
                            searchAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pay.setVisibility(View.GONE);
                        ss.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.GONE);
                    }
                });
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.About) {
            Intent i = new Intent(Home.this, abut.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.contact) {
           // Toast.makeText(this, "welcome to contact", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Home.this, About.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "Successful Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(Home.this, MainActivity.class);
            startActivity(i);

        }
        if (item.getItemId() == R.id.car) {
            carsearch();
        }
        if (item.getItemId() == R.id.house) {
            housesearch();
        }
        if (item.getItemId() == R.id.other) {
            othersearch();
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

    public void displaycart(String value) {
        //CART

        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mProgressCircle = findViewById(R.id.progress_circle);
        cartUploads = new ArrayList<>();
        cartAdapter = new CartAdapter(Home.this, cartUploads);
        cartRecyclerView.setAdapter(cartAdapter);
        cartDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //
        Query c = cartDatabaseRef.child("uploads").orderByKey().equalTo(value);
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //cartUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CartMessage upload = postSnapshot.getValue(CartMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        cartUploads.add(upload);
                    }
                    cartAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no value in these data set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ////
    public void carsearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("Cars").endAt("Cars" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void clothsearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("imageName").startAt("cloth").endAt("cloth" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void elecsearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("Electronics").endAt("Electronics" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void foodsearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("food").endAt("food" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void mobilesearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("Mobile").endAt("Mobile" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void housesearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("Houses").endAt("Houses" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void shoessearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cat").startAt("Shoes and Clothing").endAt("Shoes and Clothing" + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void othersearch() {
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("imageName");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
    public void locationsearch(String value){
        pay.setVisibility(View.VISIBLE);
        ss.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        Query c = mDatabaseRef.child("uploads").orderByChild("cit").startAt(value).endAt(value + "\uf8ff");
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchUploads.clear();
                if (dataSnapshot.exists()) {
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SearchMessage upload = postSnapshot.getValue(SearchMessage.class);
                        upload.setKey(postSnapshot.getKey());
                        searchUploads.add(upload);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    showMessage("sorry, There is no data in our database");
                    ss.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pay.setVisibility(View.GONE);
                ss.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
    }
}
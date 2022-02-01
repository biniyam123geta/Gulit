package com.biniyamshopping.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private List<SearchMessage> mUploads;

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private ImageButton mSearchBtn;
    private EditText searchfield;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.result_list);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new SearchAdapter(Search.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        searchfield = findViewById(R.id.search_field);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchfield.getText().toString();

                firebaseUserSearch(searchText);

            }
        });
    }

    //
    public void firebaseUserSearch(String searchText) {

    }

}
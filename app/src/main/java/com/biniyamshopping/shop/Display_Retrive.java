package com.biniyamshopping.shop;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Display_Retrive extends AppCompatActivity {
    private List<Messages> mUploads;
    private static final int REQUEST_CALL = 1;
    private RecyclerView mRecyclerView;
    private DisplayAdapter mAdapter;
public Display_Retrive(){
}
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display__retrive);
        mRecyclerView = findViewById(R.id.recyclerdisplay);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new DisplayAdapter(Display_Retrive.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //ref = FirebaseDatabase.getInstance().getReference();
        String value= getIntent().getStringExtra("value");
       // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
        Query c=mDatabaseRef.child("uploads").orderByKey().equalTo(value);
        c.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                if(dataSnapshot.exists()){
                    // String cat=dataSnapshot.child("cat").getValue().t;
                    //  Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Messages upload = postSnapshot.getValue(Messages.class);
                       upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean makePhoneCall() {
        String number = "0929039713";
//       Toast.makeText(Display_Retrive.this,number,Toast.LENGTH_SHORT).show();
        return  true;
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

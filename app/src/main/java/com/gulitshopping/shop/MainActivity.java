package com.gulitshopping.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gulitshopping.shop.DbService.HomeShopDbService.HomeDbService;
import com.gulitshopping.shop.Utils.PermissionUtils;
import com.gulitshopping.shop.offline.ObjectBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText entervarcode, phone;
    FirebaseAuth mAuth;
    String codeSent;
    Button guest;
    CountryCodePicker ccp;
    String codesent;
    private DatabaseReference mDatabaseRef;
    private LinearLayout p;
    private final int REQUEST_PERMISSIONS = 1000;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            p.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, "varification fail please Retry", Toast.LENGTH_LONG).show();
            p.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);
        ObjectBox.init(this);
        requestPermissions();
        mAuth = FirebaseAuth.getInstance();
        p=findViewById(R.id.progress_bar);
        entervarcode = findViewById(R.id.entercode);
       // guest = findViewById(R.id.guest);
        ccp = (CountryCodePicker) findViewById(R.id.cpp);
        phone = findViewById(R.id.phone);
        ccp.registerCarrierNumberEditText(phone);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent i = new Intent(MainActivity.this, Home.class);
                    i.putExtra("val", "0");
                    startActivity(i);
                }
            }
        };
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("dev");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Messages> messages=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Messages upload = postSnapshot.getValue(Messages.class);
                    upload.setKey(postSnapshot.getKey());
                    messages.add(upload);

                }
              List<Messages> revmessages=  reverseList(messages);
                HomeDbService.getInstance(getApplicationContext(),ObjectBox.get()).storeAll(revmessages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.getvarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setVisibility(View.VISIBLE);
                sendVerificationCode();
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verifySignInCode();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "First get varification code", Toast.LENGTH_LONG).show();
                }
            }
        });
//        guest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(MainActivity.this, Home.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                i.putExtra("val", "1");
//                startActivity(i);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authListener);
    }
    public static<Message> List<Message> reverseList(List<Message> list)
    {
        List<Message> reverse = new ArrayList<>(list.size());

        ListIterator<Message> itr = list.listIterator(list.size());
        while (itr.hasPrevious()) {
            reverse.add(itr.previous());
        }

        return reverse;
    }
    /**
     * Request Permissions
     */
    private void requestPermissions() {
        List<String> permissionsToBeGranted = PermissionUtils.checkPermissions(getApplicationContext(), PermissionUtils.getAllRequiredPermissions());
        if (!permissionsToBeGranted.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToBeGranted.toArray(new String[permissionsToBeGranted.size()]), REQUEST_PERMISSIONS);
        }
    }

    private void verifySignInCode() {
        String code = entervarcode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            p.setVisibility(View.GONE);

                            Intent i = new Intent(MainActivity.this, Home.class);
                            i.putExtra("val", "0");
                            startActivity(i);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {
        p.setVisibility(View.VISIBLE);
        // String phone = enterphone.getText().toString();
        final String sphone = ccp.getFullNumberWithPlus();

        String validphone = phone.getText().toString().trim();
        if (validphone.isEmpty()) {
            phone.setError("phone  is required");
            phone.requestFocus();
            p.setVisibility(View.GONE);
            return;
        }
        if (validphone.length() < 9) {
            phone.setError("Please enter a valid phone");
            phone.requestFocus();
            p.setVisibility(View.GONE);
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                sphone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }



}

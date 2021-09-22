package com.biniyamshopping.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText entervarcode,phone;
    FirebaseAuth mAuth;
    private ProgressBar p;
    String codeSent;
    Button guest;
    CountryCodePicker ccp;
    private FirebaseAuth.AuthStateListener authListener;
    String codesent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        p = findViewById(R.id.progress);
        p.setVisibility(View.INVISIBLE);
        entervarcode = findViewById(R.id.entercode);
        guest=findViewById(R.id.guest);
        ccp = (CountryCodePicker) findViewById(R.id.cpp);
        phone=findViewById(R.id.phone);
        ccp.registerCarrierNumberEditText(phone);
        authListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    //String cc=firebaseAuth.getCurrentUser().getEmail();
                    //  Toast.makeText(MainActivity.this, cc, Toast.LENGTH_LONG).show();


                    Intent i=new Intent(MainActivity.this,Home.class);
                    i.putExtra("val","0");
                    startActivity(i);
                }
            }
        };
        findViewById(R.id.getvarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode();
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                verifySignInCode();}
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "First get varification code", Toast.LENGTH_LONG).show();
                }
            }
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i=new Intent(MainActivity.this,Home.class);
               i.putExtra("val","1");
               startActivity(i);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authListener);
    }
    private void verifySignInCode(){
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
                            p.setVisibility(View.VISIBLE);

                            Intent i=new Intent(MainActivity.this,Home.class);
                            i.putExtra("val","0");
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

    private void sendVerificationCode(){
        p.setVisibility(View.VISIBLE);
       // String phone = enterphone.getText().toString();
        final String sphone   = ccp.getFullNumberWithPlus();

        String validphone=phone.getText().toString().trim();
        if(validphone.isEmpty()){
            phone.setError("phone  is required");
            phone.requestFocus();
            return;
        }
        if(validphone.length() < 9 ){
            phone.setError("Please enter a valid phone");
            phone.requestFocus();
            p.setVisibility(View.INVISIBLE);
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                sphone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
       // OnVerificationStateChangedCallbacks

    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            p.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, "varification fail please Retry", Toast.LENGTH_LONG).show();
            p.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

}

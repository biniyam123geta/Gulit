package com.gulitshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gulitshopping.shop.Home;
import com.gulitshopping.shop.MainActivity;
import com.gulitshopping.shop.R;

public class EmailLoginActivity extends AppCompatActivity {
private Button submit;
private EditText email, password;
  private  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_email_login);
        submit=findViewById(R.id.submit);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(v->{
            if(email.getText().toString()!=null && password.getText().toString()!=null){
            OnSubmit(email.getText().toString(),password.getText().toString());}
            else{
                Toast.makeText(getApplicationContext(),
                        "Invalid email or password", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    private void OnSubmit(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),
                                    "signInWithEmail:success ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EmailLoginActivity.this, Home.class);
                            i.putExtra("val", "0");
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
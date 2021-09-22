package com.biniyamshopping.shop;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Sell extends AppCompatActivity {
    FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener authListener;
    private static final int PICK_IMAGE_REQUEST = 1;
EditText type,phone,desc,price;
    RadioButton New, used;
Button submit;
FirebaseDatabase database;
DatabaseReference ref;
    String str=""; String cit=""; String cat="";private static final String TAG = "Your Tag";
    EditText txtdata ;
    ImageView imgview;
    int value=0;
int Image_Request_Code=7;
ProgressDialog pd;
    ProgressBar p;
    Uri FilePathUri;
    private ProgressBar mProgressBar;
    private StorageTask mUploadTask;
    StorageReference storageReference;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.sell);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mauth=FirebaseAuth.getInstance();
        p=findViewById(R.id.progress);
        p.setVisibility(View.INVISIBLE);
        //
        authListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    currentUser=firebaseAuth.getCurrentUser().getPhoneNumber();
                    phone.setText(currentUser);
                }
            }
        };//
        New = (RadioButton)findViewById(R.id.New);
        used = (RadioButton)findViewById(R.id.used);
        type=findViewById(R.id.type);
        phone=findViewById(R.id.phone);
        desc=findViewById(R.id.desc);
        imgview=findViewById(R.id.imageview);
        txtdata=findViewById(R.id.txtdata);
         price=findViewById(R.id.price);

         cit=getIntent().getStringExtra("cit");
        cat=getIntent().getStringExtra("cat");
        submit=findViewById(R.id.submit);
        database=FirebaseDatabase.getInstance();
        mProgressBar = findViewById(R.id.progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        ref = FirebaseDatabase.getInstance().getReference("uploads");

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),Image_Request_Code);
            }
        });
    }
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(authListener);
        p.setVisibility(View.INVISIBLE);

    }
///////radio selected
public void radioaction(View view) {
    boolean checked = ((RadioButton) view).isChecked();

    // Check which radio button was clicked
    switch(view.getId()) {
        case R.id.New:
            if(checked)
                str = "New";
            break;
        case R.id.used:
            if(checked)
                str = "Used";
            break;}
    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
    public void submit(View view ){
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(Sell.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }

    }
    /////////for upload image

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String txt = txtdata.getText().toString();
        if(txt.isEmpty()){
            txtdata.setError("item name  is required");
            txtdata.requestFocus();

            return;
        }
        String pr = price.getText().toString();
        if(pr.isEmpty()){
            price.setError("price  is required");
            price.requestFocus();

            return;
        }
        p.setVisibility(View.VISIBLE);
        if (FilePathUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(FilePathUri));
            mUploadTask = fileReference.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(Sell.this, "Upload successful", Toast.LENGTH_LONG).show();////
                           /* Messages upload = new Messages(txtdata.getText().toString().trim(),
                                    // taskSnapshot.getDownloadUrl().toString());
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId =ref.push().getKey();
                            ref.child(uploadId).setValue(upload);*////

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                            Messages upload = new Messages(price.getText().toString().trim(),cat.toString().trim(),cit.toString().trim(),desc.getText().toString().trim(),txtdata.getText().toString().trim(),downloadUrl.toString(),str.toString().trim(),type.getText().toString().trim(),phone.getText().toString().trim());

                            String uploadId = ref.push().getKey();
                            ref.child(uploadId).setValue(upload);
                            p.setVisibility(View.INVISIBLE);
                            Intent i=new Intent(Sell.this,Home.class);
                            i.putExtra("val","0");
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Sell.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}

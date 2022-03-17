package com.gulitshopping.shop;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.exifinterface.media.ExifInterface;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sell extends AppCompatActivity {
    FirebaseAuth mauth;
    EditText type, phone, desc, price;
    RadioButton New, used;
    Button submit;
    FirebaseDatabase database;
    DatabaseReference ref;
    String str = "";
    String cit = "";
    String cat = "";
    EditText txtdata;
    ImageView imgview;
    int value = 0;
    int Image_Request_Code = 7;
    ProgressDialog pd;
    ProgressBar p;
    Uri FilePathUri;
    StorageReference storageReference;
    String currentUser;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressBar mProgressBar;
    private StorageTask mUploadTask;
    AnstronCoreHelper anstronCoreHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.sell);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mauth = FirebaseAuth.getInstance();
        p = findViewById(R.id.progress);
        p.setVisibility(View.INVISIBLE);
        //
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    currentUser = firebaseAuth.getCurrentUser().getPhoneNumber();
                    phone.setText(currentUser);
                }
            }
        };
        New = (RadioButton) findViewById(R.id.New);
        used = (RadioButton) findViewById(R.id.used);
        type = findViewById(R.id.type);
        phone = findViewById(R.id.phone);
        desc = findViewById(R.id.desc);
        imgview = findViewById(R.id.imageview);
        txtdata = findViewById(R.id.txtdata);
        price = findViewById(R.id.price);

        cit = getIntent().getStringExtra("cit");
        cat = getIntent().getStringExtra("cat");
        submit = findViewById(R.id.submit);
        database = FirebaseDatabase.getInstance();
        mProgressBar = findViewById(R.id.progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("dev");
        ref = FirebaseDatabase.getInstance().getReference("dev");
       anstronCoreHelper=new AnstronCoreHelper(this);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), Image_Request_Code);
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
        switch (view.getId()) {
            case R.id.New:
                if (checked)
                    str = "New";
                break;
            case R.id.used:
                if (checked)
                    str = "Used";
                break;
        }
        //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void submit(View view) {
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
            if(FilePathUri!=null){
                BackTask bt=new BackTask();
                bt.execute();
            }
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String txt = txtdata.getText().toString();
        if (txt.isEmpty()) {
            txtdata.setError("item name  is required");
            txtdata.requestFocus();
            return;
        }
        String pr = price.getText().toString();
        if (pr.isEmpty()) {
            price.setError("price  is required");
            price.requestFocus();
            return;
        }
        String stype = type.getText().toString();
        if (stype.isEmpty()) {
            type.setError("type  is required");
            type.requestFocus();

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
                            Toast.makeText(Sell.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                            Messages upload = new Messages(price.getText().toString().trim(), cat.trim(), cit.trim(), desc.getText().toString().trim(), txtdata.getText().toString().trim(), downloadUrl.toString(), str.trim(), type.getText().toString().trim(), phone.getText().toString().trim());

                            String uploadId = ref.push().getKey();
                            ref.child(uploadId).setValue(upload);
                            p.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(Sell.this, Home.class);
                            i.putExtra("val", "0");
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

    public void imagecompressor(Uri uri){
             FilePathUri  = compimage(uri.toString());

    }

   public Uri compimage(String uriString ){
       try {
           Uri imageUri = Uri.parse(uriString);
           Bitmap scaledBitmap = null;

           BitmapFactory.Options options = new BitmapFactory.Options();

           // by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
           // you try the use the bitmap here, you will get null.
           options.inJustDecodeBounds = true;
           Bitmap bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri), null, options);

           int actualHeight = options.outHeight;
           int actualWidth = options.outWidth;

           // max Height and width values of the compressed image is taken as 816x612
           float maxHeight = 816.0f;
           float maxWidth = 612.0f;
           float imgRatio = actualWidth / actualHeight;
           float maxRatio = maxWidth / maxHeight;

           // width and height values are set maintaining the aspect ratio of the image

           if (actualHeight > maxHeight || actualWidth > maxWidth) {
               if (imgRatio < maxRatio) {
                   imgRatio = maxHeight / actualHeight;
                   actualWidth = (int) (imgRatio * actualWidth);
                   actualHeight = (int) maxHeight;
               } else if (imgRatio > maxRatio) {
                   imgRatio = maxWidth / actualWidth;
                   actualHeight = (int) (imgRatio * actualHeight);
                   actualWidth = (int) maxWidth;
               } else {
                   actualHeight = (int) maxHeight;
                   actualWidth = (int) maxWidth;

               }
           }

           // setting inSampleSize value allows to load a scaled down version of the original image
           options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

           // inJustDecodeBounds set to false to load the actual bitmap
           options.inJustDecodeBounds = false;

           // this options allow android to claim the bitmap memory if it runs low on memory
           options.inPurgeable = true;
           options.inInputShareable = true;
           options.inTempStorage = new byte[16 * 1024];

           // load the bitmap from its path
           bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri), null, options);
           scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);


           float ratioX = actualWidth / (float) options.outWidth;
           float ratioY = actualHeight / (float) options.outHeight;
           float middleX = actualWidth / 2.0f;
           float middleY = actualHeight / 2.0f;

           Matrix scaleMatrix = new Matrix();
           scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

           Canvas canvas = new Canvas(scaledBitmap);
           canvas.setMatrix(scaleMatrix);
           canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

           // check the rotation of the image and display it properly
           androidx.exifinterface.media.ExifInterface exif;
           try {
               exif = new ExifInterface(this.getContentResolver().openInputStream(imageUri));

               int orientation = exif.getAttributeInt(
                       ExifInterface.TAG_ORIENTATION, 0);
               Log.d("EXIF", "Exif: " + orientation);
               Matrix matrix = new Matrix();
               if (orientation == 6) {
                   matrix.postRotate(90);
                   Log.d("EXIF", "Exif: " + orientation);
               } else if (orientation == 3) {
                   matrix.postRotate(180);
                   Log.d("EXIF", "Exif: " + orientation);
               } else if (orientation == 8) {
                   matrix.postRotate(270);
                   Log.d("EXIF", "Exif: " + orientation);
               }
               scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                       scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                       true);
           } catch (IOException e) {
               e.printStackTrace();
           }

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

               String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
               ContentValues values = new ContentValues();
               values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
               values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
               values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Gulit-ጉሊት/");
               values.put(MediaStore.Images.Media.IS_PENDING, 1);

               Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
               Uri resultUri = this.getContentResolver().insert(collection, values);

               OutputStream out = this.getContentResolver().openOutputStream(resultUri);

               // write the compressed bitmap at the destination specified by filename.
               scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
               values.clear();
               values.put(MediaStore.Images.Media.IS_PENDING, 0);
               this.getContentResolver().update(resultUri, values, null, null);

               return resultUri;

           }

       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
       return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    private class BackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            imagecompressor(FilePathUri);
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
        }
    }
}

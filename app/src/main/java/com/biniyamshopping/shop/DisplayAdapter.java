package com.biniyamshopping.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.offline.ObjectBox;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import www.sanju.motiontoast.MotionToast;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ImageViewHolder> {
    SqlDatabase db;
    boolean color;
    InterstitialAd ad;
    private DatabaseReference mDatabaseRef;
    private final Context mContext;
    private final List<Messages> mUploads;
     private CartMessage cartmessage;
    public DisplayAdapter(Context context, List<Messages> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.displaydetail, parent, false);
        //Toast.makeText(mContext, addres, Toast.LENGTH_SHORT).show();
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
         Messages uploadCurrent=new Messages();
         uploadCurrent = mUploads.get(position);
         System.out.println("image name");
         System.out.println(uploadCurrent.getImageName());
         System.out.println("end image name");
        holder.txtdata.setText(uploadCurrent.getImageName());
        //final String  selectedkey=uploadCurrent.getKey();
        final String phone = uploadCurrent.getPhone();
        String condition = uploadCurrent.getCond();
        // holder.cond.setText(condition);
        holder.desc.setText(uploadCurrent.getDesc());
        holder.location.setText(uploadCurrent.getCit());
        holder.price.setText(uploadCurrent.getPrice() + " Birr");
        Messages selecteditem = mUploads.get(position);
        final String selectedkey = selecteditem.getKey();
        MobileAds.initialize(mContext, "ca-app-pub-6002206915132015~3669917621");
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.drawable.loadimage)
                .fit()
                .centerCrop()
                //.centerCrop()
                .into(holder.im);
        cartmessage=new CartMessage();
        cartmessage.setImageName(uploadCurrent.getImageName());
        cartmessage.setPhone(uploadCurrent.getPhone());
        cartmessage.setCond(uploadCurrent.getCond());
        cartmessage.setDesc(uploadCurrent.getDesc());
        cartmessage.setPrice(uploadCurrent.getPrice());
        cartmessage.setCit(uploadCurrent.getCit());
        cartmessage.setImageURL(uploadCurrent.getImageURL());
        cartmessage.setType(uploadCurrent.getType());
        cartmessage.setKey(uploadCurrent.getKey());
       // cartmessage.set_id(uploadCurrent.get_id());
       boolean exist= CartDbService.getInstance(mContext, ObjectBox.get()).existsInCache(uploadCurrent.getKey());
       if(exist){
           System.out.println("exist in catch");
           holder.mLabeledSwitch.setChecked(true);
       }
        holder.mLabeledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   CartDbService.getInstance(mContext, ObjectBox.get()).removeBookingByID(cartmessage.getKey());
                    boolean r = CartDbService.getInstance(mContext, ObjectBox.get()).storeCart(cartmessage);
                    if (r == true) {
                      showMotionToast((Activity) mContext,
                                "Cart",
                                "succesfully added to cart",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM);

                    } else {
                        showMotionToast((Activity) mContext,
                                "Cart",
                                "Not inserted to Cart",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM);
                    }
                } else {
                   boolean deletecart= CartDbService.getInstance(mContext,ObjectBox.get()).removecart(cartmessage.getKey());
                   if(deletecart){
                    showMotionToast((Activity) mContext,
                            "Cart",
                            "succesfully Delete item from cart",
                            MotionToast.TOAST_DELETE,
                            MotionToast.GRAVITY_BOTTOM);}
                   else{
                       showMotionToast((Activity) mContext,
                               "Cart",
                               "something went wrong",
                               MotionToast.TOAST_ERROR,
                               MotionToast.GRAVITY_BOTTOM);
                   }
                }
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               call(phone);

            }
        });

    }
    public static void showMotionToast(Activity activity, String title, String message, String type, int position) {
        MotionToast.Companion.createColorToast(activity, title, message, type, position, MotionToast.LONG_DURATION, ResourcesCompat.getFont(activity.getApplicationContext(), R.font.helvetica_regular));
    }

    /**
     * Call Phone Number
     */
    private void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(callIntent);

    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView txtdata, cond;
        public TextView location, cat, desc, price;
        public ImageView im;
        public Button call;
        private AdView mAdView;
        private final Switch mLabeledSwitch;

        // public   ImageView star;   //private AdView mAdView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            mLabeledSwitch = (itemView).findViewById(R.id.status_switch);
            //mAdView=(itemView).findViewById(R.id.adView);
            //itemView.setOnClickListener(ImageAdapter.this);
            // mAdView=(itemView).findViewById(R.id.adView);

            location = itemView.findViewById(R.id.location);
            txtdata = itemView.findViewById(R.id.imname);
            //cond=itemView.findViewById(R.id.cond);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            im = itemView.findViewById(R.id.im);
            call = itemView.findViewById(R.id.call);
        }
    }


}

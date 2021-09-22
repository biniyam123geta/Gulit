package com.biniyamshopping.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder>{
    private DatabaseReference mDatabaseRef;
    private Context mContext;
    private List<CartMessage> mUploads;

    public CartAdapter(Context context, List<CartMessage> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public CartAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.test, parent, false);
        //Toast.makeText(mContext, addres, Toast.LENGTH_SHORT).show();
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ImageViewHolder holder, int position) {
        // mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        CartMessage uploadCurrent = mUploads.get(position);
        holder.name.setText(uploadCurrent.getImageName());
        holder.zoomout.setVisibility(View.INVISIBLE);

        holder.loc.setText(uploadCurrent.getCit());
        final String  phone=uploadCurrent.getPhone();

        holder.cat.setText("Type: "+uploadCurrent.getCat());
        holder.price.setText(uploadCurrent.getPrice()+" Birr");
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.mipmap.load_g)
                .fit()
                .centerInside()
                //.centerCrop()
                .into(holder.im);
        //MobileAds.initialize(mContext,"ca-app-pub-6002206915132015~3669917621");

       // AdRequest adRequest = new AdRequest.Builder().build();
       // holder.mAdView.loadAd(adRequest);
        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CartMessage selecteditem=mUploads.get(position);
                final String  selectedkey=selecteditem.getKey();
                Intent i=new Intent(mContext,Display_Retrive.class);
                i.putExtra("value",selectedkey);
                i.putExtra("position",position);
                mContext.startActivity(i);
            }
        });
        holder.zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.zoomin);
                holder.price.setVisibility(View.INVISIBLE);
                holder.name.setVisibility(View.INVISIBLE);
                holder.cat.setVisibility(View.INVISIBLE);
                holder.im.startAnimation(animation);
                holder.zoomout.setVisibility(View.VISIBLE);
            }
        });
        holder.zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.zoomout);
                holder.im.startAnimation(animation);
                holder.zoomout.setVisibility(View.INVISIBLE);
                holder.price.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                holder.cat.setVisibility(View.VISIBLE);
                holder.zoom.setVisibility(View.VISIBLE);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             holder.call.setVisibility(View.INVISIBLE);
             holder.phone.setVisibility(View.VISIBLE);
                holder.phone.setText(phone);
            }
        });
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.call.setVisibility(View.INVISIBLE);
                holder.phone.setVisibility(View.VISIBLE);
                Intent i=new Intent(mContext,Call.class);
                i.putExtra("value",phone);
                holder.phone.setText(phone);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView name,price,loc,cat,type;
        public ImageView im;
        Button zoom,call,phone,zoomout;
        private AdView mAdView;
        public ImageViewHolder(View itemView) {
            super(itemView);
           // mAdView=(itemView).findViewById(R.id.adView);
            zoom=(itemView).findViewById(R.id.zoom);
            call=(itemView).findViewById(R.id.call);
            phone=(itemView).findViewById(R.id.phone);
            zoomout=(itemView).findViewById(R.id.zoomout);
            im=itemView.findViewById(R.id.im);
            loc=itemView.findViewById(R.id.location);
            cat=itemView.findViewById(R.id.catagory);
            name=itemView.findViewById(R.id.imagename);
            price=itemView.findViewById(R.id.price);
        }
    }
}



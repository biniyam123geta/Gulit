package com.biniyamshopping.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ImageViewHolder> {
SqlDatabase db;
boolean color; InterstitialAd ad;
    private DatabaseReference mDatabaseRef;
    private Context mContext;
    private List<Messages> mUploads;
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

        Messages uploadCurrent = mUploads.get(position);
        holder.txtdata.setText(uploadCurrent.getImageName());
        //final String  selectedkey=uploadCurrent.getKey();
        final String  phone=uploadCurrent.getPhone();
         String  condition=uploadCurrent.getCond();
        // holder.cond.setText(condition);
        holder.desc.setText(uploadCurrent.getDesc());
//        holder.location.setText(uploadCurrent.getCit());
        holder.price.setText(uploadCurrent.getPrice()+" Birr");
        Messages selecteditem=mUploads.get(position);
        final String  selectedkey=selecteditem.getKey();
        MobileAds.initialize(mContext,"ca-app-pub-6002206915132015~3669917621");
        AdRequest adRequest = new AdRequest.Builder().build();
        holder.mAdView.loadAd(adRequest);

      //  MobileAds.initialize(mContext,"ca-app-pub-6002206915132015~3669917621");
      //  AdRequest adRequest = new AdRequest.Builder().build();
       // holder.mAdView.loadAd(adRequest);


       // ad=new InterstitialAd(mContext);
       // ad.setAdUnitId("ca-app-pub-6002206915132015/8066234301");
       // ad.loadAd(new AdRequest.Builder().build());
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.drawable.loadimage)
                .fit()
                .centerCrop()
                //.centerCrop()
                .into(holder.im);

            holder.star.setColorFilter(mContext.getResources().getColor(R.color.green));

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new SqlDatabase(mContext);
   boolean ch=db.checkIfRecordExist(selectedkey);
                color=ch;
                if(ch==false) {

                    boolean r = db.insertdata(holder.txtdata.getText().toString(), holder.price.getText().toString(), holder.desc.getText().toString(), selectedkey);
                    if (r == true) {
                        holder.star.setColorFilter(mContext.getResources().getColor(R.color.yellow));
                        Toast.makeText(mContext, "succesfully added to cart", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(mContext, "not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //Toast.makeText(mContext, "already inserted", Toast.LENGTH_SHORT).show();
                    db.deletData(selectedkey);
                    holder.star.setColorFilter(mContext.getResources().getColor(R.color.red));
                    Toast.makeText(mContext, "data deleted from cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // ad.show();
                Intent i=new Intent(mContext,Call.class);
                i.putExtra("value",phone);
                mContext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView txtdata,cond;
        public TextView location,cat,desc,price;
      public   ImageView im;
      public Button call;
        private AdView mAdView;
      public   ImageView star;   //private AdView mAdView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            //mAdView=(itemView).findViewById(R.id.adView);
            //itemView.setOnClickListener(ImageAdapter.this);
            mAdView=(itemView).findViewById(R.id.adView);
          star=itemView.findViewById(R.id.star);
            location=itemView.findViewById(R.id.location);
            txtdata=itemView.findViewById(R.id.imname);
           // cond=itemView.findViewById(R.id.cond);
            desc=itemView.findViewById(R.id.desc);
            price=itemView.findViewById(R.id.price);
            im=itemView.findViewById(R.id.im);
            call=itemView.findViewById(R.id.call);
        }
    }


}

package com.biniyamshopping.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    String addres = null;
    String bini = "";
    private DatabaseReference mDatabaseRef;
    private final Context mContext;
    private final List<Messages> mUploads;

    public ImageAdapter(Context context, List<Messages> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        Messages uploadCurrent = mUploads.get(position);
        String c = uploadCurrent.getCit();
        holder.textViewName.setText(uploadCurrent.getImageName());
        bini = uploadCurrent.getImageName();
        holder.price.setText("Price:" + uploadCurrent.getPrice());
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.drawable.loadimage)
                .fit()
                .centerInside()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String selectedkey = uploadCurrent.getKey();
                Intent i = new Intent(mContext, Display_Retrive.class);
                i.putExtra("value", selectedkey);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewName1, imageView1;
        public TextView price, price1;
        public LinearLayout lin;
        public ImageView imageView;
        public CardView card;

        public ImageViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.price);
            textViewName = itemView.findViewById(R.id.imgtitle);
            imageView = itemView.findViewById(R.id.imgfirbase);
        }
    }

}
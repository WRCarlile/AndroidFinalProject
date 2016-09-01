package com.epicodus.bigfun.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bigfun.Constants;
import com.epicodus.bigfun.EventDetailActivity;
import com.epicodus.bigfun.R;
import com.epicodus.bigfun.models.UserEvents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;

public class FirebaseEventViewHolder extends RecyclerView.ViewHolder {
//    private static final int MAX_WIDTH = 1600;
//    private static final int MAX_HEIGHT = 2200;
    View mView;
    Context mContext;
    public ImageView mEventImageView;

    public FirebaseEventViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindEvent(UserEvents event) {
        if(!event.getImageUrl().contains("http")) {
            try {
                Bitmap imageBitmap = decodeFromFirebaseBase64(event.getImageUrl());
                mEventImageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mEventImageView = (ImageView) mView.findViewById(R.id.eventImageView);
            TextView nameTextView = (TextView) mView.findViewById(R.id.eventName);


            Picasso.with(mContext)
                    .load(event.getImageUrl())
//                .resize(MAX_WIDTH, MAX_HEIGHT)
//                .centerCrop()
                    .into(mEventImageView);

            nameTextView.setText(event.getName());
        }
    }
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}

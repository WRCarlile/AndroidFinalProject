package com.epicodus.bigfun.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import butterknife.Bind;

public class FirebaseEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 1600;
    private static final int MAX_HEIGHT = 2200;

    View mView;
    Context mContext;

    public FirebaseEventViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindEvent(UserEvents event) {
        ImageView eventImageView = (ImageView) mView.findViewById(R.id.eventImageView);
        TextView nameTextView = (TextView) mView.findViewById(R.id.eventName);
        TextView descriptionTextView = (TextView) mView.findViewById(R.id.eventDescription);


        Picasso.with(mContext)
                .load(event.getImageUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(eventImageView);

        nameTextView.setText(event.getName());
        descriptionTextView.setText(event.getDescription());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<UserEvents> events = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_SAVED_EVENT);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    events.add(snapshot.getValue(UserEvents.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("events", Parcels.wrap(events));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

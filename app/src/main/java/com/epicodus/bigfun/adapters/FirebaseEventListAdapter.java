package com.epicodus.bigfun.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.epicodus.bigfun.EventDetailActivity;
import com.epicodus.bigfun.models.UserEvents;
import com.epicodus.bigfun.utils.ItemTouchHelperAdapter;
import com.epicodus.bigfun.utils.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseEventListAdapter extends FirebaseRecyclerAdapter<UserEvents, FirebaseEventViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ArrayList<UserEvents> mEvents = new ArrayList<>();
    private ChildEventListener mChildEventListener;


    public FirebaseEventListAdapter(Class<UserEvents> modelClass, int modelLayout,
                                    Class<FirebaseEventViewHolder> viewHolderClass,
                                    Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mEvents.add(dataSnapshot.getValue(UserEvents.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void populateViewHolder(final FirebaseEventViewHolder viewHolder, UserEvents model, int position) {
        viewHolder.bindEvent(model);

        viewHolder.mEventImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra("position", viewHolder.getAdapterPosition());
                intent.putExtra("events", Parcels.wrap(mEvents));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mEvents, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mEvents.remove(position);
        getRef(position).removeValue();
    }
    private void setIndexInFirebase() {
        for (UserEvents event : mEvents) {
            int index = mEvents.indexOf(event);
            DatabaseReference ref = getRef(index);
            ref.child("index").setValue(Integer.toString(index));
        }
    }
    @Override
    public void cleanup() {
        super.cleanup();
        setIndexInFirebase();
        mRef.removeEventListener(mChildEventListener);
    }
}
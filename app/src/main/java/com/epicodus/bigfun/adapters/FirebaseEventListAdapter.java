package com.epicodus.bigfun.adapters;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.epicodus.bigfun.models.UserEvents;
import com.epicodus.bigfun.utils.ItemTouchHelperAdapter;
import com.epicodus.bigfun.utils.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class FirebaseEventListAdapter extends FirebaseRecyclerAdapter<UserEvents, FirebaseEventViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

    public FirebaseEventListAdapter(Class<UserEvents> modelClass, int modelLayout,
                                    Class<FirebaseEventViewHolder> viewHolderClass,
                                    Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
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
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }
}
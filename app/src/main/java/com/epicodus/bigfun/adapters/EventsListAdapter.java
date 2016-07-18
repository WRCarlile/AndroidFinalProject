package com.epicodus.bigfun.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bigfun.EventDetailActivity;
import com.epicodus.bigfun.R;
import com.epicodus.bigfun.models.UserEvents;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventViewHolder> implements View.OnClickListener{
    private static final int MAX_WIDTH = 1600;
    private static final int MAX_HEIGHT = 2200;

    private ArrayList<UserEvents> mEvents = new ArrayList<>();
    private Context mContext;

    public EventsListAdapter(Context context, ArrayList<UserEvents> events) {
        mContext = context;
        mEvents = events;
    }

    @Override
    public EventsListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventsListAdapter.EventViewHolder holder, int position) {
        holder.bindEvent(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.eventName)
        TextView mNameTextView;
        @Bind(R.id.eventDescription) TextView mDescriptionTextView;
        @Bind(R.id.eventImageView)
        ImageView mEventImageView;

        private Context mContext;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }


        public void bindEvent(UserEvents events) {
            mNameTextView.setText(events.getName());
            mDescriptionTextView.setText(events.getDescription());
            Picasso.with(mContext)
                    .load(events.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mEventImageView);
        }
        @Override
        public void onClick(View v) {
            Log.d("click listener", "working");
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, EventDetailActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("events", Parcels.wrap(mEvents));
            mContext.startActivity(intent);
        }
    }
}
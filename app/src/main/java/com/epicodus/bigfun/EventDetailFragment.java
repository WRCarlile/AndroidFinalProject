package com.epicodus.bigfun;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import com.epicodus.bigfun.models.UserEvents;

import butterknife.Bind;
import butterknife.ButterKnife;



public class EventDetailFragment extends Fragment {
    @Bind(R.id.eventImageView) ImageView mEventImageView;
    @Bind(R.id.eventDescription) TextView mEventDescription;
    @Bind(R.id.eventName) TextView mEventName;

    private UserEvents mEvent;


    public static EventDetailFragment newInstance(UserEvents event) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", Parcels.wrap(event));
        eventDetailFragment.setArguments(args);
        return eventDetailFragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = Parcels.unwrap(getArguments().getParcelable("event"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);
        Picasso.with(view.getContext()).load(mEvent.getImageUrl()).into(mEventImageView);
        mEventName.setText(mEvent.getName());
        mEventDescription.setText(mEvent.getDescription());

        return view;
    }
}
package com.epicodus.bigfun;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import com.epicodus.bigfun.models.UserEvents;

import butterknife.Bind;
import butterknife.ButterKnife;



public class EventDetailFragment extends Fragment implements View.OnClickListener{
    @Bind(R.id.eventImageView) ImageView mEventImageView;
    @Bind(R.id.eventDescription) TextView mEventDescription;
    @Bind(R.id.eventName) TextView mEventName;
    @Bind(R.id.saveEvent) Button mSaveEvent;

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

        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Log.d("current user", user + " ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(view.getContext()).load(mEvent.getImageUrl()).into(mEventImageView);
        mEventName.setText(mEvent.getName());
        mEventDescription.setText(mEvent.getDescription());
        mSaveEvent.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == mSaveEvent) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference eventRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_SAVED_EVENT)
                    .child(uid);

            DatabaseReference pushRef = eventRef.push();
            String pushId = pushRef.getKey();
            mEvent.setPushId(pushId);
            pushRef.setValue(mEvent);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }

    }
}
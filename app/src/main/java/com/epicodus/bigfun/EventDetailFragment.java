package com.epicodus.bigfun;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
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

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;



public class EventDetailFragment extends Fragment implements View.OnClickListener{
    @Bind(R.id.eventImageView) ImageView mEventImageView;
    @Bind(R.id.eventDescription) TextView mEventDescription;
    @Bind(R.id.eventName) TextView mEventName;
    @Bind(R.id.saveEvent) Button mSaveEvent;
    @Bind(R.id.bMap) Button mMap;

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

//        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//        Log.d("current user", user + " ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);

        if (!mEvent.getImageUrl().contains("http")) {
            try {
                Bitmap image = decodeFromFirebaseBase64(mEvent.getImageUrl());
                mEventImageView.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Picasso.with(view.getContext()).load(mEvent.getImageUrl()).into(mEventImageView);
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            mSaveEvent.setOnClickListener(this);
            mMap.setOnClickListener(this);
        }
        return view;
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
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
        if (view == mMap ) {
            String latitude = mEvent.getLatitude();
            String longitude = mEvent.getLongitude();
            String street = mEvent.getStreet();
            String geo = "geo:"+latitude+","+longitude+"?q="+street;
            Uri gmmIntentUri = Uri.parse(geo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        else {
            Toast.makeText(getContext(), "No Address", Toast.LENGTH_SHORT).show();
        }

    }
}
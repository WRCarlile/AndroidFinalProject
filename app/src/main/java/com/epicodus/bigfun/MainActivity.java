
package com.epicodus.bigfun;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bigfun.adapters.EventsListAdapter;
import com.epicodus.bigfun.models.UserEvents;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    //    @Bind(R.id.addEvent) ImageButton mAddEvent;
    @Bind(R.id.bSavedEvents)
    Button mSavedEvents;

    private EventsListAdapter mAdapter;

    public ArrayList<UserEvents> mEvents = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_events", "email", "public_profile"));
        getPlaces("");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        updateUI();
                        handleFacebookAccessToken(loginResult.getAccessToken());

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        updateUI();
//
//                                        try {
//
//                                            JSONObject events = object.getJSONObject("events");
//                                            JSONArray data = events.getJSONArray("data");
//
//                                            for (int i = 0; i < data.length(); i++) {
//                                                JSONObject eventData = data.getJSONObject(i);
//                                                JSONObject imageJSON = eventData.getJSONObject("cover");
//                                                String city = "";
//                                                String street = "";
//                                                String zip = "";
//                                                String longitude = "";
//                                                String latitude = "";
//                                                JSONObject placeJSON = eventData.getJSONObject("place");
//
//                                                try {
//                                                    JSONObject locationJSON = placeJSON.getJSONObject("location");
//                                                    city = locationJSON.optString("city", "");
//                                                    street = locationJSON.optString("street", "");
//                                                    zip = locationJSON.optString("zip", "");
//                                                    latitude = locationJSON.optString("latitude", "");
//                                                    longitude = locationJSON.optString("longitude", "");
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
////
//                                                String time = eventData.optString("start_time", "");
//                                                try {
//                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d'T'hh:mm:ssZ");
//                                                    Date date = dateFormat.parse(time);
//                                                    dateFormat = new SimpleDateFormat("E MMM d  h:mm:a");
//                                                    time = dateFormat.format(date);
//
//                                                } catch (java.text.ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                String imageUrl = imageJSON.optString("source", "");
//                                                String name = eventData.getString("name");
//                                                String description = eventData.optString("description", "");
//
//                                                UserEvents result = new UserEvents(name, description, imageUrl, city, street, time, zip, latitude, longitude);
//                                                mEvents.add(result);
//
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                        MainActivity.this.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                mAdapter = new EventsListAdapter(getApplicationContext(), mEvents);
//                                                mRecyclerView.setAdapter(mAdapter);
//                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                                                mRecyclerView.setLayoutManager(layoutManager);
//                                                mRecyclerView.setHasFixedSize(true);
//                                            }
//                                        });
                                    }

                                });

                        request.executeAsync();
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "events.limit(50){cover,id,start_time,place, name, description}");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        updateUI();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });

//        getPlaces("place");
        setContentView(R.layout.main);
        ButterKnife.bind(this);
//        mAddEvent.setOnClickListener(this);
        mSavedEvents.setOnClickListener(this);


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
            }
        };

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        greeting = (TextView) findViewById(R.id.greeting);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    private void getPlaces(String params) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        ArrayList<String> ids = new ArrayList<>();
                        try {
                            JSONObject object = response.getJSONObject();
                            JSONArray data = object.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject idData = data.getJSONObject(i);
                                String id = idData.getString("id");

                                ids.add(id);
                                getEvents(id);

                            }
                            Log.d("------",ids.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("type", "place");
        parameters.putString("q", "*");
        parameters.putString("center", "45.5231-122.6765");
        parameters.putString("distance", "1000");
        parameters.putString("fields", "name,id");
        parameters.putString("limit", "50");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getEvents(String params) {
       final String id = params.toString();
        GraphRequest request = GraphRequest.newGraphPathRequest(

        AccessToken.getCurrentAccessToken(),
                "/",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                    JSONObject graph = response.getJSONObject();

                        try {
                            JSONObject objectId = graph.getJSONObject(id);
                            try {
                                JSONObject events = objectId.getJSONObject("events");
                                JSONArray data = events.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject eventData = data.getJSONObject(i);
                                    JSONObject imageJSON = eventData.getJSONObject("cover");
                                    String city = "";
                                    String street = "";
                                    String zip = "";
                                    String longitude = "";
                                    String latitude = "";

                                    JSONObject placeJSON = eventData.getJSONObject("place");
                                    try {

                                        JSONObject locationJSON = placeJSON.getJSONObject("location");
                                        city = locationJSON.optString("city", "");
                                        street = locationJSON.optString("street", "");
                                        zip = locationJSON.optString("zip", "");
                                        latitude = locationJSON.optString("latitude", "");
                                        longitude = locationJSON.optString("longitude", "");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String time = eventData.optString("start_time", "");
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d'T'hh:mm:ssZ");
                                        Date date = dateFormat.parse(time);
                                        dateFormat = new SimpleDateFormat("E MMM d  h:mm:a");
                                        time = dateFormat.format(date);

                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String imageUrl = imageJSON.optString("source", "");
                                    String name = eventData.getString("name");
                                    String description = eventData.optString("description", "");

                                    UserEvents result = new UserEvents(name, description, imageUrl, city, street, time, zip, latitude, longitude);
                                    mEvents.add(result);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new EventsListAdapter(getApplicationContext(), mEvents);
                                    mRecyclerView.setAdapter(mAdapter);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                                    mRecyclerView.setLayoutManager(layoutManager);
                                    mRecyclerView.setHasFixedSize(true);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("ids", params);
        long unixTime = System.currentTimeMillis();
        Log.d("time --------", unixTime +"");
        parameters.putString("fields","events.limit(10).fields(cover,id,start_time,place, name, description).since(1472749338)");
        request.setParameters(parameters);
        request.executeAsync();

    }
//

    @Override
    public void onClick(View view) {
//        if(view == mAddEvent) {
//            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
//            startActivity(intent);
//        }
        if(view == mSavedEvents) {
            Intent intent = new Intent(MainActivity.this, SavedEventListActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            profilePictureView.setProfileId(profile.getId());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

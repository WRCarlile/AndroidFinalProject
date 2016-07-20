package com.epicodus.bigfun;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.bigfun.adapters.FirebaseEventViewHolder;
import com.epicodus.bigfun.models.UserEvents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedEventListActivity extends Activity {
        private DatabaseReference mEventReference;
        private FirebaseRecyclerAdapter mFirebaseAdapter;

        @Bind(R.id.recyclerView)
        RecyclerView mRecyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.saved_event_list);
            ButterKnife.bind(this);

            mEventReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_SAVED_EVENT);
            setUpFirebaseAdapter();
        }

        private void setUpFirebaseAdapter() {
            mFirebaseAdapter = new FirebaseRecyclerAdapter<UserEvents, FirebaseEventViewHolder>
                    (UserEvents.class, R.layout.event_list, FirebaseEventViewHolder.class,
                            mEventReference) {

                @Override
                protected void populateViewHolder(FirebaseEventViewHolder viewHolder,
                                                  UserEvents model, int position) {
                    viewHolder.bindEvent(model);
                }
            };
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mFirebaseAdapter);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mFirebaseAdapter.cleanup();
        }
    }

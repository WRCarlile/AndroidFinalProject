package com.epicodus.bigfun;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.View;

import com.epicodus.bigfun.adapters.FirebaseEventListAdapter;
import com.epicodus.bigfun.adapters.FirebaseEventViewHolder;
import com.epicodus.bigfun.models.UserEvents;
import com.epicodus.bigfun.utils.OnStartDragListener;
import com.epicodus.bigfun.utils.SimpleItemTouchHelperCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedEventListActivity extends FragmentActivity implements OnStartDragListener{
    private DatabaseReference mEventReference;
    private FirebaseEventListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;


    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.saved_event_list);
        ButterKnife.bind(this);

        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mEventReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_EVENT)
                .child(uid);

        mFirebaseAdapter = new FirebaseEventListAdapter(UserEvents.class,
                R.layout.event_list_item_drag, FirebaseEventViewHolder.class,
                mEventReference, this, this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}

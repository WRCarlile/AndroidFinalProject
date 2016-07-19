package com.epicodus.bigfun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.bigfun.models.UserEvents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddEventActivity extends FragmentActivity implements View.OnClickListener{


        @Bind(R.id.bCreateNew)
        Button mCreateNew;
        @Bind(R.id.eventNameInput)
        EditText mEventNamenInput;
        @Bind(R.id.eventDescriptionInput)
        EditText mEventDescriptionInput;

        private DatabaseReference mAddNewEvent;

//        private String name;
//        private String description;
//        private String imageUrl;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            mAddNewEvent = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child(Constants.FIREBASE_CHILD_SAVED_EVENT);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_event);
            ButterKnife.bind(this);
            mCreateNew.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if( view == mCreateNew) {
                String name = mEventNamenInput.getText().toString();
                String description = mEventDescriptionInput.getText().toString();
                String imageUrl = "hi";
                UserEvents eventAdd = new UserEvents(name, description, imageUrl);
                saveEventToFirebase(eventAdd);
                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        public void saveEventToFirebase(UserEvents eventAdd) {
            mAddNewEvent.push().setValue(eventAdd);
        }
    }



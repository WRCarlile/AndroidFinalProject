package com.epicodus.bigfun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.bigfun.models.UserEvents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddEventActivity extends FragmentActivity implements View.OnClickListener{
    private static final int REQUEST_IMAGE_CAPTURE = 111;

        @Bind(R.id.bCreateNew)
        Button mCreateNew;
        @Bind(R.id.eventNameInput)
        EditText mEventNamenInput;
        @Bind(R.id.eventDescriptionInput)
        EditText mEventDescriptionInput;

        private DatabaseReference mAddNewEvent;


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
        public void onLaunchCamera() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageLabel.setImageBitmap(imageBitmap);
                encodeBitmapAndSaveToFirebase(imageBitmap);
            }
        }
        public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference(Constants.FIREBASE_CHILD_SAVED_EVENT)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(mEvent.getPushId())
                    .child("imageUrl");
            ref.setValue(imageEncoded);
        }
    }



package com.epicodus.bigfun;

import org.parceler.Parcel;

public class UserEvents {
    private String mName;
    private String mDescription;
    private String mImageUrl;

    public UserEvents() {};

    public UserEvents(String name, String description, String image) {
        this.mName = name;
        this.mDescription = description;
        this.mImageUrl = image;

    }

    public String getName() {
        return mName;
    }
    public String getDescription() {
        return mDescription;
    }
    public String getImageUrl() {return mImageUrl;}

}


package com.epicodus.bigfun;


public class UserEvents {
    private String mName;
    private String mDescription;
    private String mImageUrl;


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


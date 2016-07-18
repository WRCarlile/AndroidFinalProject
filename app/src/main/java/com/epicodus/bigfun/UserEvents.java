package com.epicodus.bigfun;
import com.epicodus.bigfun.UserEvents;
import org.parceler.Parcel;
@Parcel
public class UserEvents {
    private String mName;
    private String mDescription;
    private String mImageUrl;

    public UserEvents() {};

    public UserEvents(String name, String description, String imageUrl) {
        this.mName = name;
        this.mDescription = description;
        this.mImageUrl = imageUrl;

    }

    public String getName() {
        return mName;
    }
    public String getDescription() {
        return mDescription;
    }
    public String getImageUrl() {return mImageUrl;}


}


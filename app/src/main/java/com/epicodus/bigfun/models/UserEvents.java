package com.epicodus.bigfun.models;
import org.parceler.Parcel;
@Parcel
public class UserEvents {
    String name;
    String description;
    String imageUrl;
    String pushId;
    String index;

    public UserEvents() {};

    public UserEvents(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.index = "not_specified";

    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageUrl() {return imageUrl;}


    public String getPushId(){
        return pushId;
    }

    public void setPushId(String pushId){
        this.pushId = pushId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}


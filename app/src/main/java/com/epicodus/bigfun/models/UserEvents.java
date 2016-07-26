package com.epicodus.bigfun.models;
import org.parceler.Parcel;
@Parcel
public class UserEvents {
    String name;
    String description;
    String imageUrl;
    String city;
    String street;
    String time;
    String zip;
    String latitude;
    String longitude;
    String pushId;
    String index;

    public UserEvents() {};

    public UserEvents(String name, String description, String imageUrl, String city, String street, String time, String zip, String latitude, String longitude) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.index = "not_specified";
        this.city = city;
        this.street = street;
        this.time = time;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageUrl() {return imageUrl;}
    public String getCity() {return city;}
    public String getStreet() {return street;}
    public String getTime() {return time;}
    public String getZip() {return zip;}
    public String getLatitude() {return latitude;}
    public String getLongitude() {return longitude;}



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


package com.example.dell.tourassistant.PlacePackage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 10/13/2017.
 */

public class SinglePlace implements Parcelable{

    private String name;
    private String placeLocation;
    private String placeId;
    private double distance;
    private boolean openNow=false;
    private double rating;
    private double lat;
    private double lon;
    public static  int catagoryId;

    public SinglePlace(String name, String placeLocation, String placeId, double distance, boolean openNow, double rating, double lat, double lon) {
        this.name = name;
        this.placeLocation = placeLocation;
        this.placeId = placeId;
        this.distance = distance;
        this.openNow = openNow;
        this.rating = rating;
        this.lat = lat;
        this.lon = lon;
    }

    public SinglePlace() {
            super();
    }

    protected SinglePlace(Parcel source){
        this.name = source.readString();
        this.placeLocation = source.readString();
        this.placeId = source.readString();
        this.distance = source.readDouble();
        this.openNow = source.readByte()!=0;
        this.rating = source.readDouble();
        this.lat = source.readDouble();
        this.lon = source.readDouble();
        this.catagoryId=source.readInt();
    }

    public static final Creator<SinglePlace>CREATOR=new Creator<SinglePlace>() {
        @Override
        public SinglePlace createFromParcel(Parcel source) {
            return new SinglePlace(source);
        }

        @Override
        public SinglePlace[] newArray(int size) {
            return new SinglePlace[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeString(this.placeLocation);
        dest.writeString(this.placeId);
        dest.writeDouble(this.distance);
        dest.writeByte((byte) (this.openNow ? 1 : 0));
        dest.writeDouble(this.rating);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeInt(this.catagoryId);


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static int getCatagoryId() {
        return catagoryId;
    }

    public static void setCatagoryId(int catagoryId) {
        SinglePlace.catagoryId = catagoryId;
    }
}

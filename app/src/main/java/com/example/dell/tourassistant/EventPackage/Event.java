package com.example.dell.tourassistant.EventPackage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 10/26/2017.
 */

public class Event implements Parcelable {
    private String destination;
    private double lattitude;
    private double longitude;
    private String fromDate;
    private String toDate;
    private int budget;
    private String key;
    public double totalExpense=0;
    private ArrayList<Expense> expenseList;
    private ArrayList<Moment> momentList;

    public Event(String destination, String fromDate, String toDate, int budget, double lattitude,double longitude) {
        this.destination = destination;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.budget = budget;
        this.lattitude = lattitude;
        this.longitude = longitude;
        expenseList = new ArrayList<>();
        momentList = new ArrayList<>();
    }

    public Event() {
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;

    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Expense> getExpenseList() {
        return expenseList;
    }

    public void addExpense(Expense expense){
        if (this.expenseList == null) {
           this.expenseList = new ArrayList<>();
        }
        this.expenseList.add(expense);
        totalExpense = totalExpense+ expense.getExpenseAmount();

    }

    public ArrayList<Moment> getMomentList() {
        return momentList;
    }
    public void addMoment(Moment moment){
        if (this.momentList == null){
            this.momentList = new ArrayList<Moment>();
        }
        this.momentList.add(moment);
    }

    public  double getTotalExpense() {
        return totalExpense;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    protected Event(Parcel source){

        destination = source.readString();
        fromDate = source.readString();
        toDate = source.readString();
        budget = source.readInt();
        key = source.readString();
        lattitude = source.readDouble();
        longitude = source.readDouble();
        expenseList = source.createTypedArrayList(Expense.CREATOR);
        momentList = source.createTypedArrayList(Moment.CREATOR);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destination);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeInt(budget);
        dest.writeString(key);
        dest.writeDouble(lattitude);
        dest.writeDouble(longitude);
        dest.writeTypedList(expenseList);
        dest.writeTypedList(momentList);
    }
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };


}

package com.example.dell.tourassistant.EventPackage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 10/26/2017.
 */

public class Expense implements Parcelable {


    private String expensePurpose;
    private double expenseAmount;
    private String expenseTime;

    public String getExpenseTime() {
        return expenseTime;
    }

    public Expense(String expensePurpose, double expenseAmount, String expenseTime) {
        this.expensePurpose = expensePurpose;
        this.expenseAmount = expenseAmount;
        this.expenseTime = expenseTime;

    }

    public Expense() {
    }

    protected Expense(Parcel in) {
        expensePurpose = in.readString();
        expenseAmount = in.readInt();
        expenseTime = in.readString();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public String getExpensePurpose() {
        return expensePurpose;
    }



    public double getExpenseAmount() {
        return expenseAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.expensePurpose);
        dest.writeDouble(this.expenseAmount);
        dest.writeString(this.expenseTime);

    }
}

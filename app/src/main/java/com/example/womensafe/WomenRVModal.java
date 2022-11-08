package com.example.womensafe;


import android.os.Parcel;
import android.os.Parcelable;

public class WomenRVModal {
    String username;
    String contactNo1;
    String contactNo2;
    String contactNo3;
    String contactNo4;
    String contactNo5;
    String userID;

    public WomenRVModal(String username, String contactNo1, String contactNo2, String contactNo3, String contactNo4, String contactNo5, String userID) {
        this.username = username;
        this.contactNo1 = contactNo1;
        this.contactNo2 = contactNo2;
        this.contactNo3 = contactNo3;
        this.contactNo4 = contactNo4;
        this.contactNo5 = contactNo5;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactNo1() {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public String getContactNo3() {
        return contactNo3;
    }

    public void setContactNo3(String contactNo3) {
        this.contactNo3 = contactNo3;
    }

    public String getContactNo4() {
        return contactNo4;
    }

    public void setContactNo4(String contactNo4) {
        this.contactNo4 = contactNo4;
    }

    public String getContactNo5() {
        return contactNo5;
    }

    public void setContactNo5(String contactNo5) {
        this.contactNo5 = contactNo5;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public WomenRVModal() {

    }

    @Override
    public String toString() {
        return "WomenRVModal{" +
                "username='" + username + '\'' +
                ", contactNo1='" + contactNo1 + '\'' +
                ", contactNo2='" + contactNo2 + '\'' +
                ", contactNo3='" + contactNo3 + '\'' +
                ", contactNo4='" + contactNo4 + '\'' +
                ", contactNo5='" + contactNo5 + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}





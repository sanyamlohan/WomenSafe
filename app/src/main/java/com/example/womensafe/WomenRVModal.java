package com.example.womensafe;


import android.os.Parcel;
import android.os.Parcelable;

public class WomenRVModal implements Parcelable {
    String Username;
    String ContactNo1;
    String ContactNo2;
    String ContactNo3;
    String ContactNo4;
    String ContactNo5;
    String userID;

    public WomenRVModal(){

    }

    public WomenRVModal(String username, String contactNo1, String contactNo2, String contactNo3, String contactNo4, String contactNo5, String userID) {
        Username = username;
        ContactNo1 = contactNo1;
        ContactNo2 = contactNo2;
        ContactNo3 = contactNo3;
        ContactNo4 = contactNo4;
        ContactNo5 = contactNo5;
        this.userID = userID;
    }

    protected WomenRVModal(Parcel in) {
        Username = in.readString();
        ContactNo1 = in.readString();
        ContactNo2 = in.readString();
        ContactNo3 = in.readString();
        ContactNo4 = in.readString();
        ContactNo5 = in.readString();
        userID = in.readString();
    }

    public static final Creator<WomenRVModal> CREATOR = new Creator<WomenRVModal>() {
        @Override
        public WomenRVModal createFromParcel(Parcel in) {
            return new WomenRVModal(in);
        }

        @Override
        public WomenRVModal[] newArray(int size) {
            return new WomenRVModal[size];
        }
    };

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String contactNo1) {
        ContactNo1 = contactNo1;
    }

    public String getContactNo2() {
        return ContactNo2;
    }

    public void setContactNo2(String contactNo2) {
        ContactNo2 = contactNo2;
    }

    public String getContactNo3() {
        return ContactNo3;
    }

    public void setContactNo3(String contactNo3) {
        ContactNo3 = contactNo3;
    }

    public String getContactNo4() {
        return ContactNo4;
    }

    public void setContactNo4(String contactNo4) {
        ContactNo4 = contactNo4;
    }

    public String getContactNo5() {
        return ContactNo5;
    }

    public void setContactNo5(String contactNo5) {
        ContactNo5 = contactNo5;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Username);
        parcel.writeString(ContactNo1);
        parcel.writeString(ContactNo2);
        parcel.writeString(ContactNo3);
        parcel.writeString(ContactNo4);
        parcel.writeString(ContactNo5);
        parcel.writeString(userID);
    }
}

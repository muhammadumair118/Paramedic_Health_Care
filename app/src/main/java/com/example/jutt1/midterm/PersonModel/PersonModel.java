package com.example.jutt1.midterm.PersonModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RatingBar;

import java.io.Serializable;

public class PersonModel implements Parcelable {
    private String Id;
    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String ImageURL;
    private String Address;
    private String Specialization;
    private String Rating_bar;
    private String Account_Status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Cnic;
    private String Blood_Group;
    private String Complain;

    public PersonModel(String name, String imageURL, String complain, String email) {
        Name = name;
        Email = email;
        ImageURL = imageURL;
        Complain = complain;
    }
    public PersonModel(String name, String imageURL, String phone, String email, String password) {
        Name = name;
        Email = email;
        ImageURL = imageURL;
        Phone = phone;
        Password = password;
    }

    public PersonModel(String id, String name, String email, String password, String phone, String imageURL, String address) {
        Id = id;
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        ImageURL = imageURL;
        Address = address;
    }

    public PersonModel(String id, String name, String email, String password, String phone, String imageURL, String address, String specialization, String rating_bar, String account_Status) {
        Id = id;
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        ImageURL = imageURL;
        Address = address;
        Specialization = specialization;
        Rating_bar = rating_bar;
        Account_Status = account_Status;
    }

    public String getCnic() {
        return Cnic;
    }

    public void setCnic(String cnic) {
        Cnic = cnic;
    }

    public String getBlood_Group() {
        return Blood_Group;
    }

    public void setBlood_Group(String blood_Group) {
        Blood_Group = blood_Group;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        Complain = complain;
    }

    public PersonModel(String id, String name, String email, String phone, String imageURL, String account_Status, String cnic, String blood_Group, String complain) {
        Id = id;
        Name = name;
        Email = email;
        Phone = phone;
        ImageURL = imageURL;
        Account_Status = account_Status;
        Cnic = cnic;
        Blood_Group = blood_Group;
        Complain = complain;

    }

    protected PersonModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Email = in.readString();
        Password = in.readString();
        Phone = in.readString();
        ImageURL = in.readString();
        Address = in.readString();
        Specialization = in.readString();
        Rating_bar = in.readString();
        Account_Status = in.readString();
    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel in) {
            return new PersonModel(in);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    public String getRating_bar() {
        return Rating_bar;
    }

    public void setRating_bar(String rating_bar) {
        Rating_bar = rating_bar;
    }

    public String getAccount_Status() {
        return Account_Status;
    }

    public void setAccount_Status(String account_Status) {
        Account_Status = account_Status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(Password);
        dest.writeString(Phone);
        dest.writeString(ImageURL);
        dest.writeString(Address);
        dest.writeString(Specialization);
        dest.writeString(Rating_bar);
        dest.writeString(Account_Status);

    }
}

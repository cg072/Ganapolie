package com.kch.www.newkchproject.DataSet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by YONSAI on 2017-05-17.
 */

public class MoveListDataSet implements Parcelable {

    String name;
    String number;
    Bitmap img;

    public MoveListDataSet(String name, String number, Bitmap img) {
        this.name = name;
        this.number = number;
        this.img = img;
    }

    protected MoveListDataSet(Parcel in) {
        name = in.readString();
        number = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<MoveListDataSet> CREATOR = new Creator<MoveListDataSet>() {
        @Override
        public MoveListDataSet createFromParcel(Parcel in) {
            return new MoveListDataSet(in);
        }

        @Override
        public MoveListDataSet[] newArray(int size) {
            return new MoveListDataSet[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeParcelable(img, flags);
    }

}

package com.james.papertales.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WallData implements Parcelable {

    public String name, desc, date, url, authorName;
    public int authorId;
    public ArrayList<String> images, categories;

    public WallData(String name, String desc, String date, String url, ArrayList<String> images, ArrayList<String> categories, String authorName, int authorId) {
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.url = url;
        this.images = images;
        this.categories = categories;
        this.authorName = authorName;
        this.authorId = authorId;
    }

    protected WallData(Parcel in) {
        name = in.readString();
        desc = in.readString();
        date = in.readString();
        url = in.readString();
        images = new ArrayList<>();
        in.readList(images, String.class.getClassLoader());
        categories = new ArrayList<>();
        in.readList(categories, String.class.getClassLoader());
        authorName = in.readString();
        authorId = in.readInt();
    }

    public static final Creator<WallData> CREATOR = new Creator<WallData>() {
        @Override
        public WallData createFromParcel(Parcel in) {
            return new WallData(in);
        }

        @Override
        public WallData[] newArray(int size) {
            return new WallData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(url);
        dest.writeList(images);
        dest.writeList(categories);
        dest.writeString(authorName);
        dest.writeInt(authorId);
    }
}

package com.james.papertales.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WallData implements Parcelable {

    public String name, desc, authorName;
    public int authorId;
    public ArrayList<String> images;

    public WallData(String name, String desc, ArrayList<String> images, String authorName, int authorId) {
        this.name = name;
        this.desc = desc;
        this.images = images;
        this.authorName = authorName;
        this.authorId = authorId;
    }

    protected WallData(Parcel in) {
        name = in.readString();
        desc = in.readString();
        images = new ArrayList<>();
        in.readList(images, String.class.getClassLoader());
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
        dest.writeList(images);
        dest.writeString(authorName);
        dest.writeInt(authorId);
    }
}

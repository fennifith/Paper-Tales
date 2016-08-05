package com.james.papertales.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TextListData implements Parcelable {

    public static final Creator<TextListData> CREATOR = new Creator<TextListData>() {
        public TextListData createFromParcel(Parcel in) {
            return new TextListData(in);
        }

        public TextListData[] newArray(int size) {
            return new TextListData[size];
        }
    };

    public String name;
    public String content;
    public String primary;

    public TextListData(String name, String content, String primary) {
        this.name = name;
        this.content = content;
        this.primary = primary;
    }

    public TextListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        name = in.readString();
        content = in.readString();
        primary = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(content);
        out.writeString(primary);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

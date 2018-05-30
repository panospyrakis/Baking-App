package com.udacity.spyrakis.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StepsItem implements Parcelable {

    @SerializedName("videoURL")
    private String videoURL;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private int id;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public String toString() {
        return
                "StepsItem{" +
                        "videoURL = '" + videoURL + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",shortDescription = '" + shortDescription + '\'' +
                        ",thumbnailURL = '" + thumbnailURL + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoURL);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.thumbnailURL);
    }

    public StepsItem() {
    }

    protected StepsItem(Parcel in) {
        this.videoURL = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<StepsItem> CREATOR = new Parcelable.Creator<StepsItem>() {
        @Override
        public StepsItem createFromParcel(Parcel source) {
            return new StepsItem(source);
        }

        @Override
        public StepsItem[] newArray(int size) {
            return new StepsItem[size];
        }
    };
}
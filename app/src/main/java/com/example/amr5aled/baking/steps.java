package com.example.amr5aled.baking;


import android.os.Parcel;
import android.os.Parcelable;

public class steps implements Parcelable{

    private int id,recipe_ID ;
    private String shortDescription,description,videoURL,thumbnailURL ;

    public steps(int id, String shortDescription, String description, String videoURL,int recipe_ID,String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.recipe_ID=recipe_ID ;
        this.thumbnailURL=thumbnailURL ;
    }

    protected steps(Parcel in) {
        id = in.readInt();
        recipe_ID = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<steps> CREATOR = new Creator<steps>() {
        @Override
        public steps createFromParcel(Parcel in) {
            return new steps(in);
        }

        @Override
        public steps[] newArray(int size) {
            return new steps[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(recipe_ID);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}

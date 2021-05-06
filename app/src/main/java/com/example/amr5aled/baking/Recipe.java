package com.example.amr5aled.baking;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;



public class Recipe implements Parcelable{
    private int id ,servings;
    private String name ,image ;
    private ArrayList<ingredients> ingredient_list ;
    private ArrayList<steps> step_list ;

    public Recipe(int id, int servings, String name, String image, ArrayList<ingredients> ingredient_list, ArrayList<steps> step_list) {
        this.id = id;
        this.servings = servings;
        this.name = name;
        this.image = image;
        this.ingredient_list = ingredient_list;
        this.step_list = step_list;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<ingredients> getIngredient_list() {
        return ingredient_list;
    }

    public void setIngredient_list(ArrayList<ingredients> ingredient_list) {
        this.ingredient_list = ingredient_list;
    }

    public ArrayList<steps> getStep_list() {
        return step_list;
    }

    public void setStep_list(ArrayList<steps> step_list) {
        this.step_list = step_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeString(image);
    }
}

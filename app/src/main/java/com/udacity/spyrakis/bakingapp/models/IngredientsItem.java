package com.udacity.spyrakis.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IngredientsItem implements Parcelable {

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return
                "IngredientsItem{" +
                        "quantity = '" + quantity + '\'' +
                        ",measure = '" + measure + '\'' +
                        ",ingredient = '" + ingredient + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public IngredientsItem() {
    }

    protected IngredientsItem(Parcel in) {
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<IngredientsItem> CREATOR = new Parcelable.Creator<IngredientsItem>() {
        @Override
        public IngredientsItem createFromParcel(Parcel source) {
            return new IngredientsItem(source);
        }

        @Override
        public IngredientsItem[] newArray(int size) {
            return new IngredientsItem[size];
        }
    };
}
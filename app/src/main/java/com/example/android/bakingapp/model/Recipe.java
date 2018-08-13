package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Recipe implements Parcelable{
    private int id;
    private String name;
    private int servings;
    private String image;
    private List<RecipeStep> steps;
    private List<Ingredient> ingredients;

    public Recipe(int id, String name, int servings, String image,
                  List<RecipeStep> steps, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(servings);
        out.writeString(image);
        out.writeTypedList(steps);
        out.writeTypedList(ingredients);
    }

    public static final Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        steps = in.createTypedArrayList(RecipeStep.CREATOR);
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}

package com.example.android.bakingapp.util;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParsingUtils {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String LONG_DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";
    private static final String JSON_CUP = "CUP";
    private static final String JSON_TABLESPOON = "TBLSP";
    private static final String JSON_TEASPOON = "TSP";
    private static final String JSON_GRAM = "G";
    private static final String JSON_KILOGRAM = "K";
    private static final String JSON_OUNCE = "OZ";
    private static final String CUP = "cup";
    private static final String TABLESPOON = "tbsp";
    private static final String TEASPOON = "tsp";
    private static final String GRAM = "g";
    private static final String KILOGRAM = "kg";
    private static final String OUNCE = "oz";
    public static final String NO_MEASURE = "UNIT";

    public static List<Recipe> parseRecipes(String jsonString) {
        try {
            JSONArray recipesArray = new JSONArray(jsonString);
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (int i = 0; i < recipesArray.length(); i++) {
                Recipe recipe = parseRecipe(recipesArray.optJSONObject(i));
                if (recipe != null) recipes.add(recipe);
            }
            return recipes;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Recipe parseRecipe(JSONObject recipeJson) {
        if (recipeJson == null) return null;

        int id = recipeJson.optInt(ID);
        String name = recipeJson.optString(NAME);
        int servings = recipeJson.optInt(SERVINGS);
        String image = recipeJson.optString(IMAGE);
        JSONArray ingredientsJson = recipeJson.optJSONArray(INGREDIENTS);
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientsJson.length(); i++) {
            JSONObject ingredientJson = ingredientsJson.optJSONObject(i);
            Ingredient ingredient = parseRecipeIngredient(ingredientJson);
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }
        JSONArray stepsJson = recipeJson.optJSONArray(STEPS);
        ArrayList<RecipeStep> steps = new ArrayList<>();

        for (int i = 0; i < stepsJson.length(); i++) {
            JSONObject stepJson = stepsJson.optJSONObject(i);
            RecipeStep recipeStep = parseRecipeStep(stepJson);
            if (recipeStep != null) {
                steps.add(recipeStep);
            }
        }
        return new Recipe(id, name, servings, image, steps, ingredients);
    }

    public static RecipeStep parseRecipeStep(JSONObject stepJson) {
        if (stepJson == null) return null;

        int id = stepJson.optInt(ID);
        String shortDescription = stepJson.optString(SHORT_DESCRIPTION);
        String description = stepJson.optString(LONG_DESCRIPTION);
        String videoUrl = stepJson.optString(VIDEO_URL);
        String thumbnailUrl = stepJson.optString(THUMBNAIL_URL);
        return new RecipeStep(id, shortDescription, description, videoUrl, thumbnailUrl);
    }

    public static Ingredient parseRecipeIngredient(JSONObject ingredientJson) {
        if (ingredientJson == null) return null;

        int quantity = ingredientJson.optInt(QUANTITY);
        String measure = parseMeasure(ingredientJson.optString(MEASURE));
        String ingredient = ingredientJson.optString(RECIPE_INGREDIENT);
        return new Ingredient(quantity, measure, ingredient);
    }

    private static String parseMeasure(String measure) {
        if (measure == null) return null;

        switch (measure) {
            case JSON_CUP:
                return CUP;
            case JSON_TABLESPOON:
                return TABLESPOON;
            case JSON_TEASPOON:
                return TEASPOON;
            case JSON_GRAM:
                return GRAM;
            case JSON_KILOGRAM:
                return KILOGRAM;
            case JSON_OUNCE:
                return OUNCE;
            default:
                return measure;
        }
    }
}


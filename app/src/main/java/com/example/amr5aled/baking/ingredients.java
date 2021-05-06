package com.example.amr5aled.baking;



public class ingredients {
     private double quantity;
     private String measure,ingredient ;
    int recipe_ID ;
    public ingredients(double quantity, String measure, String ingredient,int recipe_ID) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipe_ID=recipe_ID ;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }
}

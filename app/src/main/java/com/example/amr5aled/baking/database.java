    package com.example.amr5aled.baking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;



public class database extends SQLiteOpenHelper {
    private static final String name ="database";
    private static final int version =2 ;
    final String table1 ="ingredients" ;
    final String table2 ="steps" ;
    final String table3 ="Recipe" ;
    final String create1="CREATE TABLE "+table1+"( quantity INTEGER , measure VARCHAR(30),  ingredient VARCHAR(30) ,recipe_ID INTEGER)" ;
    final String create2="CREATE TABLE "+table2+"( id INTEGER , shortDescription VARCHAR(30) ,description VARCHAR(30),videoURL VARCHAR(100) ,recipe_ID INTEGER , thumbnailURL VARCHAR(30) )" ;
    final String create3="CREATE TABLE "+table3+"( id INTEGER, servings INTEGER , name VARCHAR(30), image VARCHAR(50))" ;

    Context context ;
    public database(Context context) {
        super(context, name, null, version);
        this.context=context ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create1);
        db.execSQL(create2);
        db.execSQL(create3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table1);
        db.execSQL("DROP TABLE IF EXISTS "+table2);
        db.execSQL("DROP TABLE IF EXISTS "+table3);
        onCreate(db);
    }



    public ArrayList<ingredients> restore_ingred(int id)
    {
        ArrayList<ingredients> array_values = new ArrayList<>() ;
        SQLiteDatabase sql = this.getReadableDatabase() ;
        Cursor cursor = sql.rawQuery("SELECT * FROM "+table1+ " WHERE recipe_ID = "+id ,null) ;
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            array_values.add(new ingredients(cursor.getInt(cursor.getColumnIndex("quantity")),cursor.getString(cursor.getColumnIndex("measure")),cursor.getString(cursor.getColumnIndex("ingredient")),cursor.getInt(cursor.getColumnIndex("recipe_ID"))));
            cursor.moveToNext();
        }

        return array_values;
    }


    public ArrayList<steps> restore_steps(int id)
    {
        ArrayList<steps> array_values = new ArrayList<>() ;
        SQLiteDatabase sql = this.getReadableDatabase() ;
        Cursor cursor = sql.rawQuery("SELECT * FROM "+table2 + " WHERE recipe_ID = "+id,null) ;
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            array_values.add(new steps(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("shortDescription")),cursor.getString(cursor.getColumnIndex("description")),cursor.getString(cursor.getColumnIndex("videoURL")),cursor.getInt(cursor.getColumnIndex("recipe_ID")),cursor.getString(cursor.getColumnIndex("thumbnailURL"))));
            cursor.moveToNext();
        }

        return array_values;
    }


    public boolean insert_data_recipe(ArrayList<Recipe> recip) {

        SQLiteDatabase sqLitewrite = this.getWritableDatabase();
        for (int i = 0; i < recip.size(); i++) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", recip.get(i).getId());
            contentValues.put("servings", recip.get(i).getServings());
            contentValues.put("name", recip.get(i).getName());
            contentValues.put("image", recip.get(i).getImage());

            for (int j = 0; j < recip.get(i).getStep_list().size(); j++) {
                ContentValues contentValues1 = new ContentValues() ;

                contentValues1.put("id",recip.get(i).getStep_list().get(j).getId());
                contentValues1.put("shortDescription",recip.get(i).getStep_list().get(j).getShortDescription());
                contentValues1.put("description",recip.get(i).getStep_list().get(j).getShortDescription());
                contentValues1.put("videoURL",recip.get(i).getStep_list().get(j).getVideoURL());
                contentValues1.put("recipe_ID",recip.get(i).getStep_list().get(j).getRecipe_ID());
                sqLitewrite.insert(table2,null,contentValues1);
            }
            for (int k = 0; k < recip.get(i).getIngredient_list().size(); k++) {
                ContentValues contentValues2 = new ContentValues() ;
                contentValues2.put("quantity",recip.get(i).getIngredient_list().get(k).getQuantity());
                contentValues2.put("measure",recip.get(i).getIngredient_list().get(k).getMeasure());
                contentValues2.put("ingredient",recip.get(i).getIngredient_list().get(k).getIngredient());
                contentValues2.put("recipe_ID",recip.get(i).getIngredient_list().get(k).getRecipe_ID());
                sqLitewrite.insert(table1,null,contentValues2);
            }

            sqLitewrite.insert(table3, null, contentValues);
        }
        sqLitewrite.close();
        return true ;
    }
    public ArrayList<Recipe> restore_recipe()
    {
        ArrayList<Recipe> array_values = new ArrayList<>() ;
        SQLiteDatabase sql = this.getReadableDatabase() ;
        Cursor cursor = sql.rawQuery("SELECT * FROM "+table3 ,null) ;
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            int id =cursor.getInt(cursor.getColumnIndex("id")) ;
            ArrayList<ingredients> list1 =restore_ingred(id);
            ArrayList<steps> list2 = restore_steps(id);
            array_values.add(new Recipe(id,cursor.getInt(cursor.getColumnIndex("servings")),cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("image")),list1,list2));
            cursor.moveToNext();
        }

        return array_values;
    }
    public int delete_it()
    {
        SQLiteDatabase sqLiteread = this.getReadableDatabase() ;
        String []array={};
         sqLiteread.delete(table1,null,array) ;
         sqLiteread.delete(table2,null,array);
         sqLiteread.delete(table3,null,array);
        return 0;
    }

    public ArrayList<String>getrecipe()
    {
        ArrayList<String> array_values = new ArrayList<>() ;
        SQLiteDatabase sql = this.getReadableDatabase() ;
        Cursor cursor = sql.rawQuery("SELECT * FROM "+table3 ,null) ;
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {

            array_values.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        return array_values ;
    }

}

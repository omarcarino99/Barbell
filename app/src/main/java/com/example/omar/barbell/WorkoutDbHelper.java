package com.example.omar.barbell;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workout.db";
    public static final int VERSION_NUMBER = 3;


    public WorkoutDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_WORKOUTS = "CREATE TABLE " + WorkoutContract.WorkoutEntry.TABLE_NAME_WORKOUT + " ("
                + WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY, "
                + WorkoutContract.WorkoutEntry.WORKOUT_TITLE + " STRING, "
                + WorkoutContract.WorkoutEntry.WORKOUT_DATE + " STRING" + ")";

        String CREATE_TABLE_EXERCISES = "CREATE TABLE " + WorkoutContract.WorkoutEntry.TABLE_NAME_EXERCISES + " ("
                + WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY, "
                + WorkoutContract.WorkoutEntry.EXERCISE_NAME  + " STRING, "
                + WorkoutContract.WorkoutEntry.REPS + " INTEGER, "
                + WorkoutContract.WorkoutEntry.RPE + " INTEGER, "
                + WorkoutContract.WorkoutEntry.WEIGHT + " INTEGER, "
                + WorkoutContract.WorkoutEntry.WORKOUT_ID + " INTEGER, "
                + "FOREIGN KEY"+"("+ WorkoutContract.WorkoutEntry.WORKOUT_ID+")" + " REFERENCES "
                + WorkoutContract.WorkoutEntry.TABLE_NAME_WORKOUT
                +"("
                +(WorkoutContract.WorkoutEntry._ID) +")" +")";
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_EXERCISES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


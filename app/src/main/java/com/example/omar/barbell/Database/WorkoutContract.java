package com.example.omar.barbell.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class WorkoutContract {

    private WorkoutContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.omar.barbell";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WORKOUT = "workout";
    public static final String PATH_EXERCISE = "exercise";

    public static final class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME_WORKOUT = "workouts";
        public static final String TABLE_NAME_EXERCISES = "exercises";
        public static final String JOIN_TABLE = "workouts/exercises";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WORKOUT);
        public static final Uri CONTENT_URI_EXERCISE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXERCISE);
        public static final Uri JOIN_TABLE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, JOIN_TABLE);

        public static final String _ID = BaseColumns._ID;

        //workout table columns
        public static final String WORKOUT_TITLE = "workout_title";
        public static final String WORKOUT_DATE = "workout_date";

        //exercise table columns
        public static final String EXERCISE_NAME = "exercise_name";
        public static final String WORKOUT_ID = "workout_id";
        public static final String REPS = "reps";
        public static final String RPE = "rpe";
        public static final String WEIGHT = "weight";
    }


}


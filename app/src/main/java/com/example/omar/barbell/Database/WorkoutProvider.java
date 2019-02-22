package com.example.omar.barbell.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.omar.barbell.R;

public class WorkoutProvider extends ContentProvider {

    private static final String LOG_TAG = WorkoutProvider.class.getSimpleName();
    private WorkoutDbHelper dbHelper;
    public static final int WORKOUT = 100;
    public static final int WORKOUT_ID = 101;
    public static final int EXERCISE = 102;
    public static final int EXERCISE_ID = 103;
    public static final int WORkOUT_EXERCISE = 104;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_WORKOUT, WORKOUT);
        uriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_WORKOUT + "/#", WORKOUT_ID);
        uriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_EXERCISE, EXERCISE);
        uriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_EXERCISE + "/#", EXERCISE_ID);
        uriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.WorkoutEntry.JOIN_TABLE + "/#", WORkOUT_EXERCISE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WorkoutDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = uriMatcher.match(uri);
        switch (match) {
            case WORKOUT:
                cursor = db.query(WorkoutContract.WorkoutEntry.TABLE_NAME_WORKOUT, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WORKOUT_ID:
                selection = WorkoutContract.WorkoutEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(WorkoutContract.WorkoutEntry.TABLE_NAME_WORKOUT, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EXERCISE:
                cursor = db.query(WorkoutContract.WorkoutEntry.TABLE_NAME_EXERCISES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EXERCISE_ID:
                selection = WorkoutContract.WorkoutEntry.WORKOUT_ID + "=? " + " AND " + WorkoutContract.WorkoutEntry._ID + "=?";
                cursor = db.query(WorkoutContract.WorkoutEntry.TABLE_NAME_EXERCISES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WORkOUT_EXERCISE:
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                final String QUEREY = "SELECT exercises._id, " +
                        "exercise_name," +
                        "weight," +
                        "reps," +
                        "rpe" +
                        " FROM " +
                        "exercises" +
                        " INNER JOIN workouts ON workouts._id = exercises.workout_id" +
                        " WHERE workouts._id=?";
                cursor = db.rawQuery(QUEREY, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Failed to retrieve" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        int rowcount = cursor.getCount();
        Log.d("ROWCOUNT", "Number of rows = " + String.valueOf(rowcount));
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case WORKOUT:
                return insertWorkout(uri, values);
            case EXERCISE:
                return insertExercise(uri, values);
            default:
                throw new IllegalArgumentException("Insertion Failed" + uri);
        }
    }

    private Uri insertExercise(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME_EXERCISES, null, values);
        if (id == -1) {
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            Toast.makeText(getContext(), "It worked!", Toast.LENGTH_SHORT).show();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertWorkout(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME_WORKOUT, null, values);
        if (id == -1) {
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            Toast.makeText(getContext(), "It worked!", Toast.LENGTH_SHORT).show();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case EXERCISE_ID:
                selection = WorkoutContract.WorkoutEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateExercise(uri, values, selection, selectionArgs);
        }
        return 0;
    }

    private int updateExercise(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(WorkoutContract.WorkoutEntry.EXERCISE_NAME)) {
            String exerciseName = values.getAsString(WorkoutContract.WorkoutEntry.EXERCISE_NAME);
            if (exerciseName == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.MISSING_INPUT_MESSAGE));
            }
        }
        if (values.containsKey(WorkoutContract.WorkoutEntry.WEIGHT)) {
            String weight = values.getAsString(WorkoutContract.WorkoutEntry.WEIGHT);
            if (weight == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.MISSING_INPUT_MESSAGE));
            }
        }

        if (values.containsKey(WorkoutContract.WorkoutEntry.REPS)) {
            String reps = values.getAsString(WorkoutContract.WorkoutEntry.REPS);
            if (reps == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.MISSING_INPUT_MESSAGE));
            }
        }

        if (values.containsKey(WorkoutContract.WorkoutEntry.RPE)) {
            String exerciseName = values.getAsString(WorkoutContract.WorkoutEntry.RPE);
            if (exerciseName == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.MISSING_INPUT_MESSAGE));
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = db.update(WorkoutContract.WorkoutEntry.TABLE_NAME_EXERCISES, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}


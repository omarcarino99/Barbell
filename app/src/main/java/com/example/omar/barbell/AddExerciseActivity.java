package com.example.omar.barbell;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentUris.withAppendedId;

public class AddExerciseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText exerciseNameET;
    private EditText weightET;
    private EditText repsET;
    private EditText rpeET;
    private Button saveButton;
    private Uri uri;
    private long workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        Intent intent = getIntent();
        workoutId = intent.getLongExtra("value", 0);

        uri = intent.getData();
        if (uri != null) {
            getLoaderManager().initLoader(0, null, this);
        }

        exerciseNameET = findViewById(R.id.exercise_name_edit_text);
        weightET = findViewById(R.id.weight);
        repsET = findViewById(R.id.reps);
        rpeET = findViewById(R.id.rpe);
        saveButton = findViewById(R.id.save_exercise_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExercise();
            }
        });
    }

    private void saveExercise() {
        String exerciseName = exerciseNameET.getText().toString();
        String weight = weightET.getText().toString();
        String reps = repsET.getText().toString();
        String rpe = rpeET.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WorkoutContract.WorkoutEntry.EXERCISE_NAME,exerciseName);
        values.put(WorkoutContract.WorkoutEntry.WEIGHT,weight);
        values.put(WorkoutContract.WorkoutEntry.REPS,reps);
        values.put(WorkoutContract.WorkoutEntry.RPE,rpe);
        values.put(WorkoutContract.WorkoutEntry.WORKOUT_ID, workoutId);

        Uri uri = getContentResolver().insert(WorkoutContract.WorkoutEntry.CONTENT_URI_EXERCISE, values);
        Intent intent = new Intent(AddExerciseActivity.this,ExercisesListActivity.class);
        Uri exerciseListUri = withAppendedId(WorkoutContract.WorkoutEntry.JOIN_TABLE_URI, workoutId);
        intent.setData(exerciseListUri);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {
                WorkoutContract.WorkoutEntry._ID,
        };

        return new CursorLoader(
                this,
                uri,
                projections,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String exerciseName = data.getString(data.getColumnIndexOrThrow("exercise_name"));
            String weight = data.getString(data.getColumnIndexOrThrow("weight"));
            String reps = data.getString(data.getColumnIndexOrThrow("reps"));
            String rpe = data.getString(data.getColumnIndexOrThrow("rpe"));

            exerciseNameET.setText(exerciseName);
            weightET.setText(weight);
            repsET.setText(reps);
            rpeET.setText(rpe);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        exerciseNameET.setText("");
        weightET.setText("");
        repsET.setText("");
        rpeET.setText("");
    }
}

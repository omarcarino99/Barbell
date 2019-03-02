package com.example.omar.barbell.UI;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.omar.barbell.Database.WorkoutContract;
import com.example.omar.barbell.ExerciseListAdapter;
import com.example.omar.barbell.R;

import static android.content.ContentUris.withAppendedId;

public class AddExerciseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText exerciseNameET;
    private EditText weightET;
    private EditText repsET;
    private EditText rpeET;
    private Button saveButton;
    private Uri exerciseUri;
    private long workoutId;
    private long exerciseId;
    private String stringWorkoutId;
    private String stringExerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exerciseNameET = findViewById(R.id.exercise_name_edit_text);
        weightET = findViewById(R.id.weight);
        repsET = findViewById(R.id.reps);
        rpeET = findViewById(R.id.rpe);
        saveButton = findViewById(R.id.save_exercise_button);

        Intent intent = getIntent();
        exerciseUri = intent.getData();

        if (exerciseUri != null) {
            workoutId = intent.getLongExtra("workoutId", 0);
            exerciseId = intent.getLongExtra("exerciseId", 0);
            stringWorkoutId = String.valueOf(workoutId);
            stringExerciseId = String.valueOf(exerciseId);
            getLoaderManager().initLoader(0, null, this);
        } else {
            workoutId = intent.getLongExtra("workoutIdValue", 0);
        }

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
        values.put(WorkoutContract.WorkoutEntry.EXERCISE_NAME, exerciseName);
        values.put(WorkoutContract.WorkoutEntry.WEIGHT, weight);
        values.put(WorkoutContract.WorkoutEntry.REPS, reps);
        values.put(WorkoutContract.WorkoutEntry.RPE, rpe);
        values.put(WorkoutContract.WorkoutEntry.WORKOUT_ID, workoutId);

        Intent intent = new Intent(AddExerciseActivity.this, ExercisesListActivity.class);
        Uri exerciseListUri = withAppendedId(WorkoutContract.WorkoutEntry.JOIN_TABLE_URI, workoutId);
        intent.setData(exerciseListUri);

        if (exerciseUri == null) {
            Uri uri = getContentResolver().insert(WorkoutContract.WorkoutEntry.CONTENT_URI_EXERCISE, values);
            intent.putExtra("id", workoutId);
        } else {
            int rowsUpdated = getContentResolver().update(exerciseUri, values, null, null);
            intent.putExtra("id", workoutId);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu_button:
                deleteExercise();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteExercise() {
        int rowsDeleted = 0;
        String[] selectionArgs = new String[]{stringExerciseId};
        rowsDeleted = getContentResolver().delete(exerciseUri, null, selectionArgs);
        Uri uri = withAppendedId(WorkoutContract.WorkoutEntry.JOIN_TABLE_URI, workoutId);
        Intent intent = new Intent(AddExerciseActivity.this, ExercisesListActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {
                WorkoutContract.WorkoutEntry._ID,
                WorkoutContract.WorkoutEntry.EXERCISE_NAME,
                WorkoutContract.WorkoutEntry.WEIGHT,
                WorkoutContract.WorkoutEntry.REPS,
                WorkoutContract.WorkoutEntry.RPE,
                WorkoutContract.WorkoutEntry.WORKOUT_ID
        };

        String[] selectionArgs = new String[]{stringWorkoutId, stringExerciseId};
        return new CursorLoader(
                this,
                exerciseUri,
                projections,
                null,
                selectionArgs,
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

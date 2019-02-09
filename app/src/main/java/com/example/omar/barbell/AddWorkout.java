package com.example.omar.barbell;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddWorkout extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private Uri workoutUri;
    private EditText workoutTitleET;
    private EditText workoutDateET;
    private Button saveWorkoutButton;
    private int workoutId;
    private ExerciseListAdapter adapter;
    private static final int EXISTING_WORKOUT_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        Intent intent = getIntent();
        workoutUri = intent.getData();
        if (workoutUri != null){
            getLoaderManager().initLoader(EXISTING_WORKOUT_LOADER,null,this);
        }
        workoutTitleET =(EditText) findViewById(R.id.workout_title_edit_text);
        workoutDateET =(EditText) findViewById(R.id.workout_date_edit_text);
        saveWorkoutButton = findViewById(R.id.save_workout_button);

        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });


    }

    private void saveWorkout() {
        String workoutTitle = workoutTitleET.getText().toString();
        String workoutDate = workoutDateET.getText().toString();
        if (workoutTitle.isEmpty() || workoutDate.isEmpty()){
            Toast.makeText(this, "Please fill in the missing areas", Toast.LENGTH_SHORT).show();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkoutContract.WorkoutEntry.WORKOUT_TITLE,workoutTitle);
        contentValues.put(WorkoutContract.WorkoutEntry.WORKOUT_DATE,workoutDate);
        Uri uri = getContentResolver().insert(WorkoutContract.WorkoutEntry.CONTENT_URI,contentValues);
        Intent intent = new Intent(AddWorkout.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {
                BaseColumns._ID,
                WorkoutContract.WorkoutEntry.WORKOUT_TITLE,
                WorkoutContract.WorkoutEntry.WORKOUT_DATE
        };


        return new CursorLoader(
                this,
                workoutUri,
                projections,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String workoutTitileIndex = data.getString(data.getColumnIndexOrThrow("workout_title"));
            String workoutDate = data.getString(data.getColumnIndexOrThrow("workout_date"));


            workoutTitleET.setText(workoutTitileIndex);
            workoutDateET.setText(workoutDate);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        workoutTitleET.setText("");
        workoutDateET.setText("");
    }
}


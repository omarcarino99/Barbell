package com.example.omar.barbell;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ExercisesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uri;
    private ExerciseListAdapter adapter;
    private FloatingActionButton addExerciseButton;
    private int workoutIdInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        addExerciseButton = findViewById(R.id.add_exercise_button);

        ListView list = findViewById(R.id.exercise_list);
        adapter = new ExerciseListAdapter(this,null);
        list.setAdapter(adapter);
        final Intent intent = getIntent();
        uri = intent.getData();
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent exerciseInent = new Intent(ExercisesListActivity.this,AddExerciseActivity.class);
                intent.setData(uri);
                startActivity(exerciseInent);
            }
        });
        getLoaderManager().initLoader(0, null, this);
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
            adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

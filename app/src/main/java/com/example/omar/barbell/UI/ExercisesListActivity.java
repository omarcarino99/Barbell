package com.example.omar.barbell.UI;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.omar.barbell.Database.WorkoutContract;
import com.example.omar.barbell.Database.WorkoutDbHelper;
import com.example.omar.barbell.ExerciseListAdapter;
import com.example.omar.barbell.R;

import static android.content.ContentUris.withAppendedId;

public class ExercisesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private WorkoutDbHelper dbHelper;
    private Uri uri;
    private ExerciseListAdapter adapter;
    private FloatingActionButton addExerciseButton;
    private long intentValue;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);

        addExerciseButton = findViewById(R.id.add_exercise_button);

        final ListView list = findViewById(R.id.exercise_list);
        adapter = new ExerciseListAdapter(this, null);
        list.setAdapter(adapter);

        getIntents();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExercisesListActivity.this, AddExerciseActivity.class);
                Uri uri = withAppendedId(WorkoutContract.WorkoutEntry.CONTENT_URI_EXERCISE, id);
                long idValue = adapter.getItemId(position);

                intent.putExtra("workoutId", intentValue);
                intent.putExtra("exerciseId", idValue);

                intent.setData(uri);
                startActivity(intent);
            }
        });
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exerciseListIntent = new Intent(ExercisesListActivity.this, AddExerciseActivity.class);
                exerciseListIntent.putExtra("workoutIdValue", intentValue);
                startActivity(exerciseListIntent);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    private void getIntents() {
        intent = getIntent();
        uri = intent.getData();
        intentValue = intent.getLongExtra("id", 0);
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

package com.example.omar.barbell.UI;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.omar.barbell.Database.WorkoutContract;
import com.example.omar.barbell.ExerciseListAdapter;
import com.example.omar.barbell.R;

import static android.content.ContentUris.withAppendedId;

public class ExercisesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uri;
    private ExerciseListAdapter adapter;
    private FloatingActionButton addExerciseButton;
    public long intentValue;
    private String workoutName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView list = findViewById(R.id.exercise_list);

        View emptyView = findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);

        adapter = new ExerciseListAdapter(this, null);
        list.setAdapter(adapter);

        addExerciseButton = findViewById(R.id.add_exercise_button);

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

    private void deleteWorkout() {
        Uri uri = withAppendedId(WorkoutContract.WorkoutEntry.CONTENT_URI, intentValue);
        int rowsDeleted = 0;
        rowsDeleted = getContentResolver().delete(uri, null, null);
        Intent intent = new Intent(ExercisesListActivity.this, MainActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private void getIntents() {
        intent = getIntent();
        uri = intent.getData();
        intentValue = intent.getLongExtra("id", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ExercisesListActivity.this);
                return true;
            case R.id.delete_menu_button:
                deleteWorkout();
                return true;
            case R.id.edit_button:
                Intent intent = new Intent(ExercisesListActivity.this, AddWorkout.class);
                Uri uri = withAppendedId(WorkoutContract.WorkoutEntry.CONTENT_URI, intentValue);
                intent.putExtra("id", intentValue);
                intent.setData(uri);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projections = {
                WorkoutContract.WorkoutEntry._ID,
                WorkoutContract.WorkoutEntry.EXERCISE_NAME,
                WorkoutContract.WorkoutEntry.WEIGHT,
                WorkoutContract.WorkoutEntry.REPS,
                WorkoutContract.WorkoutEntry.RPE,
                WorkoutContract.WorkoutEntry.WORKOUT_ID,
                WorkoutContract.WorkoutEntry.WORKOUT_TITLE
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
        if (data.moveToFirst()) {
            workoutName = data.getString(data.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.WORKOUT_TITLE));
            getSupportActionBar().setTitle(workoutName);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

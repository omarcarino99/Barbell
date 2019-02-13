package com.example.omar.barbell;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.content.ContentUris.withAppendedId;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private WorkoutListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView workoutList = findViewById(R.id.workout_list);
        adapter = new WorkoutListAdapter(this,null);
        workoutList.setAdapter(adapter);
        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ExercisesListActivity.class);
                Uri uri = withAppendedId(WorkoutContract.WorkoutEntry.JOIN_TABLE_URI, id);
                intent.putExtra("id", id);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.add_workout_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddWorkout.class);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projections ={
                WorkoutContract.WorkoutEntry._ID,
                WorkoutContract.WorkoutEntry.WORKOUT_TITLE,
                WorkoutContract.WorkoutEntry.WORKOUT_DATE
        };

        return new CursorLoader(
                this,
                WorkoutContract.WorkoutEntry.CONTENT_URI,
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


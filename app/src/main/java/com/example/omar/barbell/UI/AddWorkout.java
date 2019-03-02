package com.example.omar.barbell.UI;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.barbell.Database.WorkoutContract;
import com.example.omar.barbell.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentUris.withAppendedId;

public class AddWorkout extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri workoutUri;
    private EditText workoutTitleET;
    private EditText workoutDateET;
    private Button saveWorkoutButton;
    private long id;
    private static final int EXISTING_WORKOUT_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        workoutTitleET = (EditText) findViewById(R.id.workout_title_edit_text);
        saveWorkoutButton = findViewById(R.id.save_workout_button);

        Intent intent = getIntent();
        workoutUri = intent.getData();
        if (workoutUri != null) {
            getLoaderManager().initLoader(EXISTING_WORKOUT_LOADER, null, this);
        }

        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });

    }

    private void saveWorkout() {
        String workoutTitle = workoutTitleET.getText().toString();
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(calendar);

        if (workoutTitle.isEmpty()) {
            Toast.makeText(this, "Please fill in the missing areas", Toast.LENGTH_SHORT).show();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkoutContract.WorkoutEntry.WORKOUT_TITLE, workoutTitle);
        contentValues.put(WorkoutContract.WorkoutEntry.WORKOUT_DATE, formattedDate);
        Uri uri = getContentResolver().insert(WorkoutContract.WorkoutEntry.CONTENT_URI, contentValues);
        id = ContentUris.parseId(uri);
        Uri newUri = withAppendedId(WorkoutContract.WorkoutEntry.JOIN_TABLE_URI, id);

        Intent intent = new Intent(AddWorkout.this, ExercisesListActivity.class);
        intent.putExtra("id", id);
        intent.setData(newUri);
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
        id = data.getInt(data.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry._ID));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        workoutTitleET.setText("");
        workoutDateET.setText("");
    }
}


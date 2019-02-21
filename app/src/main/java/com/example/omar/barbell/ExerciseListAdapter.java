package com.example.omar.barbell;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseListAdapter extends CursorAdapter {
    private Context mContext;
    private Cursor mCursor;

    public ExerciseListAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.exercise_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mCursor = cursor;

        TextView idNumber = view.findViewById(R.id.id_number);
        TextView exercise = view.findViewById(R.id.exercise_name);
        TextView weight = view.findViewById(R.id.exercise_weight);
        TextView reps = view.findViewById(R.id.exercise_reps);
        TextView rpe = view.findViewById(R.id.exercise_rpe);

        String idIndex = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry._ID));
        String exerciseIndex = cursor.getString(cursor.getColumnIndexOrThrow("exercise_name"));
        String weightIndex = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
        String repsIndex = cursor.getString(cursor.getColumnIndexOrThrow("reps"));
        String rpeIndex = cursor.getString(cursor.getColumnIndexOrThrow("rpe"));

        idNumber.setText(idIndex);
        exercise.setText(exerciseIndex);
        weight.setText(weightIndex);
        reps.setText(repsIndex);
        rpe.setText(rpeIndex);
    }

    @Override
    public long getItemId(int position) {
        mCursor = getCursor();
        mCursor.moveToPosition(position);
        long value = mCursor.getLong(mCursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry._ID));
        return value;
    }
}


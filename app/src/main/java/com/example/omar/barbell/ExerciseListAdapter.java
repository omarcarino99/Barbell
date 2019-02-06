package com.example.omar.barbell;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ExerciseListAdapter extends CursorAdapter {
    private Context mContext;
    private Cursor mCursor;

    public ExerciseListAdapter(Context context, Cursor c) {
        super(context, c,0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.exercise_list_item,null,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mCursor = cursor;

        TextView exercise =view.findViewById(R.id.exercise_name);
        TextView weight = view.findViewById(R.id.exercise_weight);
        TextView reps = view.findViewById(R.id.exercise_reps);
        TextView rpe = view.findViewById(R.id.rpe);

        String exerciseIndex = cursor.getString(cursor.getColumnIndexOrThrow("exercise_name"));
        String weightIndex = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
        String repsIndex = cursor.getString(cursor.getColumnIndexOrThrow("reps"));
        String rpeIndex = cursor.getString(cursor.getColumnIndexOrThrow("rpe"));

        exercise.setText(exerciseIndex);
        weight.setText(weightIndex);
        reps.setText(repsIndex);
        rpe.setText(rpeIndex);
    }
}


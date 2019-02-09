package com.example.omar.barbell;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class WorkoutListAdapter extends CursorAdapter {

    Context mContext;
    Cursor mCursor;
    public WorkoutListAdapter(Context context, Cursor c) {
        super(context, c,0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.workout_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mCursor = cursor;

        TextView workoutTitleTextView = view.findViewById(R.id.workout_title);
        TextView workoutDateTextView = view.findViewById(R.id.workout_date);

        String workoutTitleIndex = cursor.getString(cursor.getColumnIndexOrThrow("workout_title"));
        String workoutDate = cursor.getString(cursor.getColumnIndexOrThrow("workout_date"));

        workoutDateTextView.setText(workoutDate);
        workoutTitleTextView.setText(workoutTitleIndex);
    }
}

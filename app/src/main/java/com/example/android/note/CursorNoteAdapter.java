package com.example.android.note;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import data.NoteContract.NoteEntry;

public class CursorNoteAdapter extends CursorAdapter {

    public CursorNoteAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.single_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView title_tv = view.findViewById(R.id.title_tv);
        TextView desc_tv = view.findViewById(R.id.desc_tv);
        final int id = cursor.getInt(cursor.getColumnIndex(NoteEntry._ID));
        final String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COL_TITLE));
        final String desc = cursor.getString(cursor.getColumnIndex(NoteEntry.COL_DESCRIPTION));
        title_tv.setText(title);
        desc_tv.setText(desc);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentNoteUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, id);
                Intent i = new Intent(context, EditorActivity.class);
                i.setData(currentNoteUri);
                context.startActivity(i);
            }
        });
    }
}

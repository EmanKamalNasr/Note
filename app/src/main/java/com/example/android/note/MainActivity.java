package com.example.android.note;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import data.NoteContract.NoteEntry;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private ImageView addNote_imgview;

    private static final int NOTE_LOADER = 0;
    private CursorNoteAdapter cursorNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNote_imgview = findViewById(R.id.add_note);
        addNote_imgview.setOnClickListener(this);

        ListView notes_listView = findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.emptyview);
        notes_listView.setEmptyView(emptyView);
        cursorNoteAdapter = new CursorNoteAdapter(this, null);
        notes_listView.setAdapter(cursorNoteAdapter);
        getLoaderManager().initLoader(NOTE_LOADER, null, this);

    }

    @Override
    public void onClick(View v) {
        if (v == addNote_imgview) {
            startActivity(new Intent(this, EditorActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
        cursorNoteAdapter.registerDataSetObserver(dataObserver);
    }

    @Override
    protected void onStop() {
        cursorNoteAdapter.unregisterDataSetObserver(dataObserver);
        super.onStop();
    }

    private final DataSetObserver dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidateOptionsMenu();
        }

        @Override
        public void onInvalidated() {
            invalidateOptionsMenu();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (cursorNoteAdapter.getCount() == 0) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_all);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAll();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void deleteAll() {
        int rowsDeleted = getContentResolver().delete(NoteEntry.CONTENT_URI, null, null);
        if (rowsDeleted != 0) {
            //contacts deleted successfully
            Toast.makeText(getApplicationContext(), R.string.deleted_successful, Toast.LENGTH_LONG).show();
        } else {
            //contacts deleted failed
            Toast.makeText(getApplicationContext(), R.string.deleted_failed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {NoteEntry._ID,
                NoteEntry.COL_TITLE, NoteEntry.COL_DESCRIPTION};

        return new CursorLoader(getApplicationContext(),
                NoteEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorNoteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorNoteAdapter.swapCursor(null);
    }
}

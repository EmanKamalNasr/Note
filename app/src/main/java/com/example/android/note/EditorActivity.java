package com.example.android.note;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import data.NoteContract.NoteEntry;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText title_et;
    private EditText description_et;
    private static final int EXISTING_CONTACT_LOADER = 0;

    private Uri currentNoteUri;
    private boolean noteHasChanged = false;
    //OnTouchListener object that listens for any user touches on a View
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            noteHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        title_et = findViewById(R.id.title_editor_et);
        description_et = findViewById(R.id.desc_editor_et);
        title_et.setOnTouchListener(mTouchListener);
        description_et.setOnTouchListener(mTouchListener);

        Intent i = getIntent();
        currentNoteUri = i.getData();
        if (currentNoteUri == null) {
            setTitle(getString(R.string.addNote));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editNote));
            getLoaderManager().initLoader(EXISTING_CONTACT_LOADER, null, EditorActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (!noteHasChanged) {
            //if nothing changed nothing is shown and back button do its job
            super.onBackPressed();
            return;
        }
        //if something changed show thw warning dialog
        showUnSavedChangesDialog();
    }

    private void showUnSavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //when the user clicked discard-->finish the activity
                finish();
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //when user clicked keep editing-->close the dialog
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_info, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentNoteUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                checkUnsavedBeforeGoingToParent();
                return true;
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_delete:
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
                deleteNote();
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

    private void deleteNote() {
        if (currentNoteUri != null) {
            int rowDeleted = getContentResolver().delete(currentNoteUri, null, null);
            if (rowDeleted != 0) {
                //contact deleted successfully
                Toast.makeText(getApplicationContext(), R.string.deleted_successful, Toast.LENGTH_LONG).show();
            } else {
                //contact deleted failed
                Toast.makeText(getApplicationContext(), R.string.deleted_failed, Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }

    private boolean checkUnsavedBeforeGoingToParent() {
        if (!noteHasChanged) {
            finish();
            return true;
        } else {
            showUnSavedChangesDialog();
            return true;
        }
    }

    private void saveNote() {
        String title = title_et.getText().toString().trim();
        String description = description_et.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            title_et.setError(getString(R.string.notitle));
        }
        if (TextUtils.isEmpty(description)) {
            description_et.setError(getString(R.string.nonote));
        }
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            return;
        } else {
            ContentValues values = new ContentValues();
            values.put(NoteEntry.COL_TITLE, title);
            values.put(NoteEntry.COL_DESCRIPTION, description);
            if (currentNoteUri == null) {
                //insert
                Uri newUri = getContentResolver().insert(NoteEntry.CONTENT_URI, values);
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_Note_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_Note_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                //update
                int rowsAffected = getContentResolver().update(currentNoteUri, values, null, null);
                if (rowsAffected == 0) {
                    //updated failed
                    Toast.makeText(this, getString(R.string.updated_failed), Toast.LENGTH_LONG).show();

                } else {
                    //updated successfully
                    Toast.makeText(this, getString(R.string.updated_successfully), Toast.LENGTH_LONG).show();
                }
            }
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {NoteEntry._ID,
                NoteEntry.COL_TITLE, NoteEntry.COL_DESCRIPTION};

        return new CursorLoader(getApplicationContext(),
                currentNoteUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {
            String title = data.getString(data.getColumnIndex(NoteEntry.COL_TITLE));
            String description = data.getString(data.getColumnIndex(NoteEntry.COL_DESCRIPTION));

            title_et.setText(title);
            description_et.setText(description);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        title_et.setText("");
        description_et.setText("");
    }

}

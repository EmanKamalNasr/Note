package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import data.NoteContract.NoteEntry;


public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="notes.db";
    private static final int DATABASE_VERSION=1;

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(NoteEntry.SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS'" + NoteEntry.TABLE_NAME+"'");
        onCreate(db);
    }
}

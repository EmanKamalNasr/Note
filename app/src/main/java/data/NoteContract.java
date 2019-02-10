package data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class NoteContract {

    public NoteContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.note";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTES = "notes";

    public static final class NoteEntry implements BaseColumns {
        //the uri link to access the notes in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);
        //the MIME TYPES for the data that provider class handle
        //ITEM TYPE
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;
        //DIR TYPE
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

        //TABLE NAME && COL_NAMES
        public static final String TABLE_NAME = "notes";
        public static final String _ID = BaseColumns._ID;
        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "description";

        //SQL QUERY
        public static final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + " ("
                + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NoteEntry.COL_TITLE + " TEXT NOT NULL, "
                + NoteEntry.COL_DESCRIPTION + " TEXT NOT NULL );";
    }

}

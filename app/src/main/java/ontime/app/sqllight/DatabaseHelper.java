package ontime.app.sqllight;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "CREDITCARD";

    // Table columns
    public static final String _ID = "_id";
    public static final String CARD_NAME = "cardname";
    public static final String CARD_NUMBER = "cardnumber";
    public static final String CARD_DATE = "carddate";

    // Database Information
    static final String DB_NAME = "MYRESTAURANT.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_NAME + " TEXT NOT NULL, " + CARD_NUMBER + " TEXT, " + CARD_DATE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

package ontime.app.sqllight;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String number,String date) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CARD_NAME, name);
        contentValue.put(DatabaseHelper.CARD_NUMBER, number);
        contentValue.put(DatabaseHelper.CARD_DATE, date);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CARD_NAME, DatabaseHelper.CARD_NUMBER, DatabaseHelper.CARD_DATE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public void deleteContact(int id){
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
    public ArrayList<CardDetails> listContacts(){
        String sql = "select * from " + DatabaseHelper.TABLE_NAME;
        ArrayList<CardDetails> storeContacts = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String card_no = cursor.getString(2);
                String card_date = cursor.getString(3);
                storeContacts.add(new CardDetails(id, name, card_no,card_date));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }

    public int update(long _id, String name, String number,String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CARD_NAME, name);
        contentValues.put(DatabaseHelper.CARD_NUMBER, number);
        contentValues.put(DatabaseHelper.CARD_DATE, date);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}

package com.carryapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carryapp.Classes.Notifications;
import com.carryapp.helper.Constants;

import java.util.ArrayList;

/**
 * Created by Siddhi on 12/23/2016.
 */
public class NotiTableHelper extends SQLiteOpenHelper {


    private static final String NOTI_TABLE = "notiTable";
    private static final String KEY_NOTI_ID = "id";
    private static final String KEY_NOTI_MESSAGE = "message";
    private static final String KEY_NOTI_POST_ID = "ptId";
    private static final String KEY_NOTI_SENDER_ID = "senderId";
    private static final String KEY_NOTI_STATUS = "status";
    private static final String KEY_NOTIFICATION = "notification";


    public NotiTableHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + NOTI_TABLE);

        // createTable(db);
        // onCreate(db);
    }

    public void addNoti(Notifications noti) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOTI_ID, noti.getNt_id());
        values.put(KEY_NOTI_MESSAGE, noti.getNt_message());
        values.put(KEY_NOTI_STATUS, noti.getNt_message());
        values.put(KEY_NOTI_POST_ID, noti.getPt_id());
        values.put(KEY_NOTI_SENDER_ID, noti.getSender_id());
        values.put(KEY_NOTIFICATION, noti.getNotification());

        db.insert(NOTI_TABLE, null, values);

        db.close();
    }

    //get all items

    public ArrayList<Notifications> getAllNoti() {
        ArrayList<Notifications> conList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + NOTI_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Notifications noti = new Notifications(cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("message")), cursor.getString(cursor.getColumnIndex("ptId")),
                        cursor.getString(cursor.getColumnIndex("senderId")),cursor.getString(cursor.getColumnIndex("status")));

                conList.add(noti);

            } while (cursor.moveToNext());
        }

        return conList;
    }


    public void deleteAllNoti()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ NOTI_TABLE);
        database.close();

    }
}

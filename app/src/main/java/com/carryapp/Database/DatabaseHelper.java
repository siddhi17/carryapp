package com.carryapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carryapp.helper.Constants;


/**
 * Created by Siddhi on 12/20/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOTI_TABLE = "notiTable";
    private static final String KEY_NOTI_ID = "id";
    private static final String KEY_NOTI_MESSAGE = "message";
    private static final String KEY_NOTI_POST_ID = "ptId";
    private static final String KEY_NOTI_SENDER_ID = "senderId";
    private static final String KEY_NOTI_STATUS = "status";

    private static final String KEY_NOTIFICATION = "notification";

    private static final String UNITS_TABLE = "unitTable";
    private static final String KEY_UNIT_ID = "id";
    private static final String KEY_UNIT_NAME = "unitName";

    // private static final String ITEMS_TABLE = "itemsTable";
    private static final String KEY_QUERY_ITEM_ID = "id";
    private static final String KEY_QUERY_NAME = "itemName";


    private Context context;

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        createTable(db);

    }

    //create database

    public void createDatabase() {
        context.deleteDatabase(Constants.DATABASE_NAME + ".db");
        SQLiteDatabase db = this.getReadableDatabase();
    }

    //create tables

    public void createTable(SQLiteDatabase db) {
     /*   String CREATE_ITEM_NAMES_TABLE = "CREATE TABLE " + ITEM_NAMES_TABLE + "("
                + KEY_ITEM_ID + " TEXT,"
                + KEY_ITEM_NAME + " TEXT "
                + ")";

        db.execSQL(CREATE_ITEM_NAMES_TABLE);*/
/*
        String CREATE_UNITS_TABLE = "CREATE TABLE " + UNITS_TABLE + "("
                + KEY_UNIT_ID + " TEXT,"
                + KEY_UNIT_NAME + " TEXT "
                + ")";

        db.execSQL(CREATE_UNITS_TABLE);*/


        String CREATE_NOTI_TABLE = "CREATE TABLE " + NOTI_TABLE + "("
                + KEY_NOTI_ID + " TEXT,"
                + KEY_NOTI_MESSAGE + " TEXT,"
                + KEY_NOTI_STATUS + " TEXT,"
                + KEY_NOTI_POST_ID + " TEXT,"
                + KEY_NOTI_SENDER_ID + " TEXT,"
                + KEY_NOTIFICATION + " TEXT "
                + ")";

        db.execSQL(CREATE_NOTI_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //  db.execSQL("DROP TABLE IF EXISTS " + ITEM_NAMES_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + UNITS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTI_TABLE);

        context.deleteDatabase(Constants.DATABASE_NAME + ".db");

        createTable(db);
        // Create tables again
        //onCreate(db);
    }

}

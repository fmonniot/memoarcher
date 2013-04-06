package eu.monniot.memoArcher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * The database that the provider uses as its underlying data store
     */
    private static final String DATABASE_NAME = "memo_archer.db";

    /**
     * The database version (must be incremented if the database schema change)
     */
    private static final int DATABASE_VERSION = 1;
    
    /*
     * SQL Helpers
     */
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_BOWS =
        "CREATE TABLE " + BowEntry.TABLE_NAME + " (" +
        		BowEntry._ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
        		BowEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
        		BowEntry.COLUMN_NAME_MARK_UNIT + DOUBLE_TYPE + COMMA_SEP +
        		BowEntry.COLUMN_NAME_DISTANCE_UNIT + DOUBLE_TYPE +
        " )";
    

	public DatabaseHelper(Context context) {
        // calls the super constructor, requesting the default cursor factory.
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_BOWS);
	}

	/**
	 * Warning: You must not delete old database here, otherwise the user lost all his data and
	 * will stop using this application.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BowEntry.TABLE_NAME); onCreate(db);}

	/**
	 * Same as {@link DatabaseHelper#onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)}
	 */
	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BowEntry.TABLE_NAME); onCreate(db);}


}

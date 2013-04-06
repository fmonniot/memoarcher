package eu.monniot.memoArcher;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import eu.monniot.memoArcher.db.BowEntry;
import eu.monniot.memoArcher.db.DatabaseHelper;

public class BowManager {

	DatabaseHelper mDbHelper;
	SharedPreferences mPreferences;
	
	public BowManager(Context context) {
		mDbHelper = new DatabaseHelper(context);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public Bow createBow(String name, String markUnit, String distanceUnit) {

		if(TextUtils.isEmpty(name))
			throw new IllegalArgumentException("A bow cannot have an empty name.");
		
		// Set default values
		if(TextUtils.isEmpty(markUnit))
			markUnit = mPreferences.getString("pref_default_mark_unit", null);

		if(TextUtils.isEmpty(distanceUnit)) 
			distanceUnit = mPreferences.getString("pref_default_distance_unit", null);

		
		Bow bow = new Bow();
		bow.setName(name);
		
		bow.setMarkUnit(markUnit);
		bow.setDistanceUnit(distanceUnit);
		
		return bow;
		

//	    ContentValues values = new ContentValues();
//	    values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
//	    long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
//	        values);
//	    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
//	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//	        null, null, null);
//	    cursor.moveToFirst();
//	    Comment newComment = cursorToComment(cursor);
//	    cursor.close();
//	    return newComment;
	}
	
	public Bow save(Bow bow) {
		
		ContentValues values = new ContentValues();
		values.put(BowEntry.COLUMN_NAME_NAME, bow.getName());
		values.put(BowEntry.COLUMN_NAME_MARK_UNIT, bow.getMarkUnit());
		values.put(BowEntry.COLUMN_NAME_DISTANCE_UNIT, bow.getDistanceUnit());
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long bowId = db.insert(BowEntry.TABLE_NAME, BowEntry.COLUMN_NAME_NAME, values);
		
		if( bowId > 0) {
			bow.setId(bowId);
			return bow;
		}
		throw new SQLException("Failed to insert row into " + BowEntry.TABLE_NAME);
		
	}
	public boolean delete(Bow bow) {return false;}
	
}

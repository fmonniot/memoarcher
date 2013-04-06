package eu.monniot.memoArcher;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import eu.monniot.memoArcher.db.BowEntry;
import eu.monniot.memoArcher.db.DatabaseHelper;

public class BowManager {

	private static final String[] ALL_BOW_COLUMNS = new String[] {
		BowEntry._ID,
		BowEntry.COLUMN_NAME_NAME,
		BowEntry.COLUMN_NAME_MARK_UNIT,
		BowEntry.COLUMN_NAME_DISTANCE_UNIT
	};
	
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
	}
	
	public Bow findOneById(long id) {
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		Cursor c = db.query(
                BowEntry.TABLE_NAME,                // table name
                ALL_BOW_COLUMNS,                    // columns
                "_ID = ?",                          // selection (WHERE clause)
                new String[]{ String.valueOf(id) }, // selectionArgs
                null,                               // groupBy
                null,                               // having
                null                                // orderBy
        );
		
		if(c.getCount() == 0)
			return null;
		
		c.moveToFirst();
		Bow bow = cursorToBow(c);
		c.close(); db.close();
		return bow;
	}
	
	public Bow save(Bow bow) {
		
		ContentValues values = new ContentValues();
		values.put(BowEntry.COLUMN_NAME_NAME, bow.getName());
		values.put(BowEntry.COLUMN_NAME_MARK_UNIT, bow.getMarkUnit());
		values.put(BowEntry.COLUMN_NAME_DISTANCE_UNIT, bow.getDistanceUnit());
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long bowId = db.insert(BowEntry.TABLE_NAME, BowEntry.COLUMN_NAME_NAME, values);
		db.close();
		
		if( bowId > 0) {
			bow.setId(bowId);
			return bow;
		}
		throw new SQLException("Failed to insert row into " + BowEntry.TABLE_NAME);
		
	}
	
	public boolean delete(Bow bow) {
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int n = db.delete(	BowEntry.TABLE_NAME,"_ID = ?",
					new String[]{ String.valueOf(bow.getId()) });
		db.close();
		return n>0;
	}
	
	private Bow cursorToBow(Cursor c) {
		Bow bow = new Bow();
		bow.setId(c.getLong(0));
		bow.setName(c.getString(1));
		bow.setMarkUnit(c.getString(2));
		bow.setDistanceUnit(c.getString(3));
		
		return bow;
	}
}

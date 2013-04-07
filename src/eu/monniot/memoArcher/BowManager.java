package eu.monniot.memoArcher;


import java.util.ArrayList;
import java.util.List;

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
	LandmarkManager mLandmarkManager;
	
	public BowManager(Context context) {
		mDbHelper = new DatabaseHelper(context);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mLandmarkManager = new LandmarkManager(context);
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
	
	/**
	 * Return the position of the bow of all the bow. Useful for selected a default entry in a list.
	 * 
	 * @param bow
	 * @return 
	 */
	public int findPositionOf(Bow bow) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
	    Cursor c = queryAllBow(db);

    	if(c.getCount() == 0 || !c.moveToFirst()) {
    		throw new IndexOutOfBoundsException("The database does not contains any entries.");
    	} else {
    		int position = 0;
    		do {
    			if(bow.getId() == c.getLong(0)) { break; }
    			position++;
    		} while ( c.moveToNext() );
    		return position;
    	}
	}
	
	public List<Bow> getAllBow() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
	    Cursor c = queryAllBow(db);
	    
	    	if(c.getCount() == 0 || !c.moveToFirst()) {
	    		return  null;
	    	} else {
	    		List<Bow> list = new ArrayList<Bow>();
	    		do {
	    			list.add(cursorToBow(c));
	    		} while ( c.moveToNext() );
	    		return list;
	    	}
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
			mLandmarkManager.save(bow.getLandmarks(), bowId);
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

	private Cursor queryAllBow(SQLiteDatabase db) {
		return db.query(
                BowEntry.TABLE_NAME,    // table name
                ALL_BOW_COLUMNS,        // columns
                null,                   // selection (WHERE clause)
                null,                   // selectionArgs
                null,                   // groupBy
                null,                   // having
                null                    // orderBy
              );
	}
	
	private Bow cursorToBow(Cursor c) {
		Bow bow = new Bow();
		bow.setId(c.getLong(0));
		bow.setName(c.getString(1));
		bow.setMarkUnit(c.getString(2));
		bow.setDistanceUnit(c.getString(3));
		bow.addLandmarks(mLandmarkManager.getAllByBow(bow));
		return bow;
	}

}

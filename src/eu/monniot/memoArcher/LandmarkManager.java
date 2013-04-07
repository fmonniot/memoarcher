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
import eu.monniot.memoArcher.db.BowEntry;
import eu.monniot.memoArcher.db.DatabaseHelper;
import eu.monniot.memoArcher.db.LandmarkEntry;

/*
 * TODO Need to be refactored with BowManager 
 */
public class LandmarkManager {

	private static final String[] ALL_LANDMARK_COLUMNS = new String[] {
		LandmarkEntry._ID,
		LandmarkEntry.COLUMN_NAME_BOW_ID,
		LandmarkEntry.COLUMN_NAME_MARK,
		LandmarkEntry.COLUMN_NAME_DISTANCE
	};

	DatabaseHelper mDbHelper;
	SharedPreferences mPreferences;

	public LandmarkManager(Context context) {
		mDbHelper = new DatabaseHelper(context);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public Landmark createLandmark(double mark, double distance) {
		
		Landmark lm = new Landmark();
		lm.setMark(mark);
		lm.setDistance(distance);
		
		return lm;
	}
	
	/**
	 * Return a Landmark by its id
	 * 
	 * @param id
	 * @return
	 */
	public Landmark findOneById(long id) {
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		Cursor c = db.query(
				LandmarkEntry.TABLE_NAME,           // table name
                ALL_LANDMARK_COLUMNS,               // columns
                "_ID = ?",                          // selection (WHERE clause)
                new String[]{ String.valueOf(id) }, // selectionArgs
                null,                               // groupBy
                null,                               // having
                null                                // orderBy
        );
		
		if(c.getCount() == 0)
			return null;
		
		c.moveToFirst();
		Landmark lm = fromCursor(c);
		c.close(); db.close();
		return lm;
	}
	
	public List<Landmark> getAllLandmark() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
	    Cursor c = queryAll(db);
	    
	    if(c.getCount() == 0 || !c.moveToFirst()) {
	    	return  null;
	    } else {
	    	List<Landmark> list = new ArrayList<Landmark>();
	    	do {
	    		list.add(fromCursor(c));
	    	} while ( c.moveToNext() );
	    	return list;
	    }
	}
	
	public List<Landmark> getAllByBow(Bow bow) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
	    Cursor c = queryAllBy(db, "bow_id = ?", new String[]{String.valueOf(bow.getId())});
	    	
	    if(c.getCount() == 0 || !c.moveToFirst()) {
	    	return  null;
	    } else {
	    	List<Landmark> list = new ArrayList<Landmark>();
	    	do {
	    		list.add(fromCursor(c));
	    	} while ( c.moveToNext() );
	    	return list;
	    }
	}
	
	/**
	 * TODO if none id INSERT otherwise UPDATE
	 * @param lm
	 * @return
	 */
	public Landmark save(Landmark lm) {
		
		ContentValues values = new ContentValues();
		values.put(LandmarkEntry.COLUMN_NAME_MARK, lm.getMark());
		values.put(LandmarkEntry.COLUMN_NAME_DISTANCE, lm.getDistance());
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long lmId = db.insert(BowEntry.TABLE_NAME, BowEntry.COLUMN_NAME_NAME, values);
		db.close();
		
		if( lmId > 0) {
			lm.setId(lmId);
			return lm;
		}
		throw new SQLException("Failed to insert row into " + BowEntry.TABLE_NAME);
		
	}
	
	public boolean save(List<Landmark> list, long defaultBowId) {
		
		ContentValues values = null;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long newLandmarkId = 0;
		boolean result = false;
		
		try{
			db.beginTransaction();
			for(Landmark lm : list) {
				values = new ContentValues();
				values.put(LandmarkEntry.COLUMN_NAME_MARK, lm.getMark());
				values.put(LandmarkEntry.COLUMN_NAME_DISTANCE, lm.getDistance());
				if(lm.getBowId() > 0) {
					values.put(LandmarkEntry.COLUMN_NAME_BOW_ID, lm.getBowId());
				} else {
					values.put(LandmarkEntry.COLUMN_NAME_BOW_ID, defaultBowId);
				}
				newLandmarkId = db.insert(LandmarkEntry.TABLE_NAME, null, values);
				if(newLandmarkId > 0) {
					result = true;
				} else {
					result = false;
					break;
				}
			}
			
		} catch(SQLException e) {
		} finally {
			db.endTransaction();
		}
		return result;
	}
	
	public boolean delete(Landmark lm) {
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int n = db.delete(	LandmarkEntry.TABLE_NAME,"_ID = ?",
					new String[]{ String.valueOf(lm.getId()) });
		db.close();
		return n>0;
	}

	private Cursor queryAll(SQLiteDatabase db) {
		return db.query(
                LandmarkEntry.TABLE_NAME,    // table name
                ALL_LANDMARK_COLUMNS,        // columns
                null,                   // selection (WHERE clause)
                null,                   // selectionArgs
                null,                   // groupBy
                null,                   // having
                null                    // orderBy
              );
	}

	private Cursor queryAllBy(SQLiteDatabase db, String where, String[] args) {
		return db.query(
                LandmarkEntry.TABLE_NAME,    // table name
                ALL_LANDMARK_COLUMNS,        // columns
                where,                   // selection (WHERE clause)
                args,                   // selectionArgs
                null,                   // groupBy
                null,                   // having
                null                    // orderBy
              );
	}
	
	private Landmark fromCursor(Cursor c) {
		Landmark lm = new Landmark();
		lm.setId(c.getLong(0));
		lm.setMark(c.getLong(2));
		lm.setDistance(c.getLong(3));
		
		return lm;
	}

}

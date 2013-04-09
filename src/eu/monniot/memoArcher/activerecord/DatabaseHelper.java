package eu.monniot.memoArcher.activerecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	protected final static String	TAG = DatabaseHelper.class.toString();
	private final static String		DB_MIGRATIONS_PATH = "migrations";
	private final static String		DB_NAME = "memoArcher.sqlite";
	private final static int 		DB_VERSION = 1;

	
	
	public DatabaseHelper(Context context) {
		super(context,DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Get all tables and create them
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (!execMigrations(db, oldVersion, newVersion)) {
			Log.i(TAG,"No migrations found. Calling onCreate.");
			onCreate(db);
		}
		
	}
	
	private boolean execMigrations(SQLiteDatabase db, int oldVersion, int newVersion) {
		/* TODO Get the correct SQL file and execute it
		 * Note: all migrations should be stored in assets/DB_MIGRATIONS_PATH/<db_version>.sql
		 */
		return false;
	}

}
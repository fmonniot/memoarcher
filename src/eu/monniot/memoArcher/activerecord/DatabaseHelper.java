package eu.monniot.memoArcher.activerecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.monniot.memoArcher.activerecord.annotation.Column;
import eu.monniot.memoArcher.activerecord.annotation.Table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private final static String		TAG = DatabaseHelper.class.toString();
	private final static String		DB_MIGRATIONS_PATH = "migrations";
	private final static String		DB_NAME = "memoArcher.sqlite";
	private final static int 		DB_VERSION = 6;

	private static Context sContext = null;


	// Pattern Singleton 	
	public DatabaseHelper(Context context) {
		super(context,DB_NAME, null, DB_VERSION);
		sContext = context;
	}

	// Wrappers for SQLiteDatabase
	
	public void execSql(String sql) {
		getWritableDatabase().execSQL(sql);
	}
	
	public void execSql(String sql, Object[] bindArgs) {
		getWritableDatabase().execSQL(sql, bindArgs);
	}
	
	// Execute query based on activerecord.query classes

	public <T extends Model> List<T> query(Class<? extends Model> table, String sql, String[] args) {
		Cursor cursor = getWritableDatabase().rawQuery(sql, args);
		List<T> entities = extractFromCursor(table, cursor);
		cursor.close();
		
		return entities;
	}

	public <T extends Model> T querySingle(Class<? extends Model> table, String sql, String[] args) {
		List<T> entities = query(table, sql, args);

		if (entities.size() > 0)
			return entities.get(0);

		return null;
	}
	
	public Long insert(Class<? extends Model> table, List<String> values) {
		String tablename = table.getAnnotation(Table.class).name();
		ContentValues cv = new ContentValues();
		List<Field> fields = ActiveRecord.getFieldFor(table);
		int valuePosition = 0;
		
		if(fields.size() != values.size())
			throw new RuntimeException("number of INSERT values different of columns number");
		
		for(Field field : fields) {
			final String columnName = field.getAnnotation(Column.class).name();
			Class<?> columnType = field.getType();
			Object value = values.get(valuePosition);
			
			if(value == null) {
				cv.putNull(columnName);
			} else if (columnType.equals(Byte.class) || columnType.equals(byte.class)) {
				cv.put(columnName, (Byte) value);
			} else if (columnType.equals(Short.class) || columnType.equals(short.class)) {
				cv.put(columnName, (Short) value);
			} else if (columnType.equals(Integer.class) || columnType.equals(int.class)) {
				cv.put(columnName, (Integer) value);
			} else if (columnType.equals(Long.class) || columnType.equals(long.class)) {
				cv.put(columnName, (Long) value);
			} else if (columnType.equals(Float.class) || columnType.equals(float.class)) {
				cv.put(columnName, (Float) value);
			} else if (columnType.equals(Double.class) || columnType.equals(double.class)) {
				cv.put(columnName, (Double) value);
			} else if (columnType.equals(Boolean.class) || columnType.equals(boolean.class)) {
				cv.put(columnName, (Boolean) value);
			} else if (columnType.equals(Character.class) || columnType.equals(char.class)) {
				cv.put(columnName, value.toString());
			} else if (columnType.equals(String.class)) {
				cv.put(columnName, value.toString());
			} else if (columnType.equals(Byte[].class) || columnType.equals(byte[].class)) {
				cv.put(columnName, (byte[]) value);
			} else if ( columnType.isAssignableFrom(Model.class) ) { // Is a subclass of Model (relation)
				cv.put(columnName, ((Model)value).getId() );
			}
			
			
			valuePosition++;
		}
		
		
		SQLiteDatabase db =  getWritableDatabase();
		
		long id = db.insert(tablename, null, cv);
		
		db.close();
		return Long.valueOf(id);
		
	}
	

	// Database utilities
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Assume that the first migration number is 1
		if (!execMigrations(db, 0, DB_VERSION)) {
			throw new RuntimeException("No migrations found for DatabaseHelper.onCreate");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (!execMigrations(db, oldVersion, newVersion)) {
			Log.i(TAG,"No migrations found. Calling onCreate.");
			onCreate(db);
		}
		
	}

	/** Get the correct SQL file and execute it
	 *  Note: all migrations should be stored in assets/DB_MIGRATIONS_PATH/<db_version>.sql
	 */
	private boolean execMigrations(SQLiteDatabase db, int oldVersion, int newVersion) {

		boolean migrationOk = false;

		try {
			final List<String> migrationFiles = Arrays.asList(sContext.getAssets().list(DB_MIGRATIONS_PATH));
			Collections.sort(migrationFiles, String.CASE_INSENSITIVE_ORDER);

			db.beginTransaction();
			try {
				for(String file : migrationFiles) {
					try {

						final int version = Integer.valueOf(file.replace(".sql", ""));
						if(version > oldVersion && version <= newVersion) {
							executeSqlFile(db, file);
							migrationOk = true;
						}

					}catch(NumberFormatException e) {
						Log.w(TAG, "Skipping invalidly named file: " + file, e);
					}
				}
				db.setTransactionSuccessful();

			} finally {
				db.endTransaction();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return migrationOk;
	}

	private void executeSqlFile(SQLiteDatabase db, String file) {
		try {
			final InputStream in = sContext.getAssets().open(DB_MIGRATIONS_PATH + "/" + file);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;

			while( (line = reader.readLine()) != null ) {
				db.execSQL(line);
			}
			Log.i(TAG, "Correctly execute " + file);
		} catch (IOException e) {
			Log.e(TAG, "Failed to execute " + file, e);
		}
	}

	private static <T extends Model> List<T> extractFromCursor(Class<? extends Model> table,
			Cursor cursor) {
		final List<T> entities = new ArrayList<T>();

		try {
			Constructor<?> entityConstructor = table.getConstructor();
		
			if (cursor.moveToFirst()) {
				do {
					@SuppressWarnings("unchecked")
					T entity = (T) entityConstructor.newInstance();
					((Model) entity).loadFromCursor(cursor);
					entities.add(entity);
				} while (cursor.moveToNext());
			}
		
		} catch (Exception e) {
			Log.e(TAG, "Failed to extract entity from cursor.", e);
		}
		
		return entities;
	}
}
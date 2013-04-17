package eu.monniot.memoArcher.activerecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import eu.monniot.memoArcher.activerecord.annotation.Column;

import android.content.Context;
import android.util.Log;

public class ActiveRecord {
	
	// Properties
	private static boolean sInitialized = false;
	private static Context sContext = null;
	private static DatabaseHelper sDatabaseHelper = null;
	
	private static final String TAG = "ActiveRecord";
	
	// Public methods
	
	public static void initialize(Context context) {
		if(ActiveRecord.isInitialized()) {
			Log.v(TAG, "ActiveRecord already initialized.");
			return;
		}
			
		sContext = context;
		sDatabaseHelper = new DatabaseHelper(context);
		
		
		sInitialized = true;
		Log.v(TAG, "ActiveRecord correctly initialized");
	}
	
	public static DatabaseHelper databaseHelper() {
		return sDatabaseHelper;
	}
	
	public static Context getContext() {
		return sContext;
	}
	
	public static boolean isInitialized() {
		return sInitialized;
	}
	
	public static void registerTable(Class<? extends Model> table) {
		//TODO
	}
	
	public static List<Field> getFieldFor(Class<? extends Model> table) {
		boolean isIdPresent = false;
		List<Field> list = new ArrayList<Field>();

		for(Field field : table.getDeclaredFields()) {
			if(field.isAnnotationPresent(Column.class)) {
				list.add(field);
				if(field.getName() == "mId")
					isIdPresent = true;
			}
		}
		if(!isIdPresent) {
			try {
				list.add(table.getSuperclass().getDeclaredField("mId"));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}

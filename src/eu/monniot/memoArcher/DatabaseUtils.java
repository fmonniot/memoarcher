package eu.monniot.memoArcher;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

import android.database.Cursor;

public class DatabaseUtils {

	public static Cursor fetchBow() {
		Log.d("DatabaseUtils","SQL QUERY: " + new Select().from(Bow.class).toSql());
		return Cache.openDatabase().rawQuery(new Select().from(Bow.class).toSql(), Constant.EMPTY_STRING_ARRAY);
	}
}

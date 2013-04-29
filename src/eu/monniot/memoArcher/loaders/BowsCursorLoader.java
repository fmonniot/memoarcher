package eu.monniot.memoArcher.loaders;

import eu.monniot.memoArcher.DatabaseUtils;
import android.content.Context;
import android.database.Cursor;

public class BowsCursorLoader extends SimpleCursorLoader {

	public BowsCursorLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return DatabaseUtils.fetchBow();
	}

}

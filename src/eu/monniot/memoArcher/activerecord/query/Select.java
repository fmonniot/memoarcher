package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;
import android.text.TextUtils;

public class Select implements Sqlable {
	
	String[] mColumns;

	/**
	 * Initiliaze a SELECT statement on all columns
	 */
	public Select() {}
	
	/**
	 * Initialize a SELECT statement on the specified columns
	 * @param columns
	 */
	public Select(String... columns) {
		mColumns = columns;
	}
	
	/**
	 * Specified the table
	 * @param table
	 * @return
	 */
	public From from(Class<? extends Model> table) {
		return new From(table, this);
	}
	
	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		if (mColumns != null && mColumns.length > 0) {
			sql.append(TextUtils.join(", ", mColumns) + " ");
		} else {
			sql.append("* ");
		}
		
		return sql.toString();
	}

}

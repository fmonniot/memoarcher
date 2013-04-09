package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;
import android.text.TextUtils;

public class Select implements Sqlable {
	
	String[] mColumns;

	public Select() {}
	
	public Select(String... columns) {
		mColumns = columns;
	}
	
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

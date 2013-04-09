package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;

public class Update implements Sqlable {
	private Class<? extends Model> mTable;
	
	public Update(Class<? extends Model> table) {
		mTable = table;
	}
	
	public Set set(String set) {
		return new Set(this, set);
	}
	
	public Set set(String set, String... args) {
		return new Set(this, set, args);
	}
	
	Class<? extends Model> getTable() {
		return mTable;
	}
	
	@Override
	public String toSql() {
		//TODO Get table name of mTable here
		return "UPDATE " + " TODO HERE " + " ";
	}

}

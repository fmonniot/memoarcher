package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;
import eu.monniot.memoArcher.activerecord.annotation.Table;

public class Update implements Sqlable {
	private Class<? extends Model> mTable;
	
	/**
	 * Initialized an UPDATE statement
	 * @param table
	 */
	public Update(Class<? extends Model> table) {
		mTable = table;
	}
	
	/**
	 * Specified the attributes to updates
	 * Example: set("id = 1, age = 23")
	 * @param set
	 * @return
	 */
	public Set set(String set) {
		return new Set(this, set);
	}
	
	/**
	 * Specified the attributes to update using a list of arguments
	 * Example: set("id = ?, age = ?", "1", "23")
	 * @param set
	 * @param args
	 * @return
	 */
	public Set set(String set, String... args) {
		return new Set(this, set, args);
	}
	
	@Override
	public String toSql() {

		// TODO Move this part of code to its own class (maybe a ReflectionUtil class ?)
		if(!mTable.isAnnotationPresent(Table.class)) {
			throw new RuntimeException(mTable.toString()+" must implement the Table annotation");
		}
		
		return "UPDATE " + mTable.getAnnotation(Table.class).name() + " ";
	}

}

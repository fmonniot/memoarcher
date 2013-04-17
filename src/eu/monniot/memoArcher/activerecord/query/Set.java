package eu.monniot.memoArcher.activerecord.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.monniot.memoArcher.activerecord.ActiveRecord;

public class Set implements Sqlable {
	
	private Update mUpdate = null;
	
	private String mSet = null;
	private String mWhere = null;

	private List<Object> mSetArguments = null;
	private List<Object> mWhereArguments = null;

	/**
	 * Specified the attributes to update
	 * Example: Set(update, "id = 1, age = 23")
	 * @param update
	 * @param set
	 */
	public Set(Update update, String set) {
		mUpdate = update;
		mSet = set;

		mSetArguments = new ArrayList<Object>();
		mWhereArguments = new ArrayList<Object>();
	}
	
	/**
	 * Specified the attributes to update using a list of arguments
	 * Example: Set(update, "id = ?, age = ?", "1", "23")
	 * @param update
	 * @param set
	 * @param args
	 */
	public Set(Update update, String set, String[] args) {
		this(update, set);
		
		mSetArguments.addAll(Arrays.asList(args));
	}
	
	/**
	 * Add a WHERE statement
	 * Example: where("id = 1")
	 * @param where
	 * @return
	 */
	public Set where(String where) {
		mWhere = where;
		mWhereArguments.clear();
		
		return this;
	}
	
	/**
	 * Add a WHERE statement with arguments
	 * Example: where("age > ? AND age < ?", 18, "24")
	 * 
	 * @param where
	 * @param args
	 * @return
	 */
	public Set where(String where, Object... args) {
		where(where);
		mWhereArguments.addAll(Arrays.asList(args));
		
		return this;
	}
	
	/**
	 * Execute the query builded so far
	 * @return
	 */
	public void execute() {
		ActiveRecord.databaseHelper().execSql(toSql(), getArguments());
	}

	/**
	 * Return a list of all the WHERE arguments of this instance
	 * @return
	 */
	public String[] getArguments() {
		final int setSize = mSetArguments.size();
		final int whereSize = mWhereArguments.size();
		final String[] args = new String[setSize + whereSize];
		
		for (int i = 0; i < setSize; i++) {
			args[i] = mSetArguments.get(i).toString();
		}
		
		for (int i = 0; i < whereSize; i++) {
			args[i] = mWhereArguments.get(i).toString();
		}
		
		return args;
	}

	@Override
	public String toSql() {

		StringBuilder sql = new StringBuilder();
		sql.append(mUpdate.toSql());
		sql.append("SET " + mSet + " ");
		
		if(mWhere != null)
			sql.append("WHERE " + mWhere + " ");
		
		return sql.toString();
	}
}

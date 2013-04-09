package eu.monniot.memoArcher.activerecord.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Set implements Sqlable {
	
	private Sqlable mQueryBase = null;
	
	private String mSet = null;
	private String mWhere = null;

	private List<Object> mSetArguments = null;
	private List<Object> mWhereArguments = null;

	public Set(Sqlable update, String set) {
		mQueryBase = update;
		mSet = set;

		mSetArguments = new ArrayList<Object>();
		mWhereArguments = new ArrayList<Object>();
	}
	
	public Set(Sqlable update, String set, String[] args) {
		this(update, set);
		
		mSetArguments.addAll(Arrays.asList(args));
	}
	
	public Set set(String set) {
		return new Set(this, set);
	}
	
	public Set set(String set, String... args) {
		return new Set(this, set, args);
	}
	
	public Set where(String where) {
		mWhere = where;
		mWhereArguments.clear();
		
		return this;
	}
	
	public Set where(String where, Object... args) {
		where(where);
		mWhereArguments.addAll(Arrays.asList(args));
		
		return this;
	}

	@Override
	public String toSql() {

		StringBuilder sql = new StringBuilder();
		sql.append(mQueryBase.toSql());
		sql.append("SET " + mSet + " ");
		
		if(mWhere != null)
			sql.append("WHERE " + mWhere + " ");
		
		return sql.toString();
	}
	
	public void execute() {
		//TODO execute sql
	}

}

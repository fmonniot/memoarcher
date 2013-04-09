package eu.monniot.memoArcher.activerecord.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.monniot.memoArcher.activerecord.Model;

public class From implements Sqlable {

	private Class<? extends Model> mTable;
	private Sqlable mQueryBase;
	private ArrayList<Object> mArguments;
	
	private String mWhere = null;
	private String mOrderBy = null;
	private Object mLimit;

	public From(Class<? extends Model> table, Sqlable queryBase) {
		mTable = table;
		mQueryBase = queryBase;
		
		mArguments = new ArrayList<Object>();
	}
	
	public From where(String where) {
		mWhere = where;
		mArguments.clear();
		return this;
	}
	
	public From where(String where, Object... arguments) {
		where(where);
		mArguments.addAll(Arrays.asList(arguments));
		
		return this;
	}
	
	public From orderBy(String orderBy) {
		mOrderBy = orderBy;
		return this;
	}
	
	public From limit(String limit) {
		mLimit = limit;
		return this;
	}
	
	public From limit(int limit) {
		return limit(String.valueOf(limit));
	}
	
	public <T extends Model> List<T> execute() {
		// execute the query
		return null;
	}
	
	//TODO otherwise (type safety warning)
	public <T extends Model> T executeOnce() {
		limit(1);
		return (T) execute().get(0);
	}

	@Override
	public String toSql() {
		
		StringBuilder sql = new StringBuilder();

		sql.append(mQueryBase.toSql());

		//TODO Get table name of mTable here
		sql.append("FROM " + " TODO HERE " + " ");
		
		
//		for (Join join : mJoins) {
//		sql += join.toSql();
//		}
		
		if (mWhere != null) {
			sql.append("WHERE " + mWhere + " ");
		}
		
		if (mOrderBy  != null) {
			sql.append("ORDER BY " + mOrderBy + " ");
		}
		
		if (mLimit != null) {
			sql.append("LIMIT " + mLimit + " ");
		}
		
		return sql.toString().trim();
	}

}

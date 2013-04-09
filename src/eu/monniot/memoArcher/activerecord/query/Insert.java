package eu.monniot.memoArcher.activerecord.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.database.SQLException;

import eu.monniot.memoArcher.activerecord.Model;
import eu.monniot.memoArcher.activerecord.annotation.Table;

public class Insert implements Sqlable {
	private Class<? extends Model> mTable;
	private List<String> mValues = null;

	/**
	 * Initialize a new INSERT statement
	 * @param table Must have {@link Table} annotation
	 */
	public Insert(Class<? extends Model> table) {
		mTable = table;
		mValues = new ArrayList<String>();
	}
	
	/**
	 * Add a value to this INSERT statement. The call order is significant 
	 * @param value
	 * @return
	 */
	public Insert add(String value) {
		mValues.add(value);
		return this; 
	}
	
	@Override
	public String toSql() {
		if(mValues.size() == 0) {
			throw new SQLException("INSERT statement must contains at least one value");
		} else {
			// TODO Move this part of code to its own class (maybe a ReflectionUtil class ?)
			if(!mTable.isAnnotationPresent(Table.class)) {
				throw new RuntimeException(mTable.toString()+" must implement the Table annotation");
			}
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO "+ mTable.getAnnotation(Table.class).name() +" VALUES ( ");

			ListIterator<String> iterator = mValues.listIterator();
		    while(iterator.hasNext()){
		        sql.append(iterator.next());
		        
		        if(iterator.hasNext()){
		            sql.append(", ");
		        }
		    }
			
			sql.append(" )");
			return sql.toString();
		}
	}

}

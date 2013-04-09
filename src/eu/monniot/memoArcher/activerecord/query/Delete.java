package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;

public class Delete implements Sqlable {

	public Delete() {}
	
	/**
	 * Initialize a new DELETE statement. Use this with {@link From} class for specified the query
	 * @param table
	 * @return
	 */
	public From from(Class<? extends Model> table) {
		return new From(table, this);
	}
	
	@Override
	public String toSql() {
		return "DELETE ";
	}

}

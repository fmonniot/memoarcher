package eu.monniot.memoArcher.activerecord.query;

import eu.monniot.memoArcher.activerecord.Model;

public class Delete implements Sqlable {

	public Delete() {}
	
	public From from(Class<? extends Model> table) {
		return new From(table, this);
	}
	
	@Override
	public String toSql() {
		return "DELETE ";
	}

}

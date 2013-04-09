package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Insert;
import eu.monniot.memoArcher.test.activerecord.MockModel;

public class InsertTest extends SqlableTestCase {
	private static final String INSERT_PREFIX = "INSERT INTO MockModel ";

//	INSERT INTO table_name VALUES (value1, value2, value3,...)
	
	public void testInsertWithValue() {
		assertSqlEquals(INSERT_PREFIX + "VALUES ( value1 )",
						insert()
							.add("value1"));
	}
	
	public void testInsertWithMultipleValue() {
		assertSqlEquals(INSERT_PREFIX + "VALUES ( value1, value2 )",
						insert()
						.add("value1")
						.add("value2"));
	}
	
	private Insert insert() {
		return new Insert(MockModel.class);
	}
}

// expected:<...SERT INTO MockModel [VALUES (]value1, value2)> but was:<...SERT INTO MockModel [( ]value1, value2)>


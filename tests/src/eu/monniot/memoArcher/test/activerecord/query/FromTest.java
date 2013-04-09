package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.From;
import eu.monniot.memoArcher.test.activerecord.MockModel;
import eu.monniot.memoArcher.test.activerecord.SqlableMock;

public class FromTest extends SqlableTestCase {
	private static final String FROM_PREFIX = "FROM MockModel";

	public void testFrom() {
		assertSqlEquals(FROM_PREFIX, 
				from());
	}
	
	public void testFromWhere() {
		assertSqlEquals(FROM_PREFIX + " WHERE id = 1", 
				from().where("id = 1"));
	}
	
	public void testFromWhereWithArgs() {
		From query = from().where("id = ?", 1);
		assertArrayEquals(query.getArguments(), "1");
		assertSqlEquals(FROM_PREFIX + " WHERE Id = ?",
							query);

		query = from().where("Id > ? AND Id < ?", 5, 10);
		assertArrayEquals(query.getArguments(), "5", "10");
		assertSqlEquals(FROM_PREFIX + " WHERE Id > ? AND Id < ?",
							query);
		
		query = from()
				.where("Id != ?", 10)
				.where("Id IN (?, ?, ?)", 5, 10, 15)
				.where("Id > ? AND Id < ?", 5, 10);
		assertArrayEquals(query.getArguments(), "5", "10");
		assertSqlEquals(FROM_PREFIX + " WHERE Id > ? AND Id < ?",
							query);
	}
	
	public void testFromOrderBy() {
		assertSqlEquals(FROM_PREFIX + " ORDER BY Id DESC",
				from().orderBy("Id DESC"));
	}
	
	
	public void testFromLimit() {
		assertSqlEquals(FROM_PREFIX + " LIMIT 10",
				from().limit(10));
		assertSqlEquals(FROM_PREFIX + " LIMIT 10",
				from().limit("10"));
	}
	
	
	public void pendingtestFromExecute() {
		
	}

	public void pendingtestFromExecuteSingle() {
		
	}


	private From from() {
		return new From(MockModel.class, new SqlableMock());
	}
}

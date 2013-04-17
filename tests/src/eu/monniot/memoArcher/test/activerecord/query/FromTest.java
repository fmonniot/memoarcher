package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.From;
import eu.monniot.memoArcher.test.mock.MockModel;
import eu.monniot.memoArcher.test.mock.SqlableMock;

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
		assertSqlEquals(FROM_PREFIX + " WHERE id = ?",
							query);

		query = from().where("id > ? AND id < ?", 5, 10);
		assertArrayEquals(query.getArguments(), "5", "10");
		assertSqlEquals(FROM_PREFIX + " WHERE id > ? AND id < ?",
							query);
		
		query = from()
				.where("id != ?", 10)
				.where("id IN (?, ?, ?)", 5, 10, 15)
				.where("id > ? AND id < ?", 5, 10);
		assertArrayEquals(query.getArguments(), "5", "10");
		assertSqlEquals(FROM_PREFIX + " WHERE id > ? AND id < ?",
							query);
	}
	
	public void testFromOrderBy() {
		assertSqlEquals(FROM_PREFIX + " ORDER BY id DESC",
				from().orderBy("id DESC"));
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

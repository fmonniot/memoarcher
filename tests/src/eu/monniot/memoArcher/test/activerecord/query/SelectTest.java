package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Select;
import eu.monniot.memoArcher.test.activerecord.MockModel;

public class SelectTest extends SqlableTestCase {
	private static final String SELECT_PREFIX = "SELECT ";

	public void testSelect() {
		assertSqlEquals(SELECT_PREFIX+"* ", select());
	}
	
	public void testSelectFrom() {
		assertSqlEquals(SELECT_PREFIX+"* FROM MockModel", 
				select().from(MockModel.class));
	}

	public void testSelectColumns() {
		assertSqlEquals(SELECT_PREFIX+"column1 ", 
				select("column1"));
	}
	
	public void testSelectMultipleColumns() {
		assertSqlEquals(SELECT_PREFIX+"column1, column2 ", 
				select("column1", "column2"));
	}
	
	public void testSelectMultipleColumnsFrom() {
		assertSqlEquals(SELECT_PREFIX+"column1, column2 FROM MockModel", 
				select("column1", "column2").from(MockModel.class));
	}

	private Select select() {
		return new Select();
	}
	
	private Select select(String... columns) {
		return new Select(columns);
	}
}

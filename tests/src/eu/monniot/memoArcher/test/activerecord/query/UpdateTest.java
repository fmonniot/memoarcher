package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Set;
import eu.monniot.memoArcher.activerecord.query.Update;
import eu.monniot.memoArcher.test.activerecord.MockModel;

public class UpdateTest extends SqlableTestCase {
	private static final String UPDATE_PREFIX = "UPDATE MockModel ";

	public void testUpdate() {
		assertSqlEquals(UPDATE_PREFIX, update());
	}

	public void testUpdateSet() {
		assertSqlEquals(UPDATE_PREFIX + "SET Id = 5 ",
						update().set("Id = 5"));
	}

	public void testUpdateWhereNoArguments() {
		assertSqlEquals(UPDATE_PREFIX + "SET Id = 5 WHERE Id = 1 ",
						update()
							.set("Id = 5")
							.where("Id = 1"));
	}

	public void testUpdateWhereWithArguments() {
		Set set = update()
					.set("Id = 5")
					.where("Id = ?", 1);
		assertArrayEquals(set.getArguments(), "1");
		assertSqlEquals(UPDATE_PREFIX + "SET Id = 5 WHERE Id = ? ",
		set);
		
		set = update()
				.set("Id = 5")
				.where("Id = ?", 1)
				.where("Id IN (?, ?, ?)", 5, 4, 3);
		assertArrayEquals(set.getArguments(), "5", "4", "3");
		assertSqlEquals(UPDATE_PREFIX + "SET Id = 5 WHERE Id IN (?, ?, ?) ",
		set);
	}
	
	public void pendingtestExecute() {
		
	}

	private Update update() {
		return new Update(MockModel.class);
	}
}

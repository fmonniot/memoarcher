package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Delete;
import eu.monniot.memoArcher.test.mock.MockModel;


public class DeleteTest extends SqlableTestCase{

	public void testDelete() {
		assertSqlEquals("DELETE ", new Delete());
	}

	public void testDeleteFrom() {
		assertSqlEquals("DELETE FROM MockModel", new Delete().from(MockModel.class));
	}
	
	public void testDeleteFromWhere() {
		assertSqlEquals("DELETE FROM MockModel WHERE id = 1", 
				new Delete().from(MockModel.class).where("id = 1") );
	}
}

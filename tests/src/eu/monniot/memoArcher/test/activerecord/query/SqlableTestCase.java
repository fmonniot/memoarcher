package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Sqlable;
import android.app.Application;
import android.test.ApplicationTestCase;

public class SqlableTestCase extends ApplicationTestCase<Application> {

	public SqlableTestCase(Class<Application> applicationClass) {
		super(applicationClass);	
	}

	public static void assertSqlEquals(String expected, Sqlable actual) {
		assertEquals(expected, actual.toSql());
	}
}

package eu.monniot.memoArcher.test.activerecord.query;

import eu.monniot.memoArcher.activerecord.query.Sqlable;
import android.app.Application;
import android.test.ApplicationTestCase;

public class SqlableTestCase extends ApplicationTestCase<Application> {

	public SqlableTestCase() {
		super(Application.class);	
	}

	public static void assertSqlEquals(String expected, Sqlable actual) {
		assertEquals(expected, actual.toSql());
	}

	public static <T> void assertArrayEquals(T[] actual, T... expected) {
		assertEquals(expected.length, actual.length);

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
}
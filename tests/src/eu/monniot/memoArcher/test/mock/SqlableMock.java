package eu.monniot.memoArcher.test.mock;

import eu.monniot.memoArcher.activerecord.query.Sqlable;

public class SqlableMock implements Sqlable {
	@Override
	public String toSql() {
		return "";
	}

}

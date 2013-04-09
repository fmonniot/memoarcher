package eu.monniot.memoArcher.test.activerecord;

import eu.monniot.memoArcher.activerecord.query.Sqlable;

public class SqlableMock implements Sqlable {
	@Override
	public String toSql() {
		return "";
	}

}

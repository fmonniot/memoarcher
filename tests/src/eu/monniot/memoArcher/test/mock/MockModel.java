package eu.monniot.memoArcher.test.mock;

import eu.monniot.memoArcher.activerecord.Model;
import eu.monniot.memoArcher.activerecord.annotation.Column;
import eu.monniot.memoArcher.activerecord.annotation.Table;

@Table(name = "MockModel")
public class MockModel extends Model {

	@Column(name = "value")
	public int value = 1;
}
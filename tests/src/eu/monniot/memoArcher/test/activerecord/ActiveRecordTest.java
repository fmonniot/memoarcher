package eu.monniot.memoArcher.test.activerecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import eu.monniot.memoArcher.activerecord.ActiveRecord;
import eu.monniot.memoArcher.activerecord.Model;
import eu.monniot.memoArcher.activerecord.annotation.Column;
import eu.monniot.memoArcher.test.mock.MockModel;
import android.test.AndroidTestCase;
import android.util.Log;

public class ActiveRecordTest extends AndroidTestCase {

	public void setUp() {
		ActiveRecord.initialize(getContext());
	}

	public void testInitialize() {
		assertNotNull(ActiveRecord.databaseHelper());
		assertNotNull(ActiveRecord.getContext());
		assertTrue(ActiveRecord.isInitialized());
	}
	
	public void testRegisterTable() {
		// TODO
	}

	public void testGettingFields() {
		List<Field> expectedList = new ArrayList<Field>();
		
		try {
			expectedList.add(MockModel.class.getSuperclass().getDeclaredField("mId"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail("Cannot fetch the mId field.");
		}
		Log.d("ActiveRecord.getFieldFor", ActiveRecord.getFieldFor(MockModel.class).toString());

		assertTrue(ActiveRecord.getFieldFor(Modl.class).containsAll(expectedList));
	}
	
	public void testGettingFieldsWithOverridenFields() {
		List<Field> expectedList = new ArrayList<Field>();
		
		try {
			expectedList.add(Modl.class.getSuperclass().getDeclaredField("mId"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail("Cannot fetch the mId field.");
		}
		Log.d("ActiveRecord.getFieldFor", ActiveRecord.getFieldFor(Modl.class).toString());

		assertTrue(ActiveRecord.getFieldFor(Modl.class).containsAll(expectedList));
	}

	
	private class Modl extends Model {
		
		@Column
		private Long mId;
	}
}

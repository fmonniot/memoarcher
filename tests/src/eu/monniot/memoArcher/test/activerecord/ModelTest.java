package eu.monniot.memoArcher.test.activerecord;

import java.lang.reflect.Field;

import android.test.InstrumentationTestCase;

import eu.monniot.memoArcher.activerecord.ActiveRecord;
import eu.monniot.memoArcher.activerecord.Model;
import eu.monniot.memoArcher.activerecord.annotation.Column;
import eu.monniot.memoArcher.activerecord.annotation.Table;
import eu.monniot.memoArcher.test.mock.MockModel;

/**
 * Prior to any tests, tout should ensure that the database is up to date (using migrations tools)
 * @author François
 *
 */
public class ModelTest extends InstrumentationTestCase{
	
	private MockModel mModel = null;
	
	public void setUp() {
		ActiveRecord.initialize(getInstrumentation().getContext());
		mModel = new MockModel();
	}
	
	public void testModelEquals() {
		Mod2 model2 = new Mod2();

		assertFalse(mModel.equals(model2));

		changeId(mModel, new Long(1));
		assertFalse(mModel.equals(model2));

		changeId(model2, new Long(1));
		assertFalse(mModel.equals(model2));

		changeTableName(model2, "MockModel");
		assertTrue(mModel.equals(model2));
	}
	
	public void testCreateModel() {
		mModel.save();
		
		assertTrue(mModel.getId().longValue() > 0);
	}
	
	public void testReadModel() {
		mModel.save();

		MockModel modl  = MockModel.findOne(MockModel.class, mModel.getId());
		assertTrue(mModel.equals(modl));
	}
	
	public void testUpdateModel() {
		mModel.value = 23;
		mModel.save();

		MockModel modl  = MockModel.findOne(MockModel.class, mModel.getId());
		assertEquals(23, modl.value);
	}
	
	public void testDeleteModel() {
		mModel.save();
		mModel.delete();
		

		MockModel modl  = MockModel.findOne(MockModel.class, mModel.getId());
		assertNull(modl);
	}
	
	private void changeId(Model model, Long id) {
		try { 
			Field field = model.getClass().getSuperclass().getDeclaredField("mId");
			field.setAccessible(true);
			field.set(model, id);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void changeTableName(Model model, String table) {
		try { 
			Field field = model.getClass().getSuperclass().getDeclaredField("mTableName");
			field.setAccessible(true);
			field.set(model, table);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Table(name="mod2")
	private class Mod2 extends Model {
		
		@Column
		private int value = 1;
	}
}
























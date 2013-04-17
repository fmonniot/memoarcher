package eu.monniot.memoArcher.activerecord;

import java.lang.reflect.Field;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import eu.monniot.memoArcher.activerecord.annotation.Column;
import eu.monniot.memoArcher.activerecord.annotation.Table;
import eu.monniot.memoArcher.activerecord.query.Delete;
import eu.monniot.memoArcher.activerecord.query.Select;

public abstract class Model {

	// instance fields
	
	private String mTableName = null;
	//private Map<Field, String> mColumnNames = new HashMap<Field, String>();
	
	@Column(name="id")
	private Long mId = null;
	
	// Constructors
	public Model() {
		defineTableName();
	}
	
	// Public instance methods
	
	public Long getId() {
		return mId;
	}
	
	public void delete() {
		delete(getClass(), getId());
	}
	
	public void save() {
		
		ContentValues cv = new ContentValues();
		List<Field> fields = ActiveRecord.getFieldFor(this.getClass());
		
		for(Field field : fields) {
			final String columnName = field.getAnnotation(Column.class).name();
			Class<?> columnType = field.getType();
			Object value;
			try {
				value = field.get(this);
			
				if(value == null) {
					cv.putNull(columnName);
				} else if (columnType.equals(Byte.class) || columnType.equals(byte.class)) {
					cv.put(columnName, (Byte) value);
				} else if (columnType.equals(Short.class) || columnType.equals(short.class)) {
					cv.put(columnName, (Short) value);
				} else if (columnType.equals(Integer.class) || columnType.equals(int.class)) {
					cv.put(columnName, (Integer) value);
				} else if (columnType.equals(Long.class) || columnType.equals(long.class)) {
					cv.put(columnName, (Long) value);
				} else if (columnType.equals(Float.class) || columnType.equals(float.class)) {
					cv.put(columnName, (Float) value);
				} else if (columnType.equals(Double.class) || columnType.equals(double.class)) {
					cv.put(columnName, (Double) value);
				} else if (columnType.equals(Boolean.class) || columnType.equals(boolean.class)) {
					cv.put(columnName, (Boolean) value);
				} else if (columnType.equals(Character.class) || columnType.equals(char.class)) {
					cv.put(columnName, value.toString());
				} else if (columnType.equals(String.class)) {
					cv.put(columnName, value.toString());
				} else if (columnType.equals(Byte[].class) || columnType.equals(byte[].class)) {
					cv.put(columnName, (byte[]) value);
				} else if (columnType.isAssignableFrom(Model.class) ) { // Is a subclass of Model (relation)
					cv.put(columnName, ((Model)value).getId() );
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		
		SQLiteDatabase db =  ActiveRecord.databaseHelper().getWritableDatabase();
		
		if(getId() == null) {
			mId = db.insert(getTableName(), null, cv);
		} else {
			db.update(getTableName(), cv, "id = " + getId(), null);
		}
		db.close();
	}
	
	@Override
	public boolean equals(Object o) {
		Model other = (Model) o;
		
		return     (mId != null)
				&& (mId.equals(other.getId()))
				&& (getTableName() == other.getTableName());
	}
	
	// Public static methods
	
	public static void delete(Class<? extends Model> table, long id) {
		new Delete().from(table).where("id = ?", id).execute();
	}
	
	public static <E extends Model> E findOne(Class<? extends Model> table, long id) {
		return new Select().from(table).where("id = ?", id).executeSingle();
	}

	public void loadFromCursor(Cursor cursor) {
		for(Field field : ActiveRecord.getFieldFor(getClass())) {
			Class<?> fieldType = field.getType();
			final int columnIndex = cursor.getColumnIndex(field.getAnnotation(Column.class).name());

			if (columnIndex < 0) { continue; }
			field.setAccessible(true);

			Object value = null;

			if (cursor.isNull(columnIndex)) {
				field = null;
			} else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
				value = cursor.getInt(columnIndex);
			} else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
				value = cursor.getInt(columnIndex);
			} else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
				value = cursor.getInt(columnIndex);
			} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
				value = cursor.getLong(columnIndex);
			} else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
				value = cursor.getFloat(columnIndex);
			} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
				value = cursor.getDouble(columnIndex);
			} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
				value = cursor.getInt(columnIndex) != 0;
			} else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
				value = cursor.getString(columnIndex).charAt(0);
			} else if (fieldType.equals(String.class)) {
				value = cursor.getString(columnIndex);
			} else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
				value = cursor.getBlob(columnIndex);
			} else if (fieldType.isAssignableFrom(Model.class)) {
				final long entityId = cursor.getLong(columnIndex);
				@SuppressWarnings("unchecked")
				final Class<? extends Model> entityType = (Class<? extends Model>) fieldType;
				
				value = new Select().from(entityType).where("Id=?", entityId).executeSingle();
			}

			try {
				
				if(value != null) { field.set(this, value); }
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Private methods
	private void defineTableName() {
		if(!getClass().isAnnotationPresent(Table.class)) {
			throw new RuntimeException(getClass().toString()+" must implement the Table annotation");
		}
		mTableName = getClass().getAnnotation(Table.class).name();
	}
	
	private String getTableName() {
		return mTableName;
	}
	
}

package eu.monniot.memoArcher.activerecord.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	
	/**
	 * The name of the column in the database. If not set then the name is taken from the field name.
	 * @return
	 */
	public String name() default "";
	
	/**
	 * Define if the column's data can be null.
	 * @return
	 */
	public boolean notNull() default true;
}

package com.suun.publics.hibernate.MySQL;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.PropertyExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

public class MySQLRestrictions extends Restrictions{
	/**
	 * Apply an "equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression eq(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, "=");
	}
	/**
	 * Apply a "not equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression ne(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, "<>");
	}
	/**
	 * Apply a "like" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression like(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, " like ");
	}
	/**
	 * Apply a "like" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression like(String propertyName, String value, MatchMode matchMode) {
		return new MySQLSimpleExpression(propertyName, matchMode.toMatchString(value), " like " );
	}
	
	/**
	 * Apply a "greater than" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression gt(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, ">");
	}
	/**
	 * Apply a "less than" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression lt(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, "<");
	}
	/**
	 * Apply a "less than or equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression le(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, "<=");
	}
	/**
	 * Apply a "greater than or equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression ge(String propertyName, Object value) {
		return new MySQLSimpleExpression(propertyName, value, ">=");
	}
	/**
	 * Apply a "between" constraint to the named property
	 * @param propertyName
	 * @param lo value
	 * @param hi value
	 * @return Criterion
	 */
	public static Criterion between(String propertyName, Object lo, Object hi) {
		return new MySQLBetweenExpression(propertyName, lo, hi);
	}

	/**
	 * Apply an "equal" constraint to two properties
	 */
	public static PropertyExpression eqProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, "=");
	}
	/**
	 * Apply a "not equal" constraint to two properties
	 */
	public static PropertyExpression neProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, "<>");
	}
	/**
	 * Apply a "less than" constraint to two properties
	 */
	public static PropertyExpression ltProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, "<");
	}
	/**
	 * Apply a "less than or equal" constraint to two properties
	 */
	public static PropertyExpression leProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, "<=");
	}
	/**
	 * Apply a "greater than" constraint to two properties
	 */
	public static PropertyExpression gtProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, ">");
	}
	/**
	 * Apply a "greater than or equal" constraint to two properties
	 */
	public static PropertyExpression geProperty(String propertyName, String otherPropertyName) {
		return new MySQLPropertyExpression(propertyName, otherPropertyName, ">=");
	}
	
}

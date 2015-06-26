package com.suun.publics.hibernate;

/**
 * @author kyle_fang
 *
 */
public class FilterInfo {
	private String fieldName;
	private Logic logic;
	private Object value;
	private boolean isOr=false;
	/**
	 * @return the isOr
	 */
	public boolean isOr() {
		return isOr;
	}
	/**
	 * @param isOr the isOr to set
	 */
	public void setOr(boolean isOr) {
		this.isOr = isOr;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param logic the logic to set
	 */
	public void setLogic(Logic logic) {
		this.logic = logic;
	}
	/**
	 * @return the logic
	 */
	public Logic getLogic() {
		return logic;
	}
	/**
	 * @param value the value1 to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	public static Logic StrToLogic(String slogic) {
		if (slogic.equals("EQUAL")){
			return Logic.EQUAL;
		} else if (slogic.equals("NOTEQUAL")){
			return Logic.NOTEQUAL;
		} else if (slogic.equals("GREAT")){
			return Logic.GREAT;
		} else if (slogic.equals("GREATEQUAL")){
			return Logic.GREATEQUAL;
		} else if (slogic.equals("LESS")){
			return Logic.LESS;
		} else if (slogic.equals("LESSEQUAL")){
			return Logic.LESSEQUAL;
		} else if (slogic.equals("LLIKE")){
			return Logic.LLIKE;
		} else if (slogic.equals("RLIKE")){
			return Logic.RLIKE;
		} else if (slogic.equals("LIKE")){
			return Logic.LIKE;
		} else {
			return Logic.SQL;
		}
	}
	
	public enum Logic {
		EQUAL,NOTEQUAL,GREAT,GREATEQUAL,LESS,LESSEQUAL,LLIKE,RLIKE,LIKE,SQL,ISNULL,ISNOTNULL;
	}
}

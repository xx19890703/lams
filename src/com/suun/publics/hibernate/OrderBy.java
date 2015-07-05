package com.suun.publics.hibernate;

/**
 * @author kyle_fang
 *
 */
public class OrderBy {
	private String fieldName;
	private boolean isAsc;
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
	 * @param isAsc the isAsc to set
	 */
	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	/**
	 * @return the isAsc
	 */
	public boolean isAsc() {
		return isAsc;
	}

}

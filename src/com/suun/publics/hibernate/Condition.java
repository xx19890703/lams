package com.suun.publics.hibernate;

import java.util.List;


public class Condition {
	
	private List<OrderBy> orderBys = null;

	private List<com.suun.publics.hibernate.FilterInfo> filterInfos = null;

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setFilterInfos(List<com.suun.publics.hibernate.FilterInfo> filterInfos2) {
		this.filterInfos = filterInfos2;
	}

	public List<com.suun.publics.hibernate.FilterInfo> getFilterInfos() {
		return filterInfos;
	}

}

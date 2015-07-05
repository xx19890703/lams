package com.suun.service.system.workflow;

import java.io.Serializable;

public class suunGroupMember implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long dbid;

	protected int dbversion;

	private suunUser user;

	private suunGroup group;

	protected String role;

	public int getDbversion() {
		return dbversion;
	}

	public void setDbversion(int dbversion) {
		this.dbversion = dbversion;
	}

	public long getDbid() {
		return dbid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setDbid(long dbid) {
		this.dbid = dbid;
	}

	public suunGroup getGroup() {
		return group;
	}

	public void setGroup(suunGroup group) {
		this.group = group;
	}

	public String getUserNo() {
		return user.getUserNo();
	}

	public String getUserID() {
		return user.getId();
	}

	public String getUserName() {
		return user.getUserName();
	}

	public suunUser getUser() {
		return user;
	}

	public void setUser(suunUser user) {
		this.user = user;
	}

}
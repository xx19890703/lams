package com.suun.service.system.workflow;

import java.io.Serializable;

import org.jbpm.api.identity.Group;

public class suunGroup implements Serializable, Group {

	private static final long serialVersionUID = 1L;

	private String id;

	private String groupName;// 组织名称

	private String groupType;// 组织类型

	private suunGroup parentGroup;// 父组织

	private String remarks;// 备注

	protected long dbid;

	protected int dbversion;

	public int getDbversion() {
		return dbversion;
	}

	public void setDbversion(int dbversion) {
		this.dbversion = dbversion;
	}

	public long getDbid() {
		return dbid;
	}

	public void setDbid(long dbid) {
		this.dbid = dbid;
	}

	public String getParentGroupID() {
		return parentGroup != null ? parentGroup.getId() : null;
	}

	public String getParentGroupName() {
		return parentGroup == null ? "xxx" : parentGroup.getGroupName();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public suunGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(suunGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	// 实现Group接口必须的几个方法
	/** 在一个工作组类型内必须唯一*/
	public String getName() {
		return this.groupName;
	}
	/** 获取工作组类型 */
	public String getType() {
		return this.groupType;
	}
	/**用户组ID必须唯一*/
	public String getId() {
		return id;
	}
}

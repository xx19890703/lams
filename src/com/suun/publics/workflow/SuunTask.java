package com.suun.publics.workflow;

import java.util.Date;

public class SuunTask {
	private long dbid;
	private String taskName;
	private String assignee;
	private String assigneeName;
	private Date createTime;
	private String executionId;
	private String operaterId;
	private String operaterName;
	private String operate;
	private String objId;
	private String objName;
	private String invoice_id;
	private String description;
	
	public long getDbid() {
		return dbid;
	}

	public void setDbid(long l) {
		this.dbid = l;
	}
	
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getOperaterName() {
		return operaterName;
	}

	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}


	public String getObjId() {
		return objId;
	}


	public void setObjId(String objId) {
		this.objId = objId;
	}


	public String getObjName() {
		return objName;
	}


	public void setObjName(String objName) {
		this.objName = objName;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskname) {
		this.taskName = taskname;
	}

}

package com.suun.model.system.workflow;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** 流程定义管理
 */
@Entity
@Table(name = "sys_workflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Workflow {
	 
	private String objName;
	private String objDescription;
	private String objInput;
	
    @Id
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjDescription() {
		return objDescription;
	}

	public void setObjDescription(String objDescription) {
		this.objDescription = objDescription;
	}

	public String getObjInput() {
		return objInput;
	}

	public void setObjInput(String objInput) {
		this.objInput = objInput;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

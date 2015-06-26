package com.suun.model.system.workflow;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jbpm.pvm.internal.task.TaskImpl;

import com.suun.model.IdEntity;
import com.suun.model.system.security.User;

/** 流程定义管理
 */
@Entity
@Table(name = "sys_workflow_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkflowLog extends IdEntity {
	 
	private Date op_time;
	private Workflow workflow;
	private User user;
	private String operate;
	private String invoice_id;
	private String description;
	private int state=1;
	
	private TaskImpl task;
	
	@ManyToOne(optional=true,cascade=CascadeType.REFRESH)  
	@JoinColumn(name = "objname")
	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	@ManyToOne(optional=true,cascade=CascadeType.REFRESH)  
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getOp_time() {
		return op_time;
	}

	public void setOp_time(Date op_time) {
		this.op_time = op_time;
	}
	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	@OneToOne(optional=false,cascade=CascadeType.REFRESH)  
	@JoinColumn(name = "execution_id",referencedColumnName="EXECUTION_ID_")
	public TaskImpl getTask() {
		return task;
	}

	public void setTask(TaskImpl task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

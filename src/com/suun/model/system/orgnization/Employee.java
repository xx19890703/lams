package com.suun.model.system.orgnization;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

/**
 * 职员
 * @author 
 *
 */
@Entity
@Table(name = "sys_Employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee {
	
	private String employeeid;
	
    private String employeename;
    
    private String email;
    
    private String phone;
    
    private com.suun.model.system.Dic_data state;

	@Id
	@Column(length=20)
	public String getEmployeeid() {
		return employeeid;
	}
	@Column(length=40)
	public String getEmployeename() {
		return employeename;
	}

    public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	
	@Column(length=40)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(length=20)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "state", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'STATE'", referencedColumnName = "dic_no"))
	})
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public com.suun.model.system.Dic_data getState() {
		return state;
	}

	public void setState(com.suun.model.system.Dic_data state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	

}
 
package com.suun.model.system.orgnization;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.suun.model.IdEntity;

/**
 * 部门职工
 * @author 
 *
 */
@Entity
@Table(name = "sys_dep_employees")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Depemployees extends IdEntity {
	
	private Department department;
	
    private Employee employee;          
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.orgnization.Department.class)
    @JoinColumn(name="department_id")
	public Department getDepartment() {
		return department;
	}
    
	public void setDepartment(Department department) {
		this.department = department;
	}
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.orgnization.Employee.class)
	@JoinColumn(name="employee_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
 
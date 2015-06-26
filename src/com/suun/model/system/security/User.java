package com.suun.model.system.security;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suun.model.system.orgnization.Employee;


/**
 * 登录用户
 * 
 */
@Entity
@Table(name = "sys_users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties (value = {"hibernateLazyInitializer","handler","fieldHandler","password","roles"}) 
public class User {
	
	private String userId;

	private Employee employee;

	private String password;
	
	private int state=1;
	
	private Set<Role> roles = new LinkedHashSet<Role>();	 
	
	@Id
	@Column(name="user_id",length=20)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }/*, fetch = FetchType.EAGER*/)
	@JoinTable(name = "sys_user_roles", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	//@OrderBy("roleId") 
	@Fetch(FetchMode.SUBSELECT)//两次查询
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.orgnization.Employee.class)
	@JoinColumn(name="employeeid", referencedColumnName = "employeeid")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Transient
	public String getStateName(){
		if (state==1){
			return "使用";
		} else{
			return "作废";
		}
	}

	//非持久化属性.
	@Transient
	public List<String> getIdlist() {
		List<String> rolelists=new ArrayList<String>();  
		for (Role role:roles){
			rolelists.add(role.getRoleId());
		}
		return rolelists;
	}


}
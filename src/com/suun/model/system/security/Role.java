package com.suun.model.system.security;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.suun.model.system.developer.Function;
import com.suun.publics.system.DicService;
import com.suun.publics.utils.CollectionUtils;

@Entity
@Table(name = "sys_roles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role {	
	private String roleId;	
	private String roleName;
	private Set<Function> auths = new LinkedHashSet<Function>();	
	private int state;	
	private String stateName;

	@Id  
	@Column(name = "role_id",length=30)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "role_name")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_authorities", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
	@OrderBy("funId")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<Function> getAuths() {
		return auths;
	}

	public void setAuths(Set<Function> auths) {
		this.auths = auths;
	}
	@Column(name = "state_no")
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	@Transient
	public String getAuthNames() throws Exception {
		return CollectionUtils.collectAsString(auths, "funName", ", ");
	}

	@SuppressWarnings("unchecked")
	@Transient
	public List<String> getAuthIds() throws Exception {
		return CollectionUtils.collectAsList(auths, "funId");
	}
	
	@Transient
	public String getStateName() {
		stateName=DicService.GetDic_dataName("STATE",String.valueOf(this.state));
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}

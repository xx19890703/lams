package com.suun.model.system.developer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity 
@Table(name="sys_functions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Function {
	//必须以AUTH_开头,权限ID
	private String funId;
	
	private String menuId;
	
	private String funName;

	private String url;
	
	//private int isDefult;
	@Id
	@Column(length=30)
	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}
	@Column(length=30)
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
/*
	public int getIsDefult() {
		return isDefult;
	}

	public void setIsDefult(int isDefult) {
		this.isDefult = isDefult;
	}*/
	@Column(length=200)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    @Column(length=20)
	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}
	/*
	@Transient
	public String getIsDefultName() {
		if (isDefult==1){
			return "是";
		} else{
			return "否";
		}
	}*/
}

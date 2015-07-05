package com.suun.model.system.developer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sys_menus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu{
	
	private String menuId;

	private  String menuName;
	//0:顶层菜单，1：分类菜单，2，枝干菜单，3：叶子菜单
	private int menuType;

	private String menuParid;
	
	private String menuImg;	
	
	private String menuTypeName;
	
	private int itemOrder;	
	
	private String menuUrl;
	
	private int isframe;

	private int isadmin;
	
	private List<String> auths=new ArrayList<String>();

	@Id
	@Column(length=30)
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@Column(length=30)
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(length=30)
	public String getMenuParid() {
		return menuParid;
	}

	public void setMenuParid(String menuParid) {
		this.menuParid = menuParid;
	}
	@Column(length=80)
	public String getMenuImg() {
		return menuImg;
	}

	public void setMenuImg(String menuImg) {
		this.menuImg = menuImg;
	}	
	@JsonIgnore
	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	@Transient
    public String getMenuTypeName() {
		switch (menuType) {
		case 0:menuTypeName="顶层菜单";break;
		case 1:menuTypeName="分类菜单";break;
		case 2:menuTypeName="枝干菜单";break;
		case 3:menuTypeName="叶子菜单";break;
		}		
		return menuTypeName;
	}

	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	@Column(length=200)
	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	
	@JsonIgnore
	@Column(columnDefinition="int default 0")
	public int getIsadmin() {
		return isadmin;
	}
	
	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}
	@JsonIgnore
	@Column(columnDefinition="int default 0")
	public int getIsframe() {
		return isframe;
	}

	public void setIsframe(int isframe) {
		this.isframe = isframe;
	}
	@JsonIgnore
	@Transient
	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}

}
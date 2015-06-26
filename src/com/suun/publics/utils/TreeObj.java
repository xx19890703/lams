package com.suun.publics.utils;

import java.util.List;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;

//@JsonIgnoreProperties(value = { "exData" }) 
public class TreeObj {
	private String id;		
	private String text;
	private String parentId;
	private Object checked;
	private String icon;
	private String href;
	private boolean leaf;
	private boolean expanded;
	private List<TreeObj> children;
	
	private Object exData;
	
	public void setId(String id) {
		this.id = id;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public void setChecked(Object checked) {
		this.checked = checked;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	} 	
	
	public String getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public String getParentId() {
		return parentId;
	}
	public Object getChecked() {
		return checked;
	}
	public String getIcon() {
		return icon;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public List<TreeObj> getChildren() {
		return children;
	}
	public void setChildren(List<TreeObj> children) {
		this.children = children;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public Object getExData() {
		return exData;
	}
	public void setExData(Object exData) {
		this.exData = exData;
	}

}

package com.suun.model.system.demo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 组织结构
 * @author
 *
 */
@Entity
@Table(name = "DEMOTREE")
public class Demotree {
	
	private String oid;
	
    private String name;
   
    private String description;
   
    private String pid;
    
    @Id
    @Column(length=30)
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}  
	@Column(length=50)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=255)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(length=30)
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
 
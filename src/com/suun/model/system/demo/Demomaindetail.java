package com.suun.model.system.demo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/** 复合主键实例
 */
@Entity
@Table(name = "Demomaindetail")
@IdClass(DemoDetailkey.class)
public class Demomaindetail implements Serializable{

	private static final long serialVersionUID = -6746841387561597836L;

	//private DemoDetailkey key;
	private Long mid;
	
	private Long did;
	
	private String dname;
	
	private Date ddemo;

	@Id
	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}
	@Id
	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public Date getDdemo() {
		return ddemo;
	}

	public void setDdemo(Date ddemo) {
		this.ddemo = ddemo;
	} 
	
}

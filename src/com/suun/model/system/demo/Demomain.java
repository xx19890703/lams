package com.suun.model.system.demo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "Demomain")
public class Demomain {
	 
	private Long mid;

	private String mname; 
	
	private Long pmid;
	@Id
	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public Long getPmid() {
		return pmid;
	}

	public void setPmid(Long pmid) {
		this.pmid = pmid;
	}
	
}

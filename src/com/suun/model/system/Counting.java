package com.suun.model.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "sys_counting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Counting {
	
	private String obj_no;

	private Long obj_count;
	
    @Id
    @Column(length=30)
	public String getObj_no() {
		return obj_no;
	}
    
	public void setObj_no(String obj_no) {
		this.obj_no = obj_no;
	}
	
	public void setObj_count(Long obj_count) {
		this.obj_count = obj_count;
	}
	
	public Long getObj_count() {
		return obj_count;
	}

}

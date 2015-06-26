package com.suun.model.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "sys_dic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dic {
	
	private String dic_no;

	private String dic_name;

	private Integer issys;
	
    @Id
    @Column(length=30)
	public String getDic_no() {
		return dic_no;
	}
    
	public void setDic_no(String dic_no) {
		this.dic_no = dic_no;
	}
	
	public void setDic_name(String dic_name) {
		this.dic_name = dic_name;
	}
	@Column(length=50)
	public String getDic_name() {
		return dic_name;
	}

	public void setIssys(Integer issys) {
		this.issys = issys;
	}

	public Integer getIssys() {
		return issys;
	}
}

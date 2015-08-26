package com.suun.model.serviceuser;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.suun.model.IdEntity;

/**
 * 模板上传记录
 * @author renlq
 */
@Entity
@Table(name = "app_template_upload")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TemplateCfgUpLoad extends IdEntity{
	// 上传人
	private String person;
	// 上传时间
	private Date upTime;
	// 文件路径
	private String path;
	
	@Column(length=50)
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(length=50)
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	
	@Column(length=300)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}

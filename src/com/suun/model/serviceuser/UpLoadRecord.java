package com.suun.model.serviceuser;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.suun.model.IdEntity;

/**
 * 合同上传记录
 * @author renlq
 */
@Entity
@Table(name = "app_uploadrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpLoadRecord extends IdEntity{

	// 上传合同编号
	private String contractid;
	// 上传合同编号
	private String contractname;
	// 上传人
	private String person;
	// 上传时间
	private Date upTime;
	// 备注
	private String remark;
	//类型
	private String type;//上传还是下载
	@Column(name="contractid",length=30)
	@NotFound(action = NotFoundAction.IGNORE)
	public String getContractid() {
		return contractid;
	}
	public void setContractid(String contractid) {
		this.contractid = contractid;
	}
	@Column(length=50)
	public String getContractname() {
		return contractname;
	}
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}
	@Column(length=50)
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(length=50)
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	
	@Column(length=200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(length=20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

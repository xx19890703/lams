package com.suun.model.serviceuser;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.suun.model.IdEntity;
import com.suun.model.system.Dic_data;

/**
 * 合同下载记录
 * @author renlq
 */
@Entity
@Table(name = "app_downloadrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DownLoadRecord extends IdEntity{

	// 下发合同编号
	private ContractDetail contractid;
	// 状态
	private Dic_data status;
	// 下发人
	private String person;
	// 下发时间
	private Date issuedTime;
	// 导入时间
	private Date importTime;
	// 审核人
	private String checekPerson;
	// 审核时间
	private Date checkTime;
	// 下发次数
	private Integer count;
	// 备注
	private String remark;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = ContractDetail.class)
	@JoinColumn(name="contractid")
	@NotFound(action = NotFoundAction.IGNORE)
	public ContractDetail getContractid() {
		return contractid;
	}
	public void setContractid(ContractDetail contractid) {
		this.contractid = contractid;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "state", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'STATE'", referencedColumnName = "dic_no"))
	})
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public Dic_data getStatus() {
		return status;
	}
	public void setStatus(Dic_data status) {
		this.status = status;
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
	public Date getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(length=50)
	public Date getImportTime() {
		return importTime;
	}
	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}
	
	@Column
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Column(length=50)
	public String getChecekPerson() {
		return checekPerson;
	}
	public void setChecekPerson(String checekPerson) {
		this.checekPerson = checekPerson;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(length=50)
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@Column(length=200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

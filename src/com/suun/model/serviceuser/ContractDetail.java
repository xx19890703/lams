package com.suun.model.serviceuser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 合同信息
 * @author renlq
 */
@Entity
@Table(name = "app_contract_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContractDetail {
	// 合同编号
	private String did;
	// 合同名称
	private String name;
	// 制造厂信息
	private FactoryInfo finfo;
	// 所属订单
	private String orderinfo;
	// 导出时间
	private Date importTime;
	// 下发时间
	private Date issuedTime;
	// 预计导入时间
	private Date planImportTime;
	// 导出次数
	private Integer importCount;
	// 审核人
	private String auditPerson;
	// 审核时间
	private Date auditTime;
	// 合同状态
	private com.suun.model.system.Dic_data status;
	// 关联主表
	private ContractCategory conmain;
	// 模板对应数据库表
	private List<ContractTemplateRes> rescontent=new ArrayList<ContractTemplateRes>();
	// 数据状态
	private com.suun.model.system.Dic_data state;
	
	@Id
    @Column(length=30,name="contractId")
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	
	@Column(length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "state", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'STATE'", referencedColumnName = "dic_no"))
	})
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public com.suun.model.system.Dic_data getState() {
		return state;
	}
	public void setState(com.suun.model.system.Dic_data state) {
		this.state = state;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = ContractCategory.class)
	@JoinColumn(name="conmain")
	@NotFound(action = NotFoundAction.IGNORE)
	public ContractCategory getConmain() {
		return conmain;
	}
	public void setConmain(ContractCategory conmain) {
		this.conmain = conmain;
	}
	
	@OneToMany(cascade = { CascadeType.ALL },mappedBy ="condetail")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonManagedReference//主需要子的数据JSON转换
	public List<ContractTemplateRes> getRescontent() {
		return rescontent;
	}
	@JsonBackReference
	public void setRescontent(List<ContractTemplateRes> rescontent) {
		this.rescontent = rescontent;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = FactoryInfo.class)
	@JoinColumn(name="factoryinfo")
	@NotFound(action = NotFoundAction.IGNORE)
	public FactoryInfo getFinfo() {
		return finfo;
	}
	public void setFinfo(FactoryInfo finfo) {
		this.finfo = finfo;
	}
	
	@Column(length=50)
	public String getOrderinfo() {
		return orderinfo;
	}
	public void setOrderinfo(String orderinfo) {
		this.orderinfo = orderinfo;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(length=50)
	public Date getImportTime() {
		return importTime;
	}
	public void setImportTime(Date importTime) {
		this.importTime = importTime;
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
	public Date getPlanImportTime() {
		return planImportTime;
	}
	public void setPlanImportTime(Date planImportTime) {
		this.planImportTime = planImportTime;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "status", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'STATUS'", referencedColumnName = "dic_no"))
	})
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public com.suun.model.system.Dic_data getStatus() {
		return status;
	}
	public void setStatus(com.suun.model.system.Dic_data status) {
		this.status = status;
	}
	
	@Column
	public Integer getImportCount() {
		return importCount;
	}
	public void setImportCount(Integer importCount) {
		this.importCount = importCount;
	}
	
	@Column(length=50)
	public String getAuditPerson() {
		return auditPerson;
	}
	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(length=50)
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
}

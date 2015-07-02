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
	// 关联主表
	private ContractCategory conmain;
	// 模板对应数据库表
	private List<ContractTemplateRes> rescontent=new ArrayList<ContractTemplateRes>();
	// 状态
	private com.suun.model.system.Dic_data state;
	
	@Id
    @Column(length=30)
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
	
	@JsonBackReference
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="condetail",orphanRemoval=true)
	@NotFound(action = NotFoundAction.IGNORE)
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
}

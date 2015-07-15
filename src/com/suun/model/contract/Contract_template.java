package com.suun.model.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 合同对应模板配置表
 * @author renlq
 */
@Entity
@Table(name = "contract_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract_template {

	//联合主键
	private ContractTemplatePK id;
	//模板(菜单)名称
	private String templateName;
	//模板打开方式
	private String openType;
	
	@Id
	public ContractTemplatePK getId() {
		return id;
	}
	public void setId(ContractTemplatePK id) {
		this.id = id;
	}
	
	@Column(length=30)
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	@Column(length=30)
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
}

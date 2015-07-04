package com.suun.model.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.suun.model.IdEntity;

/**
 * 合同对应模板配置表
 * @author renlq
 */
@Entity
@Table(name = "contract_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract_template extends IdEntity{

	//合同编号
	private String contractId;
	//数据库表名称
	private String tableName;
	//模板URL
	private String templateUrl;
	
	@Column(length=30)
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@Column(length=30)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Column(length=30)
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	
}

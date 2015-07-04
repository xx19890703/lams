package com.suun.model.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.suun.model.IdEntity;

/**
 * 合同对应数据库表配置表
 * @author renlq
 */
@Entity
@Table(name = "contract_mode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract_mode extends IdEntity{
	//合同编号
	private String contractId;
	//数据库表名称
	private String tableName;
	
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

}

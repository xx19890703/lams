package com.suun.model.contract;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 合同对应数据库表配置表
 * @author renlq
 */
@Entity
@Table(name = "contract_mode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract_mode {
	//合同编号
	private ContractModePK id;

	@Id
	public ContractModePK getId() {
		return id;
	}

	public void setId(ContractModePK id) {
		this.id = id;
	}
}

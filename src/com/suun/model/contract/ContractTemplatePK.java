package com.suun.model.contract;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 合同对应模板配置表  联合主键
 * @author renlq
 */

@Embeddable
public class ContractTemplatePK implements Serializable {

	private static final long serialVersionUID = 1L;
	//合同编号
	private String contractId;
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
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result
				+ ((templateUrl == null) ? 0 : templateUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContractTemplatePK other = (ContractTemplatePK) obj;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (templateUrl == null) {
			if (other.templateUrl != null)
				return false;
		} else if (!templateUrl.equals(other.templateUrl))
			return false;
		return true;
	}
	
	
}

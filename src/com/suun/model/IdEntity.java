package com.suun.model;

import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 统一定义id的entity基类.
 * 
 * 
 */
@MappedSuperclass
public class IdEntity {

	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		 StringBuffer buf = new StringBuffer();
		 Map map = null;
	        try {
	            map = BeanUtils.describe(this);
	        } catch (Exception e) {
	            buf.append("BeanUtils describe bean occur exception:" + e.getMessage());
	            return buf.toString();
	        }
	        return map.toString();   
		//return ToStringBuilder.reflectionToString(this);
	}
}

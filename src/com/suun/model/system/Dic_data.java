package com.suun.model.system;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity 
@Table(name="sys_dic_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dic_data {
	
	private Dic_datakey key;

	private String data_name;

	@EmbeddedId
	public Dic_datakey getKey() {
		return key;
	}

	public void setKey(Dic_datakey key) {
		this.key = key;
	}
	
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	@Column(length=50)
	public String getData_name() {
		return data_name;
	}

}

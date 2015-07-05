package com.suun.model.system.demo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonBackReference;

/** 授权
 */
@Entity
@Table(name = "DemoDetail")
public class DemoDetail {
	 
	private String authId;

	private String displayName;
	
	private DemoGroups authgroups;
	
	private com.suun.model.system.Dic_data state;
	
    @Id
    @Column(name="authId",length=30)//必须放在方法
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}
	@Column(name="displayName",length=50)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	//optional=false联接关系为inner join  optional=true联接关系为left join
	// name声明外键列的名字，referencedColumnName声明外键指向列的列名
	
	@ManyToOne(fetch = FetchType.LAZY,optional=false,cascade = {CascadeType.ALL})
	@JoinColumn(name = "authgroup_id")
	@JsonBackReference//子JSON转换时不做主关联，避免造成死循
	public DemoGroups getAuthgroups() {
		return authgroups;
	}

	public void setAuthgroups(DemoGroups authgroups) {
		this.authgroups = authgroups;
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
}

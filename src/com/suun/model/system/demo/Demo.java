package com.suun.model.system.demo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonFormat;

/** 单表实例
 */
@Entity
@Table(name = "Demo")
public class Demo {
	 
	private String authId;

	private String displayName; 
	
	private Date demodate;
	
	private com.suun.model.system.Dic_data state;
	
    @Id
    //@Column(name="authId")必须放在方法上
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//suunplatform-servlet.xml 默认配置  "yyyy-MM-dd“
	public Date getDemodate() {
		return demodate;
	}

	public void setDemodate(Date demodate) {
		this.demodate = demodate;
	}
	
}

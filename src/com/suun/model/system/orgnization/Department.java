package com.suun.model.system.orgnization;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 组织结构
 *
 */
@Entity
@Table(name = "sys_Department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department {
	
	private String did;
	
    private String name;
   
    private String description;
   
    private String pid;
    
    private Employee header;
    
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
	@Column(length=255)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(length=30)
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.orgnization.Employee.class)
	@JoinColumn(name="header")
	@NotFound(action = NotFoundAction.IGNORE)
	public Employee getHeader() {
		return header;
	}
	
	public void setHeader(Employee header) {
		this.header = header;
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
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
 
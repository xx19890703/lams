package com.suun.model.serviceuser;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.suun.model.system.Dic_data;

/**
 * 报表模板对应的数据库表名、创建语句等
 * @author renlq
 */
@Entity
@Table(name = "app_service_templateres_content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TemplateResContent {
	// 数据库表名
	private String did;
	// 数据库表名称
	private String name;
	// 数据库creat sql文件路径
	private String csqlpath;
	// 数据库insert sql文件路径
	private String isqlpath;
	// 数据库表描述
	private String description;
	// 报表模板父资源编号
	private TemplateResDetail resdetail;
	// 状态
	private Dic_data state;
	
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
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "state", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'STATE'", referencedColumnName = "dic_no"))
	})
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public Dic_data getState() {
		return state;
	}
	public void setState(Dic_data state) {
		this.state = state;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = TemplateResDetail.class)
	@JoinColumn(name="resdetail")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonBackReference//子JSON转换时不做主关联，避免造成死循
	public TemplateResDetail getResdetail() {
		return resdetail;
	}
	public void setResdetail(TemplateResDetail resdetail) {
		this.resdetail = resdetail;
	}
	
	@Column(length=255)
	public String getCsqlpath() {
		return csqlpath;
	}
	public void setCsqlpath(String csqlpath) {
		this.csqlpath = csqlpath;
	}
	
	@Column(length=255)
	public String getIsqlpath() {
		return isqlpath;
	}
	public void setIsqlpath(String isqlpath) {
		this.isqlpath = isqlpath;
	}
}

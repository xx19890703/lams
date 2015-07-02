package com.suun.model.serviceuser;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 报表模板列表
 * @author renlq
 */
@Entity
@Table(name = "app_service_templateres_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TemplateResDetail {
	// 模板编号
	private String did;
	// 模板名称
	private String name;
	// 模板所在路径
	private String path;
	// 模板对应sql基本文件路径
	private String sqlpath;
	// 关联主表
	private TemplateRes resmain;
	// 模板对应数据库表
	private List<TemplateResContent> rescontent=new ArrayList<TemplateResContent>();
	// 状态
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
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = TemplateRes.class)
	@JoinColumn(name="resmain")
	@NotFound(action = NotFoundAction.IGNORE)
	public TemplateRes getResmain() {
		return resmain;
	}
	public void setResmain(TemplateRes resmain) {
		this.resmain = resmain;
	}
	
	@JsonBackReference
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="resdetail",orphanRemoval=true)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<TemplateResContent> getRescontent() {
		return rescontent;
	}
	@JsonBackReference
	public void setRescontent(List<TemplateResContent> rescontent) {
		this.rescontent = rescontent;
	}
	
	@Column(length=50)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(length=50)
	public String getSqlpath() {
		return sqlpath;
	}
	public void setSqlpath(String sqlpath) {
		this.sqlpath = sqlpath;
	}
}

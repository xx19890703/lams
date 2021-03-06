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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 报表模板分类表
 * @author renlq
 */
@Entity
@Table(name = "app_service_templateres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TemplateRes {
	// 模板分类编号
	private String did;
	// 模板分类名称
	private String name;
	// 模板分类描述
	private String description;
	// 资源分类父编号
	private String pid;
	// 模板子表
	private List<TemplateResDetail> resdetail=new ArrayList<TemplateResDetail>();
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
	
	@JsonIgnore
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="resmain")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<TemplateResDetail> getResdetail() {
		return resdetail;
	}
	@JsonIgnore
	public void setResdetail(List<TemplateResDetail> resdetail) {
		this.resdetail = resdetail;
	}
}

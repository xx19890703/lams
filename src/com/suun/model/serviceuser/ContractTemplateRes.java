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
 * 合同关联报表模板资源
 * @author renlq
 */
@Entity
@Table(name = "app_contract_templateres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContractTemplateRes{
	// 编号
	private String id;
	// 名称
	private String name;
	// 关联报表模板
	private TemplateResDetail template;
	// 模板打开类型
	private String openType;
	// 描述
	private String description;
	// 报表模板父资源编号
	private ContractDetail condetail;
	// 状态
	private Dic_data state;
	
	@Id
    @Column(length=30)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public TemplateResDetail getTemplate() {
		return template;
	}
	public void setTemplate(TemplateResDetail template) {
		this.template = template;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = ContractDetail.class)
	@JoinColumn(name="condetail")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonBackReference
	public ContractDetail getCondetail() {
		return condetail;
	}
	public void setCondetail(ContractDetail condetail) {
		this.condetail = condetail;
	}
	
	@Column(length=255)
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
}

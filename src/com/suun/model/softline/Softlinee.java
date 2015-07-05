package com.suun.model.softline;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;


@Entity
@Table(name = "APP_SOFTLINEE")
public class Softlinee {

	private String baseId;//编号

	private String name;//姓名
	
	private Date dobs;//出身年月
	
	private String idcarNo;//身份证号码
	
	private Date woundTime;//受伤时间
	
	private String phone;//联系电话
	
	private String addresss;//通讯地址
	
	private String postCode;//邮编
	
	private String picture;//照片

	private String unitName;//单位名称
	
	private String unitNo;//单位编码
	
	private String unitAdd;//通讯地址
	
	private String unitPostCode;//邮编
	
	private String linkName;//联系人
	
	private String lnPhone;//联系电话
	
	private Date hTime;//有害职业日期

	private String hJobName;//有害岗位名称
	
	private String hJobYeat;//有害工作年限
	
	private String hBookNo;//工伤认定书文号

	private String hBookPosition;//工伤认定部位
	
	private String checks;//申请检查科目
	
	private String mustCheck;//准检科目

	private String mark;//备注
	
	private String state;
	
    private com.suun.model.system.Dic_data slstate;//申请状态
	 
	private com.suun.model.system.Dic_data sex;//性别
	
	private Date systemData;//系统当前时间

	@Id
	@Column(length=30)
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	@Column(length=30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDobs() {
		return dobs;
	}

	public void setDobs(Date dobs) {
		this.dobs = dobs;
	}

	@Column(length=30)
	public String getIdcarNo() {
		return idcarNo;
	}

	public void setIdcarNo(String idcarNo) {
		this.idcarNo = idcarNo;
	}

	public Date getWoundTime() {
		return woundTime;
	}

	public void setWoundTime(Date woundTime) {
		this.woundTime = woundTime;
	}

	@Column(length=30)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column
	public String getAddresss() {
		return addresss;
	}

	public void setAddresss(String addresss) {
		this.addresss = addresss;
	}

	@Column(length=50)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(length=100)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Column(length=50)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(length=50)
	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	@Column(length=100)
	public String getUnitAdd() {
		return unitAdd;
	}

	public void setUnitAdd(String unitAdd) {
		this.unitAdd = unitAdd;
	}

	@Column(length=50)
	public String getUnitPostCode() {
		return unitPostCode;
	}

	public void setUnitPostCode(String unitPostCode) {
		this.unitPostCode = unitPostCode;
	}

	@Column(length=50)
	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	@Column(length=50)
	public String getLnPhone() {
		return lnPhone;
	}

	public void setLnPhone(String lnPhone) {
		this.lnPhone = lnPhone;
	}

	public Date gethTime() {
		return hTime;
	}

	public void sethTime(Date hTime) {
		this.hTime = hTime;
	}

	@Column(length=50)
	public String gethJobName() {
		return hJobName;
	}

	public void sethJobName(String hJobName) {
		this.hJobName = hJobName;
	}

	@Column(length=50)
	public String gethJobYeat() {
		return hJobYeat;
	}

	public void sethJobYeat(String hJobYeat) {
		this.hJobYeat = hJobYeat;
	}

	@Column(length=50)
	public String gethBookNo() {
		return hBookNo;
	}

	public void sethBookNo(String hBookNo) {
		this.hBookNo = hBookNo;
	}

	@Column
	public String gethBookPosition() {
		return hBookPosition;
	}

	public void sethBookPosition(String hBookPosition) {
		this.hBookPosition = hBookPosition;
	}

	@Column
	public String getChecks() {
		return checks;
	}

	public void setChecks(String checks) {
		this.checks = checks;
	}

	@Column
	public String getMustCheck() {
		return mustCheck;
	}

	public void setMustCheck(String mustCheck) {
		this.mustCheck = mustCheck;
	}

	@Column
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@Column(length=50)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Date getSystemData() {
		return systemData;
	}

	public void setSystemData(Date systemData) {
		this.systemData = systemData;
	}

	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "slstate", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'SLSTATE'", referencedColumnName = "dic_no"))
	})	
	public com.suun.model.system.Dic_data getSlstate() {
		return slstate;
	}

	public void setSlstate(com.suun.model.system.Dic_data slstate) {
		this.slstate = slstate;
	}
		
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
		    @JoinColumnOrFormula(column=@JoinColumn(name = "sex", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'SEX'", referencedColumnName = "dic_no"))
	})
	public com.suun.model.system.Dic_data getSex() {
		return sex;
	}

	public void setSex(com.suun.model.system.Dic_data sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}

package com.suun.model.professor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;


@Entity
@Table(name = "app_professorInfo")
public class Professor{
	
	//专家编号
	private String proId;
	//专家姓名
	private String proName;
	//性别
	private com.suun.model.system.Dic_data sex;
	//身份证号
	private String iDcard;
	//职称
	private com.suun.model.system.Dic_data rank;
	//鉴定科别
	private String category;
	//联系方式
	private String phoneNum;
	//工作单位
	private String workAddress;
	//备注
	private String remark;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE}, 
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

	@Id
	@Column(length=30)
	public String getProId() {
		return proId;
	}
	
	public void setProId(String proId) {
		this.proId = proId;
	}
	@Column(length=30)
	public String getProName() {
		return proName;
	}
	
	public void setProName(String proName) {
		this.proName = proName;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "rank", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'RANK'", referencedColumnName = "dic_no"))
	})
	public com.suun.model.system.Dic_data getRank() {
		return rank;
	}

	public void setRank(com.suun.model.system.Dic_data rank) {
		this.rank = rank;
	}

	@Column(length=30)
	public String getiDcard() {
		return iDcard;
	}
	public void setiDcard(String iDcard) {
		this.iDcard = iDcard;
	}
	@Column(length=20)
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Column(length=30)
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@Column(length=100)
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Professor [proId=" + proId + ", proName=" + proName + ", sex="
				+ sex + ", iDcard=" + iDcard + ", rank=" + rank + ", category="
				+ category + ", phoneNum=" + phoneNum + ", workAddress="
				+ workAddress + ", remark=" + remark + "]";
	}
	
	
}

package com.suun.model.softline;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import com.suun.model.professor.Professor;
import com.suun.model.system.Dic_data;



@Entity
@Table(name = "APP_SOFTLINEREVIEW")
@JsonIgnoreProperties (value = {"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SoftlineeReview {
	
	//Id
	private Long verdictId;
	
	//结论书编号
	private String verdictNo;
	
	//伤残情况
	private String invalidismCase;
	
	//坚定日期	
	private Date reviewDate;

	//伤残等级（十级）
	private Dic_data invalidismGrades;

	//护理等级（三级）
	private Dic_data nursingGrades;
	
	//评审依据
	private String reviewGist;
	
	//延长依据
	private String extensionGist;
	
	//延长时间
	private Date extensionTimeStart;
	private Date extensionTimeEnd;
	
	//辅助器具
	private String helpers;
	
	//康复时间
	private Date recoveryTimeStart;
	private Date recoveryTimeEnd;
	
	//疾病关联
	private String disAssociation;
	
	//鉴定专家成员（外键一对多）
	private List<Professor> accProfessionals;

	//基本信息（外键一对一）
	private Softlinee softlineeInfo;
	
	//备注
	private String remark;
	
	private Date systemData1;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getVerdictId() {
		return verdictId;
	}

	public void setVerdictId(Long verdictId) {
		this.verdictId = verdictId;
	}

	@Column
	public String getReviewGist() {
		return reviewGist;
	}

	public void setReviewGist(String reviewGist) {
		this.reviewGist = reviewGist;
	}

	@Column
	public String getExtensionGist() {
		return extensionGist;
	}

	public void setExtensionGist(String extensionGist) {
		this.extensionGist = extensionGist;
	}



	@Column
	public String getHelpers() {
		return helpers;
	}

	public void setHelpers(String helpers) {
		this.helpers = helpers;
	}


	public Date getExtensionTimeStart() {
		return extensionTimeStart;
	}

	public void setExtensionTimeStart(Date extensionTimeStart) {
		this.extensionTimeStart = extensionTimeStart;
	}

	public Date getExtensionTimeEnd() {
		return extensionTimeEnd;
	}

	public void setExtensionTimeEnd(Date extensionTimeEnd) {
		this.extensionTimeEnd = extensionTimeEnd;
	}

	public Date getRecoveryTimeStart() {
		return recoveryTimeStart;
	}

	public void setRecoveryTimeStart(Date recoveryTimeStart) {
		this.recoveryTimeStart = recoveryTimeStart;
	}

	public Date getRecoveryTimeEnd() {
		return recoveryTimeEnd;
	}

	public void setRecoveryTimeEnd(Date recoveryTimeEnd) {
		this.recoveryTimeEnd = recoveryTimeEnd;
	}

	@Column
	public String getDisAssociation() {
		return disAssociation;
	}

	public void setDisAssociation(String disAssociation) {
		this.disAssociation = disAssociation;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "app_slr_pro", joinColumns = { @JoinColumn(name = "verdictId") }, inverseJoinColumns = { @JoinColumn(name = "proId") })
	@Fetch(FetchMode.SUBSELECT)
	public List<Professor> getAccProfessionals() {
		return accProfessionals;
	}

	public void setAccProfessionals(List<Professor> accProfessionals) {
		this.accProfessionals = accProfessionals;
	}
	
	@OneToOne
	@JoinColumn(name = "sle_id")
	public Softlinee getSoftlineeInfo() {
		return softlineeInfo;
	}

	public void setSoftlineeInfo(Softlinee softlineeInfo) {
		this.softlineeInfo = softlineeInfo;
	}

	@Column
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "invalidismGrades", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'INVALIDISMGRADES'", referencedColumnName = "dic_no"))
	})
	public Dic_data getInvalidismGrades() {
		return invalidismGrades;
	}

	public void setInvalidismGrades(Dic_data invalidismGrades) {
		this.invalidismGrades = invalidismGrades;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "nursingGrades", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'NURSINGGRADES'", referencedColumnName = "dic_no"))
	})
	public Dic_data getNursingGrades() {
		return nursingGrades;
	}

	public void setNursingGrades(Dic_data nursingGrades) {
		this.nursingGrades = nursingGrades;
	}
	
	@Column(length=10)
	public String getVerdictNo() {
		return verdictNo;
	}

	public void setVerdictNo(String verdictNo) {
		this.verdictNo = verdictNo;
	}
	
	@Column
	public String getInvalidismCase() {
		return invalidismCase;
	}

	public void setInvalidismCase(String invalidismCase) {
		this.invalidismCase = invalidismCase;
	}

	@Column	
	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	
	public Date getSystemData1() {
		return systemData1;
	}

	public void setSystemData1(Date systemData1) {
		this.systemData1 = systemData1;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}

package com.suun.model.serviceuser;

import java.util.Date;

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

import com.suun.model.system.Dic_data;

@Entity
@Table(name = "app_service_factory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FactoryInfo {

	private String fno;       //制造厂编号
	private String fregister; //制造厂注册码
	private String fname;     //名称
	private String faddress;  //地址
	private String ftel;      //电话（传真）
	private String fower;     //法人
	private String fcontect;  //联系人
	private String fcontecttel;//联系人电话
	private Dic_data ftype;   //类别
	private Dic_data flevel;  //等级
	private Dic_data fdomain;  //加工领域
	private Date   ftime;      //登记时间
	private Dic_data fstandard;//资质标准
	private String fattachment;//附件
	private String remark;     //备注
	private String contractId;// 合同id
	
	@Id
    @Column(length=30,nullable=false)
	public String getFno() {
		return fno;
	}
	public void setFno(String fno) {
		this.fno = fno;
	}
	
    @Column(length=60,nullable=false)
	public String getFregister() {
		return fregister;
	}
	public void setFregister(String fregister) {
		this.fregister = fregister;
	}
	
	@Column(length=30,nullable=false)
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@Column(length=30)
	public String getFaddress() {
		return faddress;
	}
	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@Column(length=30)
	public String getFtel() {
		return ftel;
	}
	public void setFtel(String ftel) {
		this.ftel = ftel;
	}
	
	@Column(length=30)
	public String getFower() {
		return fower;
	}
	public void setFower(String fower) {
		this.fower = fower;
	}
	
	@Column(length=30)
	public String getFcontect() {
		return fcontect;
	}
	public void setFcontect(String fcontect) {
		this.fcontect = fcontect;
	}
	
	@Column(length=30)
	public String getFcontecttel() {
		return fcontecttel;
	}
	public void setFcontecttel(String fcontecttel) {
		this.fcontecttel = fcontecttel;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "ftype", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'FTYPE'", referencedColumnName = "dic_no"))
	})
	public Dic_data getFtype() {
		return ftype;
	}
	public void setFtype(Dic_data ftype) {
		this.ftype = ftype;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "flevel", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'FLEVEL'", referencedColumnName = "dic_no"))
	})
	public Dic_data getFlevel() {
		return flevel;
	}
	public void setFlevel(Dic_data flevel) {
		this.flevel = flevel;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "fstandard", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'FSTANDARD'", referencedColumnName = "dic_no"))
	})
	public Dic_data getFstandard() {
		return fstandard;
	}
	public void setFstandard(Dic_data fstandard) {
		this.fstandard = fstandard;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
			targetEntity = com.suun.model.system.Dic_data.class)
	@JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name = "fdomain", referencedColumnName = "data_no")),
		    @JoinColumnOrFormula(formula=@JoinFormula(value="'FDOMAIN'", referencedColumnName = "dic_no"))
	})
	public Dic_data getFdomain() {
		return fdomain;
	}
	public void setFdomain(Dic_data fdomain) {
		this.fdomain = fdomain;
	}
	
	@Column(length=30)
	public Date getFtime() {
		return ftime;
	}
	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}
	
	@Column(length=30)
	public String getFattachment() {
		return fattachment;
	}
	public void setFattachment(String fattachment) {
		this.fattachment = fattachment;
	}
	
	@Column(length=30)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(length=30)
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
}

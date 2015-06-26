package com.suun.model.system.demo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "DEMOGROUPS")
public class DemoGroups {
	 
	private String authGroupId;

	private String displayName;
	
	private List<DemoDetail> auths;
    @Id
    @Column(name="authgroup_id",length=20)//必须放在方法上
	public String getAuthGroupId() {
		return authGroupId;
	}

	public void setAuthGroupId(String authGroupId) {
		this.authGroupId = authGroupId;
	}
	@Column(length=50)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	//双向关联父子表	 //mappedBy定义类之间的双向关系,该参数包含了主体端(Authority)的属性名authgroups,关系是在对端表里面维护
	//                 cascade定义类和类之间的级联关系,PERSIST 级联新建 MERGE 级联更新 REMOVE 级联删除 REFRESH 级联刷新
	//                 FetchType.LAZY关系类在被访问时才加载
	@OneToMany( mappedBy = "authgroups", cascade={CascadeType.REMOVE,CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JsonManagedReference//主需要子的数据JSON转换
	public List<DemoDetail> getAuths() {
		return auths;
	}

	public void setAuths(List<DemoDetail> auths) {
		this.auths = auths;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

package com.suun.service.system.demo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.model.system.demo.DemoDetail;
import com.suun.model.system.demo.DemoGroups;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;


/**
 * 整个User模块内的业务逻辑Facade类.
 * 组合user,role,auth三者的DAO.
 * DAO均由SimpleHibernateTemplate指定泛型生成.
 * 
 */
@Service
@Transactional
public class DemoDetailManager {

	private SimpleHibernateTemplate<DemoGroups, String> demoGroupsDao;
	
	private SimpleHibernateTemplate<DemoDetail, String> demoDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		demoGroupsDao = new SimpleHibernateTemplate<DemoGroups, String>(sessionFactory, DemoGroups.class);
		demoDao = new SimpleHibernateTemplate<DemoDetail, String>(sessionFactory, DemoDetail.class);
	}

	@Transactional(readOnly = true)
	public List<DemoGroups> getAllDemoGroupsDao() {
		return demoGroupsDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<DemoGroups> getAllDemoGroups(Condition condition) {
		return demoGroupsDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<DemoGroups> getAllDemoGroups(Page<DemoGroups> page) {
		return demoGroupsDao.findAll(page);
		//List<DemoGroups> aa=demoGroupsDao.findByProperty("displayName", "rrrr");
		//List<DemoDetail> auths=demoDao.findByProperty("authgroups", aa.get(0));
		
		//return demoGroupsDao.findByCriteria(page, Restrictions.eq("auths.authId", auths.get(0).getAuthId()));
		
	}
	
	@Transactional(readOnly = true)
	public List<DemoDetail> getAllDemos(String treeid,Condition condition) {
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("authgroups.authGroupId");
		f.setLogic(Logic.EQUAL);
		f.setValue(treeid);
		condition.getFilterInfos().add(f);
		return demoDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<DemoDetail> getAllDemos(String treeid,Page<DemoDetail> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("authgroups.authGroupId");
		f.setLogic(Logic.EQUAL);
		f.setValue(treeid);
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return demoDao.findAll(page);
	}

	@Transactional(readOnly = true)
	public DemoGroups getDemoGroup(String authGroupId) {
		return demoGroupsDao.get(authGroupId);
	}	
	
	@Transactional(readOnly = true)
	public DemoDetail getDemoDetail(String authGroupId,String authId) {
		return demoDao.get(authId);
	}
	
	public void saveDemoGroup(DemoGroups authgroups) {
		List<DemoDetail> auths=demoDao.findByProperty("authgroups.authGroupId", authgroups.getAuthGroupId());
	    for(DemoDetail auth:auths){
	    	demoDao.delete(auth);
		}
	    demoDao.getSession().flush();
		//一样的，因为Authority ManyToOne authgroups CascadeType.ALL
		//authgroupsDao.delete(authgroups.getAuthGroupId());
		//authgroupsDao.getSession().flush();		
		for(DemoDetail auth:authgroups.getAuths()){
			auth.setAuthgroups(authgroups);
			Dic_data state=new Dic_data();
			Dic_datakey key=new Dic_datakey();
			key.setDic_no("SATAE");
			key.setData_no("1");
			state.setKey(key);
			auth.setState(state);
			demoDao.save(auth);
		}		
	}

	public void deleteDemoGroup(String authGroupId) {
		DemoGroups authGroups = demoGroupsDao.get(authGroupId);
		demoGroupsDao.delete(authGroups);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemoGroupIdIdUnique(String authGroupId, String orgId) {
		return demoGroupsDao.isUnique("authGroupId", authGroupId, orgId);
	}
	@Transactional(readOnly = true)
	public boolean isDemoGroupNameUnique(String authGroupName, String oldName) {
		return demoGroupsDao.isUnique("displayName", authGroupName, oldName);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemoDetailIdUnique(String authId, String orgId) {
		return demoDao.isUnique("authId", authId, orgId);
	}
	
}


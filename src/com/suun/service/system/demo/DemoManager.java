package com.suun.service.system.demo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.demo.Demo;
import com.suun.publics.hibernate.Condition;
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
public class DemoManager {

	
	private SimpleHibernateTemplate<Demo, String> authDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		authDao = new SimpleHibernateTemplate<Demo, String>(sessionFactory, Demo.class);
	}
	
	@Transactional(readOnly = true)
	public Demo getDemo(String id) {
		return authDao.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<Demo> getAllDemos() {
		return authDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Demo> getAllDemos(Page<Demo> page) {
		return authDao.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<Demo> getAllDemos(Condition condition) {
		return authDao.findAll(condition);
	}

	public void saveDemo(Demo resource) {
		authDao.save(resource);
	}

	public void deleteDemo(String id) {
		Demo resource = authDao.get(id);
		authDao.delete(resource);
	}

	@Transactional(readOnly = true)
	public boolean isAthorityIdUnique(String authGroupId, String orgId) {
		return authDao.isUnique("authId", authGroupId, orgId);
	}
}


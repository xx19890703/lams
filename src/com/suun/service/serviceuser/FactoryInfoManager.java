package com.suun.service.serviceuser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.FactoryInfo;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.hibernate.FilterInfo.Logic;

/**
 * FactoryInfoManager
 * @author renlq
 *
 */
@Service
@Transactional
public class FactoryInfoManager {

	private SimpleHibernateTemplate<FactoryInfo, String> manager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<FactoryInfo, String>(sessionFactory, FactoryInfo.class);
	}

	@Transactional(readOnly = true)
	public FactoryInfo getFactoryInfo(String id) {
		return manager.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<FactoryInfo> getAllFactoryInfos() {
		return manager.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<FactoryInfo> getAllFactoryInfo(Page<FactoryInfo> page) {
		return manager.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<FactoryInfo> getAllFactoryInfo(Condition condition) {
		return manager.findAll(condition);
	}

	public void saveFactoryInfo(FactoryInfo resource) {
		manager.save(resource);
	}

	public void deleteFactoryInfo(String id) {
		FactoryInfo resource = manager.get(id);
		manager.delete(resource);
	}

	@Transactional(readOnly = true)
	public boolean isFnoUnique(String value, String oldvalue) {
		return manager.isUnique("fno", value, oldvalue);
	}
	
	@Transactional(readOnly = true)
	public boolean isFregisterUnique(String value, String oldvalue) {
		return manager.isUnique("fregister", value, oldvalue);
	}

	@Transactional(readOnly = true)
	public Page<FactoryInfo> getSelFactoryInfo(Page<FactoryInfo> page) {
		return manager.findByCriteria(page, Restrictions.eq("status.key.data_no", "1"));
	}
	
}

package com.suun.service.serviceuser;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.UpLoadRecord;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

/**
 * UpLoadRecordManager
 * @author renlq
 *
 */
@Service
@Transactional
public class UpLoadRecordManager {

	private SimpleHibernateTemplate<UpLoadRecord, Long> manager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<UpLoadRecord, Long>(sessionFactory, UpLoadRecord.class);
	}

	@Transactional(readOnly = true)
	public UpLoadRecord getUpLoadRecord(Long id) {
		return manager.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<UpLoadRecord> getAllUpLoadRecords() {
		return manager.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<UpLoadRecord> getAllUpLoadRecord(Page<UpLoadRecord> page) {
		return manager.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<UpLoadRecord> getAllUpLoadRecord(Condition condition) {
		return manager.findAll(condition);
	}

	public void saveUpLoadRecord(UpLoadRecord resource) {
		manager.save(resource);
	}

	public void deleteUpLoadRecord(Long id) {
		UpLoadRecord resource = manager.get(id);
		manager.delete(resource);
	}

}

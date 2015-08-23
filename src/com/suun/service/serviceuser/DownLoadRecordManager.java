package com.suun.service.serviceuser;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.DownLoadRecord;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

/**
 * DownLoadRecordManager
 * @author renlq
 *
 */
@Service
@Transactional
public class DownLoadRecordManager {

	private SimpleHibernateTemplate<DownLoadRecord, Long> manager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<DownLoadRecord, Long>(sessionFactory, DownLoadRecord.class);
	}

	@Transactional(readOnly = true)
	public DownLoadRecord getDownLoadRecord(Long id) {
		return manager.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<DownLoadRecord> getAllDownLoadRecords() {
		return manager.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<DownLoadRecord> getAllDownLoadRecord(Page<DownLoadRecord> page) {
		return manager.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<DownLoadRecord> getAllDownLoadRecord(Condition condition) {
		return manager.findAll(condition);
	}

	public void saveDownLoadRecord(DownLoadRecord resource) {
		manager.save(resource);
	}

	public void deleteDownLoadRecord(Long id) {
		DownLoadRecord resource = manager.get(id);
		manager.delete(resource);
	}

}

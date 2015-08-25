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
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.OrderBy;

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
	public Page<UpLoadRecord> getAllUpLoadRecord(Page<UpLoadRecord> page,String type) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("type");
		f.setLogic(Logic.EQUAL);
		f.setValue(type);
		condition.getFilterInfos().add(f);
		OrderBy ob = new OrderBy();
		ob.setFieldName("upTime");
		ob.setAsc(false);
		condition.getOrderBys().add(ob);
		page.setCondition(condition);
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

package com.suun.service.serviceuser;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.TemplateCfgUpLoad;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.OrderBy;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

/**
 * TemplateCfgUpLoadManager
 * @author renlq
 *
 */
@Service
@Transactional
public class TemplateCfgUpLoadManager {

	private SimpleHibernateTemplate<TemplateCfgUpLoad, Long> manager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<TemplateCfgUpLoad, Long>(sessionFactory, TemplateCfgUpLoad.class);
	}

	@Transactional(readOnly = true)
	public TemplateCfgUpLoad getTemplateCfgUpLoad(Long id) {
		return manager.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<TemplateCfgUpLoad> getAllTemplateCfgUpLoads() {
		return manager.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<TemplateCfgUpLoad> getAllTemplateCfgUpLoad(Page<TemplateCfgUpLoad> page) {
		Condition condition=page.getCondition();
		OrderBy ob = new OrderBy();
		ob.setFieldName("upTime");
		ob.setAsc(false);
		condition.getOrderBys().add(ob);
		page.setCondition(condition);
		return manager.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<TemplateCfgUpLoad> getAllTemplateCfgUpLoad(Condition condition) {
		return manager.findAll(condition);
	}

	public void saveTemplateCfgUpLoad(TemplateCfgUpLoad resource) {
		manager.save(resource);
	}

	public void deleteTemplateCfgUpLoad(Long id) {
		TemplateCfgUpLoad resource = manager.get(id);
		manager.delete(resource);
	}

}

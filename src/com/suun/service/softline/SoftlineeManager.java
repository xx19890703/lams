package com.suun.service.softline;

import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.softline.Softlinee;
import com.suun.model.system.Counting;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.system.CountService;


/**
 * 整个User模块内的业务逻辑Facade类.
 * 组合user,role,auth三者的DAO.
 * DAO均由SimpleHibernateTemplate指定泛型生成.
 * 
 */
@Service
@Transactional
public class SoftlineeManager {
	
	private SimpleHibernateTemplate<Softlinee, String> dao;
	private SimpleHibernateTemplate<Counting, String> cdao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new SimpleHibernateTemplate<Softlinee, String>(sessionFactory, Softlinee.class);
		cdao = new SimpleHibernateTemplate<Counting, String>(sessionFactory, Counting.class);
	}
	
	@Transactional(readOnly = true)
	public Softlinee getSoftlinee(String id) {
		return dao.get(id);
	}
	
	@Transactional(readOnly = true)
	public List<Softlinee> getAllSoftlinees() {
		return dao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Softlinee> getAllSoftlinees(Page<Softlinee> page) {
		return dao.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<Softlinee> getAllSoftlinees(Condition condition) {
		return dao.findAll(condition);
	}

	public void saveSoftlinee(boolean isnew,Softlinee resource) {
		if (isnew){
			@SuppressWarnings("deprecation")
			int cyear=new GregorianCalendar().getTime().getYear();
			Long year=CountService.GetCountingByCountNo("Softlinee-Year");
			Counting count=new Counting();
			if ((year==0)||(cyear!=year.intValue())){
				count.setObj_no("Softlinee");
				count.setObj_count((long) 1);
				cdao.save(count);
				Counting county=new Counting();
				county.setObj_no("Softlinee-Year");
				county.setObj_count(Long.valueOf(cyear));
				cdao.save(county);
		    } else {
		    	count.setObj_no("Softlinee");
				count.setObj_count(CountService.GetCountingByCountNo("Softlinee")+1);
				cdao.save(count);
		    }
		}				
		dao.save(resource);
	}

	public void deleteSoftlinee(String id) {
		Softlinee resource = dao.get(id);
		dao.delete(resource);
	}

	@Transactional(readOnly = true)
	public boolean isAthorityIdUnique(String baseId, String orgId) {
		return dao.isUnique("baseId", baseId, orgId);
	}
}


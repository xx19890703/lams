package com.suun.service.system.demo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.demo.Demotree;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
/**
 * DemotreeManager
 * @author lilei
 *
 */
@Service
@Transactional
public class DemotreeManager {
	private SimpleHibernateTemplate<Demotree, String> orgDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		orgDao = new SimpleHibernateTemplate<Demotree, String>(sessionFactory, Demotree.class);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemotreeIdUnique(String oid, String oldoid) {
		return orgDao.isUnique("oid", oid, oldoid);
	}
	
	@SuppressWarnings("unchecked")
	public List<Demotree> findOrgByParentId(String parentId){
		List<Demotree> list=new ArrayList<Demotree>();
		String hql="from Demotree o where o.pid=?";
		list=orgDao.find(hql, parentId);
		return list;
	}
	
	public boolean isDemotreeNameUnique(String parentId,String name ,String oldname){
		if (name == null || name.equals(oldname))
			return true;
		String hql="from Demotree o where o.pid=? and o.name=?";
		if (orgDao.findUnique(hql, parentId,name)==null){
			return true;
		} else
			return false;
	}
	
	@Transactional(readOnly = true)
	public Demotree getOrgzation(String orgid) {
		return orgDao.get(orgid);
	}
	
	public void deleteOrgzation(String orgid) {
		orgDao.delete(orgid);
	}
	
	public void saveOrgzation(Demotree org) {
		orgDao.save(org);
	}

}

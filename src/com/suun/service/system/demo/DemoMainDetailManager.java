package com.suun.service.system.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.demo.DemoDetailkey;
import com.suun.model.system.demo.Demomain;
import com.suun.model.system.demo.Demomaindetail;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

@Service
@Transactional
public class DemoMainDetailManager {

	private SimpleHibernateTemplate<Demomain, Long> demomainDao;
	
	private SimpleHibernateTemplate<Demomaindetail, DemoDetailkey> demomaindetailDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		demomainDao = new SimpleHibernateTemplate<Demomain, Long>(sessionFactory, Demomain.class);
		demomaindetailDao = new SimpleHibernateTemplate<Demomaindetail, DemoDetailkey>(sessionFactory, Demomaindetail.class);
	}

	@Transactional(readOnly = true)
	public List<Demomain> getAllDemomain() {
		return demomainDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Demomain> getAllDemomain(Long pid) {
		Condition condition=new Condition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("pmid");
		f.setLogic(Logic.EQUAL);
		f.setValue(pid);
		condition.getFilterInfos().add(f);
		return demomainDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<Demomain> getAllDemomain(Page<Demomain> page) {
		return demomainDao.findAll(page);		
	}
	
	@Transactional(readOnly = true)
	public List<Demomaindetail> getAllDemomaindetail(String treeid,Condition condition) {
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("mid");
		f.setLogic(Logic.EQUAL);
		f.setValue(Long.valueOf(treeid));
		condition.getFilterInfos().add(f);
		return demomaindetailDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<Demomaindetail> getAllDemomaindetail(String treeid,Page<Demomaindetail> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("mid");
		f.setLogic(Logic.EQUAL);
		f.setValue(StringUtils.isEmpty(treeid)?0:Long.valueOf(treeid));
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return demomaindetailDao.findAll(page);
	}

	@Transactional(readOnly = true)
	public Demomain getDemomain(Long mid) {
		return demomainDao.get(mid);
	}	
	
	@Transactional(readOnly = true)
	public Demomaindetail getDemomaindetail(Long mid,Long did) {
		DemoDetailkey key=new DemoDetailkey();
		key.setMid(mid);key.setDid(did);
		return demomaindetailDao.get(key);
	}
	private void deletesubDemomain(Long pid) {
		List<Demomain> subs= getAllDemomain(pid);
		for(Demomain sub:subs){
			deletesubDemomain(sub.getMid());
			Condition condition=new Condition();
			com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
			f.setFieldName("mid");
			f.setLogic(Logic.EQUAL);
			f.setValue(sub.getMid());
			List<com.suun.publics.hibernate.FilterInfo> filterInfos=new ArrayList<com.suun.publics.hibernate.FilterInfo>();
			condition.setFilterInfos(filterInfos);
			condition.getFilterInfos().add(f);
			List<Demomaindetail> demomaindetails=demomaindetailDao.findAll(condition);
			for(Demomaindetail demomaindetail:demomaindetails){
				demomaindetailDao.delete(demomaindetail);
			}
			demomainDao.delete(sub);
		}
	}
	@Transactional
	public void deleteDemomain(Long mid) {		
		deletesubDemomain(mid);
		Condition condition=new Condition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("mid");
		f.setLogic(Logic.EQUAL);
		f.setValue(mid);
		List<com.suun.publics.hibernate.FilterInfo> filterInfos=new ArrayList<com.suun.publics.hibernate.FilterInfo>();
		condition.setFilterInfos(filterInfos);
		condition.getFilterInfos().add(f);
		List<Demomaindetail> demomaindetails=demomaindetailDao.findAll(condition);
		for(Demomaindetail demomaindetail:demomaindetails){
			demomaindetailDao.delete(demomaindetail);
		}
		Demomain demomain = demomainDao.get(mid);
		demomainDao.delete(demomain);
	}
	
	@Transactional
	public void deleteDemomaindetail(Long mid,Long did) {
		Demomaindetail demomaindetail = this.getDemomaindetail(mid,did);
		demomaindetailDao.delete(demomaindetail);
	}
	

	public void saveDemomain(Demomain demomain) {
		if(demomain.getPmid()==null) demomain.setPmid(Long.valueOf(0));
		demomainDao.save(demomain);
	}

	public void saveDemomaindetail(Demomaindetail demomaindetail) {
		demomaindetailDao.save(demomaindetail);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemomainUnique(Long mid, Long oldId) {
		return demomainDao.isUnique("mid", mid, oldId);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemomainNameUnique(String mname, String oldmname) {
		return demomainDao.isUnique("mname", mname, oldmname);
	}
	
	@Transactional(readOnly = true)
	public boolean isDemomaindetailUnique(Long mid,Long did, Long olddid) {
		DemoDetailkey key=new DemoDetailkey();
		key.setMid(mid);key.setDid(did);
		DemoDetailkey oldkey=new DemoDetailkey();
		oldkey.setMid(mid);oldkey.setDid(olddid);
		return demomaindetailDao.isUnique("id", key, oldkey);
	}
	
}


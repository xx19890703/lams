package com.suun.service.system;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.Dic;
import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

/**
 * 字典表 manage
 * @author xx
 *
 */
@Service
@Transactional
public class DicManager {
	
	/**
	 * 基础信息父表dao
	 */
	private SimpleHibernateTemplate<Dic, String> dicDao;
	private SimpleHibernateTemplate<Dic_data, Dic_datakey> dicDataDao;
	
	/**
	 * 注入sessionFactory
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dicDao = new SimpleHibernateTemplate<Dic, String>(sessionFactory, Dic.class);
		dicDataDao = new SimpleHibernateTemplate<Dic_data, Dic_datakey>(sessionFactory, Dic_data.class);
	}
	
	@Transactional(readOnly = true)
	public List<Dic> findAllDicsNoSys(){	
		/*Condition condition=new Condition();
		List<com.suun.publics.hibernate.FilterInfo> filterInfos=new ArrayList<com.suun.publics.hibernate.FilterInfo>();
		com.suun.publics.hibernate.FilterInfo filter=new com.suun.publics.hibernate.FilterInfo();
		filter.setFieldName("issys");
		filter.setLogic(Logic.EQUAL);
		filter.setValue(0);
		filterInfos.add(filter);
		condition.setFilterInfos(filterInfos);		
		return dicDao.findAll(condition);*/
		return dicDao.findByProperty("issys", 0);
	}
	
	/**
	 * 根据dicno查询所有的字典列表
	 * @param dic_no
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Dic_data> findDicsByDicno(String dic_no){		
		return dicDataDao.findByProperty("key.dic_no", dic_no);
	}
	
	/**
	 * 根据dicno查询所有的字典列表
	 * @param dic_no
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Dic_data> findDicsByDicno(String dic_no,Page<Dic_data> page){
		Condition condition=page.getCondition();
		List<com.suun.publics.hibernate.FilterInfo> filterInfos=condition.getFilterInfos();
		com.suun.publics.hibernate.FilterInfo filter=new com.suun.publics.hibernate.FilterInfo();
		//Dic_datakey key=new Dic_datakey();
		//key.setDic_no(dic_no);
		filter.setFieldName("key.dic_no");
		filter.setLogic(Logic.EQUAL);
		filter.setValue(dic_no);
		filterInfos.add(filter);
		return  dicDataDao.findAll(page);
		/*
		Criteria c =this.sessionFactory.getCurrentSession().createCriteria(Dic_data.class);
		c.add(Restrictions.eq("key.dic_no", dic_no));
		page.setResult(c.list());
		return page; */
	}
	
	@Transactional(readOnly = true)
	public Dic_data getByKey(String dic_no,String data_no){
		Dic_datakey key=new Dic_datakey();
		key.setDic_no(dic_no);
		key.setData_no(data_no);
		return dicDataDao.findUniqueByProperty("key", key);
		//return dicDataDao.get(key);
	}
	
	public void save(Dic_data bean) {
		dicDataDao.save(bean);
	}
	
	public void delete(String dic_no,String data_no) {
		Dic_datakey key=new Dic_datakey();
		key.setDic_no(dic_no);
		key.setData_no(data_no);
		dicDataDao.delete(key);
	}
	
	@Transactional(readOnly = true)
	public boolean isIdUnique(String dic_no,String id, String oid) {
		Dic_datakey key=new Dic_datakey();
		key.setDic_no(dic_no);
		key.setData_no(id);
		Dic_datakey okey=new Dic_datakey();
		okey.setDic_no(dic_no);
		okey.setData_no(oid);
		return dicDataDao.isUnique("key", key, okey);
	}
	
	
}

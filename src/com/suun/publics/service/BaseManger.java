package com.suun.publics.service;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.utils.BeanUtils;


@Transactional
public abstract class BaseManger<TEntity,TKey extends Serializable> {
	
	private SimpleHibernateTemplate<TEntity, TKey> dao = null; 			
	
	public abstract SessionFactory getSessionFactory();	
	
	@SuppressWarnings("unchecked")
	public SimpleHibernateTemplate<TEntity, TKey> getDao(){
		if (dao==null){
			dao =new SimpleHibernateTemplate<TEntity,TKey>(getSessionFactory(),(Class<TEntity>)BeanUtils.getSuperGenericType(getClass()));
		}
		return dao;
	}
		
	@Transactional(readOnly = true)
	public Page<TEntity> getAllEmployee(Page<TEntity> page) {
		return getDao().findAll(page);
	}
	
	@Transactional(readOnly = true)
	public boolean isIdUnique(String id,String oldid) {
		return getDao().isIdUnique(id, oldid);
	}
	
	public void saveRecordSet(HttpServletRequest request,TEntity entity) {
		getDao().save(entity);		
	}
	
	@Transactional(readOnly = true)
	public Page<TEntity> getAll(Page<TEntity> page){
		return getDao().findAll(page);
	}
	
	@Transactional(readOnly = true)
	public List<TEntity> getAll(Condition condition){
		return getDao().findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public TEntity get(TKey id){
		return getDao().get(id);
	}
	
	public void delete(TKey[] ids){
		for(TKey id:ids){
			getDao().delete(id);
		}
	}
}

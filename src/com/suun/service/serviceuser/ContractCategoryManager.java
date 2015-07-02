package com.suun.service.serviceuser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.ContractCategory;
import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

/**
 * TemplateResManager
 * @author renlq
 *
 */
@Service
@Transactional
public class ContractCategoryManager {

	private SimpleHibernateTemplate<ContractCategory, String> manager;
	private SimpleHibernateTemplate<ContractDetail, String> submanager;
	private SimpleHibernateTemplate<ContractTemplateRes, Long> conmanager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<ContractCategory, String>(sessionFactory, ContractCategory.class);
		submanager = new SimpleHibernateTemplate<ContractDetail, String>(sessionFactory, ContractDetail.class);
		conmanager = new SimpleHibernateTemplate<ContractTemplateRes, Long>(sessionFactory, ContractTemplateRes.class);
	}
	
	@Transactional(readOnly = true)
	public boolean isContractCategoryNameUnique(String parentId,String name ,String oldname){
		if (name == null || name.equals(oldname))
			return true;
		String hql="from ContractCategory o where o.pid=? and o.name=?";
		if (manager.findUnique(hql, parentId,name)==null){
			return true;
		} else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ContractCategory> findContractCategoryByParentId(String parentId){
		List<ContractCategory> list=new ArrayList<ContractCategory>();
		String hql="from ContractCategory o where o.pid=?";
		list=manager.find(hql, parentId);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ContractDetail> findContractDetailByParentId(String parentId){
		List<ContractDetail> list=new ArrayList<ContractDetail>();
		String hql="from ContractDetail o where o.conmain.did=?";
		list=manager.find(hql, parentId);
		return list;
	}
	
	@Transactional(readOnly = true)
	public ContractCategory getContractCategory(String did) {
		return manager.get(did);
	}
	
	public void deleteContractCategory(String depid) {
		Condition condition=new Condition();
		List<FilterInfo> filterInfos=new ArrayList<FilterInfo>();
		condition.setFilterInfos(filterInfos);
		List<ContractDetail> templateRes=getAllContractCategory(depid,condition);
		for(ContractDetail res : templateRes){
			submanager.delete(res);
		}
		manager.delete(depid);
	}
	
	@Transactional(readOnly = true)
	public List<ContractDetail> getAllContractCategory(String id,Condition condition){
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("did");
		f.setLogic(Logic.EQUAL);
		f.setValue(id);
		condition.getFilterInfos().add(f);
		return submanager.findAll(condition);
	}
	
	public void saveContractCategory(ContractCategory dep) {
		manager.save(dep);
	}
	
	@Transactional(readOnly = true)
	public Page<ContractDetail> getAllContractDetail(String treeid,Page<ContractDetail> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("conmain.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(StringUtils.isEmpty(treeid)?"0":treeid);
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return submanager.findAll(page);
	}
	
	public void saveContractDetail(ContractDetail sub) {
//		List<TemplateResContent> auths=conmanager.findByProperty("resdetail.did", sub.getDid());
//	    for(TemplateResContent auth:auths){
//	    	conmanager.delete(auth);
//		}
//	    conmanager.getSession().flush();
		//一样的，因为Authority ManyToOne authgroups CascadeType.ALL
		//authgroupsDao.delete(authgroups.getAuthGroupId());
		//authgroupsDao.getSession().flush();		
		for(ContractTemplateRes auth : sub.getRescontent()){
			auth.setCondetail(sub);
			conmanager.save(auth);
		}	
		submanager.save(sub);
	}
	
	public void deleteContractDetail(String mid,String did) {
		String hql="delete ContractDetail o where o.conmain.did=? and o.did=?";
		submanager.executeHql(hql,mid,did);
	}
	
	public void deleteTemplateResDetail(String id) {
		submanager.delete(id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public ContractDetail getContractDetail(String mainId,String subId){
		List<ContractDetail> list=new ArrayList<ContractDetail>();
		String hql="from ContractDetail o where o.conmain.did=? and o.did=?";
		list=submanager.find(hql,mainId,subId);
		if (list.size()>0){
		    return list.get(0);
		}else{
			return null;
		}
	}

}

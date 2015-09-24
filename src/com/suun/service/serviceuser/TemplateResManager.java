package com.suun.service.serviceuser;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.TemplateRes;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
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
public class TemplateResManager {

	private SimpleHibernateTemplate<TemplateRes, String> manager;
	private SimpleHibernateTemplate<TemplateResDetail, String> submanager;
	private SimpleHibernateTemplate<TemplateResContent, String> conmanager;
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<TemplateRes, String>(sessionFactory, TemplateRes.class);
		submanager = new SimpleHibernateTemplate<TemplateResDetail, String>(sessionFactory, TemplateResDetail.class);
		conmanager = new SimpleHibernateTemplate<TemplateResContent, String>(sessionFactory, TemplateResContent.class);
		this.sessionFactory=sessionFactory;		
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Transactional(readOnly = true)
	public boolean isDTemplateResIdUnique(String did, String olddid) {
		return manager.isUnique("did", did, olddid);
	}
	
	@Transactional(readOnly = true)
	public boolean isTemplateResNameUnique(String parentId,String name ,String oldname){
		if (name == null || name.equals(oldname))
			return true;
		String hql="from TemplateRes o where o.pid=? and o.name=?";
		if (manager.findUnique(hql, parentId,name)==null){
			return true;
		} else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<TemplateRes> findTemplateResByParentId(String parentId){
		List<TemplateRes> list=new ArrayList<TemplateRes>();
		String hql="from TemplateRes o where o.pid=?";
		list=manager.find(hql, parentId);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<TemplateResDetail> findTemplateResDetailByParentId(String parentId){
		List<TemplateResDetail> list=new ArrayList<TemplateResDetail>();
		String hql="from TemplateResDetail o where o.resmain.did=?";
		list=manager.find(hql, parentId);
		return list;
	}
	
	@Transactional(readOnly = true)
	public TemplateRes getTemplateRes(String depid) {
		return manager.get1(depid);
	}
	
	@Transactional(readOnly = true)
	public TemplateResDetail getTemplateResDetail(String detailId){
		return submanager.get1(detailId);
	}
	
	@Transactional(readOnly = true)
	public TemplateResContent getTemplateResContent(String id){
		return conmanager.get1(id);
	}
	
	public void deleteTemplateRes(String depid) {
		Condition condition=new Condition();
		List<FilterInfo> filterInfos=new ArrayList<FilterInfo>();
		condition.setFilterInfos(filterInfos);
		List<TemplateResDetail> templateRes=getAllTemplateRes(depid,condition);
		for(TemplateResDetail res : templateRes){
			submanager.delete(res);
		}
		manager.delete(depid);
	}
	
	@Transactional(readOnly = true)
	public List<TemplateResDetail> getAllTemplateRes(String id,Condition condition){
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("resmain.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(id);
		condition.getFilterInfos().add(f);
		return submanager.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<TemplateResDetail> getSelTemplateResDetail(String treeid,Page<TemplateResDetail> page) {
		return submanager.findByCriteria(page, Restrictions.eq("state.key.data_no", "1"),Restrictions.eq("resmain.did", treeid));
	}
	
	@Transactional(readOnly = true)
	public Page<TemplateResDetail> getAllTemplateResDetail(String treeid,Page<TemplateResDetail> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("resmain.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(StringUtils.isEmpty(treeid)?"0":treeid);
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return submanager.findAll(page);
	}
	
	public void saveTemplateRes(TemplateRes dep) {
		manager.save(dep);
	}
	
	public void saveTemplateResDetail(TemplateResDetail sub) {
//		for(TemplateResContent content : sub.getRescontent()){
//			content.setResdetail(sub);
//			conmanager.save(content);
//		}	
		submanager.save(sub);
	}
	
	public void saveTemplateResContent(TemplateResContent sub) {
		conmanager.save(sub);
	}
	
	//删除所有模板
	public void deleteTemplateResDetail(String mid,String did) {
		String hql="delete TemplateResDetail o where o.resmain.did=? and o.did=?";
		submanager.executeHql(hql,mid,did);
	}
	
	//删除所有下的数据表
	public void deleteTemplateResContent(String did) {
		String hql="delete TemplateResContent o where o.resdetail.did=?";
		submanager.executeHql(hql,did);
	}
	
	public void deleteTemplateResDetail(String id) {
		submanager.delete(id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public TemplateResDetail getTemplateResDetail(String mainId,String subId){
		List<TemplateResDetail> list=new ArrayList<TemplateResDetail>();
		String hql="from TemplateResDetail o where o.resmain.did=? and o.did=?";
		list=submanager.find(hql,mainId,subId);
		if (list.size()>0){
		    return list.get(0);
		}else{
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isGridDidUnique(String did, String olddid) {
		return submanager.isUnique("did", did, olddid);
	}
	
	@Transactional(readOnly = true)
	public boolean isGridNameUnique(String did, String olddid) {
		return submanager.isUnique("name", did, olddid);
	}
	
	//判断表(name)在数据库中是否存在
	@Transactional(readOnly = true)
	public boolean isExistTable(String name) {
		Connection conn;
		try {
			conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
			DatabaseMetaData  dm =conn.getMetaData();
			ResultSet rs = dm.getTables(conn.getCatalog(), "root", name, new String[]{"TABLE"});
			boolean flag = rs.next();
			return flag;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

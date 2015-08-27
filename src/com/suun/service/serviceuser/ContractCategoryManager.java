package com.suun.service.serviceuser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.contract.ContractModePK;
import com.suun.model.contract.ContractTemplatePK;
import com.suun.model.contract.Contract_mode;
import com.suun.model.contract.Contract_template;
import com.suun.model.serviceuser.ContractCategory;
import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
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
public class ContractCategoryManager {

	private SimpleHibernateTemplate<ContractCategory, String> manager;
	private SimpleHibernateTemplate<ContractDetail, String> submanager;
	private SimpleHibernateTemplate<ContractTemplateRes, Long> conmanager;
	private SimpleHibernateTemplate<Contract_mode, ContractModePK> contractmode;
	private SimpleHibernateTemplate<Contract_template, ContractTemplatePK> contracttemplate;
	
	private SimpleHibernateTemplate<TemplateResDetail, String> templatemanager;
	private SimpleHibernateTemplate<TemplateResContent, String> templatecmanager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		manager = new SimpleHibernateTemplate<ContractCategory, String>(sessionFactory, ContractCategory.class);
		submanager = new SimpleHibernateTemplate<ContractDetail, String>(sessionFactory, ContractDetail.class);
		conmanager = new SimpleHibernateTemplate<ContractTemplateRes, Long>(sessionFactory, ContractTemplateRes.class);
		contractmode = new SimpleHibernateTemplate<Contract_mode, ContractModePK>(sessionFactory, Contract_mode.class);
		contracttemplate = new SimpleHibernateTemplate<Contract_template, ContractTemplatePK>(sessionFactory, Contract_template.class);
		
		templatemanager = new SimpleHibernateTemplate<TemplateResDetail, String>(sessionFactory, TemplateResDetail.class);
		templatecmanager = new SimpleHibernateTemplate<TemplateResContent, String>(sessionFactory, TemplateResContent.class);
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
	

	@Transactional(readOnly = true)
	public ContractDetail getContractDetail(String did) {
		return submanager.get(did);
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
	
	@SuppressWarnings("unchecked")
	public List<ContractDetail> getAllContractDetails(String zzcid,String htmc,String htbh,String ssdd){
		String hql="from ContractDetail o where o.name like ? and o.did like ? and o.orderinfo like ? and o.finfo.fno like ?";
		List<ContractDetail> list=submanager.find(hql,"%"+htmc+"%","%"+htbh+"%","%"+ssdd+"%" ,"%"+zzcid+"%");
		return list;
	}
	
	public void saveContractCategory(ContractCategory dep) {
		manager.save(dep);
	}
	
	public void deleteContractTemplateRes(String pid) {
		String hql="delete ContractTemplateRes o where o.condetail.did=? ";
		conmanager.executeHql(hql,pid);
	}
	
	public void deleteContractDetails(String did) {
		String hql="delete ContractDetail o where o.did=? ";
		submanager.executeHql(hql,did);
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
	    // 保存子表
		for(ContractTemplateRes temp : sub.getRescontent()){
			List<TemplateResContent> ress = templatecmanager.findByProperty("resdetail.did", temp.getTemplate().getDid());
			for(TemplateResContent res : ress){
				ContractModePK modepk = new ContractModePK();
				modepk.setContractId(sub.getDid());
				modepk.setTableName(res.getName());
				Contract_mode mode = new Contract_mode();
				mode.setId(modepk);
				contractmode.save((Contract_mode)contractmode.getSession().merge(mode));
			}
			ContractTemplatePK templatepk = new ContractTemplatePK();
			templatepk.setContractId(sub.getDid());
			templatepk.setTemplateUrl(templatemanager.get(temp.getTemplate().getDid()).getPath());
			Contract_template template = new Contract_template();
			template.setId(templatepk);
			template.setTemplateName(templatemanager.get(temp.getTemplate().getDid()).getName());
			template.setOpenType(temp.getOpenType());
			contracttemplate.save((Contract_template)contracttemplate.getSession().merge(template));
			
			temp.setCondetail(sub);
			conmanager.save(temp);
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

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public ContractDetail getContractDetailByContractId(String subId){
		List<ContractDetail> list=new ArrayList<ContractDetail>();
		String hql="from ContractDetail o where  o.did=?";
		list=submanager.find(hql,subId);
		if (list.size()>0){
		    return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 *  根据 模版url 查询合同
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Contract_template findContract_templateByTemplateUrl(String url){
		List<Contract_template> list=new ArrayList<Contract_template>();
		String hql="from Contract_template o where o.id.templateUrl=? order by o.id.contractId desc";
		list=contracttemplate.find(hql,url);
		if (list.size()>0){
		    return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据合同id 查询所有模版名
	 * @param cid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contract_template> findContract_templateByContractId(String cid){
		List<Contract_template> list=new ArrayList<Contract_template>();
		String hql="from Contract_template o where o.id.contractId=?";
		list=contracttemplate.find(hql,cid);
		return list;
	}
	
	/**
	 * 根据合同id 查询所有相关表名
	 * @param cid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contract_mode> findContract_modeByContractId(String cid){
		List<Contract_mode> list=new ArrayList<Contract_mode>();
		String hql="from Contract_mode o where o.id.contractId=?";
		list=contractmode.find(hql,cid);
		return list;
	}
	
	/**
	 * 查询所有的合同id
	 * @return
	 */
	public List<String> findContract(){
		List<Contract_mode> cms = contractmode.findAll();
		Set<String> sets = new HashSet<String>();
		for(Contract_mode cm:cms){
			sets.add(cm.getId().getContractId());
		}
		return new ArrayList<String>(sets);
	}
}

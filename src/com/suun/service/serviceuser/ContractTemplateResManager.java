package com.suun.service.serviceuser;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

@Service
@Transactional
public class ContractTemplateResManager{

	private SimpleHibernateTemplate<ContractTemplateRes, Long> conmanager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		conmanager = new SimpleHibernateTemplate<ContractTemplateRes, Long>(sessionFactory, ContractTemplateRes.class);
	}

	@Transactional(readOnly = true)
	public Page<ContractTemplateRes> getContractTemplateResByContractId(String contractId,Page<ContractTemplateRes> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("condetail.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(contractId);
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return conmanager.findAll(page);
	}
	
}

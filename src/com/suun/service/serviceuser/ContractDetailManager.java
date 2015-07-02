package com.suun.service.serviceuser;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.serviceuser.ContractDetail;
import com.suun.publics.service.BaseManger;

/**
 * TemplateResManager
 * @author renlq
 *
 */
@Service
@Transactional
public class ContractDetailManager extends BaseManger<ContractDetail, String>{

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;		
	}	

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}

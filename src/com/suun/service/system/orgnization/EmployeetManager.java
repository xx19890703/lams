package com.suun.service.system.orgnization;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.orgnization.Employee;
import com.suun.publics.service.BaseManger;
/**
 * EmployeetManager
 * @author 
 *
 */
@Service
@Transactional
public class EmployeetManager extends BaseManger<Employee, String> {
	
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

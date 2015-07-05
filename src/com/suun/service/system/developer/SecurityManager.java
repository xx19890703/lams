package com.suun.service.system.developer;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.service.system.springsecurity.Resource;


/**
 * 整个User模块内的业务逻辑Facade类.
 * 组合user,role,auth三者的DAO.
 * DAO均由SimpleHibernateTemplate指定泛型生成.
 * 
 */
@Service
@Transactional
public class SecurityManager {

	private SimpleHibernateTemplate<com.suun.model.system.developer.Function, String> functionDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		functionDao = new SimpleHibernateTemplate<com.suun.model.system.developer.Function, String>(sessionFactory, com.suun.model.system.developer.Function.class);
	}
	
	@Transactional(readOnly = true)
	public com.suun.model.system.developer.Function getResource(String id) {
		return functionDao.get(id);
	}
	
	
	@Transactional(readOnly = true)
	public List<Resource> getAllResources() {
		List<com.suun.model.system.developer.Function> functions= functionDao.findAll();
		List<Resource> resources=new ArrayList<Resource>();
		for(com.suun.model.system.developer.Function fun:functions){
			Resource resource=new Resource();
			String[] urls=fun.getUrl().split(",");
			for (String url:urls){
				resource.setPath(url);
			    resource.setAuth(fun.getFunId());
			    resources.add(resource);
			}			
		}
		return resources;
	}
	
}


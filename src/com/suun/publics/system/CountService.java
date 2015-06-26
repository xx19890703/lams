package com.suun.publics.system;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.suun.model.system.Counting;

public class CountService{
 
	private static SessionFactory getSessionFactory(){
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		return (SessionFactory) ctx.getBean("sessionFactory");
	};
	
	@SuppressWarnings("unchecked")
	public static Long GetCountingByCountNo(String countno){
		Session session=getSessionFactory().openSession();
		Criterion c=Restrictions.eq("obj_no", countno);
		List<Counting> countings=session.createCriteria(Counting.class).add(c).list();
		session.close();
		if (countings.size()>0){
			return countings.get(0).getObj_count();
		} else
		    return (long) 0;
	}
  
}

package com.suun.service.system.security;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.security.Role;
import com.suun.model.system.security.User;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.hibernate.FilterInfo.Logic;


/**
 * 整个User模块内的业务逻辑Facade�?
 * 组合user,role,Function三者的DAO.
 * DAO均由SimpleHibernateTemplate指定泛型生成.
 * 
 */
@Service
@Transactional
public class UserManager {

	private SimpleHibernateTemplate<User, String> userDao;	

	private SimpleHibernateTemplate<Role, String> roleDao;
	
	private SimpleHibernateTemplate<com.suun.model.system.developer.Function, String> funDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDao = new SimpleHibernateTemplate<User, String>(sessionFactory, User.class);
		roleDao = new SimpleHibernateTemplate<Role, String>(sessionFactory, Role.class);
		funDao = new SimpleHibernateTemplate<com.suun.model.system.developer.Function, String>(sessionFactory, com.suun.model.system.developer.Function.class);	
	}
	
	private List<com.suun.publics.hibernate.FilterInfo> newFilterInfo(List<com.suun.publics.hibernate.FilterInfo> filterInfos){
		List<com.suun.publics.hibernate.FilterInfo> nfilterInfos=new ArrayList<com.suun.publics.hibernate.FilterInfo>();
	    for(com.suun.publics.hibernate.FilterInfo filterInfo:filterInfos){
	    	if (filterInfo.getFieldName().equals("stateName")){
	    		filterInfo.setFieldName("state");
	    		if (filterInfo.getLogic()==Logic.LIKE){
	    			filterInfo.setLogic(Logic.EQUAL);
	    			if (filterInfo.getValue().equals("使")||
	    					filterInfo.getValue().equals("用")||
	    					filterInfo.getValue().equals("使用")){
	    				filterInfo.setValue(1);
	    			} else if (filterInfo.getValue().equals("作")||
		    					filterInfo.getValue().equals("废")||
		    					filterInfo.getValue().equals("作废")){
		    				filterInfo.setValue(0);
		    		} else {
	    				filterInfo.setValue(-1);
	    			}
	    		} else {
	    			if (filterInfo.getValue().toString().equals("使用")){
	    				filterInfo.setValue(1);
	    			} else if (filterInfo.getValue().equals("作废")){
		    				filterInfo.setValue(0);
		    		} else {
	    				filterInfo.setValue(-1);
	    			}
	    		}
	    		
	    	} 
	    	nfilterInfos.add(filterInfo);
	    }
		return nfilterInfos;
		
	};
	 
	@Transactional(readOnly = true)
	public User getUser(String userId) {	
		return userDao.get(userId);
	}
	
	@Transactional(readOnly = true)
	public List<User> getAllUsers(Condition condition) {
		List<com.suun.publics.hibernate.FilterInfo> nfilterInfos=newFilterInfo(condition.getFilterInfos());	
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("userId");
		f.setLogic(Logic.NOTEQUAL);
		f.setValue("admin");
		nfilterInfos.add(f);
		condition.setFilterInfos(nfilterInfos);
		return userDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public Page<User> getAllUsers(Page<User> page) {
	    List<com.suun.publics.hibernate.FilterInfo> nfilterInfos=newFilterInfo(page.getCondition().getFilterInfos());	    
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("userId");
		f.setLogic(Logic.NOTEQUAL);
		f.setValue("admin");
		nfilterInfos.add(f);
		page.getCondition().setFilterInfos(nfilterInfos);
		return userDao.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public User getUserByUserId(String userId) {
		User user=userDao.findUniqueByProperty("userId", userId);
		for(Role role:user.getRoles()){
			role.getAuths().size();//本来角色加载权限的方式是lazy，在这里写这句话的意思是加载角色下的权限信息。
		}
		return user;
	}

	@Transactional(readOnly = true)
	public User findUserByLoginName(String username) {
		return userDao.findUniqueByProperty("userName", username);
	}
	
	public void saveUser(User user) {
		userDao.save(user);
	}
	
	public void deleteUser(String userId) {
		User user = userDao.get(userId);
		userDao.delete(user);
	}

	@Transactional(readOnly = true)
	public boolean isUserIdUnique(String userId, String orguserId) {
		return userDao.isUnique("userId", userId, orguserId);
	}

	@Transactional(readOnly = true)
	public List<Role> getAllRoles(Condition condition) {
		condition.setFilterInfos(newFilterInfo(condition.getFilterInfos()));
		return roleDao.findAll(condition);
	}
	
	@Transactional(readOnly = true)
	public List<Role> getAllRoles() {
		return roleDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Role> getAllRoles(Page<Role> page) {
		page.getCondition().setFilterInfos(newFilterInfo(page.getCondition().getFilterInfos()));
		return roleDao.findAll(page);
	}
	
	@Transactional(readOnly = true)
	public Role getRole(String roleId) {
		return roleDao.get(roleId);
	}
	@Transactional(readOnly = true)
	public Role getRoleByRoleId(String roleId) {
		return roleDao.findUniqueByProperty("roleId", roleId);
	}
	public void saveRole(Role role) {
		roleDao.save(role);
	}

	public void deleteRole(String roleId) {
		Role role = roleDao.get(roleId);
		roleDao.delete(role);
	}
	
	@Transactional(readOnly = true)
	public boolean isRoleIdUnique(String roleId, String orgroleId) {
		return roleDao.isUnique("roleId", roleId, orgroleId);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllAuths() {
		List<String> Auths=new ArrayList<String>();
		List<com.suun.model.system.developer.Function> funs=funDao.findAll();
		for (com.suun.model.system.developer.Function fun:funs){
			Auths.add(fun.getFunId());
		}
		return Auths;
	}
		
}


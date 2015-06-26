package com.suun.controller.system.security;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.security.Role;
import com.suun.model.system.security.User;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;

/**
 * @author zyfang
 * 
 */
@Controller 
public class UserController extends BaseCRUDController<User>{
	
	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.system.security.UserManager manager; 
			
	@RequestMapping
	@ResponseBody
	public String validateUserId(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("roleId");
		String oldid = request.getParameter("oldid");
		if (manager.isRoleIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@Override
	public Page<User> getPageRecords(HttpServletRequest request,Page<User> page) {
		return manager.getAllUsers(page);
	}

	@Override
	public List<User> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllUsers(condition);
	}

	@Override
	public User getRecordSet(HttpServletRequest request,String operateid) {	
		return manager.getUser(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteUser(ids[i]);
		    }
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,User operatebean) {
		try{
			String[] roles=request.getParameterValues("roles.roleId");	
			if (roles!=null){
				for(String roleId:roles){
					Role role=manager.getRole(roleId);
					operatebean.getRoles().add(role);
				}
			}			
			manager.saveUser(operatebean);
			return null;
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}
	@Override
	protected Map<String,Object> doNewbefore(HttpServletRequest request) {
		return getAllRoles(manager);
	}
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,User operatebean) {
		Map<String,Object> model=getAllRoles(manager);
		model.put("roleids", operatebean.getIdlist());
		return model;
	}
	
	private Map<String,Object> getAllRoles(com.suun.service.system.security.UserManager manager) {
		Map<String,Object> model=new HashMap<String,Object>();
		Condition condition=new Condition();
		List<FilterInfo> filterInfos=new ArrayList<FilterInfo>();
		FilterInfo filterInfo=new FilterInfo();
		filterInfo.setFieldName("state");
		filterInfo.setLogic(Logic.EQUAL);
		filterInfo.setValue(1);
		filterInfos.add(filterInfo);
		condition.setFilterInfos(filterInfos);
		model.put("allRoles", manager.getAllRoles(condition));
		return model;
	}
	
}

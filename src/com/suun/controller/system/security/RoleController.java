package com.suun.controller.system.security;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.developer.Function;
import com.suun.model.system.security.Role;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.TreeObj;

/**
 * @author zyfang
 * 
 */
@Controller 
public class RoleController extends BaseCRUDController<Role>{
	
	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.system.security.UserManager manager; 
	
	@Autowired
	com.suun.service.system.developer.MenuManager menumanager;
	/*
	@Override
	public Map<String,Object> doNewbefore(HttpServletRequest request) {
		Map<String,String> AuthMap = new LinkedHashMap<String,String>();
		for (String auth:menumanager.getAllAuths()){
			AuthMap.put(auth, "");			
		}
		Map<String,Object> objMap = new LinkedHashMap<String,Object>();
		objMap.put("allAuthsMap", AuthMap);
		return objMap;
	}*/
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,Role operatebean) {
		String auths="";
		for(Function func:operatebean.getAuths()){
			auths=auths.concat(";".concat(func.getMenuId().concat(",").concat(func.getFunId())));
		}
		if (auths.length()>0) auths=auths.substring(1);
		Map<String,Object> objMap = new LinkedHashMap<String,Object>();
		objMap.put("checkedAuthIds", auths);
		return objMap;
	}
			
	@RequestMapping
	@ResponseBody
	public String validateRoleId(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("roleId");
		String oldid = request.getParameter("oldid");
		if (manager.isRoleIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@Override
	public Page<Role> getPageRecords(HttpServletRequest request,Page<Role> page) {
		return manager.getAllRoles(page);
	}

	@Override
	public List<Role> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllRoles(condition);
	}

	@Override
	public Role getRecordSet(HttpServletRequest request,String operateid) {
		return manager.getRole(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteRole(ids[i]);
		    }
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,Role operatebean) {
		try{
			String rauths=request.getParameter("checkedAuthIds");
			if (!StringUtils.isEmpty(rauths)){
				String[] funids=rauths.split(";");
				Set<Function> funcs=new HashSet<Function>();
				for (String funid:funids){
					funcs.add(menumanager.getFunction(funid.split(",")[1]));
				}
				operatebean.setAuths(funcs);
			}
			manager.saveRole(operatebean);
			return null;
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}
	
	@RequestMapping
	@ResponseBody
	public List<TreeObj> getAuthTree(HttpServletRequest request){
		String node=request.getParameter("groupId");
		if (node==null){
			node="0";
		}
		return menumanager.getAuthTree(node,request);
	}
	
}

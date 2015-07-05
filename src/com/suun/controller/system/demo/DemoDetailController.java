package com.suun.controller.system.demo;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.demo.DemoGroups;
import com.suun.publics.controller.BaseDetailCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;

/**
 * @author zyfang
 * 
 */
@Controller 
public class DemoDetailController extends BaseDetailCRUDController<DemoGroups>{

	@Autowired
	com.suun.service.system.demo.DemoDetailManager demoDetailManager; 
	
	/*@RequestMapping
	@Override
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response) {	
		Authority authority=new Authority();
		authority.setAuthId("AUTH_ALL");
		return new ModelAndView(getBasePath(request)+"input","authority",authority);	
	}*/
	
	@RequestMapping
	@ResponseBody
	public String validateAuthority(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("authGroupId");
		String oldid = request.getParameter("oldid");
		if (demoDetailManager.isDemoGroupIdIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@RequestMapping
	@ResponseBody
	public String validateAuthorityDetail(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("auths[0].authId");
		String oldid = request.getParameter("oldid");
		if (demoDetailManager.isDemoDetailIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@Override
	public Page<DemoGroups> getPageRecords(HttpServletRequest request,Page<DemoGroups> page) {
		return demoDetailManager.getAllDemoGroups(page);
	}

	@Override
	public List<DemoGroups> getAllRecords(HttpServletRequest request,Condition condition) {
		return demoDetailManager.getAllDemoGroups(condition);
	}

	@Override
	public DemoGroups getRecordSet(HttpServletRequest request,String operateid) {
		return demoDetailManager.getDemoGroup(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	demoDetailManager.deleteDemoGroup(ids[i]);
		    }
			return "";
		} catch (Exception e){
			String message=e.toString();
			if (e.getMessage()!=null){
				message=e.getMessage();
			}
			logger.error(message);
			return message;
		}
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,DemoGroups operatebean) {
		try{
			demoDetailManager.saveDemoGroup(operatebean);
			return null;
		} catch (Exception e){
			String message=e.toString();
			if (e.getMessage()!=null){
				message=e.getMessage();
			}
			logger.error(message);
			return message;
		}
	}
}

package com.suun.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suun.model.system.developer.Menu;
import com.suun.publics.controller.BaseController;
import com.suun.publics.system.SystemService;
import com.suun.publics.utils.Utils;

@Controller 
public class MainController extends BaseController{
	
	@Autowired
	com.suun.service.system.security.UserManager manager; 
	
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		String loginName="超级管理员";
		if (!Utils.getLoginUserId().equals("admin")){
			loginName=manager.getUser(Utils.getLoginUserId()).getEmployee().getEmployeename(); 
		}		 
		modelandview.getModel().put("loginName",loginName);
		Utils.getLoginUserauth();
		modelandview.getModel().put("topMenu",Utils.Object2Json(SystemService.getTopMenu(Utils.getLoginUserId())));	
		return modelandview;
	}
	
	@RequestMapping
    @ResponseBody
	public String classmenu(HttpServletRequest request,HttpServletResponse response) {	
		String menuParid=request.getParameter("topMenuId");
		List<Menu> cmenu=new ArrayList<Menu>();
		for(Menu menu:SystemService.getClassMenu(Utils.getLoginUserId())){
			if (menu.getMenuParid().equals(menuParid)){
				cmenu.add(menu);
			}
		}
		return renderText(response,Utils.Object2Json(cmenu));
	}
	
	@RequestMapping
    @ResponseBody
	public String treemenu(HttpServletRequest request,HttpServletResponse response) {	
		String menuParid=request.getParameter("classMenuId");
		return renderText(response,Utils.Object2Json(SystemService.getTreeMenu(Utils.getLoginUserId()).get(menuParid)));
	}

}

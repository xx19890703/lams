package com.suun.controller.system.security;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.publics.controller.BaseController;
import com.suun.publics.jCaptcha.CaptchaService;
import com.suun.publics.utils.Utils;
import com.suun.service.system.security.UserManager;


/**
 * @author zyfang
 * 
 */
@Controller 
public class SecurityController extends BaseController{
	
	@Autowired  
	SessionRegistry sessionRegistry; 
	@Autowired 
	UserManager manager;

	@RequestMapping
	@ResponseBody
	public List<String> onlineuser(HttpServletRequest request,
			HttpServletResponse response) {			
		List<String> onlineusers=new ArrayList<String>();
		for(Object principal:sessionRegistry.getAllPrincipals()){
			if (principal instanceof User) {
				User user = (User) principal;
				onlineusers.add(user.getUsername());
			}
		}
		return onlineusers;
	}
		
	/*验证码	*/
	@RequestMapping
	public void imageCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		  CaptchaService.generateImageCode(request, response);		 
		  return ; 
	}
	/*AJAX校验码验证*/
	@RequestMapping
	@ResponseBody
	public String validateCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		if (CaptchaService.isImageCodeCorrect(request,
				request.getParameter("j_captcha_response"))){   
		    return renderText(response,"true");
		}else
			return renderText(response,"false");
	}
	@RequestMapping
	@ResponseBody
	public String validateOldPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String oldpass = request.getParameter("oldPswd");
		
		com.suun.model.system.security.User entity=manager.getUser(Utils.getLoginUserId());
		ShaPasswordEncoder sha=new ShaPasswordEncoder(256); 
		sha.setEncodeHashAsBase64(false); 
		if (entity.getPassword().equals(sha.encodePassword(oldpass, null))){
			return renderText(response,"true");
	    }else{
		    return renderText(response,"false");
	    }
    }
	@RequestMapping
	@ResponseBody
	public Map<String, Object> changepass(HttpServletRequest request,HttpServletResponse response) {
		com.suun.model.system.security.User entity=manager.getUser(Utils.getLoginUserId());
		String newpassword = request.getParameter("newPswd");
		ShaPasswordEncoder sha=new ShaPasswordEncoder(256); 
		sha.setEncodeHashAsBase64(false); 
		entity.setPassword(sha.encodePassword(newpassword, null));	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try{
			manager.saveUser(entity);
			modelMap.put("success", "true");
		}catch(Exception e){
			modelMap.put("success", "false");
			modelMap.put("error", e.getMessage());
		}
		return modelMap;
	}
}

package com.suun.publics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 典型Controller规范类
 *
 * 
 */
public abstract class BaseController extends AbstractController{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String getBasePath(HttpServletRequest request){
		return request.getRequestURI().toString().substring(
				   request.getRequestURI().toString().indexOf(request.getContextPath())+request.getContextPath().length(),
				   request.getRequestURI().toString().lastIndexOf("/"))+"/";	
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return null;
	}
	
	/**
	 * 绕过Template,直接输出内容的简便函数. 
	 */
	protected String render(HttpServletResponse response,String text, String contentType) {
		try {
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 直接输出字符串.
	 */
	protected String renderText(HttpServletResponse response,String text) {
		return render(response,text, "text/plain;charset=UTF-8");
	}

	/**
	 * 直接输出HTML.
	 */
	protected String renderHtml(HttpServletResponse response,String html) {
		return render(response,html, "text/html;charset=UTF-8");
	}

	/**
	 * 直接输出XML.
	 */
	protected String renderXML(HttpServletResponse response,String xml) {
		return render(response,xml, "text/xml;charset=UTF-8");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping
	@ResponseBody
	public Map<String, Object> userAuth(HttpServletRequest request,HttpServletResponse response,String ids) {	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<GrantedAuthority> authids = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		StringBuffer auths = new StringBuffer();
		for (GrantedAuthority authority : authids) {
			 auths.append(authority.getAuthority()+"@");
		}
		modelMap.put("isauth",auths);
		return modelMap;	
	}
	
}

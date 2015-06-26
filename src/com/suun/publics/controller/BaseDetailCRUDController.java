package com.suun.publics.controller;

import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.suun.publics.utils.Utils;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主子CRUD典型Controller规范类

 * @param <T> 主子CRUD所管理的对象类型 
 */
public abstract class BaseDetailCRUDController<T> extends BaseCRUDController<T> {
	
	@RequestMapping
	@Override
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response,T operatebean) {
		
		ModelAndView modelandview=new ModelAndView("input");
        modelandview.getModel().put("jsonbean", Utils.Object2Json(operatebean));
        Map<String,Object> model=doNewbefore(request);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, String suunplatformeoperateid) {	
		T operatebean=getRecordSet(request,suunplatformeoperateid);
		String className=ClassUtils.getShortName(((T)operatebean).getClass().getName());
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
			className=className.substring(0,1).toLowerCase().concat(className.substring(1,className.length()));
		}
		ModelAndView modelandview=new ModelAndView("input",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		modelandview.getModel().put("isEdit", "true");
		modelandview.getModel().put("jsonbean", Utils.Object2Json(operatebean));
        Map<String,Object> model=doEditbefore(request,operatebean);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "Edit");
		return modelandview;	
	}


}

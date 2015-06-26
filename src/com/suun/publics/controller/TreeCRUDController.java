package com.suun.publics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suun.publics.utils.TreeObj;
import com.suun.publics.utils.Utils;



/**
 * Tree CRUD典型Controller规范类
 * @param <T> Tree CRUD所管理的对象类型 
 */
public abstract class TreeCRUDController<T> extends BaseController {
	
	
	/*private List<T> list = new ArrayList<T>();
	
	public List<T> getList() {
		return list;
	}*/

	@RequestMapping
	@ResponseBody	
	public List<Object> lists (HttpServletRequest request) throws Exception {
		List<Object> model = new ArrayList<Object>();
		String node=request.getParameter("groupId");
		if (node==null){
			node="0";
		}
		List<T> list=this.getAllRecords(request,node);
		for(T t:list){
			TreeObj obj= new TreeObj();
			obj.setId(getTreeId(t));
			obj.setText(getTreeName(t));
			obj.setParentId(getTreePid(t));
			obj.setChecked(false);
			if(getTreeLeaf(t)==0){
				obj.setLeaf(false);
			}else{
				obj.setLeaf(true);
			}
			if(getTreeIcon(t)!=null){
				obj.setIcon(getTreeIcon(t));
			}
			model.add(obj);
		}		
		return model;
	}
	
	@RequestMapping
	public ModelAndView _new(HttpServletRequest request,HttpServletResponse response,String suunplatformTreePid) {	
		ModelAndView modelandview=new ModelAndView("input");			
		if (suunplatformTreePid==null) suunplatformTreePid="0";
		modelandview.getModel().put("suunplatformTreePid", suunplatformTreePid);
		if (!suunplatformTreePid.equals("0")){
			T operatebean=getRecordSet(request,suunplatformTreePid);
			modelandview.getModel().put("suunplatformTreePName",getTreeName(operatebean));
		} else{
			modelandview.getModel().put("suunplatformTreePName","无");
		}
		Map<String,Object> model=doNewbefore(request);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		return modelandview;
	}
	
	@RequestMapping
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response, String suunplatformTreePid) {	
		T operatebean=getRecordSet(request,suunplatformTreePid);
		String className=ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("input",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		if (!getTreePid(operatebean).equals("0")){
			T poperatebean=getRecordSet(request,getTreePid(operatebean));
			modelandview.getModel().put("suunplatformTreePName",getTreeName(poperatebean));
		} else{
			modelandview.getModel().put("suunplatformTreePName","无");
		}
		modelandview.getModel().put("suunplatformTreePid", getTreePid(operatebean));
		modelandview.getModel().put("isEdit", "true");	
		Map<String,Object> model=doEditbefore(request);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		return modelandview;	
	}
    
	@RequestMapping
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request,HttpServletResponse response, T operatebean) {	
		
		request.getSession().setAttribute("Save", "true");
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (getTreeId(operatebean).equals("0")){
        	modelMap.put("success", "false");
        	modelMap.put("error", "编号不能为0！");
		} else{
			String error=saveRecordSet(request,operatebean);
	        if (Utils.isNullOrTemp(error)){
	        	modelMap.put("success", "true");
	        	modelMap.put("saveid", getTreeId(operatebean));
	        	modelMap.put("savename", getTreeName(operatebean));
	        	if(getTreeIcon(operatebean)!=null){
	        		modelMap.put("saveicon", getTreeIcon(operatebean));
				}        	
	        } else{
	        	modelMap.put("success", "false");
	        	modelMap.put("error", error);
	        }
		}        
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request,HttpServletResponse response,String ids) {	
		String[] iditenms=ids.split(",");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String error=deleteRecordSet(request,iditenms);
		if (Utils.isNullOrTemp(error)){        		
		    modelMap.put("success", "true"); 
		}else{
			modelMap.put("success", "false"); 
			modelMap.put("error", error);
		}
		return modelMap;	
	}
	
	public abstract String deleteRecordSet(HttpServletRequest request,String[] ids);
	
	public abstract String saveRecordSet(HttpServletRequest request,T operatebean);

    // 根据父编号，获取所有条件记录集
	public abstract List<T> getAllRecords(HttpServletRequest request,String Pid);
	
	public abstract T getRecordSet(HttpServletRequest request,String operateid);
	
	public abstract String getTreeId(T t);
	
	public abstract String getTreeName(T t);
	
	public abstract String getTreePid(T t);
	
	public abstract String getTreeIcon(T t);
	
	public abstract int getTreeLeaf(T t);
	
	public Map<String,Object> doNewbefore(HttpServletRequest request) {
		return null;
	}

	public Map<String,Object> doEditbefore(HttpServletRequest request) {
		return null;
	}

}

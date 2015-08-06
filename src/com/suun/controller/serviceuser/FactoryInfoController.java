package com.suun.controller.serviceuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.serviceuser.FactoryInfo;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.system.DicService;
import com.suun.service.serviceuser.FactoryInfoManager;


/**
 * @author renlq
 * 
 */
@Controller 
public class FactoryInfoController extends BaseCRUDController<FactoryInfo>{
	
	@Autowired
	FactoryInfoManager manager; 
	
	@Override
	public Page<FactoryInfo> getPageRecords(HttpServletRequest request,Page<FactoryInfo> page) {
		return manager.getAllFactoryInfo(page);
	}

	@Override
	public List<FactoryInfo> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllFactoryInfo(condition);
	}

	@Override
	public FactoryInfo getRecordSet(HttpServletRequest request,String operateid) {
		return manager.getFactoryInfo(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteFactoryInfo(ids[i]);
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
	public String saveRecordSet(HttpServletRequest request,FactoryInfo operatebean) {
		try{
			manager.saveFactoryInfo(operatebean);
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
	protected Map<String, Object> doNewbefore(HttpServletRequest request) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects1", DicService.GetDicByDicNo("FTYPE"));
		rmap.put("allObjects2", DicService.GetDicByDicNo("FLEVEL"));
		rmap.put("allObjects3", DicService.GetDicByDicNo("FSTANDARD"));
		rmap.put("allObjects4", DicService.GetDicByDicNo("FDOMAIN"));
		return rmap;
	}

	@Override
	protected Map<String, Object> doEditbefore(HttpServletRequest request,FactoryInfo operatebean) {
		Map<String,Object> rmap = doNewbefore(request);
		return rmap;
	}
	
	@RequestMapping
	@ResponseBody
	public String checkfno(HttpServletRequest request,HttpServletResponse response) {	
		String id = request.getParameter("fno");
		String oldid = request.getParameter("oldid");
		
		if(manager.isFnoUnique(id, oldid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public String checkfregister(HttpServletRequest request,HttpServletResponse response) {	
		String id = request.getParameter("fregister");
		String oldid = request.getParameter("oldid");
		
		if(manager.isFregisterUnique(id, oldid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
}

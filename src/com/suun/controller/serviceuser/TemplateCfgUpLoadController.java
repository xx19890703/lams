package com.suun.controller.serviceuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.suun.model.serviceuser.TemplateCfgUpLoad;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.TemplateCfgUpLoadManager;


/**
 * @author renlq
 * 
 */
@Controller 
public class TemplateCfgUpLoadController extends BaseCRUDController<TemplateCfgUpLoad>{
	
	@Autowired
	TemplateCfgUpLoadManager manager; 
	
	@Override
	public Page<TemplateCfgUpLoad> getPageRecords(HttpServletRequest request,Page<TemplateCfgUpLoad> page) {
		return manager.getAllTemplateCfgUpLoad(page);
	}

	@Override
	public List<TemplateCfgUpLoad> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllTemplateCfgUpLoad(condition);
	}

	@Override
	public TemplateCfgUpLoad getRecordSet(HttpServletRequest request,String operateid) {
		return manager.getTemplateCfgUpLoad(Long.parseLong(operateid));
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteTemplateCfgUpLoad(Long.parseLong(ids[i]));
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
	public String saveRecordSet(HttpServletRequest request,TemplateCfgUpLoad operatebean) {
		try{
			manager.saveTemplateCfgUpLoad(operatebean);
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
}

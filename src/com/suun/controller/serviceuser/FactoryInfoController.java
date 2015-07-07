package com.suun.controller.serviceuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.suun.model.serviceuser.FactoryInfo;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
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
}
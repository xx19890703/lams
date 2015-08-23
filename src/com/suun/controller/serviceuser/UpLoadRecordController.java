package com.suun.controller.serviceuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.suun.model.serviceuser.UpLoadRecord;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.ContractCategoryManager;
import com.suun.service.serviceuser.UpLoadRecordManager;
import com.suun.service.system.DicManager;


/**
 * @author renlq
 * 
 */
@Controller 
public class UpLoadRecordController extends BaseCRUDController<UpLoadRecord>{
	
	@Autowired
	UpLoadRecordManager manager; 
	@Autowired
	ContractCategoryManager cmanager;
	@Autowired 
	DicManager dicManager;
	
	@Override
	public Page<UpLoadRecord> getPageRecords(HttpServletRequest request,Page<UpLoadRecord> page) {
		return manager.getAllUpLoadRecord(page);
	}

	@Override
	public List<UpLoadRecord> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllUpLoadRecord(condition);
	}

	@Override
	public UpLoadRecord getRecordSet(HttpServletRequest request,String operateid) {
		return manager.getUpLoadRecord(Long.parseLong(operateid));
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteUpLoadRecord(Long.parseLong(ids[i]));
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
	public String saveRecordSet(HttpServletRequest request,UpLoadRecord operatebean) {
		try{
			manager.saveUpLoadRecord(operatebean);
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

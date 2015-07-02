package com.suun.controller.serviceuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.TemplateResDeatilManager;
import com.suun.service.serviceuser.TemplateResManager;
import com.suun.service.system.DicManager;

/**
 * TemplateResController
 * @author renlq
 *
 */

@Controller 
public class TemplateResDetailController extends BaseCRUDController<TemplateResDetail> {

	@Autowired
	TemplateResManager mainManager;
	@Autowired
	TemplateResDeatilManager subManager;
	@Autowired 
	DicManager dicManager;
	
	@Override
	protected String deleteRecordSet(HttpServletRequest request, String[] ids) {
		try{
    		subManager.delete(ids);//传入的是employee id
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}
	@Override
	protected String saveRecordSet(HttpServletRequest request,TemplateResDetail operatebean) {
		try{
			subManager.saveRecordSet(request,operatebean);
			return null;
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}
	@Override
	protected Page<TemplateResDetail> getPageRecords(HttpServletRequest request, Page<TemplateResDetail> page) {
		return subManager.getAll(page);
	}
	@Override
	protected List<TemplateResDetail> getAllRecords(HttpServletRequest request,Condition condition) {
		return subManager.getAll(condition);
	}
	@Override
	protected TemplateResDetail getRecordSet(HttpServletRequest request,String operateid) {
		return subManager.get(operateid);
	}
	
}

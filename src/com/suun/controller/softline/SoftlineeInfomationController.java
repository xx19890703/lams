package com.suun.controller.softline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.suun.model.softline.Softlinee;
import com.suun.model.softline.SoftlineeReview;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.system.DicService;
import com.suun.service.system.DicManager;

@Controller
public class SoftlineeInfomationController extends BaseCRUDController<Softlinee>{
	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.softline.SoftlineeManager softlineeManager; 
	
	@Autowired
	com.suun.service.softline.SoftlineeReviewManager SoftlineeReviewManager; 
	
	@Autowired
	DicManager dicManager;
	
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("sel_sex",DicService.GetDataNameExtArrayByDicNo("SEX"));	
		modelandview.getModel().put("sel_slstate",DicService.GetDataNameExtArrayByDicNo("SLSTATE"));	 
		return modelandview;
	}
	
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,Softlinee operatebean) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		SoftlineeReview slr=SoftlineeReviewManager.getSoftlineeReviewByBaseId(operatebean.getBaseId());
		if (slr!=null){
			rmap.put("isSoft", true);
			rmap.put("soft", slr);
		}		
		return rmap;
	}
	
	@Override
	public Page<Softlinee> getPageRecords(HttpServletRequest request,Page<Softlinee> page) {
		return softlineeManager.getAllSoftlinees(page);
	}

	@Override
	public List<Softlinee> getAllRecords(HttpServletRequest request,Condition condition) {
		return null;
	}

	@Override
	public Softlinee getRecordSet(HttpServletRequest request,String operateid) {
		return softlineeManager.getSoftlinee(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {		
		return "";	
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,Softlinee operatebean) {
		return "";
	}
}

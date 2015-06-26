package com.suun.controller.roster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suun.model.softline.Softlinee;
import com.suun.model.softline.SoftlineeReview;
import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.system.DicService;
import com.suun.service.system.DicManager;

@Controller
public class AuthenticateRosterController extends BaseCRUDController<Softlinee>{
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
		Dic_datakey dk=new Dic_datakey();
		dk.setData_no("1");
		dk.setDic_no("SLSTATE");
		Dic_data dd=new Dic_data();
		dd.setKey(dk);
		FilterInfo filter=new FilterInfo();
		filter.setFieldName("slstate");
		filter.setLogic(Logic.GREATEQUAL);
		filter.setValue(dd);
		page.getCondition().getFilterInfos().add(filter);
		return softlineeManager.getAllSoftlinees(page);	}

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
	
	@RequestMapping
	@ResponseBody
	public String changeState(HttpServletRequest request,HttpServletResponse response){
		String id = request.getParameter("id");
		String state = request.getParameter("state");//0表示审核通过   ; 1表示审核未通过
		if(!"".equals(id)&&id!=null){
			if(!"".equals(state)&&state!=null&&"0".equals(state)){
				//设置审核时间
				java.util.Date opertime = new java.util.Date();
				SoftlineeReview slr=SoftlineeReviewManager.getSoftlineeReviewByBaseId(id);
				slr.setSystemData1(opertime);
				SoftlineeReviewManager.saveSoftlineeReview(slr);
				//设置审核通过的状态
				Softlinee softlinee = softlineeManager.getSoftlinee(id);
				Dic_datakey dk=new Dic_datakey();
				dk.setData_no("4");
				dk.setDic_no("SLSTATE");
				Dic_data dd=new Dic_data();
				dd.setKey(dk);
				softlinee.setSlstate(dd);
				softlineeManager.saveSoftlinee(false,softlinee);
				return renderText(response,"true");
			}else if(!"".equals(state)&&state!=null&&"1".equals(state)){
				//设置审核时间
				java.util.Date opertime = new java.util.Date();
				SoftlineeReview slr=SoftlineeReviewManager.getSoftlineeReviewByBaseId(id);
				slr.setSystemData1(opertime);
				SoftlineeReviewManager.saveSoftlineeReview(slr);
				//设置审核通过的状态
				Softlinee softlinee = softlineeManager.getSoftlinee(id);
				Dic_datakey dk=new Dic_datakey();
				dk.setData_no("5");
				dk.setDic_no("SLSTATE");
				Dic_data dd=new Dic_data();
				dd.setKey(dk);
				softlinee.setSlstate(dd);
				softlineeManager.saveSoftlinee(false,softlinee);
				return renderText(response,"true");
			}
		}
		return renderText(response,"false");
	}
}

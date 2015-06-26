package com.suun.controller.softline;

import java.util.ArrayList;
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

import com.suun.model.professor.Professor;
import com.suun.model.softline.Softlinee;
import com.suun.model.softline.SoftlineeReview;
import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.publics.Grid.GridData;
import com.suun.publics.controller.BaseDetailCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.system.DicService;
import com.suun.publics.utils.Utils;
import com.suun.service.professor.ProfessorManager;
import com.suun.service.softline.SoftlineeManager;
import com.suun.service.system.DicManager;

@Controller
public class SoftlineeReviewController extends BaseDetailCRUDController<SoftlineeReview>{

	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.softline.SoftlineeReviewManager softlineeReviewManager; 
	
	@Autowired
	ProfessorManager porfessorMrg;
	
	@Autowired
	SoftlineeManager softlineeMgr;
	
	@Autowired
	DicManager dicManager;
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("sex_state",DicService.GetDataNameExtArrayByDicNo("SEX"));	 
		modelandview.getModel().put("inv_state",DicService.GetDataNameExtArrayByDicNo("INVALIDISMGRADES"));	 
		modelandview.getModel().put("nur_state",DicService.GetDataNameExtArrayByDicNo("NURSINGGRADES"));	 
		modelandview.getModel().put("sls_state",DicService.GetDataNameExtArrayByDicNo("SLSTATE"));	 
		return modelandview;
	}	
	
	@RequestMapping
	@ResponseBody
	public String validateVerdict(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("verdictId");
		String oldid = request.getParameter("oldid");
		if (softlineeReviewManager.isIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@RequestMapping
	@ResponseBody
	public String validateRP(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("accProfessionals[0].proId");
		String oldid = request.getParameter("oldid");
		if (softlineeReviewManager.isProUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	
	@Override
	protected Map<String,Object> doNewbefore(HttpServletRequest request) {
		
		//获取系统的当前时间
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
		String str_date1 = formatter.format(currentTime); //将日期时间格式化 
		request.setAttribute("str_date1",str_date1);
			
				
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", DicService.GetDicByDicNo("INVALIDISMGRADES"));
		rmap.put("allObjects1", DicService.GetDicByDicNo("NURSINGGRADES"));
		return rmap;
	}
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,SoftlineeReview operatebean) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", dicManager.findDicsByDicno("INVALIDISMGRADES"));
		rmap.put("allObjects1", dicManager.findDicsByDicno("NURSINGGRADES"));
		rmap.put("slstate", dicManager.findDicsByDicno("SLSTATE"));
		return rmap;
	}
	@Override
	public Page<SoftlineeReview> getPageRecords(HttpServletRequest request,Page<SoftlineeReview> page) {
		return softlineeReviewManager.getAllSoftlineeReviews(page);
	}

	@Override
	public List<SoftlineeReview> getAllRecords(HttpServletRequest request,Condition condition) {
		return softlineeReviewManager.getAllSoftlineeReviews(condition);
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> listsForSlr(HttpServletRequest request) throws Exception {
		try {
			Page<Professor> page = new Page<Professor>(10, true);			
            GridData<Professor> gridData=new GridData<Professor>(Professor.class,page,request);	
            if(null!=request.getParameter("ids")&&!"".equals(request.getParameter("ids"))){
				String ids = request.getParameter("ids");
				String[] slrProIds = ids.split(";");
				for(int i=0;i<slrProIds.length;i++){
					FilterInfo filter=new FilterInfo();
					filter.setFieldName("proId");
					filter.setLogic(Logic.NOTEQUAL);
					filter.setValue(slrProIds[i]);
					page.getCondition().getFilterInfos().add(filter);
				}				
			}
			page = porfessorMrg.getAllProfessors(page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> listsForSl(HttpServletRequest request) throws Exception {
		try {
			Page<Softlinee> page = new Page<Softlinee>(10, true);			
            GridData<Softlinee> gridData=new GridData<Softlinee>(Softlinee.class,page,request);	
			FilterInfo filter=new FilterInfo();
			filter.setFieldName("slstate");
			filter.setLogic(Logic.EQUAL);
			Dic_datakey dk=new Dic_datakey();
			dk.setData_no("2");
			dk.setDic_no("SLSTATE");
			Dic_data dd=new Dic_data();
			dd.setKey(dk);
			filter.setValue(dd);
			page.getCondition().getFilterInfos().add(filter);
			page = softlineeMgr.getAllSoftlinees(page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}

	@Override
	public SoftlineeReview getRecordSet(HttpServletRequest request,String operateid) {
		return softlineeReviewManager.getSoftlineeReview(operateid);
	}
	

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	softlineeReviewManager.deleteSoftlineeReview(ids[i]);
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
	public String saveRecordSet(HttpServletRequest request,SoftlineeReview operatebean) {
		List<Professor> professors=new ArrayList<Professor>(); 
		try{
			for(Professor professor:operatebean.getAccProfessionals()){
				if (!Utils.isNullOrTemp(professor.getProId())){
					professors.add(professor);
				}
			}
			operatebean.setAccProfessionals(professors);
			softlineeReviewManager.saveSoftlineeReview(operatebean);
			
			String baseId=operatebean.getSoftlineeInfo().getBaseId();
			String state=operatebean.getSoftlineeInfo().getSlstate().getKey().getData_no();
			Softlinee softlinee=softlineeMgr.getSoftlinee(baseId);
			Dic_datakey dk=new Dic_datakey();
			dk.setData_no(state);
			dk.setDic_no("SLSTATE");
			Dic_data dd=new Dic_data();
			dd.setKey(dk);
			softlinee.setSlstate(dd);
			softlineeMgr.saveSoftlinee(false,softlinee);
			
			return null;
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

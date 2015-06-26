package com.suun.controller.professor;

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
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.system.DicService;

@Controller
public class ProfessorController extends BaseCRUDController<Professor>{

	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.professor.ProfessorManager professorManager; 
	
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("sel_sex",DicService.GetDataNameExtArrayByDicNo("SEX"));	 
		modelandview.getModel().put("sel_rank",DicService.GetDataNameExtArrayByDicNo("RANK"));	 
		return modelandview;
	}
	
	@RequestMapping
	@ResponseBody
	public String validateProfessor(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("proId");
		String oldid = request.getParameter("oldid");
		if (professorManager.isIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@Override
	public Page<Professor> getPageRecords(HttpServletRequest request,Page<Professor> page) {
		return professorManager.getAllProfessors(page);
	}

	@Override
	public List<Professor> getAllRecords(HttpServletRequest request,Condition condition) {
		return professorManager.getAllProfessors(condition);
	}

	@Override
	protected Map<String, Object> doNewbefore(HttpServletRequest request) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", DicService.GetDicByDicNo("SEX"));
		rmap.put("allObjects1", DicService.GetDicByDicNo("RANK"));
		return rmap;
	}

	@Override
	protected Map<String, Object> doEditbefore(HttpServletRequest request,
			Professor operatebean) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", DicService.GetDicByDicNo("SEX"));
		rmap.put("allObjects1", DicService.GetDicByDicNo("RANK"));
		return rmap;
	}

	@Override
	public Professor getRecordSet(HttpServletRequest request,String operateid) {
		return professorManager.getProfessor(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	professorManager.deleteProfessor(ids[i]);
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
	public String saveRecordSet(HttpServletRequest request,Professor operatebean) {
		try{
			professorManager.saveProfessor(operatebean);
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

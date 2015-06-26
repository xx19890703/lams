package com.suun.controller.system.orgnization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.orgnization.Employee;
import com.suun.publics.Grid.GridData;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.system.DicService;
import com.suun.service.system.DicManager;
import com.suun.service.system.orgnization.EmployeetManager;

/**
 * DepartmentController
 * @author 
 *
 */

@Controller 
public class EmployeeController extends BaseCRUDController<Employee> {
	@Autowired
	EmployeetManager empManager;
	@Autowired
	DicManager dicManager;

	@RequestMapping
	@ResponseBody
	public Map<String, Object> selectEmployee(HttpServletRequest request,HttpServletResponse response) {
		Page<Employee> page = new Page<Employee>(10, true);
		GridData<Employee> gridData=new GridData<Employee>(Employee.class,page,request);
		String existidstr=request.getParameter("existids");
		if (existidstr!=null){
			String[] existids=existidstr.split("@");
			for(String existid:existids){
				if (!existid.equals("")){
					Condition condition=page.getCondition();
					com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
					f.setFieldName("employeeid");
					f.setLogic(Logic.NOTEQUAL);
					f.setValue(existid);
					condition.getFilterInfos().add(f);
					page.setCondition(condition);
				}				
			}
		}		
		page = empManager.getAllEmployee(page);
		return gridData.getGridData(page);
	}
	
	@Override
	protected Map<String,Object> doNewbefore(HttpServletRequest request) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", DicService.GetDicByDicNo("STATE"));
		return rmap;
	}
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,Employee operatebean) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", dicManager.findDicsByDicno("STATE"));
		return rmap;
	}
	
	@RequestMapping
	@ResponseBody
	public String validateId(HttpServletRequest request,HttpServletResponse response) {	
		String id = request.getParameter("employeeid");
		String oldid = request.getParameter("oldid");
		if (empManager.isIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	
	@Override
	protected String deleteRecordSet(HttpServletRequest request, String[] ids) {
		try{
			empManager.delete(ids);
			return null;
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}		
	}

	@Override
	protected String saveRecordSet(HttpServletRequest request,
			Employee operatebean) {
		try{
			empManager.saveRecordSet(request,operatebean);
			return null;
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	protected Page<Employee> getPageRecords(HttpServletRequest request,
			Page<Employee> page) {
		return empManager.getAll(page);
	}

	@Override
	protected List<Employee> getAllRecords(HttpServletRequest request,
			Condition condition) {
		return empManager.getAll(condition);
	}

	@Override
	protected Employee getRecordSet(HttpServletRequest request, String operateid) {
		return empManager.get(operateid);
	}		
}

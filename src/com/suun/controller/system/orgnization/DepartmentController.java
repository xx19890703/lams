package com.suun.controller.system.orgnization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suun.model.system.Constant;
import com.suun.model.system.orgnization.Department;
import com.suun.model.system.orgnization.Depemployees;
import com.suun.model.system.orgnization.Employee;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.Page;
import com.suun.service.system.DicManager;
import com.suun.service.system.orgnization.DepartmentManager;
import com.suun.service.system.orgnization.EmployeetManager;

/**
 * DepartmentController
 * @author 
 *
 */

@Controller 
public class DepartmentController extends TreeGridCRUDController<Department,Depemployees> {
	
	@Autowired
	DepartmentManager depManager;
	@Autowired
	EmployeetManager empManager;
	@Autowired 
	DicManager dicManager;
	
	@RequestMapping
	@ResponseBody
	public String validateDid(HttpServletRequest request,HttpServletResponse response) {	
		String did = request.getParameter("did");
		String olddid = request.getParameter("olddid");		
		if(depManager.isDepartmentIdUnique(did, olddid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public String validateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		String pid = request.getParameter("id");
		
		if(depManager.isDepartmentNameUnique(pid, name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}

	@Override
	public String getTreeId(Department t) {
		return t.getDid().toString();
	}
	@Override
	public String getTreeName(Department t) {		
		return t.getName();
	}

	@Override
	public String getTreePid(Department t) {		
		return t.getPid();
	}

	@Override
	public String getTreeIcon(HttpServletRequest request,Department t) {		
		return "/resources/images/system/group.png";
	}

	@Override
	public int getTreeLeaf(Department t) {
		return 0;
	}
	@Override
	public Map<String,Object> doTreeNewbefore() {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	@Override
	public Map<String,Object> doTreeEditbefore() {
		return doTreeNewbefore();
	}
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
			Boolean flag=null;
            for (int i=0;i<ids.length;i++){
            	List<Department> orglist=depManager.findDepByParentId(ids[i]);
            	//判断节点是否是父节点，长度为0表示不是父节点可以删除，反之就是父节点不可删除。
            	if(orglist.size()==0){
            		depManager.deleteDepartment(ids[i]);
            		flag= true;
            	}else{
            		flag= false;
            	}
		    }
            if (flag){
            	return "";
            }else{
            	return "父节点不可删除!";
            }
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String treeSaveRecordSet(Department operatebean) {
		try{			
			depManager.saveDepartment(operatebean);
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public List<Department> getTreeAllRecords(String Pid) {
		return depManager.findDepByParentId(Pid);
	}

	@Override
	public Department getTreeRecordSet(String operateid) {
		return depManager.getDepartment(operateid);
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,
			String treeid, String[] ids) {		
        try{
        	for (int i=0;i<ids.length;i++){			
            	//depManager.deleteDepemployees(Long.valueOf(ids[i]));//传入的是id
    			depManager.deleteDepemployees(treeid,ids[i]);//传入的是employee id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,Depemployees operatebean) {
		try{
			depManager.saveDepemployees(operatebean);
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected Page<Depemployees> getGridPageRecords(HttpServletRequest request, String treeid, Page<Depemployees> page) {
		return depManager.getAllDepemployee(treeid,page);
	}

	@Override
	protected List<Depemployees> getGridAllRecords(HttpServletRequest request,
			String treeid, Condition condition) {
		return depManager.getAllDepemployee(treeid,condition);
	}

	@Override
	protected Depemployees getGridRecordSet(HttpServletRequest request,
			String treeid, String operateid) {
		return depManager.getDepemployee(treeid,operateid);
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> saveEmployees(HttpServletRequest request,
			HttpServletResponse response) {
		//request.getSession().setAttribute("Save", "true");	
		
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //List<Employee> employees=new ArrayList<Employee>();
        ObjectMapper objectMapper = new ObjectMapper();        
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			Employee[] employees = objectMapper.readValue(request.getParameter("employees"), Employee[].class);	
        	for (int i=0;i<employees.length;i++){
        		Employee employee=employees[i];
            	Depemployees operatebean=new Depemployees();        	
            	Department department=new Department();
            	department.setDid(request.getParameter("departmentid"));        	
            	operatebean.setDepartment(department);
            	operatebean.setEmployee(employee);
            	if (i==0) {
    				request.getSession().setAttribute("entity", operatebean);
    			}    			
            	depManager.saveDepemployees(operatebean);
            }
        	modelMap.put("success", "true");
        }catch(Exception e){
        	modelMap.put("success", "false");
        	modelMap.put("error", e.getMessage());
		}        
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public String getDepEmployees(HttpServletRequest request,
			HttpServletResponse response) {
		Condition condition=new Condition();
		condition.setFilterInfos(new ArrayList<FilterInfo>());
		List<Depemployees> depemployees=
				depManager.getAllDepemployee(request.getParameter("departmentid"),condition);
		String existids="";
		for (Depemployees depemployee:depemployees){
			existids=depemployee.getEmployee().getEmployeeid()+"@"+existids;
		}
		return this.renderText(response, existids);		
	}
}

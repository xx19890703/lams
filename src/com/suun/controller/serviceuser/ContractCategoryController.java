package com.suun.controller.serviceuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.fr.base.dav.LocalEnv;
import com.suun.model.serviceuser.ContractCategory;
import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.system.Constant;
import com.suun.model.utils.GetDataBaseSql;
import com.suun.model.utils.TestGetDataBaseSql;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.ContractCategoryManager;
import com.suun.service.serviceuser.ContractDetailManager;
import com.suun.service.system.DicManager;

/**
 * ContractCategoryController
 * @author renlq
 *
 */

@Controller 
public class ContractCategoryController extends TreeGridCRUDController<ContractCategory,ContractDetail>{

	@Autowired
	ContractCategoryManager mainManager;
	@Autowired
	ContractDetailManager subManager;
	@Autowired 
	DicManager dicManager;
	
	@RequestMapping
	@ResponseBody
	public String validateDid(HttpServletRequest request,HttpServletResponse response) {	
		String did = request.getParameter("did");
		String olddid = request.getParameter("olddid");		
		if(mainManager.isContractCategoryNameUnique("", did, olddid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getTest(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("renlq", "test");
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public String validateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		String pid = request.getParameter("id");
		
		if(mainManager.isContractCategoryNameUnique(pid, name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	public String getContractCategory(HttpServletRequest request,HttpServletResponse response) {
//		Condition condition=new Condition();
//		condition.setFilterInfos(new ArrayList<FilterInfo>());
//		List<TemplateResDetail> res = manager.getAllTemplateRes(request.getParameter("pid"),condition);
//		String existids="";
//		for (TemplateResDetail temp : res){
//			existids = temp.getEmployee().getEmployeeid()+"@"+existids;
//		}
//		return this.renderText(response, existids);		
		return "";
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
	public Map<String,Object> doGridNewbefore(HttpServletRequest request,String treeid) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	
	@Override
	public Map<String,Object> doGridEditbefore(HttpServletRequest request,String treeid,ContractDetail operatebean) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
			Boolean flag=null;
            for (int i=0;i<ids.length;i++){
            	List<ContractCategory> orglist=mainManager.findContractCategoryByParentId(ids[i]);
            	//判断节点是否是父节点，长度为0表示不是父节点可以删除，反之就是父节点不可删除。
            	if(orglist.size()==0){
            		mainManager.deleteContractCategory(ids[i]);
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
	public String treeSaveRecordSet(ContractCategory operatebean) {
		try{			
			mainManager.saveContractCategory(operatebean);
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public List<ContractCategory> getTreeAllRecords(String Pid) {
		return mainManager.findContractCategoryByParentId(Pid);
	}

	@Override
	public ContractCategory getTreeRecordSet(String operateid) {
		return mainManager.getContractCategory(operateid);
	}

	@Override
	public String getTreeId(ContractCategory t) {
		return t.getDid().toString();
	}

	@Override
	public String getTreeName(ContractCategory t) {
		return t.getName();
	}

	@Override
	public String getTreePid(ContractCategory t) {
		return t.getPid();
	}

	@Override
	public String getTreeIcon(HttpServletRequest request, ContractCategory t) {
		return "/resources/images/system/group.png";
	}

	@Override
	public int getTreeLeaf(ContractCategory t) {
		return 0;
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,String treeid, String[] ids) {
		try{
        	for (int i=0;i<ids.length;i++){			
        		mainManager.deleteContractDetail(treeid,ids[i]);//传入的是employee id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,ContractDetail operatebean) {
		try{
			mainManager.saveContractDetail(operatebean);
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected Page<ContractDetail> getGridPageRecords(HttpServletRequest request, String treeid,Page<ContractDetail> page) {
		return mainManager.getAllContractDetail(treeid, page);
	}

	@Override
	protected List<ContractDetail> getGridAllRecords(HttpServletRequest request, String treeid, Condition condition) {
		return mainManager.findContractDetailByParentId(treeid);
	}

	@Override
	protected ContractDetail getGridRecordSet(HttpServletRequest request,String treeid, String operateid) {
		return mainManager.getContractDetail(treeid, operateid);
	}
	
	@RequestMapping
	public ModelAndView getGridAllRecords() {	
		ModelAndView mav = new ModelAndView();
		mav.addObject(mainManager.getAllContractCategory("01",null));
		return mav;
	}
	
	@RequestMapping
	@ResponseBody
	public void downDataZip(String treeid, String id, final HttpServletResponse response){
		String envPath = FRContext.getCurrentEnv().getPath();  
        FRContext.setCurrentEnv(new LocalEnv(envPath)); 
        ContractDetail contract = mainManager.getContractDetail("2","1");
        GetDataBaseSql sql = new TestGetDataBaseSql();
        String str = sql.getDataSqlByContractId(contract.getDid());
        
        //遍历所有模板
        for(ContractTemplateRes s : contract.getRescontent()){
        	TemplateResDetail temple = s.getTemplate();
        	//遍历所有数据库表
        	for(TemplateResContent tt : temple.getRescontent()){
        		System.out.println(tt.getCsqlpath());
        	}
        }
	}
}

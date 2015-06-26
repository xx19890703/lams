package com.suun.publics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.api.history.HistoryProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suun.publics.Grid.ExportColumnInfo;
import com.suun.publics.Grid.GridData;
import com.suun.publics.Grid.GridUtils;
import com.suun.publics.Grid.export.ExportService;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.Utils;
import com.suun.publics.workflow.JbpmTemplate;
import com.suun.service.system.workflow.WorkflowManager;



/**
 * DoubleGrid CRUD典型Controller规范类
 * @param <T1> DoubleGrid CRUD所管理的对象类型 
 */
public abstract class DoubleGridCRUDController<TGrid1,TGrid2> extends BaseController {
	
    protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String ObjName=this.getClass().getSimpleName().substring(0,1).toLowerCase().
			               concat(this.getClass().getSimpleName().substring(1,this.getClass().getSimpleName().indexOf("Controller")));
	@Autowired
	protected WorkflowManager manager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping
	public void grid1exportlist(HttpServletRequest request,HttpServletResponse response) throws Exception {		
		String suunGridcolumns=request.getParameter("suunGridcolumns");
		String[] columns=suunGridcolumns.split(";");
		List<ExportColumnInfo> exportColumnInfos=new ArrayList<ExportColumnInfo>();
		for (int i = 0; i < columns.length; i++) {
			String[] cols=columns[i].split(",");
			ExportColumnInfo eInfo=new ExportColumnInfo();
		    eInfo.setHeader(cols[1]);
		    eInfo.setFieldIndex(cols[0]);
		    exportColumnInfos.add(eInfo);
		}
		String ExportFileName = request.getParameter("filename");
		String exportType=request.getParameter("exportType");
		Class beanClass=(Class<TGrid1>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[0];
		String fileName=beanClass.getSimpleName();
		if (!Utils.isNullOrTemp(ExportFileName)){
			fileName=ExportFileName;
		}
		ExportService eService=new ExportService(request,response);
		Condition condition = 
			GridUtils.getConditionFromRequest(beanClass,request);
		eService.export(exportType,
				fileName,
				exportColumnInfos,
				getGrid1AllRecords(request,condition),
				beanClass);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping
	public void grid2exportlist(HttpServletRequest request,HttpServletResponse response) throws Exception {			
		String parentid = request.getParameter("parentid");
		String suunGridcolumns=request.getParameter("suunGridcolumns");
		String[] columns=suunGridcolumns.split(";");
		List<ExportColumnInfo> exportColumnInfos=new ArrayList<ExportColumnInfo>();
		for (int i = 0; i < columns.length; i++) {
			String[] cols=columns[i].split(",");
			ExportColumnInfo eInfo=new ExportColumnInfo();
		    eInfo.setHeader(cols[1]);
		    eInfo.setFieldIndex(cols[0]);
		    exportColumnInfos.add(eInfo);
		}
		String ExportFileName = request.getParameter("filename");
		String exportType=request.getParameter("exportType");
		Class beanClass=(Class<TGrid2>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[0];
		String fileName=beanClass.getSimpleName();
		if (!Utils.isNullOrTemp(ExportFileName)){
			fileName=ExportFileName;
		}
		ExportService eService=new ExportService(request,response);
		Condition condition = 
			GridUtils.getConditionFromRequest(beanClass,request);
		eService.export(exportType,
				fileName,
				exportColumnInfos,
				getGrid2AllRecords(request,parentid,condition),
				beanClass);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping
	@ResponseBody	
	public Map<String, Object> grid1lists(HttpServletRequest request) throws Exception {
		try {
			Class beanClass=(Class<TGrid1>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[0];
			Page<TGrid1> page = new Page<TGrid1>(10, true);
            GridData<TGrid1> gridData=new GridData<TGrid1>(beanClass,page,request);	
            TGrid1 operatebean=(TGrid1)request.getSession().getAttribute("entity");
            page.setPosionRes(operatebean);
            if (operatebean!=null){
				page.getCondition().getFilterInfos().clear();
				request.getSession().removeAttribute("entity");
            }
			page = this.getGrid1PageRecords(request,page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping
	@ResponseBody	
	public Map<String, Object> grid2lists(HttpServletRequest request) throws Exception {
		try {
			String parentid = request.getParameter("parentid");
			Class beanClass=(Class<TGrid2>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[1];
			Page<TGrid2> page = new Page<TGrid2>(10, true);
            GridData<TGrid2> gridData=new GridData<TGrid2>(beanClass,page,request);	
            TGrid2 operatebean=(TGrid2)request.getSession().getAttribute("entity");
            page.setPosionRes(operatebean);
            if (operatebean!=null){
				page.getCondition().getFilterInfos().clear();
				request.getSession().removeAttribute("entity");
            }
			page = this.getGrid2PageRecords(request,parentid,page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}
	
	@RequestMapping
	public ModelAndView grid1_new(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelandview=new ModelAndView("grid1input");
		Map<String,Object> model=doGrid1Newbefore(request);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	public ModelAndView grid2_new(HttpServletRequest request,
			HttpServletResponse response,String parentid) {
		ModelAndView modelandview=new ModelAndView("grid2input");
		modelandview.getModel().put("parentid", parentid);
		Map<String,Object> model=doGrid2Newbefore(request,parentid);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}		
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	public ModelAndView grid1edit(HttpServletRequest request,
			HttpServletResponse response, String suunplatformeoperateid){ 
	    //有审批流程
		TGrid1 operatebean=getGrid1RecordSet(request,suunplatformeoperateid);		
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	List<HistoryProcessInstance> pis=
					JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getGrid1ObjId(operatebean));
	    	for(HistoryProcessInstance pi:pis){
	    		if (pi.getState().equals(HistoryProcessInstance.STATE_ENDED)){
	    			isended=true;
	    			break;
	    		}
	    	}
	    	if (isended){
	    	    ModelAndView modelandview=new ModelAndView("/workflow/NoEditError");
	    	    return modelandview;
	    	}
	    }		
		String className=ClassUtils.getShortName(((TGrid1)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("grid1input",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		modelandview.getModel().put("isEdit", "true");  
		Map<String,Object> model=doGrid1Editbefore(request,operatebean);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "Edit");
		return modelandview;	
	}
	
	@RequestMapping
	public ModelAndView grid2edit(HttpServletRequest request,
			HttpServletResponse response, String suunplatformeoperateid){ 
	    //有审批流程
		String parentid = request.getParameter("parentid");
		TGrid2 operatebean=getGrid2RecordSet(request,parentid,suunplatformeoperateid);		
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	List<HistoryProcessInstance> pis=
					JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getGrid2ObjId(operatebean));
	    	for(HistoryProcessInstance pi:pis){
	    		if (pi.getState().equals(HistoryProcessInstance.STATE_ENDED)){
	    			isended=true;
	    			break;
	    		}
	    	}
	    	if (isended){
	    	    ModelAndView modelandview=new ModelAndView("/workflow/NoEditError");
	    	    return modelandview;
	    	}
	    }		
		
		String className=ClassUtils.getShortName(((TGrid2)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("grid2input",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		modelandview.getModel().put("isEdit", "true");  
		modelandview.getModel().put("parentid", parentid);
		Map<String,Object> model=doGrid2Editbefore(request,operatebean);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "Edit");
		return modelandview;	
	}
    
	@RequestMapping
	@ResponseBody
	public Map<String, Object> grid1save(HttpServletRequest request,
			HttpServletResponse response, TGrid1 operatebean) {
		boolean isNew=request.getSession().getAttribute("OperateState").equals("New");
		//request.getSession().setAttribute("OperateState", "Save");
		//request.getSession().setAttribute("Save", "true");
		
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();	
        operatebean=doGrid1Savebefore(request,response,operatebean);
        String error=saveGrid1RecordSet(request,operatebean);
        //request.getSession().removeAttribute("OperateState");
		if (Utils.isNullOrTemp(error)){
        	//有审批流程
        	if (isJbpm()){ 
        		if (isNew){
        			manager.writelog(ObjName,getGrid1ObjId(operatebean),"新增",true);
        		} else{        			
        			try{
        				JbpmTemplate.getJbpmTemplate().deleteProcessInstance(ObjName,getGrid1ObjId(operatebean)); 
    				}catch(Exception e){
    					
    				}
        			manager.writelog(ObjName,getGrid1ObjId(operatebean),"修改",true);
        		}
        		JbpmTemplate.getJbpmTemplate().addProcessInstance(ObjName,getGrid1ObjId(operatebean));   			
        	}
        	modelMap.put("success", "true");
        } else{
        	modelMap.put("success", "false");
        	modelMap.put("error", error);
        }
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> grid2save(HttpServletRequest request,
			HttpServletResponse response, TGrid2 operatebean) {
		boolean isNew=request.getSession().getAttribute("OperateState").equals("New");
		//request.getSession().setAttribute("OperateState", "Save");
		//request.getSession().setAttribute("Save", "true");
		
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();	
        operatebean=doGrid2Savebefore(request,response,operatebean);
        String error=saveGrid2RecordSet(request,operatebean);
        //request.getSession().removeAttribute("OperateState");
		if (Utils.isNullOrTemp(error)){
        	//有审批流程
        	if (isJbpm()){ 
        		if (isNew){
        			manager.writelog(ObjName,getGrid2ObjId(operatebean),"新增",true);
        		} else{        			
        			try{
        				JbpmTemplate.getJbpmTemplate().deleteProcessInstance(ObjName,getGrid2ObjId(operatebean)); 
    				}catch(Exception e){
    					
    				}
        			manager.writelog(ObjName,getGrid2ObjId(operatebean),"修改",true);
        		}
        		JbpmTemplate.getJbpmTemplate().addProcessInstance(ObjName,getGrid2ObjId(operatebean));   			
        	}
        	modelMap.put("success", "true");
        } else{
        	modelMap.put("success", "false");
        	modelMap.put("error", error);
        }
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> grid1delete(HttpServletRequest request,
			HttpServletResponse response,String ids) {	
		String[] iditenms=ids.split(",");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//有审批流程			
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	for (int i=0;i<iditenms.length;i++){
	    		TGrid1 operatebean=getGrid1RecordSet(request,iditenms[i]);
	    		List<HistoryProcessInstance> pis=
						JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getGrid1ObjId(operatebean));
		    	for(HistoryProcessInstance pi:pis){
		    		if (pi.getState().equals(HistoryProcessInstance.STATE_ENDED)){
		    			isended=true;
		    			break;
		    		}
		    	}
		    	if (isended) break;
	    	}	    	
	    	if (isended){
	    		modelMap.put("success", "false"); 
				modelMap.put("error", "选择项存在审核通过的数据，不能删除！");
				return modelMap;
	    	}
	    }	
		
		String error=deleteGrid1RecordSet(request,iditenms);
		if (Utils.isNullOrTemp(error)){ 
			if (isJbpm()){
				for (int i=0;i<iditenms.length;i++){				
					try{
						JbpmTemplate.getJbpmTemplate().deleteProcessInstance(ObjName,iditenms[i]);
					}catch(Exception e){
						
					}
					manager.writelog(ObjName,iditenms[i],"删除");
				}
			}
		    modelMap.put("success", "true"); 
		}else{
			modelMap.put("success", "false"); 
			modelMap.put("error", error);
		}
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> grid2delete(HttpServletRequest request,
			HttpServletResponse response,String ids) {	
		String[] iditenms=ids.split(",");
		String parentid = request.getParameter("parentid");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//有审批流程			
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	for (int i=0;i<iditenms.length;i++){
	    		TGrid2 operatebean=getGrid2RecordSet(request,parentid,iditenms[i]);
	    		List<HistoryProcessInstance> pis=
						JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getGrid2ObjId(operatebean));
		    	for(HistoryProcessInstance pi:pis){
		    		if (pi.getState().equals(HistoryProcessInstance.STATE_ENDED)){
		    			isended=true;
		    			break;
		    		}
		    	}
		    	if (isended) break;
	    	}	    	
	    	if (isended){
	    		modelMap.put("success", "false"); 
				modelMap.put("error", "选择项存在审核通过的数据，不能删除！");
				return modelMap;
	    	}
	    }	
		
		String error=deleteGrid2RecordSet(request,parentid,iditenms);
		if (Utils.isNullOrTemp(error)){ 
			if (isJbpm()){
				for (int i=0;i<iditenms.length;i++){				
					try{
						JbpmTemplate.getJbpmTemplate().deleteProcessInstance(ObjName,iditenms[i]);
					}catch(Exception e){
						
					}
					manager.writelog(ObjName,iditenms[i],"删除");
				}
			}
		    modelMap.put("success", "true"); 
		}else{
			modelMap.put("success", "false"); 
			modelMap.put("error", error);
		}
		return modelMap;	
	}
	
	protected abstract String deleteGrid1RecordSet(HttpServletRequest request,String[] ids);
	protected abstract String deleteGrid2RecordSet(HttpServletRequest request,String parentid,String[] ids);
	
	protected abstract String saveGrid1RecordSet(HttpServletRequest request,TGrid1 operatebean);
	protected abstract String saveGrid2RecordSet(HttpServletRequest request,TGrid2 operatebean);

	/*
	 * 分页获取记录集
	 */
	protected abstract Page<TGrid1> getGrid1PageRecords(HttpServletRequest request,Page<TGrid1> page);
	protected abstract Page<TGrid2> getGrid2PageRecords(HttpServletRequest request,String parentid,Page<TGrid2> page);
	/*
	 * 获取所有条件记录集
	 */
	protected abstract List<TGrid1> getGrid1AllRecords(HttpServletRequest request,Condition condition);
	protected abstract List<TGrid2> getGrid2AllRecords(HttpServletRequest request,String parentid,Condition condition);
	
	protected abstract TGrid1 getGrid1RecordSet(HttpServletRequest request,String operateid);
	protected abstract TGrid2 getGrid2RecordSet(HttpServletRequest request,String parentid,String operateid);
	
	protected Map<String,Object> doGrid1Newbefore(HttpServletRequest request) {
		return null;
	}
	protected Map<String,Object> doGrid2Newbefore(HttpServletRequest request,String parentid) {
		return null;
	}

	protected Map<String,Object> doGrid1Editbefore(HttpServletRequest request,TGrid1 operatebean) {
		return null;
	}
	protected Map<String,Object> doGrid2Editbefore(HttpServletRequest request,TGrid2 operatebean) {
		return null;
	}
	
	protected TGrid1 doGrid1Savebefore(HttpServletRequest request,
			HttpServletResponse response, TGrid1 operatebean) {
		return operatebean;
	}
	protected TGrid2 doGrid2Savebefore(HttpServletRequest request,
			HttpServletResponse response, TGrid2 operatebean) {
		return operatebean;
	}
	
	protected boolean isJbpm() {
		return false;
	}
	
	protected String getGrid1ObjId(TGrid1 operatebean) {
		return "";
	}
	
	protected String getGrid2ObjId(TGrid2 operatebean) {
		return "";
	}
	//通过任务调度完成此调用	
	public String doChecked(WorkflowManager manager,String operateId) {	
		return "";
	}

}

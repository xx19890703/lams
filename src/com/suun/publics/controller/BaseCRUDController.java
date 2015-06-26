package com.suun.publics.controller;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CRUD典型Controller规范类

 * @param <T> CRUD所管理的对象类型 
 */
public abstract class BaseCRUDController<T> extends BaseController{
	
	@Autowired
	protected WorkflowManager manager;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());	
	
	private String ObjName=this.getClass().getSimpleName().substring(0,1).toLowerCase().
			               concat(this.getClass().getSimpleName().substring(1,this.getClass().getSimpleName().indexOf("Controller")));

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping
	public void exportlist(HttpServletRequest request,HttpServletResponse response) throws Exception {		
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
		Class beanClass=(Class<T>)((java.lang.reflect.ParameterizedType)getClass()
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
				getAllRecords(request,condition),
				beanClass);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping
	@ResponseBody	
	public Map<String, Object> lists(HttpServletRequest request) throws Exception {
		try {
			Class beanClass=(Class<T>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[0];
			Page<T> page = new Page<T>(10, true);
            GridData<T> gridData=new GridData<T>(beanClass,page,request);	
            T operatebean=(T)request.getSession().getAttribute("entity");
            page.setPosionRes(operatebean);
            if (operatebean!=null){
				page.getCondition().getFilterInfos().clear();
				request.getSession().removeAttribute("entity");
            }
			page = this.getPageRecords(request,page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}
	
	@RequestMapping
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response,T operatebean) {
		ModelAndView modelandview=new ModelAndView("input");
		Map<String,Object> model=doNewbefore(request);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, String suunplatformeoperateid){ 
	    //有审批流程
		T operatebean=getRecordSet(request,suunplatformeoperateid);		
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	List<HistoryProcessInstance> pis=
					JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getObjId(operatebean));
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
		String className=ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("input",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		modelandview.getModel().put("isEdit", "true");  
		Map<String,Object> model=doEditbefore(request,operatebean);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "Edit");
		return modelandview;	
	}
    
	@RequestMapping
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request,
			HttpServletResponse response, T operatebean) {
		boolean isNew=request.getSession().getAttribute("OperateState").equals("New");
		//request.getSession().setAttribute("OperateState", "Save");
		//request.getSession().setAttribute("Save", "true");
		
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();	
        operatebean=doSavebefore(request,response,operatebean);
        String error=saveRecordSet(request,operatebean);
        //request.getSession().removeAttribute("OperateState");
		if (Utils.isNullOrTemp(error)){
        	//有审批流程
        	if (isJbpm()){ 
        		if (isNew){
        			manager.writelog(ObjName,getObjId(operatebean),"新增",true);
        		} else{        			
        			try{
        				JbpmTemplate.getJbpmTemplate().deleteProcessInstance(ObjName,getObjId(operatebean)); 
    				}catch(Exception e){
    					
    				}
        			manager.writelog(ObjName,getObjId(operatebean),"修改",true);
        		}
        		JbpmTemplate.getJbpmTemplate().addProcessInstance(ObjName,getObjId(operatebean));   			
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
	public Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response,String ids) {	
		String[] iditenms=ids.split(",");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//有审批流程			
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	for (int i=0;i<iditenms.length;i++){
	    		T operatebean=getRecordSet(request,iditenms[i]);
	    		List<HistoryProcessInstance> pis=
						JbpmTemplate.getJbpmTemplate().getHisProcessInstances(ObjName+"."+getObjId(operatebean));
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
		
		String error=deleteRecordSet(request,iditenms);
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
	
	protected abstract String deleteRecordSet(HttpServletRequest request,String[] ids);
	
	protected abstract String saveRecordSet(HttpServletRequest request,T operatebean);

	/*
	 * 分页获取记录集
	 */
	protected abstract Page<T> getPageRecords(HttpServletRequest request,Page<T> page);
	/*
	 * 获取所有条件记录集
	 */
	protected abstract List<T> getAllRecords(HttpServletRequest request,Condition condition);
	
	protected abstract T getRecordSet(HttpServletRequest request,String operateid);
	
	protected Map<String,Object> doNewbefore(HttpServletRequest request) {
		return null;
	}

	protected Map<String,Object> doEditbefore(HttpServletRequest request,T operatebean) {
		return null;
	}
	
	protected T doSavebefore(HttpServletRequest request,
			HttpServletResponse response, T operatebean) {
		return operatebean;
	}
	
	protected boolean isJbpm() {
		return false;
	}
	
	protected String getObjId(T operatebean) {
		return "";
	}
	//通过任务调度完成此调用	
	public String doChecked(WorkflowManager manager,String operateId) {	
		return "";
	}
}

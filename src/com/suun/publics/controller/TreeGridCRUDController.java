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

import com.fr.base.StringUtils;
import com.suun.publics.Grid.ExportColumnInfo;
import com.suun.publics.Grid.GridData;
import com.suun.publics.Grid.GridUtils;
import com.suun.publics.Grid.export.ExportService;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.TreeObj;
import com.suun.publics.utils.Utils;
import com.suun.publics.workflow.JbpmTemplate;
import com.suun.service.system.workflow.WorkflowManager;



/**
 * TreeGrid CRUD典型Controller规范类
 * @param <T1> TreeGrid CRUD所管理的对象类型 
 */
public abstract class TreeGridCRUDController<TTree,TGrid> extends BaseController {
	
	
	/*private List<T> list = new ArrayList<T>();
	
	public List<T> getList() {
		return list;
	}
*/
    protected Logger logger = LoggerFactory.getLogger(getClass());
	
	//private Page<T> page = new Page<T>(10, true);
	
	private String ObjName=this.getClass().getSimpleName().substring(0,1).toLowerCase().
			               concat(this.getClass().getSimpleName().substring(1,this.getClass().getSimpleName().indexOf("Controller")));
	@Autowired
	protected WorkflowManager manager;
	
	@RequestMapping
	@ResponseBody	
	public List<Object> treelists (HttpServletRequest request) throws Exception {
		List<Object> model = new ArrayList<Object>();
		String node=request.getParameter("groupId");
		if (node==null){
			node="0";
		}
		List<TTree> list=this.getTreeAllRecords(node);
		for(TTree t:list){
			TreeObj obj= new TreeObj();
			obj.setId(getTreeId(t));
			obj.setText(getTreeName(t));
			obj.setParentId(getTreePid(t));
			obj.setChecked(false);
			if(getTreeLeaf(t)==0){
				obj.setLeaf(false);
			}else{
				obj.setLeaf(true);
			}
			if(getTreeIcon(request,t)!=null){
				obj.setIcon(request.getContextPath()+getTreeIcon(request,t));
			}
			model.add(obj);
		}		
		return model;
	}
	
	@RequestMapping
	public ModelAndView tree_new(HttpServletRequest request,HttpServletResponse response,String suunplatformTreePid) {	
		ModelAndView modelandview=new ModelAndView("treeinput");			
		if (suunplatformTreePid==null) suunplatformTreePid="0";
		modelandview.getModel().put("suunplatformTreePid", suunplatformTreePid);
		if (!suunplatformTreePid.equals("0")){
			TTree operatebean=getTreeRecordSet(suunplatformTreePid);
			modelandview.getModel().put("suunplatformTreePName",getTreeName(operatebean));
		} else{
			modelandview.getModel().put("suunplatformTreePName","无");
		}
		Map<String,Object> model=doTreeNewbefore();
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		return modelandview;
	}
	
	@RequestMapping
	public ModelAndView treeedit(HttpServletRequest request,HttpServletResponse response, String suunplatformTreePid) {	
		TTree operatebean=getTreeRecordSet(suunplatformTreePid);
		String className=ClassUtils.getShortName(((TTree)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("treeinput",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		if (!getTreePid(operatebean).equals("0")){
			TTree poperatebean=getTreeRecordSet(getTreePid(operatebean));
			modelandview.getModel().put("suunplatformTreePName",getTreeName(poperatebean));
		} else{
			modelandview.getModel().put("suunplatformTreePName","无");
		}
		modelandview.getModel().put("suunplatformTreePid", getTreePid(operatebean));
		modelandview.getModel().put("isEdit", "true");	
		Map<String,Object> model=doTreeEditbefore();
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		return modelandview;	
	}
    
	@RequestMapping
	@ResponseBody
	public Map<String, Object> treesave(HttpServletRequest request,HttpServletResponse response, TTree operatebean) {	
		request.getSession().setAttribute("Save", "true");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (getTreeId(operatebean).equals("0")){
        	modelMap.put("success", "false");
        	modelMap.put("error", "编号不能为0！");
        }else{
        	String error=treeSaveRecordSet(operatebean);
            if (StringUtils.isEmpty(error)){
            	modelMap.put("success", "true");
            	modelMap.put("saveid", getTreeId(operatebean));
            	modelMap.put("savename", getTreeName(operatebean));
            	if(getTreeIcon(request,operatebean)!=null){
            		modelMap.put("saveicon", request.getContextPath()+getTreeIcon(request,operatebean));
    			}            	
            } else{
            	modelMap.put("success", "false");
            	modelMap.put("error", error);
            }
        }        
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> treedelete(HttpServletRequest request,HttpServletResponse response,String ids) {	
		String[] iditenms=ids.split(",");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String error=treeDeleteRecordSet(iditenms);
		if (StringUtils.isEmpty(error)){        		
		    modelMap.put("success", "true"); 
		}else{
			modelMap.put("success", "false"); 
			modelMap.put("error", error);
		}
		return modelMap;	
	}
	
	public abstract String treeDeleteRecordSet(String[] ids);
	
	public abstract String treeSaveRecordSet(TTree operatebean);

    // 根据父编号，获取所有条件记录集
	public abstract List<TTree> getTreeAllRecords(String Pid);
	
	public abstract TTree getTreeRecordSet(String operateid);
	
	public abstract String getTreeId(TTree t);
	
	public abstract String getTreeName(TTree t);
	
	public abstract String getTreePid(TTree t);
	
	public abstract String getTreeIcon(HttpServletRequest request,TTree t);
	
	public abstract int getTreeLeaf(TTree t);
	
	public Map<String,Object> doTreeNewbefore() {
		return null;
	}

	public Map<String,Object> doTreeEditbefore() {
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping
	public void gridexportlist(HttpServletRequest request,HttpServletResponse response) throws Exception {		
		String treeid = request.getParameter("treeid");
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
		Class beanClass=(Class<TGrid>)((java.lang.reflect.ParameterizedType)getClass()
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
				getGridAllRecords(request,treeid,condition),
				beanClass);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping
	@ResponseBody	
	public Map<String, Object> gridlists(HttpServletRequest request) throws Exception {
		try {
			Class beanClass=(Class<TGrid>)((java.lang.reflect.ParameterizedType)getClass()
				     .getGenericSuperclass()).getActualTypeArguments()[1];
			String treeid = request.getParameter("treeid");
			Page<TGrid> page = new Page<TGrid>(10, true);
            GridData<TGrid> gridData=new GridData<TGrid>(beanClass,page,request);	
            TGrid operatebean=(TGrid)request.getSession().getAttribute("entity");
            page.setPosionRes(operatebean);
            if (operatebean!=null){
				page.getCondition().getFilterInfos().clear();
				request.getSession().removeAttribute("entity");
            }
			page = this.getGridPageRecords(request,treeid,page);
			return gridData.getGridData(page);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;		
	}
	
	@RequestMapping
	public ModelAndView grid_new(HttpServletRequest request,
			HttpServletResponse response,String treeid) {
		ModelAndView modelandview=new ModelAndView("gridinput");
		modelandview.getModel().put("treeid", treeid);
		Map<String,Object> model=doGridNewbefore(request,treeid);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}		
		TGrid operatebean=getGridClass();
		modelandview.getModel().put("jsonbean", Utils.Object2Json(operatebean));
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	public ModelAndView gridedit(HttpServletRequest request,
			HttpServletResponse response, String treeid, String suunplatformeoperateid){ 
	    //有审批流程
		TGrid operatebean=getGridRecordSet(request,treeid,suunplatformeoperateid);		
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
		
		String className=ClassUtils.getShortName(((TGrid)operatebean).getClass().getName()).toLowerCase();
		if (className.lastIndexOf("_")==className.length()-1){
			className=className.substring(0, className.length()-1);
		}
		ModelAndView modelandview=new ModelAndView("gridinput",className/*ClassUtils.getShortName(((T)operatebean).getClass().getName()).toLowerCase().replaceAll("[_]", "")*/,operatebean);
		modelandview.getModel().put("isEdit", "true");  
		modelandview.getModel().put("treeid", treeid);
		modelandview.getModel().put("jsonbean", Utils.Object2Json(operatebean));
		Map<String,Object> model=doGridEditbefore(request,treeid,operatebean);
		if (model!=null){
			modelandview.getModel().putAll(model);
		}
		request.getSession().setAttribute("OperateState", "Edit");
		return modelandview;	
	}
    
	@RequestMapping
	@ResponseBody
	public Map<String, Object> gridsave(HttpServletRequest request,
			HttpServletResponse response, TGrid operatebean) {
		boolean isNew=request.getSession().getAttribute("OperateState").equals("New");
		//request.getSession().setAttribute("OperateState", "Save");
		//request.getSession().setAttribute("Save", "true");
		
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();	
        operatebean=doGridSavebefore(request,response,operatebean);
        String error=saveGridRecordSet(request,operatebean);
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
	public Map<String, Object> griddelete(HttpServletRequest request,
			HttpServletResponse response,String treeid,String ids) {	
		String[] iditenms=ids.split(",");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//有审批流程			
	    if (isJbpm()){ 
	    	boolean isended=false;
	    	for (int i=0;i<iditenms.length;i++){
	    		TGrid operatebean=getGridRecordSet(request,treeid,iditenms[i]);
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
		
		String error=deleteGridRecordSet(request,treeid,iditenms);
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
	
	protected abstract String deleteGridRecordSet(HttpServletRequest request,String treeid,String[] ids);
	
	protected abstract String saveGridRecordSet(HttpServletRequest request,TGrid operatebean);

	/*
	 * 分页获取记录集
	 */
	protected abstract Page<TGrid> getGridPageRecords(HttpServletRequest request,String treeid,Page<TGrid> page);
	/*
	 * 获取所有条件记录集
	 */
	protected abstract List<TGrid> getGridAllRecords(HttpServletRequest request,String treeid,Condition condition);
	
	protected abstract TGrid getGridRecordSet(HttpServletRequest request,String treeid,String operateid);
	
	protected TGrid getGridClass(){
		return null;
	}
	protected Map<String,Object> doGridNewbefore(HttpServletRequest request,String treeid) {
		return null;
	}

	protected Map<String,Object> doGridEditbefore(HttpServletRequest request,String treeid,TGrid operatebean) {
		return null;
	}
	
	protected TGrid doGridSavebefore(HttpServletRequest request,
			HttpServletResponse response, TGrid operatebean) {
		return operatebean;
	}
	
	protected boolean isJbpm() {
		return false;
	}
	
	protected String getObjId(TGrid operatebean) {
		return "";
	}
	//通过任务调度完成此调用	
	public String doChecked(WorkflowManager manager,String operateId) {	
		return "";
	}

}

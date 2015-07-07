package com.suun.controller.system.demo;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suun.model.system.demo.Demo;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.jCaptcha.CaptchaService;
import com.suun.publics.system.DicService;
import com.suun.service.data.DataminingManager;
import com.suun.service.system.workflow.WorkflowManager;


/**
 * @author zyfang
 * 
 */
@Controller 
public class DemoController extends BaseCRUDController<Demo>{
	
	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.system.demo.DemoManager demoManager; 
	
	@Autowired
	DataminingManager dataminingManager;
	
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("sel_state",DicService.GetDataNameExtArrayByDicNo("STATE"));	 
		return modelandview;
	}	
	
	/*@RequestMapping
	@Override
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response) {	
		Authority authority=new Authority();
		authority.setAuthId("AUTH_ALL");
		return new ModelAndView(getBasePath(request)+"input","authority",authority);	
	}*/
	@RequestMapping
	@ResponseBody
	public List<String> onlineuser(HttpServletRequest request,
			HttpServletResponse response) {			
		List<String> onlineusers=new ArrayList<String>();
		for(Object principal:sessionRegistry.getAllPrincipals()){
			if (principal instanceof User) {
				User user = (User) principal;
				onlineusers.add(user.getUsername());
			}
		}
		return onlineusers;
	}
	
	@RequestMapping
	@ResponseBody
	public String validateAuthority(HttpServletRequest request,
			HttpServletResponse response) {	
		String id = request.getParameter("authId");
		String oldid = request.getParameter("oldid");
		if (demoManager.isAthorityIdUnique(id, oldid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}
	@Override
	public Page<Demo> getPageRecords(HttpServletRequest request,Page<Demo> page) {
		return demoManager.getAllDemos(page);
	}

	@Override
	public List<Demo> getAllRecords(HttpServletRequest request,Condition condition) {
		return demoManager.getAllDemos(condition);
	}

	@Override
	public Demo getRecordSet(HttpServletRequest request,String operateid) {
		return demoManager.getDemo(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	demoManager.deleteDemo(ids[i]);
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
	public String saveRecordSet(HttpServletRequest request,Demo operatebean) {
		try{
			demoManager.saveDemo(operatebean);
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
	
	
	/*验证码	*/
	@RequestMapping
	public void imageCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		  CaptchaService.generateImageCode(request, response);		 
		  return ; 
	}
	
	@RequestMapping
	@ResponseBody
	public String ajaxdemo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		com.suun.publics.utils.EncoderRequest erequest=new com.suun.publics.utils.EncoderRequest(request);
		erequest.get("ss");
		return renderText(response,"true"); 
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> datatest(HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		String contractId=request.getParameter("contractId");
		//List<String> result = dataminingManager.findTableData(contractId,"contract_info");
		List<String> result2 = dataminingManager.findTableData(contractId,"contract_mode");
		List<String> tables = dataminingManager.findModeTables(contractId);
		for(String tableName:tables){
			List<String> tabledatas = dataminingManager.findTableData(contractId,tableName);
			for(String tt:tabledatas){
				System.out.println(tt);
			}
		}
		//modelMap.put("list", result);
		modelMap.put("list2", result2);
		
		//dataminingManager.insertData("insert into table1 ( contractId ,col1 ,col2 ,col3  ) values (  'tttaaaaa'  ,'tt123'  ,'tt2'  ,'tt'  );");
		return modelMap;
	}
	
	
	@Override
	public boolean isJbpm() {
		return true;
	}
	@Override
	public String getObjId(Demo operatebean) {
		return operatebean.getAuthId();
	}
	@Override
	@RequestMapping
	public String doChecked(WorkflowManager manager,String operateId) {	
		manager.writelog("demo","我的测试","新增",true);
		return "";
	}
	
}

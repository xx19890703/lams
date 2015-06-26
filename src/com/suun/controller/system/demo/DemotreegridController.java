package com.suun.controller.system.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.demo.Demomain;
import com.suun.model.system.demo.Demomaindetail;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;


@Controller 
public class DemotreegridController extends TreeGridCRUDController<Demomain,Demomaindetail> {

	@Autowired
	com.suun.service.system.demo.DemoMainDetailManager demoManager; 
	
	@InitBinder  
    public void initBinder(WebDataBinder binder) {   
        // 忽略字段绑定异常   
        // binder.setIgnoreInvalidFields(true);     
        binder.registerCustomEditor(Date.class, "ddemo",   
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));   
    }  
	
	@RequestMapping
	@ResponseBody
	public String treeValidateOid(HttpServletRequest request,HttpServletResponse response) {	
		Long oid = Long.valueOf(request.getParameter("mid"));
		Long oldoid = request.getParameter("oldmid").equals("")?null:Long.valueOf(request.getParameter("oldmid"));
		
		if(demoManager.isDemomainUnique(oid, oldoid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public String treeValidateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		if(demoManager.isDemomainNameUnique(name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	demoManager.deleteDemomain(Long.valueOf(ids[i]));
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
    //根据编号查到所属所有的子节点
	@Override
	public List<Demomain> getTreeAllRecords(String Pid) {
		return demoManager.getAllDemomain(Long.valueOf(Pid));
	}

	@Override
	public Demomain getTreeRecordSet(String operateid) {
		return demoManager.getDemomain(Long.valueOf(operateid));
	}

	@Override
	public String treeSaveRecordSet(Demomain operatebean) {
		try{
			demoManager.saveDemomain(operatebean);
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

	@Override
	public String getTreeId(Demomain t) {
		return t.getMid().toString();
	}
	@Override
	public String getTreeName(Demomain t) {		
		return t.getMname();
	}

	@Override
	public String getTreePid(Demomain t) {		
		return t.getPmid().toString();
	}

	@Override
	public String getTreeIcon(HttpServletRequest request,Demomain t) {
		return null;
	}

	@Override
	public int getTreeLeaf(Demomain t) {
		return 0;
	}
	

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,Demomaindetail operatebean) {
		try{
			demoManager.saveDemomaindetail(operatebean);
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

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,String treeid, String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	demoManager.deleteDemomaindetail(Long.valueOf(treeid),Long.valueOf(ids[i]));
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
	protected Page<Demomaindetail> getGridPageRecords(HttpServletRequest request,String treeid,
			Page<Demomaindetail> page) {		
		return demoManager.getAllDemomaindetail(treeid,page);
	}

	@Override
	protected List<Demomaindetail> getGridAllRecords(HttpServletRequest request,String treeid,
			Condition condition) {
		return demoManager.getAllDemomaindetail(treeid, condition);
	}

	@Override
	protected Demomaindetail getGridRecordSet(HttpServletRequest request,String treeid, String operateid) {
		return demoManager.getDemomaindetail(Long.valueOf(treeid), Long.valueOf(operateid));
	}
	
	@RequestMapping
	@ResponseBody
	public String validateAuthorityDetail(HttpServletRequest request,
			HttpServletResponse response) {	
		String mid = request.getParameter("mid");
		String did = request.getParameter("did");
		Long olddid = request.getParameter("olddid").equals("")?null:Long.valueOf(request.getParameter("olddid"));
		if (demoManager.isDemomaindetailUnique(Long.valueOf(mid), Long.valueOf(did), olddid))
			return renderText(response,"true");
		else
			return renderText(response,"false");
	}



}

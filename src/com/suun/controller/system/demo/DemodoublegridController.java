package com.suun.controller.system.demo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.demo.Demomain;
import com.suun.model.system.demo.Demomaindetail;
import com.suun.publics.controller.DoubleGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;

/**
 * Controller
 * @author 
 *
 */

@Controller 
public class DemodoublegridController extends DoubleGridCRUDController<Demomain,Demomaindetail> {

	@Autowired
	com.suun.service.system.demo.DemoMainDetailManager demoManager; 

	@Override
	protected Page<Demomain> getGrid1PageRecords(HttpServletRequest request,
			Page<Demomain> page) {
		return demoManager.getAllDemomain(page);
	}
	@RequestMapping
	@ResponseBody
	public String grid1ValidateOid(HttpServletRequest request,HttpServletResponse response) {	
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
	public String grid1ValidateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		if(demoManager.isDemomainNameUnique(name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@Override
	public String deleteGrid1RecordSet(HttpServletRequest request,String[] ids) {
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
	public List<Demomain> getGrid1AllRecords(HttpServletRequest request,Condition condition) {
		 return demoManager.getAllDemomain();
	}

	@Override
	public Demomain getGrid1RecordSet(HttpServletRequest request,String operateid) {		
		return demoManager.getDemomain(Long.valueOf(operateid));
	}

	@Override
	public String saveGrid1RecordSet(HttpServletRequest request,Demomain operatebean) {
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
	protected String saveGrid2RecordSet(HttpServletRequest request,Demomaindetail operatebean) {
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
	protected String deleteGrid2RecordSet(HttpServletRequest request,String parentid, String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	demoManager.deleteDemomaindetail(Long.valueOf(parentid),Long.valueOf(ids[i]));
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
	protected Page<Demomaindetail> getGrid2PageRecords(HttpServletRequest request,String parentid,
			Page<Demomaindetail> page) {		
		return demoManager.getAllDemomaindetail(parentid,page);
	}

	@Override
	protected List<Demomaindetail> getGrid2AllRecords(HttpServletRequest request,String parentid,
			Condition condition) {
		return demoManager.getAllDemomaindetail(parentid, condition);
	}

	@Override
	protected Demomaindetail getGrid2RecordSet(HttpServletRequest request,String parentid, String operateid) {
		return demoManager.getDemomaindetail(Long.valueOf(parentid), Long.valueOf(operateid));
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

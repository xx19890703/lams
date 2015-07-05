package com.suun.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.system.DicManager;

@Controller 
public class DicController extends TreeGridCRUDController<Dic,Dic_data>{

	@Autowired
	DicManager manager;
	
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		return null;
	}

	@Override
	public String treeSaveRecordSet(Dic operatebean) {
		return null;
	}

	@Override
	public List<Dic> getTreeAllRecords(String Pid) {		
		return manager.findAllDicsNoSys();
	}

	@Override
	public Dic getTreeRecordSet(String operateid) {
		return null;
	}

	@Override
	public String getTreeId(Dic t) {		
		return t.getDic_no();
	}

	@Override
	public String getTreeName(Dic t) {
		return t.getDic_name();
	}

	@Override
	public String getTreePid(Dic t) {
		return "0";
	}

	@Override
	public String getTreeIcon(HttpServletRequest request, Dic t) {
		return null;
	}

	@Override
	public int getTreeLeaf(Dic t) {
		return 1;
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,
			String treeid, String[] ids) {
		try{
			for (int i=0;i<ids.length;i++){			
				manager.delete(treeid,ids[i]);
			}
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,
			Dic_data operatebean) {
		try{
			manager.save(operatebean);
			return "";
		}catch (Exception e){
			return e.getMessage();
		}		
	}

	@Override
	protected Page<Dic_data> getGridPageRecords(HttpServletRequest request,
			String treeid, Page<Dic_data> page) {
		return manager.findDicsByDicno(treeid,page);
	}

	@Override
	protected List<Dic_data> getGridAllRecords(HttpServletRequest request,
			String treeid, Condition condition) {
		return manager.findDicsByDicno(treeid);
	}

	@Override
	protected Dic_data getGridRecordSet(HttpServletRequest request,
			String treeid, String operateid) {
		return manager.getByKey(treeid, operateid);
	}
	
	@RequestMapping
	@ResponseBody
	public String validateId(HttpServletRequest request,HttpServletResponse response) {	
		String tid = request.getParameter("dicid");
		String oldid = request.getParameter("oldid");	
		String id = request.getParameter("key.data_no");
		if(manager.isIdUnique(tid, id,oldid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
}

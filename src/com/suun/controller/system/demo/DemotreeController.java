package com.suun.controller.system.demo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.demo.Demotree;
import com.suun.publics.controller.TreeCRUDController;
import com.suun.service.system.demo.DemotreeManager;


@Controller 
public class DemotreeController extends TreeCRUDController<Demotree> {
	
	@Autowired
	DemotreeManager demotreeanager;
	
	@RequestMapping
	@ResponseBody
	public String validateOid(HttpServletRequest request,HttpServletResponse response) {	
		String oid = request.getParameter("oid");
		String oldoid = request.getParameter("oldoid");
		
		if(demotreeanager.isDemotreeIdUnique(oid, oldoid)){
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
		
		if(demotreeanager.isDemotreeNameUnique(pid, name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
			Boolean flag=null;
            for (int i=0;i<ids.length;i++){
            	List<Demotree> orglist=demotreeanager.findOrgByParentId(ids[i]);
            	//判断节点是否是父节点，长度为0表示不是父节点可以删除，反之就是父节点不可删除。
            	if(orglist.size()==0){
            		demotreeanager.deleteOrgzation(ids[i]);
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
	public List<Demotree> getAllRecords(HttpServletRequest request,String Pid) {
		return demotreeanager.findOrgByParentId(Pid);
	}

	@Override
	public Demotree getRecordSet(HttpServletRequest request,String operateid) {		
		if (operateid.equals("0")){
			return new Demotree();
		}else
			return demotreeanager.getOrgzation(operateid);
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,Demotree operatebean) {
		try{
			demotreeanager.saveOrgzation(operatebean);
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
	public String getTreeId(Demotree t) {
		return t.getOid().toString();
	}
	@Override
	public String getTreeName(Demotree t) {		
		return t.getName();
	}

	@Override
	public String getTreePid(Demotree t) {		
		return t.getPid();
	}

	@Override
	public String getTreeIcon(Demotree t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTreeLeaf(Demotree t) {
		// TODO Auto-generated method stub
		return 0;
	}
}

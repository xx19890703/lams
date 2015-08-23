package com.suun.controller.serviceuser;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.DownLoadRecord;
import com.suun.model.system.Dic_data;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.system.DicService;
import com.suun.service.serviceuser.ContractCategoryManager;
import com.suun.service.serviceuser.DownLoadRecordManager;
import com.suun.service.system.DicManager;


/**
 * @author renlq
 * 
 */
@Controller 
public class DownLoadRecordController extends BaseCRUDController<DownLoadRecord>{
	
	@Autowired
	DownLoadRecordManager manager; 
	@Autowired
	ContractCategoryManager cmanager;
	@Autowired 
	DicManager dicManager;
	
	@Override
	public Page<DownLoadRecord> getPageRecords(HttpServletRequest request,Page<DownLoadRecord> page) {
		return manager.getAllDownLoadRecord(page);
	}

	@Override
	public List<DownLoadRecord> getAllRecords(HttpServletRequest request,Condition condition) {
		return manager.getAllDownLoadRecord(condition);
	}

	@Override
	public DownLoadRecord getRecordSet(HttpServletRequest request,String operateid) {
		return manager.getDownLoadRecord(Long.parseLong(operateid));
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		try{
            for (int i=0;i<ids.length;i++){
            	manager.deleteDownLoadRecord(Long.parseLong(ids[i]));
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
	public String saveRecordSet(HttpServletRequest request,DownLoadRecord operatebean) {
		try{
			manager.saveDownLoadRecord(operatebean);
			//更新合同信息
			ContractDetail contract = cmanager.getContractDetail(operatebean.getContractid().getDid());
			contract.setImportTime(new Date());
			contract.setStatus(dicManager.getByKey("STATUS", "B"));
			if(contract.getImportCount()==null)
				contract.setImportCount(1);
			else
				contract.setImportCount(contract.getImportCount()+1);
			cmanager.saveContractDetail(contract);
			
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
	protected Map<String, Object> doNewbefore(HttpServletRequest request) {
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects1", DicService.GetDicByDicNo("status"));
		return rmap;
	}

	@Override
	protected Map<String, Object> doEditbefore(HttpServletRequest request,DownLoadRecord operatebean) {
		Map<String,Object> rmap = doNewbefore(request);
		return rmap;
	}
}

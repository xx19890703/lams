package com.suun.controller.serviceuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.TreeObj;
import com.suun.service.serviceuser.ContractCategoryManager;
import com.suun.service.serviceuser.ContractTemplateResManager;

@Controller
public class QueryController extends BaseCRUDController<ContractTemplateRes> {

	@Autowired
	ContractTemplateResManager manager;

	@Autowired
	ContractCategoryManager mainManager;

	@Override
	protected String deleteRecordSet(HttpServletRequest request, String[] ids) {
		return null;
	}

	@Override
	protected String saveRecordSet(HttpServletRequest request, ContractTemplateRes operatebean) {
		return null;
	}

	@Override
	protected Page<ContractTemplateRes> getPageRecords(HttpServletRequest request, Page<ContractTemplateRes> page) {
		String contactId = request.getParameter("contractId");
		Page<ContractTemplateRes> pp = manager.getContractTemplateResByContractId(contactId, page);
		return pp;
	}

	@RequestMapping
	@ResponseBody
	public List<Object> queryTemplate(HttpServletRequest request, Page<ContractTemplateRes> page) {
		List<Object> model = new ArrayList<Object>();
		String contactId = request.getParameter("contractId");
		List<ContractTemplateRes> list = manager.getContractTemplateResByContractId(contactId);
		//modelMap.put("root", list);
		for(ContractTemplateRes ct:list){
			TreeObj obj= new TreeObj();
			obj.setId(ct.getTemplate().getPath().replaceAll("\\\\", "/"));
			obj.setText(ct.getName());
			obj.setParentId("0");
			obj.setLeaf(true);
			model.add(obj);
		}
		return model;
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryContract(HttpServletRequest request, Page<ContractTemplateRes> page) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String zzcid = request.getParameter("zzcid");
		String htmc = request.getParameter("htmc");
		String htbh = request.getParameter("htbh");
		String ssdd = request.getParameter("ssdd");
		List<ContractDetail> list = mainManager.getAllContractDetails(zzcid,htmc,htbh,ssdd);
		modelMap.put("root", list);
		return modelMap;
	}

	@Override
	protected List<ContractTemplateRes> getAllRecords(HttpServletRequest request, Condition condition) {
		return null;
	}

	@Override
	protected ContractTemplateRes getRecordSet(HttpServletRequest request, String operateid) {
		return null;
	}

}

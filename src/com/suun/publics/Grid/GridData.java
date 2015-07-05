package com.suun.publics.Grid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.Utils;

public class GridData<T> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes") 	
	public GridData(Class clazz,Page<T> page,HttpServletRequest request){
		//start,page,_dc,sort,dir,limit,query
		//页号
		String pageno=Utils.isNullOrTemp(request.getParameter("start"))?"1":
			String.valueOf(Long.valueOf(request.getParameter("start"))/Long.valueOf(request.getParameter("limit"))+1);
		page.setPageNo(Integer.valueOf(pageno));
		//页记录数
		String limit=Utils.isNullOrTemp(request.getParameter("limit"))?"10":request.getParameter("limit");
		page.setPageSize(Integer.valueOf(limit));
		try {
			Condition condition = GridUtils.getConditionFromRequest(clazz, request);
			page.setCondition(condition);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	
	public Map<String, Object> getGridData(Page<T> page){
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("page", page.getPageNo());
		modelMap.put("curpagepos", page.getCurPagePos());
		modelMap.put("total", page.getTotalCount()); 
		modelMap.put("data", page.getResult());		
		return modelMap;			
	}
 
}

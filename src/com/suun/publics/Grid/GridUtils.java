package com.suun.publics.Grid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.OrderBy;
import com.suun.publics.utils.Utils;

public class GridUtils {
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	private static Object castValue(Class clazz,String fieldname,String value) throws ParseException{
		Class fclazz=String.class;
		try {
			fclazz=Utils.getFieldType(clazz, fieldname);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			java.lang.reflect.Method method=null;
			try {
				method = clazz.getMethod(Utils.field2GetMethod(fieldname));
				if (method!=null){
					fclazz=method.getReturnType();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (fclazz==String.class){
		    return value;
		}else{
			if (Utils.isNullOrTemp(value)) {
				return null;
			}
			if (fclazz==Date.class){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
				return sdf.parse(value.substring(0, 10));	
			}else if (fclazz==Double.class||fclazz==Float.class){
				return Float.valueOf(value);
			} else{//Byte.Integer.Long.Short
				return Long.valueOf(value);
			}					
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Condition getConditionFromRequest(Class clazz,HttpServletRequest request) throws ParseException{		
		//排序字段
		String sort=request.getParameter("sort");
		//排序方向
		String dir=request.getParameter("dir");
		
		Condition condition=new Condition();
		List<OrderBy> orderBys = new ArrayList<OrderBy>();
		if (!Utils.isNullOrTemp(sort)){
			OrderBy orderBy=new OrderBy();
			orderBy.setFieldName(sort);
	  		orderBy.setAsc(!dir.equalsIgnoreCase("desc"));
	  		orderBys.add(orderBy);
		}
		condition.setOrderBys(orderBys);
		//查询字段
		String queryId=request.getParameter("SearchId");
		String queryValue = request.getParameter("query");
		
		List<com.suun.publics.hibernate.FilterInfo> filterInfos = new ArrayList<com.suun.publics.hibernate.FilterInfo>();
		if (!Utils.isNullOrTemp(queryValue)){
			try {
				com.suun.publics.hibernate.FilterInfo filterInfo=new com.suun.publics.hibernate.FilterInfo();
				filterInfo.setFieldName(queryId);
				Class fclazz=Utils.getFieldType(clazz, queryId);
				if (fclazz==String.class){
				    filterInfo.setLogic(Logic.LIKE);
				    filterInfo.setValue(queryValue);
				}else{
					filterInfo.setLogic(Logic.EQUAL);
					filterInfo.setValue(castValue(clazz, queryId,queryValue));	
				}				
				filterInfos.add(filterInfo);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				java.lang.reflect.Method method=null;
				try {
					method = clazz.getMethod(Utils.field2GetMethod(queryId));
					if (method!=null){
						com.suun.publics.hibernate.FilterInfo filterInfo=new com.suun.publics.hibernate.FilterInfo();
						filterInfo.setFieldName(queryId);
						if (method.getReturnType()==String.class){
						    filterInfo.setLogic(Logic.LIKE);
						    filterInfo.setValue(queryValue);
						}else{
							filterInfo.setLogic(Logic.EQUAL);
							filterInfo.setValue(castValue(clazz, queryId,queryValue));	
						}
						filterInfos.add(filterInfo);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}	
			}			
		} else{
			String zdvalues=request.getParameter("zdvalues")==null?"":request.getParameter("zdvalues");
			String czvalues=request.getParameter("czvalues")==null?"":request.getParameter("czvalues");
			String dpvalues=request.getParameter("dpvalues")==null?"":request.getParameter("dpvalues");
			String gxvalues=request.getParameter("gxvalues")==null?"":request.getParameter("gxvalues");
			String[] zdvaluess=zdvalues.split("@");
			String[] czvaluess=czvalues.split("@");
			String[] dpvaluess=dpvalues.split("@");
			String[] gxvaluess=gxvalues.split("@");//X@...@空格
			if(zdvaluess.length>1){
				for(int i=0;i<czvaluess.length-1;i++){					
					Object obj=castValue(clazz,zdvaluess[i],dpvaluess[i]);
					com.suun.publics.hibernate.FilterInfo filterInfo=new com.suun.publics.hibernate.FilterInfo();
					filterInfo.setFieldName(zdvaluess[i]);
					Logic logic=com.suun.publics.hibernate.FilterInfo.StrToLogic(czvaluess[i].toUpperCase());
					filterInfo.setLogic(logic);
					if (obj==null) {
						if (logic.equals(Logic.EQUAL)){
							filterInfo.setLogic(Logic.ISNULL);
						} else{
							filterInfo.setLogic(Logic.ISNOTNULL);
						}	
						filterInfo.setValue("");
					} else if (obj.equals("")){
						com.suun.publics.hibernate.FilterInfo filterInfo1=new com.suun.publics.hibernate.FilterInfo();
						filterInfo1.setFieldName(zdvaluess[i]);
						if (logic.equals(Logic.EQUAL)){
							filterInfo1.setLogic(Logic.ISNULL);
						} else{
							filterInfo1.setLogic(Logic.ISNOTNULL);
						}						
						filterInfo1.setValue("");	
						filterInfo1.setOr(true);
						filterInfos.add(filterInfo1);
						filterInfo.setValue("");
					} else {
						filterInfo.setValue(obj);
					}
					
					/*if (i>0){
						filterInfo.setOr("or".equals(gxvaluess[i-1]));
					} else{
						filterInfo.setOr(false);//此连接条件不会被使用
					}	*/
					filterInfo.setOr("or".equals(gxvaluess[i]));
					filterInfos.add(filterInfo);
				}
			}
		}
		condition.setFilterInfos(filterInfos);			
  	  
  	    return condition;  	      
	}

}

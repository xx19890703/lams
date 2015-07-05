package com.suun.publics.system;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.suun.model.system.Dic_data;

public class DicService{
 
	private static List<Dic_data> dic=null;
	@SuppressWarnings("unchecked")
	public static List<Dic_data> getDic(){
		if (dic==null){
			//ApplicationContext ctx=new ClassPathXmlApplicationContext(SpringPath);//"classpath:spring*.xml"
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
		    Session session=sessionFactory.openSession();
		    dic=session.createCriteria(Dic_data.class).list();
		    session.close();
		}
		return dic;
	}
	
	public static List<Dic_data> GetDicByDicNo(String dic_no){
		List<Dic_data> dicdata=getDic();
		List<Dic_data> rdicdata=new ArrayList<Dic_data>();
		for (int i=0;i<dicdata.size();i++){
			if ( (dicdata.get(i).getKey().getDic_no().equalsIgnoreCase(dic_no)) ){
				rdicdata.add(dicdata.get(i));
			}
		}
		return rdicdata;
	}
	
	public static String GetDic_dataName(String dic_no,String dic_datano){
		List<Dic_data> dicdata=getDic();
		for (int i=0;i<dicdata.size();i++){
			if ( (dicdata.get(i).getKey().getDic_no().equalsIgnoreCase(dic_no)) &&
			(dicdata.get(i).getKey().getData_no().equalsIgnoreCase(dic_datano)) ){
				return dicdata.get(i).getData_name();
			}
		}
		return "";
	}
	
	public static String GetDataNameExtArrayByDicNo(String dic_no){
		String aDataNames="";
		List<Dic_data> dicdata=getDic();
		for (int i=0;i<dicdata.size();i++){
			if ( (dicdata.get(i).getKey().getDic_no().equalsIgnoreCase(dic_no)) ){
				aDataNames+=",[\""+dicdata.get(i).getData_name()+"\"]";
			}
		}
		aDataNames="["+aDataNames.substring(1)+"]";
		return aDataNames;
	}
  
}

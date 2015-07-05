package com.suun.publics.Grid.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.suun.publics.Grid.ExportColumnInfo;
import com.suun.publics.utils.Utils;


public class ExportService {
	private static Log logger = LogFactory.getLog(ExportService.class);
	
	private static Map<String,AbstractWriter> writers = new HashMap<String,AbstractWriter>();
	public static int nullInt = -255;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public ExportService(HttpServletRequest request,HttpServletResponse response){
        this.request=request;
        this.response=response;
	}

	private void initAttachmentHeader(){
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0" );
		response.setHeader("Content-Type","application/force-download" );
		response.setHeader("Expires","0"); 
		response.setHeader("Content-Type","application/octet-stream" );
		response.setHeader("Content-Type","application/download" );
		response.setHeader("Cache-Control","private, max-age=0, must-revalidate" );
		response.setHeader("Pragma","public" );
	}
	
	private void downloadFile(String fileName){
		downloadFile(fileName,nullInt);
	}
	
	private void downloadFile(String fileName ,long  length){
		this.response.reset();
		initAttachmentHeader();
		String codedfilename=fileName;
		String agent = this.request.getHeader("USER-AGENT");  
		try {
			if (null != agent && -1 != agent.indexOf("MSIE")){  
			    codedfilename = java.net.URLEncoder.encode(fileName, "UTF8");  
			} else if (null != agent && -1 != agent.indexOf("Mozilla"))  {
				codedfilename =javax.mail.internet.MimeUtility.encodeText(fileName, "UTF8", "B");
			}
		} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		}      
		this.response.setHeader("Content-Disposition","attachment; filename=\""+codedfilename+"\"");		
		if (length!=nullInt){
			this.response.setHeader("Content-Length",String.valueOf(length) );
		}
	}
	
	private AbstractWriter getWriter(String expName,String[] headsName){
		AbstractWriter writer=null;
		try {
		   writer = (AbstractWriter)writers.get(expName);
		} catch (Exception e) {
			//
		} 
		if (writer==null){
			if (expName.equals("xls")){
				writer=new XlsWriter();
			} else if (expName.equals("xml")){
				writer=new XmlWriter(headsName);
			} else if (expName.equals("pdf")){
				writer=new PdfGWriter();
			} else if (expName.equals("doc")){	
				writer=new DocWriter();
	        } else if (expName.equals("csv")){
	        	writer=new CsvWriter();
			}   
			writers.put(expName, writer);
		}
		return writer;
	}
	
	@SuppressWarnings({"rawtypes" })
	public void export(String Exptype,String exportFileName,List exportColumnInfo,List data,Class beanClass) throws IOException{
		int l=exportColumnInfo.size();
		String[] properiesName=new String[l] ;
		String[] headsName=new String[l] ;
		for (int i = 0; i < exportColumnInfo.size(); i++){
			properiesName[i]=((ExportColumnInfo) exportColumnInfo.get(i)).getFieldIndex();
			headsName[i]=((ExportColumnInfo) exportColumnInfo.get(i)).getHeader();
			
		}
		downloadFile(exportFileName+"."+Exptype);		
		OutputStream out=this.response.getOutputStream();
		AbstractWriter xlsw= getWriter(Exptype,headsName);
		xlsw.init();
		xlsw.setOut(out);
		xlsw.setEncoding("GBK");
		xlsw.start();
		xlsw.addTitle(exportFileName);
		xlsw.addheadRow( headsName );
		if (beanClass==null || Map.class.isAssignableFrom(beanClass) ){
			for (int i=0,len=data.size();i<len;i++){
				Map record=(Map)data.get(i);
				xlsw.addRow(map2Array(record,properiesName));
			}
		}else{
			for (int i=0,len=data.size();i<len;i++){
				Object record= data.get(i);
				xlsw.addRow(bean2Array(record,properiesName,beanClass) );
			}
		}
		
		xlsw.end();		
		xlsw.close();     
	}
	
	@SuppressWarnings({"rawtypes" })
	public static Object[] map2Array(Map map,String[] properiesName){
		int len=properiesName.length;
		Object[] objs=new Object[ len ];
		for (int i=0;i<len;i++){
			objs[i]=map.get(properiesName[i]);
		}
		return objs;
 }	
 	 
 @SuppressWarnings({"rawtypes" })
public static Object[] bean2Array(Object bean, String[] properiesName , Class beanClass ) {
	 	int len=properiesName.length;
		Object[] objs=new Object[ len ];
		for (int i=0 ;i<len;i++){				
			objs[i]=properiesName2value(bean,properiesName[i]);
		}
		return objs;
 }
 
 public static Object properiesName2value(Object bean,String properiesName){
		String MethodName=null;
		Method method=null;
		Object res=bean;
		String s=properiesName;
		String ss=s;
		do {
			if (ss.indexOf(".")>=0){
			    MethodName=ss.substring(0,ss.indexOf("."));
			    ss=ss.substring(ss.indexOf(".")+1);	
			}else {
				MethodName=ss;
				ss="";	
			}
			MethodName=Utils.field2GetMethod(MethodName);			    		
 		try{
 			method=res.getClass().getMethod(MethodName, new Class[]{});
 		}
 		catch(Exception e){  
 		    ;   
			} 
 		if (method!=null){		    			
 			try {
					res=Utils.invokeMethod(res,MethodName,new Class[]{});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		    		    	 
 		}else{
 			res=null;
 			logger.debug(s+"所对应的方法名"+MethodName+"不存在");
 		}
		} while ((!ss.equals(""))&&(res!=null));
		return res;
	 }
	

}

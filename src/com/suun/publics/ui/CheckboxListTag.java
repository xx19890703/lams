package com.suun.publics.ui;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.suun.publics.utils.Utils;

public class CheckboxListTag extends BaseTag {

	private static final long serialVersionUID = -5219241706396072772L;
	
	protected Object lists=null;
    protected String listValue="";
    protected String listTitle="";
    protected Object checkValues=null;
    
    private Collection<Object> clist=null;  
    private Collection<Object> ccheckValue=null;    
    
    private Iterator<?> it = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setCheckValues(Object checkValues) {
		if (checkValues==null) return;
		this.checkValues = checkValues;
		if(checkValues instanceof Collection){  
			ccheckValue = (Collection<Object>) checkValues;  
        }            
        if(checkValues instanceof Map){    
            Map map = (Map) checkValues;  
            ccheckValue = map.entrySet();  
        }
        if(checkValues.getClass().isArray()){//这个是最重要的，反射判断  
            this.ccheckValue = new ArrayList<Object>();  
            int length = Array.getLength(checkValues);  
            for(int i = 0; i < length; i++){  
                Object value = Array.get(checkValues, i);  
                ccheckValue.add(value);  
            }  
        }
	}
    
    @SuppressWarnings({"rawtypes", "unchecked" })
	public void setLists(Object lists) {
        this.lists = lists;
        if(lists instanceof Collection){  
        	clist = (Collection<Object>) lists;  
        }  
          
        if(lists instanceof Map){    
            Map map = (Map) lists;  
            clist = map.entrySet();  
        }  
          
          
        if(lists.getClass().isArray()){//这个是最重要的，反射判断  
            this.clist = new ArrayList<Object>();  
            int length = Array.getLength(lists);  
            for(int i = 0; i < length; i++){  
                Object value = Array.get(lists, i);  
                clist.add(value);  
            }  
        }  
    }

    public void setListValue(String listValue) {
        this.listValue = listValue;
    }
    
    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }
    
    @Override
    public int doUiEndTag() throws JspException { 	         
    	lists=null;
        listValue="";
        listTitle="";
        checkValues=null; 
        clist=null;  
        ccheckValue=null;        
        it = null;
        return EVAL_PAGE;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public int doStartTag() throws JspException {
		//ServletRequest request = pageContext.getRequest();  
      //  Collection objs=(Collection) request.getAttribute(lists);
        if (clist != null && clist.size() > 0) {  
        	try { 
	            it = clist.iterator();  
	            JspWriter out = this.pageContext.getOut();
	 	    	StringBuffer sb = new StringBuffer();
	            while(it.hasNext()){
	            	Object obj=it.next();  
      	        	Object value="";
      	        	if(obj instanceof Map.Entry){  
      	        		value=((Map.Entry)obj).getKey();
      	        	} else{
      	        		value=Utils.getPropertyValue(obj,listValue);
      	        	}  
         			sb.append("<input type=\"checkbox\"")
         			  .append(doTag())
         			  .append(" value=\"")
         			  .append(value)
         			  .append("\"");
         			if (ccheckValue!=null && ccheckValue.size() > 0)
	         			for(Object cvalue:ccheckValue){
	         				if (value.equals(cvalue)){
	             			    sb.append(" checked=\"checked\"");
	             			    break;
	             			}
	         			}         			
         			sb.append("/>");
         			if(obj instanceof Map.Entry){  
      	        		value=((Map.Entry)obj).getValue();
      	        	} else{
      	        		value=Utils.getPropertyValue(obj,listTitle);
      	        	} 
         			sb.append(value)
         			  .append("<br/>");    			 
         			 
         		 
                }
	            out.println(sb.toString());
        	} catch (IOException  e) {  
  	            e.printStackTrace();  
  	        } catch (Exception e) {  
  	            e.printStackTrace();  
  	        }                
        }  
        return SKIP_BODY;  
    }
}

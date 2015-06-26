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

public class SelectListTag extends BaseTag {

	private static final long serialVersionUID = -1458949865998626492L;
	
	protected Object lists=null;
    protected String listValue="";
    protected String listTitle="";
    protected Object checkValue=null;
    protected Object defaultCheckValue=null;
    
    private Collection<Object> clist=null;     
    
    private Iterator<?> it = null;

	public void setCheckValue(Object checkValues) {
		this.checkValue = checkValues;
	}
	
	public void setDefaultCheckValue(Object checkValues) {
		this.defaultCheckValue = checkValues;
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
        checkValue=null; 
        clist=null;         
        it = null;
        return EVAL_PAGE;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut(); 	   
        StringBuffer sb = new StringBuffer();
        sb.append("<select ")
          .append(doTag())
          .append(">");
        if (clist != null && clist.size() > 0) { 
        	try {
	            it = clist.iterator();  	            
	            while(it.hasNext()){
	            	Object obj=it.next(); 
      	        	Object value="";
      	        	if(obj instanceof Map.Entry){  
      	        		value=((Map.Entry)obj).getKey();
      	        	} else{
      	        		value=Utils.getPropertyValue(obj, listValue);
      	        	}      	        	
      	        	sb.append("<option value=\"")
       			      .append(value)
       			      .append("\"");         			
         			Object vo=checkValue;
         			if (checkValue==null) {
         				vo=defaultCheckValue;
         			}  
         			if (vo instanceof Boolean){
         				if ((Boolean)vo) vo="1"; else vo="0"; 
         				if (value.toString().equals(vo.toString())){
             			    sb.append(" checked=\"checked\"");
             			} 
         			} else{
         				if (value.equals(vo)){
             			    sb.append(" selected ");
             			} else{
             				if (value.toString().equals(vo.toString())){
                 			    sb.append(" selected ");
                 			}
             			}
         			}  
         			sb.append(">");         			
         			if(obj instanceof Map.Entry){  
      	        		value=((Map.Entry)obj).getValue();
      	        	} else{
      	        		value=Utils.getPropertyValue(obj, listTitle);
      	        	} 
         			sb.append(value);
         			sb.append("</option>");
	            }
	            
        	} catch (Exception e) {  
  	            e.printStackTrace();  
  	        }         	
        } else{
        	sb.append("<option value=\"\"></option>");     
        }
        sb.append("</select>");
        try {
			out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return SKIP_BODY;  
    }
	
}

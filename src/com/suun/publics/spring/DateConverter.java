package com.suun.publics.spring;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class DateConverter implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) { 		
		binder.registerCustomEditor(Date.class, new CustomEditor());
	}
	
	public class CustomEditor extends PropertiesEditor {  
	    @Override  
	    public void setAsText(String text) throws IllegalArgumentException {  
	        if (text.equals("")) {  
	            text = null;  
	        } 
	        if (text!=null){
	        	DateFormat dateFormat=null;
	        	if (text.length()==10){
	        		dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	        	} else if (text.length()==13){
	        		dateFormat=new SimpleDateFormat("yyyy-MM-dd HH");		        	
		        } else if (text.length()==16){
	        		dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");		        	
		        } else if (text.length()==19){
	        		dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		        	
		        } else {
	        		dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	 //if (text.length()==23)       	
		        }
	        	try {
					setValue(dateFormat.parse(text));
				} catch (ParseException e) {
					throw new IllegalArgumentException("Could not parse date: " + e.getMessage());
				}
	        } else{
	        	setValue(null);
	        }
	    }  
	  
	    @Override  
	    public String getAsText() {  
	    	Date value = (Date) getValue();
	    	return (value != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(value) : "");
	    }  
	}  

}

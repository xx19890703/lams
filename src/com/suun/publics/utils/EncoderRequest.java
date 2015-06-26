package com.suun.publics.utils;

import javax.servlet.http.HttpServletRequest;  

import java.util.Map;  
import java.util.StringTokenizer;  
import java.util.HashMap;  
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder; 

public class EncoderRequest { 
    Map<String, String> paramMap;  
    HttpServletRequest request;  
  
    public EncoderRequest(HttpServletRequest request) {  
        String queryString = request.getQueryString();
        if (queryString==null) queryString ="";
        try {
 			queryString = URLDecoder.decode(queryString,request.getCharacterEncoding());
 		} catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}                
        this.request = request;
        paramMap = new HashMap<String, String>();  
        StringTokenizer st = new StringTokenizer(queryString, "&");  
        while (st.hasMoreTokens()) {  
            String pairs = st.nextToken(); 
            String apairs[]=pairs.split("=");
            String key = apairs[0];  
            String value = apairs[1];  
            paramMap.put(key, value);  
        }  
    }  
    
	public String get(String key) { 
    	String p=paramMap.get(key);
        if(p==null)  
            return request.getParameter(key);  
        else  
            return p;  
    }  
}

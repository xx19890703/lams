package com.suun.publics.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StringOrBoolean extends JsonSerializer<String>{
	 
	@Override
	public void serialize(String value, JsonGenerator jgen,
	                      SerializerProvider provider) throws IOException,JsonProcessingException {
	    if("true".equals(value)||"false".equals(value)){//判断如果是true或false的boolean值采用boolean
	        jgen.writeBoolean(Boolean.parseBoolean(value));
	    }else{//否则用字符串
	       jgen.writeString(value!=null?value:"");     
	    }      
	}
}

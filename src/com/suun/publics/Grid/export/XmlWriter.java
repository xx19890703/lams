package com.suun.publics.Grid.export;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class XmlWriter extends AbstractWriter {
	
	private StringBuffer xmlBuilder;
	private String[] headsName;
	
	public XmlWriter(String[] headsName){
		this.headsName=headsName;
	}
	
	public void init() {
		xmlBuilder = new StringBuffer();
		xmlBuilder.append("<? xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<root>\n");
	}
	
	public void start() {;
	    
	}

	public void end() {
		xmlBuilder.append("</root>");
		try {
			this.getOut().write(xmlBuilder.toString().getBytes(this.getEncoding()));
			this.getOut().flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void addTitle(String title){;}
	public void addheadRow(Object[] record){;}
	
	public void addRow(Object[] record) {
		xmlBuilder.append("\t\t<record>\n");
		for (short i = 0; i < record.length; i++) 		
			xmlBuilder.append("\t\t\t\t<").append(headsName[i]).append(">")
			.append(record[i]==null?"":String.valueOf(record[i]))
			.append("</").append(headsName[i]).append(">\n"); 
		xmlBuilder.append("\t\t</record>\n");
	}

}

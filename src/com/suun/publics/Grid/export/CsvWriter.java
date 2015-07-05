package com.suun.publics.Grid.export;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class CsvWriter extends AbstractWriter {
	
	private StringBuffer strBuilder;
	
	public void init() {
		strBuilder = new StringBuffer();
	}
	
	public void start() {;
	}

	public void end() {
		try {
			this.getOut().write(strBuilder.toString().getBytes(this.getEncoding()));
			this.getOut().flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void addTitle(String title){;}
	
	public void addheadRow(Object[] record){
		addRow(record);
	}

	public void addRow(Object[] record) {
		for (short i = 0; i < record.length; i++) 	
			strBuilder.append(record[i]==null?"":String.valueOf(record[i])).append(",");
			 
		strBuilder.append("\n");
	}

}

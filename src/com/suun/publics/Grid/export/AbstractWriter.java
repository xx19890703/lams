package com.suun.publics.Grid.export;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractWriter {

	private OutputStream out;
	private String encoding=null;
	
	public AbstractWriter() { }
	
	public abstract void init();
	public abstract void start();
	public abstract void end();
	
	public abstract void addTitle(String title);
	
	public abstract void addheadRow(Object[] record);
	public abstract void addRow(Object[] record);
	
	public void close() {
		try {
			getOut().close();
		} catch (IOException e) {
			// do nothing;
		}		
	}
	
	public OutputStream getOut() {
		return out;
	}
	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}

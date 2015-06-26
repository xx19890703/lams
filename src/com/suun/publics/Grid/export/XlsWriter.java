package com.suun.publics.Grid.export;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class XlsWriter extends AbstractWriter {


	private short row=0;
	

	public void init() {
		this.row = 0;
	}
	
	public void start() {
		pack2Stream(new short[] { 0x809, 0x8, 0x0, 0x10, 0x0, 0x0 });
	}

	public void end() {
		pack2Stream(new short[] { 0x0A, 0x00 });
	}
	
	public void addTitle(String title){
		/*short col=0;
		writeCell(this.row, col, title);
		this.row++;*/
	}
	
	public void addheadRow(Object[] record){
		addRow(record);
	}
	
	public void addRow(Object[] record) {
		for (short i = 0; i < record.length; i++) {
			writeCell(this.row, i, record[i]);
		}
		this.row++;
	}

	
	public void writeNumCell(short row, short col, double value) {
		pack2Stream(new short[] { 0x203, 14, });
		pack2Stream(new short[] { row,	col });
		pack2Stream(new short[] { 0x0 });
		pack2Stream( value );
	}

  
	public void writeStringCell(short row, short col, String value) {
		writeStringCell(row,col, parseString(value));
	}

	public void writeStringCell(short row, short col, byte[] strBytes) {
		int len = strBytes.length;
		pack2Stream(new short[]{ 0x204, (short)(8 + len) } );
		pack2Stream(new short[] { row,	col });
		pack2Stream(new short[] { 0x0,	(short)len  });
		pack2Stream(strBytes);
	}
	
	public void writeCell(short row, short col, Object value) {
		if (isNumber(value)){
			double dNum= Double.parseDouble(value==null?"0":String.valueOf(value));
			writeNumCell(row, col, dNum);
		}else{
			writeStringCell(row, col, value==null?"":String.valueOf(value));
		}
	}

	public void writeCell(short col, Object value) {
		writeCell(this.row, col, value==null?"":String.valueOf(value));
	}
	


	public byte[] parseString(String str) {
		if (getEncoding()!=null){
			try {
				return str.getBytes(getEncoding());
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			}
		}
		return str.getBytes();
	}
	
	public void pack2Stream(byte[] bytes) {
		try {
			this.getOut().write(bytes);
			this.getOut().flush();
		} catch (IOException e) {
		}
	}

	public void pack2Stream(double doubleNum) {
		pack2Stream(doubleToBytes(doubleNum));
	}
	
	public void pack2Stream(short shortNum) {
		pack2Stream(shortToBytes(shortNum));
	}
	
	public void pack2Stream(short[] shorts) {
		for (int i = 0; i < shorts.length; i++) {
			pack2Stream(shorts[i]);
		}
	}
	public short getRow() {
		return this.row;
	}
	
	public static final int BYTE_LEN = 1;
	public static final int SHORT_LEN = 2;
	public static final int INT_LEN = 4;
	public static final int FLOAT_LEN = 4;
	public static final int LONG_LEN = 8;
	public static final int DOUBLE_LEN = 8;
	
	public static boolean isNumber(Object value){
		return value instanceof Number;
	}
	
	public static byte[] longToBytes(long lnum) {
		byte[] bytes=new byte[LONG_LEN];
		int startIndex=0;
		for (int i = 0; i < 8; i++)
			bytes[startIndex + i] = (byte) ((lnum >> (i * 8)) & 0xff);
		return bytes;
	}
	
	public static byte[] shortToBytes(short num) {
		byte[] bytes=new byte[SHORT_LEN];
		int startIndex=0;
		bytes[startIndex] = (byte) (num & 0xff);
		bytes[startIndex + 1] = (byte) ((num >> 8) & 0xff);
		return bytes;
	}
	
	public static byte[] doubleToBytes(double dnum) {
		return longToBytes(Double.doubleToLongBits(dnum));
	}

}

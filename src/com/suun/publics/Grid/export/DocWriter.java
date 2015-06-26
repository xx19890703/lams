package com.suun.publics.Grid.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.rtf.table.RtfCell;
import com.lowagie.text.rtf.RtfWriter2;


public class DocWriter extends AbstractWriter {


	private Document document;
	private BaseFont bfChinese;
	private Font fontBody;
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream(); 
	private Table aTable;
	private float[] CellWidths;
	
	public void init() {
		this.document = new Document();
		try {
		    RtfWriter2.getInstance(this.document, buffer);
		    /** 新建一个字体,iText的方法
		　　 * STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
		　　 * UniGB-UCS2-H 是编码，在iTextAsian.jar 中以cmap为后缀
		　　 * H 代表文字版式是 横版， 相应的 V 代表 竖版
		　　 */
		    this.bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		    this.fontBody = new Font(bfChinese, 10, Font.NORMAL, CMYKColor.BLACK);
		}  
		catch (DocumentException de) {
		    System.err.println(de.getMessage());
		}
		catch (IOException ioe) 
		{
		    System.err.println(ioe.getMessage());
		}
		
	}
	
	public void start() {
	    // 打开文档，将要写入内容
        this.document.open();
        
	}

	public void end() {
		// 关闭打开的文档
		try {
			Rectangle r=new Rectangle(PageSize.A4.getRight(72), PageSize.A4
				     .getTop(72));
			int all=0;
			for (short i = 0; i < CellWidths.length; i++)
				all+=CellWidths[i];
			for (short i = 0; i < CellWidths.length; i++)
				CellWidths[i]=r.getWidth()/all*CellWidths[i];
			aTable.setWidths(CellWidths);
			document.add(aTable);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		this.document.close();
		aTable.deleteAllRows();
		try {
			this.getOut().write(this.buffer.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void addTitle(String title){
		Font titleFont= new Font(this.bfChinese, 12, Font.BOLD);

		Paragraph ptitle = new Paragraph(title);
		//设置标题格式对齐方式
		ptitle.setAlignment(Element.ALIGN_CENTER);
		ptitle.setFont(titleFont);
		try {
			document.add(ptitle);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
    }

	public void addheadRow(Object[] record){
		addRow(record);
	}
	
	public void addRow(Object[] record) {
		if (aTable==null) {
		    try {
				aTable= new Table(record.length);
				aTable.setWidth(90);// 占页面宽度 90%
			} catch (BadElementException e) {
				e.printStackTrace();
			}
		   
		    CellWidths=new float[record.length];
			for (short i = 0; i < record.length; i++)
				CellWidths[i]=5;
		}
		for (short i = 0; i < record.length; i++) {
			String s=record[i]==null?"":record[i].toString();
			CellWidths[i]=CellWidths[i]>s.length()?CellWidths[i]:s.length();
		    RtfCell cell;
			try {
				cell = new RtfCell(new Paragraph(s,this.fontBody));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);		    
		        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
			    cell.setBackgroundColor(CMYKColor.WHITE);
			    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			    cell.setBorderColor(CMYKColor.BLACK);
			    aTable.addCell(cell);
			} catch (BadElementException e) {
				e.printStackTrace();
			}	        
		}
	}


}

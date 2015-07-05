/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package com.suun.publics.workflow.image;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yangtao
 * @date 2011-9-30上午09:16:34
 * @version 1.0
 */
public class Transition {
	private Point labelPosition;   
	  private List<Point> lineTrace = new ArrayList<Point>();   
	  private String label;   
	  private String to;   
	  
	  public Transition(String label, String to) {   
	    this.label = label;   
	    this.to = to;   
	  }   
	  
	  public Point getLabelPosition() {   
	    return labelPosition;   
	  }   
	  
	  public void setLabelPosition(Point labelPosition) {   
	    this.labelPosition = labelPosition;   
	  }   
	  
	  public List<Point> getLineTrace() {   
	    return lineTrace;   
	  }   
	  
	  public void setLineTrace(List<Point> lineTrace) {   
	    this.lineTrace = lineTrace;   
	  }   
	  
	  public void addLineTrace(Point lineTrace) {   
	    if (lineTrace != null) {   
	      this.lineTrace.add(lineTrace);   
	    }   
	  }   
	  
	  public String getLabel() {   
	    return label;   
	  }   
	  public void setLabel(String label) {   
	    this.label = label;   
	  }   
	  
	  public String getTo() {   
	    return to;   
	  }   
	  
	  public void setTo(String to) {   
	    this.to = to;   
	  }   
}

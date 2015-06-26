package com.suun.publics.ui;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.suun.publics.utils.Utils;

public abstract class BaseTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 6080213477138017087L;
	
	protected String id="";
	protected String name="";
	protected String readonly="";
	protected String cssClass="";
    protected String cssStyle="";
    protected String disabled="";
    protected String onblur="";
    protected String onchange="";
    protected String onclick="";
    protected String ondblclick="";
    protected String onfocus="";
    protected String onkeydown="";
    protected String onkeypress="";
    protected String onkeyup="";
    protected String onmousedown="";
    protected String onmousemove="";
    protected String onmouseout="";
    protected String onmouseover="";
    protected String onmouseup="";
    protected String onselect="";
    
    public abstract int doUiEndTag() throws JspException;
    
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}
	public void setOnselect(String onselect) {
		this.onselect = onselect;
	}
    
	@Override
    public int doEndTag() throws JspException { 	         
		id="";
		name="";
		cssClass="";
	    cssStyle="";
	    readonly="";
	    disabled="";
	    onblur="";
	    onchange="";
	    onclick="";
	    ondblclick="";
	    onfocus="";
	    onkeydown="";
	    onkeypress="";
	    onkeyup="";
	    onmousedown="";
	    onmousemove="";
	    onmouseout="";
	    onmouseover="";
	    onmouseup="";
	    onselect="";
        return doUiEndTag();
    }
	
    public String doTag() {
    	StringBuffer sb = new StringBuffer();
    	if (!Utils.isNullOrTemp(id)){
        	sb.append(" id=\"").append(id).append("\"");
        }
        if (!Utils.isNullOrTemp(name)){
        	sb.append(" name=\"").append(name).append("\"");
        }
        if (!Utils.isNullOrTemp(readonly)){
        	sb.append(" readonly=\"").append(readonly).append("\"");
        }
    	if (!Utils.isNullOrTemp(cssStyle)){
        	sb.append(" style=\"").append(cssStyle).append("\"");
        }
        if (!Utils.isNullOrTemp(cssClass)){
        	sb.append(" class=\"").append(cssClass).append("\"");
        }
        if (!Utils.isNullOrTemp(disabled)){
        	sb.append(" disabled=\"").append(disabled).append("\"");
        }
        if (!Utils.isNullOrTemp(onblur)){
        	sb.append(" onblur=\"").append(onblur).append("\"");
        }
        if (!Utils.isNullOrTemp(onchange)){
        	sb.append(" onchange=\"").append(onchange).append("\"");
        }
        if (!Utils.isNullOrTemp(onclick)){
        	sb.append(" onclick=\"").append(onclick).append("\"");
        }
        if (!Utils.isNullOrTemp(ondblclick)){
        	sb.append(" ondblclick=\"").append(ondblclick).append("\"");
        }
        if (!Utils.isNullOrTemp(onfocus)){
        	sb.append(" onfocus=\"").append(onfocus).append("\"");
        }
        if (!Utils.isNullOrTemp(onkeydown)){
        	sb.append(" onkeydown=\"").append(onkeydown).append("\"");
        }
        if (!Utils.isNullOrTemp(onkeypress)){
        	sb.append(" onkeypress=\"").append(onkeypress).append("\"");
        }
	    if (!Utils.isNullOrTemp(onkeyup)){
        	sb.append(" onkeyup=\"").append(onkeyup).append("\"");
        }
        if (!Utils.isNullOrTemp(onmousedown)){
        	sb.append(" onmousedown=\"").append(onmousedown).append("\"");
        }
        
        if (!Utils.isNullOrTemp(onmousemove)){
        	sb.append(" onmousemove=\"").append(onmousemove).append("\"");
        }
        if (!Utils.isNullOrTemp(onmouseout)){
        	sb.append(" onmouseout=\"").append(onmouseout).append("\"");
        }
        if (!Utils.isNullOrTemp(onmouseover)){
        	sb.append(" onmouseover=\"").append(onmouseover).append("\"");
        }
        if (!Utils.isNullOrTemp(onmouseup)){
        	sb.append(" onmouseup=\"").append(onmouseup).append("\"");
        }
        if (!Utils.isNullOrTemp(onselect)){
        	sb.append(" onselect=\"").append(onselect).append("\"");
        }
		return sb.toString();
    } 
	
}

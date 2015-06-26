package com.suun.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.suun.publics.controller.BaseController;

@Controller 
public class HomeController extends BaseController{
	public class datas{
		private String listid="";
		public String getListid() {
			return listid;
		}
		public void setListid(String listid) {
			this.listid = listid;
		}
		public String getListname() {
			return listname;
		}
		public void setListname(String listname) {
			this.listname = listname;
		}
		public String getListKey() {
			return listKey;
		}
		public void setListKey(String listKey) {
			this.listKey = listKey;
		}
		private String listname="";
		private String listKey="";
	}
	
	@RequestMapping
	//@ResponseBody
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("firstMenu","<b>粗体</b>");
		
		modelandview.getModel().put("menuItems","[{text:\"<b>粗体</b>\",handler:handlerMenu},"+
		                   "{text:\"<i>斜体<i>\",handler:handlerMenu},{text:\"<u>下划线</u>\",handler:handlerMenu}," +
		                   "{text:\"<font color=red>红色字体</font>\",handler:handlerMenu}]");
		List<datas> data = new ArrayList<datas>();
		datas dataw= new datas();
		dataw.setListid("001");
		dataw.setListname("小明");
		dataw.setListKey("27");
		data.add(dataw);
		datas dataw1= new datas();
		dataw1.setListid("002");
		dataw1.setListname("小刚");
		dataw1.setListKey("25");
		data.add(dataw1);
	    modelandview.getModel().put("data",data); 
	    String[] checkdata=new String[1];
	    checkdata[0]="001";
	    modelandview.getModel().put("checkdata",checkdata); 
		return modelandview;
	}
	public ModelAndView menu(HttpServletRequest request,HttpServletResponse response) {	
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("firstMenu","<b>粗体</b>");
		
		modelandview.getModel().put("menuItems","[{text:\"<b>粗体</b>\",handler:handlerMenu},"+
		                   "{text:\"<i>斜体<i>\",handler:handlerMenu},{text:\"<u>下划线</u>\",handler:handlerMenu}," +
		                   "{text:\"<font color=red>红色字体</font>\",handler:handlerMenu}]");		
		return modelandview;
	}

}

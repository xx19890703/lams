package com.suun.publics.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.suun.publics.jCaptcha.CaptchaService;
//配置到 web.xml中，Servlet容器如tomcat会在启动该web应用程序时调用contextInitialized方法
public class InitListener implements ServletContextListener {  
	 
	public void contextInitialized(ServletContextEvent sce) {  
        System.out.println("web init ... ");  
        //系统的初始化工作  
        DicService.getDic();
        SystemService.getFuns();
        SystemService.getAllMenu(false);
        CaptchaService.getInstance();
    }
	
    public void contextDestroyed(ServletContextEvent sce) {  
        System.out.println("web exit ... ");  
        CaptchaService.removeInstance();
    }       
 
} 

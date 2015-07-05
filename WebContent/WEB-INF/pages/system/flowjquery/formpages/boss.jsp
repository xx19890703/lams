<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.jbpm.api.ProcessEngine"%>
<%@page import="org.jbpm.api.Configuration"%>
<%@page import="org.jbpm.api.TaskService"%>
<%@page import="org.jbpm.api.task.Task"%>
<%@page import="java.sql.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'manager.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	 

  </head>
  
  <body>
   <% 	
   		ProcessEngine  processEngine=Configuration.getProcessEngine();
 		TaskService taskService=processEngine.getTaskService();
 		String taskId=request.getParameter("id");
 		Task task=taskService.getTask(taskId);
 		
   %>
 	   <fieldset>
  		<legend>经理审核</legend>
  		<form action="./workflow/processformsubmit.do" method="post">
  			<input type="hidden" name="taskId" value="${param.id}" >
  			
  			申请人 : <%=taskService.getVariable(taskId,"owner") %><br/>
  			申请时间:<%=taskService.getVariable(taskId,"day") %><br/>
  			请假原因:<%=taskService.getVariable(taskId,"reason") %><br/>
  			<input type="hidden" name="result" value="结束">
  			<input type="submit" value="批准">
  			 
  		</form>
  		</fieldset>
  		<br/>
  			 
  			  
  			 
  			 
  			 
  			 
  			 
  			 
  			 
  			 
  			 
  			 
  		
  </body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'request.jsp' starting page</title>
    <script type="text/javascript"   src="${basePath}/resources/js/extjs/bootstrap.js"></script>
	<script type="text/javascript">
	
	function change(obj){
		var reg=/^[1-9]\d*$]/;
		if(!reg.test(obj.value)){
			alert("天数必须为正整数");
			obj.value="";
			obj.focus();
			return false;
		}
	}
	function validate1(){
	 
		/*
		var obj=document.getElementsByName("day")[0].value;
		var reg=/^[1-9\d*$]/;
		if(!reg.test(obj.value)){
		
			alert("天数必须为正整数");
			obj.value="";
			obj.focus();
			return false;
		}else{
		alert('dsdsds');*/
			document.forms[0].action="./workflow/processformsubmit.do";
			document.forms[0].submit();
			return true;
		//}
	}
	function callback(obj){
		if(obj=="true"){
		  	 alert( "操作成功！");
		   
		}else{
			 alert("操作失败！");
		}
		 window.setTimeout(function(){
		  	 	self.location.href='${ctx}/views/workflow/waittask/viewactiontask.jsp';
		  	 },2000);
	
	}
	function back(){
		self.location.href='${ctx}/views/workflow/waittask/viewactiontask.jsp';
	}
	</script>
  </head>
   
  <body>
  <form target="hidden_frame" action="./workflow/processformsubmit.do"  method="post">
  <center>
  	<fieldset>
  		<legend>测试sql</legend>
  			<input type="hidden" name="taskId" value="${param.id}" ><br/>
  			申请人<input type="text" name="owner" value="<sec:authentication property="name"/>"><br/>
  			user_name值<input type="text" style="width: 400px;" name="user_name" value="" ><br/>
  			<input type="hidden" name="mapname" value="owner,user_name" >	
  			<input type="button" onclick="validate1();" value="确认">
  			<input type="button" onclick="back();" value="返回 "> 
 	 	</fieldset>   
  	</center>
  	</form>
  	 <iframe name='hidden_frame' id="hidden_frame" style='display:none;height: 0px;'></iframe>
  </body>
</html>

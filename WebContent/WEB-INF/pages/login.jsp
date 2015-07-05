<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
   <title>登录页</title>  
   <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/> 
   <meta http-equiv="Cache-Control" content="no-store"/> 
   <meta http-equiv="Pragma" content="no-cache"/>
   <meta http-equiv="Expires" content="0"/> 
   <meta name="generator" content="suunframe 2.1" />  
   <link rel="icon" href="${ctx}/resources/images/system/favicon.ico" /> 
   <link href="<c:url value="/resources/css/default.css"/>" type="text/css" rel="stylesheet">
   <link href='<c:url value="/resources/js/thrid/labelvalidate/jquery.validate.css"/>' type="text/css" rel="stylesheet" />
   
   <script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
   <script type="text/javascript" src="${ctx}/resources/js/thrid/labelvalidate/jquery.validate.js"></script>
   <script type="text/javascript" src="${ctx}/resources/js/thrid/labelvalidate/messages_cn.js"></script>
   
   <script type="text/javascript"> 
     $(document).ready(function(){
    	//$('#imgValidateCode').attr('src',"${ctx}/system/security/security!imageCode.do?time="+ Math.random());
	   $("#userId").focus();
       $("#loginForm").validate({  
        // options: {
	    //            event: 'blur'
	    //          },  			  
         rules:{ 
	    	j_captcha_response: { 
	    			   required: true, 
	    			   remote: "${ctx}/system/security/security!validateCode.do?date="+ Math.random()
				     }           		
	       },
	     messages: {
	    	j_username:{
		    	required:"请填写用户名"
	       },
	       j_password:{
		    	required:"请填写密码"
	       },
	    	j_captcha_response: {
	    	   required:"请填写验证码",
		       remote: "验证码错误"
	       }
	     } 
	   });			   
	 });
     function resizeFunc(){
    		gBodyWidth = document.documentElement.clientWidth;
    		var myleft = document.getElementById("myleft");
    		var w = gBodyWidth-100-60-160;
    		
    		if(w < 0) w = 0;  
    		myleft.style.width = w/2+"px";
    		gBodyHeight = document.documentElement.clientHeight;
    		var mytop = document.getElementById("mytop");
			var h = gBodyHeight-230-30-80-40-140;
    		if(h<0) h=0;
			mytop.style.height = h/2 + "px";
    	};
      window.onresize = resizeFunc;
	  window.onload =resizeFunc;
	  //onclick="javascript:document.getElementById('loginForm').submit;document.getElementById('imgValidateCode').src='/extjs3/system/security/security!imageCode.do?time='+ Math.random();"
  </script>	    
</head>
<body style="background:url(${ctx}/resources/images/system/map.jpg);background-size:cover;overflow-y:hidden;overflow-x:hidden" >
<div id="content">
<table width="100%" border="0">
  <tr>
    <td id="mytop" height="19" align="center" valign="bottom"></td>
  </tr>
  <tr>
    <td height="80" align="center" valign="bottom"><img src="${ctx}/resources/images/system/title.png"/></td>
  </tr>
  <tr>
    <td height="28" align="center" valign="bottom"><span style="display:none">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</span>
       <% String err=request.getParameter("error")==null?"":request.getParameter("error");  
          if (err.equals("failure")) {%>
         <c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message=='Bad credentials'}">
		    <b><font color="red"> 用户名或密码错误！登录失败，请重试。</font></b>	
		 </c:if> 
	   <% }else if (err.equals("invalidsession")) {%>
	        <b><font color="red"> 登录信息失效或你的账号在其他地点登录，你已被迫下线！请重新登录。</font></b>
	   <% }else if (err.equals("maxsessions")) {%> 
	        <b><font color="red"> 你的账号在其他地点登录，你已被迫下线！</font></b>
	   <% }%>
	</td>
  </tr>
  <tr>
    <td height="225" align="left" valign="middle">
	  <table border="0">
      <tr>
        <td id="myleft" width="138" valign="middle"></td>
        <td colspan="2"  valign="middle"><img src="${ctx}/resources/images/system/ww.png" width="121" height="184"/></td>
        <td width="2" bgcolor="#0066FF"></td>
        <td width="428">
		    <form id="loginForm" action="${ctx}/j_spring_security_check" method="post">
				<table border="0" cellspacing="0" style="color: #FFFFFF;padding:10px; font-size:24px;">
					<tr valign="middle">
						<td width="65" height="30" align="right"><div align="center"><strong>用户名</strong></div></td>
						<td height="30" colspan="2" align="left" >
					  <input type='text' id='userId' name='j_username' 
					      style="font-family:arial;font-size: 13px;width:150px"  value='<c:out value="${sessionScope.SPRING_SECURITY_LAST_USERNAME}"/>' class="required"/></td>
					</tr>
					<tr valign="middle">
						<td height="30" align="right"><div align="center"><strong>密　码</strong></div></td>
						<td height="30" colspan="2" align="left" >
					  <input type='password' id='j_password' name='j_password' style="font-family:arial;font-size: 13px;width:150px" class="required"/>						</td>
					</tr>
					<tr valign="middle">
						<td height="30" align="right"><div align="center"><strong>验证码</strong></div></td>
						<td width="57" height="30" align="left">
						   <!-- 70 28 -->
						   <img id="imgValidateCode" title="单击更换一张验证码图片" border=0 align="absleft" 
						   src="${ctx}/system/security/security!imageCode.do"
						   onclick='javascript:this.src="${ctx}/system/security/security!imageCode.do?time="+ Math.random();'/>	</td>
                        <td height="30" >
                          <input type='text' id='j_captcha_response' name="j_captcha_response" autocomplete="off"  style="font-family:arial;font-size: 13px;width:66px" value="${j_captcha_response}"/>                        </td>
					</tr>
					<tr>
						<td align="right" valign="middle">&nbsp;</td>
						<td colspan="2" align="left" valign="middle">
							&nbsp;
							<input type="checkbox" name="_spring_security_remember_me" />
							两周内记住我						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
			         <!-- <td align="left"><input type="submit" value="登录" /></td>-->
					    <td align="left"><input type="submit" value="登录"/></td>
				        <td><input type="reset" value="重填" /></td>
					</tr>
				</table>
			</form></td>
      </tr> 
    </table></td>
  </tr>
  <tr>
    <td height="39" align="center"></td>
  </tr>
  <tr>
    <td height="220" align="center"><img src="${ctx}/resources/images/system/logo1.png" style="width:250px"/></td>
  </tr>
</table>
</div>
</body>
</html> 
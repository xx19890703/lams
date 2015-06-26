<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	   <title>菜单管理</title>
	   <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
		<script>
		$.validator.addMethod("beginauth", function(value) {
	  		    return value.substring(0,5) == "AUTH_";
	  	     }, '功能编号必须以"AUTH_"开头!');
		$.validator.addMethod("beginurl", function(value) {
  		    return value.substring(0,1) == "/";
  	     }, '功能编号必须以"/"开头!');	
		$("form:first").validate({
			rules: { 
				funId: { 
        			required: true, 
        			beginauth: true,
        			maxlength: 30,
        			remote: {url:'${ctx}/system/developer/menu!validateFunId',
						     type:'post',
						     data:{oldid:'${function.funId}',menuid:'${treeid}'}
			        }
        		 } ,
        		 funName: { 
         			required: true,
         			maxlength: 20
         		 } ,
        		 url:  { 
        			 beginurl:true,
        			 maxlength: 200
        		 }	
		    },
		 	messages: {
		 		funId: { 
        			required: "功能编号不能为空！", 
        			maxlength: "功能编号的长度不能超过30！",
        			remote: '功能编号已存在！'
        		 } ,
        		 funName: { 
         			required:  "功能名称不能为空！", 
        			maxlength: "功能名称的长度不能超过20！"
         		 } ,
        		 url:  { 
        			maxlength:  "URL的长度不能超过200！"
        		 }	
			}
		});
        </script>
	</head>
	<body>
	    <input type="hidden" name="menuId" value="${treeid}" />
		<table align="center">
		<br/>
			<tr>
				<td width="100" align="right">功能编号</td>					
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
		                <input  type="text" name="funId"  value="${function.funId}" style="width:174px;"/>
		            </c:when><c:otherwise>
		                <input  type="text" name="funId"  value="AUTH_" style="width:174px;"/>
		            </c:otherwise></c:choose>
		        </td>
			</tr>
			<tr>
				<td width="100" align="right">功能名称</td>
				<td width="240"><input  type="text" name=funName  value="${function.funName}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="100" align="right">URL</td>
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
		                <input  type="text" name="url"  value="${function.url}" style="width:174px;"/>
		            </c:when><c:otherwise>
		                <input  type="text" name="url"  value="/" style="width:174px;"/>
		            </c:otherwise></c:choose>
		        </td>
			</tr>			
		</table>
	</body>
</html>
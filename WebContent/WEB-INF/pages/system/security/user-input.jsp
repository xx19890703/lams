<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>  
    <script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
<script>
	$("#SuunWinForm").validate({
			 rules: { 
			    'userId': { 
                    required: true, 
        		    remote: "system/security/user!validateUserId.do?orguserId=${user.userId}"
    			},
    			'employee.employeename': {
    				required: true
    			},
    			'state': {
    				required: true
    			}
           		
			},
			messages: {
				'userId': {
					required:"用户登录名不能为空！",
					remote: "用户登录名已存在！"
				},
    			'employee.employeename': {
    				required:"用户名不能为空！"
    			},
    			'state': {
    				required:"使用状态不能为空！"
    			}
			}
		});
	function selemployee(me){		
		gridselect({listurl:$ctx+'/system/orgnization/employee!lists',//基本url
			//pagenum:5,//页记录数 默认20
			//isprewidth:true,//是否宽度按百分比 默认true  列信息	hidden: true,locked: ture
			suuncolumns:[{columnid:'employeeid',columnname:'人员编号',colwidth:20,defaultsort:true},
					 {columnid:'employeename',columnname:'人员姓名',colwidth:40}
					],
			//winWidth:500,默认值为500,350
			//winHeight:350,
			callback:function(records){
				me.value=records[0].get('employeename');
				document.getElementById("employee.employeeid").value=records[0].get('employeeid');
			}
		});
	}
</script>
</head>

<body>
<table align="center">  
    <tr>
		<td align="right">&nbsp</td>
		<td>&nbsp</td>
	</tr>  
	<tr>
		<td align="right">登录名</td>
		<td>
		    <c:choose><c:when test="${isEdit}">
		        <b>${user.userId}</b>		        
		        <input type="hidden" id="userId" name="userId" value="${user.userId}" />   
		        <input type="hidden" id="password" name="password" value="${user.password}" /> 
            </c:when><c:otherwise>
                <input type="text" onkeyup="value=value.replace(/[\W]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"  id="userId" name="userId" style="font-family:arial;width:230px" value="${user.userId}" /><font color="red">*不能为中文</font>
                <input type="hidden" id="password" name="password" value="92925488b28ab12584ac8fcaa8a27a0f497b2c62940c8f4fbc8ef19ebc87c43e" /> 
            </c:otherwise></c:choose>	       
        </td>
	</tr>
	<tr>
		<td align="right">用户名</td>
		<input type="hidden" id="employee.employeeid" name="employee.employeeid" value="${user.employee.employeeid}" />
		<td><input class="dataa" type="text" id="employee.employeename" name="employee.employeename" style="font-family:arial;width:300px"  value="${user.employee.employeename}" onclick="selemployee(this)" /></td>
	</tr>
	<tr>
		<td align="right">使用状态</td>
		<td><c:choose><c:when test="${isEdit}">
                <input type="radio" name="state" <c:if test='${user.state==1}'>checked="checked"</c:if> value="1" />使用
		        <input type="radio" name="state" <c:if test='${user.state==0}'>checked="checked"</c:if> value="0" />作废    
            </c:when><c:otherwise>
                <input type="radio" name="state" checked="checked" value="1" />使用
		        <input type="radio" name="state"  value="0" />作废
            </c:otherwise></c:choose>	
		</td>
	</tr>
	<tr>
		<td align="right">角色</td>
		<td>
			<div style="word-break:break-all;width:290px;height:100px;overflow:auto; background-color:white;border:1px solid #7f9db9;display:inline-block;padding:4px;">
			   <ui:checkboxlist name="roles.roleId" lists="${allRoles}" listValue="roleId" listTitle="roleName" checkValues="${roleids}"/>
		  	   <!--<c:forEach items="${allRoles}" var="ar">
		  	        <c:set var="check" value="false" scope="page"/>
		  	        <c:forEach items="${user.roles}" var="ur">
                    	<c:if test="${ur.roleId==ar.roleId}">
                    	   <c:if test="${check=='false'}"> <c:set var="check" value="true" scope="page"/></c:if>
                    	</c:if>
                    </c:forEach>
                    <c:if test="${check=='false'}"><input type="checkbox" name="roles.roleId" value="${ar.roleId}" />${ar.roleName}<br/></c:if>
                    <c:if test="${check=='true'}"><input type="checkbox" name="roles.roleId" value="${ar.roleId}" checked="checked"/>${ar.roleName}<br/></c:if>
          	    </c:forEach>-->
			</div>		
		</td>
	</tr>
</table>
</body>
</html>
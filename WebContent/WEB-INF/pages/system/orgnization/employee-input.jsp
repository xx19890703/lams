<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/detail.js"></script>
    <!-- script和class两种校验均可 class校验不能换行 -->
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
        $.validator.addMethod("isphone", function(value) {
    	  var length = value.length;
    	  if (length==0) return true;
    	  var phone = /(^(\d{3,4}-)?\d{6,8}$)|(^(\d{3,4}-)?\d{6,8}(-\d{1,5})?$)|(\d{11})/;
    	  return phone.test(value);
    	 }, "电话号码输入不正确！");
        $("#SuunWinForm").validate({
			rules: { 
				employeeid: { 
        			required: true, 
        			remote: {url:'${ctx}/system/orgnization/employee!validateId',
        				     type:'post',
        				     data:{oldid:'${employee.employeeid}'}
        			}
    			},
    			employeename:{
    				required:true
    			},
    			phone:{
    				isphone:true
    			},
    			email:{
    				email:true
    			}
			},
			messages: {
				employeeid: {
					required:'人员编号不能为空！',
					remote:'人员编号已存在！'
				},
				employeename: {
					required: "人员姓名不能为空！"
				}
			}
		});
     </script>
      
</head>    
<body>
  <table align="center">
    <tr>
		<td align="right">&nbsp</td>
		<td>&nbsp</td>
	</tr>
	<tr>
		<td align="right">人员编号 </td>
		<td>
		   <c:choose>		 
		     <c:when test="${isEdit}">
		         ${employee.employeeid}<input type="hidden" name="employeeid"  value="${employee.employeeid}" />
		      </c:when>		  
              <c:otherwise>
                 <input type="text" name="employeeid" style="width:174px;" value="${employee.employeeid}" /> 
              </c:otherwise>
          </c:choose>        
        </td>
	</tr>
	<tr>
		<td align="right">人员姓名</td>
		<td><input type="text" name="employeename" style="width:174px;" value="${employee.employeename}" /></td>
	</tr>
	<tr>
		<td align="right">联系电话</td>
		<td><input type="text" name="phone" style="width:174px;" value="${employee.phone}" /></td>
	</tr>
	<tr>
		<td align="right">EMAIL</td>
		<td><input type="text" name="email" style="width:174px;" value="${employee.email}" /></td>
	</tr>
	<tr>
		<td align="right">状态</td>
		<td><ui:selectlist cssStyle="width:174px;" name="state.key.data_no" lists="${allObjects}" listValue="key.data_no" listTitle="data_name" checkValue="${employee.state.key.data_no}" defaultCheckValue="1"/></td>
	</tr>
  </table>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>专家管理</title>
<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
<!-- script和class两种校验均可 class校验不能换行 -->
<script>
$.validator.addMethod("iDcard", function(num) {
	var len = num.length; 
	var re;
	if (len==0){
		return true;
		}
 　　	if (len == 15) 
 　　	 	re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/); 
 　　	else if (len == 18) 
 　　	 	re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/); 
 　　 	else {
         return false;
     } 
 　　 	var a = num.match(re); 
 　　 if (a != null) { 
 　　	 if (len==15){ 
		 　　 var D = new Date("19"+a[3]+"/"+a[4]+"/"+a[5]); 
		 　　 var B = D.getYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5]; 
 　　	 } 
 　　 else  { 
		 　　 var D = new Date(a[3]+"/"+a[4]+"/"+a[5]); 
		 　　 var B = D.getFullYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5]; 
 　　	} 
 　　 if (!B) {
         return false;
     } 
  }
 　　 if(!re.test(num)){
         return false;
     }
 　　 return true; 
   }, '身份证号格式不对');
	$("#SuunWinForm").validate({
		rules : {
			proId : {
				required : true,
				remote : {
					url : '${ctx}/professor/professor!validateProfessor',
					type : 'post',
					data : {
						oldid : '${professor.proId}'
					}
				}
			},
			ProName : {
				required : true
			},
			iDcard:{
				iDcard : true
			}
		},
		messages : {
			proId : {
				required : '专家编号不能为空！',
				remote : '专家编号已存在！'
			},
			ProName : {
				required : "专家名不能为空！"
			}
		}
	});
</script>
      
</head>    
<body>
        <br />
		<table border="0" cellspacing="0" align="center">
		<tr>
			<td align="right">编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>
			<td ><c:choose>
					<c:when test="${isEdit}">
		         	${professor.proId}<input style="width: 180px;"
							;" type="hidden" name="proId" 
							value="${professor.proId}" maxlength="30"/>
					</c:when>
					<c:otherwise>
						<input type="text" name="proId" style="width: 180px;"
							value="${professor.proId}" maxlength="30"/>
					</c:otherwise>
				</c:choose></td>
			<td style="width: 100px;" align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</td>
			<td ><input type="text" name="proName"
				style="width: 180px;" value="${professor.proName}" maxlength="30"/></td>

		</tr>

		<tr>
			<td align="right">身份证号</td>
			<td ><input type="text" name=iDcard style="width: 180px;" maxlength="18"
				value="${professor.iDcard}" /></td>
			<td align="right">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</td>
			 <td align="left">
	            <ui:selectlist cssStyle="width:180px;" name="sex.key.data_no" lists="${allObjects}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${professor.sex.key.data_no}" defaultCheckValue="1"/>
				</td> 
			
		</tr>

		<tr>
			<td align="right">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</td>
			<td >
	            <ui:selectlist cssStyle="width:180px;" name="rank.key.data_no" lists="${allObjects1}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${professor.rank.key.data_no}" defaultCheckValue="1"/>
			</td>
			<td align="right">鉴定科别</td>
			<td ><input type="text" name="category" style="width: 180px;" maxlength="10"
				value="${professor.category}" /></td>
		</tr>
		<tr>
			<td align="right">联系方式</td>
			<td colspan="3"><input type="text" name="phoneNum" style="width: 468px;" maxlength="30"
				value="${professor.phoneNum}" /></td>
		</tr>
		<tr>
			<td align="right">工作单位</td>
			<td colspan="3"><input type="text" name="workAddress" style="width: 468px;" maxlength="20"
				value="${professor.workAddress}"/></td>
		</tr>
		<tr>
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</td>
			<td colspan="3"><textarea name="remark" style="width: 468px;height: 100px;" maxlength="250">${professor.remark}</textarea>
			</td>
		</tr>
	</table>
</body>
</html>
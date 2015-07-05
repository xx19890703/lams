<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	    <title>字典管理</title>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
	    <script>	
		$("form:first").validate({
			rules: { 
				"key.data_no": { 
        			required: true, 
        			maxlength: 30,
        			remote: {url:'${ctx}/system/dic!validateId',
						     type:'post',
						     data:{oldid:'${dic_data.key.data_no}',dicid:'${treeid}'}
			        }
        		 } ,
        		 data_name: { 
         			//required: true,
         			maxlength: 50
         		 } 
		    },
		 	messages: {
		 		"key.data_no": { 
        			required: "编号不能为空！", 
        			maxlength: "编号的长度不能超过30！",
        			remote: '编号已存在！'
        		 } ,
        		 data_name: { 
         			//required:  "名称不能为空！", 
        			maxlength: "名称的长度不能超过50！"
         		 } 
			}
		});
        </script>
	</head>
	<body>
	    <input type="hidden" name="key.dic_no" value="${treeid}" />
		<table align="center">
		<br/><br/>
			<tr>
				<td width="100" align="right">编号</td>					
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
					    <b>${dic_data.key.data_no}</b>
		                <input  type="hidden" name="key.data_no" value="${dic_data.key.data_no}" />
		            </c:when><c:otherwise>
		                <input  type="text" name="key.data_no" style="width:174px;"/>
		            </c:otherwise></c:choose>
		        </td>
			</tr>
			<tr>
				<td width="100" align="right">名称</td>
				<td width="240"><input  type="text" name="data_name"  value="${dic_data.data_name}" style="width:174px;"/></td>
			</tr>
		</table>
	</body>
</html>
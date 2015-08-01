<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	   <title>表单资源配置</title>
	   <link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/detail.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/js/system/core/ux/FileUploadField.js"></script>
		<script>
		/* $("form:first").validate({
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
		}); */
		$.SuunRept({jsonbean:{"did":null,"name":null,"csqlpath":null,"description":null},beanname:"rescontent",detailnames:"did,name,csqlpath,description,state.key.data_no",required:true});
        </script>
	</head>
	<body>
		${jsonbean}
	    <input type="hidden" name="resmain.did" value="${treeid}" />
		<table align="center">
		<br/>
			<tr>
				<td width="100" align="right">模板编号</td>					
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
		                <input  type="text" name="did"  value="${templateresdetail.did}" style="width:174px;"/>
		            </c:when><c:otherwise>
		                <input  type="text" name="did"  value="" style="width:174px;"/>
		            </c:otherwise></c:choose>
		        </td>
				<td width="100" align="right">模板名称</td>
				<td width="240"><input  type="text" name="name"  value="${templateresdetail.name}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="100" align="right">模板路径</td>
				<td width="240"><input  type="text" name="path"  value="${templateresdetail.path}" style="width:174px;"/></td>
			    <td  align="right">状态</td>
				<td>     
					<input type="hidden" name="state.key.dic_no" value="STATE"/>
		        <select name="state.key.data_no" style="width:170;">
				<c:forEach var="mystatus" items="${status}">
					<c:choose >
						<c:when test="${isEdit&&mystatus.key.data_no==templateres.state.key.data_no}">
							<option selected value="${mystatus.key.data_no}">${mystatus.data_name}</option>
						</c:when>	  
		              	<c:otherwise>
		                 	<option value="${mystatus.key.data_no}">${mystatus.data_name}</option>
		              	</c:otherwise>
					</c:choose> 
		    	</c:forEach>
		    	</select>
		        </td>
			</tr>
		</table>
		<br>
	  	<div style="position:relative;overflow:auto; width:750px;">
	  		<table class="suunRept" width="100%" border="0" border="0" cellpadding="0" cellspacing="0">
		      <tr>     
		          <td align="center">编号</td>    
				  <td align="center">数据库表名</td>
				  <td align="center">表创建语句</td>
				  <td align="center">描述</td>
		      </tr>
		      <c:forEach var="item" items="${templateresdetail.rescontent}" varStatus="status">
			      <tr class="items">
					    <td align="center"><input type="text" class="sysindex" style="width:40px;" name="did" value="${item.did}"/></td>    
						<td align="center"><input type="text" style="width:200px;" name="name" value="${item.name}"/></td>
						<td align="center"><input type="text" style="width:200px;" name="csqlpath" value="${item.csqlpath}"/></td>
						<td align="center"><input type="text" style="width:200px;" name="description" value="${item.description}"/></td>
			      </tr>
		      </c:forEach>
		  </table>
		</div>
	</body>
</html>
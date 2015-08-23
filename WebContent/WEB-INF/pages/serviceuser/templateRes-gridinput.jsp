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
			$("form:first").validate({
				rules: { 
					did: { 
		    			required: true, 
		    			maxlength: 30,
		    			remote: {url:'${ctx}/serviceuser/templateRes!checkdgridid',
							     type:'post',
							     data:{oldid:'${templateresdetail.did}'}
				        }
		    		 } ,
		     		name:  { 
		    			 required: true,
		    			 maxlength: 60,
		    			 remote: {url:'${ctx}/serviceuser/templateRes!checkgridname',
						     type:'post',
						     data:{oldid:'${templateresdetail.name}'}
			        }
		    		 }	
			    },
			 	messages: {
			 		did: { 
		    			required: "模板编号不能为空！", 
		    			maxlength: "模板编号长度不能超过30！",
		    			remote: "模板编号已存在！"
		    		 } ,
		     		 name:  { 
		    			 required:  "模板名称不能为空！",
		    			 maxlength: "模板名称长度不能超过60！",
		    			 remote: "模板名称已存在！"
		    		 }	
				}
			});
			$.SuunRept({jsonbean:${jsonbean},beanname:"rescontent",detailnames:"resdetail.did,did,name,csqlpath,description,state.key.data_no",required:true});
        </script>
	</head>
	<body>
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
			        <select name="state.key.data_no" style="width:174px;">
					<c:forEach var="mystatus" items="${status}">
						<c:choose >
							<c:when test="${isEdit && mystatus.key.data_no==templateresdetail.state.key.data_no}">
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
	  	<div style="position:relative;overflow:auto; width:750px;height: 230px">
	  		<table class="suunRept" width="100%" border="0" border="0" cellpadding="0" cellspacing="0">
		      <tr>     
		          <td align="center">编号</td>    
				  <td align="center">数据库表名</td>
				  <td align="center">表创建语句</td>
				  <td align="center">描述</td>
		      </tr>
			      <tr class="items">
			     	 	<input type="hidden" name="resdetail.did" value="${resdetail.did}"/> 
					    <td align="center"><input type="text" class="sysindex" style="width:40px;" name="did" /></td>    
						<td align="center"><input type="text" style="width:200px;" class="{required:true,messages:{required:'第【】行数据库表不能为空！'}}" name="name" /></td>
						<td align="center"><input type="text" style="width:200px;" class="{required:true,messages:{required:'第【】行表创建语句不能为空！'}}" name="csqlpath"/></td>
						<td align="center"><input type="text" style="width:200px;" name="description"/></td>
			      </tr>
		  </table>
		</div>
	</body>
</html>
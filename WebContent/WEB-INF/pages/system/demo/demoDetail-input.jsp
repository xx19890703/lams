<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/detail.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
     <!-- script和class两种校验均可 class校验不能换行 -->
     <script>
         $("#SuunWinForm").validate({
			 rules: { 
				 authGroupId: { 
        			required: true, 
        			remote: {url:'${ctx}/system/demo/demoDetail!validateAuthority',
        				     type:'post',
        				     data:{oldid:'${demogroups.authGroupId}'}
        			}
    			},
    			displayName:{
    				required:true
    			}             	
			},
			messages: {
				authGroupId: {
					required:'授权组编号不能为空！',
					remote:'授权组编号已存在！'
				},
				displayName: {
					required: "授权组名称不能为空！"
				}
			}
		});
        //明细编辑
        $(top.document).ready(function(){
            $.SuunRept({jsonbean:${jsonbean},beanname:"auths",detailnames:"authgroups.authGroupId,authId,displayName,state.key.data_no",required:true});
        });
        SuunCalendar.iniCalendar();
        function setupDateTime(me){
        	SuunCalendar.show(me,{showsTime: true,ifFormat: "%Y-%m-%d %H:%M:%S"});
        }
        
     </script>
      
</head>    
<body>
  <table>
	<tr>
		<td>授权组编号  </td>
		<td>
		   <c:choose>		  
		     <c:when test="${isEdit}">
		         ${demogroups.authGroupId}<input type="hidden" name="authGroupId" size="20" value="${demogroups.authGroupId}" />
		      </c:when>		  
              <c:otherwise>
                 <input type="text" name="authGroupId" size="20" value="${demogroups.authGroupId}" /> 
              </c:otherwise>
          </c:choose>        
        </td>
	</tr>
	<tr>
		<td>授权组名称  </td>
		<td><input type="text" name="displayName" size="20" value="${demogroups.displayName}" /></td>
	</tr>
	<tr>
		<td>授权组名称  </td>
		<td><textarea style="width:100%;" name="description" rows=4 class="datee" onclick="JavaScript:setupDateTime(this);">2012-1-1</textarea></td>
	</tr>
  </table>
  <!-- 明细 <c:forEach items="${authgroups.auths}" var="p">  </c:forEach> -->
  <div style="position:relative;overflow:auto; width:600px;">
  <table class="suunRept" width="100%" border="0" border="0" cellpadding="0" cellspacing="0"> 
      <tr>     
          <td align="center">&nbsp;</td>    
		  <td align="center">授权编号</td>
		  <td align="center">授权名称</td>					
      </tr>
      <tr class="items">         
		    <input type="hidden" name="demoGroups.authGroupId" value="${demoGroups.authGroupId}"/> 
		    <td align="center"><input type="text" class="sysindex"/> </td>    
			<td align="center"><input type="text" name="authId" 
			   class="{required:true,remote:{url:'${ctx}/system/demo/demoDetail!validateAuthorityDetail',type:'post',data:{oldid:'authId.$value'}},messages:{required:'第【】行授权编号不能为空！',remote:'第【】行授权编号已存在！'}}"/></td>
			<td align="center"><input style="width:120px;" type="text" name="displayName" onclick="JavaScript:setupDateTime(this);"
			   class="datee {required:true,messages:{required:'第【】行授权名称不能为空！'}}"/> </td>
			<td align="center">
			    <!--<select name="state.key.data_no">
		  <c:choose>		
					<c:when test="${state.key.data_no==1}">
						<option value ="1" selected >使用</option><option value ="0">作废</option>
					 </c:when>		  
              		<c:otherwise>
                 		<option value ="1" >使用</option><option value ="0" selected>作废</option>
              		</c:otherwise>
				</c:choose>
				 
			   </select>--><textarea name="state.key.data_no"></textarea> </td>
      </tr>
  </table>
  </div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>模板分类配置</title>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
     $("#SuunWinForm").validate({
    	 rules: { 
    		 did: { 
    			required: true, 
    			remote: {url:'${ctx}/serviceuser/templateRes!validateDid',
    				     type:'post',
    				     data:{olddid:'${templateRes.did}'}
    			}
			}/* ,
			name:{
				required:true, 
    			remote: {url:'${ctx}/serviceuser/templateRes!validateName',
				     type:'post',
				     data:{oldname:'${templateRes.name}',id:'${suunplatformTreePid}'}
			    }
			}  */            	
		},
		messages: {
			did: {
				required:'模板分类编号不能为空！',
				remote:'模板分类编号已存在！'
			}/* ,
			name: {
				required: "模板分类名称不能为空！",
				remote:'模板分类名称已存在！'
			} */
		}
     });     
    </script>
</head>    
<body>
<table align="center">
   <tr>
        <td width="80" align="right">上级分类: </td>
        <td width="180" >
                <input type="hidden" name="pid" value="${suunplatformTreePid}"/> 
                ${suunplatformTreePName}
        </td> 
   </tr> 
		
	<tr>
		<td width="80" align="right">分类编号: </td>
		<td width="180">
            <c:choose>		  
		      <c:when test="${isEdit}">
		         ${templateres.did}
		         <input type="hidden" name="did"  value="${templateres.did}"/> 
		      </c:when>		  
           <c:otherwise>
                 <input style="width:100%;" type="text" name="did"/>
           </c:otherwise>
            </c:choose> 
        </td>
	</tr>
	<tr>
		<td width="80" align="right">分类名称: </td>
		<td width="180">            
		    <input style="width:100%;" type="text" name="name" value="${templateres.name}"/>		    
        </td>
	</tr>
	<tr>
		<td width="80" align="right">分类描述: </td>
		<td width="180">
		  <textarea style="width:100%;" name="description" rows=4>${templateres.description}</textarea>
		</td>
	</tr>	
	<tr>
	    <td  align="right">使用状态</td>
		<td >     
		 <input type="hidden" name="state.key.dic_no" value="STATE"/>
        <select name="state.key.data_no" style="width:100%;">
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
</body>
</html>
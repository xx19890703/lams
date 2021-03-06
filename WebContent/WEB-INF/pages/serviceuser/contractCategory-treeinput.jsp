<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>合同分类配置</title>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
     $("#SuunWinForm").validate({
    	 rules: { 
    		 did: { 
    			required: true, 
    			remote: {url:'${ctx}/serviceuser/contractCategory!validateDid',
    				     type:'post',
    				     data:{olddid:'${contractcategory.did}'}
    			}
			},
			name:{
				required:true, 
    			remote: {url:'${ctx}/serviceuser/contractCategory!validateName',
				     type:'post',
				     data:{oldname:'${contractcategory.name}'}
			    }
			}             	
		},
		messages: {
			did: {
				required:'合同分类编号不能为空！',
				remote:'合同分类编号已存在！'
			},
			name: {
				required: "合同分类名称不能为空！",
				remote:'合同分类名称已存在！'
			}
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
		<td width="80" align="right">合同分类编号: </td>
		<td width="180">
            <c:choose>		  
		      <c:when test="${isEdit}">
		         ${contractcategory.did}
		         <input type="hidden" name="did" value="${contractcategory.did}"/> 
		      </c:when>		  
           <c:otherwise>
                 <input style="width:100%;" type="text" name="did"/>
           </c:otherwise>
            </c:choose> 
        </td>
	</tr>
	<tr>
		<td width="80" align="right">合同分类名称: </td>
		<td width="180">            
		    <input style="width:100%;" type="text" name="name" value="${contractcategory.name}"/>		    
        </td>
	</tr>
	<tr>
		<td width="80" align="right">合同分类描述: </td>
		<td width="180">
		  <textarea style="width:100%;" name="description" rows=4>${contractcategory.description}</textarea>
		</td>
	</tr>	
	<tr>
	    <td  align="right">使用状态</td>
		<td >     
		 <ui:selectlist cssStyle="width:180px;" name="state.key.data_no" lists="${status}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${contractcategory.state.key.data_no}" defaultCheckValue="1"/>
        </td>
	</tr>
</table>
</body>
</html>
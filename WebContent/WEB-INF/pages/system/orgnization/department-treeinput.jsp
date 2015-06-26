<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>组织结构管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
     $("#SuunWinForm").validate({
    	 rules: { 
    		 did: { 
    			required: true, 
    			remote: {url:'${ctx}/system/orgnization/department!validateDid',
    				     type:'post',
    				     data:{olddid:'${department.did}'}
    			}
			},
			name:{
				required:true, 
    			remote: {url:'${ctx}/system/orgnization/department!validateName',
				     type:'post',
				     data:{oldname:'${department.name}',id:'${suunplatformTreePid}'}
			    }
			}             	
		},
		messages: {
			did: {
				required:'部门编号不能为空！',
				remote:'部门编号已存在！'
			},
			name: {
				required: "部门名称不能为空！",
				remote:'部门名称已存在！'
			}
		}
     });     
        function gridsel(me){		
			gridselect({
				listurl:$ctx+'/system/orgnization/employee!selectEmployee',//基本url
				suuncolumns:[{columnid:'employeeid',columnname:'人员编号',colwidth:20,defaultsort:true},
							 {columnid:'employeename',columnname:'人员姓名',colwidth:40},
							 {columnid:'state.data_name',columnname:'状态',colwidth:40}
							],
				//winWidth:500,默认值为500,350
				//winHeight:350,
				callback:function(records){
					me.value=records[0].get('employeename');
					document.getElementById('header.employeeid').value=records[0].get('employeeid');
				}
			});
		}
    </script>
</head>    
<body>
<table align="center">
   <tr>
        <td width="60" align="right">上级部门: </td>
        <td width="180" >
                <input type="hidden" name="pid" value="${suunplatformTreePid}"/> 
                ${suunplatformTreePName}                  
        </td> 
   </tr> 
		
	<tr>
		<td width="60" align="right">部门编号:  </td>
		<td width="180">
            <c:choose>		  
		      <c:when test="${isEdit}">
		         ${department.did}
		         <input type="hidden" name="did"  value="${department.did}"/> 
		      </c:when>		  
           <c:otherwise>
                 <input style="width:100%;" type="text" name="did"/>
           </c:otherwise>
            </c:choose> 
        </td>
	</tr>
	<tr>
		<td width="60" align="right">部门名称: </td>
		<td width="180">            
		    <input style="width:100%;" type="text" name="name" value="${department.name}"/>		    
        </td>
	</tr>
	<tr>
		<td width="60" align="right">部门描述: </td>
		<td width="180">
		  <textarea style="width:100%;" name="description" rows=4>${department.description}</textarea>
		</td>
	</tr>
	<tr>
		<td width="60" align="right">部门主管: </td>
		<td width="180">
		  <input type="hidden" id='header.employeeid' name="header.employeeid" value="${department.header.employeeid}"/>
		  <input type="text" style="width:100%;" name="header.employeename" class="dataa" onclick="gridsel(this)" value="${department.header.employeename}"/>
		</td>
	</tr>	
	<tr>
	    <td  align="right">状态</td>
		<td >     
		 <input type="hidden" name="state.key.dic_no" value="STATE"/>
        <select name="state.key.data_no" style="width:100%;">
		<c:forEach var="mystatus" items="${status}">
			<c:choose >
				<c:when test="${isEdit&&mystatus.key.data_no==department.state.key.data_no}">
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
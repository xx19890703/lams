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
    		 oid: { 
    			required: true, 
    			remote: {url:'${ctx}/system/demo/demotree!ValidateOid',
    				     type:'post',
    				     data:{oldoid:'${demotree.oid}'}
    			}
			},
			name:{
				required:true, 
    			remote: {url:'${ctx}/system/demo/demotree!ValidateName',
				     type:'post',
				     data:{oldname:'${demotree.name}',id:'${suunplatformTreePid}'}
			    }
			}             	
		},
		messages: {
			oid: {
				required:'部门编号不能为空！',
				remote:'部门编号已存在！'
			},
			name: {
				required: "部门名称不能为空！",
				remote:'部门名称已存在！'
			}
		}
     });
    </script>
</head>    
<body>
<table>
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
		         ${demotree.oid}
		         <input type="hidden" name="oid"  value="${demotree.oid}"/> 
		      </c:when>		  
           <c:otherwise>
                 <input style="width:100%;" type="text" name="oid" />
           </c:otherwise>
            </c:choose> 
        </td>
	</tr>
	<tr>
		<td width="60" align="right">部门名称: </td>
		<td width="180">            
		    <INPUT style="width:100%;" type="text" name="name" value="${demotree.name}"/>		    
        </td>
	</tr>
	<tr>
		<td width="60" align="right">部门描述: </td>
		<td width="180">
		  <textarea style="width:100%;" name="description" rows=4>${demotree.description}</textarea>
		</td>
	</tr>	
	<tr>
		<td width="60" align="right">测试: </td>
		<td width="180">
			<input name="radiobutton" type="radio" value="radiobutton" checked="checked" />
   qq
  <input type="radio" name="radiobutton" value="radiobutton" />
  33
		  <input type="checkbox" name="checkbox" value="checkbox" />
  ww
  <input type="submit" name="Submit" value="提交" />
  <img  src="${ctx}/resources/images/demo/save.gif" Onclick="www"/>
		</td>
	</tr>	
</table>
</body>
</html>
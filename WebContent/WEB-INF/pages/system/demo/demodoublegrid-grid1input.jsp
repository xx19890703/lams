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
    		 mid: { 
    			required: true, 
    			remote: {url:'${ctx}/system/demo/demodoublegrid!grid1ValidateOid',
    				     type:'post',
    				     data:{oldmid:'${demomain.mid}'}
    			}
			},
			mame:{
				required:true, 
    			remote: {url:'${ctx}/system/demo/demodoublegrid!grid1ValidateName',
				     type:'post',
				     data:{oldname:'${demomain.mname}'}
			    }
			}             	
		},
		messages: {
			mid: {
				required:'部门编号不能为空！',
				remote:'部门编号已存在！'
			},
			mname: {
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
		<td width="60" align="right">部门编号:  </td>
		<td width="180">
            <c:choose>		  
		      <c:when test="${isEdit}">
		         ${demomain.mid}
		         <input type="hidden" name="mid"  value="${demomain.mid}"/> 
		      </c:when>		  
           <c:otherwise>
                 <input style="width:100%;" type="text" name="mid" />
           </c:otherwise>
            </c:choose> 
        </td>
	</tr>
	<tr>
		<td width="60" align="right">部门名称: </td>
		<td width="180">            
		    <INPUT style="width:100%;" type="text" name="mname" value="${demomain.mname}"/>		    
        </td>
	</tr>	
</table>
</body>
</html>
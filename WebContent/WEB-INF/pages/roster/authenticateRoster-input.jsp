<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>鉴定结论审核</title>
<link rel="stylesheet" type="text/css" media="all"
	href="${ctx}/resources/js/thrid/calendar/calendar-blue.css" />
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js" />
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/system/core/gridsel.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/system/core/detail.js"></script>

</head>
<body>
<input type="hidden" name="baseId" value="${softlinee.baseId}"/>
 <table border="1" align="center" cellspacing="0" bordercolor="#6699FF" style="width:100%;height:396px;">
     <tr>
	    <td align="center" style="width:18%;">伤残等级</td>
       		<td style="width:16%;">${soft.invalidismGrades.data_name}</td>
	    	<td align="center" style="width:16%;">护理等级</td>
       		<td style="width:16%;">${soft.nursingGrades.data_name}</td>
		<td align="center"style="width:16%;">鉴定日期</td>
       <td style="width:18%;"><fmt:formatDate value="${soft.reviewDate}" pattern="yyyy-MM-dd"/></td>
  </tr>
      <tr>
	    <td  align="center"><p>延长时间</p></td>
	    <td colspan="2"><fmt:formatDate value="${soft.extensionTimeStart}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${soft.extensionTimeEnd}" pattern="yyyy-MM-dd"/></td>
	    <td  align="center"><p>康复时间</p>
         </td>
	    <td colspan="2"><fmt:formatDate value="${soft.recoveryTimeStart}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${soft.recoveryTimeEnd}" pattern="yyyy-MM-dd"/></td>
  </tr>
  <tr>
	  	<td  align="center">评审依据</td>
	    <td colspan="5">${soft.reviewGist}</td>
  </tr>
    <tr>
	  	<td  align="center">伤残情况</td>
	    <td colspan="5">${soft.invalidismCase}</td>
  </tr>
    <tr>
	  	<td  align="center">延长依据</td>
	    <td colspan="5" style="height:30px;">${soft.extensionGist}</td>
  </tr>
   
      <tr>
	  	<td  align="center">辅助器具</td>
	    <td colspan="5">${soft.helpers}</td>
  </tr>
   <tr>
	  	<td  align="center">疾病关联</td>
	    <td colspan="5">${soft.disAssociation}</td>
  </tr>
   <tr>
	  	<td  align="center">鉴定备注</td>
	    <td colspan="5">${soft.remark}</td>
  </tr>
    <tr>
	  	<td  align="center">专家组成员</td>
	    <td colspan="5">
	    <div style="position: relative; overflow: auto;height:100px;">
	    <table  width="100%" border="1" cellspacing="0">
        
						<tr>
							<td align="center"><b>姓名</b></td>
							<td align="center"><b>科别</b></td>
							<td align="center"><b>职称</b></td>
						</tr>
						<c:forEach items="${soft.accProfessionals}" var="ar">
						    <tr>								
								<td align="center">${ar.proName}</td>
								<td align="center">${ar.category}</td>
								<td align="center">${ar.rank.data_name}</td>
						    </tr>
						</c:forEach>	
        </table></div>
		</td>
  </tr> 
   <tr>
	  	<td  align="center">状态</td>
	    <td colspan="5">
	    	${softlinee.slstate.data_name}
	    </td>
  </tr>
</table>
</body>
</html>
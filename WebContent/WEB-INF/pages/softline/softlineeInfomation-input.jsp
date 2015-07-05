<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>信息查看</title>
	<script type="text/javascript">
			var tabs = new Ext.TabPanel({
			    renderTo: 'my-tabs',
			    activeTab: 0,
			    items:[
			        {contentEl:'tab1', title:'基本信息'},
			        {contentEl:'tab2', title:'鉴定信息'}
			    ]
			});
	</script>
</head>    
 <body>
 <div id="my-tabs"></div>
<div id="tab1" class="x-hide-display">
	<table border="1" align="center" cellspacing="0" bordercolor="#6699FF" style="height:396px;">
  <tr height="30"  bordercolor="#003399">
	<td style="width:16px;" rowspan="5" >被鉴定人</td>
	    <td align="center" style="width:50px;">编号</td>
        <td style="width:70px;">${softlinee.baseId}</td>
	<td style="width:50px;"><div align="center">姓名 </div></td>
	    <td style="width:120px;">${softlinee.name}</td>
    <td style="width:125px;"><div align="center">性别 </div></td>
   		<td style="width:160px;">${softlinee.sex.data_name}</td>
	    <td colspan="2" rowspan="4" align="center">
			 <img src="${ctx}/${softlinee.picture}" name="picture2" style="width:90px;height:90px"/>
		</td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">身份证号码</td>
   	    <td colspan="2">${softlinee.idcarNo}</td>
        <td align="center">出生年月 </td><td><fmt:formatDate value="${softlinee.dobs}" pattern="yyyy-MM-dd"/></td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">受伤时间</td>
   	    <td colspan="2"><fmt:formatDate value="${softlinee.woundTime}" pattern="yyyy-MM-dd"/></td>
   	    <td align="center">联系电话</td> 
        <td>${softlinee.phone}</td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">通讯地址</td>
    	<td colspan="2">${softlinee.addresss}</td>
    	<td align="center">邮编</td>
 	    <td>${softlinee.postCode}</td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">从事有毒有害岗位名称</td>
    	<td colspan="2">${softlinee.hJobName}</td>
   	    <td align="center">从事有毒有害职业日期</td>
		<td><fmt:formatDate value="${softlinee.hTime}" pattern="yyyy-MM-dd"/></td>
    	 <td align="center" style="width:110px;">接触有毒害工作年限</td>
         <td style="width:30px;">${softlinee.hJobYeat}</td>
   </tr>
   <tr >
	    <td rowspan="3" align="center">用人单位</td>
	    <td colspan="2"  align="center">单位名称</td>
        <td colspan="2">${softlinee.unitName}</td>
	    <td align="center">单位编码</td><td colspan="3">${softlinee.unitNo}</td>
  </tr>
  <tr>
	    <td colspan="2" align="center">通讯地址</td>
	    <td colspan="2">${softlinee.unitAdd}</td>
	    <td align="center">邮编</td>
	    <td colspan="3">${softlinee.unitPostCode}</td>
  </tr>
  <tr>
	    <td colspan="2"  align="center">联系人</td>
	    <td colspan="2">${softlinee.linkName}</td>
	    <td align="center">联系电话</td>
	    <td colspan="3">${softlinee.lnPhone}</td>
  </tr>
  <tr>
	    <td colspan="3" align="center">工伤认定决定书文号</td>
	    <td colspan="2">${softlinee.hBookNo}</td>
	    <td align="center">工伤认定部位</td>
	    <td colspan="3">${softlinee.hBookPosition}</td>
  </tr>
  <tr>
	    <td colspan="3" align="center">申请检查科目</td>
	    <td colspan="2">${softlinee.checks}</td>
	    <td align="center">劳鉴办医师准查科目</td>
	    <td colspan="3">${softlinee.mustCheck}</td>
  </tr>
  <tr>
	  	<td align="center">申请备注</td>
	    <td colspan="8">${softlinee.mark}</td>
  </tr>
  <tr>
		<td align="right">状态</td>
		<td colspan="8">${softlinee.slstate.data_name}</td>
   </tr>
 </table>
</div>
<div id="tab2" class="x-hide-display">
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
     
</table>
</div>
 

 </body>
</html>
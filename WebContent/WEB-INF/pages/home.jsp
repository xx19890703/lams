<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>主页</title>
<script type="text/javascript">
	Ext.onReady(function(){
		var lab = document.getElementById("lab");
		var temp = document.getElementsByTagName("span")[0].innerHTML;
		var val = temp.substr(4);
		lab.innerHTML =val;
	})
</script>
</head>
<body>
	<div>
	<br /> <br />
		<h1 align="center" style="font: font-size: 30px; font-size: 30px;"宋体";">欢迎&nbsp;<font size="6" color="#6600FF"><label id='lab'></label></font>&nbsp;进入劳动能力鉴定管理系统</h1>
		<br /> <br />
		<table border="0" align="center">
			<tr>
				<td>
					<div style="width: 130px; height: 105px; background-color: #99CCFF;">
						<sec:authorize ifAnyGranted="AUTH_SOFTLINEE_LIST,AUTH_SOFTLINEE_ADD,AUTH_SOFTLINEE_DEL,AUTH_SOFTLINEE_EDIT,AUTH_SOFTLINEE_EXP">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp12.png" width="60px" height="60px" onclick="JavaScript:suunurl({url:'${ctx}/softline/softlinee.do',title:'鉴定申请管理',iconcls:'${ctx}/resources/images/menu/archives_charge.png'});">
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/softline/softlinee.do',title:'鉴定申请管理',iconcls:'${ctx}/resources/images/menu/archives_charge.png'});">鉴定申请管理</label>
						</p>
						</sec:authorize>
						 <sec:authorize ifNotGranted="AUTH_SOFTLINEE_LIST,AUTH_SOFTLINEE_ADD,AUTH_SOFTLINEE_DEL,AUTH_SOFTLINEE_EDIT,AUTH_SOFTLINEE_EXP">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize> 
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div style="width: 130px; height: 105px; background-color: #9999CC;">
						<sec:authorize ifAnyGranted="AUTH_PRO_LIST,AUTH_PRO_ADD,AUTH_PRO_DEL,AUTH_PRO_EDIT,AUTH_PRO_EXP">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp2.png" width="50px" height="60px" onclick="JavaScript:suunurl({url:'${ctx}/professor/professor.do',title:'鉴定专家维护',iconcls:'${ctx}/resources/images/menu/demos.png'});">
							<br/><label onclick="JavaScript:suunurl({url:'${ctx}/professor/professor.do',title:'鉴定专家维护',iconcls:'${ctx}/resources/images/menu/demos.png'});">鉴定专家维护</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_PRO_LIST,AUTH_PRO_ADD,AUTH_PRO_DEL,AUTH_PRO_EDIT,AUTH_PRO_EXP">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #CC99FF;"
						>
						<sec:authorize ifAnyGranted="AUTH_SLR_ADD,AUTH_SLR_DEL,AUTH_SLR_EDIT,AUTH_SLR_EXP,AUTH_SLR_LIST">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp7.png" width="60px" height="60px" onclick="JavaScript:suunurl({url:'${ctx}/softline/softlineeReview.do',title:'鉴定评审管理',iconcls:'${ctx}/resources/images/menu/archives_setting.png'});">
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/softline/softlineeReview.do',title:'鉴定评审管理',iconcls:'${ctx}/resources/images/menu/archives_setting.png'});">鉴定评审管理</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_SLR_ADD,AUTH_SLR_DEL,AUTH_SLR_EDIT,AUTH_SLR_EXP,AUTH_SLR_LIST">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #6699FF;"
						>
						<sec:authorize ifAnyGranted="AUTH_CHECK_EDIT,AUTH_CHECK_LIST">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp16.png" width="60px" height="60px" onclick="JavaScript:suunurl({url:'${ctx}/roster/authenticateRoster.do',title:'鉴定结论审核',iconcls:'${ctx}/resources/images/menu/department.png'});">
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/roster/authenticateRoster.do',title:'鉴定结论审核',iconcls:'${ctx}/resources/images/menu/department.png'});">鉴定结论审核</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_CHECK_EDIT,AUTH_CHECK_LIST">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><div style="width: 50px; height: 50px;"></div></td>
				<td></td>
				<td><div style="width: 50px; height: 50px;"></div></td>
				<td></td>

			</tr>
			<tr>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #99CC99;"
						>
						<sec:authorize ifAnyGranted="AUTH_SOFTLINEE_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp6.png" width="60px" height="50px" onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=softlinee.cpt&cp=view',title:'鉴定花名册',iconcls:'${ctx}/resources/images/menu/softlinee.png',isframe:'1'});">
							<br/><label onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=softlinee.cpt&cp=view',title:'鉴定花名册',iconcls:'${ctx}/resources/images/menu/softlinee.png',isframe:'1'});">鉴定花名册</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_SOFTLINEE_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #0099FF;"
						>
						<sec:authorize ifAnyGranted="AUTH_SLR_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp8.png" width="60px" height="50px" onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=verdict.cpt',title:'鉴定结论书',iconcls:'${ctx}/resources/images/menu/verdict.png',isframe:'1'});">
						
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=verdict.cpt',title:'鉴定结论书',iconcls:'${ctx}/resources/images/menu/verdict.png',isframe:'1'});">鉴定结论书</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_SLR_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #66FFFF;"
						>
						<sec:authorize ifAnyGranted="AUTH_ALL_REPORT">
						<p align="center">
							<br />
								<img align="middle" src="${ctx}/resources/images/home/tp11.png" width="60px" height="50px" onclick="JavaScript:suunurl({url:'${ctx}/softline/softlineeInfomation.do',title:'综合查询',iconcls:'${ctx}/resources/images/menu/menus.png'});">
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/softline/softlineeInfomation.do',title:'综合查询',iconcls:'${ctx}/resources/images/menu/menus.png'});">综合查询</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_ALL_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
				<td>
					<div style="width: 50px; height: 105px;"></div>
				</td>
				<td>
					<div
						style="width: 130px; height: 105px; background-color: #9999FF;"
						>
						<sec:authorize ifAnyGranted="AUTH_AUROSTER_REPORT">
						<p align="center">
							<br />
								<img align="middle" src="${ctx}/resources/images/home/tp14.png" width="60px" height="50px" onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=authRoster.cpt&op=view',title:'鉴定结论花名册',iconcls:'${ctx}/resources/images/menu/softlinee.png',isframe:'1'});">
							<br /><label onclick="JavaScript:suunurl({url:'${ctx}/ReportServer?reportlet=authRoster.cpt&op=view',title:'鉴定结论花名册',iconcls:'${ctx}/resources/images/menu/softlinee.png',isframe:'1'});">鉴定结论花名册</label>
						</p>
						</sec:authorize>
						<sec:authorize ifNotGranted="AUTH_AUROSTER_REPORT">
						<p align="center">
							<br />
							<img align="middle" src="${ctx}/resources/images/home/tp13.png" width="50px" height="60px">
							<br/>
						</p>
						</sec:authorize>
					</div>
				</td>
			</tr>
		</table>

	</div>
</body>
</html>

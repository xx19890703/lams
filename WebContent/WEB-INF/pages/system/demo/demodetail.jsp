<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>授权管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
    	createsuungrid({
			containerid:"contextPanel-<%=request.getParameter("suuntitle")%>",
			keyid:"authGroupId",//关键字
			baseurl:$ctx+'/system/demo/demoDetail',//基本url
			pagenum:5,//页记录数
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true
			suuncolumns:[{columnid:'authGroupId',columnname:'授权组编号',colwidth:20,defaultsort:true},
					 {columnid:'displayName',columnname:'授权组名称',colwidth:80}
					],
			inputFormWidth:600,
			inputFormHeight:400
		});
    </script>
</head>
<body> 
</body>
</html>
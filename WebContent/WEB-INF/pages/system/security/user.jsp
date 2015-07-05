<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>登录用户管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"userId",//关键字
			baseurl:$ctx+'/system/security/user',//基本url
			//pagenum:5,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture
			suuncolumns:[{columnid:'userId',columnname:'登录名',colwidth:40,defaultsort:true},
					 {columnid:'employee.employeename',columnname:'用户名',colwidth:50},
					 {columnid:'stateName',columnname:'状态',type:'C',cdata:[["使用"],["作废"]],colwidth:10}
					],
			inputFormWidth:400,
			inputFormHeight:300,
			operation:{add:{auth:"AUTH_USER_ADD"}, 
				       edit:{auth:"AUTH_USER_EDIT"},
				       del:{auth:"AUTH_USER_DEL"},
					   exp:{auth:'AUTH_USER_EXP'}/*,
				       check:{hidden:false}*/
			          }
		});
    </script>
</head>
<body> 
</body>
</html>

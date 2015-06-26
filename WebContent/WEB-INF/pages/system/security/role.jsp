<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>角色管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"roleId",//关键字
			baseurl:$ctx+'/system/security/role',//基本url
			//pagenum:5,//页记录数
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: treu
			suuncolumns:[{columnid:'roleId',columnname:'编号',issort:true,issearch:true,colwidth:2,defaultsort:true,isASC:true},
					 {columnid:'roleName',columnname:'名称',issort:true,issearch:true,colwidth:6,defaultsort:false,isASC:true},
					 {columnid:'stateName',columnname:'使用状态',type:'C',cdata:[["使用"],["作废"]],issort:true,issearch:true,colwidth:2,defaultsort:false,isASC:true}					 
					],
			inputFormWidth:400,
			inputFormHeight:450,
			operation:{add:{auth:'AUTH_ROLE_ADD'},
			          edit:{auth:'AUTH_ROLE_EDIT'},
					  del:{auth:'AUTH_ROLE_DEL'},
					  exp:{hidden:true},
					  extend:[]
			}
		});
    </script>
</head>
<body>
</body>
</html>
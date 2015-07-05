<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>职员管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
	    createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"employeeid",//关键字
			baseurl:$ctx+'/system/orgnization/employee',//基本url
			//pagenum:5,//页记录数  默认20
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture
			suuncolumns:[{columnid:'employeeid',columnname:'人员编号',issort:true,issearch:true,colwidth:3,defaultsort:true,isASC:true},
					 {columnid:'employeename',columnname:'人员姓名',issort:true,issearch:true,colwidth:4,defaultsort:false,isASC:true},
					 {columnid:'phone',columnname:'联系电话',issearch:true,colwidth:5},
					 {columnid:'email',columnname:'EMAIL',issearch:true,colwidth:6},
					 {columnid:'state.data_name',columnname:'状态',type:'C',cdata:[["使用"],["作废"]],issort:true,issearch:true,colwidth:2,defaultsort:false,isASC:true}
					],
			inputFormWidth:280,
			inputFormHeight:240,
			operation:{add:{auth:'AUTH_EMPLOYEE_ADD'},
		          edit:{auth:'AUTH_EMPLOYEE_EDIT'},
				  del:{auth:'AUTH_EMPLOYEE_DEL'},
				  exp:{auth:'AUTH_EMPLOYEE_EXP'}
		    }
		});
    </script>
</head>
<body> 
</body>
</html>
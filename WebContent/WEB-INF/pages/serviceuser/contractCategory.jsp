<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>合同管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
    <script type="text/javascript">
       createtreegrid({containerid: 'contextPanel-'+$tabtitle,
      	 baseurl:$ctx+"/serviceuser/contractCategory",//基本url
      	 tree:{
		    title:'合同分类',
			allowEdit:true,
			inputFormWidth:300,
			inputFormHeight:260,
			operation:{add:{auth:'AUTH_CONTRACT_ADD'},edit:{auth:'AUTH_CONTRACT_EDIT'},del:{auth:'AUTH_CONTRACT_DEL'},down:{auth:'AUTH_CONTRACT_DOWN'},upload:{auth:'AUTH_CONTRACT_UPLOAD'}
		    }
        },
        grid:{
		    title:'合同资源列表',
			simplemode:true,
			keyid:'did',//"id",//关键字 
			suuncolumns:[{columnid:'did',columnname:'合同编号',colwidth:40,defaultsort:true},
	             {columnid:'name',columnname:'合同名称',colwidth:60},
	             {columnid:'finfo.fno',columnname:'制造厂信息',colwidth:60},
	             //{columnid:'orderinfo',columnname:'所属订单',colwidth:60},
	             //{columnid:'importTime',columnname:'导出时间',colwidth:60},
	             {columnid:'importCount',columnname:'下发次数',colwidth:60},
	             {columnid:'importTime',columnname:'下发时间',colwidth:60},
	             {columnid:'status.data_name',columnname:'合同状态',colwidth:60},
	             {columnid:'conmain.name',columnname:'合同分类名称',colwidth:60} ],
			inputFormWidth:800,
            inputFormHeight:400
     	 } 
         
      });
    </script>
</head>
<body></body>
</html>
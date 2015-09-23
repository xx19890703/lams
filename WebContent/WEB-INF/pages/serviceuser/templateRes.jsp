<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>模板资源管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
    <script type="text/javascript">
       createtreegrid({containerid: 'contextPanel-'+$tabtitle,
      	 baseurl:$ctx+"/serviceuser/templateRes",//基本url
      	 tree:{
		    title:'模板分类',
			allowEdit:true,
			inputFormWidth:340,
			inputFormHeight:260,
			operation:{add:{auth:'AUTH_TEMPLATERES_ADD'},edit:{auth:'AUTH_TEMPLATERES_EDIT'},del:{auth:'AUTH_TEMPLATERES_DEL'}
		    }
        },
        grid:{
		    title:'模板资源列表',
			simplemode:true,
			keyid:'did',//"id",//关键字 
			suuncolumns:[{columnid:'did',columnname:'模板编号',colwidth:40,defaultsort:true},
	             {columnid:'name',columnname:'模板名称',colwidth:80},
	             {columnid:'path',columnname:'模板路径',colwidth:80}],
	             //{columnid:'resmain.name',columnname:'模板分类名称',colwidth:60} ],
			inputFormWidth:800,
            inputFormHeight:400
     	 }
      });
    </script>
</head>
<body></body>
</html>
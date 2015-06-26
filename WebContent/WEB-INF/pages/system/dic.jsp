<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>数据字典管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
     <script type="text/javascript">
       createtreegrid({containerid: 'contextPanel-'+$tabtitle,
       	 baseurl:$ctx+"/system/dic",//基本url
       	 tree:{
		    title:'字典项',
			allowEdit:false,
			width:'20%'
         },
         grid:{
		    title:'字典数据',
			simplemode:true,
			keyid:"key.data_no",//关键字
			pagenum:25,//页记录数
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true,,state.key.data_no
			suuncolumns:[{columnid:'key.data_no',columnname:'编号',colwidth:20,defaultsort:true},
	             {columnid:'data_name',columnname:'名称',colwidth:30}],
      	   inputFormWidth:280,
           inputFormHeight:180
      	 }
       });
    </script>
</head>
<body></body>
</html>
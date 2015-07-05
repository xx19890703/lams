<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>组织结构管理</title>   
    <script type="text/javascript" src="${ctx}/resources/js/system/core/doublegridcrud.js"></script> 	
    <script type="text/javascript">
       createdoublegrid({containerid: 'contextPanel-'+$tabtitle,
       	 baseurl:$ctx+"/system/demo/demodoublegrid",//基本url
       	 grid1:{
		   allowEdit:true,
		   keyid:"mid",//关键字
      	   pagenum:5,//页记录数
      	   isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true,,state.key.data_no
      	   suuncolumns:[{columnid:'mid',columnname:'部门编号',colwidth:20,defaultsort:true},
	             {columnid:'mname',columnname:'部门名称',colwidth:80}
				   ], 
      	   inputFormWidth:280,
           inputFormHeight:300
         },
         grid2:{
      	   keyid:"did",//关键字
      	   pagenum:5,//页记录数
      	   isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true,,state.key.data_no
      	   suuncolumns:[{columnid:'did',columnname:'编号',colwidth:20,defaultsort:true},
	             {columnid:'dname',columnname:'名称',colwidth:80}
				   ],
      	   inputFormWidth:260,
           inputFormHeight:140
      	 }
       });
    </script>
</head>
<body></body>
</html>
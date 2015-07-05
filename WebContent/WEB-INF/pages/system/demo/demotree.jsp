<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>组织结构管理</title>    
	<script type="text/javascript" src="${ctx}/resources/js/system/core/treecrud.js"></script>
    <script type="text/javascript">
       createtree({
			containerid:'contextPanel-'+$tabtitle,
      	    baseurl:$ctx+"/system/demo/demotree",//基本url
      	    inputFormWidth:280,
            inputFormHeight:300
      });//参数表示的是基本路径
    </script>
</head>
<body></body>
</html>
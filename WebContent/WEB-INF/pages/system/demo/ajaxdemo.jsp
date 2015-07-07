<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>ajax</title>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
    <script type="text/javascript">var $ctx='${ctx}';</script>	
    <link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <link rel="stylesheet" href="${ctx}/resources/css/ext-fix.css" type="text/css"></link> 
    <link rel="stylesheet" href="${ctx}/resources/css/default.css" type="text/css"></link> 
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>    	
    <script type="text/javascript" charset="UTF-8">
		function endAllEdit(){
            $.ajax({
    				type: "POST",
   					url: $ctx+"/system/demo/demo!ajaxdemo.do?ssg="+encodeURI('你好'),
					data:{ss:'你好'},
   					success: function(msg){
   					    alert("ok");
   					},
   					error:function(XMLResponse){
   					    //alert(XMLResponse.responseText)
   					}
   				});
        }
		function extendAllEdit(){
			Ext.Ajax.request({
   					url: $ctx+"/system/demo/demo!ajaxdemo1.do",
   					success: function(msg){
   					    alert("ok1");
   					},
   					failure:function(){
   					    //alert('XMLResponse.responseText')
   					}
   				});
        }
		$(document).ajaxComplete(function(){ 
		    alert("$.ajaxcomplete触发");
		});
		function Ajaxcompletetest(){
            $.ajax({
    				type: "POST",
   					url: $ctx+"/system/demo/demo!ajaxdemo.do?ssg="+encodeURI('你好'),
					data:{ss:'你好'},
					complete:function(request,textStatus){ 
   					    alert("$.ajaxSetup:complete不触发");
   					}
   				});
        }
		
		 
		function testsession(){
            $.ajax({
    				type: "POST",
   					url: $ctx+"/system/demo/demo!ajaxdemo.do",
   					success: function(msg){
   					    //alert("ok");
   					},
   					error:function(XMLResponse){
   					    //alert(XMLResponse.responseText)
   					}
   				});
        }
		var int=setInterval(testsession,2000*60);
		
		//clearInterval(int); 
    </script>
</head>
<body> 
	<input type="button" onclick="javascript:endAllEdit();" value="jqueryAjaxDemo">
	<input type="button" onclick="javascript:extendAllEdit();" value="ExtAjaxDemo">
	<input type="button" onclick="javascript:Ajaxcompletetest();" value="Ajaxcompletetest">
	<div id="ajaxStateID"></div> 
</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"  %>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="myflowimage" value="${pageContext.request.contextPath}/resources/js/system/flowlib/images"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <link type="text/css" href="${ctx}/resources/js/system/flowlib/css/jquery-ui.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/js/system/flowlib/css/flowdesigner.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/js/system/flowlib/css/colortip-1.0-jquery.css" rel="stylesheet" />	
	<script type="text/javascript">
	var $ctx='${ctx}'; 
	var myflowimage = $ctx+'/resources/js/system/flowlib/images';
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/raphael.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/jquery.contextmenu.r2.js"></script>  
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/mycontextmenu.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/jquery-ui-1.10.3.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/myflow.xml.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/myflow.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/myflow.jpdl4.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/myflow.editors.js"></script>
	 
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/colortip-1.0-jquery.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/flowlib/public.js"></script>
   
<%    
String datajson=request.getAttribute("datajson")==null?"":request.getAttribute("datajson").toString().trim();
  if("".equals(datajson)){
  	datajson="{states:{rect1:{type:'start',text:{text:'开始'}, attr:{ x:342, y:44, width:50,height:50}, props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}},rect15:{type:'end',text:{text:'结束'}, attr:{ x:358, y:445, width:50,height:50}, props:{text:{value:'结束'},temp1:{value:''},temp2:{value:''}}}},paths:{},props:{props:{name:{value:'XX申请212'},key:{value:'1'},desc:{value:'一条新的流程模板'}}}}";
  }else{
  if(datajson.substring(0,1).equals("[")){
 	 datajson=datajson.substring(1,datajson.length()-1);
   } 
  }
 %>
<script type="text/javascript"> 
 var datajsons="(<%=datajson%>)";
 	$(function() {
	$(".path").colorTip({color:'yellow'}); 
		$('#myflow').myflow(
						{
							basePath :"",
							restore : eval(datajsons),
							tools : {
								save : {
									onclick : function(data) {
										
										var str=toxml(data);
										var name="";
										 if(str!=false){
										   name=getJpdgName();
										   sendxml(str,name,data);
										 }
										
										 
									}
								}
							}
						});

	});
	function sendxml(str,name,data){
	var url= '${ctx}/floweditor/makejpdl.do';
	 $.ajax({
	    url: url,
	    type: 'post',										    
	    timeout: 1000,
	    data: "name="+name+"&jsondata="+data+"&info="+str,
	    datetype:'text',
	    error: function(data){	         
	        $('#dialog1').dialog({
					autoOpen: true,
					title:'温馨提示',
					width: '30%',
					buttons: {
						"Ok": function() { 
							$(this).dialog("close"); 
						}  
					}
				});
	    },
	    success: function(data){
	       $('#dialog').dialog({
					autoOpen: true,
					title:'温馨提示',
					width: '30%',
					buttons: {
						"Ok": function() { 
							$(this).dialog("close"); 
						}  
					}
				});
	      }
	});

 } 
  
</script>
</head>
<body >
  <div class='contextMenu' id='myMenu1'> 
      	 <ul> 
       		<li id='chose'><img src="${myflowimage}/select16.gif" />&nbsp;选  择</li> 
       		<li id='linkline'><img src="${myflowimage}/16/flow_sequence.png" />&nbsp;转  换</li> 
   		    <li id='flash'><img src="${myflowimage}/reload.png" />&nbsp;刷 新</li> 
        </ul> 
         
   </div> 
    <div class='contextMenu' id='myMenu2'> 
      	 <ul> 
       		<li id='deleteid'><img src="${myflowimage}/cross.gif" />&nbsp;删  除</li> 
        </ul> 
   </div> 
 <div id="dialog" style="display: none;">
    增加成功！,<a href="#" onclick="opendelpoy();" >请上传生成的流程文件！</a>
 </div>
 <div id="dialog1"  style="display: none;">
    生成的流程图片可能有问题,请重新保存！
 </div>
<input name="stringdata" type="hidden" id="stringdataid" value="">
<input name="info" type="hidden"  id="xmldataid" value="">
 
<div id="myflow_tools"
	style=""
	class="ui-widget-content">
<div id="myflow_tools_handle" style="text-align: center;"
	class="ui-widget-header">工具集</div>
<div class="node" id="myflow_save" title="保存在本地磁盘中" ><img src="${myflowimage}/save.gif" />&nbsp;&nbsp;保存</div>

<div>
<hr />
</div>
<div class="node selectable" id="pointer" title="选择编辑对象"><img
	src="${myflowimage}/select16.gif"  />&nbsp;&nbsp;选择</div>
<div class="node selectable" title="连接流程节点，只能单向连接" id="path"><img
	src="${myflowimage}/16/flow_sequence.png" />&nbsp;&nbsp;转换</div>
<div>
 <div class="node delete" id="delete" title="可以直接按delete键删除" type="delete"><img
	src="${myflowimage}/cross.gif" />&nbsp;&nbsp;删除</div>
<hr />
</div>

<div class="node state" id="start" title="流程的开始节点" type="start"><img
	src="${myflowimage}/16/start_event_empty.png" />&nbsp;&nbsp;开始</div>
<!--<div class="node state" id="state" type="state" title="触发状态" ><img
	src="${myflowimage}/16/task_empty.png"  />&nbsp;&nbsp; 状态</div>-->
<div class="node state" id="task" type="task" title="任务"><img
	src="${myflowimage}/16/task_empty.png"  />&nbsp;&nbsp;任务</div>
<!--  <div class="node state" id="sql" type="sql" title="SQL"><img
	src="${myflowimage}/16/task_empty.png"  />&nbsp;&nbsp;SQL</div>
	-->
<div class="node state" id="fork" type="fork" title="分支"><img
	src="${myflowimage}/16/gateway_parallel.png"  />&nbsp;&nbsp;分支</div>
	<div class="node state" id="decision" type="decision" title="判断条件"><img
	src="${myflowimage}/16/gateway_parallel.png"  />&nbsp;&nbsp;判断</div>
<div class="node state" id="join" type="join" title="合并"><img    
	src="${myflowimage}/16/gateway_parallel.png"  />&nbsp;&nbsp;合并</div>
<div class="node state" id="end" type="end" title="结束"><img
	src="${myflowimage}/16/end_event_terminate.png"  />&nbsp;&nbsp;结束</div>
<div class="node state" id="end-cancel" type="end-cancel" title="取消"><img
	src="${myflowimage}/16/end_event_cancel.png"  />&nbsp;&nbsp;取消</div>
<!--<div class="node state" id="end-error" type="end-error" title="错误"><img
	src="${myflowimage}/16/end_event_error.png"  />&nbsp;&nbsp;错误</div>-->
</div>
<div id="myflow_props"
	class="ui-widget-content">
<div id="myflow_props_handle" class="ui-widget-header">属性</div>
<table border="1" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td></td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
<div>&nbsp;</div>
</div>

<div id="myflow"></div>
</body>
</html>

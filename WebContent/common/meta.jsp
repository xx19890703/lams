<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
    <style>
	   input[type="text"],input[type="password"]{
	       background-color:white;
	       border:1px solid #7f9db9; 
	       display:inline-block;
	       padding:0;
	       white-space:nowrap;
	       height:20px;
	       cursor:pointer;
	       text-align:left;
		   text-indent:4px;
	    }
	    textarea{
	       background-color:white;
	       border:1px solid #7f9db9; 
	       display:inline-block;
	       padding:0;
	      /* white-space:nowrap;*/
	       height:50px;
	       cursor:pointer;
	       text-align:left;
	       resize: none;
		   text-indent:0;
	    }
	    select{
	       text-align:left;
		   height:20px;
	    } 
	    span {
		  vertical-align: middle;
		}
	</style> 
	<script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/suuncore.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/ui/jquery.flatui.js"></script>
    <script type="text/javascript">
        var $ctx='${ctx}';  
        var $tabtitle='<%=request.getParameter("suuntitle")%>'; 
		$("select").selectCss(suunCore.UIJSPATH);			
		$(".dataa").dataSelectCss(suunCore.UIJSPATH);
		$(".datee").datetimeCss(suunCore.UIJSPATH);
		$(".datew").datetimeWCss(suunCore.UIJSPATH);
		$("input[type='text']").inputCss();		
    </script>
 
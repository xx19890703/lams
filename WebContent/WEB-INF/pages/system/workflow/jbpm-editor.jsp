<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
    <title>Web工作流设计器</title>
  </head>
  <body>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/system/workflow/scripts/ext-2.0.2/resources/css/ext-all.css" />
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/ext-2.0.2/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/ext-2.0.2/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/ext-2.0.2/ext-lang-zh_CN.js"></script>
    <script type="text/javascript">
		Ext.BLANK_IMAGE_URL = '${ctx}/resources/js/system/workflow/scripts/ext-2.0.2/resources/images/default/s.gif'
    </script>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/system/workflow/scripts/ux/ext-patch.css" />

	<script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/gef/scripts/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/gef/scripts/all-core.js"></script>
    <script type="text/javascript">
Gef.IMAGE_ROOT = '${ctx}/resources/js/system/workflow/scripts/gef/images/activities/48/';
Gef.DEPLOY_URL = '${ctx}/workflow/jbpm!deployXml.do';
Gef.XML = "${xml}";// 
//变量//
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/gef/all-editor.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/all-workflow.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/validation/all-validation.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/form/all-forms.js"></script>
    <script type='text/javascript' src='${ctx}/resources/js/system/workflow/scripts/property/all-property.js'></script>

    <script type='text/javascript' src='${ctx}/resources/js/system/workflow/scripts/ux/checkboxtree/Ext.lingo.JsonCheckBoxTree.js'></script>
    <link rel='stylesheet' type='text/css' href='${ctx}/resources/js/system/workflow/scripts/ux/checkboxtree/Ext.lingo.JsonCheckBoxTree.css' />
    <script type="text/javascript" src="${ctx}/resources/js/system/workflow/scripts/org/OrgField.js"></script>

    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/system/workflow/styles/jbpm4.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/system/workflow/styles/org.css" />

    <script type='text/javascript' src='${ctx}/resources/js/system/workflow/scripts/ux/treefield/Ext.lingo.TreeField.js'></script>

    <script type='text/javascript' src='${ctx}/resources/js/system/workflow/scripts/ux/localXHR.js'></script>
    <script type='text/javascript'>
Gef.ORG_URL = 'org.json';
    </script>
    <style type="text/css">
	    #pageh1{
		    font-size:36px;
			font-weight:bold;
			background-color:#C3D5ED;
			padding:5px;
		}
	</style>
  </body>
</html>

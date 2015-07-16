<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>服务端上传</title>
	<link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" charset="UTF-8">
		function griduploadrecord() {
			var formUpload = new Ext.form.FormPanel({
					baseCls : 'x-plain',
					labelWidth : 80,
					fileUpload : true,
					defaultType : 'textfield',
					items : [{
						xtype : 'textfield',
						fieldLabel : '文 件',
						name : 'file',
						inputType : 'file',
						allowBlank : false,
						blankText : '请上传文件',
						anchor : '90%'
					}]
				});
			var winUpload = new Ext.Window({
					title : '资源上传',
					width : 400,
					height : 200,
					minWidth : 300,
					minHeight : 100,
					layout : 'fit',
					plain : true,
					bodyStyle : 'padding:5px;',
					buttonAlign : 'center',
					items : formUpload,
					buttons : [{
						text : '上 传',
						handler : function() {
							if (formUpload.form.isValid()) {
								formUpload.getForm().submit({
									url : $ctx+'/serviceuser/contractCategory!serverupload',
									waitMsg : '正在处理',
									waitTitle : '请等待',
									success : function(form, action) {
										var flag = action.result.msg;
										Ext.Msg.alert('成功', flag);
										winUpload.hide();
									},
									failure : function(form, action) {
										console.log(action.result);
										var flag = action.result.msg;
										Ext.Msg.alert('错误', flag);
									}
								})
							}
						}
					}, {
						text : '取 消',
						handler : function() {
							winUpload.hide();
						}
					}]
				}).show();
					}

			Ext.onReady(function() {
				griduploadrecord()
			});
	</script>
</head>
<body> 
</body>
</html>
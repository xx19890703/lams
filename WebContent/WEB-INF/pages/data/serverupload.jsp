<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>制造厂数据上传</title>
	<link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" charset="UTF-8">
		function griduploadrecord() {
			var form = new Ext.form.FormPanel({  
			     baseCls : 'x-plain',  
			     labelWidth : 150,  
			     labelHeight: 150,  
			     fileUpload : true,  
			     defaultType : 'textfield',  
			     items : [{  
			        xtype : 'textfield',  
			        fieldLabel : '选择文件',  
			        name : 'file',  
			        id : 'file',  
			        inputType : 'file',  
			        anchor : '95%'  
			       }]  
			});
			var formUpload = new Ext.Window({
				resizable: false,
	            modal: true,
	            id:"suunFormWindowcfg",
	            fileUpload:true,   
	            defaultType: 'textfield',
	            autoScroll:true,
	        	width: 400,
		        height: 250,
	            title: '<center style="curor:hand">上传</center>',
	            items: form,
				buttons : [{
					text : '上 传',
					handler : function() {
							form.getForm().submit({
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
							});
					}
				},{
					text : '取 消',
					handler : function() {
						formUpload.close();
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
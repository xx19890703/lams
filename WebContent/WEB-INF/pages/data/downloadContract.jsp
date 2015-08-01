<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传</title>
<link rel="stylesheet"
	href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css"
	type="text/css"></link>
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" charset="UTF-8">
	function griduploadrecord(list) {
		var data1 = []
		for (var i = 0; i < list.length; i++) {
			var data2 = [ list[i], list[i] ];
			data1.push(data2);
		}

		aa = new Ext.data.SimpleStore({
			fields : [ 'value', 'text' ],
			data : data1
		});

		var form = new Ext.form.FormPanel({
			baseCls : 'x-plain',
			labelWidth : 150,
			labelHeight : 150,
			fileUpload : true,
			defaultType : 'textfield',
			items : [ {
				xtype : 'combo',
				id : 'cbid',
				name : 'year',//name只是改下拉的名称
				width : 180,
				store : aa,//填充数据
				fieldLabel : '合同编号',
				labelStyle : "text-align:center;width:50;",
				emptyText : '请选择',
				mode : 'local',//数据模式，local代表本地数据
				value : (new Date()).YEAR,//默认值,要设置为提交给后台的值，不要设置为显示文本
				triggerAction : 'all',// 显示所有下列数据，一定要设置属性triggerAction为all
				allowBlank : false,//不允许为空
				valueField : 'value',//值
				displayField : 'text',//显示文本
				forceSelection : true,//必须选择一个选项
				blankText : '请选择'//该项如果没有选择，则提示错误信息
			} ]
		});

		var formWin = new Ext.Window(
				{
					resizable : false,
					modal : true,
					id : "suunFormWindow",
					fileUpload : true,
					defaultType : 'textfield',
					autoScroll : true,
					width : 400,
					height : 250,
					items : form,
					title : '<center style="curor:hand">下载</center>',
					buttons : [ {
						id : "BtnSave",
						text : "下载",
						handler : function() {
							var contractId = Ext.getCmp('cbid').getValue();
							if (contractId == "") {
								alert("请选择合同！")
							} else {
								alert(contractId);
								window.open($ctx+ '/serviceuser/contractCategory!clientupload?contractId='+contractId);
								
							}
						}
					} ]
				}).show();
	}

	Ext.onReady(function() {
		Ext.Ajax.request({
			url : $ctx + '/serviceuser/contractCategory!findContract',
			method : 'POST',
			success : function(resp, opts) {
				var respText = Ext.util.JSON.decode(resp.responseText);
				griduploadrecord(respText.list);
			}
		})

	});
</script>
</head>
<body>
</body>
</html>
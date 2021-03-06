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
    <script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud2.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
		createsuungridupload({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"id",//关键字
			type:"上传",//关键字
			baseurl:$ctx+'/serviceuser/upLoadRecord',//基本url
			pagenum:15,//页记录数
			suuncolumns:[new Ext.grid.RowNumberer({header:"序号",hidden:true,width:40}),
		             {columnid:'id',hidden:true,columnname:'编号',issearch:false},
                     {columnid:'contractid',columnname:'合同编号',colwidth:80},
                     {columnid:'contractname',columnname:'合同名称',colwidth:80},
					 {columnid:'person',columnname:'上传人',colwidth:80},
					 {columnid:'upTime',columnname:'上传时间',type:'D',colwidth:80}
					// {columnid:'count',columnname:'下发次数',type:'D',colwidth:80},
					],
			inputFormWidth:720,
			inputFormHeight:300,
			operation:{check:{hidden:false}}
		});
    </script>
    <script type="text/javascript" charset="UTF-8">
   
    function addfile(callback) {
		var form = new Ext.form.FormPanel({  
		     baseCls : 'x-plain',  
		     labelWidth : 80,  
		     labelHeight: 150,
		     bodyStyle: 'padding: 15px 5px 0 5px;', 
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
	        height: 120,
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
								formUpload.hide();
								suungrid.loadStore();
							},
							failure : function(form, action) {
								var flag = action.result.msg;
								Ext.Msg.alert('错误', flag);
								formUpload.close();
								callback();
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
	</script>
</head>
<body> 
</body>
</html>
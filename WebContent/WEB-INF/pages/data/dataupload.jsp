<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>上传</title>
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
			pagenum:5,//页记录数
			suuncolumns:[new Ext.grid.RowNumberer({header:"序号",width:40}),
		             {columnid:'id',hidden:true,columnname:'编号'},
                     {columnid:'contractid',columnname:'合同编号',type:'N',colwidth:80},
                     {columnid:'contractname',columnname:'合同名称',type:'N',colwidth:80},
					 {columnid:'person',columnname:'上传人',type:'D',colwidth:80},
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
		
		var formWin =new Ext.Window({
        	resizable: false,
            modal: true,
            id:"suunFormWindow",
            fileUpload:true,   
            defaultType: 'textfield',
            autoScroll:true,
        	width: 400,
	        height: 250,
            title: '<center style="curor:hand">上传</center>',
            items: form,
            buttons: [{
            	id:"BtnSave",
                text: "保存",
                handler:function(){
                	Ext.MessageBox.wait('请等待','数据上传中……');
                	form.getForm().submit({  
                        url : $ctx+'/serviceuser/contractCategory!gridupload',  
                        method : 'POST',  
                        success : function(data) {  
                        	alert(data)
                        	formWin.close();
                        },
                        failure : function(data,form){
                        	alert('上传成功！');
                        	parent.location.href="${ctx}/j_spring_security_logout";
                        }
                	})
                	
                }
            }, {
                text: "取 消",handler:function(){
                	formWin.close();
                }
            }]
        }).show();
	}
		
    </script>
</head>
<body> 
</body>
</html>
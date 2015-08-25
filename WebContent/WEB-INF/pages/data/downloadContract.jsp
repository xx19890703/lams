<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传</title>
<script type="text/javascript" src="${ctx}/resources/js/system/core/treegridsel.js"></script>
<link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud2.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"id",//关键字
			type:"导出",//关键字
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
			items : [{
         	   name: "cbid",
        	   xtype:"textfield",
        	   id:"cbid",
        	   hidden:true
           },{
				xtype:"combo",
         	  	 name: "htmc", 
         	   	id:'htmc',
         	   	width:150,
         	   	fieldLabel: "合同信息",
         	   	onTriggerClick:function(){  
         	   	treegridselect({baseurl:$ctx+"/serviceuser/contractCategory",//基本url
    				tree:{
    					width:'30%'//默认40%
    				},
    				grid:{
    					pagenum:10,//页记录数 默认20
    					suuncolumns:[{columnid:'did',columnname:'编号',colwidth:60,defaultsort:true},
    					             {columnid:'name',columnname:'合同名称',colwidth:60} ]
    				},
    				winWidth:650,
    				winHeight:350,
    				callback:function(records){
    					if(records.length==0){
    						Ext.Msg.alert("消息","请选择一个合同!");
    						return;
    					}
    					if(records.length>1){
    						Ext.Msg.alert("消息","请选择一个合同!");
    						return;
    					}
    					Ext.getCmp("htmc").setValue(records[0].get('name'));
    					Ext.getCmp("cbid").setValue(records[0].get('did'));
    				}
    			});
         		} 
			}]
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
								window.open($ctx+ '/serviceuser/contractCategory!clientupload?contractId='+contractId);
							}
						}
					} ]
				}).show();
	}

	function add(){
		Ext.Ajax.request({
			url : $ctx + '/serviceuser/contractCategory!findContract',
			method : 'POST',
			success : function(resp, opts) {
				var respText = Ext.util.JSON.decode(resp.responseText);
				griduploadrecord(respText.list);
			}
		})

	}
</script>
</head>
<body>
</body>
</html>
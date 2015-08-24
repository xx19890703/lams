<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传</title>
<script type="text/javascript" src="${ctx}/resources/js/system/core/treegridsel.js"></script>
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
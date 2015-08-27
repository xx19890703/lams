<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>合同管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
    <script type="text/javascript">
       createtreegrid({containerid: 'contextPanel-'+$tabtitle,
      	 baseurl:$ctx+"/serviceuser/contractCategory",//基本url
      	 tree:{
		    title:'合同分类',
			allowEdit:true,
			inputFormWidth:300,
			inputFormHeight:260,
			operation:{add:{auth:'AUTH_CONTRACT_ADD'},edit:{auth:'AUTH_CONTRACT_EDIT'},del:{auth:'AUTH_CONTRACT_DEL'},down:{auth:'AUTH_CONTRACT_DOWN'},upload:{auth:'AUTH_CONTRACT_UPLOAD'}
		    }
        },
        grid:{
		    title:'合同资源列表',
			simplemode:true,
			keyid:'did',//"id",//关键字 
			suuncolumns:[{columnid:'did',columnname:'合同编号',colwidth:40,defaultsort:true},
	             {columnid:'name',columnname:'合同名称',colwidth:80},
	             {columnid:'finfo.fname',columnname:'制造厂信息',colwidth:100},
	             {columnid:'status.data_name',columnname:'合同状态',colwidth:50},
	             //{columnid:'orderinfo',columnname:'所属订单',colwidth:60},
	             //{columnid:'importTime',columnname:'导出时间',colwidth:60},
	             {columnid:'auditPerson',columnname:'审核人',colwidth:50},
	             {columnid:'auditTime',columnname:'审核时间',colwidth:50},
	             {columnid:'conmain.name',columnname:'合同分类名称',colwidth:60} ],
			inputFormWidth:800,
            inputFormHeight:400,
            operation:{audit:{hidden:false}}
     	 } 
      });
   </script>
   <script type="text/javascript">
      function auditrecord(id){
   	   var fp = new Ext.form.FormPanel({
			width: 400,  
			frame: true,   
			autoHeight: true,  
			bodyStyle: 'padding: 0px;',  
			//labelWidth: 60,  
			defaults: {  
			    anchor: '90%',  
			    allowBlank: false 
			},  
			items: [{  
			    xtype: 'textfield',  
			    fieldLabel: '合同编号', 
			    name:"contractid",
			    readOnly:true,
			    id:"contractid",
			    value:id
			},{  
			    xtype: 'textfield',  
			    id: 'auditPerson',  
			    fieldLabel: '审核人', 
			    name: 'auditPerson'
		 	},{  
			    xtype: 'datefield',  
			    id: 'auditTime',  
			    fieldLabel: '审核时间', 
			    name: 'auditTime',
			    format:"Y-m-d",
			    value: new Date()
		 	}]  
    	});
   	   
   	   var windowupload = new Ext.Window({
           	resizable: false,
            modal: true,
            id:"suunFormWindow",
           	width: 400,
   	        height: 150,
            title: '审核',
            items:[fp],
               buttons: [{
              		id:"BtnSave",
                  	text: "保存",
                  	handler:function(){
                  		if (fp.getForm().isValid()){
	                       	Ext.Ajax.request( { 
	                       		url : $ctx+'/serviceuser/contractCategory!gridaudit',
	                       		method : 'post', 
	                       		params : { 
	                       			contractid : Ext.getCmp("contractid").getValue(), 
	                       			auditPerson : Ext.getCmp("auditPerson").getValue(),
	                       			auditTime : Ext.getCmp("auditTime").getValue()
	                       		}, 
	                       		success : function(response, options) {
		                       		var o = Ext.util.JSON.decode(response.responseText);
	                       			if(o.success==true){
	                       				Ext.Msg.alert('消息','审核完成');
	                               		windowupload.close();
	                       			}else{
	                       				Ext.Msg.alert('错误', '审核失败！');
	                       			}
	                       		}, 
	                       		failure : function() { 
	                       			
	                       		} 
	                       	});
                       }
                  	}
              	}, {
                  	text: "取 消",
                  	handler:function(){
                  		windowupload.close();
                  }
              }]
          }).show(); 
       }
    </script>
</head>
<body></body>
</html>
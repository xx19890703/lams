<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>配置包上传</title>
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
	                        url : $ctx+'/serviceuser/templateRes!configupload',  
	                        method : 'POST',  
	                        success : function(msg,form) {
	                        	Ext.MessageBox.show({
    	    						title : "提示",
    	    						msg : form.result.msg,
    	    						buttons : Ext.Msg.OK,
    	    						fn:function(){	    	    	    							
    	    							formWin.close();
    	    						}
    	    					});
	                        },
	                        failure : function(msg,form){
	                        	Ext.MessageBox.show({
    	    						title : "错误",
    	    						msg : form.result.msg,
    	    						buttons : Ext.Msg.OK
    	    					});
	                        }
	                	});
	                }
	            }, {
	                text: "取 消",handler:function(){
	                	formWin.close();
	                }
	            }]
	        }).show();
		}
		
		Ext.onReady(function(){
			griduploadrecord()
		});
    </script>
</head>
<body>
</body>
</html>
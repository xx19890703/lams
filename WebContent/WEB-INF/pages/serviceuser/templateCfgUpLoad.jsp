<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>配置包上传</title>
    <link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud2.js"></script>
    <style type=text/css>         
	    .x-form-file-wrap {  
	        position: relative;  
	        height: 22px;  
	    }  
	    .x-form-file-wrap .x-form-file {  
	        position: absolute;  
	        right: 0;  
	        -moz-opacity: 0;  
	        filter:alpha(opacity: 0);  
	        opacity: 0;  
	        z-index: 2;  
	        height: 22px;  
	    }  
	    .x-form-file-wrap .x-form-file-btn {  
	        position: absolute;  
	        right: 0;  
	        z-index: 1;  
	    }  
	    .x-form-file-wrap .x-form-file-text {  
	        position: absolute;  
	        left: 0;  
	        z-index: 3;  
	        color: #777;  
	    }  
   </style>
   <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
        createsuungridupload({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"id",//关键字
			baseurl:$ctx+'/serviceuser/templateCfgUpLoad',//基本url
			pagenum:5,//页记录数
			suuncolumns:[new Ext.grid.RowNumberer({header:"序号",width:40}),
		             {columnid:'id',hidden:true,columnname:'编号',issearch:false},
					 {columnid:'person',columnname:'上传人',type:'N',colwidth:80},
					 {columnid:'upTime',columnname:'上传时间',type:'D',colwidth:100},
					 {columnid:'path',columnname:'文件路径',type:'N',colwidth:300}
					],
			inputFormWidth:720,
			inputFormHeight:300
		});
        
		function addfile(callback){
			var fp = new Ext.form.FormPanel({
					fileUpload: true,  
					width: 400,  
					frame: true,   
					autoHeight: true,  
					bodyStyle: 'padding: 15px 5px 0 5px;',  
					labelWidth: 60,  
					defaults: {  
					    anchor: '95%',  
					    allowBlank: false,  
					    msgTarget: 'side'  
					},  
					items: [/* {  
					    xtype: 'textfield',  
					    fieldLabel: '文件名称', 
					    name:"fileid",
					    readOnly:true,
					    id:"fileid"
					}, */ {  
				        xtype : 'textfield',  
				        fieldLabel : '选择文件',  
				        name : 'file',  
				        id : 'form-file',  
				        inputType : 'file',  
				        anchor : '95%'  
				       }/* ,{  
					    xtype: 'fileuploadfield',  
					    id: 'form-file',  
					    emptyText: '选择上传的文件(zip格式文件)',  
					    fieldLabel: '文件路径',  
					    name: 'file',  
					    buttonText: '浏览...',
					    listeners:{
							"fileselected": function(fb,v){	
								var fs=v.split("\\");
								Ext.getCmp("fileid").setValue(fs[fs.length-1]);
							}
						}
					 } */],  
                     buttons: [{  
                        text: '上传',  
                        handler: function() {  
                           if (fp.getForm().isValid()){
                            	fp.getForm().submit({
                                    url: $ctx+'/serviceuser/templateRes!configupload',
                                    waitTitle: '请稍后',
                                    waitMsg: '正在上传...', 
                                    success: function(form, action) {
                                    	if (action.result.success==true){
                                    		Ext.Msg.alert('消息',action.result.msg);
                                    		windowupload.close();
                                    		callback();
                                    	}
                                    },
	                            	failure : function(form, action) {
	    								Ext.Msg.alert('错误', action.result.msg);
	    								callback();
	    							}
                                });
                            }
                        }  
                     }, {  
                        text: '重置',  
                        handler: function() {  
                            fp.getForm().reset();  
                        }  
                     }]  
		    });
			
			var windowupload = new Ext.Window({
			    title:'上传文件',
			    width: 410,
		        height:140, 
			    modal :true,
				resizable: false,
			    closeAction:'close',
			    items:[fp]
			}).show();
		}
    </script>
</head>
<body>
</body>
</html>
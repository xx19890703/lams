<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Web工作流设计器</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/workflow/styles/jbpm4.css" />
    <script type="text/javascript" src="${ctx}/resources/flowlib/raphael-min.js"></script>
    
	<script type="text/javascript" src="${ctx}/resources/flowlib/jquery.contextmenu.r2.js"></script>  
	<script type="text/javascript" src="${ctx}/resources/flowlib/mycontextmenu.js"></script>
	<script type="text/javascript" src="${ctx}/resources/flowlib/jquery-ui-1.8.4.custom/js/jquery-ui-1.8.4.custom.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/flowlib/myflow.js"></script>
    <script type="text/javascript" src="${ctx}/resources/flowlib/myflow.editors.js"></script>
    <script type="text/javascript" src="${ctx}/resources/flowlib/myflow.jpdl4.js"></script>
    <style type="text/css">
.canvas {
	margin: 0;
	pading: 0;
	text-align: left;
	font-family: Arial, sans-serif, Helvetica, Tahoma;
	font-size: 12px;
	line-height: 1.5;
	color: black;
	background-image: url(${ctx}/resources/img/bg.png);
}

.node {
	width: 70px;
	text-align: center;
	vertical-align: middle;
	border: 1px solid #fff;
}

.mover {
	border: 1px solid #ddd;
	background-color: #ddd;
}

.selected {
	background-color: #ddd;
}

.state {
	
}

#myflow_props table {
	
}

#myflow_props th {
	letter-spacing: 2px;
	text-align: left;
	padding: 6px;
	background: #ddd;
}

#myflow_props td {
	background: #fff;
	padding: 6px;
}

#pointer {
	background-repeat: no-repeat;
	background-position: center;
}

#path {
	background-repeat: no-repeat;
	background-position: center;
}

#task {
	background-repeat: no-repeat;
	background-position: center;
}

#state {
	background-repeat: no-repeat;
	background-position: center;
}
</style>
    <script type='text/javascript'>
   /* $(function() {
		$('#myflow')
				.myflow(
						{
							basePath : "",
							restore : eval("({states:{rect4:{type:'start',text:{text:'开始'}, attr:{ x:409, y:10, width:50, height:50}, props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}},rect5:{type:'task',text:{text:'任务1'}, attr:{ x:386, y:116, width:100, height:50}, props:{text:{value:'任务1'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect6:{type:'fork',text:{text:'分支'}, attr:{ x:410, y:209, width:50, height:50}, props:{text:{value:'分支'},temp1:{value:''},temp2:{value:''}}},rect7:{type:'task',text:{text:'任务2'}, attr:{ x:192, y:317, width:100, height:50}, props:{text:{value:'任务2'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect8:{type:'task',text:{text:'任务3'}, attr:{ x:385, y:317, width:100, height:50}, props:{text:{value:'任务3'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect9:{type:'task',text:{text:'任务4'}, attr:{ x:556, y:320, width:100, height:50}, props:{text:{value:'任务4'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect10:{type:'join',text:{text:'合并'}, attr:{ x:410, y:416, width:50, height:50}, props:{text:{value:'合并'},temp1:{value:''},temp2:{value:''}}},rect11:{type:'end',text:{text:'结束'}, attr:{ x:409, y:633, width:50, height:50}, props:{text:{value:'结束'},temp1:{value:''},temp2:{value:''}}},rect12:{type:'task',text:{text:'任务5'}, attr:{ x:384, y:528, width:100, height:50}, props:{text:{value:'任务5'},assignee:{value:''},form:{value:''},desc:{value:''}}}},paths:{path13:{from:'rect4',to:'rect5', dots:[],text:{text:'TO 任务1'},textPos:{x:37,y:-4}, props:{text:{value:''}}},path14:{from:'rect5',to:'rect6', dots:[],text:{text:'TO 分支'},textPos:{x:56,y:-1}, props:{text:{value:''}}},path15:{from:'rect6',to:'rect8', dots:[],text:{text:'TO 任务3'},textPos:{x:24,y:-5}, props:{text:{value:''}}},path16:{from:'rect8',to:'rect10', dots:[],text:{text:'TO 合并'},textPos:{x:41,y:8}, props:{text:{value:''}}},path17:{from:'rect10',to:'rect12', dots:[],text:{text:'TO 任务5'},textPos:{x:36,y:-5}, props:{text:{value:''}}},path18:{from:'rect12',to:'rect11', dots:[],text:{text:'TO 结束'},textPos:{x:32,y:0}, props:{text:{value:''}}},path19:{from:'rect6',to:'rect7', dots:[{x:244,y:232}],text:{text:'TO 任务2'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务2'}}},path20:{from:'rect7',to:'rect10', dots:[{x:242,y:435}],text:{text:'TO 合并'},textPos:{x:-3,y:17}, props:{text:{value:'TO 合并'}}},path21:{from:'rect6',to:'rect9', dots:[{x:607,y:234}],text:{text:'TO 任务4'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务4'}}},path22:{from:'rect9',to:'rect10', dots:[{x:607,y:439}],text:{text:'TO 合并'},textPos:{x:-8,y:16}, props:{text:{value:'TO 合并'}}}},props:{props:{name:{value:'新建流程'},key:{value:''},desc:{value:''}}}})"),
							tools : {
								save : {
									onclick : function(data) {
										alert('save:\n' + data);
									}
								}
							}
						});

	});*/
    
    
		if (Ext.isIE) {
			Ext.enableGarbageCollector = false;
		}
		Ext.Loader.setConfig({
			enabled : true
		});
		Ext.Loader.setPath('Ext.ux', $ctx + '/resources/js/extjs/ux');
		Ext.QuickTips.init();
		
		function createHtml(array, divId) {
	        var imageRoot =$ctx + '/resources/workflow/scripts/gef/images/activities/'; 
	        imageRoot =imageRoot.replace('48/', '');
/*
	        if (divId) {
	            var html = '<div id="' + divId + '" unselectable="on">';
	        } else {
	            var html = '<div unselectable="on">';
	        }
*/
            var html ='';
	        for (var i = 0; i < array.length; i++) {
	            var item = array[i];
	            html += '<div id="'+item.name+'" type="'+item.name+'" class="' + item.cls
	                + '" style="text-align:center;font-size:12px;cursor:pointer;" unselectable="on"><img src="' 
	                + imageRoot + item.image + '.png" ><br>'//unselectable="on"
	                + item.title + '</div>';
	        }
	       // html += '</div>';

	        return html;
	    };
		
		var westpanel = Ext.create('Ext.panel.Panel', {
			region :'west',
	        title :'活动',
	        iconCls :'tb-activity',
	        animCollapse: false,
	        width : 80,
	        layout:'accordion',
	        collapsible: true,
	        items :[{
	            title: '基本',
	            //iconCls: 'tb-activity',
	            autoScroll: true,
	            html: createHtml([
	                {name: 'pointer',      cls: 'node selectable', image: 'select32',               title: '选择'},
	                {name: 'path',         cls: 'node selectable', image: '32/flow_sequence',       title: '连线'},
	                {name: 'start',        cls:"node state",       image: '32/start_event_empty',   title: '开始'},
	                {name: 'end',          cls:"node state",       image: '32/end_event_terminate', title: '结束'},
	                {name: 'task',         cls:"node state",       image: '32/task_empty',          title: '任务'},
	                /*{name: 'auto',         image: '32/task_empty',          title: '自动'},*/
	                {name: 'counter-sign', cls:"node state",       image: '32/task_empty',          title: '会签'},
	                {name: 'fork',         cls:"node state",       image: '32/gateway_parallel',    title: '并行'},
	                {name: 'join',         cls:"node state",       image: '32/gateway_parallel',    title: '汇聚'}
	            ])
	        }, {
	            title: '高级',
	            //iconCls: 'tb-activity',
	            autoScroll: true,
	            html: createHtml([
	                {name: 'cancel',       cls:"node state",       image: '32/end_event_cancel',    title: '取消'},
	                {name: 'error',        cls:"node state",       image: '32/end_event_error',     title: '错误'},
	                {name: 'state',        cls:"node state",       image: '32/task_wait',           title: '等待'},
	                //{name: 'task',         image: '32/task_empty',          title: '任务'},
	                //{name: 'decision',     image: '32/gateway_exclusive',   title: '决策'},
	                //{name: 'fork',         image: '32/gateway_exclusive',    title: '并行'},
	                //{name: 'join',         image: '32/gateway_parallel',    title: '汇聚'},
	                /*{name: 'java',         image: '32/task_java',           title: 'JAVA'},
	                {name: 'script',       image: '32/task_java',           title: '脚本'},
	                {name: 'hql',          image: '32/task_hql',            title: 'HQL'},
	                {name: 'sql',          image: '32/task_sql',            title: 'SQL'},
	                {name: 'mail',         image: '32/task_empty',          title: '邮件'},
	                {name: 'custom',       image: '32/task_empty',          title: '自定义'},
	                {name: 'subProcess',   image: '32/task_empty',          title: '子流程'},
	                {name: 'jms',          image: '32/task_empty',          title: 'JMS'},
	                {name: 'ruleDecision', image: '32/gateway_exclusive',   title: '规则决策'},
	                {name: 'rules',        image: '32/task_empty',          title: '规则'},*/
	                {name: 'foreach',      cls:"node state",       image: '32/gateway_exclusive',   title: '动态分支'}
	            ])
	        }]

		});
		
		
		var PropertyPanel = Ext.create('Ext.panel.Panel', {
		    title: '属性面板',
		    iconCls: 'tb-prop',
		    layout: 'fit',
		    animCollapse: false,
		    split: true,
		    region: 'east',
		    width: 200,
		    collapsible: true,
		    html:'<div id="myflow_props"  class="ui-widget-content">'+
		         '<div id="myflow_props_handle" class="ui-widget-header">属性</div>'+
		            '<table border="1" width="100%" cellpadding="0" cellspacing="0">'+
		    	        '<tr><td></td></tr><tr><td></td></tr></table>'+
		                '<div>&nbsp;</div></div>'
		});
		
		var CanvasPanel = Ext.create('Ext.panel.Panel', {
		    region :'center',
		    autoScroll:true,
		    tbar : [{
		        }, {
		            text: '导入',
		            iconCls: 'tb-webform',
		            handler: function() {
		                /*var xml = Gef.activeEditor.serial();
		                if (!this.openWin) {
		                    this.openWin = new Ext.Window({
		                        title: 'xml',
		                        layout: 'fit',
		                        width: 500,
		                        height: 300,
		                        closeAction: 'hide',
		                        modal: true,
		                        items: [{
		                            id: '__gef_jbpm4_xml_import__',
		                            xtype: 'textarea'
		                        }],
		                        buttons: [{
		                            text: '导入',
		                            handler: function() {
		                                var xml = Ext.getDom('__gef_jbpm4_xml_import__').value;
		                                Gef.activeEditor.resetAndOpen(xml);
		                                this.openWin.hide();
		                            },
		                            scope: this
		                        }, {
		                            text: '取消',
		                            handler: function() {
		                                this.openWin.hide();
		                            },
		                            scope: this
		                        }]
		                    });
		                    this.openWin.on('show', function() {
		                        Gef.activeEditor.disable();
		                    });
		                    this.openWin.on('hide', function() {
		                        Gef.activeEditor.enable();
		                    });
		                }
		                this.openWin.show(null, function() {
		                    Ext.getDom('__gef_jbpm4_xml_import__').value = xml;
		                });*/
		            }
		        }, {
		            text: '导出',
		            iconCls: 'tb-prop',
		            handler: function() {
		               /* var xml = Gef.activeEditor.serial();
		                if (!this.openWin) {
		                    this.openWin = new Ext.Window({
		                        title: 'xml',
		                        layout: 'fit',
		                        width: 500,
		                        height: 300,
		                        closeAction: 'hide',
		                        modal: true,
		                        items: [{
		                            id: '__gef_jbpm4_xml_export__',
		                            xtype: 'textarea'
		                        }],
		                        buttons: [{
		                            text: '关闭',
		                            handler: function() {
		                                this.openWin.hide();
		                            },
		                            scope: this
		                        }]
		                    });
		                    this.openWin.on('show', function() {
		                        Gef.activeEditor.disable();
		                    });
		                    this.openWin.on('hide', function() {
		                        Gef.activeEditor.enable();
		                    });
		                }
		                this.openWin.show(null, function() {
		                    Ext.getDom('__gef_jbpm4_xml_export__').value = xml;
		                });*/
		            }
		        }, {
		            text: '保存',
		            iconCls: 'tb-save',
		            handler: function() {
		                /*var editor = Gef.activeEditor;

		                var isValid = new Validation(editor).validate();
		                if (!isValid) {
		                    return false;
		                }

		                var xml = editor.serial();
		                var model = editor.getGraphicalViewer().getContents().getModel();
		                var name = model.text;
		                Ext.Msg.wait('正在发布');
		                Ext.Ajax.request({
		                    method: 'post',
		                    url: Gef.DEPLOY_URL,
		                    success: function(response) {
		                        try {
		                            var o = Ext.decode(response.responseText);
		                            if (o.success === true) {
		                                Ext.Msg.alert('信息', '操作成功');
		                            } else {
		                                Ext.Msg.alert('错误', o.errors.msg);
		                            }
		                        } catch(e) {
		                            Ext.Msg.alert('系统错误', response.responseText);
		                        }
		                    },
		                    failure: function(response) {
		                        Ext.Msg.alert('系统错误', response.responseText);
		                    },
		                    params: {
		                        id: Gef.PROCESS_ID,
		                        procCatId: model.procCatId,
		                        procDefName: model.procDefName,
		                        procDefCode: model.procDefCode,
		                        procVerName: model.procVerName,
		                        xml: xml
		                    }
		                });*/
		            }
		        }, {
		            text: '清空',
		            iconCls: 'tb-clear',
		            handler: function() {
		                //Gef.activeEditor.clear();
		            }
		        }, {
		            text: '撤销',
		            iconCls: 'tb-undo',
		            /*handler: function() {
		                var viewer = Gef.activeEditor.getGraphicalViewer();
		                var browserListener = viewer.getBrowserListener();
		                var selectionManager = browserListener.getSelectionManager();
		                selectionManager.clearAll();
		                var commandStack = viewer.getEditDomain().getCommandStack();
		                commandStack.undo();
		            },*/
		            scope: this
		        }, {
		            text: '重做',
		            iconCls: 'tb-redo',
		            /*handler: function() {
		                var viewer = Gef.activeEditor.getGraphicalViewer();
		                var browserListener = viewer.getBrowserListener();
		                var selectionManager = browserListener.getSelectionManager();
		                selectionManager.clearAll();
		                var commandStack = viewer.getEditDomain().getCommandStack();
		                commandStack.redo();
		            },*/
		            scope: this
		        }, {
		            text: '布局',
		            iconCls: 'tb-activity',
		            /*handler: function() {

		                var viewer = Gef.activeEditor.getGraphicalViewer();
		                var browserListener = viewer.getBrowserListener();
		                var selectionManager = browserListener.getSelectionManager();
		                selectionManager.clearAll();

		                new Layout(Gef.activeEditor).doLayout();
		            },*/
		            scope: this
		        }, {
		            text: '删除',
		            iconCls: 'tb-delete',
		            //handler: this.removeSelected,
		            scope: this
		        }],
		        //itemCls:'canvas',
		        html:'<div id="myflow" class="canvas" background-image="${ctx}/resources/img/bg.png"></div>'
		});
		
	    Ext.onReady(function() {			
	    	new Ext.container.Viewport({
	            layout: 'border',
	            renderTo : Ext.getBody(),
	            items: [
                    westpanel,CanvasPanel,PropertyPanel
	            ]
	        });
			
		});
	    
		
	</script>
  </head>
  <body>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>流程图管理</title>
	<link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript">
	var $ctx='${ctx}';
    if (Ext.isIE) {
		Ext.enableGarbageCollector = false;
	}
	Ext.QuickTips.init();	

	Ext.onReady(function() {		
        var inputpanel = new Ext.Panel({
			id:'detailcenter',
			region : "center"
		});
		
		var tree = new Ext.tree.TreePanel({
		    viewConfig : {
				loadingText : "正在加载..."
			},
			header:false,
			border : true,     
			enableDD : false,     
			enableDrag : false,     
			rootVisible : false,     
			trackMouseOver : true,         
			lines : true,     
			singleExpand : false, 
			collapsible : true, // 是否可以收缩 
			autoScroll : true,
			containerScroll : true,
			root : new Ext.tree.AsyncTreeNode({     
				id : '0',    
				text : '根节点',   
				iconCls:'ico-root',  
				expanded:true,  
				leaf : false,     
				border : false,     
				draggable : false,     
				singleClickExpand : false,     
				hide : true    
			}),   
			loader : new Ext.tree.TreeLoader({     
				nodeParameter:'groupId',  
				requestMethod:'POST',  
				dataUrl : $ctx+'/system/workflow/workflow!lists'
			}),//不能使用listeners，否则new suunTreePanel的listeners会不起左右
			id:'suuntree',			
			region : 'west',
			width : 150,			
			listeners : {
				'load': function(n){
					   if (Ext.getCmp('suuntree').getRootNode().firstChild) 
						   loaddetail(Ext.getCmp('suuntree').getRootNode().firstChild.attributes.id);
				},
				'click':function(node) {
					loaddetail(node.id);
				}
			}
		});
		new Ext.Viewport({
			layout : "border",
			items : [tree,inputpanel ]
			        
		});
		
		function loaddetail(id){ 
		    Ext.getCmp('detailcenter').removeAll(true ) 
			Ext.getCmp('detailcenter').add(new Ext.Panel({
						id:'center',
						html:'<iframe width="100%" height="100%" frameborder="0" src="'+$ctx+'/system/workflow/jbpm!editor.do?objname='+id+'&time='+ Math.random()+'"></iframe>',
						region : "center"
					}));
			Ext.getCmp('detailcenter').setLayout(new Ext.layout.BorderLayout());
			Ext.getCmp('detailcenter').doLayout(true);					
		   // Ext.getCmp('center').html='<iframe width="100%" height="100%" frameborder="0" src="'+$ctx+'/system/workflow/jbpm!editor.do?objname='+id+'&time='+ Math.random()+'"></iframe>';
			//Ext.getCmp('detailcenter').load();
		}
	})		
    </script>
</head>
<body></body>
</html>
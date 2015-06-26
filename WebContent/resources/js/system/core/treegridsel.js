function treegridselect(selOption) {
	//处理默认值	
	if (!selOption.tree) selOption.tree={};
	if (!selOption.grid) {
		alert("grid is null")
		return;
	}	
	if (!selOption.baseurl){
		if ((!selOption.tree.listurl)||(!selOption.grid.listurl)){
			alert("baseurl is null")
			return;
		}		    
	}else{
		if (!selOption.tree.listurl){
			selOption.tree.listurl=selOption.baseurl+'!treelists';
		}
		if (!selOption.grid.listurl){
			selOption.grid.listurl=selOption.baseurl+'!gridlists';
		}
	} 
	suunCore.showMask();                	
	if (!selOption.winWidth) selOption.winWidth=600;
	if (!selOption.winHeight) selOption.winHeight=400;
	if ("undefined" == typeof selOption.multiSel) selOption.multiSel=false;
	if (!selOption.tree.width) selOption.tree.width='40%';
	if (!selOption.grid.pagenum) selOption.grid.pagenum=20;
	var ids="";
	if (selOption.grid.existurl){
		$.ajax({ 
			url :selOption.grid.existurl, 
			async:false, 
			success: function(data){				
				ids=data;
		    },
			failure : function() {
				Ext.MessageBox.show({
							title : "错误",
							msg : "获取existids时发生错误！",
							icon : Ext.MessageBox.ERROR,
							buttons : Ext.Msg.OK
						});
			}
		});
	}
	//tree	
	var tree = new suunTreePanel({  
		title:selOption.tree.title,
		listurl:selOption.tree.listurl,	
		region : 'west',		     
		width : selOption.tree.width,  		
		split: true,
		allowEdit:selOption.tree.allowEdit,
		listeners : {
		    'load':function (n) {
				if (!tree.selModel.selNode){
				    //tree.getRootNode().firstChild.select()此时节点没有显示，不能select()，所以选中方正loadgriddetail中
					loadgriddetail();
				}
			},
			'click': function(n) {
				loadgriddetail(n);
			}
		}     
    }); 

	//grid
	var suungrid=new suunGridPanel({
		title:selOption.grid.title,
		simplemode:selOption.grid.simplemode,
	    listurl:selOption.grid.listurl,
		pagenum:selOption.grid.pagenum,
		isprewidth:selOption.grid.isprewidth,
		suuncolumns:selOption.grid.suuncolumns,	
		region : "center",
		bbarInfo:false,
		storeAutoLoad:false,
		storebeforeload:function (store, options){
		    var tid='';
			if (tree.getSelectionModel().selNode)
				tid=tree.getSelectionModel().selNode.attributes.id;		
			else if (tree.getRootNode().firstChild){
				tid=tree.getRootNode().firstChild.id;
			}
			Ext.apply(store.baseParams, {treeid:tid,existids:ids});		
		}
	});	
	suungrid.getTopToolbar().insert(0,'-');
	suungrid.getTopToolbar().insert(0,new Ext.Button({
		iconCls : 'select',
		text : "确定",
		handler : selectRecord
	}));
	Ext.getCmp('Q_SearchId-'+suungrid.getSuunid()).width=100;
	Ext.getCmp('Q_value-'+suungrid.getSuunid()).width=50; 
	Ext.getCmp('Q_svalue-'+suungrid.getSuunid()).width=50; 
	Ext.getCmp('Q_nvalue-'+suungrid.getSuunid()).width=50;
	Ext.getCmp('Q_dvalue-'+suungrid.getSuunid()).width=50;
	Ext.getCmp('Q_cvalue-'+suungrid.getSuunid()).width=50;
	suungrid.addListener("rowdblclick", selectRecord);
	
	var selwindow=new Ext.Window({
		title : '数据信息',
		width : selOption.winWidth,
		height : selOption.winHeight,
		maximized : false,
		modal:true,
		layout : 'border',
		items : [tree,suungrid],
		listeners:{
			'show':function (){suunCore.hideMask();}
		}
	});
	selwindow.show();
	
	// 选择
	function selectRecord(){    	
		var records = suungrid.getSelectionModel().getSelections();
		if (records.length == 0) {
			Ext.MessageBox.show({
				title : '提示',
				msg : '请选择信息!',
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			})
			return;		
		} else if (!selOption.multiSel){
			if (records.length > 1) {
				Ext.MessageBox.show({
					title : '提示',
					msg : '只能选择一条信息!',
					icon : Ext.MessageBox.INFO,
					buttons : Ext.Msg.OK
				})
				return;		
			} 
		}		
		if (selOption.callback) 
			selOption.callback(records);    		
		selwindow.close();
	}
	//联动
	function loadgriddetail(node){
		var tid='';	
		if (tree.getSelectionModel().selNode)
		    tree.getSelectionModel().selNode.unselect();		
		if (!node){				
			if (tree.getRootNode().firstChild){
				tid=tree.getRootNode().firstChild.id;
			}
		}else{	
			tid=node.attributes.id;
		} 	
		suungrid.searchClear();  
		suungrid.searchClear(true); 
		suungrid.store.load({
			params : {
				start : 0,
				limit : selOption.grid.pagenum,
				treeid: tid
			},
			callback: function(r,options,success){
				if (!node){				
					if (tree.getRootNode().firstChild){
						tree.getRootNode().firstChild.select();
					}
				} else{			
					node.select();
				}
			}
		});
	}		
}

function gridselect(selOption){	
	//处理默认值
	if (!selOption.listurl) {
		alert("url is null")
		return;
	}		
	suunCore.showMask();     	
	if (!selOption.winWidth) selOption.winWidth=500;
	if (!selOption.winHeight) selOption.winHeight=350;
	if ("undefined" == typeof selOption.multiSel) selOption.multiSel=false;	
	if (!selOption.pagenum) selOption.pagenum=20;
	var ids="";
	if (selOption.existurl){
		$.ajax({ 
			url :selOption.existurl, 
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
	var suungrid=new suunGridPanel({
	    listurl:selOption.listurl,
		pagenum:selOption.pagenum,
		isprewidth:selOption.isprewidth,
		suuncolumns:selOption.suuncolumns,	
		region : "center",
		bbarInfo:false,
		storebeforeload:function (store, options){
			Ext.apply(store.baseParams,{existids:ids});			
		}
	});	
	suungrid.getTopToolbar().insert(0,'-');
	suungrid.getTopToolbar().insert(0,new Ext.Button({
		iconCls : 'select',
		text : "确定",
		handler : selectRecord
	}));
	Ext.getCmp('Q_SearchId-'+suungrid.getSuunid()).width=100;
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
		items : [suungrid],
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
}
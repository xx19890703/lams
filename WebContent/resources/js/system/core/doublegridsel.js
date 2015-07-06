function doublegridselect(selOption) {
	if (!selOption.grid1||!selOption.grid2) {
		alert("grid is null")
		return;
	}
	if (!selOption.baseurl){
		if (!selOption.grid1.listurl||!selOption.grid2.listurl){
			alert("baseurl is null")
			return;
		}		    
	}else{
		if (!selOption.grid1.listurl){
			selOption.grid1.listurl=selOption.baseurl+'!grid1lists';
		}
		if (!selOption.grid2.listurl){
			selOption.grid2.listurl=selOption.baseurl+'!grid2lists';
		}
	}
	if (!selOption.grid1.keyid) {
		alert("grid.keyid is null")
		return;
	}
	//处理默认值
	if ("undefined" == typeof selOption.vertical) selOption.vertical=true;
	if (!selOption.winWidth) selOption.winWidth=600;
	if (!selOption.winHeight) selOption.winHeight=400;
	if ("undefined" == typeof selOption.multiSel) selOption.multiSel=false;
	if (!selOption.grid1.pagenum) selOption.grid1.pagenum=20;
	if (!selOption.grid1.height) 
		selOption.grid1.height='220'
	else if (selOption.grid1.height.indexOf('%')>=0)
		selOption.grid1.height=parseFloat(selOption.grid1.height)*(selOption.winHeight-40)/100;	
	if (!selOption.grid1.width) 
		selOption.grid1.width='150';
	else if (selOption.grid1.width.indexOf('%')>=0)
		selOption.grid1.width=parseFloat(selOption.grid1.width)*(selOption.winWidth-20)/100;
	if (!selOption.grid2.pagenum) selOption.grid2.pagenum=20;
	suunCore.showMask();
	var ids="";
	if (selOption.grid2.existurl){
		$.ajax({ 
			url :selOption.grid2.existurl, 
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
	//grid1	
	var suungrid1=new suunGridPanel({ 
		title:selOption.grid1.title,
		simplemode:selOption.grid2.simplemode,
		split:true,
	    listurl:selOption.grid1.listurl,
		pagenum:selOption.grid1.pagenum,
		isprewidth:selOption.grid1.isprewidth,
		suuncolumns:selOption.grid1.suuncolumns,		
		bbarInfo:false,
		checkboxSel:false
	});
	if (selOption.vertical){
		suungrid1.region='north'; 
		suungrid1.height=selOption.grid1.height;
		suungrid1.minHeight=100;
	}else{ 
		suungrid1.region='west';
		suungrid1.width=selOption.grid1.width;
		suungrid1.minWidth=150;
	}
	Ext.getCmp('Q_SearchId-'+suungrid1.getSuunid()).width=100;
	Ext.getCmp('Q_svalue-'+suungrid1.getSuunid()).width=50; 
	Ext.getCmp('Q_nvalue-'+suungrid1.getSuunid()).width=50;
	Ext.getCmp('Q_dvalue-'+suungrid1.getSuunid()).width=50;
	Ext.getCmp('Q_cvalue-'+suungrid1.getSuunid()).width=50;
	suungrid1.on("rowclick", function(){
	    suungrid2.searchClear();  
		suungrid2.searchClear(true); 
		suungrid2.loadStore();
	});
	suungrid1.store.on('load', function () {
		if (this.data.length>0){
			if (suungrid1.getSelectionModel().getSelections().length==0)
			    suungrid1.getSelectionModel().selectRecords([this.data.items[0]]);			
		}
		suungrid2.searchClear();  
		suungrid2.searchClear(true); 
		suungrid2.loadStore();
	});
	var suungrid2=new suunGridPanel({
		title:selOption.grid2.title,
		simplemode:selOption.grid2.simplemode,
	    listurl:selOption.grid2.listurl,
		pagenum:selOption.grid2.pagenum,
		isprewidth:selOption.grid2.isprewidth,
		suuncolumns:selOption.grid2.suuncolumns,	
		region : "center",		
		bbarInfo:false,
		storeAutoLoad:false,
		storebeforeload:function (store, options){
		    var tid=''
			if (suungrid1.getSelectionModel().getSelections().length>0)
				tid=suungrid1.getSelectionModel().getSelections()[0].get(selOption.grid1.keyid);		
			else if (suungrid1.store.data.length>0){//前面已经先选中了suungrid1，所以不会存在此情况
				tid=suungrid1.store.data.items[0].get(selOption.grid1.keyid);
			}
			Ext.apply(store.baseParams, {parentid:tid,existids:ids});			
		}
	});
	
	suungrid2.getTopToolbar().insert(0,'-');
	suungrid2.getTopToolbar().insert(0,new Ext.Button({
		iconCls : 'select',
		text : "确定",
		handler : selectRecord
	}));
	Ext.getCmp('Q_SearchId-'+suungrid2.getSuunid()).width=100;
    Ext.getCmp('Q_svalue-'+suungrid2.getSuunid()).width=50; 
	Ext.getCmp('Q_nvalue-'+suungrid2.getSuunid()).width=50;
	Ext.getCmp('Q_dvalue-'+suungrid2.getSuunid()).width=50;
	Ext.getCmp('Q_cvalue-'+suungrid2.getSuunid()).width=50;	
	suungrid2.addListener("rowdblclick", selectRecord);
	
	var selwindow=new Ext.Window({
		title : '数据信息',
		width : selOption.winWidth,
		height : selOption.winHeight,
		maximized : false,
		modal:true,
		layout : 'border',
		items : [suungrid1,suungrid2],
		listeners:{
			'show':function (){suunCore.hideMask();}
		}
	});
	selwindow.show();	
	
	// 选择
	function selectRecord(){    	
		var records = suungrid2.getSelectionModel().getSelections();
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
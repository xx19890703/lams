function treeselect(selOption) {
	//处理默认值
	if (!selOption.listurl) {
		alert("url is null")
		return;
	}		
	suunCore.showMask();                	
	if (!selOption.winWidth) selOption.winWidth=400;
	if (!selOption.winHeight) selOption.winHeight=350;
	if ("undefined" == typeof selOption.multiSel) selOption.multiSel=false;
	
	var topbar=[{
		iconCls : 'select',
		text : "确定",
		handler : selectRecord
	},'-'];
	
	var tree = new suunTreePanel({  
		listurl:selOption.listurl,	
		extTopbar:topbar,
		region : "center",		     		
		allowCheckbox:true
    });

	var selwindow=new Ext.Window({
		title : '数据信息',
		width : selOption.winWidth,
		height : selOption.winHeight,
		maximized : false,
		modal:true,
		layout : 'border',
		items : [tree],
		listeners:{
			'show':function (){suunCore.hideMask();}
		}
	});
	selwindow.show();	

	tree.addListener("dblclick", selectRecord);
	
	// 选择
	function selectRecord(){    	
		var nodes = tree.getChecked();
		if (nodes.length == 0) {
			Ext.MessageBox.show({
				title : '提示',
				msg : '请选择信息!',
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			})
			return;		
		} else if (!selOption.multiSel){
			if (nodes.length > 1) {
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
			selOption.callback(nodes);    		
		selwindow.close();
	}
}

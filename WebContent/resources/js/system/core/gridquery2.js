Ext.onReady(function() {
	
	var panel2 = new Ext.Panel( {   
        id:  "panel2",  
        region : "center",
        fitToFrame: true,                   
        html: '<iframe id="im" style="background-color:white;overflow:auto;width:100%; height:100%;" src="" frameborder="0"></iframe>'  
    });    
	
	
	
	var treepanel = new Ext.tree.TreePanel( {
		title : '报表类别',
		id:"treep",
		width:'150',
		collapsible: true,
		region: 'west',
		border : false,
		autoScroll : true,
		rootVisible : false,
		loader : new Ext.tree.TreeLoader({
			url:$ctx+'/serviceuser/query!queryTemplate',
			baseParams : {  
				contractId : ''  
            }  
		}),
		root : new Ext.tree.AsyncTreeNode( {
			id : '0',  
			expanded:true,
	        draggable:false,
			text : "aaa"
		}),
		listeners : {
			'click': function(n) {
				document.getElementById('im').src=$ctx+"/ReportServer?reportlet="+n.id+"&op=write";
			}
		}     
	});  
	
	var view = new Ext.Viewport({
		layout : "border",
		items : [{
			height:60,
			region : "north",
			id:'condivp',
			items:[{
				layout : 'column',
				labelWidth : 60,
				frame:true,
				labelAlign : "right",
				items : [
							{
								layout : 'form',
								columnWidth : .2, // 该列占用的宽度，标识为50％
								width : 160,
								items : [{
									name : "htmcs",
									xtype : "textfield",
									id : "htmcs",
									readOnly:true,
									fieldLabel : "合同名称"
								}]
							},{
								layout : 'form',
								columnWidth : .2, // 该列占用的宽度，标识为50％
								width : 160,
								items : [{
									name : "htbhs",
									xtype : "textfield",
									id : "htbhs",
									readOnly:true,
									fieldLabel : "合同编号"
								}]
							}]
			}],
			tbar:[{text:"查询",
				iconCls:'edit',
				handler:showSelect}]
		},treepanel,panel2]
		
	});

})

function showSelect() {
	// ArrayReader
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : $ctx + '/serviceuser/query!queryContract'
		}),
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, [ {
			name : 'did'
		}, {
			name : 'name'
		}, {
			name : 'orderinfo'
		}, {
			name : 'finfo.fname'
		}, {
			name : 'conmain.name'
		} ])
	});
	var smm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),// 自动行号
	smm, {
		header : '合同编号',
		dataIndex : 'did'
	}, {
		header : '合同名称',
		dataIndex : 'name'
	}, {
		header : '所属订单',
		dataIndex : 'orderinfo'
	}, {
		header : '制造厂',
		dataIndex : 'finfo.fname'
	}, {
		header : '分类名',
		dataIndex : 'conmain.name'
	} ]);

	var congrid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		region : "center",
		width : 400,
		height : 300
	});

	var formm = new Ext.form.FormPanel(
			{
				title : "查询条件",
				height : 120,
				width : 605,
				frame : true,
				region : "north",
				items : [ {
					layout : 'column',
					labelWidth : 60,
					labelAlign : "right",
					items : [
							{
								layout : 'form',
								columnWidth : .5, // 该列占用的宽度，标识为50％
								width : 160,
								items : [
										{
											name : "zzcid",
											xtype : "textfield",
											id : "zzcid",
											hidden : true,
											fieldLabel : "制造厂id"
										},
										{
											xtype : "combo",
											name : "zzc",
											id : 'zzc',
											width : 150,
											fieldLabel : "制造厂",
											onTriggerClick : function() {
												gridselect({
													listurl : $ctx
															+ '/serviceuser/factoryInfo!lists',// 基本url
													suuncolumns : [ {
														columnid : 'fno',
														columnname : '制造厂编号',
														colwidth : 20,
														defaultsort : true
													}, {
														columnid : 'fname',
														columnname : '制造厂名称',
														colwidth : 40
													} ],
													callback : function(records) {
														Ext
																.getCmp('zzcid')
																.setValue(
																		records[0]
																				.get('fno'));
														Ext
																.getCmp('zzc')
																.setValue(
																		records[0]
																				.get('fname'));
													}
												});
											}
										}, {
											name : "htmc",
											xtype : "textfield",
											id : "htmc",
											fieldLabel : "合同名称"
										} ]
							}, {
								layout : 'form',
								columnWidth : .5, // 该列占用的宽度，标识为50％
								items : [ {
									name : "htbh",
									xtype : "textfield",
									id : "htbh",
									fieldLabel : "合同编号"
								}, {
									name : "ssdd",
									xtype : "textfield",
									id : "ssdd",
									fieldLabel : "所属订单"
								} ]
							} ]
				} ],
				buttons : [ {
					text : '查询',
					handler : function() {
						var zzcid = Ext.getCmp("zzcid").getValue()
						var htmc = Ext.getCmp("htmc").getValue()
						var htbh = Ext.getCmp("htbh").getValue()
						var ssdd = Ext.getCmp("ssdd").getValue()
						ds.load({
							params : {
								zzcid : zzcid,
								htmc : htmc,
								htbh : htbh,
								ssdd : ssdd
							}
						});
					}
				} ]
			});

	var htselectPanel = new Ext.Window({
		resizable : false,
		modal : true,
		id : "suunFormWindow",
		autoScroll : true,
		width : 620,
		height : 570,
		layout : "border",
		closeAction : "close",
		title : '<center style="curor:hand">合同查询</center>',
		items : [ congrid, formm ],
		buttons : [ {
			id : "BtnSave",
			text : "查 询",
			handler : function() {
				var re = congrid.getSelectionModel().getSelections();
				if (re.length == 0) {
					Ext.MessageBox.show({
						title : "提示",
						msg : "请先选择您要查看的合同！",
						icon : Ext.MessageBox.WARNING,
						buttons : Ext.Msg.OK
					});
					return;
				} else if (re.length > 1) {
					Ext.MessageBox.show({
						title : "提示",
						msg : "只能选择一条记录！",
						icon : Ext.MessageBox.WARNING,
						buttons : Ext.Msg.OK
					});
					return;
				}
				selcontractId = re[0].get('did');
				var aphyciniTree = Ext.getCmp('treep');
				var loader = aphyciniTree.getLoader()
				loader.on('beforeload', function(loader, node) {
					this.baseParams.contractId = selcontractId; // 通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载  
			    }, loader);  
				loader.load(aphyciniTree.root,function(){
					var aphyciniTree2 = Ext.getCmp('treep');
					if(aphyciniTree2.root.childNodes.length>0){
						var fn=aphyciniTree2.root.childNodes[0];
						document.getElementById('im').src=$ctx+"/ReportServer?reportlet="+fn.id+"&op=write";
					}
				}); 
				Ext.getCmp('htbhs').setValue(selcontractId);
				Ext.getCmp('htmcs').setValue(re[0].get('name'));
				htselectPanel.close();
			}
		}, {
			text : "取 消",
			handler : function() {
			}
		} ]
	});
	htselectPanel.show();
}
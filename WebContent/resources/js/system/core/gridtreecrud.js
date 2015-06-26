function creategridtree(option) {
	//处理默认值	
	if (!option.tree) option.tree={};
	if (!option.grid) {
		alert("grid is null")
		return;
	}
	if ("undefined" == typeof option.grid.allowEdit) option.grid.allowEdit=false;	
	if (!option.grid.keyid||!option.containerid) {
		alert("grid.keyid||containerid is null")
		return;
	}	
	if (!suunCore.CheckUrl("treegrid",option)) return;
	if (!option.grid.pagenum) option.grid.pagenum=20;
	if (!option.grid.width) option.grid.width='40%';
	if (!option.grid.inputFormWidth) option.grid.inputFormWidth=200;
	if (!option.grid.inputFormHeight) option.grid.inputFormHeight=150;	
	if ("undefined" == typeof option.grid.storeAutoLoad) option.grid.storeAutoLoad=true;
	option.grid.operation = option.grid.operation || {};
	option.grid.operation.add=option.grid.operation.add || {};
	option.grid.operation.edit=option.grid.operation.edit || {};
	option.grid.operation.del=option.grid.operation.del || {};
	option.grid.operation.exp=option.grid.operation.exp || {};
	option.grid.operation.extend=option.grid.operation.extend || [];
	Ext.applyIf(option.grid.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.grid.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.grid.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.grid.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.grid.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport('pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport('xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport('doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport('xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport('csv');}} 	
    ]);	                                             
	Ext.applyIf(option.grid.operation.extend,[]);
	
	if (!option.tree.inputFormWidth) option.tree.inputFormWidth=200;
	if (!option.tree.inputFormHeight) option.tree.inputFormHeight=150;
	option.tree.operation = option.tree.operation || {};
	option.tree.operation.add=option.tree.operation.add || {};
	option.tree.operation.edit=option.tree.operation.edit || {};
	option.tree.operation.del=option.tree.operation.del || {};
	option.tree.operation.extend=option.tree.operation.extend || [];
	Ext.applyIf(option.tree.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.tree.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.tree.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.tree.operation.extend,[]);	
	
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';
	//grid
	//为权限预留	 
	//var suunauths=suunCore.GetAuths(option);
	var gridtopbar=['->'];
    if (option.grid.allowEdit){
		if (suunCore.HaveAuths(option.authurl,option.grid.operation.add.auth)) {	
			if (!option.grid.operation.add.hidden){
				gridtopbar.push({
					iconCls:option.grid.operation.add.iconCls,
					text:option.grid.operation.add.text,
					tooltip:option.grid.operation.add.tooltip,
					handler:function(){
						if (!tree.selModel.selNode) {
							Ext.Msg.alert('消息','请选择树节点！');
							return;
						} 
						if (!option.grid.operation.add.onClick) {
							gridadd();
						} else {
							option.grid.operation.add.onClick(tree,suungrid);
						}
					}
			   });
			}		
		}
		if (suunCore.HaveAuths(option.authurl,option.grid.operation.edit.auth)) {	
			if (!option.grid.operation.edit.hidden){
				gridtopbar.push({
					iconCls:option.grid.operation.edit.iconCls,
					text:option.grid.operation.edit.text,
					tooltip:option.grid.operation.edit.tooltip,
					handler:function(){ 
						if (!option.grid.operation.edit.onClick) {
							gridedit(suungrid.getSelectionModel().getSelections());
						} else {
							option.grid.operation.edit.onClick(tree,suungrid);
						}
					}
			   });
			}
		}
		if (suunCore.HaveAuths(option.authurl,option.grid.operation.del.auth)) {	
			if (!option.grid.operation.del.hidden){
				gridtopbar.push({
					iconCls:option.grid.operation.del.iconCls,
					text:option.grid.operation.del.text,
					tooltip:option.grid.operation.del.tooltip,
					handler:function(){ 
						if (!option.grid.operation.del.onClick) {
							griddeleterecord();
						} else {
							option.grid.operation.del.onClick(tree,suungrid);
						}
					}
			   });
			}	
		}
	}
	var suungrid=new suunGridPanel({
		title:option.grid.title,
		simplemode:true,
	    listurl:option.grid.listurl,
		pagenum:option.grid.pagenum,
		isprewidth:option.grid.isprewidth,
		suuncolumns:option.grid.suuncolumns,	
		extTopbar:gridtopbar,
		extBottombar:[],		
		region : 'west',		     
		width : option.grid.width,
		split: true,
		storeAutoLoad:option.grid.storeAutoLoad
	});	
	suungrid.on("rowdblclick", viewrecord);
	suungrid.on("rowclick", function(){
	    var tid='';
		if (suungrid.getSelectionModel().getSelections().length>0){
			tid=suungrid.getSelectionModel().getSelections()[0].get(option.grid.keyid);		
		}else if (suungrid.store.data.length>0){//前面已经先选中了suungrid，所以不会存在此情况
			tid=suungrid.store.data.items[0].get(option.grid.keyid);
		}
		tree.root.setId(tid);
		tree.getLoader().load(tree.root);
		tree.root.ui.render();
	});
	suungrid.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid.getSelectionModel().selectRow(cur-1, true);//执行选中记录				
			}			
		}
		if (this.data.length>0){
			if (suungrid.getSelectionModel().getSelections().length==0)
			    suungrid.getSelectionModel().selectRecords([this.data.items[0]]);
			tree.getLoader().dataUrl=option.tree.listurl;
			tree.root.setId(suungrid.store.data.items[0].get(option.grid.keyid));
			tree.getLoader().load(tree.root);
			tree.root.ui.render();			
		}		
	});
	//tree	
	var treetopbar=[];	
	if (suunCore.HaveAuths(option.authurl,option.tree.operation.add.auth)) {
		if (!option.tree.operation.add.hidden){
			treetopbar.push({
				iconCls:option.tree.operation.add.iconCls,
				text:option.tree.operation.add.text,
				tooltip:option.tree.operation.add.tooltip,
				handler:function(){
					if (!option.tree.operation.add.onClick) {
						treeadd();
					} else {
						option.tree.operation.add.onClick(tree);
					}
				}
		   });
		}	
	}
	if (suunCore.HaveAuths(option.authurl,option.tree.operation.edit.auth)) {	
		if (!option.tree.operation.edit.hidden){
			treetopbar.push({
				iconCls:option.tree.operation.edit.iconCls,
				text:option.tree.operation.edit.text,
				tooltip:option.tree.operation.edit.tooltip,
				handler:function(){
					if (!option.tree.operation.edit.onClick) {
						treeedit();
					} else {
						option.tree.operation.edit.onClick(tree);
					}
				}
		   });
		}
	}
	if (suunCore.HaveAuths(option.authurl,option.tree.operation.del.auth)) {
		if (!option.tree.operation.del.hidden){
			treetopbar.push({
				iconCls:option.tree.operation.del.iconCls,
				text:option.tree.operation.del.text,
				tooltip:option.tree.operation.del.tooltip,
				handler:function(){
					if (!option.tree.operation.del.onClick) {
						treedeleterecord();
					} else {
						option.tree.operation.del.onClick(tree);
					}
				}
		   });
		}	
	}	

	var tree = new suunTreePanel({ 
		title:option.tree.title,
		listurl:option.tree.listurl,	
		extTopbar:treetopbar,
		region : "center",		
		allowCheckbox:true,
		autoLoadRoot:false,
		listeners : {
			'dblclick': function(n) {
				loadcontextdetail(n);
			}
		}     
    }); 	
	
	Ext.getCmp(option.containerid).add(suungrid);
	Ext.getCmp(option.containerid).add(tree);	
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
	
	return {"grid":suungrid,"tree":tree};
		
	//tree function	
	//浏览			
	function loadcontextdetail(node){
	    var tid=node.attributes.id;
		suunCore.createWinView({winWidth:option.tree.inputFormWidth,
			winHeight:option.tree.inputFormHeight,
			contexturl:option.tree.editurl,
			postparam:{suunplatformTreePid : tid}
		});
	}
    //新增
	function doadd(parentnode) {	
        suunCore.createWinForm({winTitle:"新增",
			winWidth:option.tree.inputFormWidth,
			winHeight:option.tree.inputFormHeight,
			contexturl:option.tree.newurl,
			postparam:{suunplatformTreePid : parentnode.attributes['id']},
			formAction:option.tree.saveurl,
			callback:function(message){	    		
				if(tree.getSelectionModel().selNode)     		
					tree.getSelectionModel().selNode.unselect();                
				if (parentnode.ui.checkbox)
					parentnode.ui.checkbox.checked=false;				
				parentnode.attributes.checked = false;
				if (parentnode.isLoaded()){	//没有加载过，展开时会自动重新加载					
					parentnode.appendChild({
						id : message.saveid,
						text : message.savename,
						icon : message.saveicon,
						checked : false
					});
				}
				parentnode.expand(false,false,function(pnode){
					node=pnode.findChild("id",message.saveid);
					loadgriddetail(node);
				});				
			}
		});			
	}
	
	function treeadd() {
	    if (suungrid.getSelectionModel().getSelections().length==0) {
			Ext.Msg.alert('消息','请父项记录！');
			return;
		}
		var records = tree.getChecked();
		if (records.length > 1) {
			Ext.Msg.alert('提示', '只能选择一个父节点！');
			return;
		}
		if (records.length == 0) {
			Ext.MessageBox.confirm('提示', '是否添加根目录?', function(btn) {
				if (btn == "yes") {
					selectnode = tree.root;
					doadd(selectnode);					
				} else {
					Ext.Msg.alert('提示', '请选择父节点！');
					return;
				}
			});
		} else {
			selectnode = records[0];
			doadd(selectnode);
		}		
	}
    //修改
	function treeedit() {
		var records = tree.getChecked();	
		if (records.length == 0) {
			Ext.Msg.alert('消息','请选择一条要修改的数据！');
			return;
		}
		if (records.length > 1) {
			Ext.Msg.alert('提示','一次只能选择一条数据修改！');
			return;
		}
		nodeobj = records[0];
		parentnodeobj = records[0].parentNode;
		suunCore.createWinForm({winTitle:"修改",
			winWidth:option.tree.inputFormWidth,
			winHeight:option.tree.inputFormHeight,
			contexturl:option.tree.editurl,
			postparam:{suunplatformTreePid : nodeobj.attributes['id']},
			formAction:option.tree.saveurl,
			callback:function(message){	    		
				nodeobj.setId(message.saveid);
				nodeobj.setText(message.savename);
				nodeobj.getUI().getIconEl().src=message.saveicon;
				nodeobj.ui.checkbox.checked=false;
				nodeobj.attributes.checked = false;
				loadgriddetail(nodeobj);
			}
		});	
	}
    //删除
	function treedeleterecord() {
		var records = tree.getChecked();
		if (records.length == 0) {
			Ext.Msg.alert('消息','请选择要删除的数据！');
			return;
		} else {
			var ids = [];
			var pn=null;
			Ext.each(records,function(rec) {
				if (!pn){
					pn=rec.parentNode;
				} else{
					if (pn!=rec.parentNode){
						Ext.Msg.alert('消息','不能选择不同父节点的数据！');
						pn=null;
						return;
					}
				}											
				ids.push(rec.attributes['id']);
			});
			if (pn){
				Ext.MessageBox.confirm('提示','确定要删除所选择的数据？',
				function(btn) {
					if (btn == "yes") {										
						var nodeids = ids.join(',');
						suunCore.DeleteRecords({contexturl:option.tree.deleteurl,
							postparam:{ ids: nodeids},
							callback:function(message){											    
								if (message.success == "true") {
									tree.getLoader().load(pn,function(){
										if (tree.getRootNode()==pn){
											loadgriddetail();
										} else{		
											loadgriddetail(pn);
										}
									}); 									
								} else {
									Ext.MessageBox.alert('提示','父节点下有子节点不能删除！');
								}
							}
						});												
					}
				})
			}
		}
	}
	//grid function	
	// 添加
	function gridadd(){ 		
		suunCore.createWinForm({winTitle:"新增",
			winWidth:option.grid.inputFormWidth,
			winHeight:option.grid.inputFormHeight,
			postparam:{treeid:tree.selModel.selNode.id},
			contexturl:option.grid.newurl,
			formAction:option.grid.saveurl,
			callback:function(message){	    		
				suungrid.searchClear();  
				suungrid.searchClear(true); 
				suungrid.loadStore()
			}
		});
	}
	//编辑
	function gridedit(record){
		if (record.length == 0) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "请先选择您要编辑的行！",
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.Msg.OK
			});
			return;
		} else if (record.length > 1) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "只能选择一行进行编辑！",
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.Msg.OK
			});
			return;
		} 
		suunCore.createWinForm({winTitle:"修改",
			winWidth:option.grid.inputFormWidth,
			winHeight:option.grid.inputFormHeight,
			contexturl:option.grid.editurl,
			postparam:{suunplatformeoperateid: record[0].get(option.grid.keyid),treeid:tree.selModel.selNode.id},
			formAction:option.grid.saveurl,
			callback:function(message){	    		
				suungrid.loadStore();
			}
		});
	}
	//删除
	function griddeleterecord() {
		var record =suungrid.getSelectionModel().getSelections(); 
		if (record.length == 0) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "请先选择您要删除的行！",
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			})
			return;
		} else {
			Ext.MessageBox.show({
				title : "提示",
				msg : "确信要删除选择的行吗？",
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OKCANCEL,
				fn:function(btn,text,opt){
					if (btn=='ok'){
						var ids = "";
						for (var i = 0; i < record.length; i++) {
							ids += record[i].get(option.grid.keyid)
							if (i < record.length - 1) {
								ids = ids + ",";
							}
						}
						suunCore.DeleteRecords({contexturl:option.grid.deleteurl,
							postparam:{ ids: ids,treeid:tree.selModel.selNode.id},
							callback:function(message){
								suungrid.loadStore()  
							}
						});            	    					
					}
				}
			});       	    					
		}
	}
	//查看
	function viewrecord(g, o, q) {
		suunCore.createWinView({winWidth:option.grid.inputFormWidth,
			winHeight:option.grid.inputFormHeight,
			contexturl:option.grid.editurl,
			postparam:{suunplatformeoperateid:g.getSelectionModel().getSelected().get(option.grid.keyid)}
		});
	}
	//导出
	function suunExport(exporttype){	
		var suunGridcolumnstr='';
		var cols=suungrid.colModel.config;
		for (i=0;i<option.grid.suuncolumns.length;i++){	        			
			for (j=0;j<cols.length;j++){
				if (cols[j].header==option.grid.suuncolumns[i].columnname){
					if (!cols[j].hidden){
						suunGridcolumnstr=suunGridcolumnstr+option.grid.suuncolumns[i].columnid+','
								+option.grid.suuncolumns[i].columnname+';';
						break;
					}
				}
			}            	
		}			
		var seacher=suungrid.getSeacher();
		htmlstr='<input type="text" name="SearchId" value="'+seacher.SearchId+'"/>'+
			'<input type="text" name="query" value="'+seacher.SearchValue+'"/>'+
			'<input type="text" name="zdvalues" value="'+seacher.zdvalues+'"/>'+
			'<input type="text" name="czvalues" value="'+seacher.czvalues+'"/>'+
			'<input type="text" name="dpvalues" value="'+seacher.suvalues+'"/>'+
			'<input type="text" name="gxvalues" value="'+seacher.gxvalues+'"/>'+
			'<input type="text" name="treeid" value="'+tree.selModel.selNode.id+'"/>'+
			'<input type="text" name="suunGridcolumns" value="'+suunGridcolumnstr.substring(0, suunGridcolumnstr.length-1)+'"/>';
		if (suungrid.store.sortInfo)				
			htmlstr=htmlstr+'<input type="text" name="sort" value="'+suungrid.store.sortInfo.field+'"/>'+
			'<input type="text" name="dir" value="'+suungrid.store.sortInfo.direction+'"/>';
		suunCore.GridExportFile({exporturl:option.grid.exporturl+'?dt='+ new Date().getTime(),
			contextstr:htmlstr,type:exporttype
		});
	}
}

function createtreegrid(option) {
	//处理默认值	
	if (!option.tree) option.tree={};
	if (!option.grid) {
		alert("grid is null")
		return;
	}
	if ("undefined" == typeof option.tree.allowEdit) option.tree.allowEdit=false;	
	if ("undefined" == typeof option.grid.simplemode) option.grid.simplemode=false;
	console.log(Ext.getCmp(option.containerid));
	if (!option.grid.keyid||!option.containerid) {
		alert("grid.keyid||containerid is null")
		return;
	}	
	if (!suunCore.CheckUrl("treegrid",option)) return;
	
	if (!option.tree.width) option.tree.width='25%';
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
	if (!option.grid.pagenum) option.grid.pagenum=20;
	if (!option.grid.inputFormWidth) option.grid.inputFormWidth=200;
	if (!option.grid.inputFormHeight) option.grid.inputFormHeight=150;	
	option.grid.operation = option.grid.operation || {};
	option.grid.operation.add=option.grid.operation.add || {};
	option.grid.operation.edit=option.grid.operation.edit || {};
	option.grid.operation.del=option.grid.operation.del || {};
	option.grid.operation.audit=option.grid.operation.audit || {};
	option.grid.operation.exp=option.grid.operation.exp || {};
	option.grid.operation.check=option.grid.operation.check || {};
	option.grid.operation.extend=option.grid.operation.extend || [];
	Ext.applyIf(option.grid.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.grid.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.grid.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});
	Ext.applyIf(option.grid.operation.audit,{hidden:true,iconCls:'select',text:"审核",tooltip:'审核信息'});
	Ext.applyIf(option.grid.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.grid.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport('pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport('xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport('doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport('xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport('csv');}} 	
    ]);	                                             
	Ext.applyIf(option.grid.operation.check,{hidden:true,iconCls:'submitflow',text:"提交",tooltip:'提交审批'});
	Ext.applyIf(option.grid.operation.extend,[]);
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';
	//tree
	//为权限预留	 
	//var suunauths=suunCore.GetAuths(option);
	var treetopbar=[];
	if (option.tree.allowEdit){
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
	}	

	var tree = new suunTreePanel({ 
		title:option.tree.title,
		listurl:option.tree.listurl,	
		extTopbar:treetopbar,
		region : 'west',		     
		width : option.tree.width,  		
		split: true,
		allowCheckbox:option.tree.allowEdit,
		listeners : {
		    'load':function (n) {
				if (!tree.selModel.selNode){
				    //tree.getRootNode().firstChild.select()此时节点没有显示，不能select()，所以选中方正loadgriddetail中
					loadgriddetail();
				}
			},
			'click': function(n) {
				loadgriddetail(n);
			},
			'dblclick': function(n) {
				loadcontextdetail(n);
			}
		}     
    }); 

	//grid
	//为权限预留
	var gridtopbar=['->'];
	if (!option.grid.operation.check.hidden){
		if (suunCore.HaveAuths(option.authurl,option.grid.operation.check.auth)) {	
			gridtopbar.push(
				new Ext.form.ComboBox({
				    id : "checkstate-"+option.containerid,
					width : 60,
					mode: 'local',
					typeAhead: false,
					displayText: '全部',
					value:'-1',
					editable: false,
					forceSelection: true,
					triggerAction: 'all',
					//selectOnFocus:true,
					store : [['-1', '全部'], ['0', '待提交']],
					listeners : {
						select :function(){
							suungrid.loadStore();
						}
					}
				})
			);
			gridtopbar.push({
				iconCls:option.grid.operation.check.iconCls,
				text:option.grid.operation.check.text,
				tooltip:option.grid.operation.check.tooltip,
	            handler:function(){ 
	                if (!option.grid.operation.check.onClick) {
	                	;
	                } else {
	                	option.grid.operation.check.onClick(tree,suungrid);
	                }
	            }
	       });
		}
	}
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
	// grid审核
	if (suunCore.HaveAuths(option.authurl,option.grid.operation.audit.auth)) {		
    	if (!option.grid.operation.audit.hidden){
			gridtopbar.push({
				iconCls:option.grid.operation.audit.iconCls,
				text:option.grid.operation.audit.text,
				tooltip:option.grid.operation.audit.tooltip,
	            handler:function(){ 
	                if (!option.grid.operation.audit.onClick) {
	                	gridauditrecord(refresh);
	                } else {
	                	option.grid.operation.audit.onClick(tree,suungrid);
	                }
	            }
	       });
		}	
	}
	var bottombar=[];
	if (!option.grid.simplemode && !option.grid.operation.exp.hidden &&
			option.grid.operation.exp.btns.length>0){
		if (suunCore.HaveAuths(option.authurl,option.grid.operation.exp.auth)) {		
			bottombar.push('-');
			for (i=0;i<option.grid.operation.exp.btns.length;i++){
				bottombar.push(option.grid.operation.exp.btns[i]);
			}
		}
	}

	var suungrid=new suunGridPanel({
		title:option.grid.title,
		simplemode:option.grid.simplemode,
	    listurl:option.grid.listurl,
		pagenum:option.grid.pagenum,
		isprewidth:option.grid.isprewidth,
		suuncolumns:option.grid.suuncolumns,	
		extTopbar:gridtopbar,
		extBottombar:bottombar,
		region : "center",
		storeAutoLoad:false,
		storebeforeload:function (store, options){
		    var tid='';
			if (tree.getSelectionModel().selNode)
				tid=tree.getSelectionModel().selNode.attributes.id;		
			else if (tree.getRootNode().firstChild){
				tid=tree.getRootNode().firstChild.id;
			}
			Ext.apply(store.baseParams, {treeid:tid});		
		}
	});
	
	function refresh(){
		suungrid.loadStore();
	}
	
	Ext.getCmp(option.containerid).add(tree);
	Ext.getCmp(option.containerid).add(suungrid);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
		
	suungrid.addListener("rowdblclick", viewrecord);
	suungrid.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid.selModel.selectRow(cur-1, true);//执行选中记录
			}
		}
	});
	return {"tree":tree,"grid":suungrid};
	
	//tree function
	//联动
	function loadgriddetail(node){
		var tid='';	
		if (tree.getSelectionModel().selNode)
		    if ( tree.getSelectionModel().selNode!=node)
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
				limit : option.grid.pagenum,
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
		if (!tree.selModel.selNode) {
			Ext.Msg.alert('消息','请选择树节点！');
			return;
		}
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
	
	//审核
	function gridauditrecord(refresh) {
		var record =suungrid.getSelectionModel().getSelections(); 
		if (record.length == 0) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "请先选择您要审核的行！",
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			})
			return;
		} else if (record.length > 1) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "只能选择一行数据进行审核！",
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.Msg.OK
			});
			return;
		}
		auditrecord(record[0].get(option.grid.keyid),refresh);
	}
	
	// 查看
	function viewrecord(g, o, q) {
		suunCore.createWinView({winWidth:option.grid.inputFormWidth,
			winHeight:option.grid.inputFormHeight,
			contexturl:option.grid.editurl,
			postparam:{suunplatformeoperateid:g.getSelectionModel().getSelected().get(option.grid.keyid),treeid:tree.selModel.selNode.id}
		});
	}
	// 导出
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

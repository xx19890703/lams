function createtree(option) {
	//处理默认值
	if (!option.containerid) {
		alert("containerid is null")
		return;
	}	
	if (!suunCore.CheckUrl("tree",option)) return;
	if (!option.inputFormWidth) option.inputFormWidth=200;
	if (!option.inputFormHeight) option.inputFormHeight=150;	
	option.operation = option.operation || {};
	option.operation.add=option.operation.add || {};
	option.operation.edit=option.operation.edit || {};
	option.operation.del=option.operation.del || {};
	option.operation.extend=option.operation.extend || [];
	Ext.applyIf(option.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.operation.extend,[]);
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';	
	//为权限预留
	//var suunauths=suunCore.GetAuths(option);
	var topbar=[];	
	if (suunCore.HaveAuths(option.authurl,option.operation.add.auth)) {	
		if (!option.operation.add.hidden){
			topbar.push({
				iconCls:option.operation.add.iconCls,
				text:option.operation.add.text,
				tooltip:option.operation.add.tooltip,
	            handler:function(){ 
	                if (!option.operation.add.onClick) {
	                	add();
	                } else {
	                	option.operation.add.onClick(tree);
	                }
	            }
	       });
		}	
	}	
	if (suunCore.HaveAuths(option.authurl,option.operation.edit.auth)) {	
		if (!option.operation.edit.hidden){
			topbar.push({
				iconCls:option.operation.edit.iconCls,
				text:option.operation.edit.text,
				tooltip:option.operation.edit.tooltip,
	            handler:function(){ 
	                if (!option.operation.edit.onClick) {
	                	edit();
	                } else {
	                	option.operation.edit.onClick(tree);
	                }
	            }
	       });
		}	
	}		
	if (suunCore.HaveAuths(option.authurl,option.operation.del.auth)) {	
		if (!option.operation.del.hidden){
			topbar.push({
				iconCls:option.operation.del.iconCls,
				text:option.operation.del.text,
				tooltip:option.operation.del.tooltip,
	            handler:function(){ 
	                if (!option.operation.del.onClick) {
	                	deleterecord();
	                } else {
	                	option.operation.del.onClick(tree);
	                }
	            }
	       });
		}	
	}						
	var tree = new suunTreePanel({  
		listurl:option.listurl,	
		extTopbar:topbar,
		region : 'west',		     
		width : '40%',  		
		split: true,
		allowCheckbox:true,
		listeners : {
			'click': function(n) {
				loadcontextdetail(n);
			}
		}     
    }); 
	tree.loader.on('load', function () {
		if (!tree.selModel.selNode)
			loadcontextdetail();
	});

	var inputpanel = new Ext.Panel({
		loadMask : {
			msg : "正在加载...,请稍等..."
		},
		//frame : true,
		autoScroll : true,
		containerScroll : true,
		border: true,
		region : "center",		
		listeners : {
			domready : function(d) {}
		}
	});	

	Ext.getCmp(option.containerid).add(tree);
	Ext.getCmp(option.containerid).add(inputpanel);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
    
    return tree;
	//浏览			
	function loadcontextdetail(node){
		var aurl='';
		var tid='';	
		if (tree.getSelectionModel().selNode)
		    tree.getSelectionModel().selNode.unselect();		
		if (!node){				
			if (tree.getRootNode().firstChild){
				aurl=option.editurl;
				tid=tree.getRootNode().firstChild.id;
			}else{
			    aurl=option.viewnulldetailurl; 
			}
		}else{
		    tid=node.attributes.id;
			aurl=option.editurl;			
		} 	
		
		suunCore.extComLoadurlReadonly(inputpanel,aurl,{suunplatformTreePid : tid},function(){
			if (!node){				
				if (tree.getRootNode().firstChild){
					tree.getRootNode().firstChild.select();
				}
			} else{			
			    node.select();
			}
		});
	}
    //新增
	function doadd(parentnode) {	
        suunCore.createWinForm({winTitle:"新增",
			winWidth:option.inputFormWidth,
			winHeight:option.inputFormHeight,
			contexturl:option.newurl,
			postparam:{suunplatformTreePid : parentnode.attributes['id']},
			formAction:option.saveurl,
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
					loadcontextdetail(node);
				});				
			}
		});			
	}
	
	function add() {
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
	function edit() {
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
			winWidth:option.inputFormWidth,
			winHeight:option.inputFormHeight,
			contexturl:option.baseurl+'!edit.do',
			postparam:{suunplatformTreePid : nodeobj.attributes['id']},
			formAction:option.saveurl,
			callback:function(message){	    		
				nodeobj.setId(message.saveid);
				nodeobj.setText(message.savename);
				nodeobj.getUI().getIconEl().src=message.saveicon;
				nodeobj.ui.checkbox.checked=false;
				nodeobj.attributes.checked = false;
				loadcontextdetail(nodeobj);
			}
		});	
	}
    //删除
	function deleterecord() {
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
						suunCore.DeleteRecords({contexturl:option.deleteurl,
							postparam:{ ids: nodeids},
							callback:function(message){											    
								if (message.success == "true") {
									tree.getLoader().load(pn); 
									if (tree.getRootNode()==pn){
										loadcontextdetail();
								    } else{		
										loadcontextdetail(pn);
									}
								} else {
									Ext.MessageBox.alert('提示',message.error);
								}
							}
						});												
					}
				})
			}
		}
	}
}

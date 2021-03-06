function createsuungrid(option){	
	//处理默认值
	if (!option.containerid||!option.keyid) {
		alert("containerid||keyid is null")
		return;
	}	
	if (!suunCore.CheckUrl("grid",option)) return;
	if ("undefined" == typeof option.isprewidth) option.isprewidth=true;
	if ("undefined" == typeof option.simplemode) option.simplemode=false;
	if (!option.pagenum) option.pagenum=20;
	if (!option.inputFormWidth) option.inputFormWidth=200;
	if (!option.inputFormHeight) option.inputFormHeight=150;
	option.operation = option.operation || {};
	option.operation.add=option.operation.add || {};
	option.operation.edit=option.operation.edit || {};
	option.operation.del=option.operation.del || {};
	option.operation.exp=option.operation.exp || {};
	option.operation.check=option.operation.check || {};
	option.operation.extend=option.operation.extend || [];
	Ext.applyIf(option.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport('pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport('xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport('doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport('xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport('csv');}} 	
    ]);	                                             
	Ext.applyIf(option.operation.check,{hidden:true,iconCls:'submitflow',text:"提交",tooltip:'提交审批'});
	Ext.applyIf(option.operation.extend,[]);
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';	
	//为权限预留
	//var suunauths=suunCore.GetAuths(option);
	var topbar=['->'];
	if (!option.operation.check.hidden){
		if (suunCore.HaveAuths(option.authurl,option.operation.check.auth)) {
			topbar.push(
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
						select :function (){    	
					    	suungrid.loadStore();
						}
					}
				})
			);
			topbar.push({
				iconCls:option.operation.check.iconCls,
				text:option.operation.check.text,
				tooltip:option.operation.check.tooltip,
	            handler:function(){ 
	                if (!option.operation.check.onClick) {
	                	;
	                } else {
	                	option.operation.check.onClick(suungrid);
	                }
	            }
	       });
		}
	}
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
	                	option.operation.add.onClick(suungrid);
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
	                	edit(suungrid.getSelectionModel().getSelections());
	                } else {
	                	option.operation.edit.onClick(suungrid);
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
	                	option.operation.del.onClick(suungrid);
	                }
	            }
	       });
		}	
	}
	var bottombar=[];
	if (!option.simplemode && !option.operation.exp.hidden &&
			option.operation.exp.btns.length>0){
		if (suunCore.HaveAuths(option.authurl,option.operation.exp.auth)) {	
			bottombar.push('-');
			for (i=0;i<option.operation.exp.btns.length;i++){
				bottombar.push(option.operation.exp.btns[i]);
			}
		}
	}	
	var suungrid=new suunGridPanel({
		simplemode:option.simplemode,
	    listurl:option.listurl,
		pagenum:option.pagenum,
		isprewidth:option.isprewidth,
		suuncolumns:option.suuncolumns,	
		extTopbar:topbar,
		extBottombar:bottombar,
		region : "center",
		storebeforeload:function (store, options){
			if (!option.operation.check.hidden){
				checkstate=Ext.getCmp("checkstate-"+option.containerid).getValue()==null? 
		    			-1:Ext.getCmp("checkstate-"+option.containerid).getValue();
				Ext.apply(store.baseParams,{state:checkstate});
			}			
		}
	});
	
	Ext.getCmp(option.containerid).add(suungrid);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
		
	suungrid.addListener("rowdblclick", viewrecord);
	suungrid.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid.getSelectionModel().selectRow(cur-1, true);//执行选中记录
			}
		} 
	});
	return suungrid;
	// 添加
	function add(){    	
		suunCore.createWinForm({winTitle:"新增",
			winWidth:option.inputFormWidth,
			winHeight:option.inputFormHeight,
			contexturl:option.newurl,
			formAction:option.saveurl,
			callback:function(message){	    		
				suungrid.searchClear();  
				suungrid.searchClear(true); 
				suungrid.loadStore()
			}
		});
	}
	//编辑
	function edit(record){
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
			winWidth:option.inputFormWidth,
			winHeight:option.inputFormHeight,
			contexturl:option.editurl,
			postparam:{suunplatformeoperateid: record[0].get(option.keyid)},
			formAction:option.saveurl,
			callback:function(message){	    		
				suungrid.loadStore();
			}
		});
	}
	//删除
	function deleterecord() {
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
							ids += record[i].get(option.keyid)
							if (i < record.length - 1) {
								ids = ids + ",";
							}
						}
						suunCore.DeleteRecords({contexturl:option.deleteurl,
							postparam:{ ids: ids},
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
		suunCore.createWinView({winWidth:option.inputFormWidth,
			winHeight:option.inputFormHeight,
			contexturl:option.editurl,
			postparam:{suunplatformeoperateid:g.getSelectionModel().getSelected().get(option.keyid)}
		});
	}
	//导出
	function suunExport(exporttype){	
		var suunGridcolumnstr='';
		var cols=suungrid.colModel.config;
		for (i=0;i<option.suuncolumns.length;i++){	        			
			for (j=0;j<cols.length;j++){
				if (cols[j].header==option.suuncolumns[i].columnname){
					if (!cols[j].hidden){
						suunGridcolumnstr=suunGridcolumnstr+option.suuncolumns[i].columnid+','
								+option.suuncolumns[i].columnname+';';
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
			'<input type="text" name="suunGridcolumns" value="'+suunGridcolumnstr.substring(0, suunGridcolumnstr.length-1)+'"/>';
		if (suungrid.store.sortInfo)				
			htmlstr=htmlstr+'<input type="text" name="sort" value="'+suungrid.store.sortInfo.field+'"/>'+
			'<input type="text" name="dir" value="'+suungrid.store.sortInfo.direction+'"/>';
		suunCore.GridExportFile({exporturl:option.exporturl+'?dt='+ new Date().getTime(),
			contextstr:htmlstr,type:exporttype
		});
	}    
}
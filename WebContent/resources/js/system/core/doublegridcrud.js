function createdoublegrid(option) {
	if (!option.grid1||!option.grid2) {
		alert("grid is null")
		return;
	}	
	if (!option.grid1.keyid||!option.grid2.keyid||!option.containerid) {
		alert("grid.keyid||containerid is null")
		return;
	}
	//处理默认值
	if ("undefined" == typeof option.grid1.allowEdit) option.grid1.allowEdit=false;
	if ("undefined" == typeof option.grid1.simplemode) option.grid1.simplemode=false;
	if ("undefined" == typeof option.grid2.simplemode) option.grid2.simplemode=false;
	if (!suunCore.CheckUrl("doublegrid",option)) return;	
		
	if (!option.vertical) option.vertical=true;
	if (!option.grid1.height) option.grid1.height=200;
	if (!option.grid1.width) option.grid1.width=400;
	if (!option.grid1.pagenum) option.grid1.pagenum=20;
	if (!option.grid1.inputFormWidth) option.grid1.inputFormWidth=200;
	if (!option.grid1.inputFormHeight) option.grid1.inputFormHeight=150;
	option.grid1.operation = option.grid1.operation || {};
	option.grid1.operation.add=option.grid1.operation.add || {};
	option.grid1.operation.edit=option.grid1.operation.edit || {};
	option.grid1.operation.del=option.grid1.operation.del || {};
	option.grid1.operation.exp=option.grid1.operation.exp || {};
	option.grid1.operation.check=option.grid1.operation.check || {};
	option.grid1.operation.extend=option.grid1.operation.extend || [];
	Ext.applyIf(option.grid1.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.grid1.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.grid1.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.grid1.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.grid1.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport(1,'pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport(1,'xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport(1,'doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport(1,'xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport(1,'csv');}} 	
    ]);	                                             
	Ext.applyIf(option.grid1.operation.check,{hidden:true,iconCls:'submitflow',text:"提交",tooltip:'提交审批'});
	Ext.applyIf(option.grid1.operation.extend,[]);
	if (!option.grid2.pagenum) option.grid2.pagenum=20;
	if (!option.grid2.inputFormWidth) option.grid2.inputFormWidth=200;
	if (!option.grid2.inputFormHeight) option.grid2.inputFormHeight=150;
	option.grid2.operation = option.grid2.operation || {};
	option.grid2.operation.add=option.grid2.operation.add || {};
	option.grid2.operation.edit=option.grid2.operation.edit || {};
	option.grid2.operation.del=option.grid2.operation.del || {};
	option.grid2.operation.exp=option.grid2.operation.exp || {};
	option.grid2.operation.check=option.grid2.operation.check || {};
	option.grid2.operation.extend=option.grid2.operation.extend || [];
	Ext.applyIf(option.grid2.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.grid2.operation.edit,{hidden:false,iconCls:'edit',text:"修改",tooltip:'修改一条信息'});
	Ext.applyIf(option.grid2.operation.del,{hidden:false,iconCls:'remove',text:"删除",tooltip:'删除信息'});	
	Ext.applyIf(option.grid2.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.grid2.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport(1,'pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport(1,'xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport(1,'doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport(1,'xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport(1,'csv');}} 	
    ]);	                                             
	Ext.applyIf(option.grid2.operation.check,{hidden:true,iconCls:'submitflow',text:"提交",tooltip:'提交审批'});
	Ext.applyIf(option.grid2.operation.extend,[]);
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';
	//grid1
	//为权限预留	 
	//var suunauths='';
	if (option.grid1.auths||option.grid2.auths){
		//suunauths=suunCore.GetAuths(option);
	} else{
		option.grid1.auths={};
		option.grid2.auths={};
	}
	var grid1topbar=['->'];
	if (!option.grid1.operation.check.hidden){
		if (suunCore.HaveAuths(option.authurl,option.grid1.operation.check.auth)) {		
			grid1topbar.push(
				new Ext.form.ComboBox({
				    id : "checkstate1-"+option.containerid,
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
					    	suungrid1.loadStore();
						}
					}
				})
			);
			grid1topbar.push({
				iconCls:option.grid1.operation.check.iconCls,
				text:option.grid1.operation.check.text,
				tooltip:option.grid1.operation.check.tooltip,
	            handler:function(){ 
	                if (!option.grid1.operation.check.onClick) {
	                	;
	                } else {
	                	option.grid1.operation.check.onClick(suungrid1);
	                }
	            }
	       });
		}
	}
	if (option.grid1.allowEdit){
		if (suunCore.HaveAuths(option.authurl,option.grid1.operation.add.auth)) {		
			if (!option.grid1.operation.add.hidden){
				grid1topbar.push({
					iconCls:option.grid1.operation.add.iconCls,
					text:option.grid1.operation.add.text,
					tooltip:option.grid1.operation.add.tooltip,
		            handler:function(){
		                if (!option.grid1.operation.add.onClick) {
		                	add(1);
		                } else {
		                	option.grid1.operation.add.onClick(suungrid1);
		                }
		            }
		       });
			}	
		}
		if (suunCore.HaveAuths(option.authurl,option.grid1.operation.edit.auth)) {		
			if (!option.grid1.operation.edit.hidden){
				grid1topbar.push({
					iconCls:option.grid1.operation.edit.iconCls,
					text:option.grid1.operation.edit.text,
					tooltip:option.grid1.operation.edit.tooltip,
		            handler:function(){
		                if (!option.grid1.operation.edit.onClick) {
		                	edit(1,suungrid1.getSelectionModel().getSelections());
		                } else {
		                	option.grid1.operation.edit.onClick(suungrid1);
		                }
		            }
		       });
			}	
		}
		if (suunCore.HaveAuths(option.authurl,option.grid1.operation.del.auth)) {	
			if (!option.grid1.operation.del.hidden){
				grid1topbar.push({
					iconCls:option.grid1.operation.del.iconCls,
					text:option.grid1.operation.del.text,
					tooltip:option.grid1.operation.del.tooltip,
		            handler:function(){
		                if (!option.grid1.operation.del.onClick) {
		                	deleterecord(1,suungrid1.getSelectionModel().getSelections());
		                } else {
		                	option.grid1.operation.del.onClick(suungrid1);
		                }
		            }
		       });
			}	
		}
	}	
	var grid1bottombar=[];
	if (!option.grid1.simplemode && !option.grid1.operation.exp.hidden &&
			option.grid1.operation.exp.btns.length>0){
	    if (suunCore.HaveAuths(option.authurl,option.grid1.operation.exp.auth)) {		
			grid1bottombar.push('-');
			for (i=0;i<option.grid1.operation.exp.btns.length;i++){
				grid1bottombar.push(option.grid1.operation.exp.btns[i]);
			}
		}
	}	
	//grid2
	var grid2topbar=['->'];
	if (!option.grid2.operation.check.hidden){
		if (suunCore.HaveAuths(option.authurl,option.grid2.operation.check.auth)) {		
			grid2topbar.push(
				new Ext.form.ComboBox({
				    id : "checkstate2-"+option.containerid,
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
					    	suungrid2.loadStore();
						}
					}
				})
			);
			grid2topbar.push({
				iconCls:option.grid2.operation.check.iconCls,
				text:option.grid2.operation.check.text,
				tooltip:option.grid2.operation.check.tooltip,
	            handler:function(){ 
	                if (!option.grid2.operation.check.onClick) {
	                	;
	                } else {
	                	option.grid2.operation.check.onClick(suungrid1,suungrid2);
	                }
	            }
	       });
			grid2topbar.push(option.grid2.operation.check);
		}
	}	
	if (suunCore.HaveAuths(option.authurl,option.grid2.operation.add.auth)) {
		if (!option.grid2.operation.add.hidden){
			grid2topbar.push({
				iconCls:option.grid2.operation.add.iconCls,
				text:option.grid2.operation.add.text,
				tooltip:option.grid2.operation.add.tooltip,
	            handler:function(){
	                if (!option.grid2.operation.add.onClick) {
	                	add(2);
	                } else {
	                	option.grid2.operation.add.onClick(suungrid1,suungrid2);
	                }
	            }
	       });
		}	
	}
	if (suunCore.HaveAuths(option.authurl,option.grid2.operation.edit.auth)) {	
		if (!option.grid2.operation.edit.hidden){
			grid2topbar.push({
				iconCls:option.grid2.operation.edit.iconCls,
				text:option.grid2.operation.edit.text,
				tooltip:option.grid2.operation.edit.tooltip,
	            handler:function(){
	                if (!option.grid2.operation.edit.onClick) {
	                	edit(2,suungrid2.getSelectionModel().getSelections());
	                } else {
	                	option.grid2.operation.edit.onClick(suungrid1,suungrid2);
	                }
	            }
	       });
		}	
	}
	if (suunCore.HaveAuths(option.authurl,option.grid2.operation.del.auth)) {
		if (!option.grid2.operation.del.hidden){
			grid2topbar.push({
				iconCls:option.grid2.operation.del.iconCls,
				text:option.grid2.operation.del.text,
				tooltip:option.grid2.operation.del.tooltip,
	            handler:function(){
	                if (!option.grid2.operation.del.onClick) {
	                	deleterecord(2,suungrid2.getSelectionModel().getSelections());
	                } else {
	                	option.grid2.operation.del.onClick(suungrid1,suungrid2);
	                }
	            }
	       });
		}		
	}
	var grid2bottombar=[];
	if (!option.grid2.simplemode && !option.grid2.operation.exp.hidden &&
			option.grid2.operation.exp.btns.length>0){
		if (suunCore.HaveAuths(option.authurl,option.grid2.operation.exp.auth)) {
			grid2bottombar.push('-');
			for (i=0;i<option.grid2.operation.exp.btns.length;i++){
				grid2bottombar.push(option.grid2.operation.exp.btns[i]);
			}			
		}
	}
	
	var suungrid1=new suunGridPanel({
		title:option.grid1.title,
		simplemode:option.grid1.simplemode,
		split:true,
	    listurl:option.grid1.listurl,
		pagenum:option.grid1.pagenum,
		isprewidth:option.grid1.isprewidth,
		suuncolumns:option.grid1.suuncolumns,	
		extTopbar:grid1topbar,
		extBottombar:grid1bottombar
	});
	if (option.vertical){
		suungrid1.region='north'; 
		suungrid1.height=option.grid1.height;
	}else{ 
		suungrid1.region='west';
		suungrid1.width=option.grid1.width;
	}
	suungrid1.on("rowdblclick", function () {viewrecord(1)});
	suungrid1.on("rowclick", function(){
	    suungrid2.searchClear();  
		suungrid2.searchClear(true); 
		suungrid2.loadStore();
	});
	suungrid1.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid1.getSelectionModel().selectRow(cur-1, true);//执行选中记录				
			}			
		}
		if (this.data.length>0){
			if (suungrid1.getSelectionModel().getSelections().length==0)
			    suungrid1.getSelectionModel().selectRecords([this.data.items[0]]);			
		}
		suungrid2.searchClear();  
		suungrid2.searchClear(true); 
		suungrid2.loadStore();
	});
	var suungrid2=new suunGridPanel({
		title:option.grid2.title,
		simplemode:option.grid2.simplemode,
	    listurl:option.grid2.listurl,
		pagenum:option.grid2.pagenum,
		isprewidth:option.grid2.isprewidth,
		suuncolumns:option.grid2.suuncolumns,	
		extTopbar:grid2topbar,
		extBottombar:grid2bottombar,
		region : "center",
		storeAutoLoad:false,
		storebeforeload:function (store, options){
		    var tid='';
			if (suungrid1.getSelectionModel().getSelections().length>0)
				tid=suungrid1.getSelectionModel().getSelections()[0].get(option.grid1.keyid);		
			else if (suungrid1.store.data.length>0){//前面已经先选中了suungrid1，所以不会存在此情况
				tid=suungrid1.store.data.items[0].get(option.grid1.keyid);
			}
			Ext.apply(store.baseParams, {parentid:tid});			
		}
	});
	
	suungrid2.addListener("rowdblclick", function () {viewrecord(2)});
	suungrid2.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid2.getSelectionModel().selectRow(cur-1, true);//执行选中记录
			}			
		}
	});
	
	Ext.getCmp(option.containerid).add(suungrid1);
	Ext.getCmp(option.containerid).add(suungrid2);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
	Ext.getCmp(option.containerid).doLayout(true);
	
	return {"grid1":suungrid1,"grid2":suungrid2};
	/*var resizer;
	if (option.vertical){
	    resizer=new Ext.Resizable(suungrid1.id, {
			pinned:true,
			dynamic: true,
			handles:'s',
			minHeight:100,	
			heightIncrement:1
	    });	
	}else{ 
		resizer=new Ext.Resizable(suungrid1.id, {
			pinned:true,
			dynamic: true,
			handles:'e',
			minWidth:100,	
			widthIncrement:1
	    });
	};
	resizer.on("resize", suungrid1.syncSize, Ext.getCmp(option.containerid));*/
	
    // 添加
    function add(no){   
        if (no==1) 	{
			suunCore.createWinForm({winTitle:"新增",
				winWidth:option.grid1.inputFormWidth,
				winHeight:option.grid1.inputFormHeight,
				contexturl:option.grid1.newurl,
				formAction:option.grid1.saveurl,
				callback:function(){	    		
					suungrid1.searchClear();  
					suungrid1.searchClear(true); 
					suungrid1.loadStore();
				}
			});
		} else{
			if (suungrid1.getSelectionModel().selections.length==0){
				Ext.Msg.alert('消息','请选择父表格的行！');
    			return;
			}
			suunCore.createWinForm({winTitle:"新增",
				winWidth:option.grid2.inputFormWidth,
				winHeight:option.grid2.inputFormHeight,
				contexturl:option.grid2.newurl,
				formAction:option.grid2.saveurl,
				postparam:{parentid: suungrid1.getSelectionModel().selections.items[0].get(option.grid1.keyid)},
				callback:function(){	    		
					suungrid2.searchClear();  
					suungrid2.searchClear(true); 
					suungrid2.loadStore();
				}
			});
		}
    }
    //编辑
    function edit(no,record){
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
		if (no==1){
			suunCore.createWinForm({winTitle:"修改",
				winWidth:option.grid1.inputFormWidth,
				winHeight:option.grid1.inputFormHeight,
				contexturl:option.grid1.editurl,
				formAction:option.grid1.saveurl,
				postparam:{suunplatformeoperateid: record[0].get(option.grid1.keyid)},
				callback:function(){	    		
					suungrid1.loadStore();
				}
			});		
		}else{
			suunCore.createWinForm({winTitle:"修改",
				winWidth:option.grid2.inputFormWidth,
				winHeight:option.grid2.inputFormHeight,
				contexturl:option.grid2.editurl,
				formAction:option.grid2.saveurl,
				postparam:{parentid: suungrid1.getSelectionModel().selections.items[0].get(option.grid1.keyid),
					suunplatformeoperateid: record[0].get(option.grid2.keyid)},
				callback:function(){	    		
					suungrid2.loadStore();
				}
			});
		}
    }
	//删除
	function deleterecord(no,record){
		if (record.length == 0) {
			Ext.MessageBox.show({
				title : "提示",
				msg : "请先选择您要删除的行！",
				icon : Ext.MessageBox.INFO,
				buttons : Ext.Msg.OK
			});
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
							ids += record[i].get(option.grid1.keyid)
							if (i < record.length - 1) {
								ids = ids + ",";
							}
						}
						if (no==1){
							suunCore.DeleteRecords({contexturl:option.grid1.deleteurl,
							    postparam:{ ids: ids},
								callback:function(message){
									suungrid1.loadStore();  
								}
							});
						}else{
							suunCore.DeleteRecords({contexturl:option.grid2.deleteurl,
							    postparam:{parentid: suungrid1.getSelectionModel().selections.items[0].get(option.grid1.keyid),
								    ids: ids},
								callback:function(message){
									suungrid2.loadStore();   
								}
							}); 	
						}												
					}
				}
			});        	    					
		}
	}
	//查看
	function viewrecord(no) {
	    if (no==1) {
			suunCore.createWinView({winWidth:option.grid1.inputFormWidth,
				winHeight:option.grid1.inputFormHeight,
				contexturl:option.grid1.editurl,
				postparam:{suunplatformeoperateid:suungrid1.getSelectionModel().getSelected().get(option.grid1.keyid)}
			});
		} else{
			suunCore.createWinView({winWidth:option.grid2.inputFormWidth,
				winHeight:option.grid2.inputFormHeight,
				contexturl:option.grid2.editurl,
				postparam:{parentid: suungrid1.getSelectionModel().selections.items[0].get(option.grid1.keyid),
				           suunplatformeoperateid:suungrid2.getSelectionModel().getSelected().get(option.grid2.keyid)}
			});
		}
	}
	//导出
	function suunExport(no,exporttype){
	    var suunGridcolumnstr='';
		var cols=suungrid1.colModel.config,
			columns=option.grid1.suuncolumns;
		if (no==2) {
			cols=suungrid2.colModel.config;
			columns=option.grid2.suuncolumns;
		}
		for (i=0;i<columns.length;i++){	        			
			for (j=0;j<cols.length;j++){
				if (cols[j].header==columns[i].columnname){
					if (!cols[j].hidden){
						suunGridcolumnstr=suunGridcolumnstr+columns[i].columnid+','
								+columns[i].columnname+';';
						break;
					}
				}
			}            	
		}	
		var seacher=suungrid1.getSeacher(),
		    url=option.grid1.exporturl;
		if (no==2){
			seacher=suungrid2.getSeacher(),
		    url=option.grid2.exporturl;
		}		
		htmlstr='<input type="text" name="SearchId" value="'+seacher.SearchId+'"/>'+
			'<input type="text" name="query" value="'+seacher.SearchValue+'"/>'+
			'<input type="text" name="zdvalues" value="'+seacher.zdvalues+'"/>'+
			'<input type="text" name="czvalues" value="'+seacher.czvalues+'"/>'+
			'<input type="text" name="dpvalues" value="'+seacher.suvalues+'"/>'+
			'<input type="text" name="gxvalues" value="'+seacher.gxvalues+'"/>'+
			'<input type="text" name="suunGridcolumns" value="'+suunGridcolumnstr.substring(0, suunGridcolumnstr.length-1)+'"/>';
		if (no==1)	{	
			if (suungrid1.store.sortInfo)				
				htmlstr=htmlstr+'<input type="text" name="sort" value="'+suungrid1.store.sortInfo.field+'"/>'+
				'<input type="text" name="dir" value="'+suungrid1.store.sortInfo.direction+'"/>';
		} else{
		    htmlstr=htmlstr+'<input type="text" name="parentid" value="'+suungrid1.getSelectionModel().selections.items[0].get(option.grid1.keyid)+'"/>';
			if (suungrid2.store.sortInfo)				
				htmlstr=htmlstr+'<input type="text" name="sort" value="'+suungrid2.store.sortInfo.field+'"/>'+
				'<input type="text" name="dir" value="'+suungrid2.store.sortInfo.direction+'"/>';				
		}		
		suunCore.GridExportFile({exporturl:url+'?dt='+ new Date().getTime(),
			contextstr:htmlstr,type:exporttype
		});
    }
}
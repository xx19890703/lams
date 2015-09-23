function createsuungridupload(option){	
	//处理默认值
	if (!option.containerid||!option.keyid) {
		alert("containerid||keyid is null")
		return;
	}	
	//if (!suunCore.CheckUrl("grid",option)) return;
	if ("undefined" == typeof option.isprewidth) option.isprewidth=true;
	if ("undefined" == typeof option.simplemode) option.simplemode=false;
	if (!option.pagenum) option.pagenum=20;
	if (!option.inputFormWidth) option.inputFormWidth=200;
	if (!option.inputFormHeight) option.inputFormHeight=150;
	option.operation = option.operation || {};
	option.operation.addfile=option.operation.addfile || {};
	option.operation.add=option.operation.add || {};
	option.operation.edit=option.operation.edit || {};
	option.operation.del=option.operation.del || {};
	option.operation.exp=option.operation.exp || {};
	option.operation.check=option.operation.check || {};
	option.operation.extend=option.operation.extend || [];
	Ext.applyIf(option.operation.add,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
	Ext.applyIf(option.operation.addfile,{hidden:false,iconCls:'add',text:"添加",tooltip:'增加一条信息'});
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
	topbar.push({
		iconCls:"add",
		text:"导入",
        handler:function(){ 
        	addfile(refresh);
        }
   });
	
	
	var bottombar=[];
	if (!option.simplemode && !option.operation.exp.hidden &&
			option.operation.exp.btns.length>0){
			bottombar.push('-');
			for (i=0;i<option.operation.exp.btns.length;i++){
				bottombar.push(option.operation.exp.btns[i]);
			}
	}	
	var suungrid=new suunGridPanel({
		simplemode:option.simplemode,
		fileid:'aaa',
	    listurl:option.baseurl+"!lists",
		pagenum:option.pagenum,
		isprewidth:option.isprewidth,
		suuncolumns:option.suuncolumns,	
		extTopbar:topbar,
		extBottombar:bottombar,
		region : "center",
		storebeforeload:function (store, options){
			Ext.apply(store.baseParams,{type:option.type});
		}
	});
	
	Ext.getCmp(option.containerid).add(suungrid);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
		
	suungrid.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid.getSelectionModel().selectRow(cur-1, true);//执行选中记录
			}
		} 
	});
	return suungrid;
	
	function refresh(){
		suungrid.loadStore();
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
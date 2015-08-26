	function showwin(){
	         // ArrayReader
	         var ds = new Ext.data.Store({
	        	 proxy: new Ext.data.HttpProxy({
	        		 	url:$ctx+'/serviceuser/query!queryContract'
	        	 }),
	             reader: new Ext.data.JsonReader({
	                 root: 'root'
	             }, [
	                 {name: 'did'},
	                 {name: 'name'},
	                 {name: 'orderinfo'},
	                 {name: 'finfo.fname'},
	                 {name: 'conmain.name'}
	             ])
	         });
	         var smm = new Ext.grid.CheckboxSelectionModel();
	 var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),//自动行号
		    smm,
		    {header:'合同编号',dataIndex:'did'},
		    {header:'合同名称',dataIndex:'name'},
		    {header:'所属订单',dataIndex:'orderinfo'},
		    {header:'制造厂',dataIndex:'finfo.fname'},
		    {header:'分类名',dataIndex:'conmain.name'}
		]);
	 
	 var congrid = new Ext.grid.GridPanel({
		    ds: ds,
		    cm: cm,
		    region:"center",
		    width:400,
		    height:300
		});
	 
	 
	 
	 
    	var formm = new Ext.form.FormPanel({
            title:"查询条件",
            height: 120,
            width: 605,
            frame: true,
            region : "north",
            items: [{
            	layout:'column', 
            	labelWidth: 60,
                labelAlign: "right",
            	items:[{
            		layout:'form',
                    columnWidth:.5,  //该列占用的宽度，标识为50％
                    width:160,
                    items: [{
                	   name: "zzcid",
                	   xtype:"textfield",
                	   id:"zzcid",
                	   hidden:true,
                	   fieldLabel: "制造厂id"
                   },{
                	   xtype:"combo",
                	   name: "zzc", 
                	   id:'zzc',
                	   width:150,
                	   fieldLabel: "制造厂",
                	   onTriggerClick:function(){  
                		   	gridselect({listurl:$ctx+'/serviceuser/factoryInfo!lists',//基本url
           					suuncolumns:[{columnid:'fno',columnname:'制造厂编号',colwidth:20,defaultsort:true},
           							 {columnid:'fname',columnname:'制造厂名称',colwidth:40}
           							],
           					callback:function(records){
           						Ext.getCmp('zzcid').setValue(records[0].get('fno'));
           						Ext.getCmp('zzc').setValue(records[0].get('fname'));
           					}
           				});
                		} 
                   },{
                	   name: "htmc",
                	   xtype:"textfield",
                	   id:"htmc",
                	   fieldLabel: "合同名称"
                   }]
                },{
                	layout:'form',
                    columnWidth:1,  //该列占用的宽度，标识为50％
                    items: [{
                	   name: "htbh",
                	   xtype:"textfield",
                	   id:"htbh",
                	   fieldLabel: "合同编号"
                   },{
                	   name: "ssdd",
                	   xtype:"textfield",
                	   id:"ssdd",
                	   fieldLabel: "所属订单"
                   }]
                }]
            }
            ],
            buttons: [{ 
            	text: '查询', 
            	handler:function(){
            		var zzcid=Ext.getCmp("zzcid").getValue()
            		var htmc = Ext.getCmp("htmc").getValue()
            		var htbh = Ext.getCmp("htbh").getValue()
            		var ssdd = Ext.getCmp("ssdd").getValue()
            		ds.load({
            			params:{
            				zzcid:zzcid,
            				htmc:htmc,
            				htbh:htbh,
            				ssdd:ssdd
            			}
            		});
            	}
            }]
         });
    	
    	
    	
    	
    	var htselectPanel = new Ext.Window({    		
        	resizable: false,
            modal: true,
            id:"suunFormWindow",
            autoScroll:true,
        	width: 620,
            height: 570,
            layout:"border",
            closeAction:"close",
            title: '<center style="curor:hand">合同查询</center>',
            items:[congrid,formm],
            buttons: [{
            	id:"BtnSave",
                text: "查 询",
                handler:function(){
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
                	selcontractId=re[0].get('did');
                	
                	var sg = createsuungrids({
                		containerid:'contextPanel-'+$tabtitle,
                		keyid:"authId",//关键字
                		baseurl:$ctx+'/serviceuser/query',//基本url
                		pagenum:5,//页记录数
                		suuncolumns:[new Ext.grid.RowNumberer({header:"序号",width:40}),
                                 {columnid:'name',columnname:'名称',colwidth:80,defaultsort:true},
                				 {columnid:'openType',columnname:'打开方式',colwidth:80},
                				 {columnid:'template.path',columnname:'路径',colwidth:80},
                				 new Ext.grid.Column({
         							header : "管理",
         							dataIndex : "ttId",
         							sortable : false,
         							width : 60,
         							renderer : function(j, i, g, m, h) {
         								var l = g.json.template.path;
         								l=l.replace("\\", "/")
        								var f = g.json.openType;
         								var k = "";
         								if (l != 0) {
         									k += '<button  title="预览" '+
         										'onclick="showReport2(\''+l+'\',\''+f+'\');">预览</button>';									
         								}
         								return k;
         							}
         						})
                				],
                		inputFormWidth:400,
                		inputFormHeight:350,
                		operation:{check:{hidden:false}}
                	});  
                	
                	htselectPanel.close();
                }
            }, {
                text: "取 消",handler:function(){
                }
            }]
        }); 
    	htselectPanel.show();
    	}
    	
Ext.onReady(function(){
	showwin();
})
function showReport2(url,type){
        	new Ext.Window({
    			title : '报表展现',
    			width : 800,
    			height : 600,
    			layout : 'fit',
    			plain : true,
    			closeAction : 'close',
    			modal: true,
    			bodyStyle : 'background-color:white;padding:0px;',
    			buttonAlign : 'center',
    			html: '<iframe style="background-color:white;overflow:auto;width:100%; height:100%;" src="'+$ctx+'/ReportServer?reportlet='+url+'&op='+type+'" frameborder="0"></iframe>',
    		}).show(); 
        }
 var selcontractId ="";
function createsuungrids(option){	
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
	option.operation.extend=option.operation.extend || [];
	Ext.applyIf(option.operation.exp,{hidden:false,btns:[]});
	Ext.applyIf(option.operation.add,{hidden:false,text:"查询",tooltip:'选择查询条件'});
	Ext.applyIf(option.operation.exp.btns,[
	    {iconCls:'pdf',handler:function(btn,pressed){suunExport('pdf');}}, 
	    {iconCls:'excel',handler:function(btn,pressed){suunExport('xls');}},
	    {iconCls:'word',handler:function(btn,pressed){suunExport('doc');}},
	    {iconCls:'xml',handler:function(btn,pressed){suunExport('xml');}},
	    {iconCls:'csv',handler:function(btn,pressed){suunExport('csv');}} 	
    ]);	                                             
	Ext.applyIf(option.operation.extend,[]);
	if (option.formType)
		ftype='ENCTYPE="multipart/form-data"';	
	//为权限预留
	//var suunauths=suunCore.GetAuths(option);
	var topbar=[];
//	
//	if (suunCore.HaveAuths(option.authurl,option.operation.add.auth)) {
//		if (!option.operation.add.hidden){
//			topbar.push({
//				iconCls:option.operation.add.iconCls,
//				text:option.operation.add.text,
//				tooltip:option.operation.add.tooltip,
//	            handler:function(){ 
//	                if (!option.operation.add.onClick) {
//	                	add();
//	                } else {
//	                	option.operation.add.onClick(suungrid);
//	                }
//	            }
//	       });
//		}	
//	}
	var bottombar=[];
//	if (!option.simplemode && !option.operation.exp.hidden &&
//			option.operation.exp.btns.length>0){
//		if (suunCore.HaveAuths(option.authurl,option.operation.exp.auth)) {	
//			bottombar.push('-');
//			for (i=0;i<option.operation.exp.btns.length;i++){
//				bottombar.push(option.operation.exp.btns[i]);
//			}
//		}
//	}	
	var suungrid2=new suunGridPanel({
		simplemode:option.simplemode,
	    listurl:option.listurl,
		pagenum:option.pagenum,
		isprewidth:option.isprewidth,
		suuncolumns:option.suuncolumns,	
		extTopbar:topbar,
		extBottombar:bottombar,
		region : "center",
		storebeforeload:function (store, options){
			Ext.apply(store.baseParams,{contractId:selcontractId});
		}
	});
	
	Ext.getCmp(option.containerid).add(suungrid2);
	Ext.getCmp(option.containerid).setLayout(new Ext.layout.BorderLayout());
    Ext.getCmp(option.containerid).doLayout(true);
		
	suungrid2.addListener("rowdblclick", viewrecord);
	suungrid2.store.on('load', function () {
		if (this.reader.jsonData){			
			var cur=this.reader.jsonData.curpagepos;
			if(cur>0) {
				suungrid.getSelectionModel().selectRow(cur-1, true);//执行选中记录
			}
		} 
	});
	return suungrid2;
}
//添加
function add(){    
	showwin();
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
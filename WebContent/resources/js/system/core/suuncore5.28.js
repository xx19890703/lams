if (Ext.isIE) {
	Ext.enableGarbageCollector = false;
	//解决IE不支持String.trim
	String.prototype.trim = function () {
		var str = this, str = str.replace(/^\s\s*/, ''), ws = /\s/, i = str.length;
		while (ws.test(str.charAt(--i))) ;
		return str.slice(0, i-1);
	} 
	//解决IE不支持Array.indexOf
	if(!Array.indexOf){
		Array.prototype.indexOf = function(obj){
			for(var i=0; i<this.length; i++){
				if(this[i]==obj){
					return i;
				}
			}
			return -1;
		}
	}
}
//jquery 1.9以后不再支持$.browser
var matched, browser;
function uaMatch( ua ) {
	ua = ua.toLowerCase();
	var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
		/(webkit)[ \/]([\w.]+)/.exec( ua ) ||
		/(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
		/(msie) ([\w.]+)/.exec( ua ) ||
		ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
		[];

	return {
		browser: match[ 1 ] || "",
		version: match[ 2 ] || "0"
	};
};
matched = uaMatch( navigator.userAgent );
browser = {};
if ( matched.browser ) {
	browser[ matched.browser ] = true;
	browser.version = matched.version;
}
// Chrome is Webkit, but Webkit is also Safari.
if ( browser.chrome ) {
	browser.webkit = true;
} else if ( browser.webkit ) {
	browser.safari = true;
}
if(!browser.msie) {
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
		browser.msie11 = true;
	}
}

$.browser = browser;
//jquery 1.9以后不再支持$.browser end
//全局键盘事件,回车时保存
$(document).keyup(function(event){
	if (document.getElementById("suunFormWindow")) {
		if (document.getElementById("suunFormWindow").hidden==false){
			if(event.keyCode == 13){ 
				if (document.getElementById("BtnSave")){
					document.getElementById("BtnSave").click();
				}
			}
		}
	}	
})

Ext.QuickTips.init();

//初始化系统
Ext.BLANK_IMAGE_URL = $ctx+"/resources/images/extjs/s.gif";
//全局超时、404等处理
//Ext Ajax
Ext.util.Observable.observeClass(Ext.data.Connection);
Ext.data.Connection.on("requestcomplete", function(g, request, f) {
    suunCore.requestComplete(request,f.url);	
});
Ext.data.Connection.on("requestexception", function(g, request, f) {
    suunCore.requestComplete(request,f.url);	
});
//jquery Ajax
/*$.ajaxSetup({   
	contentType:"application/x-www-form-urlencoded;charset=utf-8",   
	complete:function(request,textStatus){   
		suunCore.requestComplete(request,this.url);
    }	
});*/
$(document).ajaxComplete(function(event,request, settings){   
	suunCore.requestComplete(request,this.url);	
});
//初始化系统

Ext.ux.Toast = function() {
    var msgCt;

    function createBox(t, s){
        return ['<div class="">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
		/**
		 * Shows popup
		 * @param {String} title
		 * @param {String} format
		 */
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div',style:'position:absolute;z-index:10000'}, true);
            }
            var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
            msgCt.alignTo(document, 't-t');
            m.slideIn('t').pause(2.5).ghost("t", {remove:true});
        }
	}

}();

Ext.ux.TabCloseMenu = Ext.extend(Object, {
    closeTabText: '关闭',
    closeOtherTabsText: '关闭其他',
    showCloseAll: true,
    closeAllTabsText: '关闭所有',    
    constructor : function(config){
        Ext.apply(this, config || {});
    },
    //public
    init : function(tabs){
        this.tabs = tabs;
        tabs.on({
            scope: this,
            contextmenu: this.onContextMenu,
            destroy: this.destroy
        });
    },    
    destroy : function(){
        Ext.destroy(this.menu);
        delete this.menu;
        delete this.tabs;
        delete this.active;    
    },
    // private
    onContextMenu : function(tabs, item, e){
        this.active = item;
        var m = this.createMenu(),
            disableAll = true,
            disableOthers = true,
            closeAll = m.getComponent('closeall');
        
        m.getComponent('close').setDisabled(!item.closable);
        tabs.items.each(function(){
            if(this.closable){
                disableAll = false;
                if(this != item){
                    disableOthers = false;
                    return false;
                }
            }
        });
        m.getComponent('closeothers').setDisabled(disableOthers);
        if(closeAll){
            closeAll.setDisabled(disableAll);
        }
        
        e.stopEvent();
        m.showAt(e.getPoint());
    },    
    createMenu : function(){
        if(!this.menu){
            var items = [{
                itemId: 'close',
                iconCls:'btn-close',
                text: this.closeTabText,
                scope: this,
                handler: this.onClose
            }];
            if(this.showCloseAll){
                items.push('-');
            }
            items.push({
                itemId: 'closeothers',
                iconCls:'btn-close',
                text: this.closeOtherTabsText,
                scope: this,
                handler: this.onCloseOthers
            });
            if(this.showCloseAll){
                items.push({
                    itemId: 'closeall',
                    iconCls:'btn-close',
                    text: this.closeAllTabsText,
                    scope: this,
                    handler: this.onCloseAll
                });
            }
            this.menu = new Ext.menu.Menu({
                items: items
            });
        }
        return this.menu;
    },    
    onClose : function(){
        this.tabs.remove(this.active);
    },    
    onCloseOthers : function(){
        this.doClose(true);
    },    
    onCloseAll : function(){
        this.doClose(false);
    },    
    doClose : function(excludeActive){
        var items = [];
        this.tabs.items.each(function(item){
            if(item.closable){
                if(!excludeActive || item != this.active){
                    items.push(item);
                }    
            }
        }, this);
        Ext.each(items, function(item){
            this.tabs.remove(item);
        }, this);
    }
});
Ext.preg('tabclosemenu', Ext.ux.TabCloseMenu);

suunGridPanel = Ext.extend(Ext.grid.GridPanel,{
	suunid:'',
	listurl:'',
	pagenum:20,
	isprewidth:true,
	suuncolumns:[],	
	extTopbar:[],
	extBottombar:[],
	storeAutoLoad:true,
	checkboxSel:true,
	bbarInfo:true,
	simplemode:false,
	zdstr:"",czstr:"",sustr:"",gxstr:"",	
	constructor : function(cfg) {
		this.suuncolumns=[];this.extTopbar=[];this.extBottombar=[];
		this.zdstr="";this.czstr="";this.sustr="";this.gxstr="";
		Ext.apply(this, cfg || {});
		if ("undefined" == typeof this.checkboxSel) this.checkboxSel=true;
		if ("undefined" == typeof this.isprewidth) this.isprewidth=true;
		if ("undefined" == typeof this.pagenum) this.pagenum=20;
		if ("undefined" == typeof this.bbarInfo) this.bbarInfo=true;
		if ("undefined" == typeof this.simplemode) this.simplemode=false;
		if ((this.listurl=='')||(!this.listurl)) {
			alert("listurl is null");
			return;
		}
		if ((this.suunid=='')||(!this.suunid)) this.suunid=Ext.id();
		if ("undefined" == typeof this.title) this.title=''; 
		var iniobj=this.initUIComponents();	
        suunGridPanel.superclass.constructor.call(this, {
        	header:this.title!='',
			title:this.title,
			id:'suunextgrid-'+this.suunid,
			tbar : iniobj.topbar,
			iconCls : 'icon-grid',
			store : iniobj.suunGridStore,
			selModel  : iniobj.sm,		
			columnLines : true,
			loadMask : true,
			viewConfig : {
				forceFit : this.isprewidth,
				enableRowBody : false,
				showPreview : false
			},
			columns :iniobj.gridcolumns,
			stripeRows : true,//斑马线效果 
			bbar: new Ext.PagingToolbar({
				pageSize: this.pagenum,
				store: iniobj.suunGridStore,
				displayInfo: this.bbarInfo,
				items:this.extBottombar,
				listeners:{
					'change':function(){
						if (this.store.reader.jsonData){
							if (this.store.reader.jsonData.page<=0){
							    this.afterTextItem.setText(String.format(this.afterPageText, 0));
								this.inputItem.setValue(0);
							    this.store.data.length=0;
								this.updateInfo();
							}
						}else{
							this.afterTextItem.setText(String.format(this.afterPageText, 0));
							this.inputItem.setValue(0);
							this.store.data.length=0;
							this.updateInfo();
						}							
					}
				}			
			})
		});
		this.on('render', function(grid) {
			grid.tip = new Ext.ToolTip({
				target : grid.getView().mainBody,
				delegate : '.x-grid3-cell',
				trackMouse : true,
				maxWidth : 900,
				dismissDelay : 30000,
				layout : "fit",
				bodyStyle : 'font:bold 12px tahoma,arial,sans-serif;',
				renderTo:document.body,
				listeners : {
					'beforeshow' : function (tip) {
						if (this.tipShow){
					        this.tipShow(tip);     
					    } else{
						    if (tip.triggerElement.textContent){
								textContent=tip.triggerElement.textContent;
							} else{
								textContent=tip.triggerElement.innerText;
							}
							if (textContent.trim() != "") {
								tip.update(textContent);
							} else {
								return false;
							}
						}						 
					}
				}
			});
		});
		if (this.storeAutoLoad)
		    this.loadStore();
	},
	initUIComponents : function() {
	    //var 定义的是局部变量， 不带是全局变量		
	    var sm=null,
			SeachemptyText=null,SeachemptyId=null,
			datafields=[],searchdata=[],moresearchdata=[],sortdata={},
		    suunGridcolumns=[],gridcolumns=[];
		if (this.checkboxSel)
            sm=new Ext.grid.CheckboxSelectionModel();
		else
			sm=new Ext.grid.RowSelectionModel()
		for (i=0;i<this.suuncolumns.length;i++){
			var suuncolumn=this.suuncolumns[i];
			if ((suuncolumn instanceof Ext.grid.RowNumberer)||(suuncolumn instanceof Ext.grid.Column)){
				
			} else{
				if (!suuncolumn.columnid||!suuncolumn.columnname){
					alert("columnid||columnname is null")
				  return;
				}	
				if ("undefined" == typeof suuncolumn.colwidth){
					suuncolumn.colwidth=50;
				}	
				if ("undefined" == typeof suuncolumn.issort){
					suuncolumn.issort=true;
				}
				if ("undefined" == typeof suuncolumn.issearch){
					suuncolumn.issearch=true;
				}
				if ("undefined" == typeof suuncolumn.isASC){
					suuncolumn.isASC=true;
				}
				if ("undefined" == typeof suuncolumn.hidden){
					suuncolumn.hidden=false;
				}
				if ("undefined" == typeof suuncolumn.defaultsort){
					suuncolumn.defaultsort=false;
				}
			}			
			suunGridcolumns.push(suuncolumn);
		}
		//处理默认值
		if (this.checkboxSel)
			gridcolumns.push(sm); 
		for (i=0;i<suunGridcolumns.length;i++){
            if ((suunGridcolumns[i] instanceof Ext.grid.RowNumberer)||(suunGridcolumns[i] instanceof Ext.grid.Column)){
            	gridcolumns.push(suunGridcolumns[i]);
			} else{
				datafields.push(suunGridcolumns[i].columnid);
				gridcolumns.push({header:suunGridcolumns[i].columnname,width:suunGridcolumns[i].colwidth,
						dataIndex:suunGridcolumns[i].columnid,sortable:suunGridcolumns[i].issort,
						hidden:suunGridcolumns[i].hidden});
				if (suunGridcolumns[i].issearch){
					if (suunGridcolumns[i].type){
						if (suunGridcolumns[i].type=="C"){
							if (!suunGridcolumns[i].cdata){
								suunGridcolumns[i].cdata=[[""]];
							}
							searchdata.push([suunGridcolumns[i].columnid, suunGridcolumns[i].columnname,suunGridcolumns[i].type,suunGridcolumns[i].cdata]);
							moresearchdata.push({"MoreSearchId" : suunGridcolumns[i].columnid,"MoreSearchName" : suunGridcolumns[i].columnname,"MoreSearchType" :suunGridcolumns[i].type,"MoreSearchCdata" :suunGridcolumns[i].cdata});
						} else {
							searchdata.push([suunGridcolumns[i].columnid, suunGridcolumns[i].columnname,suunGridcolumns[i].type]);
							moresearchdata.push({"MoreSearchId" : suunGridcolumns[i].columnid,"MoreSearchName" : suunGridcolumns[i].columnname,"MoreSearchType" :suunGridcolumns[i].type});
						}						
					} else{
						searchdata.push([suunGridcolumns[i].columnid, suunGridcolumns[i].columnname,'S']);
					    moresearchdata.push({"MoreSearchId" : suunGridcolumns[i].columnid,"MoreSearchName" : suunGridcolumns[i].columnname,"MoreSearchType" :'S'});
					}					
				}
				if (suunGridcolumns[i].defaultsort){
					sortdata={field:suunGridcolumns[i].columnid,direction:suunGridcolumns[i].isASC?'ASC':'DESC'};
				}
			}			
		}
		if (searchdata.length>0){
			SeachemptyText=searchdata[0][1];
			SeachemptyId=searchdata[0][0];
		}
		//创建数据源
		var suunGridStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				type : 'ajax',
				url : this.listurl,
				actionMethods: { read: 'POST' },
			  simpleSortMode : true
			}),
			reader : new Ext.data.JsonReader({
				root : "data",
				totalProperty : "total",
				id : "id",
				fields : datafields
			}),
			sortInfo : sortdata,
			remoteSort : true,
			autoLoad : false,
			listeners:{
				'beforeload': function (store, options) {
					var seacher=this.scope.getSeacher();
					Ext.apply(store.baseParams, {limit:this.scope.pagenum,start:(store.currentPage-1)*this.scope.pagenum,
						        SearchId:seacher.SearchId,
								query:seacher.SearchValue,
								zdvalues : seacher.zdvalues,
								czvalues : seacher.czvalues,
								dpvalues : seacher.suvalues,
								gxvalues : seacher.gxvalues
						}
					);
					if (this.scope.storebeforeload){
					    this.scope.storebeforeload(store, options);     
					}
			    }
			},
			scope : this						
		});	
	  
		var topbar=new Ext.Toolbar({
			items : [{
						text : "查询：",
						hidden :this.simplemode
					},/*new Ext.form.ComboBox(*/{
						xtype : "combo",
					    id : "Q_SearchId-"+this.suunid,
						hidden :this.simplemode,
						width : 140,
						mode: 'local',
						typeAhead: false,
						displayText: SeachemptyText,
						value:SeachemptyId,
						editable: false,
						forceSelection: true,
						triggerAction: 'all',
						//selectOnFocus:true,
						displayField: 'SearchName',
						valueField: 'SearchId',
						store : new Ext.data.ArrayStore({
						  fields: ['SearchId', 'SearchName','stype','cdata'],
						  data : searchdata
						}),
						listeners : {
							"select" : function (){
								shows(this.scope,this.selectedIndex);								
							}
						},
						scope : this
					}/*)*/,
					{
						xtype : "textfield",
						id : "Q_svalue-"+this.suunid,
						hidden :this.simplemode,
						width : 148,
						name : "Q_value"
					},{
						xtype : "combo",
     					editable : false,
    					allowBlank : true,
    					id : "Q_cvalue-"+this.suunid,
    					hidden :this.simplemode,
                        forceSelection : true,
     					blankText : '请选择...', 
    					emptyText : '请选择...', 			
    					triggerAction : "all",
    					width : 148,
     					displayField : "codeName",
     					mode:'local',
     					store : new Ext.data.ArrayStore({
     						 fields:['codeName']
						})			
		    		},
					{
						xtype : "numberfield",
						id : "Q_nvalue-"+this.suunid,
						hidden :this.simplemode,
						width : 148,
						name : "Q_value"
					},
					{
						xtype : "datefield",
						id : "Q_dvalue-"+this.suunid,
						hidden :this.simplemode,
						width : 148,
						format:'Y-m-d',
						editable:false,
						name : "Q_value"
					},
					{
						xtype : "button",
						id:'search-'+this.suunid,
						hidden :this.simplemode,
						//text : "查询",
						tooltip : '简单查询', 			
						iconCls : "search",
						handler : function (){
							var seacher=this.getSeacher();
							if (seacher.SearchValue!=""){
								this.searchClear(true);//清除复杂查询
								Ext.getCmp('clearSelect-'+this.suunid).show();
								Ext.getCmp('search-'+this.suunid).setIconClass("searchc");
								this.loadStore();
							}
						},
						scope : this
					}, {
						xtype : "button",
						id:'moreSearch-'+this.suunid,
						hidden :this.simplemode,
						iconCls : "searchm",
						//text : '查询...',
						tooltip : '详细查询',
						handler : function (){
							suunCore.GridSearchMore({searchdata:moresearchdata,z:this.zdstr,c:this.czstr,s:this.sustr,g:this.gxstr,
								callback:function(z,c,s,g,scope){
									if (z=="") {scope.searchClear(true);return;}
									scope.zdstr = z;scope.czstr =c;scope.sustr = s;scope.gxstr = g;
									scope.loadStore();
									scope.searchClear();//清除简单查询
									Ext.getCmp('moreSearch-'+scope.suunid).setIconClass("searchmc");
									if (!(scope.zdstr == "" && scope.czstr == "" && scope.sustr == "" && scope.gxstr == "")) {
										Ext.getCmp('clearSelect-'+scope.suunid).show();
									}
								},scope : this
							});
						},
						scope : this
					}, {
						xtype : "button",
						id:'clearSelect-'+this.suunid,
						hidden : true,
						//text : "清空",
						tooltip : '清空查询条件',
						iconCls : "reset",
						handler : function (){
							this.searchClear();  
							this.searchClear(true); 
							this.loadStore();								
							Ext.getCmp('clearSelect-'+this.suunid).hide();
						},
						scope : this
			}].concat(this.extTopbar)
		});	
		function shows(scope,si){
			seachering=!Ext.getCmp('clearSelect-'+scope.suunid).hidden;
			if (seachering)
			    scope.searchClear(false);
			Ext.getCmp('Q_svalue-'+scope.suunid).hide();
			Ext.getCmp('Q_nvalue-'+scope.suunid).hide();
			Ext.getCmp('Q_dvalue-'+scope.suunid).hide();
			Ext.getCmp('Q_cvalue-'+scope.suunid).hide();
			var data=Ext.getCmp('Q_SearchId-'+scope.suunid).getStore().getAt(si).data;
			var stype=data.stype;
			if (stype=='N'){
			    Ext.getCmp('Q_nvalue-'+scope.suunid).show();
			} else if (stype=='D'){
				Ext.getCmp('Q_dvalue-'+scope.suunid).show();
			} else if (stype=='C'){
				Ext.getCmp('Q_cvalue-'+scope.suunid).store.loadData(data.cdata,false);
				Ext.getCmp('Q_cvalue-'+scope.suunid).show();
			} else{
				Ext.getCmp('Q_svalue-'+scope.suunid).show();
			}
			if (seachering)
			    scope.store.reload();
		};
		shows(this,0);
		return {
		    sm:sm,
			topbar:topbar,
			suunGridStore:suunGridStore,
			gridcolumns:gridcolumns
		}
	},	
	searchClear:function(isMore){
	    if (!isMore){
	    	Ext.getCmp('Q_svalue-'+this.suunid).reset();
			Ext.getCmp('Q_nvalue-'+this.suunid).reset();
			Ext.getCmp('Q_dvalue-'+this.suunid).reset();
			Ext.getCmp('Q_cvalue-'+this.suunid).reset();
	        Ext.getCmp('search-'+this.suunid).setIconClass("search");
	    }else{   
		  this.zdstr = "",this.czstr = "",this.sustr = "",this.gxstr = "";
		  Ext.getCmp('moreSearch-'+this.suunid).setIconClass("searchm"); 
		}
		if (this.zdstr == ""&&Ext.getCmp('Q_svalue-'+this.suunid).getValue()=="")		
		  Ext.getCmp('clearSelect-'+this.suunid).hide();		
	},
	getSeacher:function(){
		SeachemptyId='';
	    if (Ext.getCmp('Q_SearchId-'+this.suunid).store.data.length>0){
			SeachemptyId=Ext.getCmp('Q_SearchId-'+this.suunid).store.data.items[0].data['SearchId'];
		} 
	    svalue=Ext.getCmp('Q_svalue-'+this.suunid).getValue();
	    if (!Ext.getCmp('Q_nvalue-'+this.suunid).hidden){
	    	svalue=Ext.getCmp('Q_nvalue-'+this.suunid).getValue();
	    } else if (!Ext.getCmp('Q_dvalue-'+this.suunid).hidden){
	    	svalue=Ext.getCmp('Q_dvalue-'+this.suunid).getRawValue();
	    } else if (!Ext.getCmp('Q_cvalue-'+this.suunid).hidden){
	    	svalue=Ext.getCmp('Q_cvalue-'+this.suunid).getValue();
	    }
	    return {
		    SearchId:Ext.getCmp('Q_SearchId-'+this.suunid).getValue()==null? 
			                   SeachemptyId:Ext.getCmp('Q_SearchId-'+this.suunid).getValue(),
			SearchValue:svalue,
		    zdvalues : this.zdstr,
		    czvalues : this.czstr,
		    suvalues : this.sustr,
		    gxvalues : this.gxstr
		}
	},
	loadStore:function(){
	    this.store.load({params:{start:0,limit:this.pagenum}});
	},
	getSuunid:function(){
	    return this.suunid;
	}
});

suunTreePanel = Ext.extend(Ext.tree.TreePanel,{
	suunid:'',
	listurl:'',	
	extTopbar:[],
	allowCheckbox:false,
	constructor : function(cfg) {
	    this.extTopbar=[];
		Ext.apply(this, cfg || {});
		if ((this.listurl=='')||(!this.listurl)) {
			alert("listurl is null");
			return;
		}
		if ((this.suunid=='')||(!this.suunid)) this.suunid=Ext.id();
		this.extTopbar.push({
			pressed : false,
			text : '刷新',
			tooltip : '刷新数据',
			iconCls : 'refresh',
			scope : this,
			handler : function (){
				this.root.reload();	
			}
		});
		if ("undefined" == typeof this.title) this.title=''; 
		if ("undefined" == typeof this.autoLoadRoot) this.autoLoadRoot=true;
		if (this.title==''){
			if (this.split){
				this.extTopbar.push('->');
				this.extTopbar.push({
					id:'treecollapsebtn-'+this.suunid,
					iconCls : 'collapse',
					handler : this.collapse,
					scope : this
				});
			}
		}		
		suunTreePanel.superclass.constructor.call(this, {
			id:'suuntree-'+this.suunid,
			viewConfig : {
				loadingText : "正在加载..."
			},
			header:this.title!='',
			title:this.title,
			tbar:this.extTopbar,
			border : true,     
			enableDD : false,     
			enableDrag : false,     
			rootVisible : false,     
			trackMouseOver : true,         
			lines : true,     
			singleExpand : false, 
			collapsible : true, // 是否可以收缩 
			autoScroll : true,
			containerScroll : true,
			root : new Ext.tree.AsyncTreeNode({     
				id : '0',    
				text : '根节点',   
				iconCls:'ico-root',  
				expanded:true,  
				leaf : false,     
				border : false,     
				draggable : false,     
				singleClickExpand : false,     
				hide : true    
			}),   
			loader : new Ext.tree.TreeLoader({     
				nodeParameter:'groupId',  
				requestMethod:'POST'
			})//不能使用listeners，否则new suunTreePanel的listeners会不起作用
		});
		if (this.autoLoadRoot)
		    this.getLoader().dataUrl=this.listurl;
		this.on('append',function (ownerTree,pnode, node, index) {
			if (!this.allowCheckbox)
				node.attributes.checked=null;
		});
		this.on('collapse',function () {
			if (this.title==''){
				if (this.split){
					Ext.getCmp('treecollapsebtn-'+this.suunid).hide();
				}
			}
		});
		this.on('expand',function (ownerTree,pnode, node, index) {
			if (this.title==''){
				if (this.split){
					Ext.getCmp('treecollapsebtn-'+this.suunid).show();
				}
			}
		});
	},
	getSuunid:function(){
	    return this.suunid;
	}
});

var suunCore = {    
/*	GetHttpRequest:function (){
		if ( window.XMLHttpRequest ) // Gecko
			return new XMLHttpRequest() ;
		else if ( window.ActiveXObject ) // IE
			return new ActiveXObject("MsXml2.XmlHttp") ;
	},
	AjaxPage: function (type,url){
		var oXmlHttp = new XMLHttpRequest();
		oXmlHttp.OnReadyStateChange = function(){
			if ( oXmlHttp.readyState == 4 ){
				if ( oXmlHttp.status == 200 || oXmlHttp.status == 304 ){
					// 解决IE内存泄露
					oXmlHttp.onreadystatechange = null;
					oXmlHttp=null;
					this.IncludeJsCss( type, url, oXmlHttp.responseText );
				} else {
					alert( 'XML request error: ' + oXmlHttp.statusText + ' (' + oXmlHttp.status + ')' ) ;
				}
			}
		}
		oXmlHttp.open('GET', url, true);
		oXmlHttp.send(null);
	},			
*/	UIJSPATH :$ctx+"/resources/js/system/core/ui/images",
    userAuths:'',
	requestComplete:function(request,url){
		if (request) {
		    if(request.getResponseHeader){
				if(request.getResponseHeader("sessionstatus")=="__timeout"){
					Ext.ux.Toast.msg("操作提示：", "操作已经超时，将重新登录!");
					window.location.href=window.location.href;
				} else if (request.status==403) {
					/*Ext.MessageBox.show({
						title : "403错误",
						msg : "你目前没有权限访问："+url,
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});*/
					Ext.ux.Toast.msg("系统访问权限提示：", "你目前没有权限访问：{0}",url);
				} else if (request.status==500) {
					/*Ext.MessageBox.show({
						title : "500错误",
						msg : "您访问的URL:"+url+"出错了，具体原因请联系管理员。",
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});*/
					Ext.ux.Toast.msg("后台出错", "您访问的URL:{0}出错了，具体原因请联系管理员。",url);
				} else if (request.status==404) {
					/*Ext.MessageBox.show({
						title : "404错误",
						msg : "您访问的URL:"+url+"对应的页面不存在，具体原因请联系管理员。",
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});*/
					Ext.ux.Toast.msg("后台出错","您访问的URL:{0}对应的页面不存在，具体原因请联系管理员。", url);
				} else if (request.status==0) {//jquery
					Ext.ux.Toast.msg("后台出错", "你访问的URL：{0}没有响应！",url);
				} 
			} else{
				if (request.status==0) {
					Ext.ux.Toast.msg("后台出错", "你访问的URL：{0}没有响应！",url);
				} 
			}
		}
	},
    bodyMask:null,
    createMask:function(){
	    if (suunCore.bodyMask==null)
			suunCore.bodyMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在载入，请等待..."});	
	},
	showMask:function(){
	    if (suunCore.bodyMask==null)
			suunCore.bodyMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在载入，请等待..."});
	    suunCore.bodyMask.show();	
	},
	hideMask:function(){
	    if (suunCore.bodyMask)
			suunCore.bodyMask.hide();	
	},
    csses:[],scripts:[],
	escape2Html:function(str) {
        var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
        return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
    },
	//执行脚本		
	setHtml:function (htmlDiv, html, execScript,callback){
	    if ("undefined" == typeof execScript)
		    execScript=true;
		html = html || '';
		var rejs = /(?:<script([^>]*)?>)((\n|\r|.)*?)(?:<\/script>)/ig, 
		    srcRe = /\ssrc=([\'\"])(.*?)\1/i, 
			typeRe = /\stype=([\'\"])(.*?)\1/i, match, attrs, srcMatch, typeMatch, el, s,
		    myid=Ext.id(),  
            newcssjses=[], 
            cssjsLoading=true,			
			hd=document.getElementsByTagName("head")[0];
			
			html += '<span id="' + myid  + '"></span>';
			htmlDiv.innerHTML = html.replace(/(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/ig,'');			
		if (execScript){		 
		    //css加载
			var recss = /(?:<link((?!type).)*type=["']?text\/css["']?[^>]*[\/]?>)/ig, // /(?:<link.*?>)((\n|\r|.)*?)/ig, 
				hrefRecss = /\shref=([\'\"])(.*?)\1/i;
			while ((match = recss.exec(html))) {
				attrs = match[0];
				srcMatch = attrs ? attrs.match(hrefRecss) : false;
				if (srcMatch && srcMatch[2]) {
				    srccss=suunCore.escape2Html(srcMatch[2]);
					newcss=suunCore.csses.indexOf(srccss)<0;
					newcss=newcss&&hd.innerHTML.indexOf(srccss)<0;
					if (newcss){
					   suunCore.csses.push(srccss);
					   newcssjses.push({type:"link",file:srccss});
					}					   
				}
			}				
			//js加载
			while ((match = rejs.exec(html))) {
				attrs = match[1];
				srcMatch = attrs ? attrs.match(srcRe) : false;
				if (srcMatch && srcMatch[2]) {
				    srcjs=suunCore.escape2Html(srcMatch[2]);
					newjs=suunCore.scripts.indexOf(srcjs)<0;
					newjs=newjs&&hd.innerHTML.indexOf(srcjs)<0;
					if (newjs){
					   suunCore.scripts.push(srcjs);
					   newcssjses.push({type:"script",file:srcjs});
					}
				}
			}
			function includecssjs(cssjses, callback) {
				if  (cssjses.length>0) {					   
					var elem;
					if (cssjses[0].type=="link"){
						elem = document.createElement("link");
						elem.setAttribute("rel", "stylesheet");
						elem.setAttribute("type", "text/css");
						elem.setAttribute("href", cssjses[0].file);							 
					}else{
						elem = document.createElement("script");
						elem.setAttribute("type", "text/javascript");
						//elem.setAttribute("async", "async");
						elem.setAttribute("src", cssjses[0].file);
					}
					hd.appendChild(elem);
					// 文件还没有处理过
					if (typeof document.getElementsByTagName(cssjses[0].type)[cssjses[0].file] === "undefined") {
						// 使onload取得正确elem和index
						(function(e) {
							e.onload = e.onreadystatechange = function() {
								if (! e.readyState || /loaded|complete/.test(e.readyState)) {
									// 解决IE内存泄露
									e.onload = e.onreadystatechange = null;
									// 释放引用
									e= null;
									cssjses.shift();
									includecssjs(cssjses,callback);
									if  (cssjses.length==0)
										callback && callback();
								}
							};
						})(elem);
					}					
				};
			};
			function htmlexecScript(signid){
				interval = setInterval(function() {
					if (!document.getElementById(myid)) {
						return false;
					}
					clearInterval(interval);
					while ((match = rejs.exec(html))) {
						attrs = match[1];
						srcMatch = attrs ? attrs.match(srcRe) : false;
						if (srcMatch && srcMatch[2]) {
						} else if (match[2] && match[2].length > 0) {
							try{
								if (window.execScript) {
									window.execScript(match[2]);
								} else {
									window.eval(match[2]);
								}
							}catch(e){
								var msg="";
								if (e.fileName){//firefox
									if (e.fileName.indexOf("suuncore.js")!=-1){
										var line=e.lineNumber-3;
										if (line<0) line=0;
										var srcs=match[2].split("\n");
										for (i=line;i<srcs.length;i++) {
											if (i==e.lineNumber+2) break;
											msg=msg+(i+1)+" "+srcs[i]+"\n";    
										}
										msg=e.message+' line:'+e.lineNumber+' column:'+e.columnNumber+"\n"+msg;
									}else{
										msg=e.message+' line:'+e.lineNumber+' column:'+e.columnNumber+"\nfileName:"+e.fileName;
									}	
								} else if (e.stack){//google
									msg=e.message+"\n"+e.stack;
								} else{
									 msg=e;
									 if (e.message) msg=e.message;
								}							  
							   alert(msg);
							}
						}
					}
					el = document.getElementById(myid);
					if (el) {
						Ext.removeNode(el);
					}
					if (callback) {callback(htmlDiv);}
				}, 10);
			};
			if  (newcssjses.length>0) {			
				includecssjs(newcssjses,function(){
				    htmlexecScript(myid); 
				});
			} else{
				htmlexecScript(myid); 
			}			
		}
		return;
	},
	setExtComHtml:function (extCom, html, execScript, callback){
		suunCore.setHtml(extCom.body.dom, html, execScript, callback);
	},
	loadurl:function(htmldiv,dourl,postparams,callback){
	    Ext.Ajax.request({
    	    url: dourl,
    	    params: postparams,
    	    success: function(msg){
    	    	var ohtml=$(msg.responseText);
  		        var newhtml="";
  		        for (x = 0; x < ohtml.length; x++){
  		          if (ohtml[x].outerHTML){
      		        newhtml=newhtml+ohtml[x].outerHTML;
  		          }
  		        }
                suunCore.setHtml(htmldiv,newhtml,true,callback);       	    	
    	    },
    	    failure: function(){
			    suunCore.hideMask();
    	    }
    	});
	},
	loadurlReadonly:function(htmldiv,dourl,postparams,callback){
	    Ext.Ajax.request({
    	    url: dourl,
    	    params: postparams,
    	    success: function(msg){
			    html=msg.responseText;
			    //html=html.replace(new RegExp('<input ','ig'),'<input disabled="disabled" ');
				//html=html.replace(new RegExp('<file ','ig'),'<file disabled="disabled" ');
				html=html.replace(new RegExp('<select ','ig'),'<select disabled="disabled" ');
				//html=html.replace(new RegExp('<textarea ','ig'),'<textarea disabled="disabled" ');
				//html=html.replace(new RegExp('<button ','ig'),'<button disabled="disabled" ');
				//html=html.replace(new RegExp('onclick','ig'),'readonlyonclick');	
            	html=html.replace(new RegExp('suunDateTime','ig'),'suunDateTimeReadOnly');			
    	    	var ohtml=$(html);
  		        var newhtml="";
  		        for (x = 0; x < ohtml.length; x++){
  		            if (ohtml[x].outerHTML){
					    //$(ohtml[x]).find(".dropselectbox").removeClass("dropselectbox");
  		            	/*:input 
						<input type="button" value="Input Button"/>,<input type="checkbox" />,<input type="file" />,
						<input type="hidden" />,<input type="image" />,<input type="password" />,<input type="radio" />,
						<input type="reset" />,<input type="submit" />,<input type="text" />,<select><option>Option</option></select>,
						<textarea></textarea>,<button>Button</button>*/
     		          	$($(ohtml[x]).find(":input")).attr("disabled","disabled");
						$($(ohtml[x]).find(":input")).removeAttr("onclick");
						$($(ohtml[x]).find("span")).removeAttr("onclick");
						newhtml=newhtml+ohtml[x].outerHTML;
  		            }
  		        }
                suunCore.setHtml(htmldiv,'<form>'+newhtml+'</form>',true,callback);                
    	    },
    	    failure: function(){
			    suunCore.hideMask();
    	    }
    	});
	},
	extComLoadurl:function(extCom,dourl,postparams,callback){
		suunCore.loadurl(extCom.body.dom,dourl,postparams,callback);
	},
	extComLoadurlReadonly:function(extCom,dourl,postparams,ROcallback){
		suunCore.loadurlReadonly(extCom.body.dom,dourl,postparams,function(hdiv){
			if (ROcallback) {ROcallback(hdiv);}
		});
	},
	//创建view的window
	createWinView:function (options){
		if (!options.contexturl) {
			alert("contexturl is null")
			return;
		}
		suunCore.showMask();
		if (!options.winTitle) {
			options.winTitle='信息查看';
		}
		if (!options.winWidth) {
			options.winWidth=600;
		}
		if (!options.winHeight) {
			options.winHeight=400;
		}
		if ("undefined" == typeof options.postparam){
			options.postparam={};
		}		
		var SuunWinForm = new Ext.Window({    		
        	resizable: false,
            modal: true,
            autoScroll:true,
        	width: options.winWidth,
	        height: options.winHeight,
            title: '<center style="curor:hand">'+options.winTitle+'</center>',
            buttons: [{
                text: "关闭",handler:function(){
                	SuunWinForm.close();
                }
            }],
			listeners:{'show':function(){
				//不能放在下面，否则SuunWinForm.body.dom不存在
				suunCore.extComLoadurlReadonly(SuunWinForm,options.contexturl,options.postparam,function(){
		                                       if (options.callback) {options.callback();} 
										       suunCore.hideMask();
										   });
            }}
        }); 
		SuunWinForm.show();
	},
	//创建form的window
	createWinForm:function (options){
	    if (!options.contexturl||!options.formAction) {
			alert("contexturl or fromaction is null")
			return;
		}
		suunCore.showMask();
		if (!options.winTitle) {
			options.winTitle='新增';
		}
		if (!options.winWidth) {
			options.winWidth=600;
		}
		if (!options.winHeight) {
			options.winHeight=400;
		}
		if ("undefined" == typeof options.postparam){
			options.postparam={};
		}
		var SuunWinForm = new Ext.Window({    		
        	resizable: false,
            modal: true,
            id:"suunFormWindow",
            autoScroll:true,
        	width: options.winWidth,
	        height: options.winHeight,
            title: '<center style="curor:hand">'+options.winTitle+'</center>',
            buttons: [{
            	id:"BtnSave",
                text: "保存",
                handler:function(){
                	//不是form
                	if ($(document.getElementById("SuunWinForm")).length==0){
                		SuunWinForm.close();
                		return;
                	}
                	suunCore.showMask();//防止重复提交
                	var suunvalidate=$(document.getElementById("SuunWinForm")).validate();	                	
                	var isvalidate=null;
                	try{
                		isvalidate=suunvalidate.form();
                	}catch(e){
                		isvalidate=null;
                	}	
                	if (isvalidate==null){	 
                		SuunWinForm.hide();
            	    	Ext.MessageBox.show({
    						title : "编程错误",
    						msg : "校验写错了？！",
    						icon : Ext.MessageBox.ERROR,
    						buttons : Ext.Msg.OK,
    						fn:function(){	  
    							suunCore.hideMask();
    							SuunWinForm.show();
    						}
    					});
                	}
                	else if (isvalidate){
	       				Ext.Ajax.request({
	                	    form: document.getElementById("SuunWinForm"),
	                	    success: function(msg){ 
	                	    	try{//if (msg.responseText=='{"success":"true"}'){	
	                	    		var message = message = eval('('+ msg.responseText+ ')');
	                	    		if (message.success == "true") {	    	                	    
	                	    				if (options.callback) {options.callback(message);}
	    									SuunWinForm.close();
											suunCore.hideMask();
	    									return true;
	    	                	    	} else{
	    	                	    		SuunWinForm.hide();
	    	                	    		suunCore.hideMask();
	    		                	    	Ext.MessageBox.show({
	    	    	    						title : "错误",
	    	    	    						msg : message.error,
	    	    	    						icon : Ext.MessageBox.ERROR,
	    	    	    						buttons : Ext.Msg.OK,
	    	    	    						fn:function(){	    	    	    							
	    	    	    							SuunWinForm.show();
	    	    	    						}
	    	    	    					});
	    		                	    	
	    	                	    	}
	                	    	}catch(e){
								    alert(e);
									suunCore.hideMask();
	                	    		SuunWinForm.close();
	                        	}								
	                	    },
	                	    failure: function(){
	                	    	SuunWinForm.hide();
	                	    	suunCore.hideMask();
	                	    	Ext.MessageBox.show({
    	    						title : "错误",
    	    						msg : "通讯超时失败！",
    	    						icon : Ext.MessageBox.ERROR,
    	    						buttons : Ext.Msg.OK,
    	    						fn:function(){	    	    							    							
    	    							SuunWinForm.show();
    	    						}
    	    					});		                	    	
	                	    }
	                	});
       				} else{
       					SuunWinForm.hide();
       					var errors='';
	                	for ( var i = 0; i<suunvalidate.errorList.length; i++ ){
	                		errors=errors+(i+1).toString()+":"+suunvalidate.errorList[i].message+'<br>';
	                	}
						suunCore.hideMask();
       					Ext.MessageBox.show({
    						title : "数据输入问题",
    						msg : errors,
    						icon : Ext.MessageBox.ERROR,
    						buttons : Ext.Msg.OK,
    						fn:function(){									
    							SuunWinForm.show();
    						}
    					});
       					suunvalidate.resetForm();
       				}	                	
                }
            }, {
                text: "取 消",handler:function(){
                	suunCore.showMask();
                	SuunWinForm.close();
                	suunCore.hideMask();
                }
            }],
            listeners:{'beforedestroy':function(){
            }}
        }); 
    	Ext.Ajax.request({
    	    url: options.contexturl,
    	    params: options.postparam,
    	    success: function(msg){
			    SuunWinForm.show();//不能放在下面，否则SuunWinForm.body.dom不存在
    	    	if (msg.responseText.indexOf('<title>有流程单据审核通过不能编辑</title>')>0){
    	    		SuunWinForm.update(msg.responseText,true); 
    	    	} else {
    	    		var ohtml=$(msg.responseText);		
  		            var newhtml="";
  		            for (x = 0; x < ohtml.length; x++){
  		                if (ohtml[x].outerHTML){
							newhtml=newhtml+ohtml[x].outerHTML;
  		                }
					}
					suunCore.setExtComHtml(SuunWinForm,
					                       '<form id="SuunWinForm" action="'+options.formAction+'" method="post">'+newhtml+'</form>',
										   true,
										   function(){
										       suunCore.hideMask();
										   });
					//SuunWinForm.update('<form id="SuunWinForm" action="'+fromaction+'" method="post">'+newhtml+'</form>',true); 
                    //update当载入html的script有问题时没有处理，没有等待处理完包含的js直接处理页面script					
    	    	}
    	    },
    	    failure: function(){
    	    	SuunWinForm.close();
				suunCore.hideMask();
    	    	/*Ext.MessageBox.show({
					title : "错误",
					msg : "出错了！",
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.Msg.OK
				});*/    	    	
    	    }
    	});
    },
	DeleteRecords:function (options){
	    if (!options.contexturl) {
			alert("contexturl is null")
			return;
		}		
		suunCore.showMask();
		if ("undefined" == typeof options.postparam){
			options.postparam={};
		}
	    Ext.Ajax.request({
			url: options.contexturl,
			params: options.postparam,
			success: function(msg){
				var message = eval('('+ msg.responseText+ ')');
				if (message.success == "true") {
				  //if (msg.responseText=='{"success":"true"}'){
				    suunCore.hideMask();
					Ext.MessageBox.show({
						title : "提示",
						msg : "数据删除成功！",
						icon : Ext.MessageBox.INFO,
						buttons : Ext.Msg.OK
					}); 
					if (options.callback) {options.callback(message);}
				}else{
				    suunCore.hideMask();
					Ext.MessageBox.show({
						title : "错误",
						msg : "数据删除失败:"+message.error,
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
			    }				
		    },
		    failure: function(){
			    suunCore.hideMask();
			    Ext.MessageBox.show({
					title : "错误",
					msg : "通讯超时失败！",
					icon : Ext.MessageBox.ERROR,
				    buttons : Ext.Msg.OK
				});				
		    }
		});
	},
	GridExportFile:function (options){
	    if (!options.exporturl) {
			alert("exporturl is null")
			return;
		}
		if (!options.contextstr) {
			options.contextstr='';
		}
	    if (!options.exporttype) {
			options.exporttype='xls';
		}
  	    Ext.Msg.prompt('导出文件名', '请输入导出文件名称(不带扩展名):', function(btn, text){
  	        if (btn == 'ok'){
				if (! Ext.fly('suungridexportform')) {	    	                		
					var ifrm = document.createElement('iframe');
					ifrm.id = 'suungridexportiframe';
					ifrm.name = 'suungridexportiframe';
					ifrm.scrolling='no';
					ifrm.width='0';
					ifrm.height='0';
					ifrm.frameborder='0';
					document.body.appendChild(ifrm);
					var frm = document.createElement('form');
					frm.id = 'suungridexportform';
					frm.name = 'suungridexportform';
					frm.method='post';
					frm.className = 'x-hidden';
					frm.target='suungridexportiframe';
					frm.action=options.exporturl;
					document.body.appendChild(frm);
				}
				options.contextstr=options.contextstr+
					'<input type="text" id="suunexporttype" name="exportType" value="'+options.type+'"/>'+
					'<input type="text" id="suunexportfilename" name="filename" value="'+text+'"/>';    		    
				Ext.fly('suungridexportform').dom.innerHTML=options.contextstr;
				Ext.fly('suungridexportform').dom.submit();
  	        }
  	    });
    },
    GridSearchMore:function (options){
		// 判断详细条件查询是否成立
		function succeorerro(zdvalues, czvalues, srvalues, gxvalues) {
			
			// 判断单行查询条件是否成立
			for (var a = 1; a < 7; a++) {
				if (!(zdvalues[a - 1].value == "" && czvalues[a - 1].value == ""
						&& srvalues[a - 1].value == "" || zdvalues[a - 1].value != ""
						&& czvalues[a - 1].value != ""
						&& srvalues[a - 1].value != "")) {
					Ext.MessageBox.show({
								title : "错误",
								msg : "第" + a + "行查询条件不成立 , 请重新输入！",
								icon : Ext.MessageBox.ERROR,
								buttons : Ext.Msg.OK
							});
					return true;
				}
			}
			// 关系符判断
			for (var b = 1; b < 6; b++) {
				for (var c = b; c < 6; c++) {
					if (zdvalues[b - 1].value != "" && czvalues[b - 1].value != ""
							&& srvalues[b - 1].value != ""
							&& zdvalues[c].value != "" && czvalues[c].value != ""
							&& srvalues[c].value != ""
							&& gxvalues[b - 1].value == "") {
						Ext.MessageBox.show({
									title : "错误",
									msg : "查询条件不成立 , 第" + b + "行没有输入关系符！",
									icon : Ext.MessageBox.ERROR,
									buttons : Ext.Msg.OK
								});
						return true;
					}
				}
			}
		}
	
		function zzstring(list) {
			var liststr = "";
			for (var i = 0; i < list.length; i++) {
				if (list[i].value != "") {
					liststr = liststr + list[i].value + "@";
				}
			}
			return liststr;
		}
	
	    suunCore.showMask();
		var opt = "<option></option>";
		for (var i = 0; i < options.searchdata.length; i++) {
			opt = opt + "<option value='" + options.searchdata[i].MoreSearchId+"' stype='"+options.searchdata[i].MoreSearchType+"' cdatd='"+options.searchdata[i].MoreSearchCdata+"'>" + options.searchdata[i].MoreSearchName + "</option>";
		}
		/*var myData=[];
		for (i=0;i<5;i++){
			myData.push(["<select name='ziduanvalue' style='width:100%'>"+ opt + "</select>",
				"<select name='caozuovalue'style='width:100%'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>",
				"<input style='width:98%' type='text' name='shuruvalue'>",
				"<select name='guanxivalue'style='width:100%'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"
			]);
		}
		myData.push(["<select name='ziduanvalue' style='width:100%'>"+ opt + "</select>",
				"<select name='caozuovalue'style='width:100%'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>",
				"<input style='width:98%' type='text' name='shuruvalue'>",
				""
		]);		
		var columstor = new Ext.data.ArrayStore({
			fields: [
			    {name: 'ziduanname'},
			    {name: 'caozuoname'},
			    {name: 'shuruvalue'},
			    {name: 'guanxivalue'}
			]
		});
		columstor.loadData(myData);*/
		var htmls='<table width="480px" cellspacing="0" cellpadding="0" border="0" style="border:solid 2px #78b0dc;" align="center">'+
		            '<tr><td style="border:solid 1px #78b0dc;">&nbsp</td><td align="center" style="border:solid 1px #78b0dc;">字段名</td><td align="center" style="border:solid 1px #78b0dc;">操作符</td><td align="center" style="border:solid 1px #78b0dc;">查询值</td><td align="center" style="border:solid 1px #78b0dc;">关系符</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue' style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue' style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue' style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue'style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue'  style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue'style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue'style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px'><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            '<tr><td style="border:solid 1px #78b0dc;"><input type="checkbox"></td><td align="center" style="border:solid 1px #78b0dc;"><select name="ziduanvalue" style="width:120px">'+ opt + '</select></td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='caozuovalue'style='width:100px'><option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option></select>"+
		            '</td><td align="center" style="border:solid 1px #78b0dc;"><input style="width:180px" type="text" name="shuruvalue"</td><td align="center" style="border:solid 1px #78b0dc;">'+
		            "<select name='guanxivalue' style='width:54px' hidden><option></option><option value='and'>&nbsp;&nbsp;并且&nbsp;&nbsp;</option><option value='or'>&nbsp;&nbsp;或者&nbsp;&nbsp;</option></select>"+
		            '</td></tr>'+
		            //'<tr><td style="border:solid 1px #78b0dc;" colspan="5">&nbsp;&nbsp;<input class="reverse" type="button" value="全选中">&nbsp;&nbsp;<input class="addBtn" type="button" value="添加">&nbsp;&nbsp;<input class="delBtn" type="button" value="删除"></td></tr>'+
		          '</table>';
		winDetailed = new Ext.Window({
			title : '详细条件查询',
			height : 250,
			width : 500,
			layout : 'fit',
			modal : true,
			closeAction : 'hide',	
			html:htmls,
			/*items : [{
						xtype : 'grid',
						border : false,
						viewConfig : {
						forceFit : true,
						enableRowBody : false,
						showPreview : false
					},
					enableHdMenu:false,
					enableColumnHide:false,
					enableDragDrop:false,
					enableColumnMove:false,
					enableColumnResize:false,               
					stripeRows : true,//斑马线效果
					columns : [
						{
							header : '字段名',
							width : 30,
							dataIndex : 'ziduanname',
							sortable:false
						}, {
							header : '操作符',
							width : 18,
							dataIndex : 'caozuoname',
							sortable:false
						}, {
							header : '输入值',
							width : 27,
							dataIndex : 'shuruvalue',
							sortable:false
						}, {
							header : '关系符',
							width : 18,
							dataIndex : 'guanxivalue',
							sortable:false
						}
					],
					store : columstor
				}],*/
				listeners : {
					"hide": function() {
						winDetailed.close();
						winDetailed.destroy();
						winDetailed = null;
					}
				},buttons : [{
					text : "查 询",
					handler : function() {
						suunCore.showMask();
						var zdvalues = document.getElementsByName("ziduanvalue");
						var czvalues = document.getElementsByName("caozuovalue");
						var srvalues = document.getElementsByName("shuruvalue");
						var gxvalues = document.getElementsByName("guanxivalue");
						// 判断详细条件查询是否成立
						if (succeorerro(zdvalues, czvalues,srvalues, gxvalues)) {
						    suunCore.hideMask();
							return;
						}
						zdstr = zzstring(zdvalues);
						czstr = zzstring(czvalues);
						sustr = zzstring(srvalues);
						gxstr = zzstring(gxvalues);
                        if (options.callback) {options.callback(zdstr,czstr,sustr,gxstr,options.scope);}
						winDetailed.hide();
						suunCore.hideMask();
					}
				},{
				    text : "重置",
					handler : function() {
						var zdvalues = document.getElementsByName("ziduanvalue");
						for (var i = 0; i < zdvalues.length; i++) {
							zdvalues[i].value = "";
						}
						var czvalues = document.getElementsByName("caozuovalue");
						for (var i = 0; i < czvalues.length; i++) {
							czvalues[i].value = "";
						}
						var srvalues = document.getElementsByName("shuruvalue");
						for (var i = 0; i < srvalues.length; i++) {
							srvalues[i].value = "";
						}
						var gxvalues = document.getElementsByName("guanxivalue");
						for (var i = 0; i < gxvalues.length; i++) {
							gxvalues[i].value = "";
						}
					}
				},
				{
				    text : "返回",
					handler : function() {
						winDetailed.hide();
					}
				}]
		});
		winDetailed.show();	
		
		interval = setInterval(function() {
			html=$(winDetailed.body.dom.innerHTML);
			se=html.find("select[name='ziduanvalue']");
			if (se.length>0){
				clearInterval(interval);
				zdstrs = options.z.split('@');
				czstrs = options.c.split('@');
				sustrs = options.s.split('@');
				gxstrs = options.g.split('@');
				zd=$("select[name='ziduanvalue']");
				cz=$("select[name='caozuovalue']");
				su=$("input[name='shuruvalue']");
				gx=$("select[name='guanxivalue']");
				for (i=0;i<sustrs.length-1;i++){					
					$(zd[i]).val(zdstrs[i]);
					$(cz[i]).val(czstrs[i]);
					$(su[i]).val(sustrs[i]);
					$(gx[i]).val(gxstrs[i]);
				}	
				$("select").selectCss(suunCore.UIJSPATH); 
				$("input[type='text']").inputCss();
				zd.change(function (e){
					stype=$(this.options[this.options.selectedIndex]).attr("stype");
					cz=$(this.parentElement.parentElement.parentElement).find("select[name='caozuovalue']")[0];
					sr=$(this.parentElement.parentElement.parentElement).find("name='shuruvalue'")[0];
					if(stype=="S") {
						cz.innerHTML="<option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option><option value='like'>&nbsp;包含&nbsp;</option>"
					}else{
						cz.innerHTML="<option></option><option value='GREAT'>&nbsp;大于&nbsp;</option><option value='LESS'>&nbsp;小于&nbsp;</option><option value='EQUAL'>&nbsp;等于&nbsp;</option><option value='GREATEQUAL'>&nbsp;大于等于&nbsp;</option><option value='LESSEQUAL'>&nbsp;小于等于&nbsp;</option><option value='NOTEQUAL'>&nbsp;不等于&nbsp;</option>";
					}
					sr.innerHTML='<div class="x-form-field-wrap x-form-field-trigger-wrap" id="ext-gen126" style="width: 148px;"><input type="text" size="24" autocomplete="off" id="Q_cvalue-ext-gen90" name="Q_cvalue-ext-gen90" class=" x-form-text x-form-field x-form-empty-field x-trigger-noedit" readonly="" style="width: 131px;"><img src="/lams/resources/images/extjs/s.gif" alt="" class="x-form-trigger x-form-arrow-trigger" id="ext-gen127" style="height: 19px;"></div>';	
				});
				suunCore.hideMask();				
			}else{
			    return false;
            }			
		}, 10);
		
	},
	GetAuths:function (options){
		if (suunCore.userAuths==''){
			$.ajax({ 
				url :options.authurl, 
				async:false, 
				success: function(data){				
					suunCore.userAuths=data.isauth;
			    },
				failure : function() {
					Ext.MessageBox.show({
								title : "错误",
								msg : "获取权限时发生错误！",
								icon : Ext.MessageBox.ERROR,
								buttons : Ext.Msg.OK
							});
				}
			});
		}
		return suunCore.userAuths;
	},
	HaveAuths:function (authurl,auths){
		if (suunCore.userAuths==''){
			$.ajax({ 
				url :authurl, 
				async:false, 
				success: function(data){				
					suunCore.userAuths=data.isauth;
			    },
				failure : function() {
					Ext.MessageBox.show({
								title : "错误",
								msg : "获取权限时发生错误！",
								icon : Ext.MessageBox.ERROR,
								buttons : Ext.Msg.OK
							});
				}
			});
		} 
		if (auths){
			aa=auths.split(',');
			for (i=0;i<aa.length;i++){
				if (suunCore.userAuths.indexOf(aa[i])!=-1){
					return true;
				}
			}
			return false;
		} else
			return true;		
	}
	,
	CheckUrl:function (ctype,options){
		if (ctype=="grid"){
			if (!options.baseurl&&
					(!options.authurl||!options.listurl||!options.newurl||!options.editurl||
					 !options.saveurl||!options.deleteurl||!options.exporturl)) {
						alert("url is null")
				return false;
			}	
			//处理默认值	
			if (!options.listurl) options.listurl=options.baseurl+'!lists';
			if (!options.newurl) options.newurl=options.baseurl+'!_new.do';
			if (!options.editurl) options.editurl=options.baseurl+'!edit.do';
			if (!options.saveurl) options.saveurl=options.baseurl+'!save.do';
			if (!options.deleteurl) options.deleteurl=options.baseurl+'!delete';	
			if (!options.exporturl) options.exporturl=options.baseurl+'!exportlist';
			if (!options.authurl) options.authurl=options.baseurl+'!userAuth';
			return true;
		} else if (ctype=="tree"){
			//处理默认值
			if (!options.baseurl&&
				(!options.authurl||!options.listurl||!options.newurl||!options.editurl||
				 !options.saveurl||!options.deleteurl||!options.viewnulldetailurl)) {
				alert("url is null")
				return false;
			}	
			if (!options.listurl) options.listurl=options.baseurl+'!lists';
			if (!options.newurl) options.newurl=options.baseurl+'!_new.do';
			if (!options.editurl) options.editurl=options.baseurl+'!edit.do';
			if (!options.saveurl) options.saveurl=options.baseurl+'!save';
			if (!options.deleteurl) options.deleteurl=options.baseurl+'!delete';	
			if (!options.viewnulldetailurl) options.viewnulldetailurl=options.baseurl+'-input.do';
			return true;
		} else if (ctype=="treegrid"){
			if (!options.tree.allowEdit) {
				if (!options.tree.newurl) options.tree.newurl='';
				if (!options.tree.editurl) options.tree.editurl='';
				if (!options.tree.saveurl) options.tree.saveurl='';
				if (!options.tree.deleteurl) options.tree.deleteurl='';
			}
			if (!options.tree.baseurl&&!options.grid.baseurl) {
				if (!options.baseurl){
					if (!options.authurl&&
						(!options.tree.baseurl&&(!options.tree.listurl||!options.tree.newurl||!options.tree.editurl||!options.tree.saveurl||!options.tree.deleteurl))||
						(!options.grid.baseurl&&(!options.grid.listurl||!options.grid.newurl||!options.grid.editurl||!options.grid.saveurl||!options.grid.deleteurl||!options.grid.exporturl))){
						alert("url is null")
						return false;
					}		    
			    }else{
				    if (!options.tree.baseurl){
						options.tree.baseurl=options.baseurl;
					}
					if (!options.grid.baseurl){
						options.grid.baseurl=options.baseurl;
					}
					if (!options.authurl) options.authurl=options.baseurl+'!userAuth';
				}
			} 			
			
			if (!options.grid.listurl) options.grid.listurl=options.grid.baseurl+'!gridlists';
			if (!options.grid.newurl) options.grid.newurl=options.grid.baseurl+'!grid_new.do';
			if (!options.grid.editurl) options.grid.editurl=options.grid.baseurl+'!gridedit.do';
			if (!options.grid.saveurl) options.grid.saveurl=options.grid.baseurl+'!gridsave';
			if (!options.grid.deleteurl) options.grid.deleteurl=options.grid.baseurl+'!griddelete';	
			if (!options.grid.exporturl) options.grid.exporturl=options.grid.baseurl+'!gridexportlist';
			
			if (!options.tree.listurl) options.tree.listurl=options.tree.baseurl+'!treelists';
			if (!options.tree.newurl) options.tree.newurl=options.tree.baseurl+'!tree_new.do';
			if (!options.tree.editurl) options.tree.editurl=options.tree.baseurl+'!treeedit.do';
			if (!options.tree.saveurl) options.tree.saveurl=options.tree.baseurl+'!treesave';
			if (!options.tree.deleteurl) options.tree.deleteurl=options.tree.baseurl+'!treedelete';
			return true;
		} else if (ctype=="gridtree"){
			if (!options.grid.allowEdit) {
				if (!options.grid.newurl) options.grid.newurl='';
				if (!options.grid.editurl) options.grid.editurl='';
				if (!options.grid.saveurl) options.grid.saveurl='';
				if (!options.grid.deleteurl) options.grid.deleteurl='';
			}
			if (!options.tree.baseurl&&!options.grid.baseurl) {
				if (!options.baseurl){
					if (!options.authurl&&
						(!options.tree.baseurl&&(!options.tree.listurl||!options.tree.newurl||!options.tree.editurl||!options.tree.saveurl||!options.tree.deleteurl))||
						(!options.grid.baseurl&&(!options.grid.listurl||!options.grid.newurl||!options.grid.editurl||!options.grid.saveurl||!options.grid.deleteurl||!options.grid.exporturl))){
						alert("url is null")
						return false;
					}		    
			    }else{
				    if (!options.tree.baseurl){
						options.tree.baseurl=options.baseurl;
					}
					if (!options.grid.baseurl){
						options.grid.baseurl=options.baseurl;
					}
					if (!options.authurl) options.authurl=options.baseurl+'!userAuth';
				}
			} 			
			
			if (!options.grid.listurl) options.grid.listurl=options.grid.baseurl+'!gridlists';
			if (!options.grid.newurl) options.grid.newurl=options.grid.baseurl+'!grid_new.do';
			if (!options.grid.editurl) options.grid.editurl=options.grid.baseurl+'!gridedit.do';
			if (!options.grid.saveurl) options.grid.saveurl=options.grid.baseurl+'!gridsave';
			if (!options.grid.deleteurl) options.grid.deleteurl=options.grid.baseurl+'!griddelete';	
			if (!options.grid.exporturl) options.grid.exporturl=options.grid.baseurl+'!gridexportlist';
			
			if (!options.tree.listurl) options.tree.listurl=options.tree.baseurl+'!treelists';
			if (!options.tree.newurl) options.tree.newurl=options.tree.baseurl+'!tree_new.do';
			if (!options.tree.editurl) options.tree.editurl=options.tree.baseurl+'!treeedit.do';
			if (!options.tree.saveurl) options.tree.saveurl=options.tree.baseurl+'!treesave';
			if (!options.tree.deleteurl) options.tree.deleteurl=options.tree.baseurl+'!treedelete';
			return true;
		} else if (ctype=="doublegrid"){
			if (!options.grid1.allowEdit) {
				if (!options.grid1.newurl) options.grid1.newurl='';
				if (!options.grid1.editurl) options.grid1.editurl='';
				if (!options.grid1.saveurl) options.grid1.saveurl='';
				if (!options.grid1.deleteurl) options.grid1.deleteurl='';
			}
			if (!options.grid1.baseurl&&!options.grid2.baseurl) {
				if (!options.baseurl){
					if (!options.authurl&&
						(!options.grid1.baseurl&&(!options.grid1.listurl||!options.grid1.newurl||!options.grid1.editurl||!options.grid1.saveurl||!options.grid1.deleteurl||!options.grid2.exporturl))||
						(!options.grid2.baseurl&&(!options.grid2.listurl||!options.grid2.newurl||!options.grid2.editurl||!options.grid2.saveurl||!options.grid2.deleteurl||!options.grid2.exporturl))){
						alert("url is null")
						return false;
					}		    
			    }else{
				    if (!options.grid1.baseurl){
						options.grid1.baseurl=options.baseurl;
					}
					if (!options.grid2.baseurl){
						options.grid2.baseurl=options.baseurl;
					}
				}
				if (!options.authurl) options.authurl=options.baseurl+'!userAuth';
			} 
			//处理默认值
			if (!options.grid1.listurl) options.grid1.listurl=options.grid1.baseurl+'!grid1lists';
			if (!options.grid1.newurl) options.grid1.newurl=options.grid1.baseurl+'!grid1_new.do';
			if (!options.grid1.editurl) options.grid1.editurl=options.grid1.baseurl+'!grid1edit.do';
			if (!options.grid1.saveurl) options.grid1.saveurl=options.grid1.baseurl+'!grid1save';
			if (!options.grid1.deleteurl) options.grid1.deleteurl=options.grid1.baseurl+'!grid1delete';
			if (!options.grid1.exporturl) options.grid1.exporturl=options.grid1.baseurl+'!grid1exportlist';
			
			if (!options.grid2.listurl) options.grid2.listurl=options.grid2.baseurl+'!grid2lists';
			if (!options.grid2.newurl) options.grid2.newurl=options.grid2.baseurl+'!grid2_new.do';
			if (!options.grid2.editurl) options.grid2.editurl=options.grid2.baseurl+'!grid2edit.do';
			if (!options.grid2.saveurl) options.grid2.saveurl=options.grid2.baseurl+'!grid2save';
			if (!options.grid2.deleteurl) options.grid2.deleteurl=options.grid2.baseurl+'!grid2delete';	
			if (!options.grid2.exporturl) options.grid2.exporturl=options.grid2.baseurl+'!grid2exportlist';
			return true;
		}		
	}
};



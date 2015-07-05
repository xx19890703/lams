<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>劳动能力鉴定管理信息系统</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/> 
    <meta http-equiv="Cache-Control" content="no-store"/> 
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/> 
    <meta name="generator" content="suunframe 2.1" /> 
    <link rel="shortcut icon" href="${ctx}/resources/images/system/favicon.ico"/>     
    <link rel="stylesheet" href="${ctx}/resources/js/thrid/extjs/resources/css/ext-all.css" type="text/css"></link>
    <link rel="stylesheet" href="${ctx}/resources/css/ext-fix.css" type="text/css"></link>   
    <link rel="stylesheet" href="${ctx}/resources/css/default.css" type="text/css"></link> 
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>    
    <script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>	
    <script type="text/javascript">var $ctx='${ctx}';</script>	
    <script type="text/javascript" src="${ctx}/resources/js/system/core/suuncore.js"></script>       
	<style type="text/css">
		.column{font-size:14px;height:24px;line-height:30px}
		.column ul{width:100%;overflow:hidden;border:0}
		.column li{margin-left:5px;height:24px;text-align:center;float:left;display:inline;}
		.column .current{background-color:#FFFFFF}
		.column a { text-decoration: none}	
		span {vertical-align: middle;}
	</style>    
    <script type="text/javascript">  
      function setCookie(cookiename, cookievalue, days,path) {
         var date = new Date();
         date.setTime(date.getTime() + Number(days)* 24 * 3600 * 1000);//设置毫秒值
         document.cookie = cookiename + "=" + cookievalue + "; path= "+path+";expires = " + date.toGMTString();
      }  
      function getCookie(cookiename) {
        var result;
        var mycookie = document.cookie;
        var start2 = mycookie.indexOf(cookiename + "=");
        if (start2 > -1) {
          start = mycookie.indexOf("=", start2) + 1;
          var end = mycookie.indexOf(";", start);        
          if (end == -1) {
            end = mycookie.length;
          }        
          result = unescape(mycookie.substring(start, end));
        }        
        return result;
     }

     var defaultTheme = getCookie("theme")?getCookie("theme"):'ext-all';
       
     Ext.util.CSS.swapStyleSheet("theme",$ctx+ "/resources/js/thrid/extjs/resources/css/"+ defaultTheme+ ".css");
		
    var toppanel = new Ext.Panel({
			id:'suun-toppanel',
			frame : false,
	    region : "north",
	    height: '80',
	    html:'<table width="100%" height="78px" border="0" cellpadding="0" cellspacing="0">'+
	        	 '<tr><td width="310" align="left" valign="middle" background="${ctx}/resources/images/system/topr.jpg"></td>'+
	        	 '<td align="right" valign="top" background="${ctx}/resources/images/system/topc.jpg">'+
	        	 '<table width="100%" height="78px" cellpadding="0" cellspacing="0">'+
	        	 '<tr height="20px"><td align="right" valign="bottom"><span style="color: #FFFFFF;font-size:13px;*font-size:12px;!important;font-weight: bold;">欢迎您：${loginName}'+
	           	/* '<%try{%><security:authentication property="principal.username"></security:authentication><%}catch(Exception e1){;} %>'+*/
	           	 '</span>&nbsp;&nbsp;<a style="color:#0099FF;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/home.do\',title:\'主页\',iconcls:\'home\',closable:false})">主页</a>&nbsp;&nbsp;'+
	           	 '<A style="color:#0099FF;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunCore.createWinForm({winTitle:\'修改密码\',winWidth:320,winHeight:190,contexturl:\'${ctx}/system/security/changePswd.do\',formAction:\'${ctx}/system/security/security!changepass.do\'});">修改密码</A>&nbsp;&nbsp;'+
	           	 '<a style="color:#0099FF;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="${ctx}/j_spring_security_logout">退出</a>&nbsp;</td><td width="150" align="right" valign="top"><div id="header-theme"/></td></tr>'+
	        	 '<tr><td colspan="2" valign="bottom"><div id="header-nav"></div></td></tr></table></td></tr></table>'
		});
		
		var leftpanel = new Ext.Panel({
			      id: 'app-menus',
            region: 'west',
            title:'系统菜单',
            //frame : true,
            //animCollapse: true,
            width: 200,
            minWidth: 150,
            maxWidth: 400,
            split: true,
            iconCls:'navigation',
            collapsible: true,
            layout : "accordion",
           /* header:false,
            html:'<A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/developer/authority.do\',title:\'授权管理\'});">授权管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/security/user.do\',title:\'用户管理\'});">用户管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/security/role.do\',title:\'角色管理\'});">角色管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/orgnization/department.do\',title:\'组织机构\'});">组织机构管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/developer/menu.do\',title:\'菜单管理\'});">菜单管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demo.do\',title:\'demo\'});">Demo</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demodetail.do\',title:\'Demodetail\'});">Demodetail</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demotree.do\',title:\'Demotree\'});">Demotree</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demotreegrid.do\',title:\'Demotreegrid\'});">Demotreegrid</A>'+
			'<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demodoublegrid.do\',title:\'Demodoublegrid\'});">Demodoublegrid</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/demoselect.do\',title:\'Demoselect\'});">Demoselect</A>'+
			'<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/flowjquery/flowdesgner.do\',title:\'流程图编辑器jquery\',isframe:true});">流程图编辑器jquery</A>'+
            //'<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/workflow/editor.do\');">流程图编辑器</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/workflow/workflow.do\',title:\'流程图管理\',isframe:true});">流程图管理</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/ReportServer?reportlet=updatenodedata.cpt&op=view\',title:\'报表demo\',isframe:true});">报表demo</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/ReportServer?reportlet=power1.cpt&op=write\',title:\'填报demo\',isframe:true});">填报demo</A>'+
            '<br><A style="color:#3C78B5;font-size:13px;*font-size:12px;!important;text-decoration:none;" href="JavaScript:suunurl({url:\'${ctx}/system/demo/ajaxdemo.do\',title:\'ajax测试demo\',isframe:true});">JQUERY ajax测试demo</A>'*/
		});

		var mainpanel = new Ext.TabPanel({ 
			id:'suun-mainpanel',　
			//layout: 'border',
			//src: '${ctx}/home.do'
			region : "center",
			defaults : {
					autoScroll : true,
					closable : true
			},
			deferredRender : true,
			enableTabScroll : true,
			activeTab : 0,
			onTitleDbClick : function(e, target, o) {
				var t = this.findTargets(e);
				if (t.item && t.item.closable) {
					if (t.item.fireEvent('beforeclose', t.item) !== false) {
						t.item.fireEvent('close', t.item);
						this.remove(t.item);
					}
				}
			},
			plugins:[new Ext.ux.TabCloseMenu({  
				  closeTabText : '关闭当前页',  
				  closeOthersTabsText : '关闭其他页',  
				  closeAllTabsText : '关闭所有页' 
			   })
			],
			items : [],
			listeners : {
				"add" : function(d, b, c) {
					if (d.items.length > 8) {
						d.remove(d.items.get(1));
						d.doLayout();
					}
				}
			}
		});
			
		Ext.onReady(function() {	
			suunCore.showMask();			
			new Ext.Viewport({
					id:'main',
					layout: {type: 'border',padding: '2 2 2 2'}, // pad the layout from the window edges	            
					renderTo : Ext.getBody(),
					items : [toppanel,leftpanel,mainpanel]
			});           
			new Ext.form.ComboBox ({
				renderTo : "header-theme",
				mode : "local",
				editable : false,
				value : "切换皮肤",
				width : 100,
				triggerAction : "all",
				store : [[ "ext-all", "缺省浅蓝" ],
								[ "ext-all-css04", "灰白主题" ],
								[ "ext-all-css05", "绿色主题" ],
								[ "ext-all-css03", "粉红主题" ],
								[ "xtheme-tp", "灰色主题" ],
								[ "xtheme-default2", "灰蓝主题" ],
								[ "xtheme-default16","蓝绿主题" ],
								[ "xtheme-access","Access风格" ] ],
				listeners : {
					scope : this,
					"select" : function(d, b, c) {
						if (d.value != "") {
							var a = new Date();
							a.setDate(a.getDate() + 300);
							setCookie("theme",d.value, a,$ctx);
							Ext.util.CSS.swapStyleSheet("theme",
									$ctx+ "/resources/js/thrid/extjs/resources/css/"+ d.value+ ".css");
						}
					}
				}
			});
			var navtabs = new Ext.TabPanel({
				id:"header-nav-tabs",
		        renderTo: "header-nav",
		        width:$('#header-nav').width(),
		        activeTab: 0,
		        //cls: 'ss',
		        //tabCls: 'sstab',
		        //tabPosition: 'bottom',//指定了选项卡的位置，left,right,
		        deferredRender : true,
				enableTabScroll : true,
				frame : false,
				border : false,
				plain : true,
				height : 0,
				tabMargin : 1,
				//resizeTabs : true,
				minTabWidth : 120,
				defaults : {
					autoScroll : true,
					closable : false,
					bodyStyle : "padding-bottom: 12px;"
				}        
		    });
			NavtabsStyleChange();
			var topMenu=${topMenu};
		    for (var i = 0; i < topMenu.length; i++)
		    	navtabs.add({
		            title: '<span style="padding:0 0 0 20px;background-size:15px 15px;background-image:url(\'${ctx}'+topMenu[i].menuImg+'\')!important;background-repeat:no-repeat;">'+topMenu[i].menuName+'</span>',
		            id: "topMenu-"+topMenu[i].menuId
		        });		    
		    if (navtabs.items.length>0) {
		    	var e = getCookie("_topNavId");
		    	var a=null;
		    	if (!e){
		    		a=navtabs.items.get(0);
		    	} else{
		    		a=navtabs.getItem(e);
		    		if (!a){
		    			a=navtabs.items.get(0);
		    		}
		    	}
		    	if (a){
		    		navtabs.activate(a);
			    	var b = new Date();
					b.setDate(b.getDate() + 300);
					setCookie("_topNavId", a.getId(),b,$ctx);
					loadWestMenu(a.id.split("-")[1]);
		    	}		    	
		    }
		    navtabs.addListener("tabchange",  function(d,c) {
                	var b = new Date();
    				b.setDate(b.getDate() + 300);
    				setCookie("_topNavId", c.getId(),b,$ctx);
    				loadWestMenu(c.id.split("-")[1]);				
			});
			suunurl({url:'${ctx}/home.do',title:'主页',iconcls:'${ctx}/resources/images/system/home.png',closable:false});
			suunCore.hideMask(); 
		});		
		
		
		function suunurl(options){		    
		    suunCore.showMask();							
		    try{
				if (!options.url||!options.title){
					alert("dourl||title is null")
					return;
				}
				var e = mainpanel.items.indexOfKey('contextPanel-'+options.title);	
				if (e < 0) {
				    if ("undefined" == typeof options.iconcls){
						options.iconcls="";
					}
					if ("undefined" == typeof options.closable){
						options.closable=true;
					}
					var loadurl="";
					if (options.url.indexOf('?')<0){
						loadurl=options.url+'?dt='+ new Date().getTime();
					}else{
						loadurl=options.url+'&dt='+ new Date().getTime();
					}
					if (options.isframe){						
						mainpanel.add({
							id : 'contextPanel-'+options.title,
							title: '<span style="padding:0 0 0 20px;background-size:15px 15px;background-image:url(\''+options.iconcls+'\')!important;background-repeat:no-repeat;">'+options.title+'</span>',
							region : "center",
							closable: options.closable,
							iconCls : options.iconcls,
							html:'<iframe onload="JavaScript:suunCore.hideMask();" width="100%" height="100%" frameborder="0" src="'+loadurl+'"></iframe>'
						}).show();
					}else{
						mainpanel.add({
							id : 'contextPanel-'+options.title,
							title: '<span style="padding:0 0 0 20px;background-size:15px 15px;background-image:url(\''+options.iconcls+'\')!important;background-repeat:no-repeat;">'+options.title+'</span>',
							region : "center",
							closable: options.closable
						}).show();
						suunCore.extComLoadurl(Ext.getCmp('contextPanel-'+options.title),loadurl,{suuntitle:options.title},
						                        function(htmlDiv){
											        suunCore.hideMask();
											    });
						//Ext.getCmp('contextPanel-'+options.title).load({url:options.url,params:{suuntitle:options.title,dt:new Date().getTime()},scripts:true});
						//load当载入html的script有问题时没有处理，没有等待处理完包含的js直接处理页面script
					}
				} else {
					mainpanel.items.items[e].show();
					suunCore.hideMask(); 
				}			  
			}catch(e){
			    alert(e);
			}			    
		}	
		function loadWestMenu(topMenuId) {		
			Ext.Ajax.request( {
				url : "${ctx}/main!classmenu.do?topMenuId=" + topMenuId,// + "&isReload=" + isReload,
				success : function(response, options) {
					var arr = eval(response.responseText);
					var __activedPanelId = getCookie("__activedPanelId");
					leftpanel.removeAll();
					for ( var i = 0; i < arr.length; i++) {
						var panel = new Ext.tree.TreePanel( {
							id : "classMenu-"+arr[i].menuId,
							title : '<span style="color:#15428b;font-weight:bold;padding:0 0 0 20px;background-size:15px 15px;background-image:url(\'${ctx}'+arr[i].menuImg+'\')!important;background-repeat:no-repeat;">'+arr[i].menuName+'</span>',
							//icon : '${ctx}'+arr[i].menuImg,							
							layout : "fit",
							animate : false,
							border : false,
							autoScroll : true,
							loader : new Ext.tree.TreeLoader({
								dataUrl:"${ctx}/main!treemenu.do?classMenuId=" + arr[i].menuId/*,
								preloadChildren : true*/
							}),
							root : new Ext.tree.AsyncTreeNode( {
								id : '0',  
								expanded:true,
						        draggable:false,
								text : arr[i].menuName
							}),
							listeners : {
								expand: function(p) { 							
									var expires = new Date();
									expires.setDate(expires.getDate() + 300);
									setCookie("__activedPanelId", p.id, expires, $ctx);
								}
							},
							rootVisible : false
						});
						
						leftpanel.add(panel);
						//panel.expandAll();
						if ("classMenu-"+arr[i].menuId == __activedPanelId) {
							leftpanel.layout.activeItem = panel;
						}
					}
					leftpanel.doLayout();
				}
			});
		}
		function NavtabsStyleChange(){
			var mincwidth=document.documentElement.clientWidth;
			$(document.body).css("width",730);
			if (mincwidth<730){
				mincwidth=730;
			} 
			$(document.body).css("width",mincwidth);
			Ext.getCmp('header-nav-tabs').setWidth(mincwidth-312);
			var cel=$('#header-nav')[0].firstChild;
			$(cel).css("height",25);
			cel=$('#header-nav')[0].firstChild.firstChild.children[1];
			$(cel).css("height",0);
			$(cel).css("border-bottom-width",0);
			cel=$('#header-nav')[0].firstChild.firstChild.firstChild.firstChild;
			$(cel).css("border-bottom-width",0);
			//360 ul.offsetLeft<0
			var cel=$('#header-nav')[0].firstChild.firstChild.firstChild.firstChild;
			if (cel){
				if (cel.offsetLeft<0){$(cel).css("width",$('#header-nav')[0].offsetWidth)};
			}
		}
		window.onresize = NavtabsStyleChange;
    </script>
  </head>
  <body> 
  </body>
</html>

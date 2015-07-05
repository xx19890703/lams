<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>组织结构管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
     <script type="text/javascript">
     var gridtree=createtreegrid({containerid: 'contextPanel-'+$tabtitle,
       	 baseurl:$ctx+"/system/developer/menu",//基本url
       	 tree:{
		    title:'菜单树',
			allowEdit:true,
			inputFormWidth:320,
			inputFormHeight:310
         },
         grid:{
		    title:'功能',
			simplemode:true,
			keyid:"funId",//关键字
			pagenum:25,//页记录数
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true,,state.key.data_no
			suuncolumns:[{columnid:'funId',columnname:'编号',colwidth:20,defaultsort:true},
	             {columnid:'funName',columnname:'名称',colwidth:10},
				 {columnid:'url',columnname:'路径',colwidth:40}],
      	   inputFormWidth:280,
           inputFormHeight:180
      	 }
       });
    </script>
   <!--   <script type="text/javascript">
	    //权限	 
		var suunauths=suunCore.GetAuths({authurl:$ctx+"/system/developer/menu!userAuth"});
		var gridtopbar=['功能模块','->'];
		
        //if (suunauths.indexOf('')>-1) {
			 gridtopbar.push({
					iconCls:'add',
					text:"添加",
					tooltip:'增加一条信息',
					handler:function(){
						topadd();
					}
		    });		
		//}
		//if (suunauths.indexOf('')>-1)) {
			gridtopbar.push({
					iconCls:'edit',
					text:"修改",
					tooltip:'修改一条信息',
					handler:function(){ 
						gridedit(topgrid.getSelectionModel().getSelections());
					}
			});
		//}
		//if (suunauths.indexOf('')>-1)) {
			gridtopbar.push({
					iconCls:'remove',
					text:"删除",
					tooltip:'删除信息',
					handler:function(){ 
						griddeleterecord();
					}
			});
		//}
	    var topgrid=new suunGridPanel({
			//title:'功能模块',
			simplemode:true,
		    listurl:$ctx+"/system/developer/menu!toplists",
			pagenum:10,
			isprewidth:true,
			extTopbar:gridtopbar,
			suuncolumns:[{columnid:'menuId',columnname:'编号',defaultsort:true},
			             {columnid:'menuName',columnname:'名称',colwidth:70},
			             {columnid:'menuOrder',columnname:'显示序号',colwidth:10}],	
			extBottombar:[],		
			region : 'north',		     
			height : 200,
			split: true,
			storeAutoLoad:true
		});	
	    var clientpanel=new Ext.Panel({
	    	id:'contextPanel-clientpanel',
	    	region : 'center'
	    });
	    Ext.getCmp('contextPanel-'+$tabtitle).add(topgrid);
		Ext.getCmp('contextPanel-'+$tabtitle).add(clientpanel);	
		Ext.getCmp('contextPanel-'+$tabtitle).setLayout(new Ext.layout.BorderLayout());
	    Ext.getCmp('contextPanel-'+$tabtitle).doLayout(true);
	    
	    var mgridtree=creategridtree({
	    	containerid: 'contextPanel-clientpanel',
	       	baseurl:$ctx+"/system/developer/menu",//基本url
	       	grid:{
			    title:'菜单组',
				simplemode:true,
				allowEdit:true,
				keyid:"id",//关键字
				pagenum:5,//页记录数
				isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true,,state.key.data_no
				suuncolumns:[{hidden:true,columnid:'id',columnname:'编号',defaultsort:true},
		             {columnid:'menuName',columnname:'名称',colwidth:70} ],
	      	   inputFormWidth:260,
	           inputFormHeight:140,
	           storeAutoLoad:false
	      	 },
	       	 tree:{
			    title:'菜单项',				
				inputFormWidth:280,
				inputFormHeight:300
	         }	         
	    });
	    mgridtree.storebeforeload=function (store, options){
	    	var tid=''
			if (topgrid.getSelectionModel().getSelections().length>0)
				tid=topgrid.getSelectionModel().getSelections()[0].get('id');		
			else if (topgrid.store.data.length>0){//前面已经先选中了mgridtree1，所以不会存在此情况
				tid=topgrid.store.data.items[0].get('id');
			}
			Ext.apply(store.baseParams, {parentid:tid});
		}
	    topgrid.store.on('load', function () {
			if (this.reader.jsonData){			
				var cur=this.reader.jsonData.curpagepos;
				if(cur>0) {
					topgrid.getSelectionModel().selectRow(cur-1, true);//执行选中记录
				}			
			}
			if (this.data.length>0){
				if (topgrid.getSelectionModel().getSelections().length==0)
					topgrid.getSelectionModel().selectRecords([this.data.items[0]]);			
			}
			mgridtree.loadStore();
		});
	    topgrid.on("rowdblclick", viewrecord);
	    topgrid.on("rowclick", function(){
	    	topgrid.loadStore();;
		});
	    
	    function topadd(){
	    	suunCore.createWinForm({winTitle:"新增",
				winWidth:260,
				winHeight:200,
				contexturl:$ctx+"/system/developer/menu!_new",
				formAction:$ctx+"/system/developer/menu!save",
				callback:function(message){	    		
					topgrid.searchClear();  
					topgrid.searchClear(true); 
					topgrid.loadStore()
				}
			});
	    }
		//查看
		function viewrecord(g, o, q) {
			suunCore.createWinView({winWidth:200,
				winHeight:400,
				contexturl:$ctx+"/system/developer/menu!edit",
				postparam:{suunplatformeoperateid:g.getSelectionModel().getSelected().get('id')}
			});
		}
		
    </script>-->
</head>
<body></body>
</html>
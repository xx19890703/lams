<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>评审管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		grid=createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"baseId",//关键字
			baseurl:$ctx+'/roster/authenticateRoster',  //基本url
			suuncolumns:[ 
                     {columnid:'baseId',columnname:'编号',type:'S',colwidth:20,issort:true,issearch:false,defaultsort:true},
			         {columnid:'name',columnname:'姓名',colwidth:20,issort:true,issearch:true},
					 {columnid:'sex.data_name',columnname:'性别',type:'C',cdata:${sel_sex},colwidth:15,issearch:false},
					 {columnid:'idcarNo',columnname:'身份证号码',colwidth:40},
					 {columnid:'unitName',columnname:'单位名称',colwidth:60,issort:false,issearch:true},
					 {columnid:'phone',columnname:'联系电话',colwidth:30,issearch:false},
					 {columnid:'systemData',columnname:'申请日期',type:'D',colwidth:30,issearch:true},
					 {columnid:'slstate.data_name',columnname:'状态',type:'C',cdata:[["鉴定通过"],["鉴定未通过"],["审核通过"],["审核未通过"]],issearch:true,colwidth:30},
					 {columnid:'slstate',columnname:'状态',type:'C',hidden:true,cdata:[],issearch:false,colwidth:30},
					 new  Ext.grid.Column({header : "审核",
							dataIndex : "slstate",
							sortable : true,
							width : 40,
							renderer : function(j, i, g, m, h) {
								if(g.data.slstate){
									var l = g.data.slstate['key']['data_no'];
									var id = g.data.baseId;
									var k = "";
									if (l == 1) {
										k +=                         
											k += '<img src="${ctx}/resources/images/button/system-setting.png" onclick="javaScript:changeState(\''+grid.getId()+'\',\''+id+'\');"/>';
									}else{
										k +='<img src="${ctx}/resources/images/button/showDetail.png"/>';
									}
									return k;
								}
							}
					})
					],
					inputFormWidth:720,
					inputFormHeight:465,
					
					operation:{
						edit:{hidden:true},
						add:{hidden:true},
						del:{hidden:true},
						exp:{hidden:true}
				    }
		});
		
		function changeState(g,l){
			Ext.Msg.buttonText.yes = "通过";  
			Ext.Msg.buttonText.no = "不通过";  
			  
			var obj = Ext.Msg.show({  
			    title: "审核提示",  
			    msg: "确定审核这个人吗？",  
			    buttons: Ext.Msg.YESNOCANCEL, 
			    closable : false,
			    icon: Ext.Msg.INFO,  
			    fn: function(id, msg){  
			    	if (id == "yes") {
						Ext.Ajax.request({
							url : $ctx+'/roster/authenticateRoster!changeState',
							method : "post",
							params : {
								id:l,
								state:'0'
							},
							success : function(response) {
								  var obj=Ext.decode(response.responseText);    
					                if(obj == true) {
					                	Ext.ux.Toast.msg("提示信息", "审核通过");
					                	Ext.getCmp(g).getStore().reload();
					                } else {  
					                	Ext.ux.Toast.msg("提示信息", "审核失败");
					                }  
							},
							failure : function() {
								Ext.ux.Toast.msg("提示信息", "审核失败");
							}
						});
					}else if(id == "no"){
						Ext.Ajax.request({
							url : $ctx+'/roster/authenticateRoster!changeState',
							method : "post",
							params : {
								id:l,
								state:'1'
							},
							success : function(response) {
								var obj=Ext.decode(response.responseText);    
				                if(obj == true) {
				                	Ext.ux.Toast.msg("提示信息", "审核未通过");
				                	Ext.getCmp(g).getStore().reload();
				                } else {  
				                	Ext.ux.Toast.msg("提示信息", "审核失败");
				                }  
							},
							failure : function() {
								Ext.ux.Toast.msg("提示信息", "审核失败");
							}
						});
					} 
			    }  
			}); 
		}
    </script>
</head>
<body> 
</body>
</html>





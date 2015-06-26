<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>申请管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"baseId",//关键字
			baseurl:$ctx+'/softline/softlinee',//基本url
			//pagenum:5,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture
			suuncolumns:[ 				 
			         {columnid:'baseId',columnname:'编号',type:'S',colwidth:30,defaultsort:true},
			         {columnid:'name',columnname:'姓名',type:'S',colwidth:20,issort:true,issearch:true,defaultsort:false,isASC:true},
					 {columnid:'sex.data_name',columnname:'性别',type:'C',cdata:${sex_state},colwidth:20,issort:false,issearch:false,defaultsort:false,isASC:true},
					 {columnid:'dobs',columnname:'出生日期',type:'D',colwidth:25,issort:true,issearch:true,defaultsort:false,isASC:true},
					 {columnid:'idcarNo',columnname:'身份证号码',type:'S',colwidth:40,issort:false,issearch:true,defaultsort:false,isASC:true},
					 {columnid:'woundTime',columnname:'受伤时间',type:'D',colwidth:25,issort:true,issearch:true,defaultsort:false,isASC:true},
					 {columnid:'phone',columnname:'联系电话',colwidth:30,issort:false,issearch:true,defaultsort:false,isASC:true},
					 {columnid:'addresss',columnname:'通讯地址',colwidth:80,issort:false,issearch:true,defaultsort:false,isASC:true} ,
					 {columnid:'slstate.data_name',columnname:'状态',type:'C',cdata:${sl_state},issort:true,issearch:false,colwidth:30,defaultsort:false,isASC:true},
					 {columnid:'slstate',columnname:'修改状态',type:'C',cdata:${sl_state},hidden:true,issort:true,issearch:false,colwidth:30,defaultsort:false,isASC:true},
					 new  Ext.grid.Column({header : "复核",
							dataIndex : "baseId",
							sortable : true,
							width : 40,
							renderer : function(j, i, g, m, h, s) {
								var creator = g.data.slstate['data_name'];
								var k = "";
								if(creator=="已申请"){
									k += '<button title="复核" style="background:url(${ctx}/resources/images/system/conference_add.png);width:20px;height:18px;" onclick="javaScript:saveSlstate(this,\''+j+'\');"></button>';
								}
							    return k;
							}		
					}) 
					],
					inputFormWidth:850,
					inputFormHeight:490,
					operation:{add:{auth:'AUTH_SOFTLINEE_ADD'},
				          edit:{auth:'AUTH_SOFTLINEE_EDIT'},
						  del:{auth:'AUTH_SOFTLINEE_DEL'},
						  exp:{auth:'AUTH_SOFTLINEE_EXP'},
						  extend:[]
				    }
					
				    
		});
		function saveSlstate(me,l){
				Ext.Msg.confirm("信息确认", "您确认要复核该记录吗？", function(c) {
				if (c == "yes") {
					Ext.Ajax.request({
						url:$ctx+"/softline/softlinee/saveSlstate",
						params:{baseId:l},
						method : "post",
						
						success : function(response) {
							if(response){
								Ext.ux.Toast.msg("信息", "成功复核所选记录！");
								Ext.getCmp(suunCore.GetParentExtGridId(me)).getStore().reload();
							}else{
								Ext.ux.Toast.msg("操作信息","失败复核信息！");
							}
						}
					});
				}
			});
		}	
    </script>
</head>
<body> 
</body>
</html>
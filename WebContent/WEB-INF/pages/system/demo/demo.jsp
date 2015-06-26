<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>授权管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"authId",//关键字
			baseurl:$ctx+'/system/demo/demo',//基本url
			pagenum:5,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture type:"S"/"N"/"D"/"C":字符串、数字、时间、选择，默认S，C是必须设置cdata属性
			suuncolumns:[new Ext.grid.RowNumberer({header:"序号",width:40}),
                     {columnid:'authId',columnname:'编号',type:'N',colwidth:20,defaultsort:true},
					 {columnid:'displayName',columnname:'名称',colwidth:80},
					 {columnid:'demodate',columnname:'时间',type:'D',colwidth:80},
					 {columnid:'state.data_name',columnname:'状态',type:'C',cdata:${sel_state},colwidth:80},
					 new Ext.grid.Column({
							header : "管理",
							dataIndex : "ttId",
							sortable : false,
							width : 60,
							renderer : function(j, i, g, m, h) {
								var l = g.data.ttId;
								var f = g.data.ttName;
								var k = "";
								if (l != 0) {
									k += '<button title="删除" style="background-color:transparent;border:0;padding:0;width:16px;height:16px;" class="btn-close" onclick="javascript:alert(\'ww\');"></button>';									
								}
								return k;
							}
						})
					],
			inputFormWidth:400,
			inputFormHeight:350,
			operation:{check:{hidden:false}}
		});
    </script>
</head>
<body> 
</body>
</html>
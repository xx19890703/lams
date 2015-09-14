<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>制造厂管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"fno",//关键字
			baseurl:$ctx+'/serviceuser/factoryInfo',//基本url
			pagenum:5,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture type:"S"/"N"/"D"/"C":字符串、数字、时间、选择，默认S，C是必须设置cdata属性
			suuncolumns:[{columnid:'fno',columnname:'制造厂编号',colwidth:80,defaultsort:true},
					 {columnid:'fregister',columnname:'注册码',colwidth:100},
					 {columnid:'fname',columnname:'工厂名称',colwidth:200},
					 {columnid:'faddress',columnname:'地址',colwidth:100},
					 {columnid:'ftype.data_name',columnname:'类别',type:'C',cdata:${sel_data1},colwidth:80},
					 {columnid:'flevel.data_name',columnname:'等级',type:'C',cdata:${sel_data2},colwidth:80},
					 {columnid:'fdomain.data_name',columnname:'加工领域',type:'C',cdata:${sel_data3},colwidth:80},
					 {columnid:'fstandard.data_name',columnname:'资质标准',type:'C',cdata:${sel_data4},colwidth:80},
					 {columnid:'status.data_name',columnname:'状态',type:'C',cdata:${sel_data5},colwidth:50},
					],
			inputFormWidth:600,
			inputFormHeight:320
		});
    </script>
</head>
<body> 
</body>
</html>
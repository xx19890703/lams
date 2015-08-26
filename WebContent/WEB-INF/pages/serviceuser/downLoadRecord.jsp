<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>合同下载记录</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var cn=new Ext.grid.RowNumberer();
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"id",//关键字
			baseurl:$ctx+'/serviceuser/downLoadRecord',//基本url
			pagenum:5,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture type:"S"/"N"/"D"/"C":字符串、数字、时间、选择，默认S，C是必须设置cdata属性
			suuncolumns:[new Ext.grid.RowNumberer({header:"序号",width:40}),
		             {columnid:'id',hidden:true,columnname:'编号'},
                     {columnid:'contractid.did',columnname:'合同编号',type:'N',colwidth:80,defaultsort:true},
					 {columnid:'contractid.name',columnname:'合同名称',colwidth:80},
					 {columnid:'person',columnname:'下发人',type:'D',colwidth:80},
					 {columnid:'issuedTime',columnname:'下发时间',type:'D',colwidth:80},
					 {columnid:'importTime',columnname:'预计导入时间',type:'D',colwidth:80},
					// {columnid:'count',columnname:'下发次数',type:'D',colwidth:80},
					 {columnid:'state.data_name',columnname:'状态',type:'C',colwidth:80},
					],
			inputFormWidth:720,
			inputFormHeight:300
		});
    </script>
</head>
<body> 
</body>
</html>
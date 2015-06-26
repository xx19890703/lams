<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>信息查看</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"baseId",//关键字
			baseurl:$ctx+'/softline/softlineeInfomation',//基本url
			suuncolumns:[ //new Ext.grid.RowNumberer({header:"序号",width:40}),				 
			         {columnid:'baseId',columnname:'编号',type:'S',colwidth:30,defaultsort:true,issearch:true},
			         {columnid:'name',columnname:'姓名',colwidth:20,issort:true},
					 {columnid:'sex.data_name',columnname:'性别',type:'C',cdata:${sel_sex},colwidth:15,issort:false,issearch:false},
					 {columnid:'idcarNo',columnname:'身份证号码',colwidth:40,issort:false},
					 {columnid:'unitName',columnname:'单位名称',colwidth:50,issort:false},
					 {columnid:'phone',columnname:'联系电话',colwidth:30,issort:false,issearch:false},
					 {columnid:'addresss',columnname:'通讯地址',colwidth:80,issort:false},
					 {columnid:'systemData',columnname:'申请日期',type:'D',colwidth:80,issort:false},
					 {columnid:'slstate.data_name',columnname:'状态',type:'C',cdata:${sel_slstate },issort:true,colwidth:30}
					],
					inputFormWidth:800,
					inputFormHeight:500,
					operation:{
						add:{hidden:true},
						edit:{hidden:true},
						del:{hidden:true},
						exp:{hidden:true}
					},
					
		});
    </script>
</head>
<body> 
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>专家管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
    	createsuungrid({
			containerid:"contextPanel-<%=request.getParameter("suuntitle")%>",
			keyid:'proId',//关键字
			baseurl:$ctx+'/professor/professor',//基本url
			isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: true
			suuncolumns:[
				         {columnid:'proId',columnname:'编号',type:'S',issort:true,issearch:true,colwidth:5,defaultsort:true,isASC:true},
				         {columnid:'proName',columnname:'姓名',type:'S',issort:true,colwidth:5},
						 {columnid:'sex.data_name',columnname:'性别',type:'C',cdata:${sel_sex},colwidth:5,issort:true,issearch:false},
						 {columnid:'rank.data_name',columnname:'职称',type:'C',cdata:${sel_rank},issort:true,colwidth:5},
						 {columnid:'category',columnname:'鉴定科别',type:'S',issort:true,colwidth:5},
						 {columnid:'workAddress',columnname:'工作单位',type:'S',issort:true,colwidth:5,issearch:false}
						],
			inputFormWidth:610,
			inputFormHeight:330,
			operation:{add:{auth:'AUTH_PRO_ADD'},
		          edit:{auth:'AUTH_PRO_EDIT'},
				  del:{auth:'AUTH_PRO_DEL'},
				  exp:{auth:'AUTH_PRO_EXP'},
				  extend:[]
		    }
		});
    </script>
</head>
<body> 
</body>
</html>
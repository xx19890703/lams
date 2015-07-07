<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>评审管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridcrud.js"></script>
    <script type="text/javascript" charset="UTF-8">
		createsuungrid({
			containerid:'contextPanel-'+$tabtitle,
			keyid:"verdictId",//关键字
			baseurl:$ctx+'/softline/softlineeReview',  //基本url
			pagenum:15,//页记录数
			//isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture
			suuncolumns:[ 
                     {columnid:'verdictId',columnname:'verdictId',issearch:false,hidden:true},
			         {columnid:'verdictNo',columnname:'评审编号',colwidth:40,issort:true,issearch:true,defaultsort:true},
			         {columnid:'softlineeInfo.name',columnname:'姓名',issort:true,issearch:true,colwidth:40},
			         {columnid:'softlineeInfo.sex.data_name',columnname:'性别',type:'C',cdata:${sex_state},issort:true,issearch:true,colwidth:40},
			         {columnid:'invalidismGrades.data_name',columnname:'伤残等级',type:'C',cdata:${inv_state},issort:true,issearch:true,colwidth:40},
			         {columnid:'nursingGrades.data_name',columnname:'护理等级',type:'C',cdata:${nur_state},issort:true,issearch:true,colwidth:40},
			         {columnid:'reviewDate',columnname:'鉴定日期',type:'D',issort:true,issearch:true,colwidth:40},
			         {columnid:'softlineeInfo.slstate.data_name',columnname:'鉴定状态',type:'C',cdata:${sls_state},issort:true,issearch:false,colwidth:40}
						
			         ],
					inputFormWidth:720,
					inputFormHeight:540,
					operation:{add:{auth:'AUTH_SLR_ADD'},
				          edit:{auth:'AUTH_SLR_EDIT'},
						  del:{auth:'AUTH_SLR_DEL'},
						  exp:{auth:'AUTH_SLR_EXP'},
						  extend:[]
				    }
		});
    </script>
</head>
<body> 
</body>
</html>





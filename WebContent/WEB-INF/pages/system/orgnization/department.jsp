<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title>组织结构管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/treegridcrud.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
    <script type="text/javascript">
       createtreegrid({containerid: 'contextPanel-'+$tabtitle,
      	 baseurl:$ctx+"/system/orgnization/department",//基本url
      	 tree:{
		    title:'组织机构',
			allowEdit:true,
			inputFormWidth:280,
			inputFormHeight:260,
			operation:{add:{auth:'AUTH_DEPARTMENT_ADD'},
		          edit:{auth:'AUTH_DEPARTMENT_EDIT'},
				  del:{auth:'AUTH_DEPARTMENT_DEL'}
		    }
        },
        grid:{
		    title:'部门人员列表',
			simplemode:true,
			keyid:'employee.employeeid',//"id",//关键字 
			suuncolumns:[
			     {columnid:'employee.employeeid',columnname:'人员编号',colwidth:40,defaultsort:true},
	             {columnid:'employee.employeename',columnname:'人员姓名',colwidth:60} ],
	        operation:{add:{auth:'AUTH_DEPARTMENT_EDIT,AUTH_DEPARTMENT_ADD',
	        	            tooltip:'增加信息',
	        	            onClick:function(tree,grid){
					        	if (!tree.selModel.selNode) {
					    			Ext.Msg.alert('消息','请选择树节点！');
					    			return;
					    		}
					            gridselect({
		        	            	listurl:$ctx+'/system/orgnization/employee!selectEmployee',//基本url
		        	            	existurl:$ctx+"/system/orgnization/department!getDepEmployees.do?departmentid="+tree.selModel.selNode.attributes.id,
					                suuncolumns:[{columnid:'employeeid',columnname:'人员编号',colwidth:20,defaultsort:true},
							                     {columnid:'employeename',columnname:'人员姓名',colwidth:40},
							                     {columnid:'state.data_name',columnname:'状态',type:'C',cdata:[["使用"],["作废"]],colwidth:40}
							                    ],
							        multiSel:true,            
									callback:function(records){
									    var recordData=[]; 
										Ext.each(records,function(record){
											recordData.push(record.data);
										}); 
									    Ext.Ajax.request({
											url: $ctx+"/system/orgnization/department!saveEmployees",
											params: {departmentid:tree.selModel.selNode.attributes.id,employees:Ext.encode(recordData)},
											success: function(msg){
												grid.loadStore();      	    	
											},
											failure: function(){
												suunCore.hideMask();
												/*Ext.MessageBox.show({
													title : "错误",
													msg : "出错了！",
													icon : Ext.MessageBox.ERROR,
													buttons : Ext.Msg.OK
												});*/    	    	
											}
    	                                });  
									}
								});
	                        }
	        	            },
			          edit:{hidden:true},
					  del:{auth:'AUTH_DEPARTMENT_EDIT,AUTH_DEPARTMENT_ADD'},
					  exp:{hidden:true},
					  extend:[]
			}
     	 }
      });
    </script>
</head>
<body></body>
</html>
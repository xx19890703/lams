<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>	
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/treesel.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/treegridsel.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/doublegridsel.js"></script>
    <script  type="text/javascript">       
        function gridseldemo(me){		
			gridselect({listurl:$ctx+'/system/demo/demo!lists',//基本url
				pagenum:5,//页记录数 默认20
				//isprewidth:true,//是否宽度按百分比 默认true  列信息	hidden: true,locked: ture
				suuncolumns:[{columnid:'authId',columnname:'编号',colwidth:20,defaultsort:true},
						 {columnid:'displayName',columnname:'名称',colwidth:40},
						 {columnid:'state.data_name',columnname:'状态',colwidth:40}
						],
				//winWidth:500,默认值为500,350
				//winHeight:350,
				callback:function(records){
					me.value=records[0].get('displayName');
				}
			});
		}
		function treeseldemo(me){		
			treeselect({listurl:$ctx+'/system/demo/demotree!lists',//基本url
				//winWidth:500,默认值为500,350
				//winHeight:350,
				callback:function(nodes){
					me.value=nodes[0].attributes.text;
				}
			});
		}
		function treegridseldemo(me){		
			treegridselect({baseurl:$ctx+"/system/demo/demotreegrid",//基本url
				tree:{
					//listurl::$ctx+"/system/demo/demotreegrid!treelists',
					width:'30%'//默认40%
				},
				grid:{
				    //listurl::$ctx+"/system/demo/demotreegrid!gridlists',
					pagenum:5,//页记录数 默认20
					//isprewidth:true,//是否宽度按百分比 默认true 列信息	hidden: true,locked: true,,state.key.data_no
					suuncolumns:[{columnid:'did',columnname:'编号',colwidth:20,defaultsort:true},
						 {columnid:'dname',columnname:'名称',colwidth:40},
						 {columnid:'ddemo',columnname:'日期',colwidth:40}  ]
				},
				//winWidth:500,默认值为600,400
				//winHeight:350,
				callback:function(records){
					me.value=records[0].get('dname');
				}
			});
		}
		function doublegridseldemo(me){		
			doublegridselect({
				baseurl:$ctx+"/system/demo/demodoublegrid",//基本url
				vertical:false,//垂直排列 默认true
				grid1:{
					keyid:"mid",//关键字
				    pagenum:5,//页记录数 默认20
					width:'50%',//默认40%vertical=false起作用
					height:'50%',//默认40% vertical=true起作用
					//listurl::$ctx+"/system/demo/demodoublegrid!grid1lists',
				    //isprewidth:true,//是否宽度按百分比 默认true  列信息	hidden: true,locked: true,,state.key.data_no
				    suuncolumns:[{columnid:'mid',columnname:'部门编号',colwidth:20,defaultsort:true},
						 {columnid:'mname',columnname:'部门名称',colwidth:80}]					
				},
				grid2:{
				    //listurl::$ctx+"/system/demo/demodoublegrid!grid2lists',
					pagenum:5,//页记录数 默认20
					//isprewidth:true,//是否宽度按百分比 默认true 列信息	hidden: true,locked: true,,state.key.data_no
					suuncolumns:[{columnid:'did',columnname:'编号',colwidth:20,defaultsort:true},
						 {columnid:'dname',columnname:'名称',colwidth:40},
						 {columnid:'ddemo',columnname:'日期',colwidth:40}  ]
				},
				winWidth:870,//默认值为600,400
				winHeight:500,
				callback:function(records){
					me.value=records[0].get('dname');
				}
			});
		}
     </script>
</head>    
<body>
  <input type="hidden" name="mid" value="${parentid}"/>
  <table>
	<tr>
		<td>grid名称  </td>
		<td><input type="text" class="dataa" onclick="gridseldemo(this)"/></td>
	</tr>	
	<tr>
		<td>tree名称  </td>
		<td><input type="text" class="dataa" onclick="treeseldemo(this)"/></td>
	</tr>	
	<tr>
		<td>treegrid名称  </td>
		<td><input type="text" class="dataa" onclick="treegridseldemo(this)"/></td>
	</tr>
	<tr>
		<td>doublegrid名称  </td>
		<td><input type="text" class="dataa" onclick="doublegridseldemo(this)"/></td>
	</tr>
  </table>
</body>
</html>
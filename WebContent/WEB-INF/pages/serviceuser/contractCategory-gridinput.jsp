<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	   <title>合同信息配置</title>
       <link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
	   <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"/>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/detail.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/treegridsel.js"></script>
		<script>
			$("form:first").validate({
				rules: { 
					did: { 
	        			required: true, 
	        			maxlength: 30,
	        			remote: {url:'${ctx}/system/developer/menu!validateFunId',
							     type:'post',
							     data:{oldid:'${contractdetail.did}',id:$("input[name=d]")}
				        }
	        		 } ,
	        		 name: { 
	         			required: true,
	         			maxlength: 20,
	         			remote: {url:'${ctx}/system/developer/menu!validateFunId',
						     type:'post',
						     data:{oldname:'${contractdetail.name}',name:'${treeid}'}
			        	}
	         		 } ,
	        		 'finfo.fno':  { 
	        			 required: true
	        		 }	
			    },
			 	messages: {
			 		did: { 
	        			required: "合同编号不能为空！", 
	        			maxlength: "合同编号的长度不能超过30！",
	        			remote: '合同编号已存在！'
	        		 } ,
	        		 name: { 
	         			required:  "合同名称不能为空！", 
	        			maxlength: "合同名称的长度不能超过20！",
	        			remote: '合同编号已存在！'
	         		 } ,
	        		 'finfo.fno':  { 
	        			 required:  "制造厂信息不能为空！"
	        		 }	
				}
			}); 
			
			SuunCalendar.iniCalendar();
	        function setupDateTime(me){
	        	SuunCalendar.show(me,{showsTime: true,ifFormat: "%Y-%m-%d %H:%M:%S"});
	        }
	        
			function selectfactory(me){		
				gridselect({listurl:$ctx+'/serviceuser/factoryInfo!lists?type=sel',//基本url
					suuncolumns:[{columnid:'fno',columnname:'制造厂编号',colwidth:20,defaultsort:true},
							 {columnid:'fname',columnname:'制造厂名称',colwidth:40}
							],
					callback:function(records){
						me.value=records[0].get('fname');
						document.getElementById("fno").value=records[0].get('fno');
					}
				});
			}
        
	        function showReport(url,type){
	        	new Ext.Window({
	    			title : '报表展现',
	    			width : 800,
	    			height : 600,
	    			layout : 'fit',
	    			plain : true,
	    			closeAction : 'close',
	    			modal: true,
	    			bodyStyle : 'background-color:white;padding:0px;',
	    			buttonAlign : 'center',
	    			html: '<iframe style="background-color:white;overflow:auto;width:100%; height:100%;" src="${ctx}/ReportServer?reportlet='+url+'&op='+type+'" frameborder="0"></iframe>',
	    		}).show(); 
	        }
        
	        function selecttemp(me){		
	        	treegridselect({baseurl:$ctx+"/serviceuser/templateRes",//基本url
					tree:{
						width:'30%'//默认40%
					},
					grid:{
						pagenum:10,//页记录数 默认20
						suuncolumns:[{columnid:'did',columnname:'编号',colwidth:20,defaultsort:true},
							 {columnid:'name',columnname:'名称',colwidth:40},
							 {columnid:'path',columnname:'模版文件',colwidth:40}  ]
					},
					winWidth:650,
					winHeight:350,
					callback:function(records){
						me.value=records[0].get('name');
						$(me).parent().next().val(records[0].get('did'));
					}
				});
			}
	        $(document).ready(function(){
        		$.SuunRept({jsonbean:${jsonbean},beanname:"rescontent",detailnames:"id,name,template.name,template.did,openType,description,condetail.did,state.key.data_no",required:true});
	        });
        </script>
	</head>
	<body>
	    <input type="hidden" name="conmain.did" value="${treeid}" />
		<table align="center">
		<br/>
			<tr>
				<td width="80" align="right">合同编号</td>					
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
		                <input type="text" name="did"  value="${contractdetail.did}" style="width:180px;"/>
		                <input type="hidden" name="importCount"  value="${contractdetail.importCount }"/>
		                <input type="hidden" name="importTime"  value="${contractdetail.importTime }"/>
		                <input type="hidden" name="status.key.data_no"  value="${contractdetail.status.key.data_no }"/>
		                <input type="hidden" name="orderinfo"  value="${contractdetail.orderinfo}"/>
		            </c:when><c:otherwise>
		                <input type="text" name="did"  value="" style="width:180px;"/>
		                <input type="hidden" name="status.key.data_no"  value="A"/>
		            </c:otherwise></c:choose>
		        </td>
				<td width="80" align="right">合同名称</td>
				<td width="240"><input  type="text" name="name"  value="${contractdetail.name}" style="width:180px;"/></td>
			</tr>
			<tr>
				<td width="80" align="right">制造厂信息</td>
				<td width="240">
					<input type="hidden" id="fno" name="finfo.fno" value="${contractdetail.finfo.fno}" />
					<input class="dataa" type="text" id="fname" name="finfo.fname" style="font-family:arial;width:180px"  value="${contractdetail.finfo.fname}" onclick="selectfactory(this)" />
				</td>
			    <td  align="right">状态</td>
				<td >     
					<ui:selectlist cssStyle="width:180px;" name="state.key.data_no" lists="${status}" listValue="key.data_no" listTitle="data_name" 
	            		checkValue="${contractdetail.state.key.data_no}" defaultCheckValue="1"/>
		        </td>
			</tr>
			<!-- <tr>
				<td width="100" align="right">导出时间</td>
				<td width="240"> 
					<input class="datew" style="width:180px;" name="importTime" type="text" onclick="JavaScript:setupDateTime(this);" value="${contractdetail.importTime}"/>
           	 	</td>
				<td width="100" align="right">预计导入时间</td>
				<td width="240">
					<input class="datew" style="width:180px;" name="planImportTime" type="text" onclick="JavaScript:setupDateTime(this);" value="${contractdetail.planImportTime}"/>
				</td>
			</tr>
			<tr>
				<td width="100" align="right">所属订单</td>
				<td width="240"><input  type="text" name="orderinfo"  value="${contractdetail.orderinfo}" style="width:180px;"/></td>
			</tr>-->
		</table>
		<br>
	  <div style="margin-left: 40px;position:relative;overflow:auto; width:570px;height: 230px">
		  <table class="suunRept" width="100%" border="1" cellpadding="0" cellspacing="0"> 
		      <tr>     
		          <td align="center">编号</td>    
				  <td align="center">报表名称</td>
				  <td align="center">报表浏览方式</td>
				  <td>预览</td>
		      </tr>
			      <tr class="items">         
			      	<input type="hidden" name="resdetail.did" value="${condetail.did}"/>
					   	<td align="center"><input type="text" name="id" class="sysindex" style="width:40px;"/></td>    
						<td align="center"><input type="text" class="dataa" style="width:200px;" onclick="selecttemp(this);" name="name" class="{required:true,messages:{required:'第【】行报表不能为空！'}}"/><input type="hidden" name="template.did" /></td>
			      		<td align="center"><input type="text" style="width:80px;" value="view" name="openType" class="{required:true,messages:{required:'第【】行报表打开方式不能为空！'}}"/></td>
			      		<td align="center"><input type="button" value="预览" onclick="showReport('${fn:replace(item.template.path,"\\", "/")}','${item.openType}')"></td>
			      </tr>
		  </table>
	  </div>
	</body>
</html>
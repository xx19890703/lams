<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	   <title>合同信息配置</title>
	   <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
       <link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"/>
	    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/detail.js"></script>
		<script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
		<script>
			function selectfactory(me){		
				gridselect({listurl:$ctx+'/serviceuser/factoryInfo!lists',//基本url
					suuncolumns:[{columnid:'fno',columnname:'制造厂编号',colwidth:20,defaultsort:true},
							 {columnid:'fname',columnname:'制造厂名称',colwidth:40}
							],
					callback:function(records){
						me.value=records[0].get('fname');
						document.getElementById("fno").value=records[0].get('fno');
					}
				});
			}
		
		SuunCalendar.iniCalendar();
        function setupDateTime(me){
        	SuunCalendar.show(me,{showsTime: true,ifFormat: "%Y-%m-%d %H:%M:%S"});
        }
        
        function showReport(url){
        	new Ext.Window({
    			title : '报表展现',
    			width : 800,
    			height : 600,
    			layout : 'fit',
    			plain : true,
    			closeAction : 'close',
    			modal: true,
    			bodyStyle : 'padding:0px;',
    			buttonAlign : 'center',
    			html: '<iframe style="overflow:auto;width:100%; height:100%;" src="" frameborder="0"></iframe>',
    		}).show(); 
        }
        </script>
	</head>
	<body>
	    <input type="hidden" name="resmain.did" value="${treeid}" />
		<table align="center">
		<br/>
			<tr>
				<td width="100" align="right">合同编号</td>					
				<td width="240">
				    <c:choose><c:when test="${isEdit}">
				    	 <input  type="hidden" name="conmain.did"  value="${contractdetail.conmain.did}" style="width:174px;"/>
		                <input  type="text" name="did"  value="${contractdetail.did}" style="width:174px;"/>
		            </c:when><c:otherwise>
		                <input  type="text" name="did"  value="" style="width:174px;"/>
		            </c:otherwise></c:choose>
		        </td>
				<td width="100" align="right">合同名称</td>
				<td width="240"><input  type="text" name="name"  value="${contractdetail.name}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="100" align="right">制造厂信息</td>
				<td width="240">
					<input type="hidden" id="fno" name="finfo.fno" value="${contractdetail.finfo.fno}" />
					<input class="dataa" type="text" id="fname" name="finfo.fname" style="font-family:arial;width:174px"  value="${contractdetail.finfo.fname}" onclick="selectfactory(this)" />
				</td>
				<td width="100" align="right">所属订单</td>
				<td width="240"><input  type="text" name="orderinfo"  value="${contractdetail.orderinfo}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="100" align="right">导出时间</td>
				<td width="240"> 
					<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:174px;height:18px;cursor:pointer;text-align:left;"> 
                		<input name="importTime" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:158px;" onblur="validateTime(this)" value='<fmt:formatDate value="${contractdetail.importTime}" pattern="yyyy-MM-dd"/>'>
                		<img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            		</div>
           	 	</td>
				<td width="100" align="right">预计导入时间</td>
				<td width="240">
					<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:174px;height:18px;cursor:pointer;text-align:left;"> 
                		<input name="planImportTime" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:158px;" onblur="validateTime(this)" value='<fmt:formatDate value="${contractdetail.planImportTime}" pattern="yyyy-MM-dd"/>'>
                		<img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            		</div>
				</td>
			</tr>
			<tr>
			    <td  align="right">状态</td>
				<td >     
				 <input type="hidden" name="state.key.dic_no" value="STATE"/>
		        <select name="state.key.data_no" style="width:170;">
				<c:forEach var="mystatus" items="${status}">
					<c:choose >
						<c:when test="${isEdit&&mystatus.key.data_no==contractdetail.state.key.data_no}">
							<option selected value="${mystatus.key.data_no}">${mystatus.data_name}</option>
						</c:when>	  
		              	<c:otherwise>
		                 	<option value="${mystatus.key.data_no}">${mystatus.data_name}</option>
		              	</c:otherwise>
					</c:choose> 
		    	</c:forEach>
		    	</select>
		        </td>
			</tr>
		</table>
		<br>
	  <div style="position:relative;overflow:auto; width:650px;">
	  <table class="suunRept" width="100%" border="1" cellpadding="0" cellspacing="0"> 
	      <tr>     
	          <td align="center">编号</td>    
			  <td align="center">报表名称</td>
			  <td align="center">报表浏览方式</td>
			  <td align="center">报表模板</td>
			  <td>预览</td>
	      </tr>
	      <c:forEach var="item" items="${contractdetail.rescontent}" varStatus="status">
		      <tr class="items">         
				    <td align="center"><input type="text" name="rescontent[${status.index }].id" value="${item.id}"/></td>    
					<td align="center"><input type="text" name="rescontent[${status.index }].name" value="${item.name}"/></td>
		      		<td align="center"><input type="text" name="rescontent[${status.index }].openType" value="${item.openType}"/></td>
					<td align="center"><input type="text" name="rescontent[${status.index }].template.did" value="${item.template.did}"/></td>
		      		<td align="center"><input type="button" value="预览" onclick="showReport('${item.template.did}')"></td>
		      </tr>
	      </c:forEach>
	  </table>
	  </div>
	</body>
</html>
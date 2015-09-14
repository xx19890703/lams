<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>制造厂管理</title>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"/>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
    <!-- script和class两种校验均可 class校验不能换行 -->
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
	    $("form:first").validate({
			rules: { 
				fno: { 
	    			required: true, 
	    			maxlength: 30,
	    			remote: {url:'${ctx}/serviceuser/factoryInfo!checkfno',
						     type:'post',
						     data:{oldid:'${factoryinfo.fno}'}
			   		}
	    		 } ,
	    		 fname: { 
	     			required: true,
	     			maxlength: 30
	     		 } ,
	     		fregister:  { 
	    			 required: true,
	    			 maxlength: 60,
	    			 remote: {url:'${ctx}/serviceuser/factoryInfo!checkfregister',
					     type:'post',
					     data:{oldid:'${factoryinfo.fregister}'}
		        	}
	    		} ,
	    		ftel: {
	    			number:true,
	    			maxlength: 11
	    		},
	    		fcontecttel: {
	    			number:true,
	    			maxlength: 11
	    		}
		    },
		 	messages: {
		 		fno: { 
	    			required: "制造厂编号不能为空！", 
	    			maxlength: "制造厂编号长度不能超过30！",
	    			remote: "制造厂编号已存在！"
	    		 } ,
	    		 fname: { 
	     			required:  "制造厂名称不能为空！", 
	    			maxlength: "制造厂名称长度不能超过30！"
	     		 } ,
	     		 fregister:  { 
	    			 required:  "制造厂注册码不能为空！",
	    			 maxlength: "制造厂注册码长度不能超过60！",
	    			 remote: "制造厂注册码已存在！"
	    		 } ,
	    		 ftel: {
	    			 number: "请输入数字！",
	    			 maxlength:"电话号码不能超过11位！"
	    		 },
	    		 fcontecttel: {
	    			 number: "请输入数字！",
	    			 maxlength:"联系人电话不能超过11位！"
	    		 }
			}
		});
	    SuunCalendar.iniCalendar();
	    function setupDateTime(me){
	    	SuunCalendar.show(me,{ifFormat: "%Y-%m-%d %H:%M:%S",showsTime: true});
	    }
	    
	    
     </script>
      
</head>    
<body>
	<br>
	<table border="0" cellspacing="0" align="center">
		<tr>
			<td style="width: 80px;" align="right">制造厂编号  </td>
			<td>
			   <c:choose>		  
			     <c:when test="${isEdit}">
			         <input type="text" name="fno" style="width: 180px;" readonly="readonly" value="${factoryinfo.fno}" />
			      </c:when>		  
	              <c:otherwise>
	                 <input type="text" name="fno" style="width: 180px;" value="${factoryinfo.fno}" />                     
	              </c:otherwise>
	          </c:choose>        
	        </td>
	        <td style="width: 80px;" align="right">制造厂注册码 </td>
			<td>
	        	<input type="text" name="fregister" style="width: 180px;" value="${factoryinfo.fregister}"/>
	        </td>
		</tr>
		<tr>
			<td style="width: 80px;" align="right">制造厂名称  </td>
			<td>
				<input type="text" name="fname" style="width: 180px;" value="${factoryinfo.fname}"/>
	        </td>
	        <td style="width: 80px;" align="right">地址 </td>
			<td>
				<input type="text" name="faddress" style="width: 180px;" value="${factoryinfo.faddress}"/>
	        </td>
		</tr>
		<tr>
			<td style="width: 80px;" align="right">电话（传真） </td>
			<td>
				<input type="text" name="ftel" style="width: 180px;" value="${factoryinfo.ftel}"/>
	        </td>
	        <td style="width: 80px;" align="right">法人 </td>
			<td>
				<input type="text" name="fower" style="width: 180px;" value="${factoryinfo.fower}"/>
	        </td>
		</tr>
		<tr>
			<td style="width: 80px;" align="right">联系人  </td>
			<td>
				<input type="text" name="fcontect" style="width: 180px;" value="${factoryinfo.fcontect}"/>
	        </td>
	        <td style="width: 80px;" align="right">联系人电话 </td>
			<td>
				<input type="text" name="fcontecttel" style="width: 180px;" value="${factoryinfo.fcontecttel}"/>
	        </td>
		</tr>
		<tr>
			<td align="right">类别</td>
			<td>
	            <ui:selectlist cssStyle="width:180px;" name="ftype.key.data_no" lists="${allObjects1}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${factoryinfo.ftype.key.data_no}" defaultCheckValue="1"/>
			</td>
			<td align="right">等级</td>
			<td>
	            <ui:selectlist cssStyle="width:180px;" name="flevel.key.data_no" lists="${allObjects2}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${factoryinfo.flevel.key.data_no}" defaultCheckValue="1"/>
			</td>
		</tr>
		<tr>
			<td align="right">加工领域</td>
			<td>
	            <ui:selectlist cssStyle="width:180px;" name="fdomain.key.data_no" lists="${allObjects3}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${factoryinfo.fdomain.key.data_no}" defaultCheckValue="1"/>
			</td>
			<td align="right">资质标准</td>
			<td>
	            <ui:selectlist cssStyle="width:180px;" name="fstandard.key.data_no" lists="${allObjects4}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${factoryinfo.fstandard.key.data_no}" defaultCheckValue="1"/>
			</td>
		</tr>
		<tr>
			<td style="width: 80px;" align="right">登记时间  </td>
			<td>
				<input class="datew" type="text" onclick="JavaScript:setupDateTime(this);" name="ftime" style="width: 180px;" value="${factoryinfo.ftime}" readonly="readonly"/>
	        </td>
	        <td style="width: 80px;" align="right">状态  </td>
			<td>
			<input type="hidden" name="fattachment" style="width: 180px;" value="${factoryinfo.fattachment}"/>
	            <ui:selectlist cssStyle="width:180px;" name="status.key.data_no" lists="${allObjects5}" listValue="key.data_no" listTitle="data_name" 
	            	checkValue="${factoryinfo.status.key.data_no}" defaultCheckValue="1"/>
			</td>
		</tr>
		<tr>
			
			<td style="width: 80px;" align="right">备注</td>
			<td colspan="3">
				<textarea style="margin-left: 0px;" name="remark" rows="2" cols="72">${factoryinfo.remark}</textarea>
	        </td>
		</tr>
	</table>
</body>
</html>
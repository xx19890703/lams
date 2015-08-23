<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>合同下载记录</title>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"/>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
    <!-- script和class两种校验均可 class校验不能换行 -->
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
   	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/treegridsel.js"></script>
    <script>
	    $("form:first").validate({
			rules: { 
				fno: { 
	    			required: true, 
	    			maxlength: 30,
	    			remote: {url:'${ctx}/serviceuser/downloadrecord!check',
						     type:'post',
						     data:{oldid:'${downloadrecord.id}'}
			        }
	    		 } ,
	    		 fname: { 
	     			required: true,
	     			maxlength: 30
	     		 } ,
	     		fregister:  { 
	    			 required: true,
	    			 maxlength: 60,
	    			 remote: {url:'${ctx}/serviceuser/downloadrecord!checkfregister',
					     type:'post',
					     data:{oldid:'${downloadrecord.id}'}
		        }
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
	    		 }	
			}
		});
	    
	    SuunCalendar.iniCalendar();
        function setupDateTime(me){
        	SuunCalendar.show(me,{showsTime: true,ifFormat: "%Y-%m-%d %H:%M:%S"});
        }
	    
	    function selectContract(){		
        	treegridselect({baseurl:$ctx+"/serviceuser/contractCategory",//基本url
				tree:{
					width:'30%'//默认40%
				},
				grid:{
					pagenum:10,//页记录数 默认20
					suuncolumns:[{columnid:'did',columnname:'编号',colwidth:20,defaultsort:true},
						 {columnid:'name',columnname:'合同名称',colwidth:40},
						 {columnid:'status.data_name',columnname:'合同状态',colwidth:40}  ]
				},
				winWidth:650,
				winHeight:350,
				callback:function(records){
					$("input[name='contractid.did']").val(records[0].get('did'));
					$("input[name='contractid.name']").val(records[0].get('name'));
				}
			});
		}
     </script>
      
</head>    
<body>
	<br>
	<table border="0" cellspacing="0" align="center">
		<tr>
			<td>
			   <c:choose>		  
			     <c:when test="${isEdit}">
			         <input type="hidden" name="id" style="width: 180px;" readonly="readonly" value="${downloadrecord.id}" />
			      </c:when>		  
	              <c:otherwise>
	                 <input type="hidden" name="id" style="width: 180px;" value="${downloadrecord.id}" />                     
	              </c:otherwise>
	          </c:choose>        
	        </td>
		</tr>
		<tr>
	        <td style="width: 80px;" align="right">下发合同编号</td>
			<td>
	        	<input type="text" class="dataa" name="contractid.did" style="width: 180px;" readonly="readonly" value="${downloadrecord.contractid.did}" onclick="selectContract();"/>
	        </td>
			<td style="width: 80px;" align="right">下发合同名称  </td>
			<td>
				<input type="text" name="contractid.name" style="width: 180px;" readonly="readonly" value="${downloadrecord.contractid.name}"/>
	        </td>
		</tr>
		<tr>
	        <td style="width: 80px;" align="right">下发人 </td>
			<td>
				<input type="text" name="person" style="width: 180px;" value="${downloadrecord.person}"/>
	        </td>
			<td style="width: 80px;" align="right">下发时间 </td>
			<td>
				<input class="datew" style="width:180px;" name="issuedTime" type="text" onclick="JavaScript:setupDateTime(this);" value="${downloadrecord.issuedTime}"/>
	        </td>
		</tr>
		<tr>
	        <td style="width: 80px;" align="right">审核人</td>
			<td>
				<input type="text" name="checekPerson" style="width: 180px;" value="${downloadrecord.checekPerson}"/>
	        </td>
	        <td style="width: 80px;" align="right">审核时间 </td>
			<td>
				<input type="text" name="checkTime" style="width: 180px;" value="${downloadrecord.checkTime}"/>
	        </td>
		</tr>
		<tr>
	        <td style="width: 80px;" align="right">预计导入时间 </td>
			<td>
				<input class="datew" style="width:180px;" name="importTime" type="text" onclick="JavaScript:setupDateTime(this);" value="${downloadrecord.importTime}"/>
	        </td>
			<td style="width: 80px;" align="right">备注</td>
			<td>
				<textarea rows="3" cols="200" name="remark" style="width: 180px;">${downloadrecord.remark}</textarea>
	        </td>
		</tr>
	</table>
</body>
</html>
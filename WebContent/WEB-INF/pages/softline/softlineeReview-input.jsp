<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>授权管理</title>
<link rel="stylesheet" type="text/css" media="all"
	href="${ctx}/resources/js/thrid/calendar/calendar-blue.css" />
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js" />
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
<!-- script和class两种校验均可 class校验不能换行 -->
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/system/core/ux/FileUploadField.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/system/core/gridsel.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/system/core/detail.js"></script>
<script>
	    function gridselsoftlinee(me){		
			gridselect({listurl:$ctx+'/softline/softlineeReview!listsForSl',//基本url
				suuncolumns:[{columnid:'baseId',columnname:'编号',colwidth:25,defaultsort:true},
						 {columnid:'name',columnname:'姓名',colwidth:40},
						 {columnid:'gender',columnname:'性别',colwidth:20},
						 {columnid:'idcarNo',columnname:'身份证号',colwidth:90},
						 {columnid:'phone',columnname:'电话',colwidth:80},
						 {columnid:'slstate.data_name',columnname:'状态',colwidth:40},
						 {columnid:'mustCheck',columnname:'准查科目',hidden:true,colwidth:40}
						],
				winWidth:600,
				winHeight:400,
				callback:function(records){
					me.value=records[0].get('name');
					$("#softlineeInfo\\.baseId")[0].value=records[0].get('baseId');
						document.getElementById("invalidismCase").value=records[0].get('mustCheck');
				}
			});
			
		}
	    function gridselrp(me){	
	    	var id=me.name.substring(0,me.name.length-7)+"proId";
	    	var sid='';
	    	for(i=0;i<$(".sysindex").size();i++){
	    		if (id!="accProfessionals["+i+"].proId"){
	    			var v=document.getElementById("accProfessionals["+i+"].proId").value;
		    		if (v!=""){
		    			sid=sid+v+';';
		    		}
	    		}	    		
	    	}
			gridselect({listurl:$ctx+'/softline/softlineeReview!listsForSlr?ids='+sid,//基本url
				suuncolumns:[{columnid:'proId',columnname:'编号',colwidth:2,defaultsort:true},
						 {columnid:'proName',columnname:'姓名',colwidth:3},
						 {columnid:'sex.data_name',columnname:'性别',colwidth:1},
						 {columnid:'rank.data_name',columnname:'职称',colwidth:3},
						 {columnid:'category',columnname:'鉴定科别',colwidth:4},
						],
				callback:function(records){
					me.value=records[0].get('proName');					
					document.getElementById(id).value=records[0].get('proId');
					var ranki=me.name.substring(0,me.name.length-7)+"rank.data_name";
					document.getElementById(ranki).value=records[0].get('rank.data_name');
					var categoryi=me.name.substring(0,me.name.length-7)+"category";
					document.getElementById(categoryi).value=records[0].get('category');
				}
			});
		}
	  
	 $.validator.addMethod("isverdictNo", function(value) {
		
	    	  var length = value.length;
	    	  if(value.length<4) return false;
	    	  var verdictNo =  /^[0-9][0-9][0-9][0-9]$/;
	    	  return verdictNo.test(value);
	    	 }, "评审编号必须为4位数字！");  
       $("#SuunWinForm").validate({
			 rules: { 
				 verdictNo: { 
        			required: true, 
        			isverdictNo:true
    				}
				},
			messages: {
				verdictNo: {
					required:'评审编号不能为空！'
					}
				}
		});
       jQuery("#reviewDate").suunDateTime();

       //明细编辑
	     $(top.document).ready(function(){
	         $.SuunRept({jsonbean:${jsonbean},beanname:"accProfessionals",detailnames:"proId,proName,category,rank.data_name",required:true});
	     }); 
	     SuunCalendar.iniCalendar();
	     function setupDateTime(me){
	     	SuunCalendar.show(me);
	     }
	     function sss(me){
	     	 setupDateTime(me.previousElementSibling);
	     }
	     function validateTime(object){
	    	 if(!object.value) return;
	       	 var array = object.value.split("-");
	       	 if (array[0].length!=4) {
	       		 Ext.Msg.alert('提示','格式不正确，日期格式为****-**-**');
	    	    	 object.value="";
	    	         return false;
	    	       }
	       	var D = new Date(array[0]+"/"+array[1]+"/"+array[2]);
	       	var B = D.getFullYear()==array[0]&&(D.getMonth()+1)==array[1]&&D.getDate()==array[2]; 
	       	if (!B) {
	       		Ext.Msg.alert('提示','格式不正确，日期格式为****-**-**');
	       		object.value="";
	               return false;
	           }else{
	        	   if(array[1].length==1){
	        		   array[1]="0"+array[1];
	        	   }
	        	   if(array[2].length==1){
	        		   array[2]="0"+array[2];
	        	   }
	           		object.value= array[0]+"-"+array[1]+"-"+array[2];
	           }
	        }
     </script>

</head>
<body>
	<br />

	<table align="center">
		<tr>
			<td align="right" style="width:130px">评审单申请人</td>
			<input type="hidden" id="softlineeInfo.baseId"
				name="softlineeInfo.baseId"
				value="${softlineeReview.softlineeInfo.baseId}">
				<td><input style="width:170px" type="text" name="softlineeInfo.name"					
					value="${softlineeReview.softlineeInfo.name}" 
					<c:choose><c:when test="${isEdit}"> disabled="disabled"	</c:when>	  
	                <c:otherwise> onclick="gridselsoftlinee(this)" class="dataa {required:true,messages:{required:'申请人不能为空！'}}" </c:otherwise></c:choose> /></td>
				<td align="right" style="width:95px">评审编号</td>
				<td><input type="hidden" name="verdictId"
					value="${softlineeReview.verdictId}" /> <input type="text" style="width:170px"
					name="verdictNo" value="${softlineeReview.verdictNo}" onKeyUp="if(this.value.length>4){this.value=this.value.substr(0,4)};this.value=this.value.replace(/[^\d]/g,'');" /></td>
		</tr>

		<tr>
			<td align="right">伤残等级</td>
			<td><ui:selectlist cssStyle="width:170px;"
					name="invalidismGrades.key.data_no" lists="${allObjects}"
					listValue="key.data_no" listTitle="data_name"
					checkValue="${softlineeReview.invalidismGrades.key.data_no}"
					defaultCheckValue="0" /></td>
			<td align="right"><div align="right">护理等级</div></td>
			<td><ui:selectlist cssStyle="width:170px;"
					name="nursingGrades.key.data_no" lists="${allObjects1}"
					listValue="key.data_no" listTitle="data_name"
					checkValue="${softlineeReview.nursingGrades.key.data_no}"
					defaultCheckValue="0" /></td>
		</tr>
		<tr>
			<td align="right">评审依据</td>
			<td colspan="3"><textarea name="reviewGist"
					style="height:30px;width: 500px;" maxlength="250">${softlineeReview.reviewGist}</textarea></td>
		</tr>
		<tr>
			<td align="right">伤残情况</td>
			<td colspan="3"><textarea id="invalidismCase" name="invalidismCase"
					style="height:30px;width: 500px;" maxlength="250">${softlineeReview.invalidismCase}</textarea></td>
		</tr>
		<tr>
			<td align="right">延长依据</td>
			<td colspan="3"><textarea name="extensionGist"
					style="height:30px;width: 500px;" maxlength="250">${softlineeReview.extensionGist}</textarea></td>
		</tr>
		<tr>
			<td align="right">延长时间</td>
			<td align="left">
				<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
	                <input name="extensionTimeStart" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlineeReview.extensionTimeStart}" pattern="yyyy-MM-dd"/>'>
	                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            	</div>至
            	<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
	                <input name="extensionTimeEnd" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlineeReview.extensionTimeEnd}" pattern="yyyy-MM-dd"/>'>
	                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            	</div>
        	</td>
			</td>
			<td align="right"><div align="right">康复时间</div></td>
				<td align="left">
				<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
	                <input name="recoveryTimeStart" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlineeReview.recoveryTimeStart}" pattern="yyyy-MM-dd"/>'>
	                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            	</div>至
            	<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
	                <input name="recoveryTimeEnd" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlineeReview.recoveryTimeEnd}" pattern="yyyy-MM-dd"/>'>
	                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            	</div>
        	</td>
		</tr>
		<tr>
			<td align="right">辅助器具</td>
			<td colspan="3"><textarea name="helpers" style="height:20px;width: 500px;" maxlength="250">${softlineeReview.helpers}</textarea></td>
		</tr>
		<tr>
			<td align="right">疾病关联</td>
			<td colspan="3"><textarea name="disAssociation"
					style="height:20px;width: 500px;" maxlength="250">${softlineeReview.disAssociation}</textarea></td>
		</tr>
		<tr>
			<td align="right">备注</td>
			<td colspan="3"><textarea name="remark" style="height:20px;width: 500px;" maxlength="250">${softlineeReview.remark}</textarea></td>
		</tr>
		<tr>
			<td align="right">鉴定日期</td>
			<td align="left">
				<div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
	                <input name="reviewDate" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlineeReview.reviewDate}" pattern="yyyy-MM-dd"/>'>
	                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            	</div>
        </div><font color=red>例如：1988-2-3</font></td>
			<td align="right">鉴定状态</td>
		    <td> <c:choose><c:when test="${isEdit}">
						 <ui:selectlist cssStyle="width:170px;"
						name="softlineeInfo.slstate.key.data_no" lists="${slstate}"
						listValue="key.data_no" listTitle="data_name"
						checkValue="${softlineeReview.softlineeInfo.slstate.key.data_no}"
						/>
					</c:when>	  
	            <c:otherwise>       
	                <select  name="softlineeInfo.slstate.key.data_no"  style="width: 170px"><option value="1" >鉴定通过</option><option value="3" >鉴定未通过</option></select>
	            </c:otherwise></c:choose>
		</td>
		</tr>
		<tr>
			<td align="right">专家组成员</td>
			<td colspan="3" rowspan="2"><div align="center"
					style="position: relative; overflow: auto; width: 500px;height:140px;">
					<table class="suunRept" width="100%" border="0" border="0"
						cellpadding="0" cellspacing="0">
						<tr>
							<td align="center">姓名</td>
							<td align="center">科别</td>
							<td align="center">职称</td>
						</tr>
						<tr class="items">
								<input type="hidden" id="proId" name="proId" >
								<td align="center"><input type="text" style="width:140px;"
									name="proName" onclick="gridselrp(this)" class="dataa" /></td>
								<td align="center"><input type="text" id="category"  readonly="readonly" style="width:140px;"
									name="category" /></td>
								<td align="center"><input type="text" id="rank.data_name" readonly="readonly" style="width:140px;"
									name="rank.data_name" /></td>
						</tr>
					</table>
				</div></td>
		<tr>
			<td align="right">&nbsp;</td>		
		</tr>		
	</table>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"/>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
    <!-- script和class两种校验均可 class校验不能换行 -->
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
    <script>
        $.validator.addMethod("buga", function(value) {
  		    return value == "buga";
  	     }, 'Please enter "buga"!');
        $("#SuunWinForm").validate({
			 rules: { 
				 authId: { 
        			required: true, 
        			remote: {url:'${ctx}/system/demo/demo/validateAuthority',
        				     type:'post',
        				     data:{oldid:'${demo.authId}'}
        			}
    			},
    			displayName:{
    				required:true,
    				buga:true
    			}            	
			},
			messages: {
				authId: {
					required:'授权编号不能为空！',
					remote:'授权编号已存在！'
				},
				displayName: {
					required: "授权名称不能为空！"
				}
			}
		}); 
       //class="{required:true,buga:true,messages:{required:'授权名称不能为空！'}}"
         //class="{required:true,remote:{url:'${ctx}/security/authority/checkauthority',type:'post',data:{oldid:'${authority.authId}'}},messages:{required:'授权编号不能为空！',remote:'授权编号已存在！'}}"
   jQuery("#demo2").suunDateTime({
							showsTime: true,
							ifFormat: "%Y/%m/%d-%H:%M",
							daFormat: "%l;%M %p, %e %m,  %Y",
							//align: "TL",
							electric: false,
							singleClick: true,
							step           :    1,
							button: ".next()" //next sibling
						});      	
/*     Calendar.setup({
        inputField     :    "demo",      // id of the input field
        ifFormat       :    "%m/%d/%Y %I:%M %p",       // format of the input field
        showsTime      :    true,            // will display a time selector
        button         :    "f_trigger_b",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    }); */
    jQuery("#txt1").suunDateTime();//此方法会新建一个Calendar，不是整个页面共用一个，最好用以下方法
    
    SuunCalendar.iniCalendar();
    function setupDateTime(me){
    	SuunCalendar.show(me,{ifFormat: "%Y-%m-%d %H:%M:%S",showsTime: true});
    }
     </script>
      
</head>    
<body>
<table>
	<tr>
		<td>授权编号  </td>
		<td>
		   <c:choose>		  
		     <c:when test="${isEdit}">
		         ${demo.authId}<input type="hidden" name="authId" style="width:174px;" value="${demo.authId}" />
		      </c:when>		  
              <c:otherwise>
                 <input type="text" name="authId" style="width:174px" value="${demo.authId}" />                     
                   <!--  class="{required:true,remote:{url:'${ctx}/developer/demo!checkauthority',type:'post',data:{oldid:'${demo.authId}'}},messages:{required:'授权编号不能为空！',remote:'授权编号已存在！'}}" -->
              </c:otherwise>
          </c:choose>        
        </td>
	</tr>
	<tr>
		<td>授权名称  </td>
		<td><select  id="shcategorys1" name="categoryId1" style="font-family:arial;width:130px"  >
              <option value="0">--请选择类别--</option>  
             </select><input type="text" name="displayName" size="10" value="${demo.displayName}" 
		    class="{buga:true,required:true,messages:{required:'授权名称不能为空！'}}" /></td>
	</tr>
	<tr>
		<td>使用状态  </td>
		<td>
		   <select id="state.key.data_no" name="state.key.data_no" style="font-family:arial;width:130px"  >
              <option value="0" <c:choose><c:when test="${demo.state.key.data_no==0}">selected="true"</c:when></c:choose> >作废</option>  
              <option value="1" <c:choose><c:when test="${demo.state.key.data_no==1}">selected="true"</c:when></c:choose> >使用</option>
             </select>   
        </td>
	</tr>
	<tr>
		<td>授权名称  </td>
		<td>
		<span style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:128px;height:18px;cursor:pointer;text-align:left;">
		    <input type="text" style="display:block;float:left; border: 0px;height:17px;width:112px;" name="demo2" id="demo2" value="${demo.displayName}" />
	        <img src="${ctx}/resources/images/demo/calendar.gif" id="f_trigger_b" style="padding-top:1px;vertical-align:middle;cursor: pointer;" title="日期选择" >
	    </span>
	</td>
	</tr>
	<tr>
		<td>select</td>
		<td><select  id="shcategorys" name="categoryId" style="font-family:arial;width:174px"  >
              <option value="0">--请选择类别--</option>  
			  <option value="1" checked="true">测试</option>
             </select>
	</td>	
	</tr>
	<tr>
		<td>select</td>
		<td><input class="datew" name="demodate" type="text" onclick="JavaScript:setupDateTime(this);" value="${demo.demodate}"/></td>
	</tr>
	<tr>
		<td>select</td>
		<td><input class="datew" type="text" id="txt1"/></td>
	</tr>
	
</table>
</body>
</html>
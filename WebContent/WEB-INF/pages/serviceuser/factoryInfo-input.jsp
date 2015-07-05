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
   jQuery("#factoryinfo2").suunDateTime({
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
        inputField     :    "factoryinfo",      // id of the input field
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
			<td>制造厂编号  </td>
			<td>
			   <c:choose>		  
			     <c:when test="${isEdit}">
			         ${factoryinfo.fno}<input type="hidden" name="fno" style="width:174px;" value="${factoryinfo.fno}" />
			      </c:when>		  
	              <c:otherwise>
	                 <input type="text" name="fno" style="width:174px" value="${factoryinfo.fno}" />                     
	              </c:otherwise>
	          </c:choose>        
	        </td>
	        <td>制造厂注册码 </td>
			<td>
	        	<input type="text" name="fregister" size="10" value="${factoryinfo.fregister}"/>
	        </td>
		</tr>
		<tr>
			<td>名称  </td>
			<td>
				<input type="text" name="fname" size="10" value="${factoryinfo.fname}"/>
	        </td>
	        <td>地址 </td>
			<td>
				<input type="text" name="faddress" size="10" value="${factoryinfo.faddress}"/>
	        </td>
		</tr>
	</table>
</body>
</html>
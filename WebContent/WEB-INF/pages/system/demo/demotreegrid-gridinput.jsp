<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/resources/js/thrid/calendar/calendar-blue.css"  />
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/jquery.calendar.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/calendar/calendar-cn-utf8.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
     <!-- script和class两种校验均可 class校验不能换行 -->
     <script>
        $("#SuunWinForm").validate({
			 rules: { 
				 did: { 
        			required:true,
					remote:{url:'${ctx}/system/demo/demotreegrid!validateAuthorityDetail',
					        type:'post',
					        data:{olddid:'${demomaindetail.did}',mid:'${treeid}'}
						   }
    			},
    			dname:{
    				required:true
    			}             	
			},
			messages: {
				did: {
					required:'编号不能为空！',
					remote:'编号已存在！'
				},
				dname: {
					required: "名称不能为空！"
				}
			}
		});   
		SuunCalendar.iniCalendar();
        function setupDateTime(me){
        	SuunCalendar.show(me,{showsTime: true,ifFormat: "%Y-%m-%d %H:%M:%S"});
        }		
     </script>
      
</head>    
<body>
  <input type="hidden" name="mid" value="${treeid}"/>
  <table>
	<tr>
		<td>编号  </td>
		<td>
		   <c:choose>		  
		     <c:when test="${isEdit}">
		         ${demomaindetail.did}<input type="hidden" name="did"  value="${demomaindetail.did}" />
		      </c:when>		  
              <c:otherwise>
                 <input type="text" name="did" value="${demomaindetail.did}" /> 				  
              </c:otherwise>
          </c:choose>        
        </td>
	</tr>
	<tr>
		<td>名称  </td>
		<td><input type="text" name="dname" value="${demomaindetail.dname}" /></td>
	</tr>	
	<tr>
		<td>日期  </td>
		<td><input type="text" name="ddemo" value="${demomaindetail.ddemo}" class="datee" onclick="JavaScript:setupDateTime(this);" /></td>
	</tr>	
  </table>
</body>
</html>
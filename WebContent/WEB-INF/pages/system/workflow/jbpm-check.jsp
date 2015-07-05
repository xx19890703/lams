<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>首页</title>
    <script type="text/javascript">
        function jbpmimage(id){
			var jbpmimageForm = new Ext.Window({
	        	resizable: true,
	        	layout:'fit', 
	        	closeAction:'close',
                modal: true,
	        	width: 600,
		        height: 400,//<center style="curor:hand"></center>
	            title: '流程图',
	            border : false,
	            constrain : true,//保证窗口不会越过浏览器的边界
	            bodyStyle: 'background-color:#ffffff',
	            items: [new Ext.IFrame({
	         	    src:$ctx+'/workflow/jbpm!processDetail.do?id='+id+'&time='+ Math.random()
		         })]
	        });
			jbpmimageForm.show();
		}
        function jbpminput(path,operateid){
			var jbpminputForm = new Ext.Window({
	        	resizable: true,
	        	layout:'fit', 
	        	closeAction:'close',
                modal: true,
	        	width: 600,
		        height: 400,
	            title: '流程图',
	            border : false,
	            constrain : true
	        });		 
			Ext.Ajax.request({
				url: path+'?suunplatformeoperateid='+operateid+'&time='+ Math.random(),
	    	    success: function(msg){
	    	    	suunCore.setHTMLreadonly(jbpminputForm,msg.responseText);
	    	    	jbpminputForm.show();
	    	    },
	    	    failure: function(){
	    	    	jbpminputForm.close();
	    	    	Ext.MessageBox.show({
						title : "错误",
						msg : "出错了！",
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
	    	    }
	    	}); 
		}
        function suuncheck(dbid,transition){
			Ext.Ajax.request({
				url: $ctx+'/workflow/jbpm!selectTransition.do?time='+ Math.random(),
				params: {dbid :dbid,transition: transition,id:'${id}'},
				success: function(msg){
					//var message = message = eval('('+ msg.responseText+ ')');
					var message = Ext.decode(msg.responseText);
					//alert(msg.responseText);
    	    		if (message.success === true){
    	    			Ext.MessageBox.show({
    						title : "提示",
    						msg : "审核成功！",
    						icon : Ext.MessageBox.INFO,
    						buttons : Ext.Msg.OK
    					});
    	    			//reload grid
    	    		}
	    	    },
	    	    failure: function(){
	    	    	jbpmimageForm.close();
	    	    	Ext.MessageBox.show({
						title : "错误",
						msg : "出错了！",
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
	    	    }
	    	}); 
        }
	</script>
</head>
 
<body>
<table width="410" height="119" border="0">
  <tr>
    <td height="25" colspan="3">
    <div align="right">
    <c:forEach var="outgo" items="${outgoes}" varStatus="row">
           <a href="JavaScript:suuncheck('${dbid}','${outgo}')" >${outgo}</a>
        </c:forEach>
    </div></td>
  </tr>
  <tr>
    <td width="289" height="30">操作历史</td>
    <td width="111" colspan="2"><div align="right"><a href="JavaScript:jbpmimage('${id}')">流程图</a>&nbsp;&nbsp;<a href="JavaScript:jbpminput('${ctx}/${objinput.objInput}','${operateid}')">单据</a></div></td>
  </tr>
  <tr>
    <td height="56" colspan="3"><table width="400" border="1">
        <c:choose><c:when test="${fn:length(history)==0}">
            <tr><td>无操作历史</td></tr>
        </c:when><c:otherwise>
            <tr>
		        <td>时间</td>
		        <td>操作人</td>
		        <td>操作</td>
		        <td>备注</td>
	        </tr>
            <c:forEach var="his" items="${history}" varStatus="row">
                <tr>
			        <td><fmt:formatDate value="${his.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/>&nbsp;</td>
			        <td>${his.operaterName}&nbsp;</td>
			        <td>${his.operate}&nbsp;</td>
			        <td>${his.description}&nbsp;</td>
		        </tr>
            </c:forEach>
        </c:otherwise></c:choose>   
    </table></td>
  </tr>  
  
</table>
 
</body>
</html>
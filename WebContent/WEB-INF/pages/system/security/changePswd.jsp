<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>更改密码</title>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script> 
	<script type="text/javascript" >
		//SuunWinForm不可变
		$("#SuunWinForm").validate({
			 rules: { 
				 oldPswd: { 
        			required: true, 
        			remote: {url:'${ctx}/system/security/security!validateOldPassword',
        				     type:'post'
        			}
    			},
    			newPswd:{
    				required:true
    			},
    			confirmPswd:{
    				required:true,
    				equalTo:'#newPswd'
    			}             	
			},
			messages: {
				oldPswd: {
					required:'旧的密码不能为空！',
					remote:'旧的密码不正确！'
				},
				newPswd: {
					required:'新的密码不能为空！'
				},
				confirmPswd: {
					required: '新的密码确认不能为空！',
					equalTo: '新的密码与确认不一致！'
				}
			}
		});

	</script>
</head>
<body>
  <table width="100%"  border="0" align="center" cellspacing="1" >
  	            <tr>
                  <td width="35%" align="right">&nbsp;</td>
                  <td align="left">&nbsp;</td> 
                </tr>
                <tr>
                  <td width="35%" align="right">旧的密码</td>
                  <td align="left"><input type ="password" name="oldPswd" id="oldPswd" maxlength="25"></td> 
                </tr>
                <tr>
                  <td width="35%" align="right">新的密码</td>
                  <td align="left"><input type="password" name="newPswd" id="newPswd" maxlength="25"></td> 
                </tr>
                <tr>
                  <td width="35%" align="right">新的密码确认</td>
                  <td align="left"><input type="password" name="confirmPswd" id="confirmPswd" maxlength="25"></td> 
                </tr>
                 <tr>
                  <td width="35%" align="right">&nbsp;</td>
                  <td align="left">&nbsp;</td> 
                </tr>
  </table>
</body>
</html>

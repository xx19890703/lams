<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>授权管理</title>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/system/core/gridsel.js"></script>
     <!-- script和class两种校验均可 class校验不能换行 -->
     <script type="text/javascript">
       $("#SuunWinForm").validate({
			 rules: { 
				 did: { 
        			required:true,
					remote:{url:'${ctx}/system/demo/demodoublegrid!ValidateAuthorityDetail',
					        type:'post',
					        data:{olddid:'${demomaindetail.did}',mid:'${parentid}'}
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
        function seldemo(me){		
			gridselect({listurl:$ctx+'/system/demo/demo!lists',//基本url
				pagenum:5,//页记录数
				isprewidth:true,//是否宽度按百分比 列信息	hidden: true,locked: ture
				suuncolumns:[{columnid:'authId',columnname:'编号',colwidth:20,defaultsort:true},
						 {columnid:'displayName',columnname:'名称',colwidth:40},
						 {columnid:'state.data_name',columnname:'状态',colwidth:40}
						],
				//winWidth:500,默认值为500,350
				//winHeight:350,
				callback:function(records){
					me.value=records[0].get('displayName');
				}
			});
		}
     </script>
      
</head>    
<body>
  <input type="hidden" name="mid" value="${parentid}"/>
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
		<td><input type="text" name="dname" value="${demomaindetail.dname}" class="dataa" onclick="seldemo(this)"/></td>
	</tr>		
  </table>
</body>
</html>
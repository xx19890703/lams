<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><c:choose>		  
	<c:when test="${isEdit}">
	     修改角色
	</c:when>		  
    <c:otherwise>
        创建角色 
    </c:otherwise>
</c:choose></title>  
<!-- script和class两种校验均可 class校验不能换行 -->
    <script type="text/javascript" src="${ctx}/resources/js/thrid/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>    
	<script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/thrid/extjs/ext-all.js"></script>	
    <script type="text/javascript">var $ctx='${ctx}';</script>	
    <script type="text/javascript" src="${ctx}/resources/js/system/core/suuncore.js"></script>  
	<script type="text/javascript" src="${ctx}/resources/js/system/core/ux/TreeCheckNodeUI.js"></script>
	<script>
		$("#SuunWinForm").validate({
				roleId: { 
					required: true, 
					remote: "system/role!validateRoleId.do?orgroleId=${role.roleId}"
				},
				'roleName': {
				    required: true
				},
				messages: {
				    roleId: {
					    remote: "此角色已存在"
					}
				}
		});
        var lastNode=null;
        window.setTimeout(function (){
        	//设置初始权限信息
			var records = document.getElementById("checkedAuthIds").value.split(';');
			suunCore.showMask("正在加载该角色已有的权限信息,请耐心等待...");
			var tree = new Ext.ux.TreeCheckPanel({
				imagePath:'${ctx}/resources/js/system/core/ux/images',
				viewConfig : {
					loadingText : "正在加载..."
				},
				header:false,
				border : true,     
				enableDD : false,     
				enableDrag : false,     
				rootVisible : false,     
				trackMouseOver : true,         
				lines : true,     
				singleExpand : false, 
				collapsible : true, // 是否可以收缩 
				autoScroll : true,
				containerScroll : true,
				readonly:$("#roleName").attr("disabled")=="disabled"||$("#roleName").attr("readonly")=="readonly",
				checkModel : 'multiple',
				renderTo:'auth-tree',
				height : 300,
				width : '100%',
				root : new Ext.tree.AsyncTreeNode({     
					id : '0',    
					text : '根节点',   
					iconCls:'ico-root',  
					expanded:true,  
					leaf : false,     
					border : false,     
					draggable : false,     
					singleClickExpand : false,     
					hide : true    
				}),   
				loader : new Ext.ux.TreeCheckLoader({    			
					nodeParameter:'groupId',  
					requestMethod:'POST',  
					baseParams:{roleId:'${role.roleId}'},
					dataUrl : '${ctx}/system/security/role!getAuthTree.do'
				}),
				listeners : {
					'load': function(n) {
						suunCore.showMask("正在加载该角色已有的权限信息,请耐心等待...");
						if (n.attributes.exData=="3"){
							for (j=0;j<records.length;j++){
								for (i=0;i<n.childNodes.length;i++){
									if (n.childNodes[i].id==records[j]){
										//alert(n.childNodes[i].id);
										n.childNodes[i].attributes.checked=='all';
										n.childNodes[i].getUI().check('all',false,false);
										n.childNodes[i].getUI().parentCheck(n.childNodes[i]);
										break;
									}
								}					
							}
						}
						suunCore.hideMask();
					},
					'checkchange': function() {	
                        getCheckednodes(this);	
					}
				}				
			});
			function checkparent(node){
				var nodechecked=0;
				for (j=0;j<node.childNodes.length;j++){
					if (node.childNodes[j].attributes.checked=='all'){
						nodechecked++;
					}
				}
				if (nodechecked==0){
					node.getUI().check('none',false,false);
				} else if (nodechecked==node.childNodes.length){
					node.getUI().check('all',false,false);								
				}else{
					node.getUI().check('part',false,false);	
				}
			}
			function getCheckednodes(tree){
				//获取到已经选择上的权限信息
				var records = tree.getChecked();
				var ids = [];
				for (j=0;j<records.length;j++){
					if (records[j].isLeaf()&&(records[j].id.indexOf(',')!=-1))
						ids.push(records[j].id);
				};
				var nodeids = ids.join(';');
				document.getElementById("checkedAuthIds").value=nodeids;			
			}	
		},0); 		
	</script>
</head>

<body>
<table align="center">
    <tr>
		<td>角色代号:</td>        
		<td><c:choose><c:when test="${isEdit}">
            <b>${role.roleId}</b> 
			<input type="hidden" id="roleId" name="roleId" value="${role.roleId}" />			
            </c:when><c:otherwise><input style="width: 300px;" type="text" id="roleId" name="roleId" value="${role.roleId}" /></c:otherwise></c:choose>
        </td>
	</tr>
	<tr>
		<td>角色名称:</td>
		<td><input type="text" style="width: 300px;" id="roleName" name="roleName" value="${role.roleName}" class="required"/>
		</td>
	</tr>
	<tr>
		<td>使用状态:</td>
		<td>
		    <c:choose><c:when test="${isEdit}">
                <input type="radio" name="state" <c:if test='${role.state==1}'>checked="checked"</c:if> value="1" />使用
		        <input type="radio" name="state" <c:if test='${role.state==0}'>checked="checked"</c:if> value="0" />作废    
            </c:when><c:otherwise>
                <input type="radio" name="state" checked="checked" value="1" />使用
		        <input type="radio" name="state"  value="0" />作废
            </c:otherwise></c:choose>	
		</td>
	</tr>
	<tr>
		<td>拥有授权:</td>
		<td>
			<div id="auth-tree" style="width:300px;overflow: hidden;"></div> <!--overflow:no;-->
			<input type="hidden" name="checkedAuthIds" id="checkedAuthIds" value="${checkedAuthIds}"/>
		</td>
	</tr>	
</table>
</body>
</html>
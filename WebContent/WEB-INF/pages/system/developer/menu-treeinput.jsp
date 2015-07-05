<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	   <title>菜单管理</title>	   
	   <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.validate.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/messages_cn.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/thrid/validate/jquery.metadata.js"></script>
       <script type="text/javascript" src="${ctx}/resources/js/system/core/ux/FileUploadField.js"></script>
	   <style type=text/css>         
	    .x-form-file-wrap {  
	        position: relative;  
	        height: 22px;  
	    }  
	    .x-form-file-wrap .x-form-file {  
	        position: absolute;  
	        right: 0;  
	        -moz-opacity: 0;  
	        filter:alpha(opacity: 0);  
	        opacity: 0;  
	        z-index: 2;  
	        height: 22px;  
	    }  
	    .x-form-file-wrap .x-form-file-btn {  
	        position: absolute;  
	        right: 0;  
	        z-index: 1;  
	    }  
	    .x-form-file-wrap .x-form-file-text {  
	        position: absolute;  
	        left: 0;  
	        z-index: 3;  
	        color: #777;  
	    }  
	   </style>  
       <script type="text/javascript">
            var fwpath=$ctx+'/system/developer/menu!';	
			function fileManager() {					
				var store =  new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
						type : 'ajax',
						url : fwpath+'listFile.do',
						actionMethods: { read: 'POST' },
					    simpleSortMode : true
					}),
					reader : new Ext.data.JsonReader({
						root : "data",
						id : "id",
						fields : [{name:'filepath'}]
					}),
					autoLoad : true
				});
				var sm = new Ext.grid.CheckboxSelectionModel();
				var grid = new Ext.grid.GridPanel({
					store : store,
					selModel:sm,
					stripeRows : true,//斑马线效果
					columnLines : true,
					columns : [sm,{header : "图片",
								width : 3,
								id:'filepath',
								dataIndex : 'filepath',
								sortable : true,
								renderer : function(value, o, record) {
								    var imgpathshow=$ctx+"/resources/images/menu/"+value;
									return "<img src='"+imgpathshow+"'/>";
								}
							}, {
								dataIndex : 'filepath',
								header : '文件名',
								width : 5
							}],
					viewConfig : {
						forceFit : true
					},
					listeners:{
						"rowdblclick": selectRecord
					},
					tbar : [{
						pressed : false,
						text : '选择',
						tooltip : '选择文件',
						iconCls : 'select',
						handler : selectRecord 
					}, '-', {
						pressed : false,
						text : '刷新',
						tooltip : '刷新文件列表',  
						iconCls : 'refresh',
						handler : function(){
							store.load();
						}
					},'-', {
						pressed : false,
						text : '添加',
						tooltip : '上传文件',  
						iconCls : 'add',
						handler : upload
					}, '-', {
						pressed : false,  
						text : '删除',
						tooltip : '删除文件',
						iconCls : 'remove',
						handler : function() {
							var records = sm.getSelections();
							var ids = "";
							if (records.length == 0) {
								Ext.MessageBox.show({
											title : "提示",
											msg : "请先选择您要删除的文件!",
											icon : Ext.MessageBox.INFO,
											buttons : Ext.Msg.OK
										})
								return;
							} else {
								Ext.MessageBox.confirm('提示', '确定要删除所选择的文件么？', function(btn) {
									if (btn == "yes") {
										for (var i = 0; i < records.length; i++) {
											ids += records[i].get("filepath")
											if (i < records.length - 1) {
												ids = ids + ",";
											}
										}
										Ext.Ajax.request({
													url :fwpath+'delFile.do',
													method : 'post',
													params : {
														fileName : ids
													},
													success : function(response) {
														Ext.Msg.alert('消息',Ext.decode(response.responseText).msg);
														store.load();
													}
												})
									}
								});
							}
						}
					}]
				});
				
				var windowlist=new Ext.Window({
								title : '节点图片管理',
								width :500,
								height : 300,
								resizable: false,
					            modal: true,
					            closeAction:'close',
								layout : 'fit',
								items : [grid]
							});
				windowlist.show();
				
				function selectRecord() {
					var records = sm.getSelections();
					if (records.length == 0) {
						Ext.MessageBox.show({
									title : "提示",
									msg : "请先选择文件!",
									icon : Ext.MessageBox.INFO,
									buttons : Ext.Msg.OK
								})
						return;
					} else if (records.length > 1) {
						Ext.MessageBox.show({
							title : '提示',
							msg : '只能选择一条信息!',
							icon : Ext.MessageBox.INFO,
							buttons : Ext.Msg.OK
						})
						return;		
					} else {							
						var imgsrc=records[0].get("filepath");
						var imginp=document.getElementsByName("menuImg");
						var img=document.getElementsByName("menuImg2");
						 for(var i=0;i<imginp.length;i++){
							 if(imginp[i]!=null){
						       imginp[i].value='/resources/images/menu/'+imgsrc;
						    }
						 }
						
						 for(var i=0;i<img.length;i++){
							 if(img[i]!=null){
						       img[i].src=$ctx+'/resources/images/menu/'+imgsrc;
						    }
						 }
						 windowlist.close();
					}
				}
				
				function upload(){
					var fp=new Ext.form.FormPanel({ 
							fileUpload: true,  
							width: 400,  
							frame: true,   
							autoHeight: true,  
							bodyStyle: 'padding: 15px 5px 0 5px;',  
							labelWidth: 60,  
							defaults: {  
							    anchor: '95%',  
							    allowBlank: false,  
							    msgTarget: 'side'  
							},  
							items: [{  
							    xtype: 'textfield',  
							    fieldLabel: '上传名称', 
							    name:"txtname",
							    readOnly:true,
							    id:"filename"
							}, {  
							    xtype: 'fileuploadfield',  
							    id: 'form-file',  
							    emptyText: '选择上传的图片文件',  
							    fieldLabel: '上传图片',  
							    name: 'photopath',  
							    buttonText: '浏览...',
							    listeners:{
									"fileselected": function(fb,v){	
										var fs=v.split("\\");
										Ext.getCmp("filename").setValue(fs[fs.length-1]);
									}
								}
							 }],  
		                     buttons: [{  
		                        text: '上传',  
		                        handler: function() {  
		                            if (fp.getForm().isValid()) {  
		                            	fp.getForm().submit({  
		                                    url: fwpath+'upload.do',//后台处理的页面 
		                                    waitTitle: '请稍后',
		                                    waitMsg: '正在上传...', 
		                                    success: function(form, action) { 
		                                    	if (action.result.success==true){
		                                    		Ext.Msg.alert('消息',"图片上传成功！");
		                                    		windowupload.close(); 
													store.load();
		                                    	} else{
		                                    		Ext.Msg.alert('消息',"图片上传失败！");
		                                    	}  
		                                    }  
		                                });
		                            }  
		                        }  
		                     }, {  
		                        text: '重置',  
		                        handler: function() {  
		                            fp.getForm().reset();  
		                        }  
		                     }]  
				    });
					
					var windowupload=new Ext.Window({
					    title:'上传文件',
					    width: 410,
				        height:140, 
					    modal :true,
						resizable: false,
					    closeAction:'close',
					    items:[fp]
					})
					windowupload.show();
				}
			}		
	 </script>
	 <script>
	    $("#SuunWinForm").validate({
		 rules: { 
			 menuId:{
				 required: true, 
	       		 maxlength: 30,
	       		 remote: {url:'${ctx}/system/developer/menu!validateName',
		             type:'post',
		             data:{oldname:'${menu.menuName}',id:'${suunplatformTreePid}'}
	                }
			 },
			 menuName: { 
       			required: true, 
       			maxlength: 30,
       			remote: {url:'${ctx}/system/developer/menu!validateName',
			             type:'post',
			             data:{oldname:'${menu.menuName}',id:'${suunplatformTreePid}'}
		                }
       		 } ,
       		 menuURL:  { 
       			maxlength: 200
       		 },
		     itemOrder:{
		       digits:true,
		       maxlength:5
		}   	
		},
		messages: {
			menuId:{            
				required: "菜单编号不能为空！",				
				maxlength:"菜单编号的长度不能超过30！",
				remote:'菜单编号已存在！'
			 },
			menuName: {
				required: "菜单名称不能为空！",				
				maxlength:"菜单名字的长度不能超过30！",
				remote:'菜单名称已存在！'
			},
			menuURL: {
				maxlength:"菜单URL的长度不能超过200！"
			},
		    itemOrder:{
		      digits:"显示顺序的值必须是整数数字！",
		      maxlength:'显示顺序的值长度不能超过5！'
		    }
		}
	   });
    </script>
	</head>
	<body>	
		<table align="center">
		<br/>
			<input type="hidden" name="" value="0" />
			<tr>
				<td width="120" align="right">上级菜单</td>
				<td width="180">
				   &nbsp;&nbsp;<input type="hidden" name="menuParid" value="${suunplatformTreePid}"/>${suunplatformTreePName}
				</td>
			</tr>
			<tr>
			    <td width="120" align="right">菜单编号</td>
				<td width="140">
				    <c:choose>
				       <c:when test="${isEdit}">
		                    <input type="text" name="menuId" value="${menu.menuId}" style="width:174px;"/>
						</c:when>
						<c:otherwise>
							<input type="text" name="menuId" style="width:174px;" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td width="120" align="right">菜单名称</td>
				<td width="140"><input  type="text" name="menuName"  value="${menu.menuName}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="120" align="right">菜单类型</td>
				<td width="140" >
					<select  id="menuType" name="menuType" style="width:174px;">
		                <option value="0" <c:choose><c:when test="${menu.menuType==0}">selected="true"</c:when></c:choose> >顶层菜单</option>  
		                <option value="1" <c:choose><c:when test="${menu.menuType==1}">selected="true"</c:when></c:choose> >分类菜单</option>
		                <option value="2" <c:choose><c:when test="${menu.menuType==2}">selected="true"</c:when></c:choose> >枝干菜单</option>  
		                <option value="3" <c:choose><c:when test="${menu.menuType==3}">selected="true"</c:when></c:choose> >叶子菜单</option>
		            </select> 
                </td>
			</tr>
			<tr>
				<td width="120" align="right">菜单URL</td>
				<td width="140" >
					<input  type="text" name="menuUrl"  value="${menu.menuUrl}" style="width:174px;"/></td>
			</tr>
			<tr>
				<td width="120" align="right">菜单图片</td>
				<td width="140" >
				     <input type="button" value="选择图片" onclick="fileManager();" />&nbsp;&nbsp;&nbsp;&nbsp;
					 <c:choose>
					    <c:when test="${isEdit}">
					      <input type="hidden" name="menuImg" value="${menu.menuImg}"/>
	                      &nbsp;&nbsp;&nbsp;&nbsp; <img src="${ctx}/${menu.menuImg}" name="menuImg2" />
						</c:when>
						<c:otherwise>
						     <input  type="hidden" name="menuImg" value="/resources/images/system/group.png"/>
						  &nbsp;&nbsp;&nbsp;&nbsp; <img src="${ctx}/resources/images/system/group.png" name="menuImg2" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>			
			<tr>
				<td width="120" align="right">显示顺序 </td>
				<td width="140">  
				  <c:choose>		  
				      <c:when test="${isEdit}">
				        <input  type="text" name="itemOrder" value="${menu.itemOrder}" style="width:174px;"/>		
				      </c:when>		  
		              <c:otherwise>
		                <input  type="text" name="itemOrder" value="0" style="width:174px;"/>		
		              </c:otherwise>
		          </c:choose>
		        </td>
	         </tr>
	         <tr>
				<td width="120" align="right">是否iframe显示 </td>
				<td width="140"> 
				    <%
				        java.util.Map map = new java.util.HashMap(); 
				        map.put("1","是");  
				        map.put("0","否"); 
				        request.setAttribute("allObjects",map);   
			        %>
				    <ui:radiolist name="isframe" lists="${allObjects}" checkValue="${menu.isframe}" defaultCheckValue="0"/> 
		        </td>
	         </tr>
	         <tr>
				<td width="120" align="right">是否开发菜单 </td>
				<td width="140">  
				    <ui:radiolist name="isadmin" lists="${allObjects}" checkValue="${menu.isadmin}" defaultCheckValue="0"/> 
		        </td>
	         </tr>
		</table>
	</body>
</html>
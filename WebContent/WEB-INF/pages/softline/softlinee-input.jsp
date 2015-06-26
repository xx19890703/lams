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
            var fwpath=$ctx+'/softline/softlinee!';	
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
						id : "baseId",
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
					columns : [sm,{header : "照片",
								width : 3,
								id:'filepath',
								dataIndex : 'filepath',
								sortable : true,
								renderer : function(value, o, record) {
								    var imgpathshow=$ctx+"/resources/images/picture/"+value;
									return "<img src='"+imgpathshow+"' style='width:100px;height:40px;'/>";
								}
							}, {
								dataIndex : 'filepath',
								header : '照片文件名',
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
								title : '照片管理',
								width :550,
								height : 400,
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
						var imginp=document.getElementsByName("picture");
						var img=document.getElementsByName("picture2");
						 for(var i=0;i<imginp.length;i++){
							 if(imginp[i]!=null){
						       imginp[i].value='/resources/images/picture/'+imgsrc;
						    }
						 }
						
						 for(var i=0;i<img.length;i++){
							 if(img[i]!=null){
						       img[i].src=$ctx+'/resources/images/picture/'+imgsrc;
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
    $.validator.addMethod("idcarNo", function(num) {
    	var len = num.length, re; 
    	if (len==0) return true;
     　　 if (len == 15) 
     　　 re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/); 
     　　 else if (len == 18) 
     　　 re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/); 
     　　 else {
             return false;
         } 
     　　 var a = num.match(re); 
     　　 if (a != null) 
     　　 { 
     　　 if (len==15) 
     　　 { 
     　　 var D = new Date("19"+a[3]+"/"+a[4]+"/"+a[5]); 
     　　 var B = D.getYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5]; 
     　　 } 
     　　 else 
     　　 { 
     　　 var D = new Date(a[3]+"/"+a[4]+"/"+a[5]); 
     　　 var B = D.getFullYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5]; 
     　　 } 
     　　 if (!B) {
             return false;
         } 
     　　 } 
     　　 if(!re.test(num)){
             return false;
         }
     　　 return true; 
	   }, '身份证号格式不对');
    $.validator.addMethod("isphone", function(value) {
  	  var length = value.length;
  	  if (length==0) return true;
  	  var phone = /(^(\d{3,4}-)?\d{6,8}$)|(^(\d{3,4}-)?\d{6,8}(-\d{1,5})?$)|(\d{11})/;
  	  return phone.test(value);
  	 }, "电话号码输入不正确！");
     
        $("#SuunWinForm").validate({
			 rules: { 
    			hJobYeat:{
    				digits:true
    			},
    			postCode:{
    				digits:true,
    				maxlength:6,
    			    minlength:6  
    			},
    			unitPostCode:{
    				digits:true,
    				maxlength:6,
    			    minlength:6  
    			},
    			idcarNo:{
    				idcarNo:true
    			}      	
		},
			messages: {
				idcarNo: {
					required:'身份证号不能为空！'
				},
    			hJobYeat:{
    				//required:"",
    				digits:"接触有毒害工作年限只能输入数字！"
    			},
    			postCode:{
    				digits:"被鉴定人邮政编码只能输入数字！",
    				maxlength:"被鉴定人邮政编码只能输入6位数字！",
    			    minlength:"被鉴定人邮政编码只能输入6位数字！"  
    			},
    			unitPostCode:{
    				digits:"用人单位邮政编码只能输入数字！",
    				maxlength:"用人单位邮政编码只能输入6位数字！",
    			    minlength:"用人单位邮政编码只能输入6位数字！"  
    			},
    			unitPostCode:{
    				digits:"邮政编码只能输入数字！",
    				maxlength:"邮政编码只能输入6位数字！",
    			    minlength:"邮政编码只能输入6位数字！"  
    			}
			}
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
 </br>
 <table border="1" align="center" cellspacing="0" bordercolor="#6699FF" style="width:790px;height:396px;">
  <tr height="30"  bordercolor="#003399">
	<td style="width:16px;" rowspan="5" >被鉴定人</td>
	    <td align="center" style="width:40px;" type="alphanum">编号</td>
        <td style="width:85px;">
	    <c:choose>
	    		 <c:when test="${isEdit}">
			         ${softlinee.baseId}<input type="hidden" name="baseId" value="${softlinee.baseId}"/>
			     </c:when>		  
	             <c:otherwise>
	                 <input type="text" name="baseId" style="width:78px" value="${maxBaseId}" readonly="readonly"/>                     
	             </c:otherwise>
   	    </c:choose>       
    </td>
	<td style="width:70px;"><div align="center">姓名 </div></td>
	    <td style="width:180px;">
	<input type="text" name="name" id="name" value="${softlinee.name}" style="width:180px" maxlength="30"/></td>
    <td style="width:125px;"><div align="center">性别 </div></td>
   		<td style="width:90px;">  
	   	<div align="left">
		 	<ui:selectlist cssStyle="width:90px;"  name="sex.key.data_no" lists="${allObjectd}" listValue="key.data_no" listTitle="data_name" checkValue="${softlinee.sex.key.data_no}" defaultCheckValue="0"/>
   		</div></td>
	    <td colspan="2" rowspan="4" align="center">
			 <c:choose>
				 <c:when test="${isEdit}">
					 <input type="hidden" name="picture" value="${softlinee.picture}"/>
	                 	 <img src="${ctx}/${softlinee.picture}" name="picture2" style="width:140px;height:97px"/></c:when>
				<c:otherwise>
					 <input  type="hidden" name="picture" value="/resources/images/system/default_image_male.jpg"/>
					 	<img src="${ctx}/resources/images/system/default_image_male.jpg" name="picture2" style="width:140px;height:97px" /></c:otherwise>
				</c:choose></br>
   					 <input type="button" value="编辑照片" onclick="fileManager();" />&nbsp;&nbsp;&nbsp;&nbsp;
   		</td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">身份证号码</td>
   	    <td colspan="2" align="center">   	     
   	    	 <div align="left">
    	  		<input type="text" name="idcarNo" id="idcarNo" value="${softlinee.idcarNo}" style="width:255px" maxlength="18"/>        
       		 </div>
       	</td>
        <td align="center">出生年月 </td>
        <td align="center"> 
        <div align="left">
            <div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
                <input name="dobs" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlinee.dobs}" pattern="yyyy-MM-dd"/>'>
                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            </div>
        </div></td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">受伤时间</td>
   	    <td colspan="2">
   			<div align="left">
            <div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
                <input name="woundTime" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlinee.woundTime}" pattern="yyyy-MM-dd"/>'>
                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            </div><font color=red>例如：1988-2-3</font>
        </div></td>
   	    <td align="center">联系电话</td> 
        <td>
        	<input type="text" name="phone" id="phone" value="${softlinee.phone}" style="width:90px" maxlength="50"/></td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">通讯地址</td>
    	<td colspan="2">
    		<input type="text" name="addresss" id="addresss" value="${softlinee.addresss}" style="width:255px" maxlength="250"/>    	
    	</td>
    	<td align="center">邮编</td>
 	    <td>
 	    	<input type="text" name="postCode" id="postCode" value="${softlinee.postCode}" style="width:90px"/></td>
   </tr>
   <tr>
    	<td colspan="2"  align="center">从事有毒有害岗位名称</td>
    	<td colspan="2">
    		<input type="text" name="hJobName" id="hJobName" value="${softlinee.hJobName}" style="width:255px" maxlength="50"/></td>
   	    <td><div align="center">从事有毒有害职业日期</div></td>
		<td align="center">
		  	 <div align="left">
            <div style="background-color:white;border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;width:88px;height:18px;cursor:pointer;text-align:left;"> 
                <input name="hTime" class="JuiCssed" type="text" style="vertical-align:middle;border:0px;cursor:pointer;height:17px;width:68px;" onblur="validateTime(this)" value='<fmt:formatDate value="${softlinee.hTime}" pattern="yyyy-MM-dd"/>'>
                <img style="vertical-align:middle;border:0px;cursor:pointer;height:16px;width:16px;" src="${ctx}/resources/js/system/core/ui/images/calendar.png" onclick="sss(this)"> 
            </div>
        </div>
		  	</td>
    	 <td style="width:108px;">接触有毒害工作年限</td>
         <td><input type="text" name="hJobYeat" id="hJobYeat" value="${softlinee.hJobYeat}" style="width:45px" maxlength="10"/></td>
   </tr>
   <tr>
	    <td rowspan="3" align="center">用人单位</td>
	    <td colspan="2"  align="center">单位名称</td>
	    <td colspan="2"><input type="text" name="unitName" id="unitName" value="${softlinee.unitName}" style="width:255px" maxlength="32"/> </td>
	    <td  align="center">单位编码</td>
	    <td colspan="3"><input type="text" name="unitNo" id="unitNo" value="${softlinee.unitNo}" style="width:255px" maxlength="50"/></td>
  </tr>
  <tr>
	    <td colspan="2" align="center">通讯地址</td>
	    <td colspan="2"><input type="text" name="unitAdd" id="unitAdd" value="${softlinee.unitAdd}" style="width:255px" maxlength="60"/></td>
	    <td align="center">邮编</td>
	    <td colspan="3"><input type="text" name="unitPostCode" id="unitPostCode" value="${softlinee.unitPostCode}" style="width:255px"/></td>
  </tr>
  <tr>
	    <td colspan="2"  align="center">联系人</td>
	    <td colspan="2"><input type="text" name="linkName" id="linkName" value="${softlinee.linkName}" style="width:255px" maxlength="50"/></td>
	    <td align="center">联系电话</td>
	    <td colspan="3"><input type="text" id="lnPhone" name="lnPhone" id="lnPhone" value="${softlinee.lnPhone}" style="width:255px" maxlength="50"/></td>
  </tr>
  <tr>
	    <td colspan="3" align="center">工伤认定决定书文号</td>
	    <td colspan="2"><input type="text" name="hBookNo" id="hBookNo" value="${softlinee.hBookNo}" style="width:255px" maxlength="50"/></td>
	    <td align="center">工伤认定部位</td>
	    <td colspan="3"><input type="text" name="hBookPosition" id="hBookPosition" value="${softlinee.hBookPosition}" style="width:255px" maxlength="250"/></td>
  </tr>
  <tr>
	    <td colspan="3" align="center">申请检查科目</td>
	    <td colspan="2"><input type="text" name="checks" id="checks" value="${softlinee.checks}" style="width:255px" maxlength="250"/></td>
	    <td align="center">劳鉴办医师准查科目</td>
	    <td colspan="3"><input type="text" name="mustCheck" id="mustCheck" value="${softlinee.mustCheck}" style="width:255px" maxlength="250"/></td>
  </tr>
  <tr>
	  	<td align="center">备注</td>
	    <td colspan="9"><textarea name="mark" style="width: 785px;" maxlength="250">${softlinee.mark}</textarea></td>
  </tr>
  <tr>
		<td align="right">状态</td>
		<td colspan="9"><input type="hidden" name="slstate.key.data_no" value="0"/>
			<ui:selectlist cssStyle="width:174px;" disabled="disabled"  lists="${allObjects}" listValue="key.data_no" listTitle="data_name" checkValue="${softlinee.slstate.key.data_no}" defaultCheckValue="0"/>
			<input type="hidden" name="systemData"  size="27" value="${str_date1}"/>
			
		</td>
   </tr>
 </table>
 </body>
</html>
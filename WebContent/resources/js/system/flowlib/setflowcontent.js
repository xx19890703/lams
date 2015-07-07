	$(function() {
	$("#myflow_tools_handle").colorTip({color:'yellow'});
	$("#pointer").colorTip({color:'yellow'});   
	$("#path").colorTip({color:'yellow'});   
	${"#start"}.colorTip({color:'green'});
	${"#state"}.colorTip({color:'blue'});
	${"#task"}.colorTip({color:'yellow'});
	${"#fork"}.colorTip({color:'yellow'});
	${"#join"}.colorTip({color:'yellow'});
	${"#end"}.colorTip({color:'yellow'});
	${"#end-cancel"}.colorTip({color:'yellow'});
	${"#end-error"}.colorTip({color:'yellow'});
	${"#end"}.colorTip({color:'yellow'});
	
	
		$('#myflow').myflow(
						{
							basePath :"",
							restore : eval(datajsons),
							tools : {
								save : {
									onclick : function(data) {
										
										var str=toxml(data);
										 
										var name=getJpdgName();
										 
										sendxml(str,name,data);
										 
									}
								}
							}
						});

	});
	function sendxml(str,name,data){
	var url= '/floweditor/makejpdl.do';
	alert(url);
	 $.ajax({
	    url: url,
	    type: 'post',										    
	    timeout: 1000,
	    data: "name="+name+"&jsondata="+data+"&info="+str,
	    datetype:'text',
	    error: function(data){	         
	        $('#dialog1').dialog({
					autoOpen: true,
					title:'温馨提示',
					width: '30%',
					buttons: {
						"Ok": function() { 
							$(this).dialog("close"); 
						}  
					}
				});
	    },
	    success: function(data){
	       $('#dialog').dialog({
					autoOpen: true,
					title:'温馨提示',
					width: '30%',
					buttons: {
						"Ok": function() { 
							$(this).dialog("close"); 
						}  
					}
				});
	      }
	});

 }
var processxml = "";
var statesxml = "";
var transitionxml = "";
var name="";
(function ($) {
	
	getJpdgName=function(){  
		return name;
	
	}
	toxml = function (obj) {
	
		processxml = eval('(' + processxml + ')');
		var transitionlength = transitionxml.split("}").length - 1;
		var pdesc=trimAll( processxml.desc);
		var pname=trimAll( processxml.name);
		var pkey=trimAll( processxml.key);
		var checknNames = [pdesc,pname,pkey];  
		var descriptions = [ "描述", "名称","版本"]; 
		var checkLengths = [ "50","50","50"];  
		var checkNulls = [ "notnull", "notnull", ""]; 
		var checkTypes = ["text","text","text" ];  
		if(!nullvalidate(checknNames, descriptions,checkNulls)){
			return false;
		} 
		
		if (!isunchartsvalidate(checknNames,descriptions,checkTypes)) {
			return false;
		}
		
		if (!checkFormLength(checknNames,descriptions,checkLengths)) {
			return false;
		}
	 
		
		str = "<process description=\"" + pdesc+ "\" name=\"" + pname + "\" xmlns=\"http://jbpm.org/4.4/jpdl\">";
		str=str+"<on event=\"start\">";
		str=str+"<event-listener class=\"com.suun.model.zcontroller.floweditor.ProcessFlowListener\">";
		str=str+"<field name=\"classname\"><string value=\"classpath1\"></string></field>";
		str=str+"<field name=\"status\"><string value=\"start\"></string></field>";
		str=str+"</event-listener></on>";
	    str=str+"<on event=\"end\">";
		str=str+"<event-listener class=\"com.suun.model.zcontroller.floweditor.ProcessFlowListener\">";
		str=str+"<field name=\"classname\"><string value=\"classpath1\"></string></field>";
		str=str+"<field name=\"status\"><string value=\"end\"></string></field>";
		str=str+"</event-listener></on>";
		name=pname;
		var statelength = statesxml.split("}").length - 1;
		for (var i = 0; i < statelength; i++) {
			var b = statesxml.split("}");
			if (b[i].substring(0, 1) == ",") {
				b[i] = b[i].substring(1, b[i].length);
			}
			b[i] = eval('(' + b[i] + '})');
			switch (b[i].type) {
			case "task":
				if (b[i].assignee == '使用者') {   
					str = str + "<" + b[i].type + " form=\"" + b[i].form + "\" assignee=\"#{owner}\" g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
				} else {
					str = str + "<" + b[i].type + " form=\"" + b[i].form + "\" assignee=\"" + b[i].assignee + "\" g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
				}
				break;
			case "decision":
			     b[i].expr= b[i].expr.replace(/\"/ig,"'");
 
				str = str + "<" + b[i].type + " expr=\"#{" + b[i].expr + "}\"   g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
				break;
			 case "sql":
		   
		        str = str + "<" + b[i].type + " var=\"" + b[i].vara + "\"  g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
			    // b[i].query= b[i].query.replace(/\"/ig,"'");
			    str=str+"<query>"+b[i].query+"</query>";	
			    b[i].paramevalue=b[i].paramevalue.replace(/%/ig,"@");
			    str=str+"<parameters> <object name=\""+b[i].paramekey+"\" expr=\""+b[i].paramevalue+"\" /> </parameters>";	
				
				break;
		   case "end":
		   
		        str = str + "<" + b[i].type + "   g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
			   
				break;
			default:
				str = str + "<" + b[i].type + "   g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
				break;
				
			}
		//	str = str + "<" + b[i].type + " g=\"" + b[i].x + "," + b[i].y + "," + b[i].width + "," + b[i].height + "\" name=\"" + b[i].text + "\">";
			for (var j = 0; j < transitionlength; j++) { //和链接对比，增加链接标签
				var t = transitionxml.split("}");
				if (t[j].substring(0, 1) == ",") {
					t[j] = t[j].substring(1, t[j].length);
				}
				t[j] = eval('(' + t[j] + '})');				
				if (b[i].rect == t[j].from) {
				if(b[i].type!="start"){
					if (t[j].dots == undefined) { //折线节点
						str = str + "<transition  g=\"" + t[j].x + "," + t[j].y + "\" name=\"" + t[j].name + "\"  to=\"" + t[j].name + "\"/>";
					} else {
						str = str + "<transition  g=\"" + t[j].dots + ":\" name=\"" + t[j].name + "\"  to=\"" + t[j].name + "\"/>";						
					}
					}
					else{
						if (t[j].dots == undefined) { //折线节点
						str = str + "<transition  g=\"" + t[j].x + "," + t[j].y + "\" to=\"" + t[j].name + "\"/>";
					} else {
						str = str + "<transition  g=\"" + t[j].dots + ":\" to=\"" + t[j].name + "\"/>";						
					}
					}
				}
			}
			str = str + "</" + b[i].type + ">";
		}
		str = str + " </process>";
		return str;		
	};
	
})(jQuery);

 var flowfilename="([{name:'',value:''},";
 function getFlowfilenames(){ 
 
	 $.ajax({
	    url:$ctx+'/system/workflow/workflow!findflowfiles.do',
	    type: 'post',	    
	    datetype:'json',
	    error: function(data){	         
	         alert("操作异常，请重新操作！");
	    },
	    success: function(data){
	    
	    for(var i=0;i<data.data.length;i++){
	       flowfilename=flowfilename+"{name:'"+data.data[i].fileName+"',value:'"+data.data[i].filePath+"'},";
	    }
	    
	     if(flowfilename.substring(flowfilename.length-1,flowfilename.length)==","){
	    	flowfilename=flowfilename.substring(0,flowfilename.length-1);
	     }
	      flowfilename=flowfilename+"])"; 
	       
	   }
  });
 
}
 getFlowfilenames();
  var flowallusers="([{name:'使用者自己',value:'onwer'}])";
 /*function getFlowfileusers(){ 
	 $.ajax({
	    url:$ctx+'users/findallusers.do',
	    type: 'post',	    
	    datetype:'json',
	    error: function(data){	         
	         alert("操作异常，请重新操作！");  
	    },
	    success: function(data){
	    for(var i=0;i<data.data.length;i++){
	       flowallusers=flowallusers+"{name:'"+data.data[i].filePath+"',value:'"+data.data[i].fileName+"'},";
	    }
	     if(flowallusers.substring(flowallusers.length-1,flowallusers.length)==","){
	    	flowallusers=flowallusers.substring(0,flowallusers.length-1);
	     }
	      flowallusers=flowallusers+"])"; 
	   }
  });
}
 getFlowfileusers();
 */
 function  opendelpoy(){
 	window.parent.addTab("119", "发布流程管理", $ctx+"views/workflow/deploy/viewflows.jsp");
 }

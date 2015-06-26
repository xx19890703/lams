
function ajax(method, url, successfun, failfun, params) {
	Ext.Ajax.request({
		method : method,
		url : url,
		success : successfun,
		failure : failfun,
		params : params
	});
	
}

/***
 * 验证参数是否为空
 */
function nullvalidate(checknNames, descriptions,checkNulls) {
	  for (i = 0; i < checknNames.length; i = i + 1) { 
	    if(checknNames[i].length<1&&checkNulls[i]=='notnull'){
	        Ext.Msg.alert("提示",descriptions[i] + "不能为空!");
                return false; 
	    }
	  }
	  return true;
}

// 根据表单ID验证长度  
function checkFormLength(checknNames, descriptions,checkLengths) {  
    
     for (i = 0; i < checknNames.length; i = i + 1) { 
     var length=getInputLengths(checknNames[i]);
     var relength=checkLengths[i];
	    if(length>relength){
	        Ext.Msg.alert("提示",descriptions[i] + "长度不能超过" + relength+ "字符!");
            return false; 
	    }
	  }
	  return true;
}  
// 计算输入框字符数  
function getInputLengths(obj) {  
    var count = 0;  
    for (j = 0; j < obj.length; j = j + 1) {  
        if (obj.charCodeAt(i) > 256) {  
            count = count + 2;  
        } else {  
            count = count + 1;  
        }  
    }  
    return count;  
} 

function isunchartsvalidate(checknNames, descriptions,checkTypes) {
	
	 for (i = 0; i < checknNames.length; i = i + 1) { 
	 if(checknNames[i].length>0){
	 if(checkTypes[i]=="email") {
		var regExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
				if(regExp.test(checknNames[i]) == false) {
					Ext.Msg.alert("提示", descriptions[i] + "输入不合法!");
					return false;
				}
	 }
	 if(checkTypes[i] == "phone") {
			var regExp = new RegExp("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
			if(regExp.test(checknNames[i]) == false) {
				Ext.Msg.alert("提示", descriptions[i] + "输入不合法!");
				return false;
			}
		}
		if(checkTypes[i] == "text") {
			var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i"); 
			if(pat.test(checknNames[i]) == true) {
				Ext.Msg.alert("提示", descriptions[i] + "输入不合法!");
				return false;
			}
		}
		 
	}
	}
	return true;
	
}

function emailCheck(objvalue) {
	
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(objvalue);
	
}

function checkExt(file) {
	if (!(/(?:jpg|gif)$/i.test(file.value))) {
		alert("只允许上传jpg和gif的图片");
		if (window.ActiveXObject) { //for IE
			file.select(); //select the file ,and clear selection
			document.selection.clear();
		} else if (window.opera) { //for opera
			file.type = "text";
			file.type = "file";
		} else
			file.value = ""; //for FF,Chrome,Safari
	} else {
		alert("ok"); //or you can do nothing here.
	}
};

/**
 * 验证长度
 * 参数1名称
 * 参数2条件
 * 参数3 条件1的值
 * 参数4 条件2的值
 */
function isLengthError(id, condition) {
	var flag = true;
	if (arguments[0].length == 0)
		return false;
	
	for (var i = 0; i < arguments.length; i++) {
		
		if (arguments[i].length == true) {
			flag = false;
		}
	}
	
}

/***
 * 只能输入数字
 * onkeydown="myKeyDown()"
 */
function myKeyDown() {
	var k = window.event.keyCode;
	if ((k == 46) || (k == 8) || (k == 189) || (k == 109) || (k == 190) || (k == 110) || (k >= 48 && k <= 57) || (k >= 96 && k <= 105) || (k >= 37 && k <= 40)) {}
	else if (k == 13) {
		window.event.keyCode = 9;
	} else {
		window.event.returnValue = false;
	}
}

/****去除所有空格****/
function trimAll(str) {
	return str.replace(/\s+/g, "");
}

function isNULL(str) {
	 
	if (str.toLowerCase() == "null") {
		return true;
	}
	return false;
	
}
/****电话号码格式校验 ，返回true表示电话格式正确，false表示格式不正确 ****/

function phoneNumber(phonenumber) {
	var flag = true;
	//电话号码正则表达式（支持手机号码，3-4位区号，7-8位直播号码，1－4位分机号）
	var phonere = new RegExp("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	if (phonere.test(phonenumber) == false) {
		flag = false;
	}
	return flag;
}
/****中文校验 返回false不是中文  true是中文****/
function isChinese(someValue) {
	var flag = false;
	var menuurl = new RegExp("[\u4E00-\u9FFF]");
	if (menuurl.test(someValue) == true) {
		flag = true;
	}
	return flag;
}


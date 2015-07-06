(function ($) { 
    var defaults = { 
        container: ".suunRept", // 最外层的容器class
        items: ".items", // 具体行class
		reverse:".reverse",// 反选按钮
        addBtn: ".addBtn", // 添加按钮class
        delBtn: ".delBtn",// 删除按钮class
        sysindex: ".sysindex"
    } 
    var date = new Array(),isSelected=false; 
    $.extend({  
        SuunRept: function (options) { 
            options = $.extend(defaults, options); 
            $con = $(options.container); 
            $con.attr({style:"border:solid 2px #78b0dc;"});
            for (x = 0; x < $con.find("tr").length; x++){
            	var tr=$($con.find("tr")[x]);
            	for (y = 0; y < tr.find("td").length; y++){
            		$(tr.find("td")[y]).attr({style:"border:solid 1px #78b0dc;"});
            	}
            }
            $items = $con.find(options.items); // 模板行            
            var tempHtml = escape('<tr><td style="border:solid 1px #78b0dc;width:20px;" align="center"><input type="hidden" class="sysindex"/><input type="checkbox"/></td>'+$items.html()+'</tr>'); 
            $items.remove();
            var trs=$con.find("tr");
            for (x = 0; x < trs.length; x++){ 
            	ctr=$(trs[x]);
            	var ihtml="<td style='border:solid 1px #78b0dc;'>&nbsp;</td>"+trs[x].innerHTML;
                ctr.empty();
                ctr.append(ihtml);
            }            	
            
            var details=new Array();
            details=options.detailnames.split(",");
            var curHtml=""; 
            var jbean=options.jsonbean[options.beanname]; 
            if ((jbean!=null)&&(jbean.length>0)){ 	          		
            	for (x = 0; x < jbean.length; x++){
                	curHtml=unescape(tempHtml);
                	//citem=$(curHtml).children();
                    for (i = 0; i < details.length; i++){
                    	var adetails=details[i].split(".");
                    	var objdetail=null;
                    	try{
                    	    objdetail=jbean[x][adetails[0]];
                    	    for (d= 1; d < adetails.length; d++){
                    		    objdetail=objdetail[adetails[d]];
                    	    }//jbean[x][details[i]]
                    	}catch(e){
                    		objdetail=null; 
                    	}    
                    	if (objdetail){ 
                    		/*curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'['+x+'].'+details[i]+
                    				 '" value="'+objdetail+'"');
                    		curHtml=curHtml.replace(new RegExp('name(\\s*)=(\\s*)'+details[i],'ig'),'name="'+options.beanname+'['+x+'].'+details[i]+
                   				 '" value="'+objdetail+'"');
                    		curHtml=curHtml.replace(new RegExp(details[i]+'.\\$value','ig'),objdetail);*/
                    		/*for (j = 0; j < citem.length; j++){
                    			if ($(citem[j]).attr('name')==details[i]){
                    				$(citem[j]).attr('name',options.beanname+'['+x+'].'+details[i]);
                    				$(citem[j]).attr('value',jbean[x][details[i]]);
                    			}
                    		}*/
                    		inputs=$(curHtml).find("input");
                    		for (j = 0; j < inputs.length; j++){
                    			if (inputs[j].name==details[i]){
                    				curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'['+x+'].'+details[i]+
                            				 '" value="'+objdetail+'"');
                            		curHtml=curHtml.replace(new RegExp('name(\\s*)=(\\s*)'+details[i],'ig'),'name="'+options.beanname+'['+x+'].'+details[i]+
                           				 '" value="'+objdetail+'"');
                            		curHtml=curHtml.replace(new RegExp(details[i]+'.\\$value','ig'),objdetail);
                            		break;
                    			}
                    		}
                    		areas=$(curHtml).find("textarea");
                    		for (j = 0; j < areas.length; j++){
                    			if (areas[j].name==details[i]){                    				
                    				//document.getElementsByName(options.beanname+'['+(index-1)+'].'+details[i])[0]
                            		curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'['+x+'].'+details[i]+'"');
                            		curHtml=curHtml.replace(new RegExp('name(\\s*)=(\\s*)'+details[i],'ig'),'name="'+options.beanname+'['+x+'].'+details[i]+'"');
                            		curHtml=curHtml.replace(new RegExp(details[i]+'.\\$value','ig'),objdetail);
                            		if ($.browser.msie){
                            			src=curHtml;                            		
                                		while(src.length>0){
                                			fp=src.search(new RegExp("<textarea",'i'));                                    		
                                    		if (fp>=0){
                                    			ep0=src.search(new RegExp("</textarea>",'i'));
                                        		ep1=src.substring(fp).search(new RegExp("/>",'i'));
                                        		if ((ep0==-1)&&(ep1==-1)){
                                        			break;
                                        		}else if(ep0==-1){
                                        			ep=ep1;
                                        		}else if(ep1==-1){
                                        			ep=ep0;
                                        		}else if (ep0>ep1){
                                        			ep=ep1+2;
                                        		}else{
                                        			ep=ep0+11;
                                        		}
                                    			if ($(curHtml.substring(fp,ep)).attr('name')==options.beanname+'['+x+'].'+details[i]){
                                        			curHtml=curHtml.substring(0,fp)+$($(curHtml).find("textarea")[j]).append(objdetail)[0].outerHTML+curHtml.substring(ep);
                                        			break;
                                        		}else{
                                        			src=src.substring(ep); 
                                        		}
                                    		} else{
                                    			break;
                                    		}
                                    		
                                		}
                            		} else{
                            			curHtml=curHtml.replace($(curHtml).find("textarea")[j].outerHTML,$($(curHtml).find("textarea")[j]).append(objdetail)[0].outerHTML);
                            		}
                            		break;
                    			}
                    		}
                    	}else{
                    		/*for (j = 0; j < citem.length; j++){
                    			if ($(citem[j]).attr('name')==details[i]){
                    				$(citem[j]).attr('name',options.beanname+'['+x+'].'+details[i]);
                    			}
                    		}*/
                    		curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'['+x+'].'+details[i]+'"');
                    		curHtml=curHtml.replace(new RegExp('name(\\s*)=(\\s*)'+details[i],'ig'),'name="'+options.beanname+'['+x+'].'+details[i]+'"');
                    	}                     	
                    }
                    curHtml=curHtml.replace(new RegExp('【(\\d+|s?)】','ig'),'【'+String(x+1)+'】');
                    $con.append(curHtml);
                }  
                for ( j= 0; j < $con.find(":checkbox").length; j++){					
					var tr=$($con.find(":checkbox")[j]).parent().parent();					
					$(tr[0]).find(options.sysindex).attr("value",j+1);
                }
            } else{            	
            	curHtml=unescape(tempHtml);
            	//citem=$(curHtml).children();
            	for (i = 0; i < details.length; i++){ 
            		/*for (j = 0; j < citem.length; j++){
            			if ($(citem[j]).attr('name')==details[i]){
            				$(citem[j]).attr('name',options.beanname+'['+x+'].'+details[i]);
            			}
            		}*/
            		curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'[0].'+details[i]+'"');
            		curHtml=curHtml.replace(new RegExp(details[i]+'.\\$value','ig'),'');              	    
                }
                
            	curHtml=curHtml.replace(new RegExp('【(\\d+|s?)】','ig'),'【1】');
            	$con.append(curHtml);
            	$con.find(options.sysindex).attr("value","1");
            }           
            if (options.required)
			    var btn='<tr class="bt"><td style="border:solid 1px #78b0dc;" colspan="'+($items.find('td').length+1)+'">&nbsp;<input type="hidden" class="{required:true,messages:{required:\'至少有一条明细！\'}}" value="1" /><input type="button" class="reverse" value="全选中" /><input type="button" class="addBtn" value="添加"/><input type="button" class="delBtn" value="删除"/></td></tr>';
			else
			    var btn='<tr class="bt"><td style="border:solid 1px #78b0dc;" colspan="'+($items.find('td').length+1)+'">&nbsp;<input type="hidden" value="1" /><input type="button" class="reverse" value="全选中" /><input type="button" class="addBtn" value="添加"/><input type="button" class="delBtn" value="删除"/></td></tr>';
			    //$con.children().after(btn); 
			$con.append(btn);
            // 添加行事件
            $con.find(options.addBtn).bind("click", function () {
            	/*date.push(new Date());
            	date.splice(0,date.length-2);
            	if (date.length > 1
            		&& (date[date.length - 1].getTime() - date[date.length - 2].getTime() < 500))//小于1秒则认为重复提交
            	    return;*/
            	/*if (clicked) return;
            	clicked=true;
            	if ($(options.addBtn).attr("disabled")) return;
            	$(options.addBtn).attr("disabled","disabled");*/
            	var curHtml=unescape(tempHtml);
            	for (i = 0; i < details.length; i++){            		
                	curHtml=curHtml.replace(new RegExp('(\"|\')'+details[i]+'(\"|\')','ig'),'"'+options.beanname+'['+$con.find(":checkbox").length+'].'+details[i]+'"');  
                	curHtml=curHtml.replace(new RegExp('name(\\s*)=(\\s*)'+details[i],'ig'),'name="'+options.beanname+'['+$con.find(":checkbox").length+'].'+details[i]+'"');
                	curHtml=curHtml.replace(new RegExp(details[i]+'.\\$value','ig'),'');
                	//$(curHtml).find(options.sysindex).attr("value",j+1)
                	//curHtml=curHtml.replace(new RegExp('\\$sysindex','ig'),String($con.find(":checkbox").length+1));
                }      
                curHtml=curHtml.replace(new RegExp('【(\\d+|s?)】','ig'),'【'+String($con.find(":checkbox").length+1)+'】');
                $(".bt").before(curHtml); 
                $(".bt").find("input[type='hidden']").attr("value","1");
                for ( j= 0; j < $con.find(":checkbox").length; j++){					
					var tr=$($con.find(":checkbox")[j]).parent().parent();					
					$(tr[0]).find(options.sysindex).attr("value",j+1);
                }
                /*$(options.addBtn).removeAttr("disabled");
                clicked=false;*/                
            }); 
            // 删除行事件
            $con.find(options.delBtn).bind("click", function () {
            	/*date.push(new Date());
            	date.splice(0,date.length-2);
            	if (date.length > 1
            		&& (date[date.length - 1].getTime() - date[date.length - 2].getTime() < 500))//小于1秒则认为重复提交
            	    return;*/
            	/*if ($(options.delBtn).attr("disabled")) return;
            	$(options.delBtn).attr("disabled","disabled");*/
				var a = $con.find(":checkbox:checked");
				if (a.length > 2){
					if(confirm("确定要删除多个表格吗？")){
						$con.find(":checkbox:checked").parent().parent().remove();
					}
				}else {
						if(a.length < 1) {alert("至少选一行！")}else {
							$con.find(":checkbox:checked").parent().parent().remove();
						}
				}
				if ($con.find(":checkbox").length==0){
					$(".bt").find("input[type='hidden']").attr("value","");
					$con.find(options.addBtn).click();
				}else{
					for ( j= 0; j < $con.find(":checkbox").length; j++){					
						var tr=$($con.find(":checkbox")[j]).parent().parent();
						var index=$(tr[0]).find(options.sysindex).attr("value");
						$(tr[0]).find(options.sysindex).attr("value",j+1);
						var vdetails=new Array([details.length]);
						for (i = 0; i < details.length; i++){
							try{
								vdetails[i]=document.getElementsByName(options.beanname+'['+(index-1)+'].'+details[i])[0].value;
							}catch(e){
								vdetails[i]=null;
							}						
						}
						var ctr=tr[0].innerHTML;
						for (i = 0; i < details.length; i++){  	
							//ctr=ctr.replace(options.beanname+'['+(index-1)+'].'+details[i],options.beanname+'['+j+'].'+details[i]);
							ctr=ctr.replace(new RegExp(options.beanname+'\\[(\\d+)\\].'+details[i],'ig'),options.beanname+'['+j+'].'+details[i]);  
							ctr=ctr.replace(new RegExp('name(\\s*)=(\\s*)'+options.beanname+'\\[(\\d+)\\].'+details[i],'ig'),'name='+options.beanname+'['+j+'].'+details[i]);	
				        }
		                ctr=ctr.replace(new RegExp('【(\\d+|s?)】','ig'),'【'+String(j+1)+'】');
						$(tr[0]).empty();
						$(tr[0]).append(ctr);
						for (i = 0; i < details.length; i++){
							try{
								document.getElementsByName(options.beanname+'['+j+'].'+details[i])[0].value=vdetails[i];
							}catch(e){
								;
							}
						}
	                }   
				}
				/*$(options.delBtn).removeAttr("disabled");
				*/
                
            }); 
			$con.find(options.reverse).bind("click", function () {
				isSelected=!isSelected;
				if (isSelected){
					$con.find(options.reverse).attr("value","全反选");
				} else
				    $con.find(options.reverse).attr("value","全选中");
				for (i = 0; i < $con.find(":checkbox").length; i++)
					$con.find(":checkbox")[i].checked=isSelected;
			});
	         /*  $con.find(options.reverse).toggle(//toggle 1.8后不再支持此方法
	        		
					function () {
						$con.find(":checkbox").attr("checked",true);
						},
					function () {
						$con.find(":checkbox").attr("checked",false);
						}						
				);*/
        }
    });     
})(jQuery); 

function suunreplace(src,t,p){
	if (t=="") return src;
	var fp=src.charAt(t[0]);
	var mysrc=src;
	while (fp>=0){
	    for (i=fp+1;i<mysrc.length;i++){
			;
		}
	}
	return src;
}


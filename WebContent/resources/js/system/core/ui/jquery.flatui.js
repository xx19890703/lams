(function($){
    $.fn.extend({
		selectCss:function(ctx){
			this.each(function(){
				if (this.hidden) return; 
				if ($(this).hasClass("JuiCssed")) return;
				swidth=$(this).width();
				sheight=this.offsetHeight;//$(this).attr("style")+margin:-2px;//
				if ($.browser.msie){
					$(this).attr("style","margin-top:-2px;margin-left:-2px;height:"+(sheight+2)+"px;width:"+(swidth+2)+"px;background-color:white;border:none;display:inline-block;padding:0;white-space:nowrap;");
					$(this).wrap('<div style="width:'+swidth+'px; height:'+(sheight-1)+'px; overflow:hidden;border:1px solid #7f9db9;"></div>');
				}else if ($.browser.firefox){
					$(this).attr("style","padding-top:1px;background:transparent;width:"+(swidth+14)+"px; font-size: 12px; border:none; height:"+(sheight-2)+"px;-webkit-appearance: none;");
					$(this).wrap('<div style="width:'+(swidth-2)+'px; height:'+(sheight-2)+'px; overflow:hidden; background:white url('+ctx+'/droparrow.gif) no-repeat right center;border:1px solid #7f9db9;"></div>');
				}else if ($.browser.msie11){
					$(this).attr("style","padding-top:1px;background:transparent;width:"+(swidth+18)+"px; font-size: 12px; border:none; height:"+(sheight-2)+"px;-webkit-appearance: none;");
					$(this).wrap('<div style="width:'+(swidth)+'px; height:'+(sheight-2)+'px; overflow:hidden; background:white url('+ctx+'/droparrow.gif) no-repeat right center;border:1px solid #7f9db9;"></div>');
				} else{
					$(this).attr("style","padding-top:1px;background:transparent;width:"+(swidth+16)+"px; font-size: 12px; border:none; height:"+(sheight-2)+"px;-webkit-appearance: none;");
					$(this).wrap('<div style="width:'+(swidth)+'px; height:'+(sheight-2)+'px; overflow:hidden; background:white url('+ctx+'/droparrow.gif) no-repeat right center;border:1px solid #7f9db9;"></div>');
				}
				$(this).addClass("JuiCssed");//防止重复处理	
			})		        
		},
		dataSelectCss:function(ctx){
			this.each(function(){
				if (this.hidden) return;
			    if ($(this).hasClass("JuiCssed")) return;
				swidth=$(this).width();
				sheight=this.offsetHeight;
				var html='<span style="background-image:url('+ctx+'/dataa.png);'+
	             'background-repeat:no-repeat;background-position:right;background-color:white;border:1px solid #7f9db9;'+
				 'display:inline-block;padding:0;white-space:nowrap;width:'+swidth+'px;height:'+(sheight-2)+'px;cursor:pointer;text-align:left;" ';
				if ($(this).attr("onclick")){
					html=html+'onclick="'+$(this).attr("onclick").replace(new RegExp('this','i'),"this.firstChild");
				}
				$(this).wrap(html=html+'"></span>');
				//ostyle=$(this).attr("style");	
                //if (!ostyle) ostyle="";				
				$(this).attr("style","border:0px;cursor:pointer;height:"+(sheight-3)+"px;width:"+(swidth-17)+"px;");//+ostyle
				$(this).attr("onclick","");	
				$(this).attr("readonly","readonly");
				$(this).addClass("JuiCssed");//防止重复处理	
			})			
		},
		datetimeCss:function(ctx){
			this.each(function(){
				if (this.hidden) return;
				if ($(this).hasClass("JuiCssed")) return;
				swidth=$(this).width();
				sheight=this.offsetHeight;
				wrapspan='<span style="background-image:url('+ctx+'/calendar.png);'+
						 'background-repeat:no-repeat;background-position:right;background-color:white;border:1px solid #7f9db9;'+
						 'display:inline-block;padding:0;white-space:nowrap;width:'+swidth+'px;height:'+(sheight-2)+'px;cursor:pointer;text-align:left;" ';
				if ($(this).attr("onclick")){			
					wrapspan=wrapspan+'onclick="'+$(this).attr("onclick").replace(new RegExp('this','i'),"this.firstChild")+'"></span>';
					$(this).removeAttr("onclick");					
				} else{
					wrapspan=wrapspan+'onclick="var actualLeft=this.offsetLeft;var current=this.offsetParent;while(current!==null){'+
　　　　　　                          'actualLeft+=current.offsetLeft;current=current.offsetParent;}'+
									  'if ((event?event:window.event).clientX>this.firstChild.clientWidth+actualLeft){this.firstChild.onclick();}"></span>';
				}	
				$(this).wrap(wrapspan);
				//ostyle=$(this).attr("style");	
                //if (!ostyle) ostyle="";				
				$(this).attr("style","border:0px;cursor:pointer;height:"+(sheight-3)+"px;width:"+(swidth-17)+"px;");//+ostyle
				$(this).attr("readonly","readonly");
				$(this).addClass("JuiCssed");//防止重复处理				
			})			
		},
		datetimeWCss:function(ctx){
			this.each(function(){
				if (this.hidden) return;
				if ($(this).hasClass("JuiCssed")) return;
				swidth=$(this).width();
				sheight=this.offsetHeight;
				wrapspan='<span style="background-image:url('+ctx+'/calendar.png);'+
						 'background-repeat:no-repeat;background-position:right;background-color:white;border:1px solid #7f9db9;'+
						 'display:inline-block;padding:0;white-space:nowrap;width:'+swidth+'px;height:'+(sheight-2)+'px;cursor:pointer;text-align:left;" ';
				if ($(this).attr("onclick")){			
					wrapspan=wrapspan+'onclick="var actualLeft=this.offsetLeft;var current=this.offsetParent;while(current!==null){'+
                    'actualLeft+=current.offsetLeft;current=current.offsetParent;}'+
					  'if ((event?event:window.event).clientX>this.firstChild.clientWidth+actualLeft){'+$(this).attr("onclick").replace(new RegExp('this','i'),"this.firstChild")+'}"></span>';
					$(this).removeAttr("onclick");					
				} else{
					wrapspan=wrapspan+'onclick="var actualLeft=this.offsetLeft;var current=this.offsetParent;while(current!==null){'+
                    'actualLeft+=current.offsetLeft;current=current.offsetParent;}'+
						  'if ((event?event:window.event).clientX>this.firstChild.clientWidth+actualLeft){this.firstChild.onclick();}"></span>';
               	}	  	
				$(this).wrap(wrapspan);
				//ostyle=$(this).attr("style");	
                //if (!ostyle) ostyle="";				
				$(this).attr("style","border:0px;cursor:pointer;height:"+(sheight-3)+"px;width:"+(swidth-17)+"px;");//+ostyle
				$(this).addClass("JuiCssed");//防止重复处理				
			})			
		},
		inputCss:function(){		
			if ($.browser.msie ){
				//alert( $.browser.version );	
				//input[type="text"],input[type="password"]	样式在IE下不起作用			
				this.each(function(){  	
					if (this.hidden) return;
					if ($(this).hasClass("JuiCssed")) return;
					if ($(this).attr("class")){
						strclass=$(this).attr("class");
						//x-form-... ext的input[type='text']css
						if (strclass.indexOf("x-form-")<0&&strclass.indexOf("datee")<0&&strclass.indexOf("shuju")<0){
							strstyle="";
							if ($(this).attr("style")) {
								strstyle=$(this).attr("style");
							}						      
							$(this).attr("style","border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;height:20px;text-align:left;"+strstyle);
						}
					}else{
						strstyle="";
						if ($(this).attr("style")) {
							strstyle=$(this).attr("style");
						}
						$(this).attr("style","border:1px solid #7f9db9;display:inline-block;padding:0;white-space:nowrap;height:20px;text-align:left;"+strstyle);
					}
					$(this).addClass("JuiCssed");//防止重复处理					
				})
			} else{
				//修正input[type="text"],input[type="password"]等对Ext样式的影响
				$(".x-form-arrow-trigger").height('19px');
			}					
		}		 
	})
})(jQuery); 
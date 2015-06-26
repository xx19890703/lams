(function($) {
	var eventName = {
		nextMonth : 1,
		previousMonth : -1,
		nextYear : 2,
		previousYear : -2,
		ok : -3
	};
	$.fn.suunDateTime = function(options) {
		$.fn.suunDateTime.defaults = {
			displayArea : null,
			button : null,
			eventName : "click",
			ifFormat : "%Y-%m-%d",
			daFormat : "%Y-%m-%d",
			startDate : false,
			endDate : false,
			singleClick : true,
			dateStatusFunc : null,
			dateText : null,
			firstDay : null,
			align : "Br",
			range : [ 1900, 2999 ],
			weekNumbers : true,
			flat : null,
			flatCallback : null,
			onSelect : null,
			onClose : null,
			onUpdate : null,
			date : null,
			showsTime : false,
			timeFormat : "24",
			electric : true,
			step : 2,
			position : null,
			cache : false,
			showOthers : false,
			multiple : null,
			debug : false
		};
		var opts = $.extend({}, $.fn.suunDateTime.defaults, options);
		return this
				.each(function() {
					var this_inputField = this;
					var this_displayArea = null;
					var this_button = null;
					var this_flat = null;
					if (opts.displayArea) {
						try {
							this_displayArea = eval("jQuery(this)"
									+ opts.displayArea + ".get(0);")
						} catch (err) {
							opts.displayArea = null
						}
					}
					if (opts.button) {
						try {
							this_button = eval("jQuery(this)" + opts.button+ ".get(0);")
						} catch (err) {
							opts.button = null
						}
					}
					if (opts.flat) {
						try {
							this_flat = eval("jQuery(this)" + opts.flat+ ".get(0);")
						} catch (err) {
							opts.flat = null
						}
					}
					if (!(this_flat || opts.multiple || this_inputField
							|| this_displayArea || this_button)) {
						log("opts");
						return
					}
					function onSelect(cal, dStr, action) {
						var p = cal.opts;
						var update = (cal.dateClicked || p.electric);
						if (update && this_inputField) {
							if (action == 3) {
								this_inputField.value = "";
								this_inputField.realValue = null
							} else {
								this_inputField.value = cal.date.print(p.ifFormat);
								this_inputField.realValue = cal.date
							}
							if (typeof this_inputField.onchange == "function") {
								this_inputField.onchange()
							}
						}
						if (update && this_displayArea) {
							this_displayArea.innerHTML = cal.date.print(p.daFormat)
						}
						if (update && typeof p.onUpdate == "function") {
							p.onUpdate(cal)
						}
						if (update && p.flat) {
							if (typeof p.flatCallback == "function") {
								p.flatCallback(cal)
							}
						}
						if (update && p.singleClick && cal.dateClicked) {
							cal.callCloseHandler()
						}
					}
					if (this_flat != null) {
						var cal = new Calendar(opts.firstDay, opts.date,
								opts.onSelect || onSelect);
						cal.showsOtherMonths = opts.showOthers;
						cal.showsTime = opts.showsTime;
						cal.time24 = (opts.timeFormat == "24");
						cal.opts = opts;
						cal.weekNumbers = opts.weekNumbers;
						cal.setRange(opts.range[0], opts.range[1]);
						cal.setDateStatusHandler(opts.dateStatusFunc);
						cal.getDateText = opts.dateText;
						if (opts.ifFormat) {
							cal.setDateFormat(opts.ifFormat)
						}
						if (this_inputField
								&& typeof this_inputField.value == "string") {
							log("rar")
						}
						cal.create(this_flat);
						cal.show();
						return
					}
					var triggerEl = this_button || this_displayArea
							|| this_inputField;
					triggerEl["on" + opts.eventName] = function() {
						log("clicked");
						var dateEl = this_inputField || this_displayArea;
						var dateFmt = this_inputField ? opts.ifFormat
								: opts.daFormat;
						var mustCreate = false;
						var cal = window.calendar;
						if (dateEl) {
							opts.date = Date.parseDateTime(dateEl.value
									|| dateEl.innerHTML, dateFmt)
						}
						if (!(cal && opts.cache)) {
							window.calendar = cal = new Calendar(opts.firstDay,
									opts.date, opts.onSelect || onSelect,
									opts.onClose || function(cal) {
										cal.hide()
									});
							cal.showsTime = opts.showsTime;
							cal.time24 = (opts.timeFormat == "24");
							cal.weekNumbers = opts.weekNumbers;
							mustCreate = true
						} else {
							if (opts.date) {
								cal.setDate(opts.date)
							}
							cal.hide()
						}
						if (opts.multiple) {
							cal.multiple = {};
							for (var i = opts.multiple.length; --i >= 0;) {
								var d = opts.multiple[i];
								var ds = d.print("%Y%m%d");
								cal.multiple[ds] = d
							}
						}
						cal.showsOtherMonths = opts.showOthers;
						cal.yearStep = opts.step;
						cal.setRange(opts.range[0], opts.range[1]);
						cal.opts = opts;
						cal.setDateStatusHandler(opts.dateStatusFunc);
						cal.getDateText = opts.dateText;
						cal.setDateFormat(dateFmt);
						if (mustCreate) {
							cal.create()
						}
						cal.refresh();
						if (!opts.position) {
							cal.showAtElement(this_button || this_inputField || this_displayArea, opts.align)
						} else {
							cal.showAt(opts.position[0], opts.position[1])
						}
						return false
					}
				});
		function log(msg) {
			if (opts.debug) {
				window.loadFirebugConsole();
				if (window.console && window.console.log) {
					window.console.log("suunDateTime: " + msg)
				}
			}
		}
	}
})(jQuery);

//zyfang
var AllCal=null;
var dateEl=null;
SuunCalendar={
		Cal:null,		
		defaults : {
				displayArea:  null,
				button:       null,
				eventName:    "click",
				ifFormat:     "%Y-%m-%d",
				daFormat:     "%Y-%m-%d",
				singleClick:  true,
				dateStatusFunc: null,
				dateText:     null,
				firstDay:     null,
				align:        "Br",
				range:        [1900, 2999],
				weekNumbers:  true,
				flat:         null,
				flatCallback: null,
				onSelect:     null,
				onClose:      null,
				onUpdate:     null,
				date:         null,
				showsTime:    false,
				timeFormat:   "24",
				electric:     true,
				step:         2,
				position:     null,
				cache:        false,
				showOthers:   false,
				multiple:     null,
				debug:        false
		},
		iniCalendar:function(){
			if (AllCal==null){
				AllCal= new Calendar()
			}				
			this.Cal = AllCal;
		},
		show:function(triggerEl,options){	
			dateEl=null;
			var opts = $.extend({}, this.defaults, options);
			var dateFmt = opts.ifFormat;
			var cdate = null;
			dateEl = opts.inputField || opts.displayArea;
			if (!dateEl){
				if (($(triggerEl)[0].type=="text")||($(triggerEl)[0].type=="textarea"))
					dateEl=triggerEl;
			}
			if (dateEl)
			   cdate = Date.parseDate(dateEl.value || dateEl.innerHTML, dateFmt);			
			this.Cal.showsTime = opts.showsTime;
			this.Cal.showsOtherMonths = opts.showOthers;
			this.Cal.time24 = (opts.timeFormat == "24");
			this.Cal.yearStep = opts.step;
			this.Cal.setRange(opts.range[0], opts.range[1]);
			this.Cal.opts = opts;
			this.Cal.weekNumbers = opts.weekNumbers;
			this.Cal.setDateStatusHandler(opts.dateStatusFunc);
			this.Cal.getDateText = opts.dateText; 
			this.Cal.onSelected=opts.onSelected || function onSelect(cal) {
				if (dateEl){
					var p = cal.opts;
				    var update = (cal.dateClicked || p.electric);
				    if (update && $(dateEl)[0].type=="text"){
				    	dateEl.value = cal.date.print(p.ifFormat);
						if (typeof dateEl.onchange == "function")
							dateEl.onchange();
				    }
				    if (update && $(dateEl)[0].type=="textarea")
				    	dateEl.innerHTML = cal.date.print(p.ifFormat);
					if (update && typeof p.onUpdate == "function")
						p.onUpdate(cal);
					if (update && p.flat) {
						if (typeof p.flatCallback == "function")
							p.flatCallback(cal);
					}
					if (update && p.singleClick && cal.dateClicked)
						cal.callCloseHandler();
				}				
			};
			this.Cal.onClose=opts.onClose || function(cal) { cal.hide(); }			
			this.Cal.create();
			if (cdate){
				this.Cal.setDate(cdate);
			}			
			this.Cal.refresh();
			this.Cal.showAtElement(triggerEl);
		}		
}
//zyfang

Calendar = function(d, c, f, a) {
	this.activeDiv = null;
	this.currentDateEl = null;
	this.getDateStatus = null;
	this.getDateToolTip = null;
	this.getDateText = null;
	this.timeout = null;
	this.onSelected = f || null;
	this.onClose = a || null;
	this.dragging = false;
	this.hidden = false;
	this.minYear = 1900;
	this.maxYear = 2099;
	this.dateFormat = Calendar._TT.DEF_DATE_FORMAT;
	this.ttDateFormat = Calendar._TT.TT_DATE_FORMAT;
	this.isPopup = true;
	this.weekNumbers = true;
	this.firstDayOfWeek = typeof d == "number" ? d : Calendar._FD;
	this.showsOtherMonths = false;
	this.dateStr = c;
	this.ar_days = null;
	this.showsTime = false;
	this.time24 = true;
	this.yearStep = 2;
	this.hiliteToday = true;
	this.multiple = null;
	this.table = null;
	this.element = null;
	this.shadow = null;
	this.tbody = null;
	this.firstdayname = null;
	this.monthsCombo = null;
	this.yearsCombo = null;
	this.hilitedMonth = null;
	this.activeMonth = null;
	this.hilitedYear = null;
	this.activeYear = null;
	this.dateClicked = false;
	if (typeof Calendar._SDN == "undefined") {
		if (typeof Calendar._SDN_len == "undefined") {
			Calendar._SDN_len = 3
		}
		var b = new Array();
		for (var e = 8; e > 0;) {
			b[--e] = Calendar._DN[e].substr(0, Calendar._SDN_len)
		}
		Calendar._SDN = b;
		if (typeof Calendar._SMN_len == "undefined") {
			Calendar._SMN_len = 3
		}
		b = new Array();
		for (var e = 12; e > 0;) {
			b[--e] = Calendar._MN[e].substr(0, Calendar._SMN_len)
		}
		Calendar._SMN = b
	}
};
Calendar._C = null;
Calendar.is_ie = (/msie/i.test(navigator.userAgent) && !/opera/i
		.test(navigator.userAgent));
Calendar.is_ie5 = (Calendar.is_ie && /msie 5\.0/i.test(navigator.userAgent));
Calendar.is_opera = /opera/i.test(navigator.userAgent);
Calendar.is_khtml = /Konqueror|Safari|KHTML/i.test(navigator.userAgent);
Calendar.getAbsolutePos = function(e) {
	var a = 0, d = 0;
	var c = /^div$/i.test(e.tagName);
	if (c && e.scrollLeft) {
		a = e.scrollLeft
	}
	if (c && e.scrollTop) {
		d = e.scrollTop
	}
	var f = {
		x : e.offsetLeft - a,
		y : e.offsetTop - d
	};
	if (e.offsetParent) {
		var b = this.getAbsolutePos(e.offsetParent);
		f.x += b.x;
		f.y += b.y
	}
	return f
};
Calendar.isRelated = function(c, a) {
	var d = a.relatedTarget;
	if (!d) {
		var b = a.type;
		if (b == "mouseover") {
			d = a.fromElement
		} else {
			if (b == "mouseout") {
				d = a.toElement
			}
		}
	}
	while (d) {
		if (d == c) {
			return true
		}
		d = d.parentNode
	}
	return false
};
Calendar.removeClass = function(e, d) {
	if (!(e && e.className)) {
		return
	}
	var a = e.className.split(" ");
	var b = new Array();
	for (var c = a.length; c > 0;) {
		if (a[--c] != d) {
			b[b.length] = a[c]
		}
	}
	e.className = b.join(" ")
};
Calendar.addClass = function(b, a) {
	Calendar.removeClass(b, a);
	b.className += " " + a
};
Calendar.getElement = function(a) {
	var b = Calendar.is_ie ? window.event.srcElement : a.currentTarget;
	while (b.nodeType != 1 || /^div$/i.test(b.tagName)) {
		b = b.parentNode
	}
	return b
};
Calendar.getTargetElement = function(a) {
	var b = Calendar.is_ie ? window.event.srcElement : a.target;
	while (b.nodeType != 1) {
		b = b.parentNode
	}
	return b
};
Calendar.stopEvent = function(a) {
	a || (a = window.event);
	if (Calendar.is_ie) {
		a.cancelBubble = true;
		a.returnValue = false
	} else {
		a.preventDefault();
		a.stopPropagation()
	}
	return false
};
Calendar.addEvent = function(a, c, b) {
	if (a.attachEvent) {
		a.attachEvent("on" + c, b)
	} else {
		if (a.addEventListener) {
			a.addEventListener(c, b, true)
		} else {
			a["on" + c] = b
		}
	}
};
Calendar.removeEvent = function(a, c, b) {
	if (a.detachEvent) {
		a.detachEvent("on" + c, b)
	} else {
		if (a.removeEventListener) {
			a.removeEventListener(c, b, true)
		} else {
			a["on" + c] = null
		}
	}
};
Calendar.createElement = function(c, b) {
	var a = null;
	if (document.createElementNS) {
		a = document.createElementNS("http://www.w3.org/1999/xhtml", c)
	} else {
		a = document.createElement(c)
	}
	if (typeof b != "undefined") {
		b.appendChild(a)
	}
	return a
};
Calendar._add_evs = function(el) {
	with (Calendar) {
		addEvent(el, "mouseover", dayMouseOver);
		addEvent(el, "mousedown", dayMouseDown);
		addEvent(el, "mouseout", dayMouseOut);
		if (is_ie) {
			addEvent(el, "dblclick", dayMouseDblClick);
			el.setAttribute("unselectable", true)
		}
	}
};
Calendar.findMonth = function(a) {
	if (typeof a.month != "undefined") {
		return a
	} else {
		if (typeof a.parentNode.month != "undefined") {
			return a.parentNode
		}
	}
	return null
};
Calendar.findYear = function(a) {
	if (typeof a.year != "undefined") {
		return a
	} else {
		if (typeof a.parentNode.year != "undefined") {
			return a.parentNode
		}
	}
	return null
};
Calendar.showMonthsCombo = function() {
	var e = Calendar._C;
	if (!e) {
		return false
	}
	var e = e;
	var f = e.activeDiv;
	var d = e.monthsCombo;
	if (e.hilitedMonth) {
		Calendar.removeClass(e.hilitedMonth, "hilite")
	}
	if (e.activeMonth) {
		Calendar.removeClass(e.activeMonth, "active")
	}
	var c = e.monthsCombo.getElementsByTagName("div")[e.date.getMonth()];
	Calendar.addClass(c, "active");
	e.activeMonth = c;
	var b = d.style;
	b.display = "block";
	if (f.navtype < 0) {
		b.left = f.offsetLeft + "px"
	} else {
		var a = d.offsetWidth;
		if (typeof a == "undefined") {
			a = 50
		}
		b.left = (f.offsetLeft + f.offsetWidth - a) + "px"
	}
	b.top = (f.offsetTop + f.offsetHeight) + "px"
};
Calendar.showYearsCombo = function(d) {
	var a = Calendar._C;
	if (!a) {
		return false
	}
	var a = a;
	var c = a.activeDiv;
	var f = a.yearsCombo;
	if (a.hilitedYear) {
		Calendar.removeClass(a.hilitedYear, "hilite")
	}
	if (a.activeYear) {
		Calendar.removeClass(a.activeYear, "active")
	}
	a.activeYear = null;
	var b = a.date.getFullYear() + (d ? 1 : -1);
	var j = f.firstChild;
	var h = false;
	for (var e = 12; e > 0; --e) {
		if (b >= a.minYear && b <= a.maxYear) {
			j.innerHTML = b;
			j.year = b;
			j.style.display = "block";
			h = true
		} else {
			j.style.display = "none"
		}
		j = j.nextSibling;
		b += d ? a.yearStep : -a.yearStep
	}
	if (h) {
		var k = f.style;
		k.display = "block";
		if (c.navtype < 0) {
			k.left = c.offsetLeft + "px"
		} else {
			var g = f.offsetWidth;
			if (typeof g == "undefined") {
				g = 50
			}
			k.left = (c.offsetLeft + c.offsetWidth - g) + "px"
		}
		k.top = (c.offsetTop + c.offsetHeight) + "px"
	}
};
Calendar.tableMouseUp = function(ev) {
	var cal = Calendar._C;
	if (!cal) {
		return false
	}
	if (cal.timeout) {
		clearTimeout(cal.timeout)
	}
	var el = cal.activeDiv;
	if (!el) {
		return false
	}
	var target = Calendar.getTargetElement(ev);
	ev || (ev = window.event);
	Calendar.removeClass(el, "active");
	if (target == el || target.parentNode == el) {
		Calendar.cellClick(el, ev)
	}
	var mon = Calendar.findMonth(target);
	var date = null;
	if (mon) {
		date = new Date(cal.date);
		if (mon.month != date.getMonth()) {
			date.setMonth(mon.month);
			cal.setDate(date);
			cal.dateClicked = false;
			cal.callHandler()
		}
	} else {
		var year = Calendar.findYear(target);
		if (year) {
			date = new Date(cal.date);
			if (year.year != date.getFullYear()) {
				date.setFullYear(year.year);
				cal.setDate(date);
				cal.dateClicked = false;
				cal.callHandler()
			}
		}
	}
	with (Calendar) {
		removeEvent(document, "mouseup", tableMouseUp);
		removeEvent(document, "mouseover", tableMouseOver);
		removeEvent(document, "mousemove", tableMouseOver);
		cal._hideCombos();
		_C = null;
		return stopEvent(ev)
	}
};
Calendar.tableMouseOver = function(n) {
	var a = Calendar._C;
	if (!a) {
		return
	}
	var c = a.activeDiv;
	var j = Calendar.getTargetElement(n);
	if (j == c || j.parentNode == c) {
		Calendar.addClass(c, "hilite active");
		Calendar.addClass(c.parentNode, "rowhilite")
	} else {
		if (typeof c.navtype == "undefined"
				|| (c.navtype != 50 && (c.navtype == 0 || Math.abs(c.navtype) > 2))) {
			Calendar.removeClass(c, "active")
		}
		Calendar.removeClass(c, "hilite");
		Calendar.removeClass(c.parentNode, "rowhilite")
	}
	n || (n = window.event);
	if (c.navtype == 50 && j != c) {
		var m = Calendar.getAbsolutePos(c);
		var p = c.offsetWidth;
		var o = n.clientX;
		var q;
		var l = true;
		if (o > m.x + p) {
			q = o - m.x - p;
			l = false
		} else {
			q = m.x - o
		}
		if (q < 0) {
			q = 0
		}
		var f = c._range;
		var h = c._current;
		var g = Math.floor(q / 10) % f.length;
		for (var e = f.length; --e >= 0;) {
			if (f[e] == h) {
				break
			}
		}
		while (g-- > 0) {
			if (l) {
				if (--e < 0) {
					e = f.length - 1
				}
			} else {
				if (++e >= f.length) {
					e = 0
				}
			}
		}
		var b = f[e];
		c.innerHTML = b;
		a.onUpdateTime()
	}
	var d = Calendar.findMonth(j);
	if (d) {
		if (d.month != a.date.getMonth()) {
			if (a.hilitedMonth) {
				Calendar.removeClass(a.hilitedMonth, "hilite")
			}
			Calendar.addClass(d, "hilite");
			a.hilitedMonth = d
		} else {
			if (a.hilitedMonth) {
				Calendar.removeClass(a.hilitedMonth, "hilite")
			}
		}
	} else {
		if (a.hilitedMonth) {
			Calendar.removeClass(a.hilitedMonth, "hilite")
		}
		var k = Calendar.findYear(j);
		if (k) {
			if (k.year != a.date.getFullYear()) {
				if (a.hilitedYear) {
					Calendar.removeClass(a.hilitedYear, "hilite")
				}
				Calendar.addClass(k, "hilite");
				a.hilitedYear = k
			} else {
				if (a.hilitedYear) {
					Calendar.removeClass(a.hilitedYear, "hilite")
				}
			}
		} else {
			if (a.hilitedYear) {
				Calendar.removeClass(a.hilitedYear, "hilite")
			}
		}
	}
	return Calendar.stopEvent(n)
};
Calendar.tableMouseDown = function(a) {
	if (Calendar.getTargetElement(a) == Calendar.getElement(a)) {
		return Calendar.stopEvent(a)
	}
};
Calendar.calDragIt = function(b) {
	var c = Calendar._C;
	if (!(c && c.dragging)) {
		return false
	}
	var e;
	var d;
	if (Calendar.is_ie) {
		d = window.event.clientY + document.body.scrollTop;
		e = window.event.clientX + document.body.scrollLeft
	} else {
		e = b.pageX;
		d = b.pageY
	}
	c.hideShowCovered();
	var a = c.element.style;
	a.left = (e - c.xOffs) + "px";
	a.top = (d - c.yOffs) + "px";
	return Calendar.stopEvent(b)
};
Calendar.calDragEnd = function(ev) {
	var cal = Calendar._C;
	if (!cal) {
		return false
	}
	cal.dragging = false;
	with (Calendar) {
		removeEvent(document, "mousemove", calDragIt);
		removeEvent(document, "mouseup", calDragEnd);
		tableMouseUp(ev)
	}
	cal.hideShowCovered()
};
Calendar.finish = function() {
	$(".calendar-close").each(function(a, b) {
		Calendar.cellClick($(".calendar-close")[a])
	})
};
Calendar.dayMouseDown = function(ev) {
	var el = Calendar.getElement(ev);
	if (el.disabled) {
		return false
	}
	var cal = el.calendar;
	cal.activeDiv = el;
	Calendar._C = cal;
	if (el.navtype != 300) {
		with (Calendar) {
			if (el.navtype == 50) {
				el._current = el.innerHTML;
				addEvent(document, "mousemove", tableMouseOver)
			} else {
				addEvent(document, Calendar.is_ie5 ? "mousemove" : "mouseover",
						tableMouseOver)
			}
			addClass(el, "hilite active");
			addEvent(document, "mouseup", tableMouseUp)
		}
	} else {
		if (cal.isPopup) {
			cal._dragStart(ev)
		}
	}
	if (el.navtype == -1 || el.navtype == 1) {
		if (cal.timeout) {
			clearTimeout(cal.timeout)
		}
		cal.timeout = setTimeout("Calendar.showMonthsCombo()", 250)
	} else {
		if (el.navtype == -2 || el.navtype == 2) {
			if (cal.timeout) {
				clearTimeout(cal.timeout)
			}
			cal.timeout = setTimeout(
					(el.navtype > 0) ? "Calendar.showYearsCombo(true)"
							: "Calendar.showYearsCombo(false)", 250)
		} else {
			cal.timeout = null
		}
	}
	return Calendar.stopEvent(ev)
};
Calendar.dayMouseDblClick = function(a) {
	Calendar.cellClick(Calendar.getElement(a), a || window.event);
	if (Calendar.is_ie) {
		document.selection.empty()
	}
};
Calendar.dayMouseOver = function(b) {
	var a = Calendar.getElement(b);
	if (Calendar.isRelated(a, b) || Calendar._C || a.disabled) {
		return false
	}
	if (a.ttip) {
		if (a.ttip.substr(0, 1) == "_") {
			a.ttip = a.caldate.print(a.calendar.ttDateFormat)
					+ a.ttip.substr(1)
		}
		a.calendar.tooltips.innerHTML = a.ttip
	}
	if (a.navtype != 300) {
		Calendar.addClass(a, "hilite");
		if (a.caldate) {
			Calendar.addClass(a.parentNode, "rowhilite")
		}
	}
	return Calendar.stopEvent(b)
};
Calendar.dayMouseOut = function(ev) {
	with (Calendar) {
		var el = getElement(ev);
		if (isRelated(el, ev) || _C || el.disabled) {
			return false
		}
		removeClass(el, "hilite");
		if (el.caldate) {
			removeClass(el.parentNode, "rowhilite")
		}
		return stopEvent(ev)
	}
};
Calendar.cellClick = function(b, p) {
	var k = b.calendar;
	var f = false;
	var a = false;
	var q = null;
	if (typeof b.navtype == "undefined") {
		if (k.currentDateEl) {
			Calendar.removeClass(k.currentDateEl, "selected");
			Calendar.addClass(b, "selected");
			f = (k.currentDateEl == b);
			if (!f) {
				k.currentDateEl = b
			}
		}
		k.date.setDateOnly(b.caldate);
		q = k.date;
		var h = !(k.dateClicked = !b.otherMonth);
		if (!h && !k.currentDateEl) {
			k._toggleMultipleDate(new Date(q))
		} else {
			a = !b.disabled
		}
		if (h) {
			k._init(k.firstDayOfWeek, q)
		}
	} else {
		if (b.navtype == 200) {
			Calendar.removeClass(b, "hilite");
			k.callCloseHandler();
			return
		}
		q = new Date(k.date);
		if (b.navtype == -3) {
		}
		if (b.navtype == 3) {
		}
		if (b.navtype == 0) {
			q.setDateOnly(new Date())
		}
		k.dateClicked = false;
		var j = q.getFullYear();
		var o = q.getMonth();
		function i(t) {
			var u = q.getDate();
			var s = q.getMonthDays(t);
			if (u > s) {
				q.setDate(s)
			}
			q.setMonth(t)
		}
		function r() {
			var t = k.opts.startDate;
			if (!t) {
				q.setFullYear(j - 1);
				return
			}
			var s = new Date(t);
			if (j > s.getFullYear()) {
				q.setFullYear(j - 1);
				if (j - 1 == s.getFullYear() && o - 1 < s.getMonth()) {
					q.setMonth(s.getMonth())
				}
			}
		}
		function g() {
			var s = k.opts.endDate;
			if (!s) {
				q.setFullYear(j + 1);
				return
			}
			var t = new Date(s);
			if (j < t.getFullYear()) {
				q.setFullYear(j + 1);
				if (j + 1 == t.getFullYear() && (o > t.getMonth())) {
					q.setMonth(t.getMonth());
					q.setDate(0)
				}
			}
		}
		function n() {
			var t = k.opts.startDate;
			if (!t) {
				if (o > 0) {
					i(o - 1)
				} else {
					q.setFullYear(j - 1);
					i(11)
				}
				return
			}
			var s = new Date(t);
			if (j > s.getFullYear()) {
				if (o > 0) {
					i(o - 1)
				} else {
					q.setFullYear(j - 1);
					i(11)
				}
			} else {
				if (j == s.getFullYear()) {
					if (o > s.getMonth() && o > 0) {
						i(o - 1)
					}
				}
			}
		}
		function c() {
			var s = k.opts.endDate;
			if (!s) {
				if (o < 11) {
					i(o + 1)
				} else {
					q.setFullYear(j + 1);
					i(0)
				}
				return
			}
			var t = new Date(s);
			if (j < t.getFullYear()) {
				if (o < 11) {
					i(o + 1)
				} else {
					q.setFullYear(j + 1);
					i(0)
				}
			} else {
				if (j == t.getFullYear()) {
					if (o < t.getMonth()) {
						q.setMonth(o + 1)
					}
				}
			}
		}
		function m() {
			if ((typeof k.getDateStatus == "function")
					&& k.getDateStatus(q, q.getFullYear(), q.getMonth(), q
							.getDate())) {
				return false
			}
		}
		function e() {
			var s = b._range;
			var v = b.innerHTML;
			for (var t = s.length; --t >= 0;) {
				if (s[t] == v) {
					break
				}
			}
			if (p && p.shiftKey) {
				if (--t < 0) {
					t = s.length - 1
				}
			} else {
				if (++t >= s.length) {
					t = 0
				}
			}
			var u = s[t];
			b.innerHTML = u;
			k.onUpdateTime();
			return
		}
		function d() {
			var v = k.date.getFullYear();
			var s = k.date.getMonth();
			var t = [ v - 4, v - 3, v - 2, v - 1, v, v + 1, v + 2, v + 3,
					v + 4, v + 5 ];
			if (k.datemenu.selectedmonth) {
				$(k.datemenu.selectedmonth).removeClass("x-date-menu-sel")
			}
			if (k.datemenu.selectedyear) {
				$(k.datemenu.selectedyear).removeClass("x-date-menu-sel")
			}
			for (var u = 0; u < 12; u++) {
				if (u == s) {
					$(k.datemenu.datemenuM[u]).addClass("x-date-menu-sel");
					k.datemenu.selectedmonth = k.datemenu.datemenuM[u];
					k.datemenu.selectedmonthIndex = u
				}
				if (u < 10) {
					k.datemenu.datemenuY[u].innerHTML = "<a href='#'>" + t[u]
							+ "</a>";
					if ($(k.datemenu.datemenuY[u].innerHTML).text() == v) {
						$(k.datemenu.datemenuY[u]).addClass("x-date-menu-sel");
						k.datemenu.selectedyear = k.datemenu.datemenuY[u]
					}
				}
			}
			$(k.datemenu).show("normal");
			$(k.shadow).hide()
		}
		switch (b.navtype) {
		case 400:
			Calendar.removeClass(b, "hilite");
			var l = Calendar._TT.ABOUT;
			if (typeof l != "undefined") {
				l += k.showsTime ? Calendar._TT.ABOUT_TIME : ""
			} else {
				l = 'Help and about box text is not translated into this language.\nIf you know this language and you feel generous please update\nthe corresponding file in "lang" subdir to match calendar-en.js\nand send it back to <mihai_bazon@yahoo.com> to get it into the distribution  ;-)\n\nThank you!\nhttp://dynarch.com/mishoo/calendar.epl\n'
			}
			return;
		case -2:
			r();
			break;
		case -1:
			n();
			break;
		case 1:
			c();
			break;
		case 2:
			g();
			break;
		case -3:
			m();
			break;
		case 100:
			k.setFirstDayOfWeek(b.fdow);
			return;
		case 50:
			e();
			return;
		case 0:
			if ((typeof k.getDateStatus == "function")
					&& k.getDateStatus(q, q.getFullYear(), q.getMonth(), q
							.getDate())) {
				return false
			}
			break;
		case 99:
			d();
			break
		}
		if (!q.equalsTo(k.date)) {
			k.setDate(q);
			a = true
		} else {
			if (b.navtype == 0 || b.navtype == 3 || b.navtype == -3) {
				a = f = true
			}
		}
	}
	if (a) {
		p && k.callHandler(b.navtype)
	}
	if (f) {
		Calendar.removeClass(b, "hilite");
		p && k.callCloseHandler()
	}
};
Calendar.prototype.create = function(b) {
	var e = null;
	if (!b) {
		e = document.getElementsByTagName("body")[0];
		this.isPopup = true
	} else {
		e = b;
		this.isPopup = false
	}
	this.date = this.dateStr ? new Date(this.dateStr) : new Date();
	var o = Calendar.createElement("table");
	this.table = o;
	o.cellSpacing = 0;
	o.cellPadding = 0;
	o.calendar = this;
	Calendar.addEvent(o, "mousedown", Calendar.tableMouseDown);
	var O = Calendar.createElement("div");
	this.element = O;
	O.className = "calendar";
	if (this.isPopup) {
		O.style.position = "absolute";
		O.style.display = "none";
		$(O).css("z-index", "10086")
	}
	O.appendChild(o);
	var t = Calendar.createElement("thead", o);
	var v = null;
	var k = null;
	var F = this;
	var R = function(m, j, i, n) {
		v = Calendar.createElement("td", k);
		v.colSpan = j;
		v.className = n ? (n + " button") : "button";
		if (i != 0 && Math.abs(i) <= 2) {
			v.className += " nav"
		}
		Calendar._add_evs(v);
		v.calendar = F;
		v.navtype = i;
		v.innerHTML = "<div unselectable='on'>" + m + "</div>";
		return v
	};
	k = Calendar.createElement("tr", t);
	k.className = "x-date-top";
	R("", 1, 300);
	R(Calendar._TT.CALENDAR, 6, 300);
	if (this.isPopup) {
		R("&#x00d7;", 1, 200, "calendar-close")
	}
	k = Calendar.createElement("tr", t);
	k.className = "headrow";
	this._nav_pm = R("", 1, -1, "left");
	var S = 6;
	(this.isPopup) && --S;
	(this.weekNumbers) && ++S;
	this.title = R("", S, 99);
	this.title.className = "title";
	this._nav_nm = R("", 1, 1, "right");
	k = Calendar.createElement("tr", t);
	k.className = "daynames";
	if (this.weekNumbers) {
		v = Calendar.createElement("td", k);
		v.className = "name wn";
		v.innerHTML = Calendar._TT.WK
	}
	for (var M = 7; M > 0; --M) {
		v = Calendar.createElement("td", k);
		if (!M) {
			v.navtype = 100;
			v.calendar = this;
			Calendar._add_evs(v)
		}
	}
	this.firstdayname = (this.weekNumbers) ? k.firstChild.nextSibling
			: k.firstChild;
	this._displayWeekdays();
	var D = Calendar.createElement("tbody", o);
	this.tbody = D;
	for (M = 6; M > 0; --M) {
		k = Calendar.createElement("tr", D);
		if (this.weekNumbers) {
			v = Calendar.createElement("td", k)
		}
		for (var L = 7; L > 0; --L) {
			v = Calendar.createElement("td", k);
			v.calendar = this;
			Calendar._add_evs(v)
		}
	}
	if (this.showsTime) {
		k = Calendar.createElement("tr", D);
		k.className = "time";
		v = Calendar.createElement("td", k);
		v.className = "time";
		v.colSpan = 2;
		v.innerHTML = Calendar._TT.TIME || "&nbsp;";
		v = Calendar.createElement("td", k);
		v.className = "time";
		v.colSpan = this.weekNumbers ? 4 : 3;
		(function() {
			function T(ac, ae, ad, af) {
				var aa = Calendar.createElement("span", v);
				aa.className = ac;
				aa.innerHTML = ae;
				aa.calendar = F;
				aa.navtype = 50;
				aa._range = [];
				if (typeof ad != "number") {
					aa._range = ad
				} else {
					for (var ab = ad; ab <= af; ++ab) {
						var Z;
						if (ab < 10 && af >= 10) {
							Z = "0" + ab
						} else {
							Z = "" + ab
						}
						aa._range[aa._range.length] = Z
					}
				}
				Calendar._add_evs(aa);
				return aa
			}
			var X = F.date.getHours();
			var i = F.date.getMinutes();
			var n = F.date.getSeconds();
			var Y = !F.time24;
			var j = (X > 12);
			if (Y && j) {
				X -= 12
			}
			var V = T("hour", X, Y ? 1 : 0, Y ? 12 : 23);
			var U = Calendar.createElement("span", v);
			U.innerHTML = ":";
			U.className = "colon";
			var y = T("minute", i, 0, 59);
			var U = Calendar.createElement("span", v);
			U.innerHTML = ":";
			U.className = "colon";
			var m = T("second", n, 0, 59);
			var W = null;
			v = Calendar.createElement("td", k);
			v.className = "time";
			v.colSpan = 2;
			if (Y) {
				W = T("ampm", j ? "pm" : "am", [ "am", "pm" ])
			} else {
				v.innerHTML = "&nbsp;"
			}
			F.onSetTime = function() {
				var aa, Z = this.date.getHours(), ab = this.date.getMinutes();
				n = this.date.getSeconds();
				if (Y) {
					aa = (Z >= 12);
					if (aa) {
						Z -= 12
					}
					if (Z == 0) {
						Z = 12
					}
					W.innerHTML = aa ? "pm" : "am"
				}
				V.innerHTML = (Z < 10) ? ("0" + Z) : Z;
				y.innerHTML = (ab < 10) ? ("0" + ab) : ab;
				m.innerHTML = (n < 10) ? ("0" + n) : n
			};
			F.onUpdateTime = function() {
				var aa = this.date;
				var ab = parseInt(V.innerHTML, 10);
				if (Y) {
					if (/pm/i.test(W.innerHTML) && ab < 12) {
						ab += 12
					} else {
						if (/am/i.test(W.innerHTML) && ab == 12) {
							ab = 0
						}
					}
				}
				var ac = aa.getDate();
				var Z = aa.getMonth();
				var ad = aa.getFullYear();
				aa.setHours(ab);
				aa.setMinutes(parseInt(y.innerHTML, 10));
				aa.setSeconds(parseInt(m.innerHTML, 10));
				aa.setFullYear(ad);
				aa.setMonth(Z);
				aa.setDate(ac);
				this.dateClicked = false;
				this.callHandler()
			}
		})()
	} else {
		this.onSetTime = this.onUpdateTime = function() {
		}
	}
	var I = Calendar.createElement("tfoot", o);
	k = Calendar.createElement("tr", I);
	k.className = "footrow";
	this._nav_clear = R(Calendar._TT.Clear, 2, 3);
	this._nav_now = R(Calendar._TT.TODAY, this.weekNumbers ? 4 : 3, 0);
	this._nav_ok = R(Calendar._TT.OK, 2, -3);
	this.tooltips = v;
	this.datemenu = Calendar.createElement("div");
	$(this.datemenu).css({
		"z-index" : "auto",
		position : "absolute",
		top : 0,
		left : 0,
		height : 190,
		width : 226,
		top : 19,
		display : "none"
	});
	var p = Calendar.createElement("table", this.datemenu);
	$(p).css({
		height : 190,
		width : 226
	});
	p.cellSpacing = 0;
	p.cellPadding = 0;
	p.calendar = this;
	var B = Calendar.createElement("tbody", p);
	B.className = "datemenubody";
	var w = Calendar.createElement("tr", B);
	for (var J = 0; J < 2; J++) {
		var E = Calendar.createElement("td", w);
		E.className = "x-date-menu-month"
	}
	for (var J = 0; J < 2; J++) {
		var E = Calendar.createElement("td", w);
		E.className = "x-date-menu-yearbtn"
	}
	for (var K = 0; K < 5; K++) {
		var w = Calendar.createElement("tr", B);
		for (var J = 0; J < 2; J++) {
			var E = Calendar.createElement("td", w);
			E.className = "x-date-menu-month"
		}
		for (var J = 0; J < 2; J++) {
			var E = Calendar.createElement("td", w);
			E.className = "x-date-menu-year"
		}
	}
	var w = Calendar.createElement("tr", B);
	w.className = "x-date-menu-btns";
	var E = Calendar.createElement("td", w);
	$(E).attr("colspan", "4");
	var c = Calendar.createElement("button", E);
	c.className = "x-date-menu-ok";
	c.innerHTML = "&nbsp;" + Calendar._TT.OK + "&nbsp;";
	c.onclick = function() {
		var n = new Date(F.date);
		var j = F.opts.endDate;
		var U = F.opts.startDate;
		var y = $(F.datemenu.selectedyear.innerHTML).text();
		var i = F.datemenu.selectedmonthIndex;
		if (!j && !U) {
			n.setFullYear(y);
			n.setMonth(i);
			n.setDate(1);
			F.setDate(n);
			$(F.datemenu).hide("normal");
			$(F.shadow).show();
			return false
		}
		n.setFullYear(y);
		n.setMonth(i);
		n.setDate(1);
		if (j) {
			var T = new Date(j)
		}
		if (U) {
			var m = new Date(U)
		}
		if (T && m) {
			if (n < m || n > T) {
				n = new Date(F.date)
			}
		} else {
			if (T) {
				if (n > T) {
					n = new Date(F.date)
				}
			} else {
				if (n < m) {
					n = new Date(F.date)
				}
			}
		}
		F.setDate(n);
		$(F.datemenu).hide("normal");
		$(F.shadow).show()
	};
	var g = Calendar.createElement("button", E);
	g.className = "x-date-menu-cancel";
	g.innerHTML = "&nbsp;" + Calendar._TT.Cancel + "&nbsp;";
	var d = this;
	g.onclick = function() {
		$(F.datemenu).hide("normal");
		$(F.shadow).show()
	};
	var C = this.date.getFullYear();
	var K = this.date.getMonth();
	var A = [ C - 4, C - 3, C - 2, C - 1, C, C + 1, C + 2, C + 3, C + 4, C + 5 ];
	this.datemenu.datemenuY = $("td.x-date-menu-year", $(this.datemenu));
	this.datemenu.datemenuM = $("td.x-date-menu-month", $(this.datemenu));
	var d = this;
	for (var M = 0; M < 12; M++) {
		this.datemenu.datemenuM[M].innerHTML = "<a href='#'>" + Calendar._MN[M]
				+ "</a>";
		if (M % 2 != 0) {
			$(this.datemenu.datemenuM[M]).addClass("x-date-menu-sep")
		}
		if (M == K) {
			$(this.datemenu.datemenuM[M]).addClass("x-date-menu-sel");
			this.datemenu.selectedmonth = this.datemenu.datemenuM[M];
			this.datemenu.selectedmonthIndex = M
		}
		this.datemenu.datemenuM[M].onclick = function(i) {
			return function() {
				if (d.datemenu.selectedmonth) {
					$(d.datemenu.selectedmonth).removeClass("x-date-menu-sel")
				}
				$(d.datemenu.datemenuM[i]).addClass("x-date-menu-sel");
				d.datemenu.selectedmonth = d.datemenu.datemenuM[i];
				d.datemenu.selectedmonthIndex = i
			}
		}(M);
		if (M < 10) {
			this.datemenu.datemenuY[M].innerHTML = "<a href='#'>" + A[M]
					+ "</a>";
			if ($(this.datemenu.datemenuY[M].innerHTML).text() == C) {
				$(this.datemenu.datemenuY[M]).addClass("x-date-menu-sel");
				this.datemenu.selectedyear = this.datemenu.datemenuY[M]
			}
			this.datemenu.datemenuY[M].onclick = function(i) {
				return function() {
					if (d.datemenu.selectedyear) {
						$(d.datemenu.selectedyear).removeClass(
								"x-date-menu-sel")
					}
					$(d.datemenu.datemenuY[i]).addClass("x-date-menu-sel");
					d.datemenu.selectedyear = d.datemenu.datemenuY[i]
				}
			}(M)
		}
	}
	$("td.x-date-menu-yearbtn", $(this.datemenu))[0].innerHTML = "<a class='x-date-menu-prev'></a>";
	$("td.x-date-menu-yearbtn", $(this.datemenu))[1].innerHTML = "<a class='x-date-menu-next'></a>";
	$("a.x-date-menu-prev", $("td.x-date-menu-yearbtn", $(this.datemenu)))
			.click(
					function() {
						for (var j = 0; j < 10; j++) {
							A[j] -= 10;
							d.datemenu.datemenuY[j].innerHTML = "<a href='#'>"
									+ A[j] + "</a>"
						}
					});
	$("a.x-date-menu-next", $("td.x-date-menu-yearbtn", $(this.datemenu)))
			.click(
					function() {
						for (var j = 0; j < 10; j++) {
							A[j] += 10;
							d.datemenu.datemenuY[j].innerHTML = "<a href='#'>"
									+ A[j] + "</a>"
						}
					});
	O.appendChild(this.datemenu);
	var z = Calendar.createElement("div");
	this.shadow = z;
	z.className = "x-shadow";
	$(z).css({
		"z-index" : "-1",
		position : "absolute",
		top : 3,
		left : -5,
		height : 183,
		width : /*(!$.browser.mozilla) ? 249 : */237
	});
	var l = $("<div class='xst'>").appendTo(z);
	var Q = $("<div class='xstl'>").appendTo(l);
	var a = $("<div class='xstc'>").css("width",
			(/*(!$.browser.mozilla) ? 227 : */225)).appendTo(l);
	var N = $("<div class='xstr'>").appendTo(l);
	var s = $("<div class='xsc'>").css("height", 171).appendTo(z);
	var r = $("<div class='xsml'>").appendTo(s);
	var x = $("<div class='xsmc'>").css("width",
			(/*(!$.browser.mozilla) ? 227 :*/ 225)).appendTo(s);
	var q = $("<div class='xsmr'>").appendTo(s);
	var u = $("<div class='xsb'>").appendTo(z);
	var H = $("<div class='xsbl'>").appendTo(u);
	var P = $("<div class='xsbc'>").css("width",
			(/*(!$.browser.mozilla) ? 227 : */225)).appendTo(u);
	var G = $("<div class='xsbr'>").appendTo(u);
	O.appendChild(this.shadow);
	O = Calendar.createElement("div", this.element);
	this.monthsCombo = O;
	O.className = "combo";
	for (M = 0; M < Calendar._MN.length; ++M) {
		var f = Calendar.createElement("div");
		f.className = Calendar.is_ie ? "label-IEfix" : "label";
		f.month = M;
		f.innerHTML = Calendar._SMN[M];
		O.appendChild(f)
	}
	O = Calendar.createElement("div", this.element);
	this.yearsCombo = O;
	O.className = "combo";
	for (M = 12; M > 0; --M) {
		var h = Calendar.createElement("div");
		h.className = Calendar.is_ie ? "label-IEfix" : "label";
		O.appendChild(h)
	}
	this._init(this.firstDayOfWeek, this.date);
	e.appendChild(this.element)
};
Calendar._keyEvent = function(k) {
	var a = window._dynarch_popupCalendar;
	if (!a || a.multiple) {
		return false
	}
	(Calendar.is_ie) && (k = window.event);
	var i = (Calendar.is_ie || k.type == "keypress"), l = k.keyCode;
	if (k.ctrlKey) {
		switch (l) {
		case 37:
			i && Calendar.cellClick(a._nav_pm);
			break;
		case 39:
			i && Calendar.cellClick(a._nav_nm);
			break;
		default:
			return false
		}
	} else {
		switch (l) {
		case 32:
			Calendar.cellClick(a._nav_now);
			break;
		case 27:
			i && a.callCloseHandler();
			break;
		case 37:
		case 38:
		case 39:
		case 40:
			if (i) {
				var e, m, j, g, c, d;
				e = l == 37 || l == 38;
				d = (l == 37 || l == 39) ? 1 : 7;
				function b() {
					c = a.currentDateEl;
					var n = c.pos;
					m = n & 15;
					j = n >> 4;
					g = a.ar_days[j][m]
				}
				b();
				function f() {
					var n = new Date(a.date);
					n.setDate(n.getDate() - d);
					a.setDate(n)
				}
				function h() {
					var n = new Date(a.date);
					n.setDate(n.getDate() + d);
					a.setDate(n)
				}
				while (1) {
					switch (l) {
					case 37:
						if (--m >= 0) {
							g = a.ar_days[j][m]
						} else {
							m = 6;
							l = 38;
							continue
						}
						break;
					case 38:
						if (--j >= 0) {
							g = a.ar_days[j][m]
						} else {
							f();
							b()
						}
						break;
					case 39:
						if (++m < 7) {
							g = a.ar_days[j][m]
						} else {
							m = 0;
							l = 40;
							continue
						}
						break;
					case 40:
						if (++j < a.ar_days.length) {
							g = a.ar_days[j][m]
						} else {
							h();
							b()
						}
						break
					}
					break
				}
				if (g) {
					if (!g.disabled) {
						Calendar.cellClick(g)
					} else {
						if (e) {
							f()
						} else {
							h()
						}
					}
				}
			}
			break;
		case 13:
			if (i) {
				Calendar.cellClick(a.currentDateEl, k)
			}
			break;
		default:
			return false
		}
	}
	return Calendar.stopEvent(k)
};
Calendar.prototype._init = function(o, z) {
	var B = 0;
	var y = new Date(), s = y.getFullYear(), C = y.getMonth(), c = y.getDate();
	this.table.style.visibility = "hidden";
	var k = z.getFullYear();
	if (k < this.minYear) {
		k = this.minYear;
		z.setFullYear(k)
	} else {
		if (k > this.maxYear) {
			k = this.maxYear;
			z.setFullYear(k)
		}
	}
	this.firstDayOfWeek = o;
	this.date = new Date(z);
	var A = z.getMonth();
	var F = z.getDate();
	var D = z.getMonthDays();
	z.setDate(1);
	var t = (z.getDay() - this.firstDayOfWeek) % 7;
	if (t < 0) {
		t += 7
	}
	z.setDate(0 - t);
	z.setDate(z.getDate() + 1);
	var f = this.tbody.firstChild;
	var m = Calendar._SMN[A];
	var q = this.ar_days = new Array();
	var p = Calendar._TT.WEEKEND;
	var e = this.multiple ? (this.datesCells = {}) : null;
	for (var v = 0; v < 6; ++v, f = f.nextSibling) {
		var a = f.firstChild;
		if (this.weekNumbers) {
			a.className = "day wn";
			a.innerHTML = z.getWeekNumber();
			a = a.nextSibling
		}
		f.className = "daysrow";
		var x = false, g, d = q[v] = [];
		for (var u = 0; u < 7; ++u, a = a.nextSibling, z.setDate(g + 1)) {
			g = z.getDate();
			var h = z.getDay();
			var E = this.opts.startDate ? new Date(this.opts.startDate) : null;
			var w = this.opts.endDate ? new Date(this.opts.endDate) : null;
			if ((E != null && E > z) || (w != null && w < z)) {
				Calendar.removeEvent(a, "mouseover", Calendar.dayMouseOver);
				Calendar.removeEvent(a, "mousedown", Calendar.dayMouseDown);
				Calendar.removeEvent(a, "mouseout", Calendar.dayMouseOut);
				if (Calendar.is_ie) {
					Calendar.removeEvent(a, "dblclick",
							Calendar.dayMouseDblClick);
					a.setAttribute("unselectable", false)
				}
				a.className = "day disabled"
			} else {
				Calendar.addEvent(a, "mouseover", Calendar.dayMouseOver);
				Calendar.addEvent(a, "mousedown", Calendar.dayMouseDown);
				Calendar.addEvent(a, "mouseout", Calendar.dayMouseOut);
				if (Calendar.is_ie) {
					Calendar.addEvent(a, "dblclick", Calendar.dayMouseDblClick);
					a.setAttribute("unselectable", true)
				}
				a.className = "day"
			}
			a.pos = v << 4 | u;
			d[u] = a;
			var n = (z.getMonth() == A);
			if (!n) {
				if (this.showsOtherMonths) {
					a.className += " othermonth";
					a.otherMonth = true
				} else {
					a.className = "emptycell";
					a.innerHTML = "&nbsp;";
					a.disabled = true;
					continue
				}
			} else {
				a.otherMonth = false;
				x = true
			}
			a.disabled = false;
			a.innerHTML = this.getDateText ? this.getDateText(z, g) : g;
			if (e) {
				e[z.print("%Y%m%d")] = a
			}
			if (this.getDateStatus) {
				var r = this.getDateStatus(z, k, A, g);
				if (this.getDateToolTip) {
					var l = this.getDateToolTip(z, k, A, g);
					if (l) {
						a.title = l
					}
				}
				if (r === true) {
					a.className += " disabled";
					a.disabled = true
				} else {
					if (/disabled/i.test(r)) {
						a.disabled = true
					}
					a.className += " " + r
				}
			}
			if (!a.disabled) {
				a.caldate = new Date(z);
				if (!this.multiple && n && g == F && this.hiliteToday) {
					a.className += " selected";
					this.currentDateEl = a
				}
				if (z.getFullYear() == s && z.getMonth() == C && g == c) {
					a.className += " today"
				}
				if (p.indexOf(h.toString()) != -1) {
					a.className += a.otherMonth ? " oweekend" : " weekend"
				}
			}
		}
		if (!(x || this.showsOtherMonths)) {
			f.className = "emptyrow";
			B++
		}
	}
	var b = (this.showsTime ? 11 : 10) - B;
	$(this.shadow).css("height", 19 * b);
	$(this.shadow).children(".xsc").height(19 * b - 17);
	B = 0;
	this.title.innerHTML = Calendar._MN[A] + ", " + k;
	this.onSetTime();
	this.table.style.visibility = "visible";
	this._initMultipleDates()
};
Calendar.prototype._initMultipleDates = function() {
	if (this.multiple) {
		for ( var b in this.multiple) {
			var a = this.datesCells[b];
			var c = this.multiple[b];
			if (!c) {
				continue
			}
			if (a) {
				a.className += " selected"
			}
		}
	}
};
Calendar.prototype._toggleMultipleDate = function(b) {
	if (this.multiple) {
		var c = b.print("%Y%m%d");
		var a = this.datesCells[c];
		if (a) {
			var e = this.multiple[c];
			if (!e) {
				Calendar.addClass(a, "selected");
				this.multiple[c] = b
			} else {
				Calendar.removeClass(a, "selected");
				delete this.multiple[c]
			}
		}
	}
};
Calendar.prototype.setDateToolTipHandler = function(a) {
	this.getDateToolTip = a
};
Calendar.prototype.setDate = function(a) {
	if (!a.equalsTo(this.date)) {
		this._init(this.firstDayOfWeek, a)
	}
};
Calendar.prototype.refresh = function() {
	this._init(this.firstDayOfWeek, this.date)
};
Calendar.prototype.setFirstDayOfWeek = function(a) {
	this._init(a, this.date);
	this._displayWeekdays()
};
Calendar.prototype.setDateStatusHandler = Calendar.prototype.setDisabledHandler = function(
		a) {
	this.getDateStatus = a
};
Calendar.prototype.setRange = function(b, c) {
	this.minYear = b;
	this.maxYear = c
};
Calendar.prototype.callHandler = function(a) {
	if (this.onSelected) {
		this.onSelected(this, this.date.print(this.dateFormat), a)
	}
};
Calendar.prototype.callCloseHandler = function() {
	if (this.onClose) {
		this.onClose(this)
	}
	this.hideShowCovered()
};
Calendar.prototype.destroy = function() {
	var a = this.element.parentNode;
	a.removeChild(this.element);
	Calendar._C = null;
	window._dynarch_popupCalendar = null
};
Calendar.prototype.reparent = function(b) {
	var a = this.element;
	a.parentNode.removeChild(a);
	b.appendChild(a)
};
Calendar._checkCalendar = function(b) {
	var c = window._dynarch_popupCalendar;
	if (!c) {
		return false
	}
	var a = Calendar.is_ie ? Calendar.getElement(b) : Calendar
			.getTargetElement(b);
	for (; a != null && a != c.element; a = a.parentNode) {
	}
	if (a == null) {
		window._dynarch_popupCalendar.callCloseHandler();
		return Calendar.stopEvent(b)
	}
};
Calendar.prototype.show = function() {
	var e = this.table.getElementsByTagName("tr");
	for (var d = e.length; d > 0;) {
		var f = e[--d];
		Calendar.removeClass(f, "rowhilite");
		var c = f.getElementsByTagName("td");
		for (var b = c.length; b > 0;) {
			var a = c[--b];
			Calendar.removeClass(a, "hilite");
			Calendar.removeClass(a, "active")
		}
	}
	this.element.style.display = "block";
	this.hidden = false;
	if (this.isPopup) {
		window._dynarch_popupCalendar = this;
		Calendar.addEvent(document, "keydown", Calendar._keyEvent);
		Calendar.addEvent(document, "keypress", Calendar._keyEvent);
		Calendar.addEvent(document, "mousedown", Calendar._checkCalendar)
	}
	this.hideShowCovered()
};
Calendar.prototype.hide = function() {
	if (this.isPopup) {
		Calendar.removeEvent(document, "keydown", Calendar._keyEvent);
		Calendar.removeEvent(document, "keypress", Calendar._keyEvent);
		Calendar.removeEvent(document, "mousedown", Calendar._checkCalendar)
	}
	this.element.style.display = "none";
	this.hidden = true;
	this.hideShowCovered()
};
Calendar.prototype.showAt = function(a, c) {
	var b = this.element.style;
	b.left = a + "px";
	b.top = c + "px";
	this.show()
};
Calendar.prototype.showAtElement = function(c, d) {
	var a = this;
	var e = Calendar.getAbsolutePos(c);
	if (!d || typeof d != "string") {
		this.showAt(e.x, e.y + c.offsetHeight);
		return true
	}
	function b(h) {
		if (h.x < 0) {
			h.x = 0
		}
		if (h.y < 0) {
			h.y = 0
		}
		var g = {
			x : window.innerWidth,
			y : window.innerHeight
		};
		if (Calendar.is_ie) {
			g.x = document.body.clientWidth;
			g.y = document.body.clientHeight;
			g.y += document.body.scrollTop;
			g.x += document.body.scrollLeft
		} else {
			g.y += window.scrollY;
			g.x += window.scrollX
		}
		var f = h.x + h.width - g.x;
		if (f > 0) {
			h.x -= f
		}
		f = h.y + h.height - g.y;
		if (f > 0) {
			h.y -= f
		}
	}
	this.element.style.display = "block";
	Calendar.continuation_for_the_fucking_khtml_browser = function() {
		var f = a.element.offsetWidth;
		var i = a.element.offsetHeight;
		a.element.style.display = "none";
		var g = d.substr(0, 1);
		var j = "l";
		if (d.length > 1) {
			j = d.substr(1, 1)
		}
		switch (g) {
		case "T":
			e.y -= i;
			break;
		case "B":
			e.y += c.offsetHeight;
			break;
		case "C":
			e.y += (c.offsetHeight - i) / 2;
			break;
		case "t":
			e.y += c.offsetHeight - i;
			break;
		case "b":
			break
		}
		switch (j) {
		case "L":
			e.x -= f;
			break;
		case "R":
			e.x += c.offsetWidth;
			break;
		case "C":
			e.x += (c.offsetWidth - f) / 2;
			break;
		case "l":
			e.x += c.offsetWidth - f;
			break;
		case "r":
			break
		}
		e.width = f;
		e.height = i + 40;
		a.monthsCombo.style.display = "none";
		b(e);
		a.showAt(e.x, e.y)
	};
	if (Calendar.is_khtml) {
		setTimeout("Calendar.continuation_for_the_fucking_khtml_browser()", 10)
	} else {
		Calendar.continuation_for_the_fucking_khtml_browser()
	}
};
Calendar.prototype.setDateFormat = function(a) {
	this.dateFormat = a
};
Calendar.prototype.setTtDateFormat = function(a) {
	this.ttDateFormat = a
};
Calendar.prototype.parseDateTime = function(b, a) {
	if (!a) {
		a = this.dateFormat
	}
	this.setDate(Date.parseDateTime(b, a))
};
Calendar.prototype.hideShowCovered = function() {
	if (!Calendar.is_ie && !Calendar.is_opera) {
		return
	}
	function b(k) {
		var i = k.style.visibility;
		if (!i) {
			if (document.defaultView
					&& typeof (document.defaultView.getComputedStyle) == "function") {
				if (!Calendar.is_khtml) {
					i = document.defaultView.getComputedStyle(k, "")
							.getPropertyValue("visibility")
				} else {
					i = ""
				}
			} else {
				if (k.currentStyle) {
					i = k.currentStyle.visibility
				} else {
					i = ""
				}
			}
		}
		return i
	}
	var s = new Array("applet", "iframe", "select");
	var c = this.element;
	var a = Calendar.getAbsolutePos(c);
	var f = a.x;
	var d = c.offsetWidth + f;
	var r = a.y;
	var q = c.offsetHeight + r;
	for (var h = s.length; h > 0;) {
		var g = document.getElementsByTagName(s[--h]);
		var e = null;
		for (var l = g.length; l > 0;) {
			e = g[--l];
			a = Calendar.getAbsolutePos(e);
			var o = a.x;
			var n = e.offsetWidth + o;
			var m = a.y;
			var j = e.offsetHeight + m;
			if (this.hidden || (o > d) || (n < f) || (m > q) || (j < r)) {
				if (!e.__msh_save_visibility) {
					e.__msh_save_visibility = b(e)
				}
				e.style.visibility = e.__msh_save_visibility
			} else {
				if (!e.__msh_save_visibility) {
					e.__msh_save_visibility = b(e)
				}
				e.style.visibility = "hidden"
			}
		}
	}
};
Calendar.prototype._displayWeekdays = function() {
	var b = this.firstDayOfWeek;
	var a = this.firstdayname;
	var d = Calendar._TT.WEEKEND;
	for (var c = 0; c < 7; ++c) {
		a.className = "day name";
		var e = (c + b) % 7;
		if (c) {
			a.navtype = 100;
			a.calendar = this;
			a.fdow = e;
			Calendar._add_evs(a)
		}
		if (d.indexOf(e.toString()) != -1) {
			Calendar.addClass(a, "weekend")
		}
		a.innerHTML = Calendar._SDN[(c + b) % 7];
		a = a.nextSibling
	}
};
Calendar.prototype._hideCombos = function() {
	this.monthsCombo.style.display = "none";
	this.yearsCombo.style.display = "none"
};
Calendar.prototype._dragStart = function(ev) {
	if (this.dragging) {
		return
	}
	this.dragging = true;
	var posX;
	var posY;
	if (Calendar.is_ie) {
		posY = window.event.clientY + document.body.scrollTop;
		posX = window.event.clientX + document.body.scrollLeft
	} else {
		posY = ev.clientY + window.scrollY;
		posX = ev.clientX + window.scrollX
	}
	var st = this.element.style;
	this.xOffs = posX - parseInt(st.left);
	this.yOffs = posY - parseInt(st.top);
	with (Calendar) {
		addEvent(document, "mousemove", calDragIt);
		addEvent(document, "mouseup", calDragEnd)
	}
};
Date._MD = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
Date.SECOND = 1000;
Date.MINUTE = 60 * Date.SECOND;
Date.HOUR = 60 * Date.MINUTE;
Date.DAY = 24 * Date.HOUR;
Date.WEEK = 7 * Date.DAY;
Date.parseDateTime = function(n, c) {
	var o = new Date();
	var p = 0;
	var e = -1;
	var l = 0;
	var r = n.split(/\W+/);
	var q = c.match(/%./g);
	var h = 0, g = 0;
	var s = 0;
	var f = 0;
	var k = 0;
	for (h = 0; h < r.length; ++h) {
		if (!r[h]) {
			continue
		}
		switch (q[h]) {
		case "%d":
		case "%e":
			l = parseInt(r[h], 10);
			break;
		case "%X":
			e = parseInt(r[h], 10) - 1;
			break;
		case "%x":
			e = parseInt(r[h], 10) - 1;
			break;
		case "%m":
			e = parseInt(r[h], 10) - 1;
			break;	
		case "%Y":
		case "%y":
			p = parseInt(r[h], 10);
			(p < 100) && (p += (p > 29) ? 1900 : 2000);
			break;
		case "%b":
		case "%B":
			for (g = 0; g < 12; ++g) {
				if (Calendar._MN[g].substr(0, r[h].length).toLowerCase() == r[h]
						.toLowerCase()) {
					e = g;
					break
				}
			}
			break;
		case "%H":
		case "%I":
		case "%k":
		case "%l":
			s = parseInt(r[h], 10);
			break;
		case "%P":
		case "%p":
			if (/pm/i.test(r[h]) && s < 12) {
				s += 12
			} else {
				if (/am/i.test(r[h]) && s >= 12) {
					s -= 12
				}
			}
			break;
		case "%M":
			f = parseInt(r[h], 10);
			break;
		case "%S":
			k = parseInt(r[h], 10);
			break
		}
	}
	if (isNaN(p)) {
		p = o.getFullYear()
	}
	if (isNaN(e)) {
		e = o.getMonth()
	}
	if (isNaN(l)) {
		l = o.getDate()
	}
	if (isNaN(s)) {
		s = o.getHours()
	}
	if (isNaN(f)) {
		f = o.getMinutes()
	}
	if (isNaN(k)) {
		k = o.getSeconds()
	}
	if (p != 0 && e != -1 && l != 0) {
		return new Date(p, e, l, s, f, k)
	}
	if (p != 0 && e != -1 && l == 0) {
		return new Date(p, e, 1, s, f, k)
	}
	p = 0;
	e = -1;
	l = 0;
	for (h = 0; h < r.length; ++h) {
		if (r[h].search(/[a-zA-Z]+/) != -1) {
			var u = -1;
			for (g = 0; g < 12; ++g) {
				if (Calendar._MN[g].substr(0, r[h].length).toLowerCase() == r[h]
						.toLowerCase()) {
					u = g;
					break
				}
			}
			if (u != -1) {
				if (e != -1) {
					l = e + 1
				}
				e = u
			}
		} else {
			if (parseInt(r[h], 10) <= 12 && e == -1) {
				e = r[h] - 1
			} else {
				if (parseInt(r[h], 10) > 31 && p == 0) {
					p = parseInt(r[h], 10);
					(p < 100) && (p += (p > 29) ? 1900 : 2000)
				} else {
					if (l == 0) {
						l = r[h]
					}
				}
			}
		}
	}
	if (p == 0) {
		p = o.getFullYear()
	}
	if (e != -1 && l != 0) {
		return new Date(p, e, l, s, f, k)
	}
	return o
};
Date.prototype.getMonthDays = function(b) {
	var a = this.getFullYear();
	if (typeof b == "undefined") {
		b = this.getMonth()
	}
	if (((0 == (a % 4)) && ((0 != (a % 100)) || (0 == (a % 400)))) && b == 1) {
		return 29
	} else {
		return Date._MD[b]
	}
};
Date.prototype.getDayOfYear = function() {
	var a = new Date(this.getFullYear(), this.getMonth(), this.getDate(), 0, 0,
			0);
	var c = new Date(this.getFullYear(), 0, 0, 0, 0, 0);
	var b = a - c;
	return Math.floor(b / Date.DAY)
};
Date.prototype.getWeekNumber = function() {
	var c = new Date(this.getFullYear(), this.getMonth(), this.getDate(), 0, 0,
			0);
	var b = c.getDay();
	c.setDate(c.getDate() - (b + 6) % 7 + 3);
	var a = c.valueOf();
	c.setMonth(0);
	c.setDate(4);
	return Math.round((a - c.valueOf()) / (7 * 86400000)) + 1
};
Date.prototype.equalsTo = function(a) {
	return ((this.getFullYear() == a.getFullYear())
			&& (this.getMonth() == a.getMonth())
			&& (this.getDate() == a.getDate())
			&& (this.getHours() == a.getHours())
			&& (this.getMinutes() == a.getMinutes()) && (this.getSeconds() == a
			.getSeconds()))
};
Date.prototype.setDateOnly = function(a) {
	var b = new Date(a);
	this.setDate(1);
	this.setFullYear(b.getFullYear());
	this.setMonth(b.getMonth());
	this.setDate(b.getDate())
};
Date.prototype.print = function(l) {
	var b = this.getMonth();
	var k = this.getDate();
	var n = this.getFullYear();
	var p = this.getWeekNumber();
	var q = this.getDay();
	var v = {};
	var r = this.getHours();
	var c = (r >= 12);
	var h = (c) ? (r - 12) : r;
	var u = this.getDayOfYear();
	if (h == 0) {
		h = 12
	}
	var e = this.getMinutes();
	var j = this.getSeconds();
	v["%a"] = Calendar._SDN[q];
	v["%A"] = Calendar._DN[q];
	v["%b"] = Calendar._SMN[b];
	v["%B"] = Calendar._MN[b];
	v["%C"] = 1 + Math.floor(n / 100);
	v["%d"] = (k < 10) ? ("0" + k) : k;
	v["%e"] = k;
	v["%H"] = (r < 10) ? ("0" + r) : r;
	v["%I"] = (h < 10) ? ("0" + h) : h;
	v["%j"] = (u < 100) ? ((u < 10) ? ("00" + u) : ("0" + u)) : u;
	v["%k"] = r;
	v["%l"] = h;
	v["%X"] = (b < 9) ? ("0" + (1 + b)) : (1 + b);
	v["%x"] = b + 1;
	v["%m"] = (b < 9) ? ("0" + (1+b)) : (1+b); // month, range 01 to 12
	v["%M"] = (e < 10) ? ("0" + e) : e;
	v["%n"] = "\n";
	v["%p"] = c ? "PM" : "AM";
	v["%P"] = c ? "pm" : "am";
	v["%s"] = Math.floor(this.getTime() / 1000);
	v["%S"] = (j < 10) ? ("0" + j) : j;
	v["%t"] = "\t";
	v["%U"] = v["%W"] = v["%V"] = (p < 10) ? ("0" + p) : p;
	v["%u"] = q + 1;
	v["%w"] = q;
	v["%y"] = ("" + n).substr(2, 2);
	v["%Y"] = n;
	v["%%"] = "%";
	var t = /%./g;
	if (!Calendar.is_ie5 && !Calendar.is_khtml) {
		return l.replace(t, function(a) {
			return v[a] || a
		})
	}
	var o = l.match(t);
	for (var g = 0; g < o.length; g++) {
		var f = v[o[g]];
		if (f) {
			t = new RegExp(o[g], "g");
			l = l.replace(t, f)
		}
	}
	return l
};
window._dynarch_popupCalendar = null;
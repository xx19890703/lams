 // ** I18N

// Calendar ZH language
// Author: muziq, <muziq@sina.com>
// Encoding: GB2312 or GBK
// Distributed under the same terms as the calendar itself.

// full day names
Calendar._DN = new Array
("周日",
 "周一",
 "周二",
 "周三",
 "周四",
 "周五",
 "周六",
 "周日");

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("日",
 "一",
 "二",
 "三",
 "四",
 "五",
 "六",
 "日");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 0;

// full month names
Calendar._MN = new Array
("一月",
 "二月",
 "三月",
 "四月",
 "五月",
 "六月",
 "七月",
 "八月",
 "九月",
 "十月",
 "十一月",
 "十二月");

// short month names
Calendar._SMN = new Array
("一月",
 "二月",
 "三月",
 "四月",
 "五月",
 "六月",
 "七月",
 "八月",
 "九月",
 "十月",
 "十一月",
 "十二月");
Calendar._TT = {};
Calendar._TT.INFO = "关于";
Calendar._TT.ABOUT = "DHTML Date/Time Selector\n(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n\n\nDate selection:\n- Use the \xab, \xbb buttons to select year\n- Use the "
		+ String.fromCharCode(8249)
		+ ", "
		+ String.fromCharCode(8250)
		+ " buttons to select month\n- Hold mouse button on any of the above buttons for faster selection.";
Calendar._TT.ABOUT_TIME = "\n\nTime selection:\n- Click on any of the time parts to increase it\n- or Shift-click to decrease it\n- or click and drag for faster selection.";
Calendar._TT.PREV_YEAR = "上一年";
Calendar._TT.PREV_MONTH = "上一月";
Calendar._TT.GO_TODAY = "至今天";
Calendar._TT.CALENDAR = "日历";
Calendar._TT.NEXT_MONTH = "下一月";
Calendar._TT.NEXT_YEAR = "下一年";
Calendar._TT.SEL_DATE = "选择日期";
Calendar._TT.DRAG_TO_MOVE = "拖动";
Calendar._TT.PART_TODAY = "(今日)";
Calendar._TT.OK = "确定";
Calendar._TT.Clear = "清除";
Calendar._TT.Cancel = "取消";
Calendar._TT.DAY_FIRST = "%s为周起始" ;
Calendar._TT.WEEKEND = "0,6";
Calendar._TT.CLOSE = "关闭";
Calendar._TT.TODAY = "今天";
Calendar._TT.TIME_PART = "(Shift-)点击鼠标改变值";
Calendar._TT.DEF_DATE_FORMAT = "%Y-%m-%d";
Calendar._TT.TT_DATE_FORMAT = "%a, %b %e";
Calendar._TT.WK = "星期";
Calendar._TT.TIME = "时间:";




/*
// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "帮助";

Calendar._TT["ABOUT"] =
"选择日期:\n" +
"- 点击 \xab, \xbb 按钮选择年份\n" +
"- 点击 " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " 按钮选择月份\n";
Calendar._TT["ABOUT_TIME"] = "\n\n";

Calendar._TT["PREV_YEAR"] = "上一年 (按住出菜单)";
Calendar._TT["PREV_MONTH"] = "上一月 (按住出菜单)";
Calendar._TT["GO_TODAY"] = "转到今日";
Calendar._TT["NEXT_MONTH"] = "下一月 (按住出菜单)";
Calendar._TT["NEXT_YEAR"] = "下一年 (按住出菜单)";
Calendar._TT["SEL_DATE"] = "选择日期";
Calendar._TT["DRAG_TO_MOVE"] = "拖动";
Calendar._TT["PART_TODAY"] = " (今日)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "最左边显示%s";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "关闭";
Calendar._TT["TODAY"] = "今日";
Calendar._TT["TIME_PART"] = "点击鼠标或拖动改变值";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%A, %b %e日";

Calendar._TT["WK"] = "周";
Calendar._TT["TIME"] = "时间:";
*/
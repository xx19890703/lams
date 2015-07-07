<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
  <head>
    <title><sitemesh:write property='title'></sitemesh:write></title>  
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>  
    <%@ include file="/common/meta.jsp"%>     
    <sitemesh:write property='head'/>
  </head>
  <body>
     <sitemesh:write property='body'></sitemesh:write>
  </body>
</html>
package com.suun.publics.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 用于rest URL和重写,以便构造出没有扩展名的restURL
 * 
 * <pre>
 * prefix的默认值为: /static
 * excludeExtentions的默认值为: do,jsp,jspx
 * 
 * 以下为web.xml为完整的配置使用,可删除默认值配置
 * <code>
 *	&lt;!-- default是tomcat,jetty等容器提供的servlet, 将静态资源重定向至 /static/, 原来访问css用"/styles/test.css"现需要"/static/styles/test.css" -->
 *	&lt;servlet-mapping>
 *		&lt;servlet-name>default&lt;/servlet-name>
 *		&lt;url-pattern>/static/*&lt;/url-pattern>
 *	&lt;/servlet-mapping>
 *	
 *  &lt;!-- URL重写,访问静态资源将为其增加前缀,如 /demo.css 重写至 ${prefix}/demo.css,现prefix为/static -->
 * 	&lt;filter>
 *		&lt;filter-name>RestUrlRewriteFilter&lt;/filter-name>
 *		&lt;filter-class>cn.org.rapid_framework.web.filter.RestUrlRewriteFilter&lt;/filter-class>
 *		&lt;init-param>
 *			&lt;param-name>prefix&lt;/param-name>
 *			&lt;param-value>/static&lt;/param-value>
 *		&lt;/init-param>
 *		&lt;init-param>
 *			&lt;param-name>excludeExtentions&lt;/param-name>
 *			&lt;param-value>jsp,jspx,do&lt;/param-value>
 *		&lt;/init-param>
 *		&lt;init-param>
 *			&lt;param-name>excludePrefixes&lt;/param-name>
 *			&lt;param-value>/userinfo,/security&lt;/param-value>
 *		&lt;/init-param>
 *		&lt;init-param>
 *			&lt;param-name>debug&lt;/param-name>
 *			&lt;param-value>true&lt;/param-value>
 *		&lt;/init-param>				
 *	&lt;/filter>
 *	&lt;filter-mapping>
 *		&lt;filter-name>RestUrlRewriteFilter&lt;/filter-name>
 *		&lt;url-pattern>/*&lt;/url-pattern>
 *	&lt;/filter-mapping>
 * </code>
 * </pre>
 * @author badqiu
 *
 */
public class RestUrlRewriteFilter extends OncePerRequestFilter implements Filter{
	private static final String DEFAULT_EXECUDE_EXTENTIONS = "do";
	private static final String DEFAULT_EXECUDE_PREFIXS = "/resources";
	private static final String DEFAULT_PREFIX = "/WEB-INF/views";
	
	private String prefix;
	private Set<String> excludeExtentions = new HashSet<String>();
	private String[] excludePrefixes = new String[0];
	
	protected void initFilterBean() throws ServletException {
		try {
			initParameter(getFilterConfig());
		} catch (IOException e) {
			throw new ServletException("init paramerter error",e);
		}
	}

	private void initParameter(FilterConfig filterConfig) throws IOException {
		prefix = getStringParameter(filterConfig,"prefix",DEFAULT_PREFIX);
		String excludeExtentionsString = getStringParameter(filterConfig,"excludeExtentions",DEFAULT_EXECUDE_EXTENTIONS);
		excludeExtentions = new HashSet<String>((Arrays.asList(excludeExtentionsString.split(","))));
		
		String excludePrefixsString = getStringParameter(filterConfig,"excludePrefixes",DEFAULT_EXECUDE_PREFIXS);
		if(StringUtils.hasText(excludePrefixsString)) {
			excludePrefixes = excludePrefixsString.split(",");
		}
		
		System.out.println();
		System.out.println("RestUrlRewriteFilter.prefix="+prefix+" will rewrite url from /demo.html => ${prefix}/demo.html by forward");
		System.out.println("RestUrlRewriteFilter.excludeExtentions=["+excludeExtentionsString+"] will not rewrite url");
		System.out.println("RestUrlRewriteFilter.excludePrefixes=["+excludePrefixsString+"] will not rewrite url");
	
		System.out.println();
	}

	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		
		String from = request.getRequestURI().substring(request.getContextPath().length());
		if(rewriteURL(from)) {
			final String to = prefix+from;
			request.getRequestDispatcher(to).forward(request, response);
		}else {
			filterChain.doFilter(request, response);
		}
	}
	
	private boolean rewriteURL(String from) {
		String extension = StringUtils.getFilenameExtension(from);
		if(extension == null || "".equals(extension)) {
			return false;
		}
		
		for(String excludePrefix : excludePrefixes) {
			if(from.startsWith(excludePrefix)) {
				return false;
			}
		}
		if(excludeExtentions.contains(extension)) {
			return false;
		}
		return true;
	}

	private String getStringParameter(FilterConfig filterConfig,String name,String defaultValue) {
		String value = filterConfig.getInitParameter(name);
		if(value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return value;
	}
	
	@SuppressWarnings("unused")
	private boolean getBooleanParameter(FilterConfig filterConfig,String name,boolean defaultValue) {
		String value = getStringParameter(filterConfig, name, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}
	
}

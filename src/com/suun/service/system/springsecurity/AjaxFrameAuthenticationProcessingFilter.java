package com.suun.service.system.springsecurity;

import java.io.IOException;
import java.lang.Override;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.suun.publics.utils.Utils;

public class AjaxFrameAuthenticationProcessingFilter extends LoginUrlAuthenticationEntryPoint{////implements AuthenticationEntryPoint  {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private String mainframeUrl;
	private String sessioninvalidUrl;
	
	public String getSessioninvalidUrl() {
		return sessioninvalidUrl;
	}
	public void setSessioninvalidUrl(String sessioninvalidUrl) {
		this.sessioninvalidUrl = sessioninvalidUrl;
	}
	@Deprecated
	public AjaxFrameAuthenticationProcessingFilter() {
		super();
	}
	public AjaxFrameAuthenticationProcessingFilter(String loginFormUrl,String mainframeUrl,String sessioninvalidUrl) {
		super(loginFormUrl);
		this.mainframeUrl = mainframeUrl;
		this.sessioninvalidUrl = sessioninvalidUrl;
    }
	
	@Deprecated
    public void setMainframeUrl(String mainframeUrl) {
        this.mainframeUrl = mainframeUrl;
    }

    public String getMainframeUrl() {
        return mainframeUrl;
    }
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if (request.getRequestURI().equals(request.getContextPath()+sessioninvalidUrl)){
			request.getSession().setAttribute("QueryString", request.getQueryString());
		}
		if ((isAjaxRequest(request))){
			if (request.getSession().getAttribute("commencing")==null){
			   log.debug("Authentication AjaxRequest");			
		       //response.getWriter().write("SuunAjaxAuthentication");
			   response.setHeader("sessionstatus", "__timeout");
		       //response.setStatus(401);
		       //response.setStatus(301);
		    }
		} else if (!request.getRequestURI().equals(request.getContextPath()+mainframeUrl)){
			log.debug("Authentication frameDetailUrl:"+request.getRequestURI());
			
			//RequestDispatcher dispatcher = request.getRequestDispatcher("/");
	        //dispatcher.forward(request, response);	
	        response.getWriter().write("<script type=\"text/javascript\">top.location=\""+request.getContextPath()+"/main.do\"</script>");
		} else{	
			log.debug("Authentication super.commence");
			//super.commence(request, response, authException);	
			String redirectUrl = null;

	        if (isUseForward()) {

	            if (isForceHttps() && "http".equals(request.getScheme())) {
	                // First redirect the current request to HTTPS.
	                // When that request is received, the forward to the login page will be used.
	                redirectUrl = buildHttpsRedirectUrlForRequest(request);
	            }

	            if (redirectUrl == null) {
	                String loginForm = determineUrlToUseForThisRequest(request, response, authException);
	                if (request.getSession().getAttribute("QueryString")!=null){
	                    String QueryString=(String) request.getSession().getAttribute("QueryString");
	                    loginForm =loginForm +"?" +QueryString;
	                    request.getSession().setAttribute("QueryString", null);
	                }
	                if (log.isDebugEnabled()) {
	                    log.debug("Server side forward to: " + loginForm);
	                }

	                RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);
	                request.getSession().setAttribute("commencing", null);
	                dispatcher.forward(request, response);
	                return;
	            }
	        } else {
	            // redirect to login page. Use https if forceHttps true
	            redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);
	            if (request.getSession().getAttribute("QueryString")!=null){
                    String QueryString=(String) request.getSession().getAttribute("QueryString");
                    redirectUrl =redirectUrl +"?" +QueryString;
                    request.getSession().setAttribute("QueryString", null);
                }
	        }
	        redirectStrategy.sendRedirect(request, response, redirectUrl);
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		boolean result = false;
		request.getHeaderNames();
		String requestType = request.getHeader("X-Requested-With");//.getParameter("X-Requested-With");
		if (!Utils.isNullOrTemp(requestType)) {//AJAX_REQUEST
			if (requestType.equals("XMLHttpRequest")) {
				result = true;
			}
		}
		return result;
	}

}

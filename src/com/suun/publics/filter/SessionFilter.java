package com.suun.publics.filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class SessionFilter implements Filter {

	private static final Logger logger =LoggerFactory.getLogger(SessionFilter.class);

	private List<String> exclues=new ArrayList<String>();
	private PathMatcher urlMatcher = new AntPathMatcher();
	private int defaultTimeout = -1;

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		String configFile = cfg.getInitParameter("configFile");
		if (configFile == null || configFile.length() == 0) {
			exclues=null;
	    } else{
	    	Element e=loadConfigXml(cfg,configFile);
	    	NodeList nl=e.getChildNodes();
	    	for(int i=0;i<nl.getLength();i++)
	        {
	            Node nd=nl.item(i);
	            if(nd.getNodeName().equals("mapping"))
	            {
	                if(nd.hasAttributes()){
	                	NamedNodeMap attributes=nd.getAttributes();
	                	Boolean exclue=false;
	                	String path=null;
	                	for(int j=0;j<attributes.getLength();j++){
	                		Node node=attributes.item(j);
	                		if (node.getNodeName().equalsIgnoreCase("exclue")){
	                			exclue=node.getNodeValue().equalsIgnoreCase("true");
	                		}
	                		if (node.getNodeName().equalsIgnoreCase("path")){
	                			if (!StringUtils.isEmpty(node.getNodeValue())){
	                				path=node.getNodeValue();
	                			}
	                		}
                		}
	                	if (exclue&&(path!=null)){
	                		exclues.add(path);
	                	}
	                }
	            }
	        }
	    }
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
	        String servletPath = req.getServletPath();
	        HttpSession session=req.getSession();
	        if (defaultTimeout==-1)//获取配置的超时时间
	        	defaultTimeout=session.getMaxInactiveInterval();
	        Boolean ismatcher=false;
	        for(String path:exclues){
	        	if (urlMatcher.match(path, servletPath)) {				
	        		Date d = new Date();
	        		int longtime =(int) ((d.getTime()-session.getLastAccessedTime())/1000);
	        		int remainder=session.getMaxInactiveInterval()-longtime;
	        		if (remainder>0)
	        		    session.setMaxInactiveInterval(remainder);
	        		else
	        			session.setMaxInactiveInterval(0); 
	        		ismatcher=true;
	        		break;
	        	}
			}
	        if (!ismatcher)
	            session.setMaxInactiveInterval(defaultTimeout);
		} 
        chain.doFilter(request, response);

	}
	protected Element loadConfigXml(FilterConfig filterConfig, String configFilePath) throws ServletException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            File xmlConfigFile = new File(configFilePath);

            ServletContext servletContext = filterConfig.getServletContext();

            if (servletContext.getRealPath(configFilePath) != null) {
                xmlConfigFile = new File(servletContext.getRealPath(configFilePath));
            }

            if (xmlConfigFile.canRead()) {
                try {
                    logger.debug("Loading Session config file: " + xmlConfigFile.getAbsolutePath());
                    Document document = documentBuilder.parse(xmlConfigFile);
                    return document.getDocumentElement();
                } catch (SAXException e) {
                    throw new ServletException("Could not parse " + xmlConfigFile.getAbsolutePath(), e);
                }
            } else {
                InputStream stream = servletContext.getResourceAsStream(configFilePath);
                if (stream == null) {
                    logger.debug("No config file present - using defaults and init-params. Tried: "
                            + xmlConfigFile.getAbsolutePath() + " and ServletContext:" + configFilePath);
                    return null;
                }
                try {
                    logger.debug("Loading Session config file from ServletContext " + configFilePath);
                    Document document = documentBuilder.parse(stream);
                    return document.getDocumentElement();
                } catch (SAXException e) {
                    throw new ServletException("Could not parse " + configFilePath + " (loaded by ServletContext)", e);
                } finally {
                    stream.close();
                }
            }

        } catch (IOException e) {
            throw new ServletException(e);
        } catch (ParserConfigurationException e) {
            throw new ServletException("Could not initialize DOM parser", e);
        }
    }

}

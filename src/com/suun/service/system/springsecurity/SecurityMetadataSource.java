package com.suun.service.system.springsecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


public class SecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private PathMatcher urlMatcher = new AntPathMatcher();
	
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private com.suun.service.system.developer.SecurityManager securityManager;
	
	@Autowired   
    public SecurityMetadataSource(com.suun.service.system.developer.SecurityManager securityManager) {    	
        this.securityManager=securityManager;
		loadResourceDefine();
    }


    private void loadResourceDefine() {    	
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        
		List<com.suun.service.system.springsecurity.Resource> resources=securityManager.getAllResources();
		for (com.suun.service.system.springsecurity.Resource resource : resources) {
			ConfigAttribute ca = new SecurityConfig(resource.getAuth());
			Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
			atts.add(ca);
	        resourceMap.put(resource.getPath(),atts);
		}
		/*ConfigAttribute ca = new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY");
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        resourceMap.put("/login.do*",atts);
        ca = new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY");
		atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        resourceMap.put("/resources/**",atts);
        ca = new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY");
		atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        resourceMap.put("/common/**",atts);
        ca = new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY");
		atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        resourceMap.put("/security/security!imageCode*",atts);
        ca = new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY");
		atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        resourceMap.put("/security/security!checkCode*",atts);*/
    }

    // According to a URL, Find out permission configuration of this URL.
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        // guess object is a URL.
        String url = ((FilterInvocation)object).getRequestUrl();
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            if (urlMatcher.match(resURL, url)) {
                return resourceMap.get(resURL);
            }
        }
        //所有未配置的资源一律需要登录后查看权
        ConfigAttribute ca = new SecurityConfig("IS_AUTHENTICATED_FULLY");
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		atts.add(ca);
        return atts;
       // return null;
    }
    
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

}

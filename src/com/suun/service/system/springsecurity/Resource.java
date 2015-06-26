package com.suun.service.system.springsecurity;

/**
 * URL资源
 */
public class Resource{

	private String auth;

	private String path;
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
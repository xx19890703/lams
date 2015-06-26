package com.suun.service.system.springsecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.suun.model.system.developer.Function;
import com.suun.model.system.security.Role;

public class UserDetailServiceImpl implements UserDetailsService {	
    
	private com.suun.service.system.security.UserManager userManager;
	
	@Autowired
    public UserDetailServiceImpl(com.suun.service.system.security.UserManager userManager){
    	this.userManager=userManager;
    }
    
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		com.suun.model.system.security.User user = userManager.getUserByUserId(username);
		if (user == null){
			throw new UsernameNotFoundException(username + " 用户不存在！");			
		} 
		
		Collection<GrantedAuthority> authsList = new ArrayList<GrantedAuthority>();
		if (user.getUserId().equalsIgnoreCase("admin")){
			authsList.clear();
			List<String> auths = userManager.getAllAuths();
			for (String a : auths) {
			    authsList.add(new SimpleGrantedAuthority(a));
			}	
		} else{
			for (Role role : user.getRoles()) {	
				if (role.getState()==1){
					for (Function auth : role.getAuths()) {
						authsList.add(new SimpleGrantedAuthority(auth.getFunId()));
					}
		    	}
			}
		}
		
		
		User userdetail = new User(user.getUserId()/*.concat("[").concat(user.getUserName()==null?"":user.getUserName()).concat("]")*/, 
				user.getPassword()==null?"":user.getPassword(),
				user.getState()==1, true, true, true, authsList);
	
		return userdetail;
	}
	
}

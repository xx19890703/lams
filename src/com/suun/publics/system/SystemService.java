package com.suun.publics.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.suun.model.system.developer.Function;
import com.suun.model.system.developer.Menu;
import com.suun.model.system.orgnization.Department;
import com.suun.model.system.security.User;
import com.suun.publics.utils.TreeObj;
import com.suun.publics.utils.Utils;

public class SystemService{
 
	private static List<Department> orgs=null;
	
	private static List<User> users=null;
	
	private static List<Menu> topMenu=null;
	private static List<Menu> classMenu=null;
	private static Map<String,List<TreeObj>> trees=null;
	private static List<Menu> admintopMenu=new ArrayList<Menu>();
	private static List<Menu> adminclassMenu=new ArrayList<Menu>();
	private static Map<String,List<TreeObj>> admintrees=new HashMap<String,List<TreeObj>>();
	
	private static List<Function> funs=null;

	@SuppressWarnings("unchecked")
	public static List<User> getUsers(){
		if (users==null){
			//ApplicationContext ctx=new ClassPathXmlApplicationContext(SpringPath);//"classpath:spring*.xml"
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
		    Session session=sessionFactory.openSession();
		    users=session.createCriteria(User.class).list();
		    session.close();
		}
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Department> getOrgs(){
		if (orgs==null){
			//ApplicationContext ctx=new ClassPathXmlApplicationContext(SpringPath);//"classpath:spring*.xml"
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
		    Session session=sessionFactory.openSession();
		    orgs=session.createCriteria(Department.class).list();
		    session.close();
		}
		return orgs;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Function> getFuns(){
		if (funs==null){
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
		    Session session=sessionFactory.openSession();
		    funs=session.createCriteria(Function.class).addOrder(Order.asc("menuId")).list();
		    session.close();
		}
		return funs;
	}

	public static List<Menu> getTopMenu(String userid) {
		getAllMenu(userid.equals("admin"));
		if (userid.equals("admin")){
			return admintopMenu;
		} else{
			List<Menu> tmenu=new ArrayList<Menu>();
			List<String> auths=Utils.getLoginUserauth(); 
			for(Menu m:topMenu){
				boolean b=false;
				if (m.getAuths().contains("ALL")){
					b=true;
				} else{
					for(String s:m.getAuths()){
						if (auths.contains(s)) {
							b=true;
							break;
						}
					}
				}				
				if (b){
					tmenu.add(m);
				}				
			}
			return tmenu;
		}		
	}

	public static List<Menu> getClassMenu(String userid) {
		if (userid.equals("admin")){
			return adminclassMenu;
		} else{
			List<Menu> tmenu=new ArrayList<Menu>();
			List<String> auths=Utils.getLoginUserauth(); 
			for(Menu m:classMenu){
				boolean b=false;
				if (m.getAuths().contains("ALL")){
					b=true;
				} else{
					for(String s:m.getAuths()){
						if (auths.contains(s)) {
							b=true;
							break;
						}
					}
				}				
				if (b){
					tmenu.add(m);
				}				
			}
			return tmenu;
		}
	}	

	@SuppressWarnings("unchecked")
	public static Map<String,List<TreeObj>> getTreeMenu(String userid) {
		if (userid.equals("admin")){
			return admintrees;
		} else{
			List<String> auths=Utils.getLoginUserauth(); 
			Map<String, List<TreeObj>> mtree=(Map<String, List<TreeObj>>)Utils.Json2Object(Utils.Object2Json(trees),new TypeReference<Map<String,List<TreeObj>>>() {});
			
			for(String key:mtree.keySet()){
				List<TreeObj> tObjs=mtree.get(key);
				List<String> tids=new ArrayList<String>();
				for(TreeObj tsubObj:tObjs){
					tids.add(tsubObj.getId());
				}
				for(String tid:tids){
					TreeObj tObj=null;
					for (TreeObj subObj:tObjs){
						if (subObj.getId().equals(tid)){
							tObj=subObj;
							break;
						}
					}			
					boolean b=false;
					if (tObj!=null){
						if (((List<String>)tObj.getExData()).contains("ALL")){
							getsubtree(auths,tObj);
							b=true;			
						} else{
							for(String s:(List<String>)tObj.getExData()){							
								if (auths.contains(s)) {								
									b=true;
									getsubtree(auths,tObj);	
									break;
								}
							}
						}					
						if (!b){
							tObjs.remove(tObj);
						}
					}						
				}			
			}
			return mtree;
		}	
	}
	
	@SuppressWarnings("unchecked")
	private static void getsubtree(List<String> auths,TreeObj tObj){
		if (tObj.getChildren()==null) return;
		List<String> tids=new ArrayList<String>();
		for(TreeObj tsubObj:tObj.getChildren()){
			tids.add(tsubObj.getId());
		}
		for(String tid:tids){
			TreeObj tsubObj=null;
			for (TreeObj subObj:tObj.getChildren()){
				if (subObj.getId().equals(tid)){
					tsubObj=subObj;
					break;
				}
			}			
			boolean b=false;
			if (((List<String>)tsubObj.getExData()).contains("ALL")){
				getsubtree(auths,tsubObj);
				b=true;			
			} else{
				for(String s:(List<String>)tsubObj.getExData()){							
					if (auths.contains(s)) {								
						b=true;
						getsubtree(auths,tsubObj);	
						break;
					}
				}
			}
			if (!b){
				tObj.getChildren().remove(tsubObj);
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void getAllMenu(Boolean isadmin) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
	    Session session=sessionFactory.openSession();
	    Criteria c=session.createCriteria(Menu.class);
	    List<Menu> allmenu=null;
		if (isadmin){			
		    allmenu=c.addOrder(Order.asc("menuType")).addOrder(Order.asc("itemOrder")).list();
		    session.close();
		    loadmenu(true,ctx,allmenu,admintopMenu,adminclassMenu,admintrees);
		} else {
			if (topMenu==null){
			    allmenu=c.add(Restrictions.ne("isadmin", 1)).addOrder(Order.asc("menuType")).addOrder(Order.asc("itemOrder")).list();
			    session.close();
			    topMenu=new ArrayList<Menu>();
			    classMenu=new ArrayList<Menu>();
			    trees=new HashMap<String,List<TreeObj>>();
				loadmenu(false,ctx,allmenu,topMenu,classMenu,trees);
			}
		}
	}	
	
	private static void setPmenuAuth(Menu sMenu,List<Menu> allmenu){
		if (sMenu.getMenuParid()!=null&&!sMenu.getMenuParid().equals("0")) {
			for(Menu menu:allmenu){
	    		if (menu.getMenuId().equals(sMenu.getMenuParid())){
	    			if  (sMenu.getAuths().contains("ALL")){
	    				 menu.getAuths().clear();	    				 
	    				 menu.getAuths().add("ALL");
	    			} else if (!menu.getAuths().containsAll(sMenu.getAuths())){
	    			    menu.getAuths().addAll(sMenu.getAuths());
	    			}
	    			setPmenuAuth(menu,allmenu);
	    		}
		    }
		}		
	}
	
	private static void loadmenu(Boolean isadmin,WebApplicationContext ctx,List<Menu> allmenu, List<Menu> tMenu, List<Menu> cMenu,
			Map<String, List<TreeObj>> ltrees) {
		if (!isadmin){
			List<Menu> sMenus=new ArrayList<Menu>();
			for(Function fun:funs){			
		    	for(Menu menu:allmenu){
			    	if (menu.getMenuType()==3){
			    		if (menu.getMenuId().equals(fun.getMenuId())){
			    			int index=sMenus.indexOf(menu);
			    			if (index<0){
			    				menu.getAuths().add(fun.getFunId());
			    				sMenus.add(menu);
			    			}else{
			    				sMenus.get(index).getAuths().add(fun.getFunId());
			    			}
			    		}
			    	}
			    }
		    }
			for(Menu menu:allmenu){
		    	if (menu.getMenuType()==3){
		    		if (!sMenus.contains(menu)){
		    			menu.getAuths().add("ALL");
		    			sMenus.add(menu);
		    		}
		    	}
		    }
			for(Menu menu:sMenus){
				setPmenuAuth(menu,allmenu);
			}	
		}			
		
	    List<Menu> treeMenu=new ArrayList<Menu>();
	    for(Menu menu:allmenu){
	    	if (menu.getMenuType()==0){
	    		tMenu.add(menu);
	    	} else if (menu.getMenuType()==1){
	    		cMenu.add(menu);
	    	} else{	    		
	    		treeMenu.add(menu);
	    	}
	    }
	        
	    for (Menu pmenu:cMenu){
		    List<TreeObj> treeObjs=new ArrayList<TreeObj>();
		    for (Menu menu:treeMenu){
		    	if (menu.getMenuParid().equals(pmenu.getMenuId())){
		    		TreeObj treeObj=new TreeObj();
		    		treeObj.setId(menu.getMenuId());
		    		treeObj.setText(menu.getMenuName());
		    		treeObj.setExpanded(true);
		    		treeObj.setIcon(ctx.getServletContext().getContextPath()+menu.getMenuImg());
		    		treeObj.setExData(menu.getAuths());
		    		if ((menu.getMenuType()==3)&&(!Utils.isNullOrTemp(menu.getMenuUrl()))){
		    			if (menu.getIsframe()==0){
		    				treeObj.setHref("JavaScript:suunurl({url:\'"
		    		                +ctx.getServletContext().getContextPath()+menu.getMenuUrl()
		    		                +"',title:'"+menu.getMenuName()+"',iconcls:'"+treeObj.getIcon()+"'});");
		    			} else{
		    				treeObj.setHref("JavaScript:suunurl({url:\'"
		    		                +ctx.getServletContext().getContextPath()+menu.getMenuUrl()
		    		                +"',title:'"+menu.getMenuName()+"',iconcls:'"+treeObj.getIcon()+"',isframe:true});");
		    			}
		    		}			    		
		    		treeObjs.add(treeObj);
		    		treeObj=loadchild(ctx,treeMenu,treeObj);
		    		ltrees.put(pmenu.getMenuId(), treeObjs);
		    	}
		    }	
		}
		
	}

	private static TreeObj loadchild(WebApplicationContext sce,List<Menu> treeMenu,TreeObj tObj){
		List<TreeObj> treeObjs=new ArrayList<TreeObj>();
		for (Menu menu:treeMenu){			
	    	if (menu.getMenuParid().equals(tObj.getId())){
	    		TreeObj treeObj=new TreeObj();
	    		treeObj.setId(menu.getMenuId());
	    		treeObj.setText(menu.getMenuName());
	    		treeObj.setExpanded(true);
	    		treeObj.setIcon(sce.getServletContext().getContextPath()+menu.getMenuImg());
	    		treeObj.setExData(menu.getAuths());
	    		if (!Utils.isNullOrTemp(menu.getMenuUrl())){
	    			if (menu.getIsframe()==0){
	    				treeObj.setHref("JavaScript:suunurl({url:\'"
	    		                +sce.getServletContext().getContextPath()+menu.getMenuUrl()
	    		                +"',title:'"+menu.getMenuName()+"',iconcls:'"+treeObj.getIcon()+"'});");
	    			} else{
	    				treeObj.setHref("JavaScript:suunurl({url:\'"
	    		                +sce.getServletContext().getContextPath()+menu.getMenuUrl()
	    		                +"',title:'"+menu.getMenuName()+"',iconcls:'"+treeObj.getIcon()+"',isframe:true});");
	    			}		
	    		}
	    		treeObjs.add(treeObj);
	    		//3
	    		loadchild(sce,treeMenu,treeObj);
	    	}	    	
	    }	
		if (treeObjs.size()>0)
    	    tObj.setChildren(treeObjs);
    	else
    		tObj.setLeaf(true);
		return tObj;		
	}
  
}

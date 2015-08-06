package com.suun.service.system.developer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.developer.Function;
import com.suun.model.system.developer.Menu;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.utils.TreeObj;

@Service
@Transactional
public class MenuManager {

	private SimpleHibernateTemplate<Menu,String> menuDao;
	
	private SimpleHibernateTemplate<com.suun.model.system.developer.Function, String> funDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {		
		menuDao=new SimpleHibernateTemplate<Menu,String>(sessionFactory, Menu.class);
		funDao = new SimpleHibernateTemplate<com.suun.model.system.developer.Function, String>(sessionFactory, com.suun.model.system.developer.Function.class);	
	}
	
	public List<Menu> findMenusByParentId(String parentId){		 
		String hql="from Menu m where m.menuParid=? order by m.itemOrder asc";
		@SuppressWarnings("unchecked")
		List<Menu> menuList=menuDao.find(hql,parentId);		
		return menuList;
	}
	
	@Transactional(readOnly = true)
	public  Menu getMenu(String id){
		String hql="from Menu m where m.id=?";
		Menu menu=(Menu) menuDao.findUnique(hql, id	);	
		return menu;
	}	
		
	@Transactional(readOnly = true)
	public List<String> getAllAuths() {
		List<String> Auths=new ArrayList<String>();
		List<com.suun.model.system.developer.Function> funs=funDao.findAll();
		for (com.suun.model.system.developer.Function fun:funs){
			Auths.add(fun.getFunId());
		}
		return Auths;
	}
	@Transactional(readOnly = true)
	public Page<Menu> findAll(Page<Menu> page){
		return menuDao.findAll(page); 
	}	
		
	/**
	 * 同一节点下，菜单名不能相同
	 * @param parentId
	 * @param name
	 * @param oldname
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isMenuNameUnique(String parentId,String name ,String oldname){
		if (name == null || name.equals(oldname))
			return true;
		String hql="from Menu m where m.menuParid=? and m.menuName=?";
		if (menuDao.findUnique(hql, Long.parseLong(parentId),name)==null){
			return true;
		} else
			return false;
	}	
	
	public void deleteMenu(String id){
		List<Menu> menuList=menuDao.findByProperty("menuParid", id);
		for(Menu menu:menuList){
			deleteMenu(menu.getMenuId());
		}
		List<Function> funList=funDao.findByProperty("menuId", id);
		for(Function fun:funList){
			funDao.delete(fun);
		}			
		menuDao.delete(id);
	}
	
	public void saveMenu(Menu menu){
		menuDao.save(menu);
	}
	
	public void saveMenuAndFunction(Menu menu){
		menuDao.save(menu);
		Function function = new Function();
		function.setFunId("AUTH_"+(new Date()).getTime());
		function.setMenuId(menu.getMenuId());
		function.setFunName(menu.getMenuName());
		function.setUrl(menu.getMenuUrl());
		funDao.save(function);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<TreeObj> getAuthTree(String node,HttpServletRequest request){
		List<TreeObj> trees=new ArrayList<TreeObj>();
		if (node.indexOf(",")>0) return null;
		Menu menu=menuDao.findUniqueByProperty("menuId", node);
		if (menu!=null&&menu.getIsadmin()==0){
			if (menu.getMenuType()==3) {//功能菜单
				List<Function> funcs=getAllFuns(node);
				for(Function fun:funcs){
					TreeObj obj= new TreeObj();
					obj.setId(fun.getMenuId()+','+fun.getFunId());
					obj.setText(fun.getFunName());
					obj.setParentId(fun.getMenuId());
					obj.setChecked("none");
					obj.setLeaf(true);
					obj.setExData("function");
					obj.setIcon(request.getContextPath()+"/resources/images/system/detail.gif");
					trees.add(obj);
				}
				return trees;
			}			
		}
		String hql="from Menu m where m.isadmin=0 and (m.menuParid is Null or m.menuParid='' or m.menuParid=?)";
		if (!node.equals("0"))
			hql="from Menu m where m.isadmin=0 and m.menuParid=?";	
		List<Menu> menus=(List<Menu>)menuDao.find(hql, node);
		for(Menu men:menus){
			TreeObj obj= new TreeObj();
			obj.setId(men.getMenuId());
			obj.setText(men.getMenuName());
			if ((men.getMenuParid()==null) || (men.getMenuParid().equals(""))) {
				obj.setParentId("0");
			} else
				obj.setParentId(men.getMenuParid());
			obj.setChecked("none");
			obj.setLeaf(false);
			obj.setExpanded(true);
			obj.setExData(String.valueOf(men.getMenuType()));
			obj.setIcon(request.getContextPath()+men.getMenuImg());
			trees.add(obj);
		}
		return trees;        
	}
	//Function
	@Transactional(readOnly = true)
	public Function getFunction(String funid) {
		return funDao.get(funid);
	}
	@Transactional(readOnly = true)
	public List<Function> getAllFuns(String node) {
		return funDao.findByProperty("menuId", node);
	}
	@Transactional(readOnly = true)
	public Page<Function> getPageFuns(String node,Page<Function> page) {
		com.suun.publics.hibernate.FilterInfo filterInfo=new com.suun.publics.hibernate.FilterInfo();
		filterInfo.setFieldName("menuId");
		filterInfo.setLogic(Logic.EQUAL);
		filterInfo.setValue(node);
		page.getCondition().getFilterInfos().add(filterInfo);
		return funDao.findAll(page);
	}
	@Transactional(readOnly = true)
	public boolean isFunIdUnique(String funId, String oldFunId) {
		return funDao.isUnique("funId", funId, oldFunId);
	}

	public String saveFunction(Function operatebean) {
		try{
			funDao.save(operatebean);
			return "";
		}catch(Exception e){
			return e.getMessage();
		}		
	}

	public void deleteFun(String id) {
		funDao.delete(id);		
	}
	
	public void deleteMenuByName(String id){
		List<Menu> menuList=menuDao.findByProperty("menuName", id);
		if(menuList.size()!=0){
			List<Menu> funList=menuDao.findByProperty("menuParid", menuList.get(0).getMenuId());
			for(Menu menu2:funList){
				menuDao.delete(menu2);
			}
			for(Menu menu:menuList){
				menuDao.delete(menu);
			}
		}
	}
	
}

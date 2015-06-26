package com.suun.service.system.orgnization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.orgnization.Department;
import com.suun.model.system.orgnization.Depemployees;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.hibernate.FilterInfo.Logic;
/**
 * DepartmentManager
 * @author 
 *
 */
@Service
@Transactional
public class DepartmentManager {
	private SimpleHibernateTemplate<Department, String> depDao;
	private SimpleHibernateTemplate<Depemployees, Long> depempDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		depDao = new SimpleHibernateTemplate<Department, String>(sessionFactory, Department.class);
		depempDao = new SimpleHibernateTemplate<Depemployees, Long>(sessionFactory, Depemployees.class);
	}
	
	@Transactional(readOnly = true)
	public boolean isDepartmentIdUnique(String did, String olddid) {
		return depDao.isUnique("did", did, olddid);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Department> findDepByParentId(String parentId){
		List<Department> list=new ArrayList<Department>();
		String hql="from Department o where o.pid=?";
		list=depDao.find(hql, parentId);
		return list;
	}
	@Transactional(readOnly = true)
	public boolean isDepartmentNameUnique(String parentId,String name ,String oldname){
		if (name == null || name.equals(oldname))
			return true;
		String hql="from Department o where o.pid=? and o.name=?";
		if (depDao.findUnique(hql, parentId,name)==null){
			return true;
		} else
			return false;
	}
	
	@Transactional(readOnly = true)
	public Department getDepartment(String depid) {
		return depDao.get(depid);
	}
	
	public void deleteDepartment(String depid) {
		Condition condition=new Condition();
		List<FilterInfo> filterInfos=new ArrayList<FilterInfo>();
		condition.setFilterInfos(filterInfos);
		List<Depemployees> depemployees=getAllDepemployee(depid,condition);
		for(Depemployees Depemployee:depemployees){
			depempDao.delete(Depemployee);
		}
		depDao.delete(depid);
	}
	
	public void saveDepartment(Department dep) {
		//从JSP提交的Employeeid为""
		if (dep.getHeader().getEmployeeid()=="") dep.setHeader(null);
		depDao.save(dep);
	}
	@Transactional(readOnly = true)
	public List<Depemployees> getAllDepemployee(String DepId,Condition condition){
		/*List<Depemployees> list=new ArrayList<Depemployees>();
		String hql="from Depemployees o where o.department.did=?";
		list=depempDao.find(hql, DepId);
		return list;*/
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("department.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(DepId);
		condition.getFilterInfos().add(f);
		return depempDao.findAll(condition);
	}
	@Transactional(readOnly = true)
	public Page<Depemployees> getAllDepemployee(String treeid,Page<Depemployees> page) {
		Condition condition=page.getCondition();
		com.suun.publics.hibernate.FilterInfo f=new com.suun.publics.hibernate.FilterInfo();
		f.setFieldName("department.did");
		f.setLogic(Logic.EQUAL);
		f.setValue(StringUtils.isEmpty(treeid)?"0":treeid);
		condition.getFilterInfos().add(f);
		page.setCondition(condition);
		return depempDao.findAll(page);
	}
	
	public void saveDepemployees(Depemployees depemployees) {
		depempDao.save(depemployees);
	}
	
	public void deleteDepemployees(String DepId,String EmpId) {
		String hql="delete Depemployees o where o.department.did=? and o.employee.employeeid=?";
		depempDao.executeHql(hql,DepId,EmpId);
	}
	
	public void deleteDepemployees(Long id) {
		depempDao.delete(id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Depemployees getDepemployee(String DepId,String EmpId){
		List<Depemployees> list=new ArrayList<Depemployees>();
		String hql="from Depemployees o where o.department.did=? and o.employee.employeeid=?";
		list=depempDao.find(hql,DepId,EmpId);
		if (list.size()>0){
		    return list.get(0);
		}else{
			return null;
		}
	}
}

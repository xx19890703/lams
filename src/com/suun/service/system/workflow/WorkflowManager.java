package com.suun.service.system.workflow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.system.security.User;
import com.suun.model.system.workflow.Workflow;
import com.suun.model.system.workflow.WorkflowLog;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.OrderBy;
import com.suun.publics.hibernate.SimpleHibernateTemplate;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.utils.Utils;
import com.suun.publics.workflow.SuunTask;


/**
 * Workflow管理
 * DAO均由SimpleHibernateTemplate指定泛型生成.
 * 
 */
@Service
@Transactional
public class WorkflowManager {

	private SimpleHibernateTemplate<Workflow, String> flowDao;
	private SimpleHibernateTemplate<WorkflowLog, String> logDao;
	private SimpleHibernateTemplate<User, String> userDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		flowDao = new SimpleHibernateTemplate<Workflow, String>(sessionFactory, Workflow.class);
		logDao = new SimpleHibernateTemplate<WorkflowLog, String>(sessionFactory, WorkflowLog.class);
		userDao = new SimpleHibernateTemplate<User, String>(sessionFactory, User.class);
	}	
	
	public Session getSession() {
		return logDao.getSessionFactory().getCurrentSession();
	}

	@Transactional(readOnly = true)
	public List<Workflow> getAllFlow() {
		return flowDao.findAll();
	}

	@Transactional(readOnly = true)
	public Workflow getWorkflow(String objName) {
		return flowDao.get(objName);
	}
	
	@Transactional(readOnly = true)
	public List<SuunTask> getPersonalTasks() {
		/*return flowDao.getSessionFactory().getCurrentSession()
				.createSQLQuery("select t.DBID_ as dbid,t.NAME_ as taskName,t.ASSIGNEE_ as assignee,ut.user_name as assigneeName,t.CREATE_ as createTime,t.EXECUTION_ID_ as executionId,"+
						        "l.user_id as operaterId,ul.user_name as operaterName,l.operate,l.objname as objId,w.ObjDescription as objName,l.invoice_id,l.description from jbpm4_task t "+
		                        "inner join sysworkflow_log l on (t.EXECUTION_ID_=l.execution_id and l.state=1) "+
						        "left join users ut on ut.user_id=t.ASSIGNEE_ "+
		                        "left join users ul on ul.user_id=l.user_id "+
		                        "left join workflow w on l.objname=w.objname "+
						        "where t.ASSIGNEE_='"+Utils.getLoginUserId()+"' order by l.id desc")
						        .setResultTransformer(Transformers.aliasToBean(SuunTask.class)).list();*/
		//TaskImpl task=new TaskImpl();
		//task.setAssignee(Utils.getLoginUserId());
		List<WorkflowLog> wfls=logDao.findByProperty("state", 1);
		List<SuunTask> suuntasks=new ArrayList<SuunTask>();
		for(WorkflowLog wfl:wfls ){
			if (wfl.getTask().getAssignee().equalsIgnoreCase(Utils.getLoginUserId())){
				SuunTask suuntask=new SuunTask();
				suuntask.setDbid(wfl.getTask().getDbid());
				suuntask.setTaskName(wfl.getTask().getName());
				suuntask.setAssignee(wfl.getTask().getAssignee());
				suuntask.setCreateTime(wfl.getTask().getCreateTime());
				suuntask.setExecutionId(wfl.getTask().getExecutionId());
				suuntask.setOperaterId(wfl.getUser().getUserId());
				suuntask.setOperaterName(wfl.getUser().getEmployee().getEmployeename());			
				suuntask.setOperate(wfl.getOperate());
				suuntask.setObjId(wfl.getWorkflow().getObjName());
				suuntask.setObjName(wfl.getWorkflow().getObjDescription());
				suuntask.setInvoice_id(wfl.getInvoice_id());
				suuntask.setDescription(wfl.getDescription());
				User u=userDao.findUniqueByProperty("userId", wfl.getTask().getAssignee());
				if (u!=null){
					suuntask.setAssigneeName(u.getEmployee().getEmployeename());
				}
				suuntasks.add(suuntask);
			}			
		}
		return suuntasks;
	}
	
	@Transactional(readOnly = true)
	public List<SuunTask> taskHiss(String taskid) {
		Condition cond=new Condition();
		OrderBy orderBy=new OrderBy();
		orderBy.setFieldName("op_time");
		orderBy.setAsc(true);
		List<OrderBy> orderBys=new ArrayList<OrderBy>();
		orderBys.add(orderBy);
		cond.setOrderBys(orderBys);
		FilterInfo filterInfo=new FilterInfo();
		filterInfo.setFieldName("task.executionId");
		filterInfo.setLogic(Logic.EQUAL);
		filterInfo.setValue(taskid);
		List<FilterInfo> filterInfos=new ArrayList<FilterInfo>();
		filterInfos.add(filterInfo);
		cond.setFilterInfos(filterInfos);
		List<WorkflowLog> wfls=logDao.findAll(cond);
		//List<WorkflowLog> wfls=logDao.findByProperty("task.executionId", taskid);
		List<SuunTask> suuntasks=new ArrayList<SuunTask>();
		for(WorkflowLog wfl:wfls ){
			//if (wfl.getState()!=1){
				SuunTask suuntask=new SuunTask();
				suuntask.setDbid(wfl.getTask().getDbid());
				suuntask.setTaskName(wfl.getTask().getName());
				suuntask.setAssignee(wfl.getTask().getAssignee());
				suuntask.setCreateTime(wfl.getOp_time());
				suuntask.setExecutionId(wfl.getTask().getExecutionId());
				suuntask.setOperaterId(wfl.getUser().getUserId());
				suuntask.setOperaterName(wfl.getUser().getEmployee().getEmployeename());			
				suuntask.setOperate(wfl.getOperate());
				suuntask.setObjId(wfl.getWorkflow().getObjName());
				suuntask.setObjName(wfl.getWorkflow().getObjDescription());
				suuntask.setInvoice_id(wfl.getInvoice_id());
				suuntask.setDescription(wfl.getDescription());
				suuntasks.add(suuntask);
			//}
			
		}
		return suuntasks;
	}
	
    public void writelog(String objName,String operateid,String operate){
    	writelog(objName,operateid,operate,false);
	}
	
	public void writelog(String objName,String operateid,String operate,boolean isRemind){
		/*WorkflowLog wfl=new WorkflowLog();
		wfl.setOp_time(new java.sql.Date(System.currentTimeMillis()));
		wfl.setOperate(operate);
		wfl.setInvoice_id(operateid);
		wfl.setState(1);
		User u=new User();
		u.setUserId(Utils.getLoginUserId());
		Workflow w=new Workflow();
		w.setObjName(objName);
		ExecutionImpl e=new ExecutionImpl();
		e.setId(objName+"."+operateid);		
		DbSession dbsession = JbpmTemplate.getJbpmTemplate().getEnvironmentImpl().getFromCurrent(DbSession.class); 
		TaskImpl t=dbsession.createTask();
		t.setDbid(0);
		t.setExecution(e);
		wfl.setTask(t);
		if (isRemind){
			logDao.createQuery("update WorkflowLog set state=0 where workflow.objName=? and invoice_id=?", objName,operateid).executeUpdate();		
		}
		logDao.save(wfl);*/
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logDao.executeSQL("update sys_workflow_log set state=0 where objName='"+objName+"' and invoice_id='"+operateid+"'");
		if (isRemind){			
			logDao.executeSQL("insert into sys_workflow_log (objName,invoice_id,operate,user_id,op_time,execution_id,state) "+
					          "values ('"+objName+"','"+operateid+"','"+operate+"','"+Utils.getLoginUserId()+"','"+
					          df.format(new java.sql.Date(System.currentTimeMillis()))+"','"+objName+"."+operateid+"',1)");
		} else {
			logDao.executeSQL("insert into sys_workflow_log (objName,invoice_id,operate,user_id,op_time,execution_id,state) "+
			          "values ('"+objName+"','"+operateid+"','"+operate+"','"+Utils.getLoginUserId()+"','"+
			          df.format(new java.sql.Date(System.currentTimeMillis()))+"','"+objName+"."+operateid+"',0)");
		}
		
	}
}


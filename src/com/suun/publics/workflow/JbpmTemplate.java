package com.suun.publics.workflow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.sql.DataSource;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.ProcessInstanceQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.api.job.Job;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;

import org.jbpm.pvm.internal.env.EnvironmentFactory;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.identity.impl.IdentitySessionImpl;
import org.jbpm.pvm.internal.identity.impl.UserImpl;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;
import org.jbpm.pvm.internal.model.ActivityCoordinatesImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.jbpm.pvm.internal.task.OpenTask;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.suun.publics.workflow.image.JpdlModel;
import com.suun.publics.workflow.image.JpdlModelDrawer;

/**
 * 封装的JBPM template.
 *
 */
public class JbpmTemplate {
	
    private ProcessEngine processEngine;
    private DataSource dataSource;
    //private SessionFactory sessionFactory;
    private static JbpmTemplate jbpmTemplate=null; 

    public static JbpmTemplate getJbpmTemplate() {
        if (jbpmTemplate == null) {
        	ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
        	//WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        	jbpmTemplate = (JbpmTemplate) ctx.getBean("jbpmTemplate");
        }

        return jbpmTemplate;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /*public void setDataSource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }*/
    
    public EnvironmentImpl getEnvironmentImpl() {
    	EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;
		EnvironmentImpl env = environmentFactory.openEnvironment();

        return env;
    }
    
    public String getDeploymentDbIdByObjname(String objname) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                                                               .orderAsc(ProcessDefinitionQuery.PROPERTY_NAME)
                                                               .list();
        for (ProcessDefinition pd : definitions) {
        	if (objname.equalsIgnoreCase(pd.getName())){
        		return pd.getDeploymentId();
        	}
        }

        return "";
    }

    public List<ProcessDefinition> getLatestProcessDefinitions() {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                                                               .orderAsc(ProcessDefinitionQuery.PROPERTY_NAME)
                                                               .list();
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();

        for (ProcessDefinition pd : definitions) {
            String key = pd.getKey();
            ProcessDefinition processDefinition = map.get(key);

            if ((processDefinition == null)
                    || (processDefinition.getVersion() < pd.getVersion())) {
                map.put(key, pd);
            }
        }

        return new ArrayList<ProcessDefinition>(map.values());
    }

    public List<ProcessDefinition> getSuspendedProcessDefinitions() {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();

        return repositoryService.createProcessDefinitionQuery().suspended()
                                .orderAsc(ProcessDefinitionQuery.PROPERTY_NAME)
                                .list();
    }

    public void deployXml(String name, InputStream inputStream) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
    	repositoryService.createDeployment();
    	DeploymentImpl deploymentImpl=new DeploymentImpl();
    	//deploymentImpl.setSession(sessionFactory.getCurrentSession());
    	deploymentImpl
        .addResourceFromInputStream(name, inputStream)
                         .deploy();
    }

    public void deployZip(InputStream inputStream) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        repositoryService.createDeployment()
                         .addResourcesFromZipInputStream(new ZipInputStream(
                inputStream)).deploy();
    }

    public void deployXml(String xml) {        
        try {
        	String xml1="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+xml;
        	JpdlModel jpdlModel = new JpdlModel (xml1);        	
            BufferedImage bi=new JpdlModelDrawer().draw(jpdlModel);
        	bi.flush(); 
            
            ByteArrayOutputStream bs = new ByteArrayOutputStream();  
             
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs); 
                 
            ImageIO.write(bi, "png",imOut); 
                 
            InputStream is = new ByteArrayInputStream(bs.toByteArray()); 
            Element rootEl=new SAXReader().read(new StringReader(xml1)).getRootElement(); 
            String processName=rootEl.attributeValue("name");
            
            RepositoryService repositoryService = processEngine.getRepositoryService();  
            String dbid=getDeploymentDbIdByObjname(processName);
            if (!dbid.equals(""))
                repositoryService.deleteDeploymentCascade(dbid);
            
            repositoryService.createDeployment()
                         .addResourceFromString(processName+".jpdl.xml", xml)
                         .addResourceFromInputStream(processName+".png", is)
                         .deploy();
		} catch (FileNotFoundException e) {
			    e.printStackTrace();
		} catch (IOException e) { 
                e.printStackTrace(); 
        } catch (Exception e) {
			e.printStackTrace();
		}
        
    }

    public void suspendProcessDefinitionById(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();
        repositoryService.suspendDeployment(pd.getDeploymentId());
    }

    public void resumeProcessDefinitionById(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();
        repositoryService.resumeDeployment(pd.getDeploymentId());
    }

    public void removeProcessDefinitionById(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();
        repositoryService.deleteDeploymentCascade(pd.getDeploymentId());
    }

    public Set<String> getTransitionsForTask(String id) {
        TaskService taskService = processEngine.getTaskService();

        return taskService.getOutcomes(id);
    }

    @SuppressWarnings("unchecked")
	public List<Transition> getTransitionsForSignalProcess(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();
        ProcessInstance pi = executionService.findProcessInstanceById(id);
        EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;
        EnvironmentImpl env = environmentFactory.openEnvironment();

        try {
            ExecutionImpl executionImpl = (ExecutionImpl) pi;
            ActivityImpl activity = executionImpl.getActivity();

            return (List<Transition>) activity.getOutgoingTransitions();
        } finally {
            env.close();
        }
    }

    public void completeTask(String dbid, String transitionName,
        Map<String, Object> variables) {
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariables(dbid, variables);
        
        taskService.completeTask(dbid, transitionName);
    }
    /** 
     * 开始一个新的流程实例 
     * @param id 
     * @param variables 该流程实例要用到的变量 
     * @return 
     */  
    public void startProcess(String id, Map<String, Object> variables) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ExecutionService executionService = processEngine
            .getExecutionService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();

        executionService.startProcessInstanceById(pd.getId(), variables);
    }

    @SuppressWarnings("unused")
	public void signalProcess(String id, String transitionName,
        Map<String, Object> variables) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ExecutionService executionService = processEngine
            .getExecutionService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();
        executionService.setVariables(id, variables);

        executionService.signalExecutionById(id, transitionName);
    }

    public List<ProcessInstance> getProcessInstances(String pdId) {
        ExecutionService executionService = processEngine
            .getExecutionService();

        return executionService.createProcessInstanceQuery()
                               .processDefinitionId(pdId).list();
    }
    
    public List<HistoryProcessInstance> getHisProcessInstances(String pdId) {
    	HistoryService historyService = processEngine.getHistoryService();

        return historyService.createHistoryProcessInstanceQuery()
        		.processInstanceId(pdId).list();
    }
    
    public List<ProcessInstance> getSuspendedProcessInstances() {
        ExecutionService executionService = processEngine
            .getExecutionService();

        return executionService.createProcessInstanceQuery().suspended()
                               .list();
    }

    public Set<ActivityCoordinates> getActivityCoordinates(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ExecutionService executionService = processEngine
            .getExecutionService();

        ProcessInstance pi = executionService.findProcessInstanceById(id);
        Set<ActivityCoordinates> acs=new HashSet<ActivityCoordinates> ();
        for( String activityName : getActivityNames(id) ) {  
        	  ActivityCoordinates ac = repositoryService.getActivityCoordinates(   
        	       pi.getProcessDefinitionId(),activityName );  
        	  acs.add(ac);
        }
        return acs;
    }

    // 通过processId，获取所有活跃的节点： 
    public Set<String> getActivityNames(String id){
    	 ExecutionService executionService = processEngine
    	            .getExecutionService();
    	 return executionService.findProcessInstanceById(id)
    			 .findActiveActivityNames();
    }
    
    public void suspendProcessInstance(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();

        ProcessInstance pi = executionService.findProcessInstanceById(id);
        /**
         * FIXME: 这个部分肯定是jBPM的bug，
         * api里的Execution接口和ExecutionService接口竟然都没有suspend和resume方法。
         */
        ((ExecutionImpl) pi).suspend();
    }

    public void resumeProcessInstance(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();

        ProcessInstance pi = executionService.createProcessInstanceQuery()
                                             .suspended()
                                             .processInstanceId(id)
                                             .uniqueResult();

        /**
         * FIXME: 这个部分肯定是jBPM的bug，
         * api里的Execution接口和ExecutionService接口竟然都没有suspend和resume方法。
         */
        ((ExecutionImpl) pi).resume();
    }

    public void endProcessInstance(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();

        executionService.endProcessInstance(id, Execution.STATE_ENDED);
    }

    public void removeProcessInstance(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();

        executionService.deleteProcessInstance(id);
    }

    public Map<String, Object> getVariablesForTask(String id) {
        TaskService taskService = processEngine.getTaskService();
        Set<String> names = taskService.getVariableNames(id);

        return taskService.getVariables(id, names);
    }

    public Map<String, Object> getVariablesForProcess(String id) {
        ExecutionService executionService = processEngine
            .getExecutionService();
        Set<String> names = executionService.getVariableNames(id);

        return executionService.getVariables(id, names);
    }

    public List<Task> getTasks() {
        EnvironmentImpl env = ((EnvironmentFactory) processEngine).openEnvironment();
        try {
            TaskService taskService = processEngine.getTaskService();
            List<Task> tasks=taskService.createTaskQuery().list();
            /*解决懒加载
            for (Task task : tasks) {
	        	((TaskImpl) task).getTaskDefinition();
	        	((TaskImpl) task).getParticipations();
	        	((TaskImpl) task).getExecution();
	        	((TaskImpl) task).getProcessInstance();
	        	((TaskImpl) task).getSwimlane();
	        	((TaskImpl) task).getSuperTask();
	        	((TaskImpl) task).getSubTasks();
	        	((TaskImpl) task).getVariables();
	        	((TaskImpl) task).getParentVariableScope();
	        }*/
            return tasks;
        } finally {
            env.close();
        }        
    }
    public Task getTasks(String dbid) {
    	TaskService taskService = processEngine.getTaskService();
        return taskService.getTask(dbid);
    }

    public List<Task> getPersonalTasks(String username) {
        TaskService taskService = processEngine.getTaskService();

        return taskService.findPersonalTasks(username);
    }

    public List<Task> getGroupTasks(String username) {
        TaskService taskService = processEngine.getTaskService();

        return taskService.findGroupTasks(username);
    }

    public void takeTask(String dbid, String username) {
        TaskService taskService = processEngine.getTaskService();
        taskService.takeTask(dbid, username);
    }

    public void cancelTask(String dbid) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.getTask(dbid);

        EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;

        EnvironmentImpl env = environmentFactory.openEnvironment();

        try {
            OpenTask openTask = (OpenTask) task;
            // FIXME: jbpm-4.0.CR1 is cancel, now jbpm-4.0.GA is delete
            openTask.delete("no reason");
        } finally {
            env.close();
        }
    }

    public void releaseTask(String dbid) {
        TaskService taskService = processEngine.getTaskService();
        taskService.assignTask(dbid, null);
    }
    
    public String getXmlResourceFromProcessDefinition(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
        		                                .processDefinitionKey(id)
                                                //.processDefinitionId(id)
                                                .uniqueResult();
        if (pd != null) {
            String processName = pd.getName() + ".jpdl.xml";
            //System.out.println("processName0:  " + processName);
            InputStream is=repositoryService.getResourceAsStream(pd.getDeploymentId(),
                processName);
            int i = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
				while ((i = is.read()) != -1) {
				    baos.write(i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            return baos.toString();
        } else {
        	return "<process xmlns='http://jbpm.org/4.4/jpdl' name='"+id+"'><description>测试</description><start name='开始' g='87,15,48,48'><transition to='总经理审批'/></start>"+
                    "<task g='66,93,90,50' name='总经理审批'><transition to='结束' name='通过'/></task>"+//<transition name='驳回' g='191,118;191,38:-65,0' to='开始'/>
                    "<end name='结束' g='89,167,48,48'/></process>";
        }
    }
    
    public InputStream getPngResourceFromProcessDefinition(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                                                .processDefinitionId(id)
                                                .uniqueResult();
        if (pd != null) {
            String processName = pd.getName() + ".png";
            //System.out.println("processName0:  " + processName);
            return repositoryService.getResourceAsStream(pd.getDeploymentId(),
                processName);
        } else {
            return null;
        }
    }

    public InputStream getResourceFromProcessInstance(String id) {
        RepositoryService repositoryService = processEngine
            .getRepositoryService();
        ExecutionService executionService = processEngine
            .getExecutionService();

        ProcessInstanceQuery query = executionService
            .createProcessInstanceQuery();
        query.processInstanceId(id);

        Execution processInstance = (Execution) query.uniqueResult();
        ProcessDefinition pd = ((ExecutionImpl) processInstance)
            .getProcessDefinition();
        String processName = pd.getName() + ".jpdl.xml";
        System.out.println("processName:  " + processName);

        return repositoryService.getResourceAsStream(pd.getDeploymentId(),
            processName);
    }

    public List<User> getUsers() {
        IdentityService identityService = processEngine.getIdentityService();

        return identityService.findUsers();
    }

    public List<Group> getGroupsByUser(String id) {
        IdentityService identityService = processEngine.getIdentityService();

        return identityService.findGroupsByUser(id);
    }

    public void addUser(String id, String password, String givenName,
        String familyName) {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.createUser(id, password, givenName, familyName);
    }

    public void removeUser(String id) {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.deleteUser(id);
    }

    public List<Group> getGroups() {
        EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;
        EnvironmentImpl env = environmentFactory.openEnvironment();

        try {
            IdentitySession identitySession = env.get(IdentitySession.class);

            return ((IdentitySessionImpl) identitySession).findGroups();
        } finally {
            env.close();
        }
    }

    public void addGroup(String name, String type, String parentGroupId) {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.createGroup(name, type, parentGroupId);
    }

    public void removeGroup(String id) {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.deleteGroup(id);
    }

    public void addMember(String userId, String groupId, String role) {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.createMembership(userId, groupId, role);
    }

    public boolean checkLogin(String username, String password) {
        IdentityService identityService = processEngine.getIdentityService();

        UserImpl user = (UserImpl) identityService.findUserById(username);

        return (user != null) && password.equals(user.getPassword());
    }

    public ProcessDefinition getProcessDefinitionByProcessInstanceId(
        String processInstanceId) {
        ExecutionService executionService = processEngine
            .getExecutionService();
        ProcessInstance pi = executionService.findProcessInstanceById(processInstanceId);
        EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;
        EnvironmentImpl env = environmentFactory.openEnvironment();

        try {
            return ((ExecutionImpl) pi).getProcessDefinition();
        } finally {
            env.close();
        }
    }

    public String reportOverallActivity() {
        StringBuffer buff = new StringBuffer(
                "<graph showNames='Overall Activity' decimalPrecision='2'>");
        Connection conn = null;

        try {
            conn = dataSource.getConnection();

            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(
                    "SELECT d.DBID_ as dpl, p.STRINGVAL_ as processId"
                    + " FROM JBPM4_DEPLOYMENT d, JBPM4_DEPLOYPROP p"
                    + " WHERE p.KEY_='pdid'"
                    + " AND d.DBID_=p.DEPLOYMENT_"
                    + " GROUP BY dpl,processId");

            while (rs.next()) {
                buff.append("<set name='").append(rs.getString("dpl"))
                    .append("' value='").append(rs.getString("processId"))
                    .append("'/>");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }

        buff.append("</graph>");

        return buff.toString();
    }

    public String reportMostActiveProcess() {
        StringBuffer buff = new StringBuffer(
                "<graph showNames='Most Active Process' decimalPrecision='2'>");
        Connection conn = null;

        try {
            conn = dataSource.getConnection();

            Statement state = conn.createStatement();

            String sql = "  select d.STRINGVAL_ as id, count(p.ID_) as num"
                + "    from JBPM4_DEPLOYPROP d left join JBPM4_HIST_PROCINST p"
                + "      on d.STRINGVAL_=p.PROCDEFID_"
                + "   where d.KEY_='pdid' group by d.STRINGVAL_";
            ResultSet rs = state.executeQuery(sql);

            while (rs.next()) {
                buff.append("<set name='").append(rs.getString("id"))
                    .append("' value='").append(rs.getString("num"))
                    .append("'/>");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }

        buff.append("</graph>");

        return buff.toString();
    }

    public List<ActivityCoordinates> getHistoryActivities(
        String processInstanceId) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoryActivityInstanceQuery query = historyService
            .createHistoryActivityInstanceQuery();
        List<HistoryActivityInstance> activities = query.executionId(processInstanceId)
                                                        .list();

        ProcessDefinitionImpl processDefinition = (ProcessDefinitionImpl) this
            .getProcessDefinitionByProcessInstanceId(processInstanceId);
        List<ActivityCoordinates> list = new ArrayList<ActivityCoordinates>();
        ActivityImpl start = processDefinition.getInitial();
        ActivityCoordinates startAc = start.getCoordinates();
        startAc = new ActivityCoordinatesImpl(startAc.getX(),
                startAc.getY(), 48, 48);
        list.add(startAc);

        for (HistoryActivityInstance activity : activities) {
            String activityName = activity.getActivityName();
            ActivityImpl activityImpl = processDefinition.findActivity(activityName);
            ActivityCoordinates ac = activityImpl.getCoordinates();

            if (activityImpl.getType().equals("decision")
                    || activityImpl.getType().equals("fork")
                    || activityImpl.getType().equals("join")
                    || activityImpl.getType().equals("end")
                    || activityImpl.getType().equals("end-cancel")
                    || activityImpl.getType().equals("end-error")) {
                ac = new ActivityCoordinatesImpl(ac.getX(), ac.getY(), 48,
                        48);
            }

            list.add(ac);
        }

        return list;
    }

    public List<HistoryActivityInstance> getHistoryActivitiesByProcessInstanceId(
        String processInstanceId) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoryActivityInstanceQuery query = historyService
            .createHistoryActivityInstanceQuery();

        return query.executionId(processInstanceId).list();
    }

    public List<Job> getJobs() {
        ManagementService managementService = processEngine
            .getManagementService();

        return managementService.createJobQuery().list();
    }
    
    public ProcessInstance addProcessInstance(String processDefinitionKey,
			String processInstanceKey) {
		return processEngine.getExecutionService().startProcessInstanceByKey(processDefinitionKey,
				processInstanceKey);

	}
    
    public void deleteProcessInstance(String processDefinitionKey,
			String processInstanceKey) {
		processEngine.getExecutionService()
				.deleteProcessInstanceCascade(processDefinitionKey+"."+processInstanceKey);

	}
    
}

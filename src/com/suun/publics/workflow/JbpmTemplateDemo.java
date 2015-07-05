package com.suun.publics.workflow;

import java.util.List;
import java.util.Map;

import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;

/**
 * jbpm模板类(初步实现)
 * 
 * @author Administrator
 * 
 */
public class JbpmTemplateDemo {

	/**
	 * 部署流程到数据库
	 * 
	 * @param resourceName
	 *            资源文件名字 比如(org/forever/jbpm/jpdl/process.jpdl.xml)
	 * @return 返回流程定义id(格式：key-version)
	 */
	public String Deploy(String resourceName) {
		return repositoryService.createDeployment().addResourceFromClasspath(
				resourceName).deploy();
	}

	/**
	 * 创建一个新的流程实例
	 * 
	 * @param processDefinitionKey
	 *            (process.jpdl.xml中process标签的key)
	 * @param processInstanceKey
	 *            (用户给的key，比如一个请假单的id)
	 * @return 流程实例
	 */
	public ProcessInstance addProcessInstance(String processDefinitionKey,
			String processInstanceKey) {
		return executionService.startProcessInstanceByKey(processDefinitionKey,
				processInstanceKey);

	}
	
	/**
	 * 创建一个新的流程实例
	 * @param processDefinitionKey(process.jpdl.xml中process标签的key)
	 * @param variables 该流程实例要用到的变量
	 * @param processInstanceKey(用户给定的业务key)
	 * @return
	 */
	public ProcessInstance addProcessInstance(
			String processDefinitionKey,
			Map<String, ?> variables,
			String processInstanceKey){
		return executionService.startProcessInstanceByKey(processDefinitionKey, variables, processInstanceKey);
	}
	
	/**
	 * 提交任务
	 * @param taskId 任务id
	 */
	public void completeTask(String taskId){
		taskService.completeTask(taskId);
	}
	
	/**
	 * 将任务流转到指定名字的流程中去
	 * @param taskId
	 * @param outcome
	 */
	public void completeTask(String taskId,String outcome){
		taskService.completeTask(taskId, outcome);
	}

	/**
	 * 根据key获取流程实例(这里我使用的uuid)
	 * 
	 * @param key
	 *            (对应于数据库表jbpm4_execution中的KEY_字段)
	 * @return 返回查找到得流程实例，没有返回null
	 */
	public ProcessInstance getProcessInstance(String key) {
		return executionService.createProcessInstanceQuery()
				.processInstanceKey(key).uniqueResult();
	}
	
	
	/**
	 * 根据executionId获取指定的变量值
	 * @param executionId
	 * @param variableName
	 * @return
	 */
	public Object getVariableByexecutionId(String executionId,String variableName){
		return executionService.getVariable(executionId, variableName);
	}
	
	
	/**
	 * 根据任务id获取指定变量值
	 * @param taskId
	 * @param variableName
	 * @return
	 */
	public Object getVariableByTaskId(String taskId,String variableName){
		return taskService.getVariable(taskId, variableName);
	}
	
	/**
	 * 获取指定用户名字的任务
	 * @param userId
	 * @return
	 */
	public List<Task> findPersonalTasks(String userId){
		return taskService.findPersonalTasks(userId);
	}
	
	/**
	 * 根据任务id获取任务
	 * @param taskId
	 * @return
	 */
	public Task getTask(String taskId) {
		return taskService.getTask(taskId);
		
	}
	
	/**
	 * 根据流程实例id获取
	 * @param executionId
	 * @return
	 */
	public Execution findExecutionById(String executionId) {
		return executionService.findExecutionById(executionId);
	}

	/**
	 * 彻底删除文件的部署
	 * 
	 * @param deploymentId流程定义id
	 */
	public void deleteDeploymentCascade(String deploymentId) {
		repositoryService.deleteDeploymentCascade(deploymentId);
	}

	public JbpmTemplateDemo() {
		
	}

	public JbpmTemplateDemo(ProcessEngine processEngine) {
		this.processEngine = processEngine;
		repositoryService = processEngine.getRepositoryService();
		executionService = processEngine.getExecutionService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
		managementService = processEngine.getManagementService();
	}

	private ProcessEngine processEngine;
	private RepositoryService repositoryService = null;
	private ExecutionService executionService = null;
	private TaskService taskService = null;
	private HistoryService historyService = null;
	private ManagementService managementService = null;

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
		System.out.println(processEngine);
		repositoryService = processEngine.getRepositoryService();
		executionService = processEngine.getExecutionService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
		managementService = processEngine.getManagementService();
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public ExecutionService getExecutionService() {
		return executionService;
	}

	public void setExecutionService(ExecutionService executionService) {
		this.executionService = executionService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	

	

}

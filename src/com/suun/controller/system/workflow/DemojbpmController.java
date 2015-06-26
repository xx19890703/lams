package com.suun.controller.system.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.api.job.Job;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.suun.publics.controller.BaseController;
import com.suun.publics.utils.Utils;
import com.suun.publics.workflow.JbpmTemplate;

@Controller 
public class DemojbpmController extends BaseController{

	private JbpmTemplate jbpmTemplate; 

    private JbpmTemplate getJbpmTemplate() {
        if (jbpmTemplate == null) {
        	ApplicationContext ctx = WebApplicationContextUtils
               .getWebApplicationContext(this.getServletContext());
        	jbpmTemplate = (JbpmTemplate) ctx.getBean("jbpmTemplate");
        }

        return jbpmTemplate;
    }
	

        @RequestMapping
	    public void deployXml(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        //JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	       //jbpmTemplate.Deploy("org/forever/jbpm/jpdl/process.jpdl.xml");

	        response.getWriter().print("{success:true}");
	    }

	    public void suspendProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.suspendProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void resumeProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.resumeProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void transitions(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        String type = request.getParameter("type");
	        List<Transition> transitions = null;

	        if ("taskComplete".equals(type)) {
	            String id = request.getParameter("dbid");
	            Set<String> outgoes = jbpmTemplate.getTransitionsForTask(id);
	            StringBuffer buff = new StringBuffer();
	            buff.append("[");

	            for (String str : outgoes) {
	                buff.append("'").append(str).append("',");
	            }

	            if (!outgoes.isEmpty()) {
	                buff.deleteCharAt(buff.length() - 1);
	            }

	            buff.append("]");
	            response.getWriter().print(buff.toString());

	            return;
	        } else if ("processStart".equals(type)) {
	            response.getWriter().print("[null]");

	            return;
	        } else if ("processSignal".equals(type)) {
	            String id = request.getParameter("id");
	            transitions = jbpmTemplate.getTransitionsForSignalProcess(id);
	        }

	        StringBuffer buff = new StringBuffer();
	        buff.append("[");

	        for (Transition t : transitions) {
	            buff.append("'").append(t.getName()).append("',");
	        }

	        if (!transitions.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]");
	        response.getWriter().print(buff.toString());
	    }

	    public void selectTransition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        Map<String, Object> variables = new HashMap<String, Object>();
	        String[] names = request.getParameterValues("names");
	        String[] values = request.getParameterValues("values");

	        if (names != null) {
	            for (int i = 0; i < names.length; i++) {
	                variables.put(names[i], values[i]);
	            }
	        }

	        String type = request.getParameter("type");

	        if ("taskComplete".equals(type)) {
	            String dbid = request.getParameter("dbid");
	            String t = request.getParameter("transition");
	            jbpmTemplate.completeTask(dbid, t, variables);
	        } else if ("processStart".equals(type)) {
	            String id = request.getParameter("id");
	            jbpmTemplate.startProcess(id, variables);
	        } else if ("processSignal".equals(type)) {
	            String id = request.getParameter("id");
	            String t = request.getParameter("transition");
	            jbpmTemplate.signalProcess(id, t, variables);
	        }

	        response.getWriter().print("{success:true}");
	    }

	    public void processInstances(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        String pdId = request.getParameter("pdId");
	        List<ProcessInstance> pis = jbpmTemplate.getProcessInstances(pdId);
	        StringBuffer buff = new StringBuffer("{result:[");

	        for (ProcessInstance pi : pis) {
	            buff.append("{id:'").append(pi.getId()).append("',name:'")
	                .append(pi.getName()).append("',key:'").append(pi.getKey())
	                .append("',state:'").append(pi.getState()).append("'},");
	        }

	        if (!pis.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void suspendedProcessInstances(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        List<ProcessInstance> pis = jbpmTemplate
	            .getSuspendedProcessInstances();
	        StringBuffer buff = new StringBuffer("{result:[");

	        for (ProcessInstance pi : pis) {
	            buff.append("{id:'").append(pi.getId()).append("',name:'")
	                .append(pi.getName()).append("',key:'").append(pi.getKey())
	                .append("',state:'").append(pi.getState()).append("'},");
	        }

	        if (!pis.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void activity(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        /*JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        String id = request.getParameter("id");
	        ActivityCoordinates ac = jbpmTemplate.getActivityCoordinates(id);
	        response.getWriter()
	                .print("{x:" + ac.getX() + ",y:" + ac.getY() + ",w:"
	            + ac.getWidth() + ",h:" + ac.getHeight() + "}");*/
	    }

	    public void suspendProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.suspendProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void resumeProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.resumeProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void endProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.endProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void variables(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String type = request.getParameter("type");
	        Map<String, Object> variables = null;

	        if ("taskComplete".equals(type)) {
	            String id = request.getParameter("dbid");
	            variables = jbpmTemplate.getVariablesForTask(id);
	        } else if ("processStart".equals(type)) {
	            variables = new HashMap<String, Object>();
	        } else if ("processSignal".equals(type)) {
	            String id = request.getParameter("id");
	            variables = jbpmTemplate.getVariablesForProcess(id);
	        }

	        StringBuffer buff = new StringBuffer("{result:[");

	        for (Map.Entry<String, Object> entry : variables.entrySet()) {
	            buff.append("{name:'").append(entry.getKey())
	                .append("',value:'").append(entry.getValue()).append("'},");
	        }

	        if (!variables.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }
	    
        @RequestMapping
	    @ResponseBody
	    public Map<String, Object> tasks(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
			List<Task> tasks = getJbpmTemplate().getTasks(); 
	        Map<String, Object> modelMap = new HashMap<String, Object>();
	        modelMap.put("result", tasks);
	        /*StringBuffer buff = new StringBuffer("{result:[");

	        for (Task task : tasks) {
	            buff.append("{name:'").append(task.getName())
	                .append("',assignee:'").append(task.getAssignee())
	                .append("',createTime:'").append(task.getCreateTime())
	                .append("',duedate:'").append(task.getDuedate())
	                .append("',priority:'").append(task.getPriority())
	                .append("',description:'").append(task.getDescription())
	                .append("',dbid:'").append(task.getId()).append("'},");
	        }

	        if (!tasks.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");

	        response.getWriter().print(buff.toString());*/
			return modelMap;
	        
	    }

	    public void personalTasks(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        List<Task> tasks = getJbpmTemplate().getPersonalTasks(Utils.getLoginUserId());
	        Map<String, Object> modelMap = new HashMap<String, Object>();
	        modelMap.put("result", tasks);
	        /*StringBuffer buff = new StringBuffer("{result:[");

	        for (Task task : tasks) {
	            buff.append("{name:'").append(task.getName())
	                .append("',assignee:'").append(task.getAssignee())
	                .append("',createTime:'").append(task.getCreateTime())
	                .append("',duedate:'").append(task.getDuedate())
	                .append("',priority:'").append(task.getPriority())
	                .append("',description:'").append(task.getDescription())
	                .append("',dbid:'").append(task.getId()).append("'},");
	        }

	        if (!tasks.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");

	        response.getWriter().print(buff.toString());
	        */
	    }

	    public void groupTasks(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String username = (String) request.getSession()
	                                          .getAttribute("currentUsername");
	        List<Task> tasks = jbpmTemplate.getGroupTasks(username);
	        StringBuffer buff = new StringBuffer("{result:[");

	        for (Task task : tasks) {
	            buff.append("{name:'").append(task.getName())
	                .append("',assignee:'").append(task.getAssignee())
	                .append("',createTime:'").append(task.getCreateTime())
	                .append("',duedate:'").append(task.getDuedate())
	                .append("',priority:'").append(task.getPriority())
	                .append("',description:'").append(task.getDescription())
	                .append("',dbid:'").append(task.getId()).append("'},");
	        }

	        if (!tasks.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");

	        response.getWriter().print(buff.toString());
	    }

	    public void takeTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String username = (String) request.getSession()
	                                          .getAttribute("currentUsername");
	        jbpmTemplate.takeTask(dbid, username);
	    }

	    public void cancelTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        jbpmTemplate.cancelTask(dbid);
	    }

	    public void releaseTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        jbpmTemplate.releaseTask(dbid);
	    }

	    public void users(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        List<User> users = jbpmTemplate.getUsers();

	        StringBuffer buff = new StringBuffer();
	        buff.append("{result:[");

	        for (User user : users) {
	            buff.append("{id:'").append(user.getId())
	                .append("',givenName:'").append(user.getGivenName())
	                .append("',familyName:'").append(user.getFamilyName())
	                .append("',groups:'");

	            List<Group> groups = jbpmTemplate.getGroupsByUser(user.getId());

	            for (Group group : groups) {
	                buff.append("Group(").append(group.getType()).append("): ")
	                    .append(group.getName());
	                buff.append(",");
	            }

	            if (!groups.isEmpty()) {
	                buff.deleteCharAt(buff.length() - 1);
	            }

	            buff.append("'},");
	        }

	        if (!users.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void addUser(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        String password = request.getParameter("password");
	        String givenName = request.getParameter("givenName");
	        String familyName = request.getParameter("familyName");
	        jbpmTemplate.addUser(id, password, givenName, familyName);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeUser(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeUser(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void members(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String userId = request.getParameter("userId");

	        List<Group> groups = jbpmTemplate.getGroupsByUser(userId);
	        StringBuffer buff = new StringBuffer();
	        buff.append("{result:[");

	        for (Group group : groups) {
	            buff.append("{userId:'").append(userId).append("',groupId:'")
	                .append(group.getId()).append("',groupName:'")
	                .append(group.getName()).append("',groupType:'")
	                .append(group.getType()).append("'},");
	        }

	        if (!groups.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void addMember(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        String userId = request.getParameter("userId");
	        String groupId = request.getParameter("groupId");
	        String role = request.getParameter("role");
	        jbpmTemplate.addMember(userId, groupId, role);
	        response.getWriter().print("{success:true}");
	    }

	    @SuppressWarnings("unused")
		public void removeMember(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        response.getWriter().print("{success:true}");
	    }

	    public void groups(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        List<Group> groups = jbpmTemplate.getGroups();

	        StringBuffer buff = new StringBuffer();
	        buff.append("{result:[");

	        for (Group group : groups) {
	            buff.append("{id:'").append(group.getId()).append("',name:'")
	                .append(group.getName()).append("',type:'")
	                .append(group.getType()).append("'},");
	        }

	        if (!groups.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void addGroup(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();

	        String name = request.getParameter("name");
	        String type = request.getParameter("type");
	        String parentGroupId = request.getParameter("parentGroupId");
	        jbpmTemplate.addGroup(name, type, parentGroupId);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeGroup(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeGroup(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void isLogin(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        HttpSession session = request.getSession();
	        boolean isLogin = session.getAttribute("currentUsername") != null;
	        response.getWriter().print("{success:" + isLogin + "}");
	    }

	    public void login(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        if (jbpmTemplate.checkLogin(username, password)) {
	            HttpSession session = request.getSession();
	            session.setAttribute("currentUsername", username);
	            response.getWriter().print("{success:true}");
	        } else {
	            response.getWriter().print("{success:false}");
	        }
	    }

	    public void logout(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        HttpSession session = request.getSession();
	        session.invalidate();
	        response.getWriter().print("{success:true}");
	    }

	    public void processDetail(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        /*JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        ProcessDefinition processDefinition = jbpmTemplate
	            .getProcessDefinitionByProcessInstanceId(id);
	        ActivityCoordinates ac = jbpmTemplate.getActivityCoordinates(id);
	        StringBuffer buff = new StringBuffer(
	                "<script type='text/javascript' src='scripts/replay.js'></script>"
	                + "<img style='position:absolute;left:0px;top:0px;' src='JpdlImage?id=");
	        buff.append(processDefinition.getId())
	            .append("'><div style='position:absolute;left:")
	            .append(ac.getX()).append("px;top:").append(ac.getY())
	            .append("px;width:").append(ac.getWidth()).append("px;height:")
	            .append(ac.getHeight())
	            .append("px;border:1px solid red;background-color:green;")
	            .append("filter:Alpha(opacity=50);opacity:0.5;'></div>");
	        buff.append("<script type='text/javascript'>")
	            .append("var array = [");

	        for (ActivityCoordinates acs : jbpmTemplate.getHistoryActivities(
	                id)) {
	            buff.append("{x:").append(acs.getX()).append(",y:")
	                .append(acs.getY()).append(",w:").append(acs.getWidth())
	                .append(",h:").append(acs.getHeight()).append("},");
	        }

	        if (buff.charAt(buff.length() - 1) == ',') {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("];").append("</script>");
	        response.getWriter().print(buff.toString());*/
	    }

	    public void reportOverallActivity(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String result = jbpmTemplate.reportOverallActivity();
	        response.getWriter().print(result);
	    }

	    public void reportProcessSummary(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String result = jbpmTemplate.reportOverallActivity();
	        response.getWriter().print(result);
	    }

	    public void reportMostActiveProcess(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String result = jbpmTemplate.reportMostActiveProcess();
	        response.getWriter().print(result);
	    }

	    public void historyActivities(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        String id = request.getParameter("id");
	        List<HistoryActivityInstance> list = jbpmTemplate
	            .getHistoryActivitiesByProcessInstanceId(id);

	        StringBuffer buff = new StringBuffer();
	        buff.append("{result:[");

	        for (HistoryActivityInstance hai : list) {
	            buff.append("{activityName:'").append(hai.getActivityName())
	                .append("',startTime:'").append(hai.getStartTime())
	                .append("',endTime:'").append(hai.getEndTime())
	                .append("',duration:'").append(hai.getDuration())
	                .append("'},");
	        }

	        if (!list.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    public void jobs(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
	        List<Job> list = jbpmTemplate.getJobs();

	        StringBuffer buff = new StringBuffer();
	        buff.append("{result:[");

	        for (Job job : list) {
	            buff.append("{dbid:'").append(job.getId())
	                .append("',lockOwner:'").append(job.getLockOwner())
	                .append("',dueDate:'").append(job.getDueDate())
	                .append("',exception:'").append(job.getException())
	                .append("',retries:'").append(job.getRetries())
	                .append("',exclusive:'").append(job.isExclusive())
	                .append("',lockExpirationTime:'")
	                .append(job.getLockExpirationTime()).append("'},");
	        }

	        if (!list.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }
	    
	    @RequestMapping
	    public void demo(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
		        String dbid = "100002";
			        JbpmTemplate jbpmTemplate = this.getJbpmTemplate();
			        jbpmTemplate.startProcess(dbid, null);
			        
			        response.getWriter().print("true");
	    }

}

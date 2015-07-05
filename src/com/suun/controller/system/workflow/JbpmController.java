package com.suun.controller.system.workflow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.api.job.Job;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.controller.BaseController;
import com.suun.publics.utils.Utils;
import com.suun.publics.workflow.JbpmTemplate;
import com.suun.publics.workflow.SuunTask;
import com.suun.service.system.workflow.WorkflowManager;

@SuppressWarnings("deprecation")
@Controller 
public class JbpmController extends BaseController{
	@Autowired
    WorkflowManager manager;
	/*private JbpmTemplate jbpmTemplate; 
	 

    private JbpmTemplate getJbpmTemplate() {
        if (jbpmTemplate == null) {
        	ApplicationContext ctx = WebApplicationContextUtils
               .getWebApplicationContext(JbpmTemplate.getServletContext());
        	jbpmTemplate = (JbpmTemplate) ctx.getBean("jbpmTemplate");
        }

        return jbpmTemplate;
    }*/
    
    @RequestMapping
    public void deployXml(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
        String xml = request.getParameter("xml");
        //xml="<process xmlns='http://jbpm.org/4.4/jpdl' name='zyfang'><start name='开始' g='235,91,48,48'><transition to='部门经理审批'/></start>" +
        	//	"<task g='213,195,90,50' name='部门经理审批' assignee='admin'><transition name='通过' to='结束'/><transition name='驳回' to='开始'/></task><end name='结束' g='235,337,48,48'/></process>";
       // xml="<process xmlns='http://jbpm.org/4.4/jpdl' name='zyfang'><start name='开始' g='235,91,48,48'><transition to='结束'/></start><end name='结束' g='235,337,48,48'/></process>";
        jbpmTemplate.deployXml(xml);
      
        response.getWriter().print("{success:true}");
    }
    
    @RequestMapping
    public ModelAndView editor(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    	ModelAndView modelandview=new ModelAndView("editor");
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
        String id = request.getParameter("objname");
        String xml=jbpmTemplate.getXmlResourceFromProcessDefinition(id);
        xml=xml.replaceAll("\n","");
        modelandview.getModel().put("xml", xml);
		return modelandview;
    }
    
    @RequestMapping
    public ModelAndView check(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    	ModelAndView modelandview=new ModelAndView("check");
        String id = request.getParameter("id");
        String dbid = request.getParameter("dbid");
        List<SuunTask> suuntasks=manager.taskHiss(id);
        
        Set<String> outgoes = JbpmTemplate.getJbpmTemplate().
        		   getTransitionsForTask(dbid);
        modelandview.getModel().put("objinput", manager.getWorkflow(id.substring(0, id.indexOf("."))));
        modelandview.getModel().put("operateid", id.substring(id.indexOf(".")+1));
        modelandview.getModel().put("history", suuntasks);
        modelandview.getModel().put("id", id);
        modelandview.getModel().put("dbid", dbid);
        modelandview.getModel().put("outgoes", outgoes);
		return modelandview;
    }

    //开始任务
    /*可以用单据编号"fymx-1111111" ObjName:"zyfang"
    @RequestMapping
    public void rocessInstance(String id,String ObjName) throws Exception {
		        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
		        jbpmTemplate.addProcessInstance(ObjName,id);
    }
    */
    //流程处理 request.getParameter("transition")"驳回";//通过
    @RequestMapping
    @ResponseBody
    public void selectTransition(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

        Map<String, Object> variables = new HashMap<String, Object>();
        String[] names = request.getParameterValues("names");
        String[] values = request.getParameterValues("values");

        if (names != null) {
            for (int i = 0; i < names.length; i++) {
                variables.put(names[i], values[i]);
            }
        }

        //String type = request.getParameter("type");
       // if ("taskComplete".equals(type)) {
            String dbid =  request.getParameter("dbid");//jbpmTemplate.getPersonalTasks("admin").get(0).getId();//request.getParameter("dbid");
            String t = request.getParameter("transition");
            String id = request.getParameter("id");
            jbpmTemplate.completeTask(dbid, t, variables);
            manager.writelog(id.substring(0, id.indexOf(".")),id.substring(id.indexOf(".")+1), t,true);
            //任务执行完成
            if (jbpmTemplate.getTasks(dbid)==null){
            	Class<?> clazz=Class.forName("com.suun.controller.developer.DemoController");            	
            	BaseCRUDController<?> d=(BaseCRUDController<?>)clazz.newInstance();
            	 
            	d.doChecked(manager,id.substring(id.indexOf(".")+1));
            }
        /*} else if ("processStart".equals(type)) {
            String id = request.getParameter("id");
            jbpmTemplate.startProcess(id, variables);
        } else if ("processSignal".equals(type)) {
            String id = request.getParameter("id");
            String t = request.getParameter("transition");
            jbpmTemplate.signalProcess(id, t, variables);
        }*/

        response.getWriter().print("{success:true}");
    }
    //id=objname+id
    @RequestMapping
    public void processDetail(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
        String id = request.getParameter("id");
        ProcessDefinition processDefinition = jbpmTemplate
            .getProcessDefinitionByProcessInstanceId(id);
        StringBuffer buff = new StringBuffer(
                "<script type='text/javascript' src='"+request.getContextPath()+"/resources/js/jquery.js'></script>"
                + "<img style='position:absolute;left:0px;top:0px;' src='"+request.getContextPath()+"/workflow/jbpm!getjbpmimage?id=")
                .append(processDefinition.getId()).append("'>")            ;
        Set<ActivityCoordinates> acsi = jbpmTemplate.getActivityCoordinates(id);
        for(ActivityCoordinates ac : acsi ) {	        
	        buff.append("<div style='position:absolute;left:")
	            .append(ac.getX()).append("px;top:").append(ac.getY())
	            .append("px;width:").append(ac.getWidth()).append("px;height:")
	            .append(ac.getHeight())
	            .append("px;border:1px solid red;")
	            .append("filter:Alpha(opacity=50);opacity:0.5;'></div>");
        }
        buff.append("<div id='his' style='display:none'/>")
        
            .append("<script type='text/javascript'>")
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

        buff.append("];")
            .append("var i=0;play();window.setInterval('play()',2000);")            
            .append("function play(){")
            .append("if (array.length>0){")	
            .append("l=array[i].x+1; t=array[i].y+1; var str='position:absolute;left:'+l+'px;top:'+t+'px;width:'+array[i].w+'px;height:'+array[i].h+'px;background-color:green;filter:Alpha(opacity=50);opacity:0.5;';")
            .append("$('#his').attr('style',str);")	
            .append("z=i+1;h=array[i].h-4;$('#his').html(\"<table width='100%' border='0'><tr><td style='font-size:14pt;font-weight:bold;color:white;vertical-align:middle;text-align:center;height:\"+h+\"px;'>\"+z+\"</td></tr></table>\");")	
            .append("if (i>=array.length-1) i=0; else i=i+1;")
            .append("}}</script>");
        response.getWriter().print(buff.toString());
    }
    
    @RequestMapping
	public void getjbpmimage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 部署ID
		String deploymentId = request.getParameter("id");// "zyfang-1";
		// 流程定义图名称
		// String resourceName = "process.png";
		// 获取流程定义图资源
		InputStream is = JbpmTemplate.getJbpmTemplate()
				.getPngResourceFromProcessDefinition(deploymentId);
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	    int ch;
	    while ((ch = is.read()) != -1) {
	       bytestream.write(ch);
	    }
	    byte imgdata[]=bytestream.toByteArray();
	    bytestream.close();
		  
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(imgdata);
		responseOutputStream.flush();
		responseOutputStream.close();

		return;
	}   
    //获取任务列表
    @RequestMapping
    @ResponseBody
    public Map<String, Object> tasks(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
 
        List<SuunTask> suuntasks=new ArrayList<SuunTask>();
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("result", suuntasks);
		return modelMap;
        
    }
    //获取登录用户任务列表
    @RequestMapping
    @ResponseBody
    public Map<String, Object> personalTasks(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        List<SuunTask> suuntasks=manager.getPersonalTasks();
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("result", suuntasks);
        return modelMap;
    }
    @RequestMapping
    @ResponseBody
    public void transition(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

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
    
    
    //获取登录用户组任务列表
    @RequestMapping
    @ResponseBody
    public void groupTasks(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
        List<Task> tasks = jbpmTemplate.getGroupTasks(Utils.getLoginUserId());
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
	
	@RequestMapping
	public ModelAndView processDefinitions(HttpServletRequest request,HttpServletResponse response) {	
		JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
        List<ProcessDefinition> processDefinitions = jbpmTemplate
            .getLatestProcessDefinitions();
        StringBuffer buff = new StringBuffer("{result:[");

        for (ProcessDefinition pd : processDefinitions) {
            buff.append("{id:'").append(pd.getId()).append("',name:'")
                .append(pd.getName()).append("',version:'")
                .append(pd.getVersion()).append("',dbid:'")
                .append(pd.getDeploymentId()).append("'},");
        }

        if (!processDefinitions.isEmpty()) {
            buff.deleteCharAt(buff.length() - 1);
        }

        buff.append("]}");
        try {
			response.getWriter().print(buff.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		
	public void suspendedProcessDefinitions(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        List<ProcessDefinition> processDefinitions = jbpmTemplate
	            .getSuspendedProcessDefinitions();
	        StringBuffer buff = new StringBuffer("{result:[");

	        for (ProcessDefinition pd : processDefinitions) {
	            buff.append("{id:'").append(pd.getId()).append("',name:'")
	                .append(pd.getName()).append("',version:'")
	                .append(pd.getVersion()).append("',dbid:'")
	                .append(pd.getDeploymentId()).append("'},");
	        }

	        if (!processDefinitions.isEmpty()) {
	            buff.deleteCharAt(buff.length() - 1);
	        }

	        buff.append("]}");
	        response.getWriter().print(buff.toString());
	    }

	    @SuppressWarnings({ "unused", "rawtypes" })
		public void deploy(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

	        String temp = getServletContext().getRealPath("/temp");
	        String uploadDir = getServletContext().getRealPath("/upload");
	        DiskFileUpload diskFileUpload = new DiskFileUpload();
	        diskFileUpload.setSizeMax(1 * 1024 * 1024);
	        diskFileUpload.setSizeThreshold(4096);
	        diskFileUpload.setRepositoryPath(temp);

	        List fileItems = diskFileUpload.parseRequest(request);
	        Iterator iter = fileItems.iterator();
	        System.out.println("size: " + fileItems.size());
	        System.out.println("boolean: " + iter.hasNext());

	        if (iter.hasNext()) {
	            FileItem item = (FileItem) iter.next();

	            if (!item.isFormField()) {
	                String name = item.getName();
	                long size = item.getSize();
	                System.out.println("filename: " + name);

	                String type = request.getParameter("type");

	                if ((name != null) && !name.equals("") && (size != 0)) {
	                    if (type.equals("xml")) {
	                        jbpmTemplate.deployXml(name, item.getInputStream());
	                    } else if (type.equals("zip")) {
	                        jbpmTemplate.deployZip(item.getInputStream());
	                    }
	                }
	            }
	        }

	        response.setContentType("text/html");
	        response.getWriter().print("{success:true}");
	    }

	    public void suspendProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.suspendProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void resumeProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.resumeProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeProcessDefinition(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeProcessDefinitionById(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void transitions(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

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
	    

	    public void processInstances(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

	        String id = request.getParameter("id");
	        Set<ActivityCoordinates> acs = jbpmTemplate.getActivityCoordinates(id);
	        for(ActivityCoordinates ac : acs ) {
	            response.getWriter()
	                .print("{x:" + ac.getX() + ",y:" + ac.getY() + ",w:"
	                + ac.getWidth() + ",h:" + ac.getHeight() + "}");
	        }
	    }

	    public void suspendProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.suspendProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void resumeProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.resumeProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void endProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.endProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeProcessInstance(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeProcessInstance(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void variables(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	    

	    public void takeTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String username = (String) request.getSession()
	                                          .getAttribute("currentUsername");
	        jbpmTemplate.takeTask(dbid, username);
	    }

	    public void cancelTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        jbpmTemplate.cancelTask(dbid);
	    }

	    public void releaseTask(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        String dbid = request.getParameter("id");
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        jbpmTemplate.releaseTask(dbid);
	    }

	    public void users(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        String password = request.getParameter("password");
	        String givenName = request.getParameter("givenName");
	        String familyName = request.getParameter("familyName");
	        jbpmTemplate.addUser(id, password, givenName, familyName);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeUser(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String id = request.getParameter("id");
	        jbpmTemplate.removeUser(id);
	        response.getWriter().print("{success:true}");
	    }

	    public void members(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

	        String userId = request.getParameter("userId");
	        String groupId = request.getParameter("groupId");
	        String role = request.getParameter("role");
	        jbpmTemplate.addMember(userId, groupId, role);
	        response.getWriter().print("{success:true}");
	    }

	    @SuppressWarnings("unused")
		public void removeMember(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        response.getWriter().print("{success:true}");
	    }

	    public void groups(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();

	        String name = request.getParameter("name");
	        String type = request.getParameter("type");
	        String parentGroupId = request.getParameter("parentGroupId");
	        jbpmTemplate.addGroup(name, type, parentGroupId);
	        response.getWriter().print("{success:true}");
	    }

	    public void removeGroup(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	    

	    public void reportOverallActivity(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String result = jbpmTemplate.reportOverallActivity();
	        response.getWriter().print(result);
	    }

	    public void reportProcessSummary(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String result = jbpmTemplate.reportOverallActivity();
	        response.getWriter().print(result);
	    }

	    public void reportMostActiveProcess(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
	        String result = jbpmTemplate.reportMostActiveProcess();
	        response.getWriter().print(result);
	    }

	    public void historyActivities(HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	        JbpmTemplate jbpmTemplate = JbpmTemplate.getJbpmTemplate();
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
	    

}

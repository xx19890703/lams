package com.suun.controller.system.workflow;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suun.model.system.workflow.Workflow;
import com.suun.publics.controller.BaseController;
import com.suun.publics.utils.TreeObj;
import com.suun.service.system.workflow.WorkflowManager;


@Controller 
public class WorkflowController extends BaseController {
	
	@Autowired
	WorkflowManager manager;
	
	@RequestMapping
	@ResponseBody
	public List<Object> lists (HttpServletRequest request) throws Exception {
		List<Object> model = new ArrayList<Object>();
		List<Workflow> list =manager.getAllFlow();
		for(Workflow t:list){
			TreeObj obj= new TreeObj();
			obj.setId(t.getObjName());
			obj.setText(t.getObjDescription());
			obj.setParentId("0");
			obj.setLeaf(true);
			obj.setIcon(request.getContextPath()+ "/resources/images/nouse/workflow.png");
			model.add(obj);
		}		
		return model;
	}
    
    //以下为旧的workflowediter的支持方法
    public class FileBean {

    	private String fileName;

    	private String filePath;
    	/**
    	 * @return the filePath
    	 */
    	public String getFilePath() {
    		return filePath;
    	}

    	/**
    	 * @param filePath the filePath to set
    	 */
    	public void setFilePath(String filePath) {
    		this.filePath = filePath;
    	}

    	/**
    	 * @return the fileName
    	 */
    	public String getFileName() {
    		return fileName;
    	}

    	/**
    	 * @param fileName the fileName to set
    	 */
    	public void setFileName(String fileName) {
    		this.fileName = fileName;
    	}   	
    	
    }
 // 得到ClassPath的URL
    private static URL getURLOfClassPath() {
        return WorkflowController.class.getResource("");
    }

    // 得到WebRoot目录的绝对地址
    private static String getAbsolutePathOfWebRoot() {
        String result = null;
        result = getAbsolutePathOfClassPath();
        result = result.substring(0, result.indexOf("WEB-INF"));
        return result;
    }

    // 得到ClassPath的绝对路径
    private static String getAbsolutePathOfClassPath() {
        String result = null;
        try {
            File file = new File(getURLOfClassPath().toURI());
            result = file.getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String getAbsolutePath(String relativePath) {
        String result = null;
        if (null != relativePath) {
            if (relativePath.indexOf("./") == 0) {
                String workspacePath = new File("").getAbsolutePath();
                relativePath = relativePath.substring(2);
                if (relativePath.length() > 0) {
                    relativePath = relativePath
                            .replace('/', File.separatorChar);
                    result = workspacePath + String.valueOf(File.separatorChar)
                            + relativePath;
                } else {
                    result = workspacePath;
                }
            } else if (relativePath.indexOf("/") == 0) {
                String webRootPath = getAbsolutePathOfWebRoot();
                if (relativePath.length() > 0) {
                    relativePath = relativePath
                            .replace('/', File.separatorChar);
                    result = webRootPath + relativePath;
                } else {
                    result = webRootPath;
                }
            } else {
                String classPath = getAbsolutePathOfClassPath();
                if (relativePath.length() > 0) {
                    relativePath = relativePath
                            .replace('/', File.separatorChar);
                    result = classPath + File.separatorChar + relativePath;
                } else {
                    result = classPath;
                }
            }
        }
        return result;
    }
    @RequestMapping
	@ResponseBody
	public Map<String,Object> findflowfiles(HttpServletRequest request) throws Exception{
		String path=getAbsolutePath("/WEB-INF/pages/system/flowjquery/formpages/");
		File file=new File(path);
		File[] files=file.listFiles();
		Map<String, Object> map = new HashMap<String, Object>();
		List<FileBean> fbs=new ArrayList<FileBean>();
		for(File f:files){
				FileBean fb=new FileBean();
				fb.setFileName(f.getName());
				fb.setFilePath(f.getName());
				fbs.add(fb);
		}
		map.put("data", fbs);
		return map;
	}

}

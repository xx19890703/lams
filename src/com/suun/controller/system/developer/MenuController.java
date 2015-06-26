package com.suun.controller.system.developer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.suun.model.system.developer.Menu;
import com.suun.model.system.developer.Function;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.Utils;
import com.suun.service.system.developer.MenuManager;

@Controller 
public class MenuController extends TreeGridCRUDController<Menu,Function>{
	@Autowired
	MenuManager menuManager;	
	//以下menu操作
	@Override
	public String getTreeId(Menu t) {
		return t.getMenuId().toString();
	}
	@Override
	public String getTreeName(Menu t) {
		return t.getMenuName();
	}
	@Override
	public String getTreePid(Menu t) {
		return t.getMenuParid().toString();
	}
	@Override
	public  int getTreeLeaf(Menu t){
		return t.getMenuType()==3?1:0;
	}
	/**
	 * 图片的路径要根据页面所在的位置进行配置，数据库中的是/……
	 **/
	@Override
	public String getTreeIcon(HttpServletRequest request, Menu t) {	
		if (t.getMenuImg()==null){
			return "/resources/images/system/detail.gif";
		} else
			return t.getMenuImg();
	}
	/**
	 * 同一节点下，菜单名不能相同
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public String validateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("menuName");
		String oldname = request.getParameter("oldname");
		String pid = request.getParameter("id");
		
		if(menuManager.isMenuNameUnique(pid, name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}	
	
	@Override
	public List<Menu> getTreeAllRecords(String Pid) {
		return menuManager.findMenusByParentId(Pid);
	}

	@Override
	public Menu getTreeRecordSet(String operateid) {
		return menuManager.getMenu(operateid);
	}
	
	@Override
	public String treeSaveRecordSet(Menu operatebean) {
		try{
			menuManager.saveMenu(operatebean);
			return "";
		}catch(Exception e){
			return e.getMessage();
		}
	}	

	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
			for(String id:ids){
				menuManager.deleteMenu(id);
			}
			return "";
		}catch(Exception e){
			return e.getMessage();
		}		
	}

	
	//以下Function操作
	/**
	 * 授权不能相同
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public String validateFunId(HttpServletRequest request,HttpServletResponse response) {	
		String funId = request.getParameter("funId");
		String oldFunId = request.getParameter("oldid");
		
		if(menuManager.isFunIdUnique(funId, oldFunId)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	
	@Override
	protected Function getGridRecordSet(HttpServletRequest request, String treeid,
			String operateid) {
		return menuManager.getFunction(operateid);
	}
	
	@Override
	protected List<Function> getGridAllRecords(HttpServletRequest request,
			String treeid, Condition condition) {		
		return menuManager.getAllFuns(treeid);
	}	
	
	@Override
	protected Page<Function> getGridPageRecords(HttpServletRequest request,
			String treeid, Page<Function> page) {
		return menuManager.getPageFuns(treeid, page);		
	}
	
	@Override
	protected String saveGridRecordSet(HttpServletRequest request,
			Function operatebean) {
		return menuManager.saveFunction(operatebean);
	}
	
	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,
			String treeid, String[] ids) {
		try{
			for(String id:ids){
				menuManager.deleteFun(id);
			}
			return "";
		}catch(Exception e){
			return e.getMessage();
		}
	}


	/*
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> toplists(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Menu> tops=menuManager.findMenusByParentId("0");
		modelMap.put("page", 1);
		modelMap.put("curpagepos", 0);
		modelMap.put("total", tops.size()); 
		//modelMap.put("filter", page.getCondition().getFilterInfos());
		modelMap.put("data", tops);
		
		return modelMap;
	}
	
	@RequestMapping
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response,Menu operatebean) {
		ModelAndView modelandview=new ModelAndView("input");
		request.getSession().setAttribute("OperateState", "New");
		return modelandview;	
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request,
			HttpServletResponse response,Menu operatebean) {
	//	boolean isNew=request.getSession().getAttribute("OperateState").equals("New");		
		request.getSession().setAttribute("entity", operatebean);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try{
        	menuManager.saveMenu(operatebean);
        	modelMap.put("success", "true");
        } catch (Exception e){
        	modelMap.put("success", "false");
            modelMap.put("error", e.getMessage());
        }     	       
		return modelMap;	
	}	
	*/
	
	//以下文件操作
	
	// 查找当前目录下的文件
		@RequestMapping
		@ResponseBody
		public String listFile(HttpServletResponse response){		
			String path=Utils.getAbsolutePath("/resources/images/menu/");
			File file=new File(path);
			File[] files=file.listFiles();
			String filenames="[";
			for(File f:files){
				filenames=filenames.concat("{filepath:'").concat(f.getName()).concat("'},");
			}
			if (filenames.length()>1){
				filenames=filenames.substring(0, filenames.length()-1);
			}
			filenames=filenames.concat("]");	
			return renderText(response,"{'data':".concat(filenames).concat("}"));
		}
		
		//删除文件
		@RequestMapping
		@ResponseBody
		public Map<String, Object> delFile(HttpServletRequest request) {
			String files = request.getParameter("fileName");
			String[] fileNames = files.split(",");
			for (int i = 0; i < fileNames.length; i++) {
				String path = Utils.getAbsolutePath("/resources/images/menu/"
						+ fileNames[i]);
				File file = new File(path);
				file.delete();
			}
			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("msg", "删除成功!");
			return modelMap;
		}
		//获取到上传的文件 /
		private Set<MultipartFile> getFileSet(HttpServletRequest request) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Set<MultipartFile> fileset = new LinkedHashSet<MultipartFile>();
			for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
				String key = (String) it.next();
				MultipartFile file = multipartRequest.getFile(key);
				if (file.getOriginalFilename().length() > 0) {
					fileset.add(file);
				}
			}
			return fileset;
		}

		// 保存文件/
		private void SaveFileFromInputStream(InputStream stream, String path,
				String filename) throws IOException {
			FileOutputStream fs = new FileOutputStream(path + "/" + filename);
			byte[] buffer = new byte[1024 * 1024];
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
			fs.close();
			stream.close();
		}

		//上传文件
		@RequestMapping
		@ResponseBody
		public String upload(HttpServletRequest request,HttpServletResponse response) {
			//String newfilename = request.getParameter("filename");
			Set<MultipartFile> mfs = getFileSet(request);		
			try {
				for (MultipartFile mf : mfs) {
					SaveFileFromInputStream(mf.getInputStream(),
							Utils.getAbsolutePath("/resources/images/menu") ,
							mf.getOriginalFilename());
				}
				return "{\"success\":true,\"msg\":\"图片上传成功！\"}";
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"success\":true,\"msg\":\"图片上传失败！\"}";			
			}
			//modelMap.put("msg", "删除成功!");和 renderText(response,  getForm().submit 都不触发 success
		}
	
	/*
	//下载文件

	@SuppressWarnings("unused")
	@RequestMapping  
    public void downvffloadFile(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        String fileName=request.getParameter("fileName");
		response.setCharacterEncoding("utf-8");  
        response.setContentType("multipart/form-data");  
        String vmlName=new String(fileName.getBytes(),"utf-8");
        String vmlName2=new String(fileName.getBytes("utf-8"),"gbk");
        response.setHeader("Content-Disposition", "attachment;fileName="+fileName);  
        try {  
            File file=new File(fileName);  
            System.out.println(file.getAbsolutePath());  
            InputStream inputStream=new FileInputStream("file/"+file);  
            OutputStream os=response.getOutputStream();  
            byte[] b=new byte[1024];  
            int length;  
            while((length=inputStream.read(b))>0){  
                os.write(b,0,length);  
            }  
            inputStream.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	

	// 获取文件目录树
	@RequestMapping
	@ResponseBody	
	public  List<FileTreeBean> fileDic(HttpServletRequest request){
		String node=request.getParameter("node");
		String path=FilePathUtil.getAbsolutePath("/resources/")+node;
		File file=new File(path);
		File[] files=file.listFiles();
		
		List<FileTreeBean> lists = new ArrayList<FileTreeBean>();
		FileTreeBean filebean=null;
		for(File f:files){
			if(f.isDirectory()){
			filebean=new FileTreeBean();
				filebean.setId(node+"/"+f.getName());
				filebean.setText(f.getName());
				filebean.setLeaf(false);
			}
			lists.add(filebean);
		}
		return lists;
	}
	// 新增文加件

	@RequestMapping
	@ResponseBody
	public Map<String,Object> addFileDic(HttpServletRequest request){
		String parentId=request.getParameter("parentId");
		String name=request.getParameter("fileName");
		String path=FilePathUtil.getAbsolutePath("/resources/")+parentId+"/"+name;
		File file=new File(path);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try{
			if(file.isDirectory()){
				modelMap.put("msg", "该目录已经存在");
			}else{
				file.mkdir();
				modelMap.put("msg", "目录创建成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			modelMap.put("msg", "创建目录失败");
			return modelMap;
		}
		return modelMap;
	}
	//删除目录
	@RequestMapping
	@ResponseBody
	public Map<String,Object> delFileDic(HttpServletRequest request){
		String parentId=request.getParameter("parentId");
		String path=FilePathUtil.getAbsolutePath("/resources/")+parentId;
		Map<String, Object> modelMap = new HashMap<String, Object>();
		File file=new File(path);
		boolean flag=file.delete();
		if(flag){
			modelMap.put("msg", "删除成功");
		}else{
			modelMap.put("msg", "删除失败");
		}
		return modelMap;
	}
	*/

	
	
}
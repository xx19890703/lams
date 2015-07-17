package com.suun.controller.serviceuser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.fr.base.StringUtils;
import com.suun.model.contract.Contract_mode;
import com.suun.model.contract.Contract_template;
import com.suun.model.serviceuser.ContractCategory;
import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.system.Constant;
import com.suun.model.system.developer.Menu;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.data.DataminingFactory;
import com.suun.publics.data.DataminingStrategy;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.utils.ReadFile;
import com.suun.service.data.DataminingManager;
import com.suun.service.serviceuser.ContractCategoryManager;
import com.suun.service.serviceuser.ContractDetailManager;
import com.suun.service.system.DicManager;
import com.suun.service.system.developer.MenuManager;

/**
 * ContractCategoryController
 * @author renlq
 *
 */

@Controller 
public class ContractCategoryController extends TreeGridCRUDController<ContractCategory,ContractDetail>{

	@Autowired
	ContractCategoryManager mainManager;
	@Autowired
	ContractDetailManager subManager;
	@Autowired
	DataminingManager dataminingManager;
	@Autowired 
	DicManager dicManager;
	@Autowired
	private DataminingFactory factory;
	@Autowired
	MenuManager menuManager;	
	
	@RequestMapping
	@ResponseBody
	public String validateDid(HttpServletRequest request,HttpServletResponse response) {	
		String did = request.getParameter("did");
		String olddid = request.getParameter("olddid");		
		if(mainManager.isContractCategoryNameUnique("", did, olddid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getTest(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("renlq", "test");
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public String validateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		String pid = request.getParameter("id");
		
		if(mainManager.isContractCategoryNameUnique(pid, name, oldname)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	public String getContractCategory(HttpServletRequest request,HttpServletResponse response) {
//		Condition condition=new Condition();
//		condition.setFilterInfos(new ArrayList<FilterInfo>());
//		List<TemplateResDetail> res = manager.getAllTemplateRes(request.getParameter("pid"),condition);
//		String existids="";
//		for (TemplateResDetail temp : res){
//			existids = temp.getEmployee().getEmployeeid()+"@"+existids;
//		}
//		return this.renderText(response, existids);		
		return "";
	}
	
	@Override
	public Map<String,Object> doTreeNewbefore() {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	@Override
	public Map<String,Object> doTreeEditbefore() {
		return doTreeNewbefore();
	}
	@Override
	public Map<String,Object> doGridNewbefore(HttpServletRequest request,String treeid) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	
	@Override
	public Map<String,Object> doGridEditbefore(HttpServletRequest request,String treeid,ContractDetail operatebean) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
			Boolean flag=null;
            for (int i=0;i<ids.length;i++){
            	List<ContractCategory> orglist=mainManager.findContractCategoryByParentId(ids[i]);
            	//判断节点是否是父节点，长度为0表示不是父节点可以删除，反之就是父节点不可删除。
            	if(orglist.size()==0){
            		mainManager.deleteContractCategory(ids[i]);
            		flag= true;
            	}else{
            		flag= false;
            	}
		    }
            if (flag){
            	return "";
            }else{
            	return "父节点不可删除!";
            }
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String treeSaveRecordSet(ContractCategory operatebean) {
		try{			
			mainManager.saveContractCategory(operatebean);
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public List<ContractCategory> getTreeAllRecords(String Pid) {
		return mainManager.findContractCategoryByParentId(Pid);
	}

	@Override
	public ContractCategory getTreeRecordSet(String operateid) {
		return mainManager.getContractCategory(operateid);
	}

	@Override
	public String getTreeId(ContractCategory t) {
		return t.getDid().toString();
	}

	@Override
	public String getTreeName(ContractCategory t) {
		return t.getName();
	}

	@Override
	public String getTreePid(ContractCategory t) {
		return t.getPid();
	}

	@Override
	public String getTreeIcon(HttpServletRequest request, ContractCategory t) {
		return "/resources/images/system/group.png";
	}

	@Override
	public int getTreeLeaf(ContractCategory t) {
		return 0;
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,String treeid, String[] ids) {
		try{
        	for (int i=0;i<ids.length;i++){			
        		mainManager.deleteContractDetail(treeid,ids[i]);//传入的是employee id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,ContractDetail operatebean) {
		try{
			mainManager.saveContractDetail(operatebean);
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected Page<ContractDetail> getGridPageRecords(HttpServletRequest request, String treeid,Page<ContractDetail> page) {
		return mainManager.getAllContractDetail(treeid, page);
	}

	@Override
	protected List<ContractDetail> getGridAllRecords(HttpServletRequest request, String treeid, Condition condition) {
		return mainManager.findContractDetailByParentId(treeid);
	}

	@Override
	protected ContractDetail getGridRecordSet(HttpServletRequest request,String treeid, String operateid) {
		return mainManager.getContractDetail(treeid, operateid);
	}
	
	@RequestMapping
	public ModelAndView getGridAllRecords() {	
		ModelAndView mav = new ModelAndView();
		mav.addObject(mainManager.getAllContractCategory("01",null));
		return mav;
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, Object> findContract(HttpServletRequest request,HttpServletResponse response) {	
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<String> data=mainManager.findContract();
		modelMap.put("success", "true"); 
		modelMap.put("list", data);
		return modelMap;	
	}
	
	@RequestMapping
	@ResponseBody
	public void griddown(String treeid, String id, HttpServletRequest request, HttpServletResponse response){
		response.reset();
		//获取路径
		String reportPath = FRContext.getCurrentEnv().getPath();
		
		//创建临时文件夹
		String envPath = request.getSession().getServletContext().getRealPath(File.separator + "tempfile");
		File filePaths = new File(envPath);
		if(!filePaths.exists() && !filePaths.isDirectory()){
			filePaths.mkdir();    
		} 
		
		ZipOutputStream zos = null;
		String sbuffer = new String();
		try {
	        ContractDetail contract = mainManager.getContractDetail(treeid, id);
	        String tempPath = envPath + File.separator +"down_" + id + "_" + System.currentTimeMillis() + ".zip";
	        zos = new ZipOutputStream(new FileOutputStream(tempPath));
	        DataminingStrategy strategy = factory.getStrategy();
	        int readLength = 0;
	        String filePath = "";
	        
	        //返回合同-数据库表的数据
	        for(String temp : strategy.findTableData(contract.getDid(),"contract_mode"))
	        	sbuffer = sbuffer + temp;
	        //返回合同-模板url的数据
	        for(String temp : strategy.findTableData(contract.getDid(),"contract_template"))
	        	sbuffer = sbuffer + temp;
	        
	        //遍历所有模板
	        for(ContractTemplateRes s : contract.getRescontent()){
	        	TemplateResDetail temple = s.getTemplate();
	        	//添加模板到zip包
	        	filePath = reportPath + "reportlets" + File.separator + temple.getPath();
	        	System.out.println(filePath);
	        	File tfile = new File(filePath);  
    			if (!tfile.exists()) {
    				continue;
    			}
	        	ZipEntry tentry = new ZipEntry(temple.getPath());
        		zos.putNextEntry(tentry);
        		InputStream tis = new BufferedInputStream(new FileInputStream(tfile));
    			while ((readLength = tis.read()) != -1) {
    				zos.write(readLength);
    			}
    			tis.close();
    			
	        	//遍历所有数据库表
	        	for(TemplateResContent tt : temple.getRescontent()){
	        		//sbuffer = sbuffer + sql.getDataSqlByContractId(contract.getDid(),tt.getName());
	        		//返回表数据查询语句
	        		for(String temp : strategy.findTableData(contract.getDid(), tt.getName()))
	        			sbuffer = sbuffer + temp;
	        		filePath = reportPath + tt.getCsqlpath();
	        		File file = new File(filePath);  
	    			if (!file.exists()) {
	    				continue;
	    			}
	    			ZipEntry entry = new ZipEntry(tt.getCsqlpath());
	        		zos.putNextEntry(entry);
	        		InputStream is = new BufferedInputStream(new FileInputStream(file));
	    			while ((readLength = is.read()) != -1) {
	    				zos.write(readLength);
	    			}
	    			is.close();
	        	}
	        }
	        //创建insert.sql(报表数据文件)文件
	        if (sbuffer != null && !sbuffer.trim().equals("")){
	        	ZipEntry entry = new ZipEntry("insert.sql");
	        	zos.putNextEntry(entry);
	        	ByteArrayInputStream in = new ByteArrayInputStream(sbuffer.getBytes());
				while ((readLength = in.read()) != -1) {
					zos.write(readLength);
				}
				in.close();
	        }
	        
	        //创建合同id.txt文件
	        if (id != null && !id.trim().equals("")){
	        	ZipEntry entry = new ZipEntry(id + ".txt");
	        	zos.putNextEntry(entry);
	        	ByteArrayInputStream in = new ByteArrayInputStream(id.getBytes());
				while ((readLength = in.read()) != -1) {
					zos.write(readLength);
				}
				in.close();
	        }
	        zos.close();
	        
	        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
	        InputStream input = new FileInputStream(tempPath);
	        byte[] buffer = new byte[4096];
	        int n = 0;
	        while ((n = input.read(buffer)) != -1) {
	        	outputStream.write(buffer, 0, n);
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + id + "_" + System.currentTimeMillis() + ".zip\"");  
	        response.setHeader("Content-Length",String.valueOf(input.read()));
	        response.setContentType("application/octet-stream; charset=utf-8");
	        input.close();
	        outputStream.flush();  
	        outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (Exception ex) {
					System.out.println("zip流关闭错误:" + ex.toString());
				}
			}
		}  
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> gridupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		String contractId = null;
		List<String> sqls = new ArrayList<String>();
		if (!file.isEmpty()){
			//文件上传路径
			String path = request.getSession().getServletContext().getRealPath(File.separator + "tempfile" + File.separator + "upload_" + file.getOriginalFilename());
			File destFile = new File(path);
			try {
				String envPath = FRContext.getCurrentEnv().getPath();
				FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
				ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
				BufferedInputStream bin=new BufferedInputStream(zip);
				ZipEntry entry;
				//逐个读取压缩包的文件
				while((entry = zip.getNextEntry())!=null && !entry.isDirectory()){
					//处理以.cpt结尾的文件  及 .sql结尾的文件
					String fileName = entry.getName();
					if(fileName.endsWith(".cpt")){
						destFile=new File(envPath,entry.getName());
						if(!destFile.exists()){
							(new File(destFile.getParent())).mkdirs();
						}
						FileOutputStream out=new FileOutputStream(destFile);
						BufferedOutputStream Bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							Bout.write(b);
						}
						Bout.close();
						out.close();
					}else if(fileName.endsWith(".txt")){
						String[] names = fileName.split("\\.");
						contractId = names[0];
						System.out.println("******************contractId:----"+contractId);
					}else if(fileName.endsWith(".sql")){
						destFile=new File(envPath,entry.getName());
						if(!destFile.exists()){
							(new File(destFile.getParent())).mkdirs();
						}
						FileOutputStream out=new FileOutputStream(destFile);
						BufferedOutputStream Bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							Bout.write(b);
						}
						Bout.close();
						out.close();
						
						String sql = ReadFile.readFileByChars(destFile.getAbsolutePath());
						sqls.add(sql);
						System.out.println(sql);
					}
				}  
				bin.close();  
				zip.close();
				map.put("success", true);
				map.put("msg", "上传成功!");
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", "解析上传文件出错!");
			}
		}else{
			map.put("success", false);
			map.put("msg", "上传文件为空!");
		}
		
		//判断该 contractId 是否已经存在
		List<Contract_mode> modeLists = mainManager.findContract_modeByContractId(contractId);
		if(modeLists.size()!=0){
			//如果存在 需要先清除数据
			for(Contract_mode cm :modeLists){
				dataminingManager.deleteData(contractId, cm.getId().getTableName());
			}
			dataminingManager.deleteData(contractId, "contract_mode");
			dataminingManager.deleteData(contractId, "contract_template");
			menuManager.deleteMenuByName(contractId);
		}
		for(String sql:sqls){
			dataminingManager.insertData(sql);
		}
		
		
		List<Contract_template> list = mainManager.findContract_templateByContractId(contractId);
		//根据一个上传的模板文件，查询 合同id
			
		if(contractId!=null){
			//合同id为父 菜单
			String pid =""+System.currentTimeMillis();
			Menu mm = new Menu();
			mm.setMenuId(pid);
			mm.setMenuName(contractId);
			mm.setMenuType(2);
			mm.setMenuImg("/resources/images/system/group.png");
			mm.setMenuParid("0102");
			mm.setItemOrder(999);
			mm.setIsadmin(1);
			mm.setIsframe(0);
			menuManager.saveMenu(mm);
			
			for(Contract_template cts:list){
				Menu m = new Menu();
				m.setMenuId(""+System.currentTimeMillis());
				m.setMenuName(cts.getTemplateName());
				m.setMenuType(3);
				m.setMenuImg("/resources/images/system/group.png");
				m.setMenuParid(pid);
				m.setItemOrder(999);
				//判断报表是否有打开参数
				if(null==cts.getOpenType() || "".endsWith(cts.getOpenType())){
					m.setMenuUrl("/ReportServer?reportlet="+cts.getId().getTemplateUrl().replaceAll("\\\\", "/"));
				}else{
					m.setMenuUrl("/ReportServer?reportlet="+cts.getId().getTemplateUrl().replaceAll("\\\\", "/")+"&op="+cts.getOpenType());
				}
				m.setIsadmin(1);
				m.setIsframe(1);
				menuManager.saveMenu(m);
			}
		}
		return map;
	}
	
	/**
	 * 服务端解析客户端报表数据
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> serverupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		String contractId = null;
		List<String> sqls = new ArrayList<String>();
		if (!file.isEmpty()){
			//文件上传路径
			String path = request.getSession().getServletContext().getRealPath(File.separator + "tempfile" + File.separator + "server_" + file.getOriginalFilename());
			File destFile = new File(path);
			try {
				String envPath = FRContext.getCurrentEnv().getPath();
				FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
				ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
				BufferedInputStream bin=new BufferedInputStream(zip);
				ZipEntry entry;
				//逐个读取压缩包的文件
				while((entry = zip.getNextEntry())!=null && !entry.isDirectory()){
					//处理以.cpt结尾的文件  及 .sql结尾的文件
					String fileName = entry.getName();
					if(fileName.endsWith(".txt")){
						String[] names = fileName.split("\\.");
						contractId = names[0];
					}else if(fileName.endsWith(".sql")){
						destFile=new File(envPath,entry.getName());
						if(!destFile.exists()){
							(new File(destFile.getParent())).mkdirs();
						}
						FileOutputStream out=new FileOutputStream(destFile);
						BufferedOutputStream Bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							Bout.write(b);
						}
						Bout.close();
						out.close();
						
						String sql = ReadFile.readFileByChars(destFile.getAbsolutePath());
						sqls.add(sql);
						System.out.println(sql);
					}
				}  
				bin.close();  
				zip.close();
				
				//判断该 contractId 是否已经存在
				if(contractId != null){
					List<Contract_mode> modeLists = mainManager.findContract_modeByContractId(contractId);
					if(modeLists.size()!=0){
						//如果存在 需要先清除数据
						for(Contract_mode cm :modeLists){
							dataminingManager.deleteData(contractId, cm.getId().getTableName());
						}
					}
					for(String sql:sqls){
						dataminingManager.insertData(sql);
					}
					map.put("success", true);
					map.put("msg", "上传成功!");
				}else{
					map.put("success", false);
					map.put("msg", "上传文件中未找到合同id!");
				}
			} catch (IOException e) {
				map.put("success", false);
				map.put("msg", "解析上传文件出错!");
			}
		}else{
			map.put("success", false);
			map.put("msg", "上传文件为空!");
		}
		return map;
	}
}

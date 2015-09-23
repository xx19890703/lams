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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.suun.model.contract.Contract_mode;
import com.suun.model.contract.Contract_template;
import com.suun.model.serviceuser.ContractCategory;
import com.suun.model.serviceuser.ContractDetail;
import com.suun.model.serviceuser.ContractTemplateRes;
import com.suun.model.serviceuser.FactoryInfo;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.serviceuser.UpLoadRecord;
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
import com.suun.service.serviceuser.ContractTemplateResManager;
import com.suun.service.serviceuser.FactoryInfoManager;
import com.suun.service.serviceuser.UpLoadRecordManager;
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
	ContractTemplateResManager conManager;
	
	@Autowired
	DataminingManager dataminingManager;
	
	@Autowired
	FactoryInfoManager factoryInfoManager;
	
	@Autowired
	UpLoadRecordManager uploadManager;
	
	@Autowired 
	DicManager dicManager;
	
	@Autowired
	MenuManager menuManager;	
	
	@Autowired
	private DataminingFactory factory;
	
	// 合同分类id校验
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
	
	// 合同分类name校验
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
	
	protected ContractDetail getGridClass(){
		ContractDetail trd = new ContractDetail();
		List<ContractTemplateRes> rescontent=new ArrayList<ContractTemplateRes>();
		trd.setRescontent(rescontent);
		return trd;
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,String treeid, String[] ids) {
		try{
        	for (int i=0;i<ids.length;i++){			
        		mainManager.deleteContractTemplateRes(ids[i]);
        		mainManager.deleteContractDetails(ids[i]);//传入的是employee id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,ContractDetail operatebean) {
		if(operatebean.getRescontent().size()==0)
			return "请填写合同明细表数据！";
		Set<String> oldset = new HashSet<String>();
		Set<String> newset = new HashSet<String>();
		//将原合同编号放入set中
		for(ContractTemplateRes trc : conManager.getContractTemplateResByContractId(operatebean.getDid())){
			oldset.add(trc.getTemplate().getDid());
		}
		for(ContractTemplateRes trc : operatebean.getRescontent()){
			trc.setId(operatebean.getDid()+"-"+trc.getId());
			newset.add(trc.getTemplate().getDid());
		}
		if(operatebean.getStatus().getKey().getData_no().equalsIgnoreCase("B") && newset.contains(oldset))
			return "合同已审核，不能删除以前的模板！";
		
		try{
			mainManager.deleteContractTemplateRes(operatebean.getDid());
			//mainManager.deleteContractDetails(operatebean.getDid());
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
	public Map<String, Object> gridaudit(HttpServletRequest request, String contractid, String auditPerson, String auditTime) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ContractDetail detail = subManager.get(contractid);
		if(detail != null){
			detail.setAuditPerson(auditPerson);
			detail.setAuditTime(new Date());
			detail.setStatus(dicManager.getByKey("STATUS", "B"));
			subManager.saveRecordSet(request, detail);
			modelMap.put("success", true);
		}else{
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
	/**
	 * 服务端下载数据包，给客户端导入模板使用
	 * @param treeid
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping
	@ResponseBody
	public void griddown(String treeid, String id, String filename, HttpServletRequest request, HttpServletResponse response){
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
	        FactoryInfo fi = contract.getFinfo();
	        fi.setContractId(id);
	        factoryInfoManager.saveFactoryInfo(fi);
	        
	        String tempPath = envPath + File.separator +"down_" + id + "_" + System.currentTimeMillis() + ".zip";
	        zos = new ZipOutputStream(new FileOutputStream(tempPath));
	        DataminingStrategy strategy = factory.getStrategy();
	        int readLength = 0;
	        String filePath = "";
	        
	        
	        //返回合同的数据
	        for(String temp : strategy.findTableData(contract.getDid(),"app_contract_detail"))
	        	sbuffer = sbuffer + temp;
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
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".zip\"");  
	        response.setHeader("Content-Length",String.valueOf(input.read()));
	        response.setContentType("application/octet-stream; charset=utf-8");
	        input.close();
	        outputStream.flush();  
	        outputStream.close();
	        
	      //保存上传记录
			ContractDetail cd = mainManager.getContractDetailByContractId(id);
			//保存上传记录
			UpLoadRecord upload = new UpLoadRecord();
			upload.setContractid(id);
			upload.setContractname(cd.getName());
			upload.setUpTime(new Date());
			upload.setType("导出");
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			upload.setPerson(userDetails.getUsername());
			uploadManager.saveUpLoadRecord(upload);
			
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
	/**
	 * 客户端接收服务端的数据包，生成菜单、展示模板
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> gridupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		String contractId = null;
		List<String> sqls = new ArrayList<String>();
		
		//获取文件后缀
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if(!".zip".equalsIgnoreCase(suffix)){
			map.put("success", false);
			map.put("msg", "上传文件不符合要求(要求zip格式文件)！");
			return map;
		}
		
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher match = p.matcher(file.getOriginalFilename());
        if (match.find()) {
        	map.put("success", false);
			map.put("msg", "上传文件名称不能包含中文！");
            return map;
        }
        
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
				
				//保存上传记录
				ContractDetail cd = mainManager.getContractDetailByContractId(contractId);
				//保存上传记录
				UpLoadRecord upload = new UpLoadRecord();
				upload.setContractid(contractId);
				upload.setContractname(cd.getName());
				upload.setUpTime(new Date());
				upload.setType("导出");
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				upload.setPerson(userDetails.getUsername());
				uploadManager.saveUpLoadRecord(upload);
				
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
			mm.setIsadmin(0);
			mm.setIsframe(0);
			menuManager.saveMenu(mm);
			
			//合同id为父 菜单
			String pid2 =""+System.currentTimeMillis();
			Menu mm2 = new Menu();
			mm2.setMenuId(pid2);
			mm2.setMenuName(contractId+"(浏览)");
			mm2.setMenuType(2);
			mm2.setMenuImg("/resources/images/system/group.png");
			mm2.setMenuParid("0102");
			mm2.setItemOrder(999);
			mm2.setIsadmin(0);
			mm2.setIsframe(0);
			menuManager.saveMenu(mm2);
			
			int i = 1;
			
			for(Contract_template cts:list){
				Menu m = new Menu();
				m.setMenuId(""+System.currentTimeMillis());
				m.setMenuName(cts.getTemplateName());
				m.setMenuType(3);
				m.setMenuImg("/resources/images/system/group.png");
				m.setMenuParid(pid);
				m.setItemOrder(i*10);
				m.setMenuUrl("/ReportServer?reportlet="+cts.getId().getTemplateUrl().replaceAll("\\\\", "/")+"&op=write");//+cts.getOpenType());
				m.setIsadmin(1);
				m.setIsframe(1);
				menuManager.saveMenuAndFunction(m,0);
				i++;
			}
			
			i = 1;
			for(Contract_template cts:list){
				Menu m = new Menu();
				m.setMenuId(""+System.currentTimeMillis());
				m.setMenuName(cts.getTemplateName());
				m.setMenuType(3);
				m.setMenuImg("/resources/images/system/group.png");
				m.setMenuParid(pid2);
				m.setItemOrder(i*10);
				m.setMenuUrl("/ReportServer?reportlet="+cts.getId().getTemplateUrl().replaceAll("\\\\", "/"));
				m.setIsadmin(1);
				m.setIsframe(1);
				menuManager.saveMenuAndFunction(m,1);
				i++;
			}
		}
		return map;
	}
	
	/**
	 * 服务端解析客户端上传的报表数据
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
		
		//获取文件后缀
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if(!".zip".equalsIgnoreCase(suffix)){
			map.put("success", false);
			map.put("msg", "上传文件不符合要求(要求zip格式文件)！");
			return map;
		}
		
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher match = p.matcher(file.getOriginalFilename());
        if (match.find()) {
        	map.put("success", false);
			map.put("msg", "上传文件名称不能包含中文！");
            return map;
        }
		        
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
					ContractDetail cd = mainManager.getContractDetailByContractId(contractId);
					//保存上传记录
					UpLoadRecord upload = new UpLoadRecord();
					upload.setContractid(contractId);
					upload.setContractname(cd.getName());
					upload.setUpTime(new Date());
					upload.setType("上传");
					UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					upload.setPerson(userDetails.getUsername());
					uploadManager.saveUpLoadRecord(upload);
					
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
	
	/**
	 * 客户端生成数据包，给服务端提交数据
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public void clientupload(HttpServletRequest request, HttpServletResponse response, String filename){
		String id = request.getParameter("contractId");
		response.reset();
		
		//创建临时文件夹
		String envPath = request.getSession().getServletContext().getRealPath(File.separator + "tempfile");
		File filePaths = new File(envPath);
		if(!filePaths.exists() && !filePaths.isDirectory()){
			filePaths.mkdir();    
		} 
		
		ZipOutputStream zos = null;
		String sbuffer = new String();
		try {
	        ContractDetail contract = mainManager.getContractDetailByContractId(id);
	        String tempPath = envPath + File.separator +"down_" + id + "_" + System.currentTimeMillis() + ".zip";
	        zos = new ZipOutputStream(new FileOutputStream(tempPath));
	        int readLength = 0;
	        
	        //返回合同-数据库表的数据
	        for(String temp : dataminingManager.findTableData(contract.getDid(),"contract_mode"))
	        	sbuffer = sbuffer + temp;
	        //返回合同-模板url的数据
	        for(String temp : dataminingManager.findTableData(contract.getDid(),"contract_template"))
	        	sbuffer = sbuffer + temp;
	      //遍历所有数据库表
			List<String> tableNames = dataminingManager.findModeTables(contract.getDid());
			for(String tableName:tableNames){
				for(String temp : dataminingManager.findTableData(contract.getDid(), tableName))
				sbuffer = sbuffer + temp;
			}
	        //遍历所有模板
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
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".zip\"");  
	        response.setHeader("Content-Length",String.valueOf(input.read()));
	        response.setContentType("application/octet-stream; charset=utf-8");
	        input.close();
	        outputStream.flush();  
	        outputStream.close();
	        
	        ContractDetail cd = mainManager.getContractDetailByContractId(id);
			//保存上传记录
			UpLoadRecord upload = new UpLoadRecord();
			upload.setContractid(id);
			upload.setContractname(cd.getName());
			upload.setUpTime(new Date());
			upload.setType("导出");
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			upload.setPerson(userDetails.getUsername());
			uploadManager.saveUpLoadRecord(upload);
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
}

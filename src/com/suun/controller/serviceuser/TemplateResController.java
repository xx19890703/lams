package com.suun.controller.serviceuser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.base.FRContext;
import com.suun.model.serviceuser.TemplateCfgUpLoad;
import com.suun.model.serviceuser.TemplateRes;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.system.Constant;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.TemplateCfgUpLoadManager;
import com.suun.service.serviceuser.TemplateResManager;
import com.suun.service.system.DicManager;

/**
 * TemplateResController
 * @author renlq
 *
 */

@Controller 
public class TemplateResController extends TreeGridCRUDController<TemplateRes,TemplateResDetail>{

	@Autowired
	TemplateResManager mainManager;
	
	@Autowired
	TemplateCfgUpLoadManager upManager;
	
	@Autowired 
	DicManager dicManager;
	
	 //模板分类id校验
	@RequestMapping
	@ResponseBody
	public String validateDid(HttpServletRequest request,HttpServletResponse response) {	
		String did = request.getParameter("id");
		String olddid = request.getParameter("olddid");		
		if(mainManager.isDTemplateResIdUnique(did, olddid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	//模板分类name校验
	@RequestMapping
	@ResponseBody
	public String validateName(HttpServletRequest request,HttpServletResponse response) {	
		String name = request.getParameter("name");
		String oldname = request.getParameter("oldname");
		String pid = request.getParameter("id");
		
		if(mainManager.isTemplateResNameUnique(pid, name, oldname)){
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
	public Map<String,Object> doGridEditbefore(HttpServletRequest request,String treeid,TemplateResDetail operatebean) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", dicManager.findDicsByDicno(Constant.STATE));
		return map;
	}
	
	@Override
	public String treeDeleteRecordSet(String[] ids) {
		try{
			Boolean flag=null;
            for (int i=0;i<ids.length;i++){
            	List<TemplateRes> orglist=mainManager.findTemplateResByParentId(ids[i]);
            	//判断节点是否是父节点，长度为0表示不是父节点可以删除，反之就是父节点不可删除。
            	if(orglist.size()==0){
            		mainManager.deleteTemplateRes(ids[i]);
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
	public String treeSaveRecordSet(TemplateRes operatebean) {
		try{			
			mainManager.saveTemplateRes(operatebean);
			return "";
		} catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public List<TemplateRes> getTreeAllRecords(String Pid) {
		return mainManager.findTemplateResByParentId(Pid);
	}

	@Override
	public TemplateRes getTreeRecordSet(String operateid) {
		return mainManager.getTemplateRes(operateid);
	}

	@Override
	public String getTreeId(TemplateRes t) {
		return t.getDid().toString();
	}

	@Override
	public String getTreeName(TemplateRes t) {
		return t.getName();
	}

	@Override
	public String getTreePid(TemplateRes t) {
		return t.getPid();
	}

	@Override
	public String getTreeIcon(HttpServletRequest request, TemplateRes t) {
		return "/resources/images/system/group.png";
	}

	@Override
	public int getTreeLeaf(TemplateRes t) {
		return 0;
	}

	@Override
	protected String deleteGridRecordSet(HttpServletRequest request,String treeid, String[] ids) {
		try{
        	for (int i=0;i<ids.length;i++){			
        		mainManager.deleteTemplateResContent(ids[i]);
        		mainManager.deleteTemplateResDetail(treeid,ids[i]);//传入的是 id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,TemplateResDetail operatebean) {
		try{
			if(operatebean.getRescontent().size()==0)
				return "请填写模板明细表数据！";
			//判断数据表在数据库中是否存在
			for(TemplateResContent trc:operatebean.getRescontent()){
				if(!mainManager.isExistTable(trc.getName())){
					return "数据表【" + trc.getName() + "】不存在，请检查后提交！";
				}
			}
			mainManager.deleteTemplateResContent(operatebean.getDid());
			mainManager.saveTemplateResDetail(operatebean);
			for(TemplateResContent trc:operatebean.getRescontent()){
				//设置主键
				trc.setDid(operatebean.getDid()+"-"+trc.getDid());
				trc.setResdetail(operatebean);
				mainManager.saveTemplateResContent(trc);
			}
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected Page<TemplateResDetail> getGridPageRecords(HttpServletRequest request, String treeid,Page<TemplateResDetail> page) {
		String type = request.getParameter("type");
		if(type!=null&&"sel".equals(type)){
			return mainManager.getSelTemplateResDetail(treeid,page);
		}else{
			return mainManager.getAllTemplateResDetail(treeid, page);
		}
	}

	@Override
	protected List<TemplateResDetail> getGridAllRecords(HttpServletRequest request, String treeid, Condition condition) {
		return mainManager.findTemplateResDetailByParentId(treeid);
	}

	@Override
	protected TemplateResDetail getGridRecordSet(HttpServletRequest request,String treeid, String operateid) {
		return mainManager.getTemplateResDetail(treeid, operateid);
	}
	
	protected TemplateResDetail getGridClass(){
		TemplateResDetail trd = new TemplateResDetail();
		List<TemplateResContent> rescontent=new ArrayList<TemplateResContent>();
		trd.setRescontent(rescontent);
		return trd;
	}
	
	@RequestMapping
	public ModelAndView getGridAllRecords() {	
		ModelAndView mav = new ModelAndView();
		mav.addObject(mainManager.getAllTemplateRes("01",null));
		return mav;
	}
	
	@RequestMapping
	@ResponseBody
	public String checkgriddid(HttpServletRequest request,HttpServletResponse response) {	
		String id = request.getParameter("did");
		String oldid = request.getParameter("oldid");
		
		if(mainManager.isGridDidUnique(id, oldid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	@RequestMapping
	@ResponseBody
	public String checkgridname(HttpServletRequest request,HttpServletResponse response) {	
		String id = request.getParameter("name");
		String oldid = request.getParameter("oldid");
		
		if(mainManager.isGridNameUnique(id, oldid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
	/**
	 * 服务端解析    配置模板包的数据
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return  前端extjs form.getForm().submit 需要返回类型为text/html  故返回String
	 * @throws JsonProcessingException 
	 */
	@RequestMapping
	@ResponseBody
	public String configupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException{
		
		int isflag = 0;
		Map<String,Object> map=new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper();
		//获取文件后缀
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if(!".zip".equalsIgnoreCase(suffix)){
			map.put("success", false);
			map.put("msg", "上传文件不符合要求(要求zip格式文件)！");
			return renderHtml(response, mapper.writeValueAsString(map));
		}
		
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher match = p.matcher(file.getOriginalFilename());
        if (match.find()) {
        	map.put("success", false);
			map.put("msg", "上传文件名称不能包含中文！");
			return renderHtml(response, mapper.writeValueAsString(map));
        }
        
		if (!file.isEmpty()){
			//文件上传路径
			String path = request.getSession().getServletContext().getRealPath(File.separator + "tempfile" + File.separator + "config_" + file.getOriginalFilename());
			//解包后xls临时路径前缀
			String xlspath = request.getSession().getServletContext().getRealPath(File.separator + "tempfile" + File.separator + "config_");
			//解包后xls路径
			String xls = "";
			File destFile = new File(path);
			try {
				String envPath = FRContext.getCurrentEnv().getPath();
				FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
				ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
				BufferedInputStream bin=new BufferedInputStream(zip);
				ZipEntry entry;
				//逐个读取压缩包的文件，先处理sql、cpt
				while((entry = zip.getNextEntry()) != null ){
					if (entry.isDirectory()) {
						continue;
					}
					//处理以.cpt结尾的文件  及 .sql结尾的文件  .xls文件
					String fileName = entry.getName();
					if(fileName.endsWith(".cpt")){
						destFile=new File(envPath + "reportlets",entry.getName());
						if(!destFile.exists()){
							(new File(destFile.getParent())).mkdirs();
						}
						FileOutputStream out=new FileOutputStream(destFile);
						BufferedOutputStream bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							bout.write(b);
						}
						bout.close();
						out.close();
						isflag ++;
					}else if(fileName.endsWith(".sql")){
						destFile=new File(envPath,entry.getName());
						if(!destFile.exists()){
							(new File(destFile.getParent())).mkdirs();
						}
						FileOutputStream out=new FileOutputStream(destFile);
						BufferedOutputStream bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							bout.write(b);
						}
						bout.close();
						out.close();
						isflag ++;
					}else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
						destFile=new File(envPath,entry.getName());
						xls = xlspath + entry.getName();
						FileOutputStream out=new FileOutputStream(xls);
						BufferedOutputStream bout=new BufferedOutputStream(out);
						int b;
						while((b=bin.read())!=-1){
							bout.write(b);
						}
						bout.close();
						out.close();
						isflag ++;
						String str = readExcel(xls);
						if(!str.equals("")){
							map.put("success", false);
							map.put("msg", str);
							return renderHtml(response, mapper.writeValueAsString(map));
						}else{
							//保存上传记录
							TemplateCfgUpLoad upload = new TemplateCfgUpLoad();
							UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
							upload.setPerson(userDetails.getUsername());
							upload.setUpTime(new Date(System.currentTimeMillis()));
							upload.setPath(path);
							upManager.saveTemplateCfgUpLoad(upload);
							
							map.put("success", true);
							map.put("msg", "解析上传文件成功!");
							return renderHtml(response, mapper.writeValueAsString(map));
						}
					}
					bin.close();  
					zip.close();
				}  
				if(isflag >= 3){
					map.put("success", false);
					map.put("msg", "文件错误(文件需包含.cpt .sql .xls文件)，请核对后上传!");
					return renderHtml(response, mapper.writeValueAsString(map));
				}
			} catch (IOException e) {
				map.put("success", false);
				map.put("msg", "解析上传文件出错!");
				return renderHtml(response, mapper.writeValueAsString(map));
			}
		}else{
			map.put("success", false);
			map.put("msg", "上传文件为空!");
		}
		return renderHtml(response, mapper.writeValueAsString(map));
	}
	
	
	/**
	 * 解析开发人员生成的配置包中的xls文件，生成模板信息
	 * @return
	 */
	public String readExcel(String path){
		//String filepath = FRContext.getCurrentEnv().getPath();
		//定义sheet名称
		String [] sheets = {"templateres", "templateres_detail", "templateres_content"};
		// 构造 Workbook 对象，strPath 传入文件路径  
		Workbook xls = null;
		try{
			if (path.endsWith("xlsx")){
				//2007格式
				xls = new XSSFWorkbook(new FileInputStream(new File(path)));
			}else if (path.endsWith("xls")){
				//2003格式
				xls = new HSSFWorkbook(new FileInputStream(new File(path)));
			}
		}catch(Exception e){
			return "Excel file is not found!";
		}
		
		//判断文件是否包含sheets中定义的sheet页
		for(int i=0; i<sheets.length; i++){
			if(null == xls.getSheet(sheets[i]))
				return "xls配置文件错误，缺少【" + sheets[i] + "】页";
		}
		
		// 读取sheet(templateres)的内容  
		Sheet sheet = xls.getSheet(sheets[0]);
		Row row = null;
		// 循环输出表格中的内容  
		for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			TemplateRes temp = mainManager.getTemplateRes(row.getCell(0).toString());
			if(null == temp){
			    TemplateRes tempres = new TemplateRes();
		        tempres.setDid(row.getCell(0).toString());
		        tempres.setName(row.getCell(1).toString());
		        tempres.setDescription(row.getCell(2).toString());
		        tempres.setPid(row.getCell(3).toString());
		        tempres.setState(dicManager.getByKey("STATE", "1"));
		        mainManager.saveTemplateRes(tempres);
			}else{
				return sheets[0]+"中主键(" + temp.getDid() + ")重复！";
			}
		}
		
		// 读取sheet(templateres_detail)的内容  
		sheet = xls.getSheet(sheets[1]); 
		for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
		   row = sheet.getRow(i);
		   TemplateResDetail resdetail = mainManager.getTemplateResDetail(row.getCell(0).toString());
		   if(null == resdetail){
			   TemplateRes tempres = mainManager.getTemplateRes(row.getCell(3).toString());
			   if(null != tempres){
				   TemplateResDetail tempdetail = new TemplateResDetail();
				   tempdetail.setDid(row.getCell(0).toString());
				   tempdetail.setName(row.getCell(1).toString());
				   //String str = filepath + "reportlets" + File.separator + row.getCell(2).toString().replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
				   //if(new File(str).exists())
					   tempdetail.setPath(row.getCell(2).toString());
				   //else
					   //return sheets[1]+"中"+row.getCell(2).toString()+"文件不存在！";
				   tempdetail.setResmain(tempres);
				   tempdetail.setState(dicManager.getByKey("STATE", "1"));
				   mainManager.saveTemplateResDetail(tempdetail);
			   }else{
				   return sheets[1]+"未找到此行的外键！";
			   }
		   }else{
			   return sheets[1]+"中主键(" + resdetail.getDid() + ")重复！";
		   }
		}
				
		// 读取sheet(templateres_content)的内容  
		sheet = xls.getSheet(sheets[2]); 
		for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			TemplateResDetail rescontent = mainManager.getTemplateResDetail(row.getCell(0).toString());
			if(null == rescontent){
				TemplateResDetail resdetail = mainManager.getTemplateResDetail(row.getCell(4).toString());
				if(null != resdetail){
					TemplateResContent tempcontent = new TemplateResContent();
					tempcontent.setDid(row.getCell(0).toString());
					tempcontent.setName(row.getCell(1).toString());
					//if(new File(filepath+row.getCell(2).toString().replaceAll("\\\\", Matcher.quoteReplacement(File.separator))).exists())
						tempcontent.setCsqlpath(row.getCell(2).toString());
				    //else
					   //return sheets[2]+"中"+row.getCell(2).toString()+"文件不存在！";
					tempcontent.setIsqlpath(row.getCell(3).toString());
					tempcontent.setResdetail(resdetail);
					tempcontent.setState(dicManager.getByKey("STATE", "1"));
					mainManager.saveTemplateResContent(tempcontent);
				}else{
					return sheets[2]+"未找到此行的外键！";
				}
			}else{
				return sheets[2]+"中主键(" + rescontent.getDid() + ")重复！";
			}
		}
		mainManager.getSessionFactory().getCurrentSession().flush();
		return "";
	}
}

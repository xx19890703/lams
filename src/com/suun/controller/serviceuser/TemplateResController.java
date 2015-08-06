package com.suun.controller.serviceuser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.suun.model.serviceuser.TemplateRes;
import com.suun.model.serviceuser.TemplateResContent;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.system.Constant;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
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
	DicManager dicManager;
	
	@RequestMapping
	@ResponseBody
	public String validateDid(HttpServletRequest request,HttpServletResponse response) {	
		String did = request.getParameter("did");
		String olddid = request.getParameter("olddid");		
		if(mainManager.isDTemplateResIdUnique(did, olddid)){
			return renderText(response,"true");
		}else{
			return renderText(response,"false");
		}
	}
	
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
		if(operatebean.getRescontent().size()==0)
			return "请填写明细表数据！";
		//判断数据表在数据库中是否存在
		for(TemplateResContent trc:operatebean.getRescontent()){
			if(!mainManager.isExistTable(trc.getName())){
				return "表【" + trc.getName() + "】不存在，请检查后提交！";
			}
		}
		mainManager.deleteTemplateResContent(operatebean.getDid());
		try{
			for(TemplateResContent trc:operatebean.getRescontent()){
				trc.setDid(operatebean.getDid()+"-"+trc.getDid());
			}
			mainManager.saveTemplateResDetail(operatebean);
			return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected Page<TemplateResDetail> getGridPageRecords(HttpServletRequest request, String treeid,Page<TemplateResDetail> page) {
		return mainManager.getAllTemplateResDetail(treeid, page);
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
	 */
	@RequestMapping
	@ResponseBody
	public String configupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		String returnStr = "";
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
				//逐个读取压缩包的文件
				while((entry = zip.getNextEntry()) != null ){
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
						String str = readExcel(xls);
						if(!str.equals("")){
							returnStr = "{\"success\":false,\"msg\":\"" + str + "\"}";
						}else{
							returnStr = "{\"success\":true,\"msg\":\"上传解析文件成功!\"}";
						}
					}
				}  
				bin.close();  
				zip.close();
			} catch (IOException e) {
				returnStr = "{\"success\":false,\"msg\":\"解析上传文件出错!\"}";
				return returnStr;
			}
		}else{
			returnStr = "{\"success\":false,\"msg\":\"上传文件为空!\"}";
		}
		return returnStr;
	}
	
	
	/**
	 * 解析开发人员生成的配置包中的xls文件，生成模板信息
	 * @return
	 */
	public String readExcel(String path){
		String cptpath = FRContext.getCurrentEnv().getPath()+ "reportlets" + File.separator + "";
		String sqlpath = FRContext.getCurrentEnv().getPath();
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
				return "xls配置文件错误！";
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
				return sheets[0]+"中主键重复！";
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
				   String str = cptpath+row.getCell(2).toString().replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
				   if(new File(str).exists())
					   tempdetail.setPath(row.getCell(2).toString());
				   else
					   return sheets[1]+"中"+row.getCell(2).toString()+"文件不存在！";
				   tempdetail.setResmain(tempres);
				   tempdetail.setState(dicManager.getByKey("STATE", "1"));
				   mainManager.saveTemplateResDetail(tempdetail);
			   }else{
				   return sheets[1]+"未找到此行的外键！";
			   }
		   }else{
			   return sheets[1]+"中主键重复！";
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
					if(new File(sqlpath+row.getCell(2).toString().replaceAll("\\\\", Matcher.quoteReplacement(File.separator))).exists())
						tempcontent.setCsqlpath(row.getCell(2).toString());
				   else
					   return sheets[2]+"中"+row.getCell(2).toString()+"文件不存在！";
					tempcontent.setIsqlpath(row.getCell(3).toString());
					tempcontent.setResdetail(resdetail);
					tempcontent.setState(dicManager.getByKey("STATE", "1"));
					mainManager.saveTemplateResContent(tempcontent);
				}else{
					return sheets[2]+"未找到此行的外键！";
				}
			}else{
				return sheets[2]+"中主键重复！";
			}
		}
		mainManager.getSessionFactory().getCurrentSession().flush();
		
		return "";
	}
}

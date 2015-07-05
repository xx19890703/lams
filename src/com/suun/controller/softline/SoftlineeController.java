package com.suun.controller.softline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.suun.model.softline.Softlinee;
import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.publics.controller.BaseCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.FilterInfo;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.system.CountService;
import com.suun.publics.system.DicService;
import com.suun.publics.utils.Utils;
import com.suun.service.system.DicManager;


/**
 * @author 
 * 
 */
@Controller 
public class SoftlineeController extends BaseCRUDController<Softlinee>{
	
	@Autowired  
	SessionRegistry sessionRegistry; 

	@Autowired
	com.suun.service.softline.SoftlineeManager softlineeManager; 
	
	@Autowired
	DicManager dicManager;

	@Override
	protected Map<String,Object> doNewbefore(HttpServletRequest request) {
		
		//获取系统的当前时间
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	    java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
	    String str_date1 = formatter.format(currentTime); //将日期时间格式化 
	    request.setAttribute("str_date1",str_date1);
	    
	    //自动生成编码
	    /*List<Softlinee> softlineelist=null;
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		SessionFactory sessionFactory=(SessionFactory) ctx.getBean("sessionFactory");
		Session session=sessionFactory.openSession();
		softlineelist=session.createCriteria(Softlinee.class).list();
		session.close();
		ArrayList<String> softlineelists=new ArrayList<String>();
		for(int i=0;i<softlineelist.size();i++){
			softlineelists.add(softlineelist.get(i).getBaseId());
		}
		int maxBaseId = 0;
		try
        {
            int totalCount = softlineelists.size();
            if (totalCount >= 1)
            {
                int  max = Integer.parseInt((softlineelists.get(0).toString()));
                for (int i = 0; i < totalCount; i++)
                {
                    int temp = Integer.parseInt(softlineelists.get(i).toString());
                    if (temp > max)
                    {
                        max =  temp;
                    }
                } maxBaseId = max+1;
          
            }else{
            	maxBaseId=1;
            } 
        }
        catch (Exception exception)
        {
           throw exception;
        }*/
	    String maxBaseId;
	    Long maxBaseId1=CountService.GetCountingByCountNo("Softlinee")+1;
	    Long year=CountService.GetCountingByCountNo("Softlinee-Year");
	    @SuppressWarnings("deprecation")
		int cyear=new GregorianCalendar().getTime().getYear();
	    if (cyear!=year.intValue()){
	    	maxBaseId1=(long) 1;
	    	maxBaseId=Long.toString(maxBaseId1);
	    } 
	    String sR="0000"+Long.toString(maxBaseId1);
    	int x=(sR.length()-4);
        maxBaseId=sR.substring(x);
		request.setAttribute("maxBaseId",maxBaseId);
		
		//获得状态和性别
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", DicService.GetDicByDicNo("SLSTATE"));
		rmap.put("allObjectd", DicService.GetDicByDicNo("SEX"));
		return rmap;
	}
	@Override
	protected Map<String,Object> doEditbefore(HttpServletRequest request,Softlinee operatebean) {		
		//获取系统的当前时间
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	    java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
	    String str_date1 = formatter.format(currentTime); //将日期时间格式化 
	    request.setAttribute("str_date1",str_date1);
	    
	  //获得状态和性别
		Map<String,Object> rmap=new HashMap<String,Object>();
		rmap.put("allObjects", dicManager.findDicsByDicno("SLSTATE"));
		rmap.put("allObjectd", dicManager.findDicsByDicno("SEX"));
		return rmap;
	}
	
	@Override
	public Page<Softlinee> getPageRecords(HttpServletRequest request,Page<Softlinee> page) {
		Dic_datakey dk=new Dic_datakey();
		dk.setData_no("0");
		dk.setDic_no("SLSTATE");
		Dic_data dd=new Dic_data();
		dd.setKey(dk);
		FilterInfo filter=new FilterInfo();
		filter.setFieldName("slstate");
		filter.setLogic(Logic.EQUAL);
		filter.setValue(dd);
		page.getCondition().getFilterInfos().add(filter);
		return softlineeManager.getAllSoftlinees(page);
	}

	@Override
	public List<Softlinee> getAllRecords(HttpServletRequest request,Condition condition) {
		return softlineeManager.getAllSoftlinees(condition);
	}

	@Override
	public Softlinee getRecordSet(HttpServletRequest request,String operateid) {
		return softlineeManager.getSoftlinee(operateid);
	}

	@Override
	public String deleteRecordSet(HttpServletRequest request,String[] ids) {
		String err="";
		try{
            for (int i=0;i<ids.length;i++){
            	if (softlineeManager.getSoftlinee(ids[i]).getSlstate().getKey().getData_no().equals("1")||softlineeManager.getSoftlinee(ids[i]).getSlstate().getKey().getData_no().equals("3")){
            		err=err+ids[i]+",";    				
    			}
            	try{
            		softlineeManager.deleteSoftlinee(ids[i]);
            	}catch(Exception e){
            		return "编号["+err.substring(0, err.length()-1)+"]的申请已经评审，不能删除！";
            	}
		    }
            return "";
		} catch (Exception e){
			String message=e.toString();
			logger.error(message);
			return message;
		}
	}

	@Override
	public String saveRecordSet(HttpServletRequest request,Softlinee operatebean) {
		try{
			boolean isNew=request.getSession().getAttribute("OperateState").equals("New");
			if (!isNew){
				softlineeManager.saveSoftlinee(false,operatebean);
			}else{				
				softlineeManager.saveSoftlinee(isNew,operatebean);
			}
			return null;
		} catch (Exception e){
			String message=e.toString();
			if (e.getMessage()!=null){
				message=e.getMessage();
			}
			logger.error(message);
			return message;
		}
	}

	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {
		
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("sex_state",DicService.GetDataNameExtArrayByDicNo("SEX"));	 	 
		modelandview.getModel().put("sl_state",DicService.GetDataNameExtArrayByDicNo("SLSTATE"));	 
		return modelandview;
	}
	
	//改变状态
	@RequestMapping
	@ResponseBody
	public boolean saveSlstate(HttpServletRequest request){
		String baseId=request.getParameter("baseId");	
		Softlinee softlinee=softlineeManager.getSoftlinee(baseId);
		Dic_datakey dk=new Dic_datakey();
		dk.setData_no("2");
		dk.setDic_no("SLSTATE");
		Dic_data dd=new Dic_data();
		dd.setKey(dk);
		softlinee.setSlstate(dd);
		softlineeManager.saveSoftlinee(false,softlinee);
		return true;
		
	}
	// 查找当前目录下的文件
	@RequestMapping
	@ResponseBody
	public String listFile(HttpServletResponse response){		
	
		String path=Utils.getAbsolutePath("/resources/images/picture/");
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
			String path = Utils.getAbsolutePath("/resources/images/picture/"
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
			java.util.Date date=new java.util.Date();
			java.text.SimpleDateFormat ss=new java.text.SimpleDateFormat("yyyyMMddhhmmss");
			String dates=ss.format(date);
			for (MultipartFile mf : mfs) {
				SaveFileFromInputStream(mf.getInputStream(),
						Utils.getAbsolutePath("/resources/images/picture") ,
						dates+mf.getOriginalFilename());
			}
			return "{\"success\":true,\"msg\":\"图片上传成功！\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"success\":true,\"msg\":\"图片上传失败！\"}";			
		}
	}		
	
}

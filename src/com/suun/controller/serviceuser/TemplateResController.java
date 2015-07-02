package com.suun.controller.serviceuser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.fr.base.dav.Env;
import com.fr.base.dav.LocalEnv;
import com.fr.report.WorkBook;
import com.fr.report.io.TemplateImporter;
import com.fr.report.io.core.EmbeddedTableDataExporter;
import com.suun.model.serviceuser.TemplateRes;
import com.suun.model.serviceuser.TemplateResDetail;
import com.suun.model.system.Constant;
import com.suun.publics.controller.TreeGridCRUDController;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.service.serviceuser.TemplateResDeatilManager;
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
	TemplateResDeatilManager subManager;
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
	
	@RequestMapping
	public String getTemplateRes(HttpServletRequest request,HttpServletResponse response) {
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
        		mainManager.deleteTemplateResDetail(treeid,ids[i]);//传入的是employee id
    		}
    		return "";
		}catch (Exception e){
			return e.getMessage();
		}
	}

	@Override
	protected String saveGridRecordSet(HttpServletRequest request,TemplateResDetail operatebean) {
		try{
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
	
	@RequestMapping
	public ModelAndView getGridAllRecords() {	
		ModelAndView mav = new ModelAndView();
		mav.addObject(mainManager.getAllTemplateRes("01",null));
		return mav;
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping
	@ResponseBody
	public void downPZip(String id, final HttpServletResponse response){  
	    
		byte[] data = null;
	 // 定义报表运行环境,才能执行报表  
        //String envPath = "D:\\FineReport_6.5\\WebReport\\WEB-INF"; 
        Env oldEnv = FRContext.getCurrentEnv();  
        String envPath = oldEnv.getPath();  
        System.out.println(envPath);
        FRContext.setCurrentEnv(new LocalEnv(envPath)); 
        File file = new File(envPath+"\\reportlets\\");
        //文件后缀名过滤
        String [] temp = file.list(new FilenameFilter(){
            public boolean accept(File f , String name){
                return name.endsWith(".cpt");  
            }    
        });
        for(String s : temp){
        	System.out.print(envPath+"\\"+s);
	        try {
	            // 未执行模板工作薄  
	            File cptfile = new File(envPath+"\\reportlets\\"+s);  
	            TemplateImporter tplImp = new TemplateImporter();  
	            WorkBook workbook = tplImp.generateWorkBook(cptfile);
	            // 获取报表参数并设置值，导出内置数据集时数据集会根据参数值查询出结果从而转为内置数据集  
//	            Parameter[] parameters = workbook.getParameters();  
//	            parameters[0].setValue("2015-06-10");  
	            // 定义parametermap用于执行报表，将执行后的结果工作薄保存为rworkBook  
	//            java.util.Map parameterMap = new java.util.HashMap();  
	//            for (int i = 0; i < parameters.length; i++) {  
	//                parameterMap.put(parameters[i].getName(), parameters[i].getValue());  
	//            }
	            // 定义输出流  
	            FileOutputStream outputStream1;  
	            // 将未执行模板工作薄导出为内置数据集模板  
	            outputStream1 = new FileOutputStream(new File("E:\\test\\"+s));  
	            EmbeddedTableDataExporter EmbExport = new EmbeddedTableDataExporter();  
	            EmbExport.export(outputStream1, workbook);
	            
	//            //通过名称查找数据源
	            /*NameDatabaseConnection nconn = new NameDatabaseConnection("erp");
	            Connection conn = nconn.createConnection();
	            //Statement s1 = conn.createStatement();
	            //s1.execute("show create table demo");
	            //s1.executeUpdate("CREATE TABLE if not exists erp_test_v1 (`id` varchar(255) default NULL,`name` varchar(255) default NULL);");
	            String sql = "show create table erp_customer";
	            PreparedStatement pstmt;
	            try {
	                pstmt = (PreparedStatement)conn.prepareStatement(sql);
	                ResultSet rs = pstmt.executeQuery();
	                int col = rs.getMetaData().getColumnCount();
	                System.out.println("*********************");
	                while (rs.next()) {
	                    for (int i = 1; i <= col; i++) {
	                        System.out.print(rs.getString(i) + "\t");
	                        if ((i == 2) && (rs.getString(i).length() < 8)) {
	                        }
	                     }
	                    System.out.println("");
	                }
	                    System.out.println("***************");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }*/
	//
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
	        
        }
        try {
        	this.zipFile("c:\\ren.zip",new File("e:\\test\\"));
        	InputStream in = new FileInputStream("c:\\ren.zip");
        	data = this.toByteArray(in);
        	String fileName = System.currentTimeMillis()+".zip";  
        	fileName = URLEncoder.encode(fileName, "UTF-8");  
        	response.reset();  
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
        	response.addHeader("Content-Length", "" + data.length);  
        	response.setContentType("application/octet-stream;charset=UTF-8");  
        	OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
        	outputStream.write(data);  
        	outputStream.flush();  
        	outputStream.close();  
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
	}
	

	//压缩某个文件夹下所有的文件
	private void zipFile(String zipFileName, File inputFile) throws Exception {  
        System.out.println("压缩中...");  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));  
        BufferedOutputStream bo = new BufferedOutputStream(out);  
        zip(out, inputFile, inputFile.getName(), bo);  
        bo.close();  
        out.close(); // 输出流关闭  
        System.out.println("压缩完成");  
	} 
	
	//迭代遍历每个文件夹下所有文件
	private void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	}  
	
	public void unzip() {  
        // TODO Auto-generated method stub  
        long startTime=System.currentTimeMillis();  
        try {  
            ZipInputStream Zin=new ZipInputStream(new FileInputStream("C:\\Users\\HAN\\Desktop\\stock\\SpectreCompressed.zip"));//输入源zip路径  
            BufferedInputStream Bin=new BufferedInputStream(Zin);  
            String Parent="C:\\Users\\HAN\\Desktop"; //输出路径（文件夹目录）  
            File Fout=null;  
            ZipEntry entry;  
            try {  
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
                    Fout=new File(Parent,entry.getName());  
                    if(!Fout.exists()){  
                        (new File(Fout.getParent())).mkdirs();  
                    }  
                    FileOutputStream out=new FileOutputStream(Fout);  
                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
                    int b;  
                    while((b=Bin.read())!=-1){  
                        Bout.write(b);  
                    }  
                    Bout.close();  
                    out.close();  
                    System.out.println(Fout+"解压成功");      
                }  
                Bin.close();  
                Zin.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
        long endTime=System.currentTimeMillis();  
        System.out.println("耗费时间： "+(endTime-startTime)+" ms");  
    }
	
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}
	
	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {
			return -1;
		}
		return (int) count;
	}
	
	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[4096];
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	
    public String upload(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException {
    	
    	Env oldEnv = FRContext.getCurrentEnv();  
        String envPath = oldEnv.getPath(); 
        
        //创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();  
            while(iter.hasNext()){  
                //记录上传过程起始时的时间，用来计算上传时间  
                int pre = (int) System.currentTimeMillis();  
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                if(file != null){  
                    //取得当前上传文件的文件名称  
                    String myFileName = file.getOriginalFilename();  
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){  
                        System.out.println(myFileName);  
                        //重命名上传后的文件名  
                        String fileName = file.getOriginalFilename();  
                        //定义上传路径  
                        String path = envPath + fileName;  
                        File localFile = new File(path);  
                        file.transferTo(localFile);  
                    }  
                }  
                //记录上传该文件后的时间  
                int finaltime = (int) System.currentTimeMillis();  
                System.out.println(finaltime - pre);  
            }  
              
        }  
        return "/success";  
    }  
}

package com.suun.publics.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;












/*
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.JsonGenerator; */
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Utils {
	
	public static String getLoginUserId()  {
		Authentication  auth=SecurityContextHolder.getContext().getAuthentication();
		String user=((User)auth.getPrincipal()).getUsername();
		return user; 
	}
	
	public static List<String> getLoginUserauth()  {
		Authentication  auth=SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> ca= auth.getAuthorities();
		List<String> auths=new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<GrantedAuthority> it = (Iterator<GrantedAuthority>) ca.iterator();
		while (it.hasNext()){
			GrantedAuthority ga=it.next();
			auths.add(ga.getAuthority());
		}
		return auths;
	}
	
	public static boolean isNullOrTemp(String str){
		boolean b=true;
		if (str!=null){
			if (str.length()!=0){
			   b=false;	
			}
		}	
		return b;
	}
	
	public static boolean isNullOrTemp(Integer t){
		boolean b=true;
		if (t!=null){
			
			   b=false;	
			
		}	
		return b;
	}
	
	public static String field2GetMethod(String fieldname){
		return "get".concat(fieldname.substring(0, 1).toUpperCase()).concat(fieldname.substring(1));		
	}
	
	public static String field2SetMethod(String fieldname){
		return "set".concat(fieldname.substring(0, 1).toUpperCase()).concat(fieldname.substring(1));		
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getFieldType(Class clazz,String fieldname) throws SecurityException, NoSuchFieldException{
		String[] fields=fieldname.split("\\.");
		Class cla=clazz;
		for (int i=0;i<fields.length-1;i++){
			cla=clazz.getDeclaredField(fields[i]).getType();
		}
		return cla.getDeclaredField(fields[fields.length-1]).getType();
		
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
		 
	    Class ownerClass = owner.getClass();
	    if (args==null){
	    	args=new Object[0];
	    }	    	
	    Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
		       argsClass[i] = args[i].getClass();
		}
		java.lang.reflect.Method method=null;
		try {
			method = ownerClass.getMethod( methodName, argsClass);
		} catch (Exception e) {
			throw e;
		}	
		Object object=null;
		if (method!=null){
			try {
				object=method.invoke(owner, args);
			} catch (Exception e) {
				throw e;
			}
		}
		return object;
	}
	
	public static Object getPropertyValue (Object entity,String PropertyName){
		Object entity1 = entity;
		String[] as = PropertyName.split("[.]");
		if ((as != null) && (as.length > 0)) {
			for (int j = 0; j < as.length; j++) {
				if (entity1!=null){
					try {
						entity1 = Utils.invokeMethod(entity1, Utils.field2GetMethod(as[j]), new Class[] {});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		} else{
			entity1=null;
		}
		return entity1;
	}
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/**
	 * 
	 * @param byteArray
	 * @return 16 String
	 */
	private static String byteArrayToHexString(byte[] byteArray){
		StringBuffer sb = new StringBuffer();
		for(byte byt:byteArray){
			sb.append(byteToHexString(byt));
		}
		return sb.toString();
	}
	/**
	 * ���ֽ�ת��Ϊ16�����ַ�

	 * @param byt �ֽ�
	 * @return 16�����ַ�

	 */
	private static String byteToHexString(byte byt) {
		int n = byt;
		if (n < 0)
			n = 256 + n;
		return hexDigits[n/16] + hexDigits[n%16];
	}
	/**
	 * ��ժҪ��Ϣת��Ϊ��Ӧ�ı���

	 * @param code ��������
	 * @param message ժҪ��Ϣ
	 * @return ��Ӧ�ı����ַ�
	 */
	private static String Encode(String code,String message){
		MessageDigest md;
		String encode = null;
		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encode;
	}
	/**
	 * ��ժҪ��Ϣת����MD5����
	 * @param message ժҪ��Ϣ
	 * @return MD5����֮����ַ�
	 */
	public static String md5Encode(String message){
		return Encode("MD5",message);
	}
	/**
	 * ��ժҪ��Ϣת����SHA����
	 * @param message ժҪ��Ϣ
	 * @return SHA����֮����ַ�
	 */
	public static String shaEncode(String message){
		return Encode("SHA",message);
	}
	/**
	 * ��ժҪ��Ϣת����SHA-256����
	 * @param message ժҪ��Ϣ
	 * @return SHA-256����֮����ַ�
	 */
	public static String sha256Encode(String message){
		return Encode("SHA-256",message);
	}
	/**
	 * ��ժҪ��Ϣת����SHA-512����
	 * @param message ժҪ��Ϣ
	 * @return SHA-512����֮����ַ�
	 */
	public static String sha512Encode(String message){
		return Encode("SHA-512",message);
	}
	
	/**
	* 由相对路径获得 绝对路径、File、文件列表，此工具根据 ClassPath寻找路径 注意：本类不要加到jar包中，不然会失效
	* 在Servlet中获得文件真实路径 string file_real_path=getServletContext().getRealPath("/1.swf");
	*
	* @author KyLinD 2009-4-28 http://hi.baidu.com/kylind
	* @version 1.0
	*/
	/**
	* 根据相对路径获得 File
	*
	* @param relativePath
	* 工程下的以"./"开头，web下的以"/"开头，ClassPath下的直接相对路径
	* @return File
	*/
	public static File getFileByRelativePath(String relativePath) {
		return new File(getAbsolutePath(relativePath));
	}

	/**
	 * 根据相对路径获得绝对路径
	 *
	 * @param relativePath
	 *            工程下的以"./"开头，web下的以"/"开头，ClassPath下的直接相对路径
	 * @return 绝对路径字符串
	 */
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

	// 得到WebRoot目录的绝对地址
	private static String getAbsolutePathOfWebRoot() {
		String result = null;
		result = getAbsolutePathOfClassPath();
		result = result.replace(File.separatorChar + "WEB-INF"
				+ File.separatorChar + "classes", "");
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

	// 得到ClassPath的URL
	private static URL getURLOfClassPath() {
		return getClassLoader().getResource("");
	}

	// 得到类加载器
	private static ClassLoader getClassLoader() {
		return Utils.class.getClassLoader();
	}
	//JSON转换 简单对象
	// JSON转换为对象
	/**
	 * 获取泛型的Collection Type
	 * @param jsonStr json字符串
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses 元素类型
	 */
	@SuppressWarnings("deprecation")
	public static <T> T Json2Object(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) {
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		    return mapper.readValue(jsonStr, javaType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	// JSON转换为对象
	@SuppressWarnings("rawtypes")
	public static Object Json2Object(String json,TypeReference javaType) {
		ObjectMapper objectMapper = new ObjectMapper(); 
		try {
			return objectMapper.readValue(json, javaType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static String Object2Json(Object object) {  
		ObjectMapper objectMapper = new ObjectMapper(); 
		try {   
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);	        
	        //OutputStream stream=new ByteArrayOutputStream();  
	       	//JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
	        //jsonGenerator.writeObject(object);        	
	        //return stream.toString();
		    return objectMapper.writeValueAsString(object);  
		} catch (IOException e) {  
		   e.printStackTrace();  
		}
		return "";  
	}
}

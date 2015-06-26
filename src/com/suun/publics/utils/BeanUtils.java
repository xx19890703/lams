package com.suun.publics.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 反射的Utils函数集合.扩展自Apache Commons BeanUtils, 提供侵犯隐私的反射能�?
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	protected static Logger logger = (Logger) LoggerFactory.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛�?
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛�?
	 */

	@SuppressWarnings("rawtypes")
	public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定�?继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量�?忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛�?
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量�?忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛�?
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchMethodException 如果没有该Method时抛�?
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定�?继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}
	
	/** 
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型 
     * 如: public EmployeeDao extends BaseDao<Employee, String> 
     * @param clazz 
     * @param index 
     * @return 
     */  
    @SuppressWarnings({"rawtypes" })  
    public static Class getSuperClassGenricType(Class clazz, int index){  
        Type genType = clazz.getGenericSuperclass();  
          
        if(!(genType instanceof ParameterizedType)){  
            return Object.class;  
        }  
          
        Type [] params = ((ParameterizedType)genType).getActualTypeArguments();  
          
        if(index >= params.length || index < 0){  
            return Object.class;  
        }  
          
        if(!(params[index] instanceof Class)){  
            return Object.class;  
        }  
          
        return (Class) params[index];  
    }  
      
    /** 
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型 
     * 如: public EmployeeDao extends BaseDao<Employee, String> 
     * @param <T> 
     * @param clazz 
     * @return 
     */  
    @SuppressWarnings({"rawtypes" })  
    public static<T> Class<T> getSuperGenericType(Class clazz){  
        return getSuperClassGenricType(clazz, 0);  
    }	

}

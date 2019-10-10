package com.hu.tookit.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/**
 * 
 * 对象属性复制类
 * @ClassName: BeanCopyUtil
 * @author hlz
 * @date 2017年12月14日 上午11:57:25
 *
 */
public class BeanCopyUtil extends BeanUtils{

	
	/**
	* 
	* @param target 目标对象
	* @param source 源对象
	* @param ignoreNullFlag 是否忽略null属性值
	* @throws IllegalAccessException
	* @throws InvocationTargetException
	*/
	public static void copyProperties(Object source, Object target, boolean ignoreNullFlag) {
		if (ignoreNullFlag) {
			copyPropertiesIgnoreNull(source, target);
		} else {
			copyProperties(source, target);
		}
	}

	/**
	 * 
	 * 复制对象属性过滤Null
	 * @Title: copyPropertiesIgnoreNull
	 * @return void
	 * @param source
	 * @param target
	 * @throws BeansException
	 */
	public static void copyPropertiesIgnoreNull(Object source, Object target) throws BeansException {
	    Assert.notNull(source, "Source must not be null");
	    Assert.notNull(target, "Target must not be null");
	    Class<?> actualEditable = target.getClass();
	    PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
	    for (PropertyDescriptor targetPd : targetPds) {
	        if (targetPd.getWriteMethod() != null) {
	            PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
	            if (sourcePd != null && sourcePd.getReadMethod() != null) {
	                try {
	                    Method readMethod = sourcePd.getReadMethod();
	                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
	                        readMethod.setAccessible(true);
	                    }
	                    Object value = readMethod.invoke(source);
	                    // 这里判断以下value是否为空,为空则不复制对象属性
	                    if (value != null) {
	                        Method writeMethod = targetPd.getWriteMethod();
	                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
	                            writeMethod.setAccessible(true);
	                        }
	                        writeMethod.invoke(target, value);
	                    }
	                } catch (Throwable ex) {
	                    throw new FatalBeanException("Could not copy properties from source to target", ex);
	                }
	            }
	        }
	    }
	}

	/**
	 * 
	 * 批量复制对象属性，不忽略空值
	 * @Title: batchCopyProperties
	 * @return void
	 * @param sourceList
	 * @param targetList
	 */
	@SuppressWarnings("unchecked")
	public static void batchCopyProperties(List sourceList, List targetList) {
		AssertUtil.notNull(sourceList, "sourceList must not be null");
		AssertUtil.notEmpty(sourceList, "sourceList have no elements");
		for (Object source : sourceList) {
			Object obj = new Object();
			copyProperties(source, obj);
			targetList.add(obj);
		}
	}

	@SuppressWarnings("unchecked")
	public static void batchCopyProperties(List sourceList, List targetList, 
			Class<? extends Object> sourceClass, Class<? extends Object> targetClass) {
		AssertUtil.notNull(sourceList, "sourceList must not be null");
		AssertUtil.notEmpty(sourceList, "sourceList have no elements");
		AssertUtil.isEqual(sourceList.get(0).getClass(), sourceClass, "source 类型不匹配");
		AssertUtil.isEqual(targetList.get(0).getClass(), targetClass, "target 类型不匹配");
		for (Object source : sourceList) {
			Object obj = new Object();
			copyProperties(source, obj);
			targetList.add(obj);
		}
	}
}

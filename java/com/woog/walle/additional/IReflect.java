package com.woog.walle.additional;

import java.lang.reflect.Field;

public class IReflect {
	public static Object getField(Object obj, String name) {
		Class<?> targetClass = obj.getClass();
		Field f;
		try {
            f = targetClass.getDeclaredField(name);
            f.setAccessible(true);//访问私有必须调用
            return f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static void setField(Object obj, String name, Object objvalue) {
		Class<?> targetClass = obj.getClass();
		Field f;
		try {
			f = targetClass.getDeclaredField(name);
			f.setAccessible(true);
			f.set(targetClass, objvalue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

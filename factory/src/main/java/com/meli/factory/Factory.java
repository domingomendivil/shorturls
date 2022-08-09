package com.meli.factory;

import java.lang.reflect.Method;

public class Factory<A> {
	
	private A instance;
	
	private static Object getObject(String factoryClass,String factoryMethod) throws ConfigurationException {
		try {
			Class<?> aclass = Class.forName(factoryClass);
			Method method = aclass.getMethod(factoryMethod, null);
			return method.invoke(null, null);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| java.lang.reflect.InvocationTargetException | IllegalAccessException e) {
			throw new ConfigurationException(e);
		}
	}	
	
	public  synchronized A getInstance(String factoryClass) throws ConfigurationException {
		if (instance == null) {
			String factoryMethod = "getInstance";
			return (A)getObject(factoryClass,factoryMethod);
		}
		return instance;
	}
}

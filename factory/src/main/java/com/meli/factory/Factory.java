package com.meli.factory;

import java.lang.reflect.Method;

public class Factory<A> {
	
	private A instance;
	
	private static boolean valid(String string) {
		return (string !=null) && (!string.equals(""));
	}
	
	private static Object getObject(String factoryClass,String factoryMethod) throws ConfigurationException {
		if (valid(factoryMethod) && valid(factoryClass))
			try {
				Class<?> aclass = Class.forName(factoryClass);
				Method method = aclass.getMethod(factoryMethod, null);
				return method.invoke(null, null);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
					| java.lang.reflect.InvocationTargetException | IllegalAccessException e) {
				throw new ConfigurationException(e);
			}
		throw new ConfigurationException(String.format("Invalid configuration factoryClass %, factoryMethod %",factoryClass,factoryMethod));
	}	
	
	public  synchronized A getInstance(String factoryClass) throws ConfigurationException {
		if (instance == null) {
			String factoryMethod = "getInstance";
			return (A)getObject(factoryClass,factoryMethod);
		}
		return instance;
	}
}

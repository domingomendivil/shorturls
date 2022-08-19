package com.meli.factory;

import java.lang.reflect.Method;

/**
 * Factory class helper used for dynamically creating 
 * instances of different classes.
 * It's a typed class, where A is the type of class to be constructed
 */
public class Factory<A> {
	
	/**
	 * Instance represents the class instance to be created by the factory.
	 */
	private A instance;
	
	private static boolean valid(String string) {
		return (string !=null) && (!string.equals(""));
	}
	
	/**
	 * Creates and returns an instance of a class. The factoryclass 
	 * 
	 * @param factoryClass The factory class used to get the instance
	 * @param factoryMethod The method of the factory class used to get the instance
	 * @return A dynamically created object of type A
	 * @throws ConfigurationException
	 */
	private  A getObject(String factoryClass,String factoryMethod) throws ConfigurationException {
		if (valid(factoryMethod) && valid(factoryClass))
			try {
				Class<?> aclass = Class.forName(factoryClass);
				Method method = aclass.getMethod(factoryMethod, null);
				return (A)method.invoke(null, null);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
					| java.lang.reflect.InvocationTargetException | IllegalAccessException e) {
				throw new ConfigurationException(e);
			}
		throw new ConfigurationException(String.format("Invalid configuration factoryClass %, factoryMethod %",factoryClass,factoryMethod));
	}	
	
	public  synchronized A getInstance(String factoryClass) throws ConfigurationException {
		if (instance == null) {
			//gets an instance, where the method name of the factory class is 
			//called getInstance
			String factoryMethod = "getInstance";
			return getObject(factoryClass,factoryMethod);
		}
		return instance;
	}
}

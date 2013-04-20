package com.runecore.rsi.env;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class DynamicLoader {
	
	public static void loadJar(File jarFile) throws Exception {
		JarFile jar = new JarFile(jarFile);
        Enumeration<?> enumeration = jar.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = (JarEntry) enumeration.nextElement();
            if (entry.getName().endsWith(".class")) {
                ClassReader classReader = new ClassReader(jar.getInputStream(entry));
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                byte[] classData = classReader.b;
                loadClass(entry.getName().replaceAll(".class", "").replaceAll("/", "."), classData);
            }
        }
        jar.close();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private static void loadClass(String className, byte[] b) {
	    //override classDefine (as it is protected) and define the class.
	    Class clazz = null;
	    try {
	      ClassLoader loader = ClassLoader.getSystemClassLoader();
	      Class cls = Class.forName("java.lang.ClassLoader");
	      java.lang.reflect.Method method =
	        cls.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });

	      // protected method invocaton
	      method.setAccessible(true);
	      try {
	        Object[] args = new Object[] { className, b, new Integer(0), new Integer(b.length)};
	        clazz = (Class) method.invoke(loader, args);
	        System.out.println("[DynamicLoader]: Loaded "+className);
	      } finally {
	        method.setAccessible(false);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
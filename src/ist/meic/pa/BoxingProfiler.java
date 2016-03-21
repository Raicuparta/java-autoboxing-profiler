package ist.meic.pa;
import javassist.*;
import javassist.expr.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;


public class BoxingProfiler {
	public static void main (String[] args) throws Throwable {
		
		ClassPool cp = ClassPool.getDefault();
		CtClass sumInts = cp.getCtClass("ist.meic.pa.SumInts");
		
		Loader classLoader = new Loader();
		
		final String template = ""
				+ "{System.out.println(\"REPLACED\");"
				+ "$_ = $proceed($$);"
				+ "}";

		sumInts.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {
				if (m.getMethodName().equals("valueOf")) {
					System.out.println(m.getClassName());
					m.replace(template);
				}
			}
		});
		
		Class newSumInts = sumInts.toClass();
		
		Class[] argTypes = {String[].class};
		

		Method m = newSumInts.getMethod("mamain");
		m.invoke(null);
	}
	
	private static void test() {
		System.out.println("TEST");
	}
	
	private static void beforeTest() {
		System.out.println("Modified");
	}
	
	private static void beforeValueOf(Integer target, int n) {
		System.out.println("VALUE OF");
	}
	
	private static Integer replace(int n) {
		System.out.println("VALUE OF");
		return null;
	}
	
    private static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
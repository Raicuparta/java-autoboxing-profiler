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
				
		final String template = ""
				+ "{System.out.println(\"REPLACED\");"
				+ "$_ = $proceed($$);"
				+ "}";

		sumInts.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {
				System.out.println("Method: " + m.getMethodName());
				if (m.getMethodName().equals("longValue")) {
					System.out.println(m.getClassName());
					m.replace(template);
				}
			}
		});
		
		Class newSumInts = sumInts.toClass();
		Method m = newSumInts.getMethod("mamain");
		m.invoke(null);
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
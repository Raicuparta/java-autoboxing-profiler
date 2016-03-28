package ist.meic.pa;
import javassist.*;
import javassist.expr.*;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BoxingProfiler {
	public static void main(String[] args) throws Throwable {
		
		if(args.length == 0) {
			 System.err.println("No class given as argument");
			 return;
		}
		
		BPManager.init();

		ClassPool cp = ClassPool.getDefault();
		CtClass ctClass = cp.makeClass(new FileInputStream(args[0] + ".class"));

		final String template = "{"
				+ "ist.meic.pa.BPManager.add(\"%s\", \"%s\", %b);"
				+ "$_ = $proceed($$);"
				+ "}";

		ctClass.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {

				String className = m.getClassName();
				String methodName = m.getMethodName();
				String methodLongName = "";
				methodLongName = m.where().getLongName();
				
				//true if boxing, false if unboxing
				boolean isBoxing;

				if (BPManager.isBoxing(className, methodName)) isBoxing = true;
				else if (BPManager.isUnboxing(className, methodName)) isBoxing = false;
				else return;

				String formatted = String.format(template, className, methodLongName, isBoxing);
				m.replace(formatted);
			}
		});

		Class<?>[] argTypes = {String[].class};
		Class<?> newClass = ctClass.toClass();
		Method m = newClass.getMethod("main", argTypes);
		String[] mainArgString = Arrays.copyOfRange(args, 1, args.length);
		Object[] mainArgObj = {mainArgString};
		
		m.invoke(null, mainArgObj);

		BPManager.printProfile();
	}
}
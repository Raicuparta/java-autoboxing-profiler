package ist.meic.pa;
import javassist.*;
import javassist.expr.*;
import java.lang.reflect.Method;
import java.util.TreeMap;

public class BoxingProfiler {

	static String[] wrappers = {
		"Boolean",
		"Byte",
		"Character",
		"Float",
		"Integer",
		"Long",
		"Short",
		"Double"
	};
	
	static String[] primitives = {
		"boolean",
		"byte",
		"char",
		"float",
		"int",
		"long",
		"short",
		"double"
	};
	
	static TreeMap<OutputInfo, Integer> map;
	
	private static boolean isBoxing(String className, String methodName) {
		
		if (!isWrapper(className)) return false;
		
		if (methodName.equals("valueOf")) return true;
		
		return false;
	}
	
	private static boolean isUnboxing(String className, String methodName) {
		
		if (!isWrapper(className)) return false;
		
		for (int i = 0; i < primitives.length; i++) {
			if (methodName.equals(primitives[i] + "Value")) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isWrapper(String className) {
		for (int i = 0; i < wrappers.length; i++) {
			if (className.equals("java.lang." + wrappers[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void add (String className, String methodName, boolean isBoxing) {
		OutputInfo oi = new OutputInfo(className, methodName, isBoxing);
		int count = 1;
		if(map.containsKey(oi)) count = map.get(oi) + 1;
		map.put(oi, count);
	}
	
	public static void main (String[] args) throws Throwable {
		
		OutputComparator comparator = new OutputComparator();
		map = new TreeMap<OutputInfo, Integer>(comparator);
		
		ClassPool cp = ClassPool.getDefault();
		CtClass sumInts = cp.getCtClass(args[0]);
		
		final String template = "{"
				+ "ist.meic.pa.BoxingProfiler.add(\"%s\", \"%s\", %b);"
				+ "$_ = $proceed($$);"
				+ "}";
		
		sumInts.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {
				
				String className = m.getClassName();
				String methodName = m.getMethodName();
				String methodLongName = "";
				methodLongName = m.where().getLongName();
				boolean isBoxing;
				
				if (isBoxing(className, methodName)) isBoxing = true;
				else if (isUnboxing(className, methodName)) isBoxing = false;
				else return;
				
				String formatted = String.format(template, className, methodLongName, isBoxing);
				m.replace(formatted);
			}
		});
		
		Class<?>[] argTypes = {String[].class};
		Class<?> newSumInts = sumInts.toClass();
		Method m = newSumInts.getMethod("main", argTypes);
		Object[] mainArgs = {args};
		m.invoke(null, mainArgs);
		
		for (OutputInfo oi : map.keySet()) {
			System.out.println(oi.methodName + " " + (oi.isBoxing? "boxed " : "unboxed ") + map.get(oi) + " " + oi.className);
		}
	}
}
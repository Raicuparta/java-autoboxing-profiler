package ist.meic.pa;
import javassist.*;
import javassist.expr.*;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class BoxingProfiler {

	static String[] wrappers = {
		"Boolean",
		"Byte",
		"Character",
		"Float",
		"Integer",
		"Long",
		"Short",
		"Double",
		"String"
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
		int count = 0;
		if(map.containsKey(oi)) count = map.get(oi) + 1;
		map.put(oi, count);
		System.out.println("pila");
	}
	
	public static void main (String[] args) throws Throwable {
		
		OutputComparator comparator = new OutputComparator();
		map = new TreeMap(comparator);
		
		ClassPool cp = ClassPool.getDefault();
		CtClass sumInts = cp.getCtClass(args[0]);
		
		final String boxingTemplate = "{"
				+ "ist.meic.pa.BoxingProfiler.add(\"bb\", \"bb\", true);"
				+ "$_ = $proceed($$);"
				+ "}";
		
		final String unboxingTemplate = "{"
				+ "$_ = $proceed($$);"
				+ "}";
		
		sumInts.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {
				
				String className = m.getClassName();
				String methodName = m.getMethodName();
				
				//System.out.println("Method: " + methodName + ", " + "Class: " + className);
				
				if (isBoxing(className, methodName)) m.replace(boxingTemplate);
				
				else if (isUnboxing(className, methodName)) m.replace(unboxingTemplate);
			}
		});
		
		Class[] argTypes = {String[].class};
		Class newSumInts = sumInts.toClass();
		Method m = newSumInts.getMethod("main", argTypes);
		Object[] mainArgs = {args};
		m.invoke(null, mainArgs);
		
		for (OutputInfo oi : map.keySet()) {
			System.out.println("Class: " + oi.className +", Method: " + oi.methodName + ", Boxing: " + oi.isBoxing + ", Count: " + map.get(oi));
		}
	}
}
package ist.meic.pa;

import java.util.TreeMap;

public class BPManager {
	
	// we save a list of wrappers and primitive type names
	// so we can later compare them to the class and method names
	
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
	
	public static void init() {
		OutputComparator comparator = new OutputComparator();
		map = new TreeMap<OutputInfo, Integer>(comparator);
	}

	public static boolean isBoxing(String className, String methodName) {
		if (!isWrapper(className)) return false;

		if (methodName.equals("valueOf")) return true;
		return false;
	}

	public static boolean isUnboxing(String className, String methodName) {
		if (!isWrapper(className)) return false;

		for (int i = 0; i < primitives.length; i++) {
			boolean isValueMethod = methodName.equals(primitives[i] + "Value");
			boolean isCorrespondingWrapper = className.equals("java.lang." + wrappers[i]);
			
			if (isValueMethod && isCorrespondingWrapper) return true;
		}
		return false;
	}

	public static boolean isWrapper(String className) {
		for (int i = 0; i < wrappers.length; i++) {
			if (className.equals("java.lang." + wrappers[i])) return true;
		}
		return false;
	}

	public static void add(String className, String methodName, boolean isBoxing) {
		OutputInfo oi = new OutputInfo(className, methodName, isBoxing);
		int count = 1;
		if(map.containsKey(oi)) count = map.get(oi) + 1;
		map.put(oi, count);
	}

	public static void printProfile() {
		for (OutputInfo oi : map.keySet()) {
			String boxing = oi.isBoxing? "boxed " : "unboxed ";
			String count = map.get(oi).toString();
			System.err.println(oi.methodName + " " + boxing + count + " " + oi.className);
		}
	}
}

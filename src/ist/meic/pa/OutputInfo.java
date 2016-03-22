package ist.meic.pa;

public class OutputInfo {
	boolean isBoxing;
	String methodName;
	String className;
	
	public OutputInfo(String className, String methodName, boolean isBoxing) {
		this.isBoxing = isBoxing;
		this.methodName = methodName;
		this.className = className;
	}
}

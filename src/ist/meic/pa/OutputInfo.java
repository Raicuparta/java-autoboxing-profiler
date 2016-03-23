package ist.meic.pa;

/**
 * Simple class responsible for holding information about each auto boxing occurrence.
 */
public class OutputInfo {
	boolean isBoxing;
	String methodName;
	String className;
	
	
	/**
	 * @param isBoxing true if this is a boxing operation, false if unboxing.
	 */
	public OutputInfo(String className, String methodName, boolean isBoxing) {
		this.isBoxing = isBoxing;
		this.methodName = methodName;
		this.className = className;
	}
}

package ist.meic.pa;

import java.util.Comparator;

/**
 * Comparator for {@link OutputInfo}. Orders first by methodName, then className, then isBoxing (true first).
 */
public class OutputComparator implements Comparator<OutputInfo> {

	@Override
	public int compare(OutputInfo arg0, OutputInfo arg1) {
		int methodCompare = arg0.methodName.compareTo(arg1.methodName);
		if (methodCompare != 0) return methodCompare;
		
		int classCompare = arg0.className.compareTo(arg1.className);
		if (classCompare != 0) return classCompare;
		
		if (arg0.isBoxing && !arg1.isBoxing) return -1;
		else if (!arg0.isBoxing && arg1.isBoxing) return 1;

		return 0;
	}
}

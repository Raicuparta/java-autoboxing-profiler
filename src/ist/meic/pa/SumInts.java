package ist.meic.pa;

public class SumInts {
	private static long sumOfIntegerUptoN(int n) {
		Long sum = 0L;
		for (int i = 0; i < n; i++) {
			sum += i;
		}
		return sum;
	}
	
	private static long printSum(long n) {
		System.out.println("Sum: " + n);
		return n;
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		printSum(sumOfIntegerUptoN(10));
		//System.out.println( "INTEGER: " + Integer.valueOf(5));
		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end - start));
	}
}
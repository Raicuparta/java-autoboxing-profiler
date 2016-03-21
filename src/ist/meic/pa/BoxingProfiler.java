package ist.meic.pa;
import javassist.*;
import javassist.expr.*;

import java.lang.reflect.Method;
import java.util.Arrays;


public class BoxingProfiler {
	public static void main (String[] args) throws Throwable {
		
		/*String name = args[0];
		String[] arguments = Arrays.copyOfRange(args, 1, args.length);*/
		

			ClassPool cp = ClassPool.getDefault();
			CtClass integer = cp.getCtClass("java.lang.Integer");
			//CodeConverter conv = new CodeConverter();
			//Loader classLoader = new Loader();
			//classLoader.addTranslator(cp, t);
			
			CtClass bp = cp.getCtClass("ist.meic.pa.BoxingProfiler");
			//CtClass[] argInt = {cp.get("int")};
			//CtMethod original = bp.getDeclaredMethod("test");
			
			
			//CtMethod original = bp.getDeclaredMethod("valueOf",);
			
			//CtMethod before = bp.getDeclaredMethod("beforeTest");
			//CtMethod replace = bp.getDeclaredMethod("beforeTest");
			

			Translator translator = new BoxingTranslator();
			ClassPool pool = ClassPool.getDefault();
			Loader classLoader = new Loader();
			classLoader.addTranslator(pool, translator);
			//classLoader.run("java.lang.Integer", null);
			classLoader.run("ist.meic.pa.BoxingTranslator", args);
			
			BoxingTranslator bt = (BoxingTranslator) translator;
			bt.test();
			
			
			/*integer.writeFile();
			integer.defrost();*/
						
			
			/*Class[] arg = {int.class};
			Method m = i.getClass().getMethod("valueOf", arg);
			Object[] val = {10};
			Integer result = (Integer) m.invoke(i, val);
			System.out.println("Integer: " + result);*/
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
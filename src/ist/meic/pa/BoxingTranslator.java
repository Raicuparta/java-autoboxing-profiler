package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BoxingTranslator implements Translator {
	@Override
	public void start(ClassPool arg0) throws NotFoundException, CannotCompileException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		CtClass ctClass = pool.get(className);
		if (ctClass.getName().equals("ist.meic.pa.BoxingTranslator")) {
			detectBoxing(ctClass);
		}
	}
	
	private void detectBoxing(CtClass ctClass) throws NotFoundException, CannotCompileException {
		final String template = "{"
				+ "System.out.println(\"REPLACED\");"
				+ "$_ = $proceed($$) + 10;"
				+ "}";
		
		System.out.println("Class:" + ctClass.getName());
		
		CtMethod ctm = ctClass.getDeclaredMethod("test");

		
		ctm.instrument(new ExprEditor() {
			public void edit(MethodCall m) throws CannotCompileException {
				m.replace(template);
//System.out.println("m.getClass " + m.getClassName() + " m.getMethodName " + m.getMethodName());
		        
		     }
		 });
	}
	
	public static void test() {
		System.out.println("TEST TRANSLATOR");
	}
	
	public static void main (String[] args) {
		test();
	}
}

package hu.mapro.sandbox.valueproxy;


public class MyService {

	public static A produce() {
		A a1= new A();
		a1.setValueA("value1");
		A a2 = new A();
		a2.setValueA("value2");
		a1.setRef(a2);
		A a3 = new A();
		a3.setValueA("value3");
		a2.setRef(a3);
		System.out.println("Server: sending: " + dump(a1));
		return a1;
	}
	
	public static void process(A a) {
		System.out.println("Server: received: " + dump(a));
	}
	
	public static String dump(A a) {
		if (a!=null) {
			return a.getValueA() + "," + dump(a.getRef());
		}
		return "";
	}
	
	
	
}

package test;

public class NumTest {

	public static void main(String[] args) {
		double num=5.5;
		System.out.println(num);
		String ss="628";
		int bb=Integer.valueOf(ss);
		System.out.println(bb);
		
		int h=bb/60;
		int m=bb%60;
		
		System.out.println(h+","+m);
		
	}

}

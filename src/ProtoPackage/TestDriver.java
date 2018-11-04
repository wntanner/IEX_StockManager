package ProtoPackage;

public class TestDriver {

	public static void main(String[] args) {
		// // TODO Auto-generated method stub
		 SignMediator sign = new SignMediator();
		 sign.connectToSign();
		 System.out.println(" done");

//		System.out.println(kickPalin("hello"));

	}

	public static String kickPalin(String init) {
		return otherPalin(init, init.length()-1, "");
//		return palin(init, init.length() - 1, "");
	}
	
	public static String otherPalin(String init, int i, String ans) {
		if(i == -1) {
			return ans;
		}
		
		String cur = "";
		cur = init.substring(i, i+1);
		ans += cur;
		
		i--;
		return otherPalin(init, i, ans);
	}

	public static String palin(String init, int i, String ans) {

		char[] array = init.toCharArray();

		if (i == -1) {
			return ans;
		}
		
		ans += Character.toString(array[i]);
		i--;
		
		return palin(init, i, ans);

	}

}

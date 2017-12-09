
import java.util.*;


public class my_language {
	HashMap<String, Integer> map = new HashMap<>();
	boolean flag = false;
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		my_language program = new my_language();
        while(true){
        	try {
                String s = in.nextLine();
		String[] ass = s.split("(?<=;)");
                for(String as: ass){
                	program.assignment(as);
                }
            }catch(Exception e){
            	break;
            }
        }
        in.close();
	}
	
	public void assignment(String s){
		s = s.replaceAll("\\s+", "");
		
		if(s.length() <= 1){
			System.out.println("Syntax error: no assignments");
			return;
		}
		if(s.indexOf("=") < 0 || s.indexOf("=") != s.lastIndexOf("=")){
			System.out.println("Syntax error: no assignments");
			return;
		}
		if(s.indexOf(";") != s.length() - 1){
			System.out.println("Syntax error: \";\" expected");
			return;
		}
		int indexOfEq = s.indexOf("=");
		String left = s.substring(0, indexOfEq);
		String right = s.substring(indexOfEq + 1, s.length() - 1);
		
		if(!isIdentifier(left) || !isExp(right)){
			System.out.println("Syntax error");
			return;
		}
		
		flag = false;
		int ans = computeExp(right);
		if(flag){
			System.out.println("error: undinitialized variable(s)");
			return;
		}
		map.put(left, ans);
		
		System.out.println(left + " = " + ans);
		
		return;
	}
	
	public boolean isExp(String s){
		if(isTerm(s)){
			return true;
		}
		for(int i = s.length() - 1; i >= 0; i --){
			if(s.charAt(i) == '+' || s.charAt(i) == '-'){
				String left = s.substring(0, i);
				String right = s.substring(i + 1);
				if(isExp(left) && isTerm(right)){
					return true;
				}
			}
		}
		return false;
	}
	public int computeExp(String s){
		if(isTerm(s)){
			return computeTerm(s);
		}
		for(int i = s.length() - 1; i >= 0; i --){
			if(s.charAt(i) == '+' || s.charAt(i) == '-'){
				String left = s.substring(0, i);
				String right = s.substring(i + 1);
				if(isExp(left) && isTerm(right)){
					int exp = computeExp(left);
					int term = computeTerm(right);
					if(s.charAt(i) == '+'){
						return exp + term;
					}else{
						return exp - term;
					}
				}
			}
		}
		return 0;
	}
	
	public boolean isTerm(String s){
		if(s.indexOf("*") < 0){
			return isFact(s);
		}else{
			int index = s.lastIndexOf("*");
			return isTerm(s.substring(0, index)) && isFact(s.substring(index + 1));
		}
	}
	public int computeTerm(String s){
		if(s.indexOf("*") < 0){
			return computeFact(s);
		}else{
			int index = s.lastIndexOf("*");
			return computeTerm(s.substring(0, index)) * computeFact(s.substring(index + 1));
		}
	}
	
	public boolean isFact(String s){
		if(s.length() <= 0){
			return false;
		}
		if(isLiteral(s) || isIdentifier(s)){
			return true;
		}
		if((s.charAt(0) == '-' || s.charAt(0) == '+') && isFact(s.substring(1))){
			return true;
		}
		if(s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')' && isExp(s.substring(1, s.length() - 1))){
			return true;
		}
		return false;
	}
	public int computeFact(String s){
		if(isLiteral(s)){
			return computeLiteral(s);
		}
		if(isIdentifier(s)){
			return computeIdentifier(s);
		}
		if((s.charAt(0) == '-' || s.charAt(0) == '+') && isFact(s.substring(1))){
			if(s.charAt(0) == '+'){
				return computeFact(s.substring(1));
			}else{
				return 0 - computeFact(s.substring(1));
			}
		}
		if(s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')' && isExp(s.substring(1, s.length() - 1))){
			return computeExp(s.substring(1, s.length() - 1));
		}
		return 0;
	}
	
	public boolean isIdentifier(String s){
		if(s.length() <= 0 || !isLetter(s.charAt(0))){
			return false;
		}
		for(int i = 1; i < s.length(); i ++){
			if(!isLetter(s.charAt(i)) && !isDigit(s.charAt(i))){
				return false;
			}
		}
		return true;
	}
	public int computeIdentifier(String s){
		if(map.containsKey(s)){
			return map.get(s);
		}else{
			flag = true;
			return 0;
		}
	}
	
	
	public boolean isLetter(char c){
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDigit(char c){
		return (c >= '0' && c <= '9');
	}
	public boolean isNonZeroDigit(char c){
		return (c >= '1' && c <= '9');
	}
	
	public boolean isLiteral(String s){
		if(s.length() <= 0){
			return false;
		}
		if(s.length() == 1 && s.charAt(0) == '0'){
			return true;
		}
		if(!isNonZeroDigit(s.charAt(0))){
			return false;
		}
		for(int i = 1; i < s.length(); i ++){
			if(!isDigit(s.charAt(i))){
				return false;
			}
		}
		return true;
	}
	public int computeLiteral(String s){
		return Integer.parseInt(s);
	}
	
}

package application;

import java.util.ArrayList;
import java.util.Stack;

public class NormalFormula{

  

	static Stack<String> calcStack = new Stack<String>();
	static char[] opCode = {'+', '-', '*', '/', '(', ')'};
	
	private String str;
	
	public static String Calc(String str) {
	        
	        int index = 0;
	        
	        ArrayList<String> postfixList = new ArrayList<String>();
	        Stack<Character> opStack = new Stack<Character>();
	        

	        for (int i = 0; i < str.length(); i++) {
	            for (int j = 0; j < 6; j++) {

	                if (str.charAt(i) == opCode[j]) {
	                    postfixList.add(str.substring(index, i));

	                    if (opStack.isEmpty()) { 
	                        opStack.push(opCode[j]);
	                      
	                    } else {
	                    	
	                    	 if (operationCodeOrder(opCode[j]) > operationCodeOrder(opStack.peek())
	                    			 || opCode[j]=='('){  //peek : top�� ������ ����
	                    
	                    		 opStack.push(opCode[j]);
	              
	                            }
	                    	 else if (operationCodeOrder(opCode[j]) <= operationCodeOrder(opStack.peek())) {
	                            if(opCode[j]==')'){
	                            	if(opStack.peek() != '('){
	                            	postfixList.add(opStack.peek().toString());
	                            	opStack.pop();
	                            	opStack.pop();
	                            	
	                            	}
	                            	else{
	                            		opStack.pop();
	                            		postfixList.add(opStack.toString());
	                            	}
	                            	
	                            }
	                            else{
	                            	postfixList.add(opStack.peek().toString());
	                                opStack.pop();
	                                opStack.push(opCode[j]);//���� �켱������ �����ȣ opStack�� ����
	                            }
	                    	 }
	                    }
	                    index = i + 1;
	                }
	            }
	        }
	        postfixList.add(str.substring(index, str.length())); //������ ���� ó��(�������� �����ȣ �����ϱ�)

	        if (!opStack.isEmpty()) {//opStack�� ���� ������ postfixList�� �ٿ��ֱ�
	            for (int i = 0; i < opStack.size();) {
	                postfixList.add(opStack.peek().toString());
	                opStack.pop();
	            }
	        }
	        for (int i = 0; i < postfixList.size(); i++) {

	            if (postfixList.get(i).equals("")) {

	                postfixList.remove(i);

	                i = i - 1;

	            } 
	        }
	       
	       
	        opStack.clear(); //opStack ����
	        return Calculator(postfixList);
	    }
	        public static String Calculator(ArrayList<String> list){
	        	for (int i = 0; i < list.size(); i++) {

	            calcStack.push(list.get(i));
	            for (int j = 0; j < 6; j++) {
	                if (list.get(i).charAt(0) == opCode[j]) {

	                    calcStack.pop(); 
	                    double a, b; 
	                    String s; //a�� b ������ �ٽ� CalcStack�� ���� ����
	                    
	                    a = Double.parseDouble(calcStack.pop());
	                    b = Double.parseDouble(calcStack.pop());
	                    
	                    switch (opCode[j]) {
	                        case '+':
	                            s = String.valueOf(a + b);
	                            calcStack.push(s);
	                            break;

	                        case '-':
	                            s = String.valueOf(b - a);
	                            calcStack.push(s);
	                            break;

	                        case '*':
	                            s = String.valueOf(a * b);
	                            calcStack.push(s);
	                            break;

	                        case '/':
	                            s = String.valueOf(b / a);
	                            calcStack.push(s);
	                            break;
	                            
	                        case '(':
	                        case ')':
	                        	break;

	                    }

	                }

	            }

	        }
	        double r = Double.parseDouble(calcStack.peek()); //calcStack�� Top ������ �б� 
	        String result = String.format("%.1f", r);

	      
	        return result;
	    }
	    

	    public static int operationCodeOrder(char op) {

	        switch (op) {
	        	
	        	case '(':
	        	case ')':
	        		return 0;

	            case '+':
	            case '-':
	                return 1;

	            case '*':
	            case '/':
	                return 2;

	            default:
	                return -1;
	        }
	    }
	}


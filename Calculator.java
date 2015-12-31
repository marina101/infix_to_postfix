import java.util.LinkedList;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Deque;
import java.util.Scanner;


/**
 * This program asks the user for a mathematical expression written in infix notation, then uses the
 * InfixEvaluator algorithm to convert it to its equivalent postfix expression, and to then evaluate the 
 * resulting postfix expression and display the result. Each digit in the expression must be an integer (0-9), a binary 
 * operator, or a left or right parentheses. The program will throw an error if the parentheses are not properly
 * nested/balanced or if some other kind of digit is entered.
 * @author Marina Chirchikova
 *
 */
public class Calculator {
	
	private String expression;
	private static Deque<Integer> valueStack; 
	private static Deque<Character> opStack;
	private static Queue<String> que;
	private static char[] check = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '*', '/', '%', '(', ')'};
	
	/**
	 * Constructor, sets up stacks and queues necessary for the calculations
	 * @param infix - the mathematical expression to be calculated
	 * @throws Exception in case an illegal character is inputted into the expression
	 */
	public Calculator(String infix) throws Exception{
		expression = infix;
		que = Calculator.Tokenize(infix);  //reinitialized each time there is a new input
		valueStack = new ArrayDeque<Integer>(); //reinitialized each time there is a new input
		opStack = new ArrayDeque<Character>();  //reinitialized each time there is a new input
	}
	
	//tokenizes the infix expression into a queue of string tokens 
	//(operands, operator and parentheses)
	//pre-condition: each operand must be a single digit
	//pre-condition: the following operators are permitted: + - * / %
	
	private static Queue<String> Tokenize(String infixExpression) throws Exception{
		Queue<String> queue = new LinkedList<String>();
		int length = infixExpression.length();
		int pos = 0;
		while(pos<length){
			
			//check each token to make sure its one of the allowed ones by cross-referencing with "check" array of allowed characters
			boolean match = false;
			
			for(int i = 0; i<check.length; i++){
				if(infixExpression.charAt(pos) == check[i]){
					match = true;
					//System.out.println("Match status :" + match + "char is " + infixExpression.charAt(pos));
				}
			}
			if(!match)
				throw new Exception(" Error: illegal character inputted");
				
			//if the token is valid, it is added to the queue
			queue.add(String.valueOf(infixExpression.charAt(pos)));
			pos++;
			
		}//while
		
		
		return queue;
	}
	
	
	//receives a queue of infix string tokens, evaluates the infix expression
	//and returns the result
	private static int evaluateInfix(Queue<String> infixTokenQueue) throws Exception{
		
		String token = null; 
		while(infixTokenQueue.peek() != null){
			//gets the next token from infixQueue
			token = infixTokenQueue.remove();
			
			
			//if token is a number/operand
			if(Calculator.isInteger(token)){
				valueStack.push(Integer.parseInt(token));
			}
				
			
			//if token is a left parenthesis
			else if(token.equals("(")){
				opStack.push(token.charAt(0));
				
			}
			
			//if token is a right parenthesis
			else if(token.equals(")")){
				if(opStack.peek() == null)
					throw new Exception("There is an error with the amount or balance of parentheses in the expression");
				
				//checks that opStack is not empty and its top element is not a left parenthesis
				while((opStack.peek() != null) && (! (String.valueOf( (opStack.peek())).equals("(") )))    {
					Calculator.applyOperation();
				}
				
				opStack.pop();
				
			}
			
			//if token is an operator
			else {
				
				//checks that opStack is not empty and the token's precedence is less than or equal to that of the operator at the top of op Stack
				while((opStack.peek() != null) && (Calculator.precedence(token) <= Calculator.precedence(String.valueOf(opStack.peek() ) ) ) ) {
					Calculator.applyOperation();
				}
				opStack.push(token.charAt(0));
			}
			
		}
		
		//as long as there are still remaining operations
		
		
		while(!(opStack.isEmpty())){
			if((String.valueOf(opStack.peek())).equals("("))
				throw new Exception("There are too many '(' in this expression");
			Calculator.applyOperation();
		}
		
		
		return valueStack.pop();
	}//evaluate method
	
	//pops top two values and top operation off the stacks and calculates v1 operator v2, then pushes result to value stack
	private static void applyOperation(){
		char operator = opStack.pop();
		int v2 = valueStack.pop();
		int v1 = valueStack.pop();
		int result;
		
		switch(operator){
		case '+':
			result = v1+v2;
			valueStack.push(result);
			break;
		case '-':
			result = v1-v2;
			valueStack.push(result);
			break;
		case '*':
			result = v1*v2;
			valueStack.push(result);
			break;
		case '/':
			result = v1/v2;
			valueStack.push(result);
			break;
		case '%':
			result = v1%v2;
			valueStack.push(result);
			break;
		}
	}
	
	//returns the precedence of the operator
	private static int precedence(String op){
		if((op.equals("*"))||(op.equals("/")))
			return 2;
		else if(op.equals("%"))
			return 2;
		else if(op.equals("+")||(op.equals("-")))
			return 1;
		else
			return 0;
			
	}
	


	//method to check if string is an integer
	private static boolean isInteger(String in) {
		try {
			Integer.parseInt(in);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	
	/**
	 * Returns the mathematical expression that was inputed by user
	 * @return the math expression inputed by user
	 */
	public String getInfix(){
		return expression;
	}
	
	/**
	 * Uses the infix algorithm and stacks and queues to calculate the result of the mathematical
	 * expression inputed by the user,
	 * @return the result of the expression
	 * @throws Exception if the number of parentheses are not balanced or properly nested
	 */
	public int eval() throws Exception{
		return evaluateInfix(que);
	}
	

	//main driver method
	public static void main(String[] args){	
		try
		{
		Scanner keyboardScan = new Scanner(System.in);		
		System.out.print("Enter an infix expression with integer operands and only binary operators (/%*+-): ");
		String infix = keyboardScan.nextLine();
		Calculator calc = new Calculator(infix);
		System.out.println(calc.getInfix() + " = " + calc.eval());
		keyboardScan.close();
		}
		

		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}//main method

}

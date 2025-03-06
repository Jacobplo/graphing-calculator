/**

* StringEvaluation.java

* Contains methods for converting an expression from infix to postfix notation, and evaluating an expression in postfix notation.

* @author Jacob Plourde

*/
package calculator;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Stack;
import java.util.Queue;
import java.util.Scanner;
import java.util.LinkedList;
import java.lang.Math;

public class StringEvaluation {
	// Uses a shunting-yard algorithm to convert an input mathematical expression in infix notation to postfix notation.
	public static Queue<String> InfixToPostfix(String expression) {		
		// Initializes the operator stack, output queue, and token list created from the infix expression.
		Stack<String> operatorStack = new Stack<String>();
		Queue<String> outputQueue = new LinkedList<>();
		StringTokenizer tokens = new StringTokenizer(expression);
		
		
		// Shunting-yard algorithm. Loops until all elements of the infix expression have been analyzed.
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			
			// If the next token is a...
			
			// Number.
			if(isDouble(token) || token.equals("x")) {
				outputQueue.add(token);	
			}
			
			// Constant.
			else if(Constants.constants.containsKey(token)){
				outputQueue.add(Double.toString(Constants.constants.get(token)));
			}
			
			// Rand.
			else if(token.equals("rand")) {
				outputQueue.add("rand");
			}
			
			// Function.
			else if(Constants.functions.contains(token)) {
				operatorStack.add(token);
			}
			
			// Operator.
			else if(Constants.operators.contains(token)){
				while((Constants.operators.contains(getTop(operatorStack)) && !getTop(operatorStack).equals("(")) && (getPrecedence(getTop(operatorStack)) > getPrecedence(token) || (getPrecedence(getTop(operatorStack)) == getPrecedence(token) && getAssociativity(token).equals("Left")))) {
					outputQueue.add(operatorStack.pop());
				}
				operatorStack.add(token);
			}
			
			// Left parenthesis.
			else if(token.equals("(")) {
				operatorStack.add(token);
			}
			
			// Right parenthesis.
			else if(token.equals(")")) {
				while(!(getTop(operatorStack).equals("("))) {
					assert !operatorStack.isEmpty() : "Mismatched parentheses";
					outputQueue.add(operatorStack.pop());
				}
				assert getTop(operatorStack).equals("(") : "Mistmatched parentheses";
				operatorStack.pop();
				if(Constants.functions.contains(getTop(operatorStack))) {
					outputQueue.add(operatorStack.pop());
				}
			}
		}// End expression analysis loop.
		
		// Adds all of the operator stack items to the output queue.
		while(!operatorStack.isEmpty()) {
			assert !getTop(operatorStack).equals("(") : "Mismatched parentheses";
			outputQueue.add(operatorStack.pop());
		}
		
		return outputQueue;
		
	}// End infixToPostfix().
	
	// Evaluates an input expression in postfix notation.
	public static double evaluate(Queue<String> postfixQueue, double xValue) {
		// Creates an operand stack which queue up operands to be operated on.
		Stack<Double> operandStack = new Stack<Double>();
		int queueSize = postfixQueue.size();
		// Interates through the postfixQueue until all elements have been analyzed.
		for(int i = 0; i < queueSize; ++i) {
			// If the next element of the postfixQueue is a number, push it to the operand stack.
			if(isDouble(postfixQueue.peek())) {
				operandStack.push(Double.parseDouble(checkForX(postfixQueue.remove(), xValue)));
			}
			
			// Deals with "rand" input, which is a dynamic value.
			else if(postfixQueue.peek().equals("rand")) {
				operandStack.push(Math.random());
				postfixQueue.remove();
			}
			// If the next element of the postfixQueue is a function, apply the function to the top element of the operand stack.
			else if(Constants.functions.contains(postfixQueue.peek())) {
				switch(postfixQueue.remove()) {
					case "sin":
						operandStack.push(Math.sin(operandStack.pop()));
						break;
					case "cos":
						operandStack.push(Math.cos(operandStack.pop()));
						break;
					case "tan":
						operandStack.push(Math.tan(operandStack.pop()));
						break;
					case "abs":
						operandStack.push(Math.abs(operandStack.pop()));
						break;
					case "sqrt":
						operandStack.push(Math.sqrt(operandStack.pop()));
						break;
					case "ln":
						operandStack.push(Math.log(operandStack.pop()));
						break;
					case "floor":
						operandStack.push(Math.floor(operandStack.pop()));
						break;
					case "ceil":
						operandStack.push(Math.ceil(operandStack.pop()));
						break;
				}
			}
			// If the next element of the postfixQueue is a operation, apply the operator to the top two elements of the operand stack, and push the result back onto the stack.
			else {
				double operandOne = operandStack.pop();
				double operandTwo = operandStack.pop();
				switch(postfixQueue.remove()) {
					case "+":
						operandStack.push(operandTwo + operandOne);
						break;
					case "-":
						operandStack.push(operandTwo - operandOne);
						break;
					case "*":
						operandStack.push(operandTwo * operandOne);
						break;
					case "/":
						operandStack.push(operandTwo / operandOne);
						break;
					case "^":
						operandStack.push(Math.pow(operandTwo, operandOne));
						break;
				}
			}
		}

		return operandStack.pop();
	}// End evaluate().
	
	// Checks if an input string is a double once parsed.
	private static boolean isDouble(String token) {
		Scanner scanner = new Scanner(token.trim());
		if(token.equals("x")) return true;
		if(!scanner.hasNextDouble()) return false;
		scanner.nextDouble();
		return !scanner.hasNext();
	}// End isDouble().
	
	// Gets the object at the top of an input stack.
	private static String getTop(Stack<String> stack) {
		try {
			return stack.peek();
		}
		catch(Exception e) {
			return null;
		}
	}// End getTop().
	
	// Gets the precedence of an input operator.
	private static int getPrecedence(String operator) {
		for(int i = 0; i < Constants.operatorData.size(); i++) {
			if(operator.equals(Constants.operatorData.get(i).getOperator())) {
				return Constants.operatorData.get(i).getPrecedence();
			}
		}
		return 0;
	}// End getPrecedence().
	
	// Gets the left/right associativity of an input operator.
	private static String getAssociativity(String operator) {
		for(int i = 0; i < Constants.operatorData.size(); i++) {
			if(operator.equals(Constants.operatorData.get(i).getOperator())) {
				return Constants.operatorData.get(i).getAssociativity();
			}
		}
		return null;
	}// End getAssociativity().
	
	// Checks if an input token is an x, and replaces it with the current x value for the graph.
	private static String checkForX(String element, double xValue) {
		if(element.equals("x")) {
			return Double.toString(xValue);
		}
		return element;
	}// End checkForX().

	// Returns operatorData.
	public static List<Operator> getOperatorData() {
		return Constants.operatorData;
	}// End getFunctions().

	// Returns functions.
	public static List<String> getFunctions() {
		return Constants.functions;
	}// End getFunctions().
}// End StringEvaluation.

// Stores information for an operator.
class Operator {
	
	private final String operator;
	private final int precedence;
	private final String associativity;
	
	// Constructor.
	public Operator(String operator, int precedence, String associativity) {
        this.operator = operator;
        this.precedence = precedence;
        this.associativity = associativity;
    }// End Operator().
	
	// Returns operator.
	public String getOperator() {
		return operator; 
	}// End getOperator().
	
	// Returns precedence.
    public int getPrecedence() {
    	return precedence; 
    }// End getPrecedence().
    
    // Returns associativity.
    public String getAssociativity() { 
    	return associativity;
    }// End getAssociativity().
}// End Operator.

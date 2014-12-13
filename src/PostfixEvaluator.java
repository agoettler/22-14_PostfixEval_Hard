
/*
 * Introduction to Software Design 2014
 * Author: Andrew Goettler
 * Problem: 22.14 Postfix Evaluator Modification
 * Problem difficulty: Hard
 * Problem description: Modify the postfix evaluator program of Exercise 22.13
 * 		so that it can process integer operands larger than 9.
 */

import java.util.Stack;

/**
 * A class for evaluating integer arithmetic expressions in postfix or "reverse Polish" notation.
 * 
 * @author agoettler
 *
 */
public class PostfixEvaluator
{	
	/**
	 * A stack used for performing the postfix evaluation.
	 */
	private Stack<Integer> operandStack = new Stack<Integer>();
	
	/**
	 * This method evaluates an integer arithmetic expression in postfix notation.
	 * Expressions must be in the format " operandB operandA operator".
	 * Expressions can only contain numerals, spaces and the characters "+", "-", "*", "/", "^", "%".
	 * Invalid characters and invalid operations such as division by zero will yield a result of zero
	 * accompanied by an exception and stack trace printed to System.err, and a warning message printed
	 * to System.out.
	 * 
	 * @param expression A StringBuffer object representing the expression.
	 * @return The integer result of the evaluated expression.
	 */
	public int evaluatePostfixExpression(StringBuffer expression)
	{
		try
		{
			// trim the expression and append the right parenthesis with a space for tokenizing
			expression.trimToSize();
			expression.append( " )" );
			
			// tokenize the expression
			String[] expressionTokens = tokenizeExpression(expression.toString());
			
			// initialize some useful variables
			int result = 0;
			int index = 0;
			int operandX = 0;
			int operandY = 0;
			String currentToken;
			
			// continue operating until the right parenthesis is encountered
			do
			{
				// get the current expression token
				currentToken = expressionTokens[index];
				
				// if the token at the current index is a number, push its integer equivalent onto the stack
				if(currentToken.matches("\\d+"))
				{
					operandStack.push(Integer.parseInt(currentToken));
				}
				
				// if the character at the current index is an operator, pop values off the stack and perform a calculation
				else if(currentToken.matches("[+-/*%^]"))
				{
					// if the stack is empty, use zero for an operand
					if(!operandStack.isEmpty())
					{
						operandX = operandStack.pop();
					}
					else
					{
						operandX = 0;
					}
					
					// if the stack is empty, use zero for an operand
					// this allows the negation operation to be performed correctly
					if(!operandStack.isEmpty())
					{
						operandY = operandStack.pop();
					}
					else
					{
						operandY = 0;
					}
					
					// perform the calculation and push the result onto the stack
					operandStack.push(calculate(operandY, currentToken, operandX));
				}
				
				// if a token isn't a number, an operator, or a parenthesis, the expression is invalid
				else if( !currentToken.matches( "[)]" ))
				{
					throw new NumberFormatException(String.format("Value is not a number or operator: %s", currentToken));
				}
				
				// increment the index, moving through the array of string tokens from left to right
				index++;
				
			} while(!currentToken.equals(")"));
			
			if(!operandStack.isEmpty())
			{
				result = operandStack.pop();
			}
			else
			{
				result = 0;
			}
			
			return result;
			
		} 
		
		catch (NumberFormatException numberFormatException)
		{
			// catch NumberFormatExceptions and report to the user
			numberFormatException.printStackTrace();
			System.err.printf("\nException: %s \n", numberFormatException);
			System.out.println("The expression contained an invalid operand or operator.");
			return 0;
		} 
		
		catch (ArithmeticException arithmeticException)
		{
			// catch ArithmeticExceptions and report to the user
			arithmeticException.printStackTrace();
			System.err.printf("\nException: %s \n", arithmeticException);
			System.out.println("The expression contained an invalid operation.");
			return 0;
		}
	}
	
	/**
	 * This private helper method performs calculations for the evaluatePostfixExpression method.
	 * Invalid operations, such as division by zero, will cause an ArithmeticException to be thrown.
	 * Calculations are of the form: "operandA operator operandB"
	 * 
	 * @param operandA The first operand.
	 * @param operator The operation to be performed
	 * @param operandB The second operand.
	 * @return
	 */
	private int calculate(int operandA, String operator, int operandB) throws ArithmeticException
	{	
		switch (operator)
		{
			case "+":	return operandA + operandB;
						
			case "-":	return operandA - operandB;
			
			case "*":	return operandA * operandB;
			
			case "/":	return operandA / operandB;
			
			case "^":	return (int) Math.pow(operandA, operandB); // cast returned double as int
			
			case "%":	return (int) operandA % operandB; // cast returned float as int
			
			default:	return 0;
		}
	}
	
	/**
	 * This private helper method separates the expression into space-separated tokens to 
	 * allow the evaluatePostFixExpression to process integer operands greater than 9.
	 * 
	 * @param expression The expression to be tokenized.
	 * @return A String array of the tokens from the expressions.
	 */
	private String[] tokenizeExpression(String expression)
	{
		// split the expression into tokens separated by spaces
		// this allows for integers greater than 9 to be processed
		return expression.split(" ");
	}
}

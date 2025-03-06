/**

* Keyboard.java

* Class that handles necessary key events for a graphing calculator.

* @author Jacob Plourde

*/
package calculator;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Keyboard implements KeyListener{
	private JFrame frame;
	private JTextField coordinate;
	
	private static int cursor = 0;
	private static int functionIndex = 0;
	
	private static boolean intersect = false;
	private static Object[] intersection = new Object[3];
	
	private JTextField firstFunction;
	private JTextField secondFunction;
	private JTextField guess;
	private JTextField result;

	// Constructor.
	public Keyboard(JFrame frame, JTextField coordinate, JTextField firstFunction, JTextField secondFunction, JTextField guess, JTextField result) {
		this.frame = frame;
		this.coordinate = coordinate;
		this.firstFunction = firstFunction;
		this.secondFunction = secondFunction;
		this.guess = guess;
		this.result = result;
	}// End Keyboard().

	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
	}// End keyTyped().

	
	@Override
	public void keyPressed(KeyEvent event){
		// Moves the cursor for tracing to the right.
		if(event.getKeyCode() == KeyEvent.VK_RIGHT) {
			cursor += Constants.TRACE_STEP;
			if(cursor > GraphingCalculator.functions.get(0).getxValues().size() - 1) {
				cursor = GraphingCalculator.functions.get(0).getxValues().size() - 1;
			}
			drawTrace();
		}
		
		// Moves the cursor for tracing to the left.
		if(event.getKeyCode() == KeyEvent.VK_LEFT) {
			cursor -= Constants.TRACE_STEP;
			if(cursor < 0) {
				cursor = 0;
			}
			drawTrace();
		}
		
		// Changes which function the cursor is focused on by incrementing the function index by 1.
		if(event.getKeyCode() == KeyEvent.VK_UP) {
			++functionIndex;
			if(functionIndex > GraphingCalculator.functions.size() - 1) {
				functionIndex = 0;
			}
			Window.setPointerColor(GraphingCalculator.functions.get(functionIndex).getColor());
			drawTrace();
		}
		
		// Changes which function the cursor is focused on by decrementing the function index by 1.
		if(event.getKeyCode() == KeyEvent.VK_DOWN) {
			--functionIndex;
			if(functionIndex < 0) {
				functionIndex = GraphingCalculator.functions.size() - 1;
			}
			Window.setPointerColor(GraphingCalculator.functions.get(functionIndex).getColor());
			drawTrace();
		}
		
		// Used for if the user is searching for an intersection.
		if(event.getKeyCode() == KeyEvent.VK_ENTER && intersect) {
			// Gets the first function and draws the function expression on the screen.
			if(intersection[0] == null) {
				intersection[0] = GraphingCalculator.functions.get(functionIndex);
				firstFunction.setVisible(true);
				firstFunction.setFont(new Font("Century Gothic", Font.BOLD, 25));
				firstFunction.setForeground(((Function)intersection[0]).getColor());
				firstFunction.setFocusable(false);
				firstFunction.setOpaque(false);
				firstFunction.setBorder(BorderFactory.createEmptyBorder());
				firstFunction.setEditable(false);
				firstFunction.setText("y1 = " + ((Function)intersection[0]).getExpression());
			}
			
			// Gets the second function and draws the function expression on the screen.
			else if(intersection[1] == null && intersection[0] != GraphingCalculator.functions.get(functionIndex)) {
				intersection[1] = GraphingCalculator.functions.get(functionIndex);
				secondFunction.setVisible(true);
				secondFunction.setFont(new Font("Century Gothic", Font.BOLD, 25));
				secondFunction.setForeground(((Function)intersection[1]).getColor());
				secondFunction.setFocusable(false);
				secondFunction.setOpaque(false);
				secondFunction.setBorder(BorderFactory.createEmptyBorder());
				secondFunction.setEditable(false);
				secondFunction.setText("y2 = " + ((Function)intersection[1]).getExpression());
			}
			
			// Gets the current x value of the point to be used as an estimation and draws this number to the screen.
			else if(intersection[1] != null) {
				intersection[2] = GraphingCalculator.functions.get(functionIndex).getxValues().get(cursor);
				guess.setVisible(true);
				guess.setFont(new Font("Century Gothic", Font.BOLD, 25));
				guess.setFocusable(false);
				guess.setOpaque(false);
				guess.setBorder(BorderFactory.createEmptyBorder());
				guess.setEditable(false);
				guess.setText("Guess: x = " + GraphingCalculator.round(GraphingCalculator.functions.get(functionIndex).getxValues().get(cursor), 9));
				result.setVisible(true);
				result.setFont(new Font("Century Gothic", Font.BOLD, 25));
				result.setFocusable(false);
				result.setOpaque(false);
				result.setBorder(BorderFactory.createEmptyBorder());
				result.setEditable(false);
				
				// Creates a intersection pointer object to be drawn on the intersection.
				Double xValueOfResult = Function.newtonsMethod(((Function)intersection[0]).getExpression(), ((Function)intersection[1]).getExpression(), (double)intersection[2]);
				if(xValueOfResult != null) {
					result.setText("Intersection: (" + GraphingCalculator.round(xValueOfResult, 5) + ", " + GraphingCalculator.round(StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(((Function)intersection[0]).getExpression()), xValueOfResult), 5) + ")");	
					Window.setIntersectionPoint(new Ellipse2D.Double((Constants.xCentre + xValueOfResult * Constants.xIncrement) - 10, (Constants.yCentre + StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(((Function)intersection[0]).getExpression()), xValueOfResult) * -Constants.yIncrement) - 10, 20, 20));
					Window.setIntersectionOn(true);
					frame.repaint();
				}
				else {
					result.setText("Intersection: DNE");
				}
			}
		}
	}// End keyPressed().

	@Override
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
	}// End keyReleased().
	
	// Draws the trace cursor and text.
	private void drawTrace() {
		coordinate.setText("(" + GraphingCalculator.round(GraphingCalculator.functions.get(functionIndex).getxValues().get(cursor), 9) + ", " + GraphingCalculator.round(GraphingCalculator.functions.get(functionIndex).getyValues().get(cursor), 9) + ")");
		Window.setPointer(new Ellipse2D.Double((Constants.xCentre + GraphingCalculator.functions.get(functionIndex).getxValues().get(cursor) * Constants.xIncrement) - 10, (Constants.yCentre + -GraphingCalculator.functions.get(functionIndex).getyValues().get(cursor) * Constants.yIncrement) - 10, 20, 20));
		frame.repaint();
	}// End drawTrace().
	
	// Returns cursor.
	public static int getCursor() {
		return cursor;
	}// End getCursor().

	// Sets cursor to input int.
	public static void setCursor(int cursor) {
		Keyboard.cursor = cursor;
	}// End setCursor().

	// Returns functionIndex.
	public static int getFunctionIndex() {
		return functionIndex;
	}// End getFunctionIndex().

	// Sets functionIndex to input int.
	public static void setFunctionIndex(int functionIndex) {
		Keyboard.functionIndex = functionIndex;
	}// End setFunctionIndex().

	// Sets intersect to input boolean.
	public static void setIntersect(boolean intersect) {
		Keyboard.intersect = intersect;
	}// End setIntersect().

	// Returns intersection.
	public static Object[] getIntersection() {
		return intersection;
	}// End getIntersection.
	
	// Resets intersection.
	public static void resetIntersection() {
		intersection = new Object[3];
	}// End resetIntersection().
}// End Keyboard.

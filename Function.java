/**

* Function.java

* Contains information an methods for functions.

* @author Jacob Plourde

*/
package calculator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Function {
	private final String expression;
	
	private final List<Double> yValues = new ArrayList<Double>();
	private final List<Double> xValues = new ArrayList<Double>();

	private Color color;
	
	private boolean connectByLines = true;

	// Constructor.
	public Function(String expression, Color color) {
		this.expression = expression;
		this.color = color;
		
		// Converts the expression from infix notation to postfix notation.
		Queue<String> postfixQueue = StringEvaluation.InfixToPostfix(expression);
		
		// Loops through values of x to evaluate the postfix expression for y values.
		for(double i = Constants.X_MIN; i < Constants.X_MAX; i += Constants.X_RES * 0.005) {
			
			// Clones the postfix queue so that it isn't directly modified.
			Queue<String> placeholder = new LinkedList<>();
			Iterator<String> iterator = postfixQueue.iterator();
			while(iterator.hasNext())  {
			   placeholder.add(iterator.next());
			}
			
			// Add the values to lists.
			xValues.add(i);
			yValues.add(StringEvaluation.evaluate(placeholder, i));
		}
		
		// Toggles the ability to connect graph points by lines for functions that don't need them.
		if(postfixQueue.contains("ceil") || postfixQueue.contains("floor")) {
			connectByLines = false;
		}
		
	}// End Function().
	
	// Algorithm that calculates the point of intersection between input functions closest to an input estimation x value.
	public static Double newtonsMethod(String expression1, String expression2, double estimation) {	
		if(StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(expression1), estimation) == StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(expression2), estimation)) {
			return estimation;
		}
		
		String combinedExpression = expression1 + " - ( " + expression2 + " )";
		
		// Constant values to be used for calculation.
		double x0 = estimation;
		double tolerance = 0.0000001;
		double epsilon = 0.0000001;
		double maxIterations = 1000;
		
		// Iterates until the max iterations have been reached or an intersection is found.
		for(int i = 0; i < maxIterations; ++i) {
			double y = StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(combinedExpression), x0);
			double yPrime = (StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(combinedExpression), x0 + 0.001) - StringEvaluation.evaluate(StringEvaluation.InfixToPostfix(combinedExpression), x0 - 0.001)) / (x0 + 0.001 - (x0 - 0.001));
			
			// If the functions are the same.
			if(Math.abs(yPrime) < epsilon) {
				break;
			}
			
			// Simplifies towards an intersection.
			double x1 = x0 - y / yPrime;
			
			// If an intersection has been found.
			if(Math.abs(x1 - x0) <= tolerance) {
				return x1;
			}
			
			x0 = x1;
		}
		return null;
	}// End newtonsMethod().

	// Draws the functions to the JFrame.
	public void draw(Graphics2D graphics) {
		// Draws the input expression on the graph.
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(2));
				
		// Loops through the x and y values to draw them to the window as lines connecting points.
		for(int i = 0; i < xValues.size(); ++i) {
			// Gets two values for each dimension to make lines connecting consecutive points.
			double x1 = xValues.get(i);
			double x2 = xValues.get(i);
			if(i < xValues.size() - 1) {
				x2 = xValues.get(i + 1);
			}
			double y1 = yValues.get(i);
			double y2 = yValues.get(i);
			if(i < yValues.size() - 1) {
				y2 = yValues.get(i + 1);
			}
			
			// Current segment of the graph.
			Line2D graph;
			// Draws the line segment of the graph to the screen, while detecting asymptotes.
			if(!(y2 - Constants.yTotal < y1) || !(y2 + Constants.yTotal > y1) || !connectByLines) {
				graph = new Line2D.Double(Constants.xCentre + x1 * Constants.xIncrement, Constants.yCentre + -y1 * Constants.yIncrement, Constants.xCentre + x2 * Constants.xIncrement, Constants.yCentre + -y1 * Constants.yIncrement);
			}
			else {
				graph = new Line2D.Double(Constants.xCentre + x1 * Constants.xIncrement, Constants.yCentre + -y1 * Constants.yIncrement, Constants.xCentre + x2 * Constants.xIncrement, Constants.yCentre + -y2 * Constants.yIncrement);
			}
			graphics.draw(graph);
		}		
	}// End draw().
	
	// Returns color.
	public Color getColor() {
		return color;
	}// End getColor().

	// Returns yValues.
	public List<Double> getyValues() {
		return yValues;
	}// End getyValues().

	// Returns xValues.
	public List<Double> getxValues() {
		return xValues;
	}// End getxValues().

	// Sets connectByLines to input boolean.
	public void setConnectByLines(boolean connectByLines) {
		this.connectByLines = connectByLines;
	}// End setConnectByLines().

	// Returns expression.
	public String getExpression() {
		return expression;
	}// End getExpression().
}// End Function.

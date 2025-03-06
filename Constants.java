/**

 * Constants.java

 * Contains constants to be used for a graphing calculator.

 * @author Jacob Plourde

*/
package calculator;

import java.awt.Color;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

public class Constants {
	public static final int SCREEN_HEIGHT;
	public static final int SCREEN_WIDTH;
	public static final double X_MIN;
	public static final double X_MAX;
	public static final double Y_MIN;
	public static final double Y_MAX;
	public static final double X_RES;
	
	public static final double xTotal;
	public static final double xIncrement;
	public static final double xCentre;
	public static final double yTotal;
	public static final double yIncrement;
	public static final double yCentre;
	
	public static final double TRACE_STEP;
	
	public static final Queue<Color> colors = new LinkedList<>();
	
	public static final List<Operator> operatorData = new ArrayList<>();
	public static final List<String> functions = new ArrayList<>();
	public static final Hashtable<String, Double> constants = new Hashtable<String, Double>();
	public static final List<String> operators = new ArrayList<>();
	
	// Initializes all of the constants and constant dependant variables.
	static {
		Properties properties = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream("config.properties");
			properties.load(inputStream);
		}
		catch(Exception e) {}
		
		// Sets the calculator constants to the constants in the properties file.
		SCREEN_HEIGHT = Integer.parseInt(properties.getProperty("SCREEN_HEIGHT"));
		SCREEN_WIDTH = Integer.parseInt(properties.getProperty("SCREEN_WIDTH"));
		X_MIN = Double.parseDouble(properties.getProperty("X_MIN"));
		X_MAX = Double.parseDouble(properties.getProperty("X_MAX"));
		Y_MIN = Double.parseDouble(properties.getProperty("Y_MIN"));
		Y_MAX = Double.parseDouble(properties.getProperty("Y_MAX"));
		X_RES = Double.parseDouble(properties.getProperty("X_RES"));
		
		// Sets the constants for drawing the graphs.
		xTotal = Math.abs(X_MIN) + Math.abs(X_MAX);
		xIncrement = SCREEN_WIDTH / xTotal;
		xCentre = SCREEN_WIDTH - (X_MAX * xIncrement);
		yTotal = Math.abs(-Y_MIN) + Math.abs(-Y_MAX);
		yIncrement = SCREEN_HEIGHT / yTotal;
		yCentre = SCREEN_HEIGHT - (-Y_MIN * yIncrement);
		
		// Constant for tracing x-increment.
		TRACE_STEP = Double.parseDouble(properties.getProperty("TRACE_STEP"));
		
		// Operator definitions, with precedence and associativity.
		operatorData.add(new Operator("+", 2, "Left"));
		operatorData.add(new Operator("-", 2, "Left"));
		operatorData.add(new Operator("*", 3, "Left"));
		operatorData.add(new Operator("/", 3, "Left"));
		operatorData.add(new Operator("^", 4, "Right"));
		
		// Functions defined in the algorithm.
		functions.add("sin");
		functions.add("cos");
		functions.add("tan");
		functions.add("abs");
		functions.add("sqrt");
		functions.add("ln");
		functions.add("floor");
		functions.add("ceil");
		
		// Constants defined in the algorithm.
		constants.put("pi", Math.PI);
		constants.put("e", Math.E);
		
		// Operators defined in the algorithm.
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		operators.add("^");		
		
		fillColors();
	};// End static.
	
	// Initializes the color queue with it's starting colors.
	public static final void fillColors() {
		colors.clear();
		colors.add(new Color(0, 0, 255));
		colors.add(new Color(255, 0, 0));
		colors.add(new Color(189, 52, 205));
		colors.add(new Color(32, 133, 32));
		colors.add(new Color(230, 125, 24));
		colors.add(new Color(131, 48, 8));
	}// End fillColors().
	
	// Cycles the color queue.
	public static Color nextColor() {
		colors.add(colors.peek());
		return colors.remove();
	}// End nextColor().
}// End Constants.

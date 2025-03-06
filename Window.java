/**

 * Window.java

 * Class for a JPanel with a defined paintComponent().

 * @author Jacob Plourde

*/
package calculator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class Window extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static Ellipse2D pointer;
	private static boolean pointerOn = false;
	private static Color pointerColor = Color.BLUE;
	private static Ellipse2D intersectionPoint;
	private static boolean intersectionOn;

	// Constructor.
	public Window() {}
	
	// Paints necessary information to the JFrame, depending on the calculator state.
	@Override
    public void paintComponent(Graphics gp) {
		super.paintComponent(gp);
		Graphics2D graphics = (Graphics2D)gp;
		
		// Draws the axes to the screen, adjusting their location based on the scale of the axes.
		Line2D xAxis = new Line2D.Double(0, Constants.yCentre, Constants.SCREEN_WIDTH, Constants.yCentre);
		graphics.draw(xAxis);
		Line2D yAxis = new Line2D.Double(Constants.xCentre, 0, Constants.xCentre, Constants.SCREEN_HEIGHT);
		graphics.draw(yAxis);
		// Draws the unit marks on the axes.
		// Right of y-axis.
		for(double i = Constants.xCentre; i <= Constants.SCREEN_WIDTH; i += Constants.xIncrement) {
			Line2D xMarks = new Line2D.Double(i, Constants.yCentre - 4, i, Constants.yCentre + 4);
			graphics.draw(xMarks);
		}
		// Left of y-axis.
		for(double i = Constants.xCentre; i >= 0; i -= Constants.xIncrement) {
			Line2D xMarks = new Line2D.Double(i, Constants.yCentre - 4, i, Constants.yCentre + 4);
			graphics.draw(xMarks);
		}
		// Above x-axis.
		for(double i = Constants.yCentre; i <= Constants.SCREEN_HEIGHT; i += Constants.yIncrement) {
			Line2D yMarks = new Line2D.Double(Constants.xCentre - 4, i, Constants.xCentre + 4, i);
			graphics.draw(yMarks);
		}
		// Below x-axis.
		for(double i = Constants.yCentre; i >= 0; i -= Constants.yIncrement) {
			Line2D yMarks = new Line2D.Double(Constants.xCentre - 4, i, Constants.xCentre + 4, i);
			graphics.draw(yMarks);
		}
		
		// Draws all of the functions.
		for(Function function: GraphingCalculator.functions) {
			function.draw(graphics);
		}
		
		// Sets the pointer color if it is active and draws it.
		if(pointerOn) {
			graphics.setColor(pointerColor);
			graphics.fill(pointer);	
		}
		
		//  Draws the intersection pointer.
		if(intersectionOn) {
			graphics.setColor(Color.BLACK);
			graphics.fill(intersectionPoint);
		}
    }// End paintComponent().

	// Sets pointer to input Ellipse2D.
	public static void setPointer(Ellipse2D pointer) {
		Window.pointer = pointer;
	}// End setPointer().

	// Sets pointerOn to input boolean.
	public static void setPointerOn(boolean pointerOn) {
		Window.pointerOn = pointerOn;
	}// End setPointerOn().

	// Sets pointerColor to input color.
	public static void setPointerColor(Color pointerColor) {
		Window.pointerColor = pointerColor;
	}// End setPointerColor().
	
	// Sets intersectionPoint to input Ellipse2D.
	public static void setIntersectionPoint(Ellipse2D intersectionPoint) {
		Window.intersectionPoint = intersectionPoint;
	}// End setIntersectionPoint().
	
	// Sets intersectionOn to input boolean.
	public static void setIntersectionOn(boolean intersectionOn) {
		Window.intersectionOn = intersectionOn;
	}// End setIntersectionOn().
}// End Window.

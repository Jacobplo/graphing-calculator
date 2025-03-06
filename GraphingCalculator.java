/**

 * GraphingCalculator.java

 * The main file for a graphing calculator.

 * @author Jacob Plourde

*/
package calculator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GraphingCalculator {
	public static List<Function> functions = new ArrayList<Function>();
	
	public static Boolean buttonsHidden = false;

	public static void main(String[] args) throws Exception {
		
		// Creates and displays the window for the graphing calculator.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Graphing Calculator");
		frame.setResizable(false);
		frame.setVisible(false);
		frame.setSize(Constants.SCREEN_WIDTH + 17, Constants.SCREEN_HEIGHT + 39); // Numbers are added to account for the scaling with a decorated frame.
		
		// Panel initialization;
		JPanel panel = new Window();
		panel.setLayout(null);
		frame.add(panel);
		
		// Creates a list for buttons so that they can all be hidden or shown.
		List<JButton> buttons = new ArrayList<JButton>();
		// Creates a list for other components so that they can be removed if the exit button is pressed.
		List<Component> components = new ArrayList<Component>();
		
		// Initilization of trace and intersection text fields so that they can be passed into the keyListener.
		JTextField coordinate = new TextField(panel, 0, -15, Constants.SCREEN_WIDTH, 50);
		JTextField firstFunction = new TextField(panel, 0, 25, Constants.SCREEN_WIDTH, 30);
		JTextField secondFunction = new TextField(panel, 0, 50, Constants.SCREEN_WIDTH, 30);
		JTextField guess = new TextField(panel, 0, 75, Constants.SCREEN_WIDTH, 30);
		JTextField result = new TextField(panel, 0, 100, Constants.SCREEN_WIDTH, 30);
		coordinate.setVisible(false);
		firstFunction.setVisible(false);
		secondFunction.setVisible(false);
		guess.setVisible(false);
		result.setVisible(false);
		KeyListener keyListener = new Keyboard(frame, coordinate, firstFunction, secondFunction, guess, result);
		
		// Creates necessary buttons. Adds them to the buttons list.
		JButton addFunction = new Button(panel, 0, Constants.SCREEN_HEIGHT - 50, 50, 50, "addFunction.png");
		buttons.add(addFunction);
		JButton clearLatestFunction = new Button(panel, 50, Constants.SCREEN_HEIGHT - 50, 50, 50, "clearLatestFunction.png");
		buttons.add(clearLatestFunction);
		JButton clearFunctions = new Button(panel, 100, Constants.SCREEN_HEIGHT - 50, 50, 50, "clearFunctions.png");
		buttons.add(clearFunctions);
		JButton traceCurve = new Button(panel, 150, Constants.SCREEN_HEIGHT - 50, 50, 50, "trace.png");
		buttons.add(traceCurve);
		JButton getIntersect = new Button(panel, 200, Constants.SCREEN_HEIGHT - 50, 50, 50, "intersect.png");
		buttons.add(getIntersect);
			
		// addFunction Button.
		// Opens the text field prompt to get an expression from the user. 
		addFunction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleButtons(buttons);
				JTextField input = new TextField(panel, 0, 0, Constants.SCREEN_WIDTH, 50);
				input.requestFocusInWindow();
				components.add(input);
				input.setText("y = ");
				input.setNavigationFilter(new NavigationFilterPrefixWithBackspace(4, input));
				input.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							String expression = getExpression(input.getText());
							if(!(expression == null)) {
								Function function = new Function(expression, Constants.nextColor());
								functions.add(function);
								frame.repaint();
								panel.remove(input);
								toggleButtons(buttons);
							}
						}
					}
				});
			}
		});
		
		// clearFunctions Button.
		// Clears all functions.
		clearFunctions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				functions = new ArrayList<Function>();
				Constants.fillColors();
				frame.repaint();
			}
		});
		
		// clearLatestFunction Button.
		// Clears the latest function.
		clearLatestFunction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!functions.isEmpty()) {
					functions.remove(functions.size() - 1);
					Constants.fillColors();
					for(int i = 0; i < functions.size(); ++i) {
						Constants.nextColor();
					}
					frame.repaint();					
				}
			}
		});		
		
		// exit Button (seperate from other buttons).
		JButton exit = new Button(panel, Constants.SCREEN_WIDTH - 50, Constants.SCREEN_HEIGHT - 50, 50, 50, "exit.png");
		// Exits either current screen that is open, or elsewise closes the program.
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(buttonsHidden) {
					for(int i = 0; i < components.size(); ++i) {
						panel.remove(components.get(i));
					}
					for(int i = 0; i < components.size(); ++i) {
						components.remove(components.get(i));
					}
					firstFunction.setVisible(false);
					secondFunction.setVisible(false);
					guess.setVisible(false);
					result.setVisible(false);
					Keyboard.resetIntersection();
					coordinate.setVisible(false);
					frame.removeKeyListener(keyListener);
					toggleButtons(buttons);
					Window.setPointerOn(false);
					Window.setIntersectionOn(false);
					Keyboard.setIntersect(false);
					frame.repaint();
				}
				else {
					frame.dispose();
				}
			}
		});
		
		// traceCurve button.
		traceCurve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sets all conditions 
				if(functions.isEmpty()) return;
				coordinate.setVisible(true);
				frame.addKeyListener(keyListener);
				frame.setFocusable(true);
				toggleButtons(buttons);
				Window.setPointerOn(true);
				Window.setPointerColor(Color.BLUE);
                Keyboard.setCursor(functions.get(0).getxValues().size() / 2);
                Keyboard.setFunctionIndex(0);
                Window.setPointer(new Ellipse2D.Double((Constants.xCentre + functions.get(Keyboard.getFunctionIndex()).getxValues().get(Keyboard.getCursor()) * Constants.xIncrement) - 10, (Constants.yCentre + -functions.get(Keyboard.getFunctionIndex()).getyValues().get(Keyboard.getCursor()) * Constants.yIncrement) - 10, 20, 20));
                coordinate.setFont(new Font("Century Gothic", Font.BOLD, 25));
                coordinate.setFocusable(false);
                coordinate.setOpaque(false);
                coordinate.setBorder(BorderFactory.createEmptyBorder());
                coordinate.setEditable(false);
                coordinate.setText("(" + round(functions.get(Keyboard.getFunctionIndex()).getxValues().get(Keyboard.getCursor()), 9) + ", " + round(functions.get(Keyboard.getFunctionIndex()).getyValues().get(Keyboard.getCursor()), 9) + ")");
                frame.repaint();
			}
		});
		
		// getIntersect button.
		getIntersect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sets all conditions.
				if(functions.isEmpty()) return;
				coordinate.setVisible(true);
				frame.addKeyListener(keyListener);
				frame.setFocusable(true);
				toggleButtons(buttons);
				Window.setPointerOn(true);
				Window.setPointerColor(Color.BLUE);
                Keyboard.setCursor(functions.get(0).getxValues().size() / 2);
                Keyboard.setFunctionIndex(0);
                Keyboard.setIntersect(true);
                Window.setPointer(new Ellipse2D.Double((Constants.xCentre + functions.get(Keyboard.getFunctionIndex()).getxValues().get(Keyboard.getCursor()) * Constants.xIncrement) - 10, (Constants.yCentre + -functions.get(Keyboard.getFunctionIndex()).getyValues().get(Keyboard.getCursor()) * Constants.yIncrement) - 10, 20, 20));
                coordinate.setFont(new Font("Century Gothic", Font.BOLD, 25));
                coordinate.setFocusable(false);
                coordinate.setOpaque(false);
                coordinate.setBorder(BorderFactory.createEmptyBorder());
                coordinate.setEditable(false);
                coordinate.setText("(" + round(functions.get(Keyboard.getFunctionIndex()).getxValues().get(Keyboard.getCursor()), 9) + ", " + round(functions.get(Keyboard.getFunctionIndex()).getyValues().get(Keyboard.getCursor()), 9) + ")");
                frame.repaint();
			}
		});

		frame.setVisible(true);
		
//		double i = 0;
//		while(true) {
//			Function test = new Function("abs ( sin ( tan ( x * sin ( " + i + " ) ) ) ) * sin ( x / sin ( " + i + " ) ) * x * " + i , Constants.nextColor());
//			functions.add(test);
//			if(functions.size() > 50) {
//				functions.remove(0);
//			}
//			i += 0.01;
//			frame.repaint();
//		}
	}// End main().

	// Converts an input string expression, in the form of "y = (expression)", to postfix notation, before returning.
	public static String getExpression(String inputText) {
		try {
			String expression = inputText.substring(4);
			Queue<String> postfixQueue = StringEvaluation.InfixToPostfix(expression);
			@SuppressWarnings("unused")
			double checkIfValid = StringEvaluation.evaluate(postfixQueue, JFrame.ABORT);
			return expression;
		}
		// Catches invalid expressions.
		catch(Exception e) {
			System.out.println("\nERROR: SYNTAX");
			System.out.println("Check all arguments entered.\n");
		}
		return null;
	}// End getExpression();
	
	// Toggles all buttons visibility in a button list.
	public static void toggleButtons(List<JButton> buttons) {
		for(JButton button: buttons) {
			button.setVisible(buttonsHidden);
		}
		buttonsHidden ^= true;
	}// End hideButtons().

	// Rounds an input double to an input number of decimal places.
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}// End round().
}// End GraphingCalculator.

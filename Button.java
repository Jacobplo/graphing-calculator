/**

 * Button.java

 * Button object that creates bounds, disables border, and applies an image based on the constructor.

 * @author Jacob Plourde

*/
package calculator;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;
	
	// Constructor.
	public Button(JPanel panel, int x, int y, int width, int height, String iconPath) throws Exception {
		panel.add(this);
		setBounds(x, y, width, height);
		setBorderPainted(false);
		Image icon = ImageIO.read(new File("resources" + File.separator + iconPath)).getScaledInstance(width, height, Image.SCALE_DEFAULT);
		setIcon(new ImageIcon(icon));
	}// End Button().
}// End Button.

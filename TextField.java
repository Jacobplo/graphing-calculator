/**

 * TextField.java

 * Text field class that sets bounds using a constructor. Includes a navigation filter for use with the graphing calculator.

 * @author Jacob Plourde

*/
package calculator;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;

public class TextField extends JTextField {
	private static final long serialVersionUID = 1L;

	// Constructor.
	public TextField(JPanel panel, int x, int y, int width, int height) {
		panel.add(this);
		setBounds(x, y, width, height);
	}// End TextField().
}// End TextField.


// Filter from the internet.
class NavigationFilterPrefixWithBackspace extends NavigationFilter {
    private int prefixLength;
    private Action deletePrevious;

    public NavigationFilterPrefixWithBackspace(int prefixLength, JTextComponent component) {
        this.prefixLength = prefixLength;
        deletePrevious = component.getActionMap().get("delete-previous");
        component.getActionMap().put("delete-previous", new BackspaceAction());
        component.setCaretPosition(prefixLength);
    }

    @Override
    public void setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
        fb.setDot(Math.max(dot, prefixLength), bias);
    }

    @Override
    public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
        fb.moveDot(Math.max(dot, prefixLength), bias);
    }

    class BackspaceAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent component = (JTextComponent)e.getSource();

            if (component.getCaretPosition() > prefixLength) {
                deletePrevious.actionPerformed( null );
            }
        }
    }// End BackspaceAction.
}// End NavigationFilterPrefixWithBackspace.

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 */

/**
 * @author Dakota Owen
 *
 */
public class CommandPanel extends JPanel {
	
	private static final int FONT_SIZE = 12;
	private static final String FONT_TYPE = "Times New Roman"; 
	
	private JTextField commandField = new JTextField(); 
	private LinkedList<String> commandBuffer = new LinkedList<String>();
	private String string;
	
	
	CommandPanel () {
		class AddActionListener implements ActionListener {
			   public void actionPerformed(ActionEvent event)	{
				   synchronized (commandBuffer) {
					   commandBuffer.add(commandField.getText());
					   commandField.setText("");
					   commandBuffer.notify();
				   }
		           return;
			   }
		   }
		ActionListener listener = new AddActionListener();
		commandField.addActionListener(listener);
		commandField.setFont(new Font(FONT_TYPE, Font.PLAIN, FONT_SIZE));
		setLayout(new BorderLayout());
		add(commandField, BorderLayout.CENTER);
	}

	
	public void inputString() {
		synchronized (commandBuffer) {
			while (commandBuffer.isEmpty()) {
				try {
					commandBuffer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			string = commandBuffer.pop();
		}
		return;
	}
	
	
	public String getString() {
		return string;
	}
}
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class InfoPanel extends JPanel { 
	
	private static final int TEXT_AREA_HEIGHT = 40;
	private static final int CHARACTER_WIDTH = 39;
	private static final int FONT_SIZE = 12;
	private static final String FONT_TYPE = "Times New Roman"; 

	JTextArea textArea = new JTextArea(TEXT_AREA_HEIGHT, CHARACTER_WIDTH);
	JScrollPane scrollPane = new JScrollPane(textArea);
	DefaultCaret caret = (DefaultCaret)textArea.getCaret();
	
	//UI Preset messages. 
	private static final String[] messages = {
			"The command you entered is invalid.", //0
			"You have invoked a cheat.",//1
			"Doubling is illegal at this time."//2
	};
	
	InfoPanel () {
		textArea.setEditable(false);
		textArea.setFont(new Font(FONT_TYPE, Font.PLAIN, FONT_SIZE));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void displayString (String text) {
		textArea.setText(textArea.getText()+"\n"+text);
	}

	public void displayMessage (int i) {
		//Need to check for out of bounds
		textArea.setText(textArea.getText()+"\n"+messages[i]); 
	}
	
	public void clearTextArea() {
		textArea.setText("\n"); 
	}
}
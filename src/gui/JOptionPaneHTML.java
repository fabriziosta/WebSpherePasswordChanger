package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class JOptionPaneHTML extends JOptionPane{
	private static final long serialVersionUID = 1L;
	private static final String apriBodyDefault = "<html><body style='width: 250px;'><p>";
	private static final String chiudiBody = "</p></body></html>";
	
	public JOptionPaneHTML() {
		super();
	}
	
	public JOptionPaneHTML(JFrame parent, String messaggio, String titolo, Integer tipologiaFinestra) {
		JOptionPane.showConfirmDialog(parent, apriBodyDefault + messaggio + chiudiBody, titolo, JOptionPane.CLOSED_OPTION);
	}
	
	public JOptionPaneHTML(JFrame parent, String messaggio, String titolo, Integer tipologiaFinestra, Integer sizeWindow) {
		JOptionPane.showConfirmDialog(parent, "<html><body style='width: "+ sizeWindow + "px;'><p>" + messaggio + chiudiBody, titolo, JOptionPane.CLOSED_OPTION);
	}
	
	//JOptionPane con JTextField
	public String suggerisciPercorso(JFrame parent,  String titolo, Integer sizeWindow) {
		JTextField JTF = new JTextField();
		switch(JOptionPane.showConfirmDialog(parent, JTF, titolo, JOptionPane.CANCEL_OPTION)) {
			case JOptionPane.OK_OPTION:
				return JTF.getText();
		}
		return "";
	}
}

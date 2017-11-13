package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.PasswordChanger;
import utils.FolderExplorer;
import utils.Profilo;

public class MainScreen implements ItemListener{

	private JFrame frame;
	private JTextField txtPassword;
	private JButton btnPassword;
	private JRadioButton rdbVersion;
	private JRadioButton rdbVersion2;
	private JPanel pnlCheck1;
	private JPanel pnlCheck2;
	
	private String selectedVersion;
	private FolderExplorer folderExplorer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainScreen() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainScreen.class.getResource("/img/key_20x20.png")));
		frame.setTitle("WebSphere Password Changer v1.0");
		frame.setBounds(100, 100, 400, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setResizable(false);
		
		JPanel root = new JPanel();
		frame.getContentPane().add(root, BorderLayout.CENTER);
		root.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_center = new JPanel();
		root.add(panel_center, BorderLayout.CENTER);
		panel_center.setLayout(new BorderLayout(0, 0));
		
		JPanel sub_panel_sud = new JPanel();
		panel_center.add(sub_panel_sud, BorderLayout.SOUTH);
		sub_panel_sud.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		sub_panel_sud.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);
		separator.setSize(frame.getWidth(), 50);
		
		txtPassword = new JTextField();
		panel_1.add(txtPassword, BorderLayout.CENTER);
		txtPassword.setEnabled(false);
		txtPassword.setToolTipText("Scrivi la tua nuova password");
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setColumns(10);
		
		btnPassword = new JButton("Cambia Password");
		panel_1.add(btnPassword, BorderLayout.EAST);
		btnPassword.setIcon(new ImageIcon(MainScreen.class.getResource("/img/password_20x20.png")));
		btnPassword.setEnabled(false);
		btnPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(txtPassword.getText().trim().equals("") || !txtPassword.getText().matches("[a-zA-Z0-9]{5,30}")){
					JOptionPane.showConfirmDialog(frame, "Inserire una password valida (5 < lunghezza < 30, no caratteri speciali)", "Error", JOptionPane.CLOSED_OPTION);
				}else if(!controllaCheckBox()) {
					JOptionPane.showConfirmDialog(frame, "Selezionare almeno un profilo da aggiornare!", "Error", JOptionPane.CLOSED_OPTION);
				}else {
					int result = JOptionPane.showConfirmDialog(frame, "Sei sicuro?", "Conferma Operazione", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						//1. Inizia fase di creazione del fullPath e del cambio password
						PasswordChanger changer = new PasswordChanger(folderExplorer.getProfilesList(),selectedVersion, txtPassword.getText().trim());
						
						//2.Mostra i log
						String HTML_logs = "";
						for(String singleLog : changer.getLog()) {
							HTML_logs += singleLog;
						}
						new JOptionPaneHTML(frame, HTML_logs, "Risultato delle operazioni", JOptionPane.CLOSED_OPTION, 350);
						
						//3.Salva i log appendendo in un file, con data e ora
						try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("PasswordChangerLogFile.txt", true)))){
							    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
							    LocalDateTime now = LocalDateTime.now();
							    out.println(dtf.format(now) + "  --  Matricola Telepass: " + changer.getUserId());
							    out.println("----------------------------------------------------------");
							    
							    HTML_logs = HTML_logs.replaceAll("\\<li\\>", " - ");
							    HTML_logs = HTML_logs.replaceAll("\\<\\/li\\>", "");
							    String[] listaOperazioni = HTML_logs.split(" - ");
							    for(int x=0; x < listaOperazioni.length; x++) {
							    	out.println(listaOperazioni[x]);
							    }
							    out.println("");
							    out.println("----------------------------------------------------------");
						}catch (IOException ex) { ex.printStackTrace(); }
						
						//4.Termina il programma
						frame.dispose();
					}
				}
			}
		});
		btnPassword.setToolTipText("Genera nuova password");
		
		JLabel lblPassword = new JLabel("   Nuova password:  ");
		panel_1.add(lblPassword, BorderLayout.WEST);
		
		JSeparator separator_1 = new JSeparator();
		panel_1.add(separator_1, BorderLayout.SOUTH);
		
		JPanel pnlCheckBox = new JPanel();
		panel_center.add(pnlCheckBox, BorderLayout.CENTER);
		pnlCheckBox.setLayout(new GridLayout(1, 0));
		
		pnlCheck1 = new JPanel();
		pnlCheckBox.add(pnlCheck1);
		pnlCheck1.setLayout(new GridLayout(0, 1));
		
		pnlCheck2 = new JPanel();
		pnlCheckBox.add(pnlCheck2);
		pnlCheck2.setLayout(new GridLayout(0, 1));
		
		JPanel panel_sud = new JPanel();
		root.add(panel_sud, BorderLayout.SOUTH);
		panel_sud.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCreatedByFabrizio = new JLabel("Created by Fabrizio Asta - fabrizio.asta@ibm.com    ");
		panel_sud.add(lblCreatedByFabrizio, BorderLayout.EAST);
		lblCreatedByFabrizio.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setIcon(new ImageIcon(MainScreen.class.getResource("/img/help_20x20.png")));
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String message = "<>Questa applicazione è stata creata per automatizzare la procedura di cambio password per la versione WebSphere 7.0.</li>"
								+ "<li>Le prossime versioni prevederanno anche l'aggiornamento alla v8.5"
								+ "<li>Per usare l'applicazione è sufficiente selezionare la versione del WAS che si sta usando"
								+ " e spuntare tutti i profili in cui si vuole aggiornare la propria password.</li>"
								+ "<li> Non abusare del programma: va a modificare delicati file di configurazione del WAS."
								+ " Declino ogni responsabilità per un uso improprio del tool.</li>";
				new JOptionPaneHTML(frame, message, "HELP", JOptionPane.CLOSED_OPTION);
			}
		});

		btnHelp.setHorizontalAlignment(SwingConstants.LEFT);
		btnHelp.setToolTipText("Help button");
		panel_sud.add(btnHelp, BorderLayout.WEST);
		
		JPanel panel_nord = new JPanel();
		root.add(panel_nord, BorderLayout.NORTH);
		panel_nord.setLayout(new BorderLayout(0, 0));
		
		JLabel lblProfili = new JLabel("Selezionare i profili di cui si vuole aggiornare la password:");
		panel_nord.add(lblProfili, BorderLayout.SOUTH);
		lblProfili.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblVersion = new JLabel("Selezionare versione WebSphere Application Server:");
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		panel_nord.add(lblVersion, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel_nord.add(panel, BorderLayout.CENTER);
		
		rdbVersion = new JRadioButton("v7 (base_v7)");
		rdbVersion.setSelected(false);
		panel.add(rdbVersion);
		
		rdbVersion2 = new JRadioButton("v8.5 (base_v85)");
		rdbVersion2.setEnabled(false);
		panel.add(rdbVersion2);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbVersion);
		buttonGroup.add(rdbVersion2);
		rdbVersion.addItemListener(this);
		rdbVersion2.addItemListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		int state = e.getStateChange();
		if(state == ItemEvent.SELECTED && e.getSource() == rdbVersion) {
			selectedVersion = "base_v7";
			folderExplorer = new FolderExplorer(selectedVersion, this);
		}else if(state == ItemEvent.SELECTED && e.getSource() == rdbVersion2) {
			selectedVersion = "base_v85";
			folderExplorer = new FolderExplorer(selectedVersion, this);
		}
	}
	
	private boolean controllaCheckBox() {
		boolean almenoUnaTrovata = false;
		ArrayList<Profilo> listaProfili = folderExplorer.getProfilesList();
	
		for(Profilo profiloSingolo : listaProfili) {
			if(profiloSingolo.getCheckboxAssociataProfilo().isSelected()) {
				almenoUnaTrovata = true;
				break;
			}
		}
		return almenoUnaTrovata;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	public JPanel getPnlCheck1() {
		return pnlCheck1;
	}
	public JPanel getPnlCheck2() {
		return pnlCheck2;
	}
	public JTextField getTxtPassword() {
		return txtPassword;
	}
	public JButton getBtnPassword() {
		return btnPassword;
	}
}

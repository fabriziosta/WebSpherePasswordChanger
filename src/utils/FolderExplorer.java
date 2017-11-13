package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gui.JOptionPaneHTML;
import gui.MainScreen;

public class FolderExplorer {
	private MainScreen mainScreen;
	private ArrayList<Profilo> profilesList = new ArrayList<Profilo>();
	private Integer profileCounter = 1;

	public FolderExplorer(String selectedVersion, MainScreen mainScreen) {
		this.mainScreen = mainScreen;
		Path was86path = Paths.get(CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_86 + CodificaPercorsi.RUNTIMES + selectedVersion + CodificaPercorsi.PROFILES);
		Path was64path = Paths.get(CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_64 + CodificaPercorsi.RUNTIMES + selectedVersion + CodificaPercorsi.PROFILES);
		String avviso = "";
		try {
			if (Files.exists(was86path) && Files.exists(was64path) ) {
				avviso = "Sono state trovate due installazioni nel tuo PC. <br>" +
							CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_64 + CodificaPercorsi.RUNTIMES + "<br>" +
							CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_86 + CodificaPercorsi.RUNTIMES;
				new JOptionPaneHTML(mainScreen.getFrame(), avviso, "Avviso", JOptionPane.CLOSED_OPTION);
				loadProfiles(was86path, selectedVersion, "86");
				loadProfiles(was64path, selectedVersion, "64");
			    activatePasswordButton();
			}
			else if (Files.exists(was86path)) {
				avviso = "È stata trovata un'installazione a 32bit nel tuo PC. <br>" + CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_86 + CodificaPercorsi.RUNTIMES;
				new JOptionPaneHTML(mainScreen.getFrame(), avviso, "Avviso", JOptionPane.CLOSED_OPTION);
				loadProfiles(was86path, selectedVersion, "86");
				activatePasswordButton();
			}
			else if (Files.exists(was64path) ) {
				avviso = "È stata trovata un'installazione a 64bit nel tuo PC. <br> " + CodificaPercorsi.ROOT + CodificaPercorsi.INSTALLATION_TYPE_64 + CodificaPercorsi.RUNTIMES;
				new JOptionPaneHTML(mainScreen.getFrame(), avviso, "Avviso", JOptionPane.CLOSED_OPTION);
			    loadProfiles(was64path, selectedVersion, "64");
			    activatePasswordButton();
			}else{
				avviso = "Non è stata trovata nessuna installazione x86/x64 nel tuo PC. <br>" +
						"Aiutami a trovare un percorso valido per i tuoi profili! <br>" +
						"Io ho cercato in questi posti: <li>" + was64path + "</li><li>" + was86path + "</li>";
				new JOptionPaneHTML(mainScreen.getFrame(), avviso, "Avviso", JOptionPane.CLOSED_OPTION, 350);
				JOptionPaneHTML optionPaneHTML = new JOptionPaneHTML();
				String stringPathUtente = optionPaneHTML.suggerisciPercorso(mainScreen.getFrame(), "Suggeriscimi un percorso!", 400);
				Path pathUtente = Paths.get(stringPathUtente);
				
				if(pathUtente.getFileName().toString().equals("profiles")) {
					if(pathUtente.toString().contains("x86"))
						loadProfiles(pathUtente, selectedVersion, "86");
					else
						loadProfiles(pathUtente, selectedVersion, "64");
					activatePasswordButton();
				}else { 
					new JOptionPaneHTML(mainScreen.getFrame(), "Nessuna directory 'profiles' trovata in questa posizione. <br> Il programma verrà terminato.", "Avviso", JOptionPane.CLOSED_OPTION);
					mainScreen.getFrame().dispose();
				}
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	//Carica e salva le checkbox e i profili negli ArrayList
	private void loadProfiles(Path path, String version, String installationType){
		for (File file : new File(path.toString()).listFiles()) {
			if(file.isDirectory()) {
				profilesList.add(new Profilo(version, new JCheckBox(file.getName()), file.getName(), installationType));
				
				if(profileCounter % 2 == 1) //se dispari
					mainScreen.getPnlCheck1().add(profilesList.get(profilesList.size()-1).getCheckboxAssociataProfilo());
				else //se pari
					mainScreen.getPnlCheck2().add(profilesList.get(profilesList.size()-1).getCheckboxAssociataProfilo());
				profileCounter++;
			}
		}
		mainScreen.getFrame().validate();
		mainScreen.getFrame().repaint();
	}
	
	private void activatePasswordButton() {
		this.mainScreen.getTxtPassword().setEnabled(true);
		this.mainScreen.getBtnPassword().setEnabled(true);
	}

	public ArrayList<Profilo> getProfilesList() {
		return profilesList;
	}

}

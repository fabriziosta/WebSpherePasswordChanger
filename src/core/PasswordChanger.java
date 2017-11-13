package core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CodificaPercorsi;
import utils.Profilo;

public class PasswordChanger {
	 private ArrayList<String> log = new ArrayList<String>();
	 private String pswd;
	 private String userId = "Undefined";
	 
	 public PasswordChanger(ArrayList<Profilo> listaProfili, String selectedVersion, String password){
		 String path = "";
		 Boolean risultatoSostituzionePassword = false;
		 this.pswd = password;
		 
		 for(Profilo profiloSingolo : listaProfili) {
			 risultatoSostituzionePassword = false;
			 
			 if(profiloSingolo.getCheckboxAssociataProfilo().isSelected()) {
				 int index = listaProfili.indexOf(profiloSingolo);
				 
				 if(profiloSingolo.getTipoInstallazione().equals("64"))
					 path = createFullPath(listaProfili.get(index), CodificaPercorsi.INSTALLATION_TYPE_64);
				 else
					 path = createFullPath(listaProfili.get(index), CodificaPercorsi.INSTALLATION_TYPE_86);
				 
				 if(path.equals("")) {
					 log.add(" <li> Il profilo '" + listaProfili.get(index).getNomeProfilo() + "' non può essere aggiornato perchè c'è un problema con le cells </li>");
				 }else if(!new File(path).exists())
					 log.add(" <li> Non è stato trovato un security.xml per il profilo '" + listaProfili.get(index).getNomeProfilo() + "' </li>");
				 else {	
					 
					 risultatoSostituzionePassword = patternFinder(path);
					 
					 if(risultatoSostituzionePassword != null && risultatoSostituzionePassword == true)
						 log.add(" <li> Password sostituita con successo per '"+ listaProfili.get(index).getNomeProfilo() + "' </li>");
					 else if(risultatoSostituzionePassword != null && risultatoSostituzionePassword == false)
						 log.add(" <li> Non è stato possibile aggiornare la password di '"+ listaProfili.get(index).getNomeProfilo() + "' perchè non è stata trovata nessuna occorrenza valida dentro il file security.xml </li>");
					 else //ris == null
						 log.add(" <li> Non ho sufficienti permessi per sovrascrivere il profilo '"+ listaProfili.get(index).getNomeProfilo() +"'. <br>Riavviare il programma con privilegi da amministratore. </li>");
				 }
			 }
		 }
	 }
	 
	 private String createFullPath(Profilo profilo, String installationType){
		 String fullPath = CodificaPercorsi.ROOT + installationType + CodificaPercorsi.RUNTIMES + 
				 profilo.getVersioneWAS() + CodificaPercorsi.PROFILES + profilo.getNomeProfilo() + CodificaPercorsi.CONFIG_CELLS;
		 
		 String cellName = "";
		 File[] cellsList = new File(fullPath).listFiles();
		 if(cellsList != null && cellsList.length == 1 && cellsList[0].isDirectory()) {
			 cellName = cellsList[0].getName();
			 fullPath += cellName + CodificaPercorsi.FILENAME;
		 }else //se ci sono più celle || non ce ne sono || non ci sono cartelle: non so come gestirlo quindi ritorno "" e aggiorno log
			 fullPath = "";
		 return fullPath;
	 }
	 
	 private Boolean patternFinder(String fullPath){
		 Path path = Paths.get(fullPath);
		 String content = "";
		 String patternDB2User = " alias\\=\"localNodeETx/DB2User\" userId\\=\"[0-9]{8}\" password\\=\"[a-zA-Z0-9.-=_{}]{1,}\" description";
		 String patternJMSUser = " alias\\=\"localNodeETx/JMSUser\" userId\\=\"[0-9]{8}\" password\\=\"[a-zA-Z0-9.-=_{}]{1,}\" description";
		 String patternUserId = " alias\\=\"localNodeETx/DB2User\" userId\\=\"[0-9]{8}\"";
		 try {
			 content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			 
			 if(Pattern.compile(patternUserId).matcher(content).find()  && Pattern.compile(patternJMSUser).matcher(content).find() && Pattern.compile(patternDB2User).matcher(content).find()) {
				//2.Get the userId before replacing it
				 Matcher matcher = Pattern.compile(patternUserId).matcher(content);
				 matcher.find();
				 userId = matcher.group();
				 userId = userId.substring(userId.length() - 9, userId.length() - 1);
				 
				 //3.Replace password for localNodeETx/DB2User
				 content = content.replaceAll(patternDB2User, " alias\\=\"localNodeETx/DB2User\" userId\\=\"" + userId + "\" password\\=\"" + pswd + "\" description");
				 
				 //4.Replace password for localNodeETx/JMSUser
				 content = content.replaceAll(patternJMSUser, " alias\\=\"localNodeETx/JMSUser\" userId\\=\"" + userId + "\" password\\=\"" + pswd + "\" description");
				 Files.write(path, content.getBytes(StandardCharsets.UTF_8));
			 }else //se non trovi nessuna occurrences di userId, localNodeETx/DB2User o localNodeETx/JMSUser allora salvami un messaggio nei log
				 return false;
		 } catch (AccessDeniedException ADE) {
			 return null;
		 } catch (IOException e) {
			 e.printStackTrace(); 
		 }
		 return true;
	 }
	 
	 public ArrayList<String> getLog() {return log;}

	 public String getUserId() {
		return userId;
	 }
	 
}
